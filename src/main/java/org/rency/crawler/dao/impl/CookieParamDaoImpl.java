package org.rency.crawler.dao.impl;

import java.util.List;

import org.rency.crawler.beans.CookieParam;
import org.rency.crawler.dao.CookieParamDao;
import org.rency.utils.dao.BasicDao;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("cookieParamDao")
public class CookieParamDaoImpl implements CookieParamDao {
	
	private static final Logger logger = LoggerFactory.getLogger(CookieParamDaoImpl.class);
	
	@Autowired
	@Qualifier("hibernateDao")
	private BasicDao basicDao;
	
	private final String ENTITY = CookieParam.class.getName();
	
	private final String HOST = "host";

	@Override
	public List<CookieParam> list() throws CoreException {
		try{
			return basicDao.loadAll(CookieParam.class);
		}catch(CoreException e){
			logger.error("exec load all error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public CookieParam get(String host) throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+HOST+"=?";
		try{
			List<CookieParam> list = basicDao.findByHQL(queryString, host);
			if(list != null && list.size() > 0){
				return list.get(0);
			}
			return null;
		}catch(CoreException e){
			logger.error("exec ["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean save(CookieParam cookie) throws CoreException {
		try{
			return basicDao.save(cookie);
		}catch(CoreException e){
			logger.error("exec save error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean update(CookieParam cookie) throws CoreException {
		try{
			return basicDao.update(cookie);
		}catch(CoreException e){
			logger.error("exec update error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean delete(String host) throws CoreException {
		try{
			return basicDao.deleteByProperty(ENTITY, HOST, host);
		}catch(CoreException e){
			logger.error("exec delete error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteAll() throws CoreException {
		try{
			return basicDao.deleteAll(CookieParam.class);
		}catch(CoreException e){
			logger.error("exec delete all error.",e);
			e.printStackTrace();
			throw e;
		}
	}

}
