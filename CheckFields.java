package com.humy.utils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

/**
 * @Auther: Hu Min
 * @Date: 2018/12/10 15:44
 * @Description:
 */
public class CheckFileds {
    /**
     * 判断一个model中的字段有没有空的,以最基本的model传入
     *
     * @param t
     * @return 如果有空的字段，返回true
     */
    public static boolean checkFiledsIsEmpty(Object t) {
        String defaultBasePackageName = "com.humy";
        return checkFiledsIsEmpty(t, defaultBasePackageName);
    }

    /**
     * 判断一个model中的字段有没有空的值
     *
     * @param t               需要判断的对象
     * @param basePackageName 需要检测字段所在的包名，默认是com.shanglv51下
     * @return
     */
    public static boolean checkFiledsIsEmpty(Object t, String basePackageName) {
        return checkFiledsIsEmpty(t, basePackageName, null);
    }

    /**
     * 判断一个model中的字段有没有空的值
     *
     * @param t            需要判断的对象
     * @param ignoreFields 忽略的字段，对象类.字段名，例如A.a
     * @return
     */
    public static boolean checkFiledsIsEmpty(Object t, String[] ignoreFields) {
        String defaultBasePackageName = "com.shanglv51";
        return checkFiledsIsEmpty(t, defaultBasePackageName, ignoreFields);
    }

    /**
     * 判断一个model中的字段有没有空的值
     *
     * @param t               需要判断的对象
     * @param basePackageName 对象中需要进入其内部继续判断的字段的基础包名
     * @param ignoreFields    忽略的字段，对象类.字段名，例如A.a
     * @return
     */
    public static boolean checkFiledsIsEmpty(Object t, String basePackageName, String[] ignoreFields) {
        if (t == null) {
            return true;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        if (ignoreFields != null && ignoreFields.length != 0) {
            String[] _ignoreFields = Arrays.stream(ignoreFields).filter(s -> s.startsWith(t.getClass().getSimpleName())).toArray(value -> new String[value]);
            fields = Arrays.stream(fields).filter(field -> {
                for (String ignoreField : _ignoreFields) {
                    return !ignoreField.equals(t.getClass().getSimpleName() + "." + field.getName());
                }
                return true;
            }).toArray(value -> new Field[value]);
        }
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

                if (o == null) {
                    return true;
                }
                if ((o.getClass().isArray())) {
                    for (int i = 0; i < Array.getLength(o); i++) {
                        boolean flag = checkFiledsIsEmpty(Array.get(o, i), basePackageName, ignoreFields);
                        if (flag == true) {
                            return true;
                        }
                    }
                } else if (Collection.class.isAssignableFrom(o.getClass())) {
                    Collection collection = (Collection) o;
                    for (Object o1 : collection) {
                        boolean flag = checkFiledsIsEmpty(o1, basePackageName, ignoreFields);
                        if(flag==true){
                            return true;
                        }
                    }
                } else if (o.toString().length() == 0) {
                    return true;
                }
            } else {
                try {
                    o = field.get(t);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                boolean flag = checkFiledsIsEmpty(o, basePackageName, ignoreFields);
                if(flag==true){
                    return  true;
                }
            }
        }
        return false;
    }
}
