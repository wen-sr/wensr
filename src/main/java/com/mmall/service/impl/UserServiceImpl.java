package com.mmall.service.impl;

import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wen-sr on 2017/8/23.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User login(String username, String password) {
		return null;
	}

	/**
	 * 检查用户名是否存在
	 * @param username
	 * @return
	 */
	@Override
	public boolean checkUserName(String username) {
		User u = userMapper.selectByUsername(username);
		System.out.println(u.getUsername());
		return false;
	}


}
