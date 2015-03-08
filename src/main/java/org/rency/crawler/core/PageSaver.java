package org.rency.crawler.core;

import java.net.SocketTimeoutException;
import java.util.Date;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.rency.crawler.beans.TaskQueue;
import org.rency.crawler.beans.WebPage;
import org.rency.crawler.service.TaskQueueService;
import org.rency.crawler.service.WebPageService;
import org.rency.utils.common.CONST;
import org.rency.utils.exceptions.CoreException;
import org.rency.utils.tools.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @desc 保存队列任务内容
 * @author T-rency
 * @date 2015年1月8日 下午3:53:16
 */
public class PageSaver implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(PageSaver.class);
	
	private WebPageService webPageService;
	private TaskQueueService taskQueueService;
	private final CrawlerConfiguration crawlerConfiuration;

	private TaskQueue taskQueue;
	private Document doc;
	private int retryCount = 0;
	
	public PageSaver(CrawlerConfiguration crawlerConfiuration,TaskQueue taskQueue,Document document) {
		this.crawlerConfiuration = crawlerConfiuration;
		this.webPageService = crawlerConfiuration.getWebPageService();
		this.taskQueueService = crawlerConfiuration.getTaskQueueService();
		this.taskQueue = taskQueue;
		this.doc = document;
	}

	@Override
	public void run() {
		try{
			this.crawlerConfiuration.setNewTaskDate(new Date());
			if(doc == null){
				return;
			}
			savePage();
		}catch(SocketTimeoutException e){
			if(retryCount < CONST.RETRY_COUNT){
				retryCount++;
				run();
			}else{
				return;
			}	
		}catch(Exception e){
			logger.error("页面保存服务异常.",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * @desc 保存队列任务页面内容 
	 * @date 2014年10月28日 下午3:10:13
	 * @throws Exception
	 */
	private void savePage() throws Exception{
		try{
			WebPage webPage = new WebPage();
			Element head = doc.head();
			Elements metas = head.select("meta[http-equiv]");
			for(Element ele : metas){
				String name = ele.attr("http-equiv");
				String content = ele.attr("content");
				if(name.equals("keywords")){
					webPage.setKeywords(content);
				}
				if(name.equals("description")){
					webPage.setDescription(content);
				}
			}
			webPage.setTitle(doc.title());
			String pageContent = doc.text();
			webPage.setHtml(doc.html());
			webPage.setContent(pageContent);
			webPage.setDataLength(pageContent.length());
			webPage.setUrl(taskQueue.getUrl());
			webPage.setContentMD5(MD5Utils.getMD5String(pageContent));
			boolean isSave = webPageService.add(webPage);
			logger.debug("save page["+webPage.getUrl()+"] result:"+isSave);
			
			//更新队列任务状态为已下载
			updateTaskQueueDownload();
		}catch (Exception e) {
			logger.error("saver page["+taskQueue.getUrl()+"] error.",e);
			taskQueue.setDownload(false);
			updateTaskQueueDownload();
		}
	}
	
	/**
	 * @desc 更新队列任务状态为已下载
	 * @date 2015年1月8日 下午3:51:54
	 * @throws CoreException
	 */
	private void updateTaskQueueDownload() throws CoreException{
		try {
			taskQueue.setDownload(true);
			boolean isUpdate = taskQueueService.updateTaskQueueDownload(taskQueue);
			logger.debug("update task queue["+taskQueue.getUrl()+"] download status result:"+isUpdate);
		} catch (CoreException e) {
			logger.error("update task["+taskQueue.getUrl()+"] download status.",e);
			e.printStackTrace();
			throw e;
		}
	}
}
