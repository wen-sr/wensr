package com.mmall.controller;

import com.mmall.pojo.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by wen-sr on 2017/8/23.
 */
@Controller
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	public User login(String username, String password){
		return  null;
	}


	public void checkUsername(String username){
		boolean isExist = userService.checkUserName(username);
	}
}
