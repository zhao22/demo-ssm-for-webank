package com.seanzx.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 数据库操作日志PO
 * @author zhaoxin
 * @date 2020/10/21
 */
@Table(name = "t_database_operate")
public class DatabaseOperatePO {

    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    /**
     * http请求 id (为空则为内部任务操作)
     */
    @Column(name = "request_id")
    private String requestId;

    /**
     * 操作类型
     * INSERT, UPDATE, DELETE
     */
    @Column(name = "operate_type")
    private String operateType;

    /**
     * 表名
     */
    @Column(name = "table_name")
    private String tableName;

    /**
     * 转换后的操作SQL
     */
    @Column(name = "operate_sql")
    private String operateSql;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOperateSql() {
        return operateSql;
    }

    public void setSql(String operateSql) {
        this.operateSql = operateSql;
    }

    @Override
    public String toString() {
        return "DatabaseOperatePO{" +
                "id=" + id +
                ", requestId=" + requestId +
                ", operateType='" + operateType + '\'' +
                ", tableName='" + tableName + '\'' +
                ", opearteSql='" + operateSql + '\'' +
                '}';
    }
}
