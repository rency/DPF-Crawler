package org.rency.crawler.beans;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="t_crawler_web_page")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class WebPage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date execDate;
	private String url;
	private int dataLength;
	private String html;
	private String content;
	private String contentMD5;
	private String charset;
	private String title;
	private String keywords;
	private String description;
	private String lastModified;
	private boolean isCreateIndex;
	
	public WebPage(){
		this.execDate = new Date();
		this.isCreateIndex = false;
	}

	@Id
	public String getUrl() {
		return url;
	}
	
	@Id
	public String getContentMD5() {
		return contentMD5;
	}
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getExecDate() {
		return execDate;
	}
	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	
	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public void setContentMD5(String contentMD5) {
		this.contentMD5 = contentMD5;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isCreateIndex() {
		return isCreateIndex;
	}

	public void setCreateIndex(boolean isCreateIndex) {
		this.isCreateIndex = isCreateIndex;
	}

	public String toString(){
		return "{execDate:"+execDate+", url:"+url+", dataLength:"+dataLength+", html:"+html+", content:"+content+", contentMD5:"+contentMD5+", charset:"+charset+", title:"+title+", keywords:"+keywords+", description:"+description+", lastModified:"+lastModified+", isCreateIndex:"+isCreateIndex+"}";
	}
}
