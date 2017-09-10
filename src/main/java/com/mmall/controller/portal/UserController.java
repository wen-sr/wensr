package com.mmall.controller.portal;

import com.mmall.common.Constant;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.UserService;
import org.apache.catalina.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wen-sr on 2017/8/23.
 */
@Controller
@RequestMapping("/user")
public class UserController{

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @RequestMapping(value = "/login.do", method = {RequestMethod.POST})
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);
        if(response != null){
            session.setAttribute(Constant.CURRENT_USER,response.getData());
        }
        return response;
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping(value = "/logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session){
        session.removeAttribute(Constant.CURRENT_USER);
        return ServerResponse.createBySuccessMsg("退出登录成功");
    }

    /**
     * 检查用户名,email是否存在
     * @param str
     */
    @RequestMapping(value = "/check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkUsername(String str, String type) {
        return userService.checkValid(str, type);
    }

    /**
     * 注册
     * @param user
     * @return
     */
    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user){
        return userService.register(user);
    }

    /**
     * 获取用户信息
     * @param session
     * @return
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User)session.getAttribute(Constant.CURRENT_USER);
        if(user == null ){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录");
        }
        return ServerResponse.createBySuccess(user);
    }

    /**
     * 忘记密码时获取密保问题
     * @param username
     * @return
     */
    @RequestMapping(value = "/forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userService.forgetGetQuestion(username);
    }

    /**
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username,String question, String answer){
        return userService.checkAnswer(username, question, answer);
    }

    /**
     * 忘记密码，重置密码
     * @param username
     * @param passwdNew
     * @param forgetToken
     * @return
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwdNew, String forgetToken){
        return userService.forgetResetPasswd(username, passwdNew, forgetToken);
    }

    /**
     * 修改密码
     * @param username
     * @param passwordOld
     * @param passwordNew
     * @param session
     * @return
     */
    @RequestMapping(value = "/reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(String username, String passwordOld, String passwordNew, HttpSession session){
        User user = (User) session.getAttribute(Constant.CURRENT_USER);
        if( user == null ){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        User user2 = new User();
        user2.setId(user.getId());
        return userService.resetPassword(user2,passwordOld, passwordNew);
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @RequestMapping(value = "/update_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(User user, HttpSession session){
        User u = (User) session.getAttribute(Constant.CURRENT_USER);
        if(u == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        if(!StringUtils.equals(u.getUsername(), user.getUsername())){
            return ServerResponse.createByErrorMessage("用户名不允许修改");
        }
        if(StringUtils.isNotBlank(user.getPassword())){
            return ServerResponse.createByErrorMessage("密码不允许修改");
        }
        user.setId(u.getId());
        return userService.updateUserInfo(user);
    }


}
