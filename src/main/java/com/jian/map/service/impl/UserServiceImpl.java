package com.jian.map.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.map.entity.User;
import com.jian.map.dao.UserDao;
import com.jian.map.service.UserService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

	@Autowired
	private UserDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
