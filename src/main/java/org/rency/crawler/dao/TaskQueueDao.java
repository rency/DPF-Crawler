package org.rency.crawler.dao;

import org.rency.crawler.beans.TaskQueue;
import org.rency.utils.exceptions.CoreException;

public interface TaskQueueDao {

	public abstract boolean save(TaskQueue taskQueue) throws CoreException;
	
	public abstract TaskQueue getTaskQueueByUrl(String url) throws CoreException;
	
	public abstract TaskQueue getTopTaskQueue() throws CoreException;
	
	public abstract Integer getTaskQueueCount() throws CoreException;
	
	public abstract Integer getTaskQueueTimeout(String url) throws CoreException;
	
	public abstract TaskQueue getDownloadTaskQueueWithOne() throws CoreException;
	
	public abstract TaskQueue getByUrlAndLastModified(String url,String lastModified) throws CoreException;
	
	public abstract boolean updateTaskQueue(TaskQueue taskQueue) throws CoreException;
	
	public abstract boolean updateTaskQueueTimeout(TaskQueue taskQueue) throws CoreException;
	
	public abstract boolean updateTaskQueueVisited(TaskQueue taskQueue) throws CoreException;
	
	public abstract boolean updateTaskQueueDownload(TaskQueue taskQueue) throws CoreException;
	
	/**
	 * @desc 更改Http 状态码
	 * @date 2014年10月27日 上午11:11:42
	 * @param taskQueue
	 * @return
	 * @throws CoreException
	 */
	public abstract boolean updateTaskQueueStatusCode(TaskQueue taskQueue) throws CoreException;
	
	public abstract boolean deleteTaskQueue(String url) throws CoreException;
	
	public boolean deleteTaskQueueByCrawlerId(String crawlerId) throws CoreException;
	
	public abstract boolean deleteAll() throws CoreException;
	
}