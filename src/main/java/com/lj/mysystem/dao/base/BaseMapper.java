package com.lj.mysystem.dao.base;

import com.lj.mysystem.entity.base.PageEntity;

import java.util.List;
import java.util.Map;

public interface BaseMapper<T,ID> {
    int deleteByPrimaryKey(ID id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(ID id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);

    List<T> findPageList(PageEntity pageEntity);

    int findTotalCount(PageEntity pageEntity);

    int batchDeleteByPrimaryKey(String[] ids);

    T findByMap(Map map);

    List<T> findListByMap(Map map);
}
