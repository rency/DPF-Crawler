package org.rency.crawler.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.rency.crawler.utils.CrawlerDict;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="t_crawler_task_queue")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class TaskQueue implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6004790568824598869L;

	private String crawlerId;
	
	private String url = "";
	
	private String host = "";
	
	private boolean isVisited;
	
	private boolean isDownload;
	
	private int statusCode;
	
	private int timeout;
	
	private String lastModified = "";
	
	private int requestMethod;
	
	private String httpParams = "";
	
	private Date execDate;
	
	/**
	 * 默认将会以get方式提交请求
	 */
	public TaskQueue(){
		this.execDate = new Date();
		this.isVisited = false;
		this.isDownload = false;
		this.timeout = 0;
		this.requestMethod = CrawlerDict.METHOD_GET;
	}
	
	public TaskQueue(String crawlerId,String url,int requestMethod){
		this.crawlerId = crawlerId;
		this.url=url;
		this.execDate = new Date();
		this.isVisited = false;
		this.isDownload = false;
		this.timeout = 0;
		this.requestMethod = requestMethod;
	}
	
	public TaskQueue(String crawlerId,String host,String url,int requestMethod){
		this.crawlerId = crawlerId;
		this.host = host;
		this.url=url;
		this.execDate = new Date();
		this.isVisited = false;
		this.isDownload = false;
		this.timeout = 0;
		this.requestMethod = requestMethod;
	}
	
	public TaskQueue(String crawlerId,String host,String url,int requestMethod,String httpParams){
		this.crawlerId = crawlerId;
		this.host = host;
		this.url=url;
		this.execDate = new Date();
		this.isVisited = false;
		this.isDownload = false;
		this.timeout = 0;
		this.requestMethod = requestMethod;
		this.httpParams = httpParams;
	}

	public String getCrawlerId() {
		return crawlerId;
	}

	public void setCrawlerId(String crawlerId) {
		this.crawlerId = crawlerId;
	}

	@Id
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean isDownload) {
		this.isDownload = isDownload;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public int getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(int requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getHttpParams() {
		return httpParams;
	}

	public void setHttpParams(String httpParams) {
		this.httpParams = httpParams;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	
	public String toString(){
		return "{crawlerId:"+crawlerId+", url:"+url+", host:"+host+", isVisited:"+isVisited+", isDownload:"+isDownload+", statusCode:"+statusCode+", timeout:"+timeout+", lastModified:"+lastModified+", requestMethod:"+requestMethod+", httpParams:"+httpParams+", execDate:"+execDate+"}";
	}
	
}