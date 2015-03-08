package org.rency.crawler.core;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.rency.crawler.beans.CookieParam;
import org.rency.crawler.beans.TaskQueue;
import org.rency.crawler.utils.CrawlerDict;
import org.rency.utils.exceptions.CoreException;
import org.rency.utils.exceptions.NotModifiedException;
import org.rency.utils.tools.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpManager {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpManager.class);
	
	private final int timeout = 5000;
	private final int maxTotal = 100;
        
    private HttpClientContext context = HttpClientContext.create();
    
    private PoolingHttpClientConnectionManager cm;
    
    private CookieStore cookieStore;
    
    private CloseableHttpClient httpClient;
    
    private RequestConfig requestConfig;
    
    private int statusCode;
    
    public HttpManager(){
    	if(cookieStore == null){
    		cookieStore = new BasicCookieStore();
    	}
    	
    	if(cm == null){
    		cm = new PoolingHttpClientConnectionManager();
    		cm.setMaxTotal(maxTotal);
    	}
    	
    	if(httpClient == null){
    		httpClient = HttpClients.custom()
    				.setConnectionManager(cm)
    				.setRedirectStrategy(new LaxRedirectStrategy())
    				.setDefaultCookieStore(cookieStore).build();
    	}    	
    	
    	if(requestConfig == null){
    		requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build();
    	}
    	
    }
    
    /**
     * @desc 发送http请求
     * @date 2015年1月9日 下午3:59:17
     * @param taskQueue
     * @param cookieParam
     * @return
     * @throws Exception
     */
    public CloseableHttpResponse execute(TaskQueue taskQueue,CookieParam cookieParam)throws Exception{
    	try{
			//设置cookie
			setCookie(cookieParam);
			
			if(CrawlerDict.METHOD_POST == taskQueue.getRequestMethod()){
				return post(taskQueue);
			}else{
				return get(taskQueue);
			}
		}catch(Exception e){
			logger.error("executing get request url["+taskQueue.getUrl()+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
    }
    
    /**
     * @desc 以get方式发送请求
     * @date 2015年1月9日 下午2:34:34
     * @param taskQueue
     * @return
     * @throws Exception
     */
	private CloseableHttpResponse get(TaskQueue taskQueue) throws Exception{
		try{
			HttpGet httpget = new HttpGet(taskQueue.getUrl());
			logger.debug("executing url["+taskQueue.getUrl()+"] start.");
	        httpget.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(httpget, context);
			statusCode = response.getStatusLine().getStatusCode();
			logger.debug("execute url["+taskQueue.getUrl()+"] finish. Status Code:"+statusCode);
			if(HttpUtils.httpResponseStatus(statusCode)){
				return response;
			}else{
				return null;
			}
		}catch(NotModifiedException e){
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			return null;
		}catch (UnknownHostException e) {
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			throw new SocketTimeoutException(e.toString());
		}catch (NoHttpResponseException e) {
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			throw e;
		}catch(SocketTimeoutException e){
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			throw e;
		}catch(RuntimeException e){
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			e.printStackTrace();
			return null;
		}catch(ClientProtocolException e){
			logger.error("executing get request url["+taskQueue.getUrl()+"].",e);
			e.printStackTrace();
			throw new SocketTimeoutException(e.getMessage());
		}catch(IOException e){
			logger.error("executing get request url["+taskQueue.getUrl()+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}catch(Exception e){
			logger.error("executing get request url["+taskQueue.getUrl()+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 以post方式发送请求
	 * @date 2015年1月9日 下午2:35:04
	 * @param taskQueue
	 * @return
	 * @throws Exception
	 */
	private CloseableHttpResponse post(TaskQueue taskQueue) throws Exception{
		try{
			logger.debug("executing url["+taskQueue.getUrl()+"] start. and post param is:"+taskQueue.getHttpParams());
			Map<String, String> postMap = HttpUtils.String2Map(taskQueue.getHttpParams());
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			for(String key : postMap.keySet()){
				postParams.add(new BasicNameValuePair(key, postMap.get(key)));
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(postParams);
			HttpPost post = new HttpPost(taskQueue.getUrl());
			post.setEntity(entity);
			post.setConfig(requestConfig);
			CloseableHttpResponse response = httpClient.execute(post, context);
			statusCode = response.getStatusLine().getStatusCode();
			logger.debug("execute url["+taskQueue.getUrl()+"] succ. Status Code:"+statusCode);
			if(HttpUtils.httpResponseStatus(statusCode)){
				return response;
			}else{
				return null;
			}
		}catch(NotModifiedException e){
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			return null;
		}catch (UnknownHostException e) {
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			throw new SocketTimeoutException(e.toString());
		}catch (NoHttpResponseException e) {
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			throw e;
		}catch(SocketTimeoutException e){
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			throw e;
		}catch(RuntimeException e){
			logger.debug("connection["+taskQueue.getUrl()+"] error.",e);
			e.printStackTrace();
			return null;
		}catch(ClientProtocolException e){
			logger.error("executing post url["+taskQueue.getUrl()+"].",e);
			e.printStackTrace();
			throw new SocketTimeoutException(e.getMessage());
		}catch(IOException e){
			logger.error("executing post url["+taskQueue.getUrl()+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}catch(Exception e){
			logger.error("executing post url["+taskQueue.getUrl()+"].",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 设置cookie请求参数
	 * @date 2014年11月4日 下午3:40:11
	 * @param cookieParam
	 * @return
	 * @throws CoreException
	 */
	private void setCookie(final CookieParam cookieParam) throws CoreException{
		try{
			if(cookieParam != null ){
				Cookie cookie = new ClientCookie() {
					
					@Override
					public boolean isSecure() {
						return cookieParam.getIsSecure();
					}
					
					@Override
					public boolean isPersistent() {
						return false;
					}
					
					@Override
					public boolean isExpired(Date arg0) {
						return false;
					}
					
					@Override
					public int getVersion() {
						return cookieParam.getCookieVersion();
					}
					
					@Override
					public String getValue() {
						return cookieParam.getCookieValue();
					}
					
					@Override
					public int[] getPorts() {
						return null;
					}
					
					@Override
					public String getPath() {
						return cookieParam.getPath();
					}
					
					@Override
					public String getName() {
						return cookieParam.getName();
					}
					
					@Override
					public Date getExpiryDate() {
						return null;
					}
					
					@Override
					public String getDomain() {
						return cookieParam.getCookieDomain();
					}
					
					@Override
					public String getCommentURL() {
						return null;
					}
					
					@Override
					public String getComment() {
						return null;
					}
					
					@Override
					public String getAttribute(String arg0) {
						return null;
					}
					
					@Override
					public boolean containsAttribute(String arg0) {
						return false;
					}
				}; 
				cookieStore.addCookie(cookie);
			}
			context.setCookieStore(cookieStore);
		}catch(Exception e){
			logger.error("setCookie["+cookieParam+"] error.",e);
			e.printStackTrace();
			throw new CoreException(e);
		}
	}
	
	/**
	 * @desc 释放response资源
	 * @date 2015年1月9日 下午2:58:09
	 * @param response
	 * @throws CoreException
	 */
	public void closeResources(CloseableHttpResponse response) throws CoreException{
		try{
			if(response != null){
				response.close();
			}
		}catch(Exception e){
			logger.error("close http resources error.",e);
			e.printStackTrace();
			throw new CoreException("close http resources error."+e);
		}
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
