package com.lj.mysystem.impl.user;

import com.lj.mysystem.dao.user.UserMapper;
import com.lj.mysystem.entity.user.User;
import com.lj.mysystem.service.test.TestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service
public class UserServiceImpl implements TestService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User selectByPrimaryKey(Long key) {
        return userMapper.selectByPrimaryKey(key);
    }
}
