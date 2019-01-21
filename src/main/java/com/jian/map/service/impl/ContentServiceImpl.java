package com.jian.map.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jian.map.entity.Content;
import com.jian.map.dao.ContentDao;
import com.jian.map.service.ContentService;

/**
 * @author liujian
 * @Date  
 */
@Service
public class ContentServiceImpl extends BaseServiceImpl<Content> implements ContentService {

	@Autowired
	private ContentDao dao;
	
	@Override
	public void initDao() {
		super.baseDao = dao;
	}

}
