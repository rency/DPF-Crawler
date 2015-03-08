package org.rency.crawler.core;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class URLUtil {

	/**
	 * @description:获取完整的URL路径
	* @author Administrator
	* @date 2015年1月18日 下午2:33:45
	* @param href URL路径
	* @param host 域名
	* @return
	* @throws
	 */
	public static String getFillUrl(String href,String host){
		if(StringUtils.isBlank(href) || StringUtils.isBlank(host)){
			return "";
		}
		if(href.startsWith("javascript:") || href.startsWith("mailto:") || href.startsWith("#") || href.startsWith(host+"#")){
			return "";
		}
		Matcher matcher = Pattern.compile("((https|http|ftp|rtsp|mms)?://)").matcher(href);
		if(!matcher.find()){
			href = host + href;
		}
		if(href.startsWith("/")){
			href = host + href;
		}
		return href;
	}
	
	public static String getHost(String url){
		String host = "";
		String protocal = url.substring(0, url.indexOf(":/"));
		if(url.startsWith("http://localhost") || url.startsWith("http://127.0.0.1")){
			
		}
		//String s = "http://download.csdn.net/detail/u011921945/8238127";
		String s = "http://localhost:8080/dpf/mcanj";
		Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
		Matcher m = p.matcher(s);
		if(m.find()){
			host = m.group();
		}
		return host;
	}
	
	public static void main(String[] args) {
		System.out.println(getHost("http://localhost:8080/dpf/mcanj"));
	}
	
}