package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;

/**
 * Created by wen-sr on 2017/8/23.
 */
public interface UserService {

	ServerResponse<User> login(String username, String password);

	ServerResponse<String> checkValid(String username, String type);

    ServerResponse<User> register(User user);

	ServerResponse<String> forgetGetQuestion(String username);

	ServerResponse<String> checkAnswer(String username, String question, String answer);

	ServerResponse<String> forgetResetPasswd(String username, String passwdNew, String forgetToken);

	ServerResponse<String> resetPassword(User user, String passwordOld, String passwordNew);

	ServerResponse<User> updateUserInfo(User user);
}
