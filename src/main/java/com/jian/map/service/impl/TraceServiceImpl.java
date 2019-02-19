package com.jian.map.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.map.entity.Trace;
import com.jian.map.dao.TraceDao;
import com.jian.map.service.TraceService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class TraceServiceImpl extends BaseServiceImpl<Trace> implements TraceService {

	@Autowired
	private TraceDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
