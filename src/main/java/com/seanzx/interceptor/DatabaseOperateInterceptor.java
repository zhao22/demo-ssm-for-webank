package com.seanzx.interceptor;

import com.seanzx.common.SystemLogQueue;
import com.seanzx.po.DatabaseOperatePO;
import com.seanzx.po.HttpRequestPO;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 * 拦截MyBatis，记录SQL执行日志
 * @author zhaoxin
 * @date 2020/10/21
 */
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
})
@Component
public class DatabaseOperateInterceptor implements Interceptor {

    @Autowired
    private HttpRequestInterceptor httpRequestInterceptor;

    private static final Logger logger = LoggerFactory.getLogger(DatabaseOperateInterceptor.class);

    /**
     * 不保存 http请求日志表和数据库日志表的操作日志
     * @param parameter Mapper方法 参数
     * @return
     */
    private boolean stopExecute(Object parameter) {
        return parameter instanceof HttpRequestPO || parameter instanceof DatabaseOperatePO;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] objects = invocation.getArgs();
        Object mappedStatementObject = objects[0];
        Object parameterObject = objects[1];
        if (stopExecute(parameterObject)) {
            return invocation.proceed();
        }
        try {
            if (mappedStatementObject instanceof MappedStatement) {
                // 1. 获取请求信息
                MappedStatement mappedStatement = (MappedStatement) mappedStatementObject;
                String operateType = mappedStatement.getSqlCommandType().name();
                String sql = mappedStatement.getBoundSql(parameterObject).getSql();
                String tableName = spliceTableName(sql, operateType);

                BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
                Configuration configuration = mappedStatement.getConfiguration();
                // 2. 拼接操作SQL
                String executeSql = getExecuteSql(configuration, boundSql);
                // 3. 如果当前操作由http请求发起,取出http请求对应的id
                HttpRequestPO httpRequest = httpRequestInterceptor.getThreadLocalHttpRequest();
                // 4. 保存数据库操作日志
                DatabaseOperatePO databaseOperate = build(operateType, tableName, executeSql, httpRequest);
                SystemLogQueue.put(databaseOperate);
            }
        } catch (Exception e) {
            logger.error("保存数据库操作语句异常", e);
        }
        return invocation.proceed();
    }

    /**
     * 根据参数和预处理语句合成实际使用的SQL
     * @return
     */
    private static String getExecuteSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        // 将多个空格合并为一个
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings != null && parameterMappings.size() > 0 && parameterObject != null) {
            // 如果当前参数有对应TypeHandler,使用对应TypeHandler处理
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                for (int i = 0; i < parameterMappings.size(); i++) {
                    sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));
                }
            } else {
                // 循环处理参数,替换占位符
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object object = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(object)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object object = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(object)));
                    } else {
                        sql = sql.replaceFirst("\\?", "无法转换");
                    }
                }
            }
        }
        return sql + ";";
    }

    /**
     * 将原始参数转化为SQL中实际可执行的参数
     * @param object
     * @return
     */
    private static String getParameterValue(Object object) {
        if (object instanceof String) {
            return "'" + object.toString() + "'";
        }
        if (object instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            return "'" + formatter.format(new Date()) + "'";
        }
        if (object != null) {
            return object.toString();
        }
        return "";
    }

    /**
     * 提取sql中的表名
     */
    private String spliceTableName(String sql, String operateType) {
        sql = sql.toLowerCase();
        switch (operateType) {
            case "INSERT":
                return trim(sql.substring(sql.indexOf("into") + 4, sql.indexOf("(")));
            case "UPDATE":
                return trim(sql.substring(sql.indexOf("update") + 6, sql.indexOf("set")));
            case "DELETE":
                return trim(sql.substring(sql.indexOf("from")+ 4, sql.indexOf("where")));
            default:
                return "";
        }
    }

    /**
     * 进一步简化表名
     */
    private String trim(String sql) {
        return sql.trim().replaceAll("`", "").toLowerCase();
    }

    /**
     * 创建DatabaseOperatePO 对象
     */
    private DatabaseOperatePO build(String operateType,
                                    String tableName,
                                    String sql,
                                    HttpRequestPO httpRequest) {
        DatabaseOperatePO databaseOperate = new DatabaseOperatePO();
        databaseOperate.setOperateType(operateType);
        databaseOperate.setTableName(tableName);
        databaseOperate.setSql(sql);
        if (httpRequest != null) {
            databaseOperate.setRequestId(httpRequest.getId());
        }
        return databaseOperate;
    }
}
