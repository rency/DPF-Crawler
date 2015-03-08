package org.rency.crawler.core;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.rency.crawler.service.CookieParamService;
import org.rency.crawler.service.TaskQueueService;
import org.rency.crawler.service.WebPageService;
import org.rency.pushlet.service.MessageQueueService;
import org.rency.utils.common.SpringContextHolder;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 配置爬虫
 * @author T-rency
 * @date 2014年11月21日 下午2:47:42
 */
public class CrawlerConfiguration {

	private static final Logger logger = LoggerFactory.getLogger(CrawlerConfiguration.class);
	private TaskQueueService taskQueueService = SpringContextHolder.getBean(TaskQueueService.class);
	private WebPageService webPageService = SpringContextHolder.getBean(WebPageService.class);
	private CookieParamService cookieParamService = SpringContextHolder.getBean(CookieParamService.class);
	private MessageQueueService messageQueueService = SpringContextHolder.getBean(MessageQueueService.class);
	
	/**
	 * 爬虫标识
	 */
	private String crawlerId;
	/**
	 * 搜索线程池
	 */
	private ExecutorService parserExecutor;
	/**
	 * 保存线程池
	 */
	private ExecutorService saveExecutor;
	/**
	 * 访问网络工具类
	 */
	private HttpManager httpManager;
	/**
	 * 每个线程池的容量
	 */
	private int poolSize;
	/**
	 * 爬虫起始访问地址
	 */
	private String initAddr;	
	/**
	 * 操作者
	 */
	private String user;
	
	/**
	 * 线程池新任务添加时间
	 */
	private volatile Date newTaskDate;
	
	/**
	 * @desc 新建爬虫配置
	 * @param crawlerId 爬虫标识
	 * @param poolSize 每个线程池的容量
	 * @param initAddr 爬虫起始访问地址
	 * @throws CoreException 
	 */
	public CrawlerConfiguration(String crawlerId,int poolSize,String initAddr,String user) throws CoreException{
		this.crawlerId = crawlerId;
		this.poolSize = poolSize;
		this.initAddr = initAddr;
		this.user = user;
		this.init();
	}
	
	/**
	 * @desc 初始化Crawler配置
	 * @date 2014年11月21日 下午2:35:32
	 * @throws CoreException
	 */
	private void init() throws CoreException{
		try{
			logger.info("init cralwer["+crawlerId+"] configuration...");
			this.httpManager = new HttpManager();
			this.parserExecutor = Executors.newFixedThreadPool(this.poolSize);
			this.saveExecutor = Executors.newFixedThreadPool(this.poolSize);
		}catch(Exception e){
			logger.error("init crawler["+crawlerId+"] configuration error."+e);
			e.printStackTrace();
			throw new CoreException("init crawler["+crawlerId+"] configuration error."+e);
		}
		
	}

	/**
	 * @desc 爬虫标识
	 * @date 2014年11月24日 下午4:10:25
	 * @return
	 */
	public String getCrawlerId() {
		return crawlerId;
	}

	/**
	 * @desc 搜索线程池
	 * @date 2014年11月24日 下午4:10:36
	 * @return
	 */
	public ExecutorService getParserExecutor() {
		return parserExecutor;
	}

	/**
	 * @desc 保存线程池
	 * @date 2014年11月24日 下午4:10:47
	 * @return
	 */
	public ExecutorService getSaveExecutor() {
		return saveExecutor;
	}

	/**
	 * @desc 访问网络工具
	 * @date 2014年11月24日 下午4:10:58
	 * @return
	 */
	public HttpManager getHttpManager() {
		return httpManager;
	}

	/**
	 * @desc 每个线程池的容量
	 * @date 2014年11月24日 下午4:11:12
	 * @return
	 */
	public int getPoolSize() {
		return poolSize;
	}

	/**
	 * @desc 爬虫起始访问地址
	 * @date 2014年11月24日 下午4:11:21
	 * @return
	 */
	public String getInitAddr() {
		return initAddr;
	}
	
	/**
	 * @desc 爬虫操作者
	 * @date 2014年12月3日 上午10:54:38
	 * @return
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * @desc 获取线程池新任务添加的时间
	 * @date 2014年12月29日 上午10:28:49
	 * @return
	 */
	public Date getNewTaskDate() {
		return newTaskDate;
	}

	public void setNewTaskDate(Date newTaskDate) {
		this.newTaskDate = newTaskDate;
	}

	public TaskQueueService getTaskQueueService() {
		return taskQueueService;
	}

	public WebPageService getWebPageService() {
		return webPageService;
	}

	public CookieParamService getCookieParamService() {
		return cookieParamService;
	}

	public MessageQueueService getMessageQueueService() {
		return messageQueueService;
	}
}