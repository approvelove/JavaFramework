package com.lj.mysystem.entity.base;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Data
public class PageEntity<T> extends BaseEntity {
    private int page;
    private int pageSize = 10;
    private int totalCount;
    private int totalPages;
    private int start;
    private int msg;
    private int result;
    Map<String,Object> cons = new HashMap<String, Object>();

    public PageEntity(int start, int page, int pageSize, Map map) {
        this.page = page;
        this.pageSize = pageSize;
        this.cons = map;
        this.start = start;
    }

    //分页类
    public static PageEntity getPageEntity(HttpServletRequest request) throws Exception {
        RequestMap requestMap = new RequestMap(request);
        int pageSize = Integer.parseInt(requestMap.getString("limit")==null? "10" : requestMap.getString("limit"));
        int page = Integer.parseInt(requestMap.getString("page")==null?"1":requestMap.getString("page"));
        PageEntity pageEntity = new PageEntity((page-1)*pageSize,page,pageSize,requestMap.getMap());
        return pageEntity;
    }
}
