package org.rency.crawler.dao.impl;

import java.util.List;

import org.rency.crawler.beans.WebPage;
import org.rency.crawler.dao.WebPageDao;
import org.rency.utils.dao.BasicDao;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("webPageDao")
public class WebPageDaoImpl implements WebPageDao {

	private static final Logger logger = LoggerFactory.getLogger(WebPageDaoImpl.class);
	
	@Autowired
	@Qualifier("hibernateDao")
	private BasicDao basicDao;
	
	private final String ENTITY = WebPage.class.getName();
	private final String URL = "url";
	private final String CONTENT_MD5 = "contentMD5";
	private final String IS_CREATE_INDEX = "isCreateIndex";
	private final String EXECDATE = "execDate";
	
	@Override
	public List<WebPage> list() throws CoreException {
		try{
			return basicDao.loadAll(WebPage.class);
		}catch(CoreException e){
			logger.error("exec list error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<WebPage> getByUrl(String url) throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+URL+"=? order by model."+EXECDATE;
		try{
			List<WebPage> list = basicDao.findByHQL(queryString, url);
			return list;
		}catch(CoreException e){
			logger.error("exec getByUrl["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<WebPage> getByMD5(String md5) throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+CONTENT_MD5+"=? order by model."+EXECDATE;
		try{
			List<WebPage> list = basicDao.findByHQL(queryString, md5);
			return list;
		}catch(CoreException e){
			logger.error("exec getByMD5["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public WebPage getByUrlAndMD5(String url, String md5) throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+URL+"=? and model."+CONTENT_MD5+"=?";
		try{
			List<WebPage> list = basicDao.findByHQL(queryString, new Object[]{url,md5});
			if(list != null && list.size() > 0){
				return list.get(0);
			}
			return null;
		}catch(CoreException e){
			logger.error("exec getByUrlAndMD5["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public WebPage getUnIndexerWithOne() throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+IS_CREATE_INDEX+"=? order by asc model."+EXECDATE+" limit 1";
		try{
			List<WebPage> list = basicDao.findByHQL(queryString, false);
			if(list != null && list.size() > 0){
				return list.get(0);
			}
			return null;
		}catch(CoreException e){
			logger.error("exec getUnIndexer["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean save(WebPage page) throws CoreException {
		try{
			return basicDao.save(page);
		}catch(CoreException e){
			logger.error("exec save error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean update(WebPage page) throws CoreException {
		try{
			return basicDao.update(page);
		}catch(CoreException e){
			logger.error("exec save error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteByUrl(String url) throws CoreException {
		try{
			return basicDao.deleteByProperty(ENTITY, URL, url);
		}catch(CoreException e){
			logger.error("exec deleteByUrl error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteByMD5(String md5) throws CoreException {
		try{
			return basicDao.deleteByProperty(ENTITY, CONTENT_MD5, md5);
		}catch(CoreException e){
			logger.error("exec deleteByMD5 error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteAll() throws CoreException {
		try{
			return basicDao.deleteAll(WebPage.class);
		}catch(CoreException e){
			logger.error("exec deleteAll error.",e);
			e.printStackTrace();
			throw e;
		}
	}

}
