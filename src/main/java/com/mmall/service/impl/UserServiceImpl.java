package com.mmall.service.impl;

import com.mmall.common.Constant;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import com.mmall.util.MD5Util;
import org.apache.catalina.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by wen-sr on 2017/8/23.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public ServerResponse<User> login(String username, String password) {

		if(this.checkValid(username, Constant.USERNAME).isSuccess()){
			return ServerResponse.createByErrorMessage("用户名不存在");
		}
		String md5Password = MD5Util.MD5EncodeUtf8(password);
		User user = userMapper.selectLogin(username, md5Password);
		if(user == null){
			return ServerResponse.createByErrorMessage("密码错误");
		}
		user.setPassword(org.apache.commons.lang3.StringUtils.EMPTY);
		return ServerResponse.createBySuccess("登录成功",user);
	}

	/**
	 * check username and email
	 * @param str
	 * @param type
	 * @return
	 */
	@Override
	public ServerResponse<String> checkValid(String str, String type) {
		if(org.apache.commons.lang3.StringUtils.isNotBlank(type)){
			if(Constant.USERNAME.equals(type)){
				if(userMapper.checkUsername(str) > 0 ){
					return ServerResponse.createByErrorMessage("用户名已存在");
				}

			}else if(Constant.EMAIL.equals(type)){
				if(userMapper.checkEmail(str) > 0 ){
					return ServerResponse.createByErrorMessage("Email已存在");
				}
			}else{
				return ServerResponse.createByErrorMessage("参数无效");
			}
			return ServerResponse.createBySuccessMsg("验证通过");
		}else {
			return ServerResponse.createByErrorMessage("参数错误");
		}
	}

	/**
	 *
	 * @param user
	 * @return
	 */
    @Override
    public ServerResponse<String> register(User user) {
		if(!this.checkValid(user.getUsername(),Constant.USERNAME).isSuccess()){
			return ServerResponse.createByErrorMessage("用户名已存在");
		}

		if(!this.checkValid(user.getEmail(), Constant.EMAIL).isSuccess()){
			return ServerResponse.createByErrorMessage("Email已存在");
		}
		user.setRole(Constant.Role.ROLE_CUSTOMER);
		user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
		int i = userMapper.insert(user);
		if(i > 0 ){
			return ServerResponse.createBySuccess("注册成功");
		}
        return ServerResponse.createByErrorMessage("注册失败");
    }

	@Override
	public ServerResponse<String> forgetGetQuestion(String username) {
    	ServerResponse response = this.checkValid(username, Constant.USERNAME);
    	if(response.isSuccess()){
    		return response.createByErrorMessage("用户名不存在");
		}

		User user = userMapper.selectByUsername(username);
    	if(user != null){
    		return response.createBySuccess(user.getQuestion());
		}
		return response.createBySuccessMsg("没有找到获取密保问题");
	}

	@Override
	public ServerResponse<String> checkAnswer(String username, String question, String answer) {
		int resultCount = userMapper.checkAnser(username, question, answer);
		if(resultCount > 0){
			String forgetToken = UUID.randomUUID().toString();
			TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
			return ServerResponse.createBySuccess(forgetToken);
		}
		return ServerResponse.createByErrorMessage("密保问题的答案错误");
	}

	@Override
	public ServerResponse<String> forgetResetPasswd(String username, String passwdNew, String forgetToken) {
		if(StringUtils.isBlank(forgetToken)){
			return ServerResponse.createByErrorMessage("没有token，无法重置密码");
		}
		if(!StringUtils.equals((TokenCache.getKey(TokenCache.TOKEN_PREFIX + username)),forgetToken)){
			return ServerResponse.createByErrorMessage("token无效或已过期");
		}
		int resultCount = userMapper.updatePasswdByUsername(username, MD5Util.MD5EncodeUtf8(passwdNew));
		if(resultCount > 0){
			return ServerResponse.createBySuccessMsg("恭喜您，修改密码成功");
		}
		return ServerResponse.createByErrorMessage("修改密码失败");
	}

    @Override
    public ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew) {
        String passO = userMapper.selectByPrimaryKey(user.getId()).getPassword();
        if(!StringUtils.equals(passO,MD5Util.MD5EncodeUtf8(passwordOld))){
            return ServerResponse.createByErrorMessage("原密码错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int i = userMapper.updateByPrimaryKeySelective(user);
        if(i > 0 ){
            return ServerResponse.createBySuccessMsg("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");

    }

    @Override
    public ServerResponse<User> updateUserInfo(User user) {
        String username = user.getUsername();
        user.setUsername(null);
        int i = userMapper.updateByPrimaryKeySelective(user);
        if( i > 0 ){
            user.setUsername(username);
            User u = userMapper.selectByPrimaryKey(user.getId());
            u.setPassword(null);
            return ServerResponse.createBySuccess("您的信息修改成功", u);
        }
        return ServerResponse.createByErrorMessage("您的信息修改失败");
    }

}
