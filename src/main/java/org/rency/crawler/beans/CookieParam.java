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
@Table(name="t_crawler_cookie_param")
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
public class CookieParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2116620442965932043L;

	private String host;
	private String name;
	private String cookieValue;
	private String cookieDomain;
	private String cookiePath;
	private boolean isSecure;
	private int cookieVersion;
	private String path;
	
	private Date execDate;

	public CookieParam(){
		this.execDate = new Date();
	}
	
	public CookieParam(String host){
		this.host = host;
		this.execDate = new Date();
	}
	
	@Id
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCookieValue() {
		return cookieValue;
	}

	public void setCookieValue(String cookieValue) {
		this.cookieValue = cookieValue;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public String getCookiePath() {
		return cookiePath;
	}

	public void setCookiePath(String cookiePath) {
		this.cookiePath = cookiePath;
	}

	public boolean getIsSecure() {
		return isSecure;
	}

	public void setIsSecure(boolean isSecure) {
		this.isSecure = isSecure;
	}

	public int getCookieVersion() {
		return cookieVersion;
	}

	public void setCookieVersion(int cookieVersion) {
		this.cookieVersion = cookieVersion;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	public Date getExecDate() {
		return execDate;
	}

	public void setExecDate(Date execDate) {
		this.execDate = execDate;
	}
	
	public String toString(){
		return "{host:"+host+", name:"+name+", cookieValue:"+cookieValue+", cookieDomain:"+cookieDomain+", cookiePath:"+cookiePath+", isSecure:"+isSecure+", cookieVersion:"+cookieVersion+", path:"+path+", execDate:"+execDate+"}";
	}
	
}