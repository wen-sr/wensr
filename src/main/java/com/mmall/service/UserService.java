package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by wen-sr on 2017/8/23.
 */
public interface UserService {

	ServerResponse<User> login(String username, String password);

	boolean checkUserName(String username);
}
