package com.mmall.dao;

import com.mmall.pojo.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Created by wen-sr on 2017/8/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:applicationContext.xml"})
public class UserDaoTest {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void checkUsernameTest(){
		User u = userMapper.selectByUsername("aaa");
		System.out.println(u.getEmail());
	}

}
