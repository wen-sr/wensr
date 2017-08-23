package com.mmall.service.impl;

import com.github.pagehelper.StringUtil;
import com.mmall.common.ServerResponse;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.mmall.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Created by wen-sr on 2017/8/23.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {

		if(userMapper.selectByUsername(username) <=0 ){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		String md5Password = MD5Util.MD5Encode(password,"UTF-8");
		User user = userMapper.selectLogin(username, md5Password);
		if(user == null){
			return ServerResponse.createByErrorMessage("密码错误");
		}
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);

		return ServerResponse.createBySuccess("登录成功",user);
	}

	/**
	 * 检查用户名是否存在
	 * @param username
	 * @return
	 */
	@Override
	public boolean checkUserName(String username) {
		int i = userMapper.selectByUsername(username);
		System.out.println(i);
		return false;
	}


}
