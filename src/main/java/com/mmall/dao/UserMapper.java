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
     * @param username
     * @return
	 */
    int selectByUsername(String username);

    /**
     * 登录
     * @param username
     * @param md5Password
     * @return
     */
    User selectLogin(@Param(value = "username") String username, @Param("password") String md5Password);
}