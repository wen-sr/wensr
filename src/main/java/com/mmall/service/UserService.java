package com.mmall.service;

import com.mmall.pojo.User;

/**
 * Created by wen-sr on 2017/8/23.
 */
public interface UserService {

	User login(String username, String password);

	boolean checkUserName(String username);
}
