package org.rency.crawler.core;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.rency.crawler.beans.CookieParam;
import org.rency.crawler.beans.TaskQueue;
import org.rency.crawler.service.CookieParamService;
import org.rency.crawler.service.TaskQueueService;
import org.rency.pushlet.beans.MessageQueue;
import org.rency.pushlet.service.MessageQueueService;
import org.rency.utils.common.CONST;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 查找、分析互联网URL超链接
 * @author T-rency
 * @date 2015年1月8日 下午3:53:42
 */
public class PageParser implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger(PageParser.class);
	
	private TaskQueueService taskQueueService;
	private CookieParamService cookieParamService;
	private MessageQueueService messageQueueService;
	private final TaskQueue taskQueue;
	private final CrawlerConfiguration cfg;
	
	public PageParser(CrawlerConfiguration crawlerConfiuration,TaskQueue taskQueue){
		this.cfg = crawlerConfiuration;
		this.taskQueue = taskQueue;
		this.taskQueueService = crawlerConfiuration.getTaskQueueService();
		this.cookieParamService = crawlerConfiuration.getCookieParamService();
		this.messageQueueService = crawlerConfiuration.getMessageQueueService();
	}

	@Override
	public void run(){
		try{
			this.cfg.setNewTaskDate(new Date());
			parse();
		}catch(SocketTimeoutException e){
			try {
				int timeout = taskQueueService.queryTaskQueueTimeout(taskQueue.getUrl());
				if(timeout < CONST.RETRY_COUNT){
					timeout++;
					taskQueue.setTimeout(timeout);
					taskQueue.setVisited(true);
					taskQueueService.updateTaskQueue(taskQueue);
					run();
				}
				logger.debug("get target address["+taskQueue.getUrl()+"] timeout:"+timeout);
			} catch (CoreException e1) {
				logger.error("query taskQueue["+taskQueue.getUrl()+"] timeout error."+e);
				e1.printStackTrace();
			}
		}catch(Exception e){
			logger.error("parse page["+taskQueue.getUrl()+"] error.",e);
			e.printStackTrace();
			messageQueueService.add(new MessageQueue(CONST.SERVICE_KIND_CRAWLER, this.cfg.getUser(),"发生未知错误"));
		}
	}
	
	/**
	 * @desc 解析页面
	 * @date 2015年1月8日 下午3:54:31
	 * @throws Exception
	 */
	private void parse() throws Exception{
		try{
			//判断URL是否已访问过
			if(taskQueueService.isVisited(taskQueue)){
				return ;
			}
			
			/**
			 * 获取已保存网站cookie
			 */
			CookieParam cookieParam = null;
			if(StringUtils.isNotBlank(taskQueue.getHost())){
				cookieParam = cookieParamService.query(taskQueue.getHost());
				logger.debug("query cookie["+cookieParam.toString()+"], and host is "+taskQueue.getHost());
			}
			
			//HttpClient组件请求Http
			HttpManager httpManager= this.cfg.getHttpManager();
			CloseableHttpResponse response = httpManager.execute(taskQueue,cookieParam);
			if(response == null){
				return;
			}
			//获取页面
			HttpEntity entity = response.getEntity();
			String html = EntityUtils.toString(entity);
			httpManager.closeResources(response);
			Document doc = Jsoup.parse(html);
			String uri = doc.baseUri();
			
			//保存cookie
			if(cookieParam ==null){
				setCookieParam(uri,cookieParam,httpManager.getCookieStore().getCookies());
			}
			
			/**
			 * 提取页面中Href超链接
			 */
			URLSearch.parseHref(cfg,doc);
			
			/**
			 * 提取表单的Action
			 */
			URLSearch.parseForm(cfg,doc);
			
			/**
			 * 解析执行Javascript代码
			 */
			URLSearch.parseScript(cfg,doc);
			
			//更新队列任务状态
			taskQueue.setHost(uri);
			taskQueue.setStatusCode(httpManager.getStatusCode());
			taskQueue.setVisited(true);
			taskQueueService.updateTaskQueue(taskQueue);
			
			/**
			 * 提交保存页面任务
			 */
			this.cfg.getSaveExecutor().execute(new PageSaver(this.cfg,taskQueue,doc));
			
		}catch(RejectedExecutionException e){
			logger.debug("crawler executor service force stoped...",e);
			messageQueueService.add(new MessageQueue(CONST.SERVICE_KIND_CRAWLER, this.cfg.getUser(), "服务被强制停止"));
			return;
		}catch(SocketTimeoutException e){
			throw e;
		}catch (UnknownHostException e) {
			logger.debug("Unknown Host."+e+", and give up connection "+taskQueue.getUrl());
			throw new SocketTimeoutException(e.getMessage());
		}catch(Exception e){
			logger.error("parse page["+taskQueue.getUrl()+"] error.",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 保存Cookie
	 * @date 2014年12月19日 下午2:48:52
	 * @param uri
	 * @param cookieParam
	 * @param cookies
	 * @throws CoreException
	 */
	private void setCookieParam(String uri,CookieParam cookieParam,List<Cookie> cookies) throws CoreException{
		if(StringUtils.isNotBlank(uri.trim()) || cookies.size() != 0){
			for(Cookie cookie : cookies){
				cookieParam = new CookieParam();
				cookieParam.setHost(uri);
				cookieParam.setCookieDomain(cookie.getDomain());
				cookieParam.setCookiePath(cookie.getPath());
				cookieParam.setCookieVersion(cookie.getVersion());
				cookieParam.setIsSecure(cookie.isSecure());
				cookieParam.setName(cookie.getName());
				cookieParam.setCookieValue(cookie.getValue());
				cookieParam.setPath(cookie.getPath());
				boolean isSave = cookieParamService.add(cookieParam);
				logger.debug("save cookie["+cookieParam.toString()+"] result:"+isSave);
			}
		}
	}

}
