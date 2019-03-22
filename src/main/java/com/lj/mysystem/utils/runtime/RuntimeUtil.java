package com.lj.mysystem.utils.runtime;

import java.lang.reflect.Field;

public class RuntimeUtil {

    //获取属性列表
    public static Field[] getFields(Object object) {
        return object.getClass().getDeclaredFields();
    }

}
