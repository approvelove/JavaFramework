package com.lj.mysystem.utils.app;

import com.lj.mysystem.utils.converter.DateTimeConverter;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.BigDecimalConverter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AppUtil {
    public enum Result {
        error(0),
        success(1);
        private int value;

        Result(int i) {
            this.value = i;
        }

        public int getValue() {
            return value;
        }
    }

    public final static Object returnResult(Object data, String msg, Result result) {
        Map map = new HashMap<String, Object>();
        map.put("msg",msg);
        map.put("resultCode",result.getValue());
        if (result == Result.success) {
            map.put("data",data);
        }
        return map;
    }

    public static BeanUtilsBean transMap2Bean() throws Exception {
        DateTimeConverter dateTimeConverter = new DateTimeConverter();
        BigDecimalConverter bigDecimalConverter = new BigDecimalConverter();
        ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.deregister(Date.class);
        convertUtilsBean.register(dateTimeConverter,Date.class);
        convertUtilsBean.register(bigDecimalConverter, BigDecimal.class);
        BeanUtilsBean beanUtilsBean = new BeanUtilsBean(convertUtilsBean,
                new PropertyUtilsBean());
        return beanUtilsBean;
    }
}
