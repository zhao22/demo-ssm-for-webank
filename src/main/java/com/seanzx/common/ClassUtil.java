package com.seanzx.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类拷贝封装
 * @author zhaoxin
 * @date 2019/12/17
 */
public class ClassUtil {

    private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 生成 targetClass 对应的对象，并将 source 中的属性拷贝到 对象中
     * 如果 source == null, 会返回 null
     * 如果 targetClass 为 Class.class，会产生 InstantiationException, 返回 null
     * 如果 targetClass 没有无参构造器，会 产生 IllegalAccessException, 返回 null
     */
    public static<S, T> T copy(S source, Class<T> targetClass){
        if (source == null) {
            return null;
        }
        T target;
        try {
            target = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("类型转换异常", e);
            return null;
        }
        BeanUtils.copyProperties(source, target);
        return target;
    }


    /**
     * 为集合sourceList中每个元素生成 targetClass 对应的对象，并将 source 中的属性拷贝到 对象中
     */
    public static <S, T> List<T> copy(List<S> sourceList, Class<T> targetClass) {
        List<T> targetList = new ArrayList<>(sourceList.size());
        for (S source : sourceList) {
            T target = copy(source, targetClass);
            targetList.add(target);
        }
        return targetList;
    }

    /**
     * 将对象转化为对应的Class并返回
     * value 等于 null 时, 返回 null
     * 目标Class为 value 的所属类或父类或父接口时,将 value 转换为对应类型返回
     * 否则返回 null
     */
    public static <T> T cast(Object value, Class<T> targetClass) {
        if (targetClass.isInstance(value)) {
            return targetClass.cast(value);
        }
        return null;
    }

}