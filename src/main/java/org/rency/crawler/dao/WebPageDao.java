package org.rency.crawler.dao;

import java.util.List;

import org.rency.crawler.beans.WebPage;
import org.rency.utils.exceptions.CoreException;

public interface WebPageDao {

	public abstract List<WebPage> list() throws CoreException;
	
	public abstract List<WebPage> getByUrl(String url) throws CoreException;
	
	public abstract List<WebPage> getByMD5(String md5) throws CoreException;
	
	public abstract WebPage getByUrlAndMD5(String url,String md5) throws CoreException;
	
	public abstract WebPage getUnIndexerWithOne() throws CoreException;
	
	public abstract boolean save(WebPage page) throws CoreException;
	
	public abstract boolean update(WebPage page) throws CoreException;
	
	public abstract boolean deleteByUrl(String url) throws CoreException;
	
	public abstract boolean deleteByMD5(String md5) throws CoreException;
	
	public abstract boolean deleteAll() throws CoreException;
	
}