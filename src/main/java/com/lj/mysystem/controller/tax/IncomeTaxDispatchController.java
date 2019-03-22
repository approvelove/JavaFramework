package com.lj.mysystem.controller.tax;

import com.lj.mysystem.utils.app.AppUtil;
import com.lj.mysystem.utils.string.StringUtil;
import com.lj.mysystem.utils.app.IdGen;
import com.lj.mysystem.entity.base.PageEntity;
import com.lj.mysystem.entity.tax.IncomeTaxDispatch;
import com.lj.mysystem.controller.base.BaseController;
import com.lj.mysystem.entity.base.RequestMap;
import com.lj.mysystem.service.tax.IncomeTaxDispatchService;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by JamesLee on 19-3-21 下午3:14
* Copyright © 2019 JamesLee. All rights reserved.
*/

@RestController
@RequestMapping("incometaxdispatch")
public class IncomeTaxDispatchController extends BaseController {
    @Resource
    private IncomeTaxDispatchService incomeTaxDispatchService;

    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(HttpServletRequest request) throws Exception {
        PageEntity pageEntity = PageEntity.getPageEntity(request);
        List<IncomeTaxDispatch> incomeTaxDispatchs = incomeTaxDispatchService.findPageList(pageEntity);
        int totalCount = incomeTaxDispatchService.findTotalCount(pageEntity);
        Map map = new HashMap();
        map.put("result",incomeTaxDispatchs);
        map.put("total",totalCount);
        return AppUtil.returnResult(map,"获取数据成功",AppUtil.Result.success);
    }

    @RequestMapping("test")
    @ResponseBody
    public Object test(HttpServletRequest request) throws Exception {
        RequestMap requestMap = new RequestMap(request);
        IncomeTaxDispatch incomeTaxDispatch = incomeTaxDispatchService.selectByPrimaryKey(Integer.parseInt(requestMap.getString("taxId")));
        return AppUtil.returnResult(incomeTaxDispatch,"获取数据成功",AppUtil.Result.success);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public Object add(HttpServletRequest request) throws Exception {
        RequestMap requestMap = new RequestMap(request);
        Map map = new HashMap();
        IncomeTaxDispatch incomeTaxDispatch = new IncomeTaxDispatch();
        AppUtil.transMap2Bean().populate(incomeTaxDispatch,requestMap.getMap());
        if(StringUtil.isEmpty(requestMap.getString("tax_id"))) {
           incomeTaxDispatch.setTaxId((int)IdGen.get().nextId());
           incomeTaxDispatchService.insertSelective(incomeTaxDispatch);
        } else {
           incomeTaxDispatchService.updateByPrimaryKeySelective(incomeTaxDispatch);
        }
        return AppUtil.returnResult(map,"成功添加一条数据",AppUtil.Result.success);
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public Object delete(HttpServletRequest request) throws Exception {
        RequestMap requestMap = new RequestMap(request);
        Map map = new HashMap();
        String[] ids = requestMap.getString("ids").split(",");
        incomeTaxDispatchService.batchDeleteByPrimaryKey(ids);
        return AppUtil.returnResult(map,"成功删除数据",AppUtil.Result.success);
    }
}
