package org.rency.crawler.dao;

import java.util.List;

import org.rency.crawler.beans.CookieParam;
import org.rency.utils.exceptions.CoreException;

public interface CookieParamDao {

	public abstract List<CookieParam> list() throws CoreException;
	
	public abstract CookieParam get(String host) throws CoreException;
	
	public abstract boolean save(CookieParam cookie) throws CoreException;
	
	public abstract boolean update(CookieParam cookie) throws CoreException;
	
	public abstract boolean delete(String host) throws CoreException;
	
	public abstract boolean deleteAll() throws CoreException;
	
}