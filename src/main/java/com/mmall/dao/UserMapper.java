package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 检查用户名是否存在
     *
     * @param username
     * @return
     */
    User selectByUsername(String username);

    /**
     * 登录
     *
     * @param username
     * @param md5Password
     * @return
     */
    User selectLogin(@Param(value = "username") String username, @Param("password") String md5Password);

    /**
     * check username
     *
     * @param str
     * @return
     */
    int checkUsername(String str);

    /**
     * check email
     *
     * @param str
     * @return
     */
    int checkEmail(String str);

    /**
     * 检查答案是否正确
     *
     * @param username
     * @param question
     * @param answer
     * @return
     */
    int checkAnser(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updatePasswdByUsername(@Param("username") String username, @Param("password") String password);
}