package com.lj.mysystem.impl.tax;

import com.lj.mysystem.dao.base.BaseMapper;
import com.lj.mysystem.dao.tax.IncomeTaxDispatchMapper;
import com.lj.mysystem.entity.tax.IncomeTaxDispatch;
import com.lj.mysystem.impl.base.BaseServiceImpl;
import com.lj.mysystem.service.tax.IncomeTaxDispatchService;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* Created by JamesLee on 19-3-21 下午3:14
* Copyright © 2019 JamesLee. All rights reserved.
*/

@Transactional
@Service
public class IncomeTaxDispatchServiceImpl extends BaseServiceImpl<IncomeTaxDispatch,Integer> implements IncomeTaxDispatchService {
    @Resource
    private IncomeTaxDispatchMapper incomeTaxDispatchMapper;

    @Override
    public BaseMapper getBaseMapper() {
        return incomeTaxDispatchMapper;
    }
}