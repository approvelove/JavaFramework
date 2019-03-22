package ${controllerPackage};

import ${prefix}utils.app.AppUtil;
import ${prefix}utils.string.StringUtil;
import ${prefix}utils.app.IdGen;
import ${prefix}entity.base.PageEntity;
import ${domainImport};
import ${baseControllerImport};
import ${prefix}entity.base.RequestMap;
import ${serviceImport};

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by JamesLee on ${.now?string.short}
* Copyright © ${.now?string.yyyy} JamesLee. All rights reserved.
*/

@RestController
@RequestMapping("${lowerdomainName}")
public class ${domainName}Controller extends BaseController {
    @Resource
    private ${domainName}Service ${sdomainName}Service;

    @RequestMapping(value = "list")
    @ResponseBody
    public Object list(HttpServletRequest request) throws Exception {
        PageEntity pageEntity = PageEntity.getPageEntity(request);
        List<${domainName}> ${sdomainName}s = ${sdomainName}Service.findPageList(pageEntity);
        int totalCount = ${sdomainName}Service.findTotalCount(pageEntity);
        Map map = new HashMap();
        map.put("data",${sdomainName}s);
        map.put("total",totalCount);
        return AppUtil.returnResult(map,"获取数据成功",AppUtil.Result.success);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public Object add(HttpServletRequest request) throws Exception {
        RequestMap requestMap = new RequestMap(request);
        Map map = new HashMap();
        ${domainName} ${sdomainName} = new ${domainName}();
        AppUtil.transMap2Bean().populate(${sdomainName},requestMap.getMap());
        if(StringUtil.isEmpty(requestMap.getString("${primaryKey}"))) {
           ${sdomainName}.set${UprimaryKey}(<#if pkType=="String">String.valueOf(IdGen.get().nextId())<#else>IdGen.get().nextId()</#if>);
           ${sdomainName}Service.insertSelective(${sdomainName});
        } else {
           ${sdomainName}Service.updateByPrimaryKeySelective(${sdomainName});
        }
        return AppUtil.returnResult(map,"成功添加一条数据",AppUtil.Result.success);
    }

    @RequestMapping(value = "delete")
    @ResponseBody
    public Object delete(HttpServletRequest request) throws Exception {
        RequestMap requestMap = new RequestMap(request);
        Map map = new HashMap();
        String[] ids = requestMap.getString("ids").split(",");
        ${sdomainName}Service.batchDeleteByPrimaryKey(ids);
        return AppUtil.returnResult(map,"成功删除数据",AppUtil.Result.success);
    }
}
