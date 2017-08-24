package com.mmall.dao;

import com.mmall.common.BaseTest;
import com.mmall.pojo.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserMapperTest extends BaseTest {
    @Test
    public void selectByUsername() throws Exception {
        User user = userMapper.selectByUsername("admin");
        System.out.println(user.toString());
    }

    @Autowired
    private UserMapper userMapper;

    @Test
    public void checkEmail()  {
        int i = userMapper.checkEmail("admin@happymmall.com");
        System.out.println(i);
    }

}