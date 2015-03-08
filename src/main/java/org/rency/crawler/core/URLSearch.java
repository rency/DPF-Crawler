package org.rency.crawler.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.rency.crawler.beans.TaskQueue;
import org.rency.crawler.utils.CrawlerDict;
import org.rency.utils.exceptions.CoreException;
import org.rency.utils.tools.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

public class URLSearch {
	
	private static final Logger logger = LoggerFactory.getLogger(URLSearch.class);
	
	/**
	 * @desc 提取页面中的超链接
	 * @date 2015年1月8日 下午3:45:33
	 * @param doc 页面
	 * @throws CoreException
	 */
	public static void parseHref(CrawlerConfiguration cfg,Document doc) throws CoreException{
		try{
			String host = doc.baseUri();
			Elements hrefs = doc.select("a[href]");
			for(Element ele : hrefs){
				String url = URLUtil.getFillUrl(ele.attr("abs:href").trim(), host);
				if(StringUtils.isBlank(url)){
					continue;
				}
				TaskQueue newTask = new TaskQueue(cfg.getCrawlerId(),host,url,CrawlerDict.METHOD_POST);
				commitTask(cfg,newTask);
			}
		}catch(Exception e){
			logger.error("解析页面提取Href时异常."+doc.html(),e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 提取表单中的action
	 * @date 2015年1月9日 上午11:05:32
	 * @param doc
	 * @throws CoreException
	 */
	public static void parseForm(CrawlerConfiguration cfg,Document doc) throws CoreException{
		try{
			String host = doc.baseUri();
			Elements forms = doc.select("form[action]");
			for(Element form : forms){
				String url = URLUtil.getFillUrl(form.attr("abs:action").trim(), host);
				if(StringUtils.isBlank(url)){
					continue;
				}
				
				Map<String, String> postParam = new HashMap<String, String>();
				
				//判断表单提交的方式
				int requestMethod = 0;
				String method = form.attr("method").trim().toUpperCase();
				if(HttpMethod.GET.equals(method)){
					requestMethod = CrawlerDict.METHOD_GET;
				}else if(HttpMethod.POST.equals(method)){
					requestMethod = CrawlerDict.METHOD_POST;
				}else{
					requestMethod = CrawlerDict.METHOD_GET;
				}
				
				Elements inputs = form.select("input");
				for(Element input : inputs){
					String key = input.attr("name");
					String value = input.attr("value");
					if(StringUtils.isBlank(key) || StringUtils.isBlank(value)){
						continue;
					}					
					postParam.put(key, value);
				}
				TaskQueue newTask = new TaskQueue(cfg.getCrawlerId(),host,url,requestMethod,HttpUtils.Map2String(postParam));
				commitTask(cfg,newTask);
			}
		}catch(Exception e){
			logger.error("解析页面表单Action时异常."+doc.html(),e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 解析、执行Javascript代码(执行Javascript代码，只需返回URL存入任务队列)
	 * @date 2015年1月8日 下午3:56:59
	 * @param cfg
	 * @param doc
	 * @throws CoreException 
	 */
	public static void parseScript(CrawlerConfiguration cfg,Document doc) throws CoreException{
		try{
			Elements scripts = doc.select("script");
			for(Element ele : scripts){
				if(ele.hasAttr("src")){
					String scriptHref = ele.attr("abs:src").trim();
					System.out.println(scriptHref);
				}else{
					String javascript = ele.html();
					System.out.println(javascript);
				}
			}
		}catch(Exception e){
			logger.error("解析页面提取JavaScript时异常."+doc.html(),e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 提交新的页面抓取任务
	 * @date 2015年1月9日 下午1:57:24
	 * @param cfg
	 * @param uri 域名
	 * @param href URL超链接
	 * @throws CoreException
	 */
	private static void commitTask(CrawlerConfiguration cfg,TaskQueue newTask) throws CoreException{
		try{
			boolean isSave = cfg.getTaskQueueService().add(newTask);
			logger.debug("add new taskQueue["+newTask.toString()+"] result:"+isSave);
			
			//提交新的页面解析任务
			cfg.getParserExecutor().execute(new PageParser(cfg,newTask));
			logger.debug("add new page parser task["+newTask.toString()+"]");
		}catch(Exception e){
			logger.error("提交新任务异常.taskQueue:"+newTask.toString(),e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
}