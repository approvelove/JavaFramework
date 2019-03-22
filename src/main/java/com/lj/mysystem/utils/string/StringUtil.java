package com.lj.mysystem.utils.string;

public class StringUtil {
    public static boolean isEmpty(String string) {
        if (string == null) return true;
        if (string.length() == 0) return true;
        return false;
    }
}
