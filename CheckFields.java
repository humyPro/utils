package com.humy.util;

import java.lang.reflect.Field;

/** 业务参数检测工具
 * @Author humy
 * @Date 2018/12/12 - 23:16
 */
public class CheckFields {
    
    /**
     * 对象的类的默认的基础包名
     */
    public static String defaultBasePackageName="com.humy";
    /**
     * 传入一个对象，检查这个对象中有没有空值,一般用于检测javabean对象
     * @param t 需要被检测的对象,默认包名可以自定义
     * @return
     */
    public static <T> boolean checkFieldSEmpty(T t) {
        return checkFieldSEmpty(t, defaultBasePackageName);
    }

    /**
     * 传入一个对象，检查这个对象中有没有空值,一般用于检测javabean对象
     *
     * @param t 需要被检测的对象
     * @param basePackageName  对象的类的基础包名
     * @return  如果对象中有一个字段是空值，返回true(空字符串""判定为空值)
     */
    public static <T> boolean checkFieldSEmpty(T t, String basePackageName) {
        if (t == null) {
            return true;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Package _package = field.getType().getPackage();
            Object o = null;
            if (_package == null || !_package.getName().startsWith(basePackageName)) {
                try {
                    o = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (o == null || o.toString().length() == 0) {
                    return true;
                }
            } else {
                try {
                    o = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return checkFieldSEmpty(o);
            }
        }
        return false;
    }
}
