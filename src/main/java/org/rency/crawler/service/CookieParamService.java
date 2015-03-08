package org.rency.crawler.service;

import java.util.List;

import org.rency.crawler.beans.CookieParam;
import org.rency.utils.exceptions.CoreException;

public interface CookieParamService {

	public List<CookieParam> list() throws CoreException;
	
	public CookieParam query(String host) throws CoreException;
	
	public boolean add(CookieParam cookie) throws CoreException;
	
	public boolean update(CookieParam cookie) throws CoreException;
	
	public boolean delete(String host) throws CoreException;
	
	public boolean deleteAll() throws CoreException;
	
}