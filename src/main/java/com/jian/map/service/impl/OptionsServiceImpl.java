package com.jian.map.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.map.entity.Options;
import com.jian.map.dao.OptionsDao;
import com.jian.map.service.OptionsService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class OptionsServiceImpl extends BaseServiceImpl<Options> implements OptionsService {

	@Autowired
	private OptionsDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
