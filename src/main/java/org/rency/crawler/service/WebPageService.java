package org.rency.crawler.service;

import java.util.List;

import org.rency.crawler.beans.WebPage;
import org.rency.utils.exceptions.CoreException;

public interface WebPageService {

	public List<WebPage> list() throws CoreException;
	
	public List<WebPage> queryByUrl(String url) throws CoreException;
	
	public List<WebPage> queryByMD5(String md5) throws CoreException;
	
	public WebPage queryByUrlAndMD5(String url,String md5) throws CoreException;
	
	public WebPage queryUnIndexerWithOne() throws CoreException;
	
	public boolean add(WebPage page) throws CoreException;
	
	public boolean update(WebPage page) throws CoreException;
	
	public boolean deleteByUrl(String url) throws CoreException;
	
	public boolean deleteByMD5(String md5) throws CoreException;
	
	public boolean deleteAll() throws CoreException;
	
}