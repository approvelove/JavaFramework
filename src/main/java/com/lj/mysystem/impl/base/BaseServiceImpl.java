package com.lj.mysystem.impl.base;

import com.lj.mysystem.dao.base.BaseMapper;
import com.lj.mysystem.entity.base.PageEntity;
import com.lj.mysystem.service.base.BaseService;

import java.util.List;
import java.util.Map;

public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
    public abstract BaseMapper getBaseMapper();

    @Override
    public int deleteByPrimaryKey(ID id) {
        return getBaseMapper().deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T record) {
        return getBaseMapper().insert(record);
    }

    @Override
    public int insertSelective(T record) {
        return getBaseMapper().insertSelective(record);
    }

    @Override
    public T selectByPrimaryKey(ID id) {
        return (T) getBaseMapper().selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return getBaseMapper().updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        return getBaseMapper().updateByPrimaryKey(record);
    }

    // 分页
    public List<T> findPageList(PageEntity pageEntity){
        return getBaseMapper().findPageList(pageEntity);
    }

    // 分页统计
    public int  findTotalCount(PageEntity pageEntity){
        return getBaseMapper().findTotalCount(pageEntity);
    }

    // 批量删除
    public int batchDeleteByPrimaryKey(String[] ids){
        return getBaseMapper().batchDeleteByPrimaryKey(ids);
    }

    // 查找
    public T findByMap(Map map){return (T) getBaseMapper().findByMap(map);}

    public List<T> findListByMap(Map map){return (List<T>) getBaseMapper().findListByMap(map);}
}
