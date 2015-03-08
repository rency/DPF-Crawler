package org.rency.crawler.service;

import java.util.List;

import org.rency.crawler.core.CrawlerConfiguration;
import org.rency.utils.exceptions.CoreException;

public interface CrawlerService {
	
	/**
	 * @desc 启动爬虫
	 * @date 2014年11月24日 下午3:43:01
	 * @param crawlerId
	 * @return
	 * @throws CoreException
	 */
	public boolean start(CrawlerConfiguration ccf) throws CoreException;
	
	/**
	 * @desc 停止爬虫
	 * @date 2014年11月24日 下午3:43:10
	 * @param crawlerId
	 * @return
	 * @throws CoreException
	 */
	public boolean stop(String crawlerId) throws CoreException;
	
	/**
	 * @desc 爬虫状态
	 * @date 2014年11月24日 下午3:43:17
	 * @param crawlerId
	 * @return
	 * @throws CoreException
	 */
	public boolean status(String crawlerId) throws CoreException;
	
	/**
	 * @desc 获取存放爬虫的集合
	 * @date 2014年11月26日 上午9:45:03
	 * @return
	 * @throws CoreException
	 */
	public List<CrawlerConfiguration> getCrawlerList() throws CoreException;
	
}