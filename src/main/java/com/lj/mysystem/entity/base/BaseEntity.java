package com.lj.mysystem.entity.base;


import com.lj.mysystem.utils.runtime.RuntimeUtil;

import java.io.Serializable;
import java.lang.reflect.Field;

public class BaseEntity implements Serializable {
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Field[] fields = RuntimeUtil.getFields(this);
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                result = prime * result + ((field.get(this) == null) ? 0 : field.get(this).hashCode());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        if (getClass() != object.getClass()) return false;
        Class other = object.getClass();
        Field[] fields = RuntimeUtil.getFields(this);
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                Object me = field.get(this);
                Field otherField = other.getDeclaredField(field.getName());
                otherField.setAccessible(true);
                Object otherObject = otherField.get(object);
                if (me == null) {
                    if (otherObject != null) return false;
                } else if (!me.equals(otherObject)) return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName()+":[");
        Field[] fields = RuntimeUtil.getFields(this);
        try {
            for (Field field: fields) {
                field.setAccessible(true);
                builder.append(field.getName()).append("=").append(field.get(this)).append(",");
            }
        } catch (Exception e) {
            builder.append("toString builder encounter an error");
        }
        builder.replace(builder.lastIndexOf(","),builder.lastIndexOf(",")+1,"");
        builder.append("]");
        return builder.toString();
    }
}
