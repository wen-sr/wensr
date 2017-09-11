package com.mmall.controller.backend;

import com.mmall.common.Constant;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Description:
 * User: wen-sr
 * Date: 2017-09-11  8:54
 */
@RestController
@RequestMapping("/manage/user")
public class UserManageController {

    @Autowired
    IUserService iUserService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ServerResponse login(String username, String password, HttpSession session) {
        ServerResponse response = iUserService.login(username, password);
        if(response.isSuccess()){
            User user = (User) response.getData();
            if(user.getRole() == Constant.Role.ROLE_ADMIN){
                session.setAttribute("user", user);
                return ServerResponse.createBySuccess("登录成功", user);
            }else{
                return ServerResponse.createByErrorMessage("非管理员帐号，登录失败");
            }
        }
        return response;
    }
}
