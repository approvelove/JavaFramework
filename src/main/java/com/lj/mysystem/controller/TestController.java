package com.lj.mysystem.controller;

import com.alibaba.fastjson.JSON;
import com.lj.mysystem.config.LJConfig;
import com.lj.mysystem.entity.user.User;
import com.lj.mysystem.service.test.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    private LJConfig ljConfig;

    @Resource
    private TestService testService;

    @RequestMapping("getUser")
    public Object getUser() {
        User user = testService.selectByPrimaryKey(1002437258067836928L);
        return user;
    }

    @RequestMapping("hello")
    public Object Hello() {
        return this.ljConfig.toString();
    }
}
