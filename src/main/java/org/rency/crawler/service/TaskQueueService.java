package org.rency.crawler.service;

import org.rency.crawler.beans.TaskQueue;
import org.rency.utils.exceptions.CoreException;

public interface TaskQueueService {

	public boolean add(TaskQueue taskQueue) throws CoreException;
	
	public TaskQueue queryTopTaskQueue() throws CoreException;
	
	public TaskQueue queryDownloadTaskQueueWithOne() throws CoreException;
	
	public TaskQueue queryTaskQueueByUrl(String url) throws CoreException;
	
	public Integer queryTaskQueueCount() throws CoreException;
	
	public Integer queryTaskQueueTimeout(String url) throws CoreException;
	
	public TaskQueue queryByLastModifiedAndUrl(String url,String lastModified) throws CoreException;
	
	public boolean updateTaskQueue(TaskQueue taskQueue) throws CoreException;
	
	public boolean updateTaskQueueTimeout(TaskQueue taskQueue) throws CoreException;
	
	public boolean updateTaskQueueVisited(TaskQueue taskQueue) throws CoreException;
	
	public boolean updateTaskQueueDownload(TaskQueue taskQueue) throws CoreException;
	
	public boolean updateTaskQueueStatusCode(TaskQueue taskQueue)throws CoreException;
	
	public boolean deleteTaskQueue(String url) throws CoreException;
	
	public boolean deleteTaskQueueByCrawlerId(String crawlerId) throws CoreException;
	
	public boolean deleteAll() throws CoreException;
	
	/**
	 * @desc 判断页面是否已访问过
	 * @date 2014年11月3日 下午5:16:46
	 * @param taskQueue
	 * @return
	 * @throws CoreException
	 */
	public boolean isVisited(TaskQueue taskQueue) throws CoreException;
	
}