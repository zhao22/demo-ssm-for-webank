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

}