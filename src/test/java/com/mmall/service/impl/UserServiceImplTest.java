package com.mmall.service.impl;

import com.mmall.common.BaseTest;
import com.mmall.dao.UserMapper;
import com.mmall.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceImplTest extends BaseTest {

    @Autowired
    private UserService userService;

    @Test
    public void resetPassword() throws Exception {
    }

}