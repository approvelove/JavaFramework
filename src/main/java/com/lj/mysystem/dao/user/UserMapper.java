package com.lj.mysystem.dao.user;

import com.lj.mysystem.entity.user.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User selectByPrimaryKey(@Param("user_id") Long key);
}
