package org.rency.crawler.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.rency.crawler.beans.WebPage;
import org.rency.crawler.dao.WebPageDao;
import org.rency.crawler.service.WebPageService;
import org.rency.utils.exceptions.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("webPageService")
@Scope("singleton")
public class WebPageServiceImpl implements WebPageService {

	@Autowired
	@Resource(name="webPageDao")
	private WebPageDao webPageDao;
	
	@Override
 	public List<WebPage> list() throws CoreException {
		return webPageDao.list();
	}

	@Override
	public List<WebPage> queryByUrl(String url) throws CoreException {
		return webPageDao.getByUrl(url);
	}

	@Override
	public List<WebPage> queryByMD5(String md5) throws CoreException {
		return webPageDao.getByMD5(md5);
	}

	@Override
	public boolean add(WebPage page) throws CoreException {
		WebPage webPage = queryByUrlAndMD5(page.getUrl(),page.getContentMD5());
		if(webPage != null){
			return false;
		}else{
			return webPageDao.save(page);
		}
	}

	@Override
	public boolean update(WebPage page) throws CoreException {
		return webPageDao.update(page);
	}

	@Override
	public boolean deleteByUrl(String url) throws CoreException {
		return webPageDao.deleteByUrl(url);
	}

	@Override
	public boolean deleteByMD5(String md5) throws CoreException {
		return webPageDao.deleteByMD5(md5);
	}
	
	@Override
	public boolean deleteAll() throws CoreException {
		return webPageDao.deleteAll();
	}
	
	@Override
	public WebPage queryByUrlAndMD5(String url, String md5) throws CoreException {
		return webPageDao.getByUrlAndMD5(url, md5);
	}
	
	@Override
	public WebPage queryUnIndexerWithOne() throws CoreException {
		return webPageDao.getUnIndexerWithOne();
	}
}
