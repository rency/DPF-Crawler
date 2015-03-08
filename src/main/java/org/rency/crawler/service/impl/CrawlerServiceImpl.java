package org.rency.crawler.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rency.crawler.beans.TaskQueue;
import org.rency.crawler.core.CrawlerConfiguration;
import org.rency.crawler.core.PageParser;
import org.rency.crawler.service.CrawlerService;
import org.rency.crawler.utils.CrawlerDict;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("crawlerService")
public class CrawlerServiceImpl implements CrawlerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CrawlerServiceImpl.class);
	
	/**
	 * 存放爬虫的集合
	 */
	private List<CrawlerConfiguration> crawlerList = Collections.synchronizedList(new ArrayList<CrawlerConfiguration>());

	@Override
	public boolean start(CrawlerConfiguration ccf) throws CoreException {
		crawlerList.add(ccf);
		try{
			if(ccf.getParserExecutor() == null || ccf.getSaveExecutor() == null || ccf.getHttpManager() == null){
				logger.debug("please init crawler configuration when run crawler before....");
				throw new RuntimeException("please init crawler configuration when run crawler before.");
			}
			TaskQueue taskQueue = new TaskQueue(ccf.getCrawlerId(),ccf.getInitAddr(),CrawlerDict.METHOD_GET);
			ccf.getParserExecutor().execute(new PageParser(ccf,taskQueue));			
			return true;
		}catch(Exception e){
			logger.error("start crawler["+ccf.getCrawlerId()+"] service exception."+e);
			e.printStackTrace();
			throw new CoreException("start crawler["+ccf.getCrawlerId()+"] service exception."+e);
		}
	}

	@Override
	public boolean stop(String crawlerId) throws CoreException {
		try{
			logger.info("stop crawler service...");
			CrawlerConfiguration removeConfiguration = null;
			for(CrawlerConfiguration crawler : crawlerList){
				if(crawler.getCrawlerId().equals(crawlerId)){
					removeConfiguration = crawler;
					crawler.getParserExecutor().shutdown();
					crawler.getSaveExecutor().shutdown();
				}
			}
			if(removeConfiguration != null){
				crawlerList.remove(removeConfiguration);
			}
			return true;
		}catch(Exception e){
			logger.error("stop crawler service exception."+e);
			e.printStackTrace();
			throw new CoreException("stop crawler service exception."+e);
		}
	}

	@Override
	public boolean status(String crawlerId) throws CoreException {
		try{
			logger.info("query crawler["+crawlerId+"] status service status...");
			for(CrawlerConfiguration config : crawlerList){
				if(config.getCrawlerId().equals(crawlerId)){
					if(config.getParserExecutor().isShutdown() && config.getSaveExecutor().isShutdown()){
						return false;
					}else{
						return true;
					}
				}
			}
			return false;
		}catch(Exception e){
			logger.error("query crawler status service exception."+e);
			e.printStackTrace();
			throw new CoreException("query crawler status service exception."+e);
		}
	}

	@Override
	public List<CrawlerConfiguration> getCrawlerList() throws CoreException {
		return crawlerList;
	}
	
}
