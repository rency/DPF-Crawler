package org.rency.crawler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rency.crawler.beans.CookieParam;
import org.rency.crawler.dao.CookieParamDao;
import org.rency.crawler.service.CookieParamService;
import org.rency.utils.exceptions.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("cookieParamService")
@Scope("singleton")
public class CookieParamServiceImpl implements CookieParamService {

	@Autowired
	@Resource(name="cookieParamDao")
	private CookieParamDao cookieParamDao;
	
	@Override
	public List<CookieParam> list() throws CoreException {
		return cookieParamDao.list();
	}

	@Override
	public CookieParam query(String host) throws CoreException {
		return cookieParamDao.get(host);
	}

	@Override
	public boolean add(CookieParam cookie) throws CoreException {
		CookieParam cp = query(cookie.getHost());
		if(cp == null){
			return cookieParamDao.save(cookie);
		}else{
			return false;
		}		
	}

	@Override
	public boolean update(CookieParam cookie) throws CoreException {
		return cookieParamDao.update(cookie);
	}

	@Override
	public boolean delete(String host) throws CoreException {
		return cookieParamDao.delete(host);
	}
	
	@Override
	public boolean deleteAll() throws CoreException {
		return cookieParamDao.deleteAll();
	}

}
