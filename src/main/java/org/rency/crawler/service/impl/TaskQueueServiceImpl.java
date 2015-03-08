package org.rency.crawler.service.impl;

import javax.annotation.Resource;

import org.rency.crawler.beans.TaskQueue;
import org.rency.crawler.dao.TaskQueueDao;
import org.rency.crawler.service.TaskQueueService;
import org.rency.utils.exceptions.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("taskQueueService")
public class TaskQueueServiceImpl implements TaskQueueService {

	@Autowired
	@Resource(name="taskQueueDao")
	private TaskQueueDao taskQueueDao;
	
	@Override
	public boolean add(TaskQueue taskQueue) throws CoreException {
		TaskQueue tq = queryTaskQueueByUrl(taskQueue.getUrl());
		if(tq == null){
			return taskQueueDao.save(taskQueue);
		}else{
			return false;
		}
		
	}

	@Override
	public TaskQueue queryTaskQueueByUrl(String url) throws CoreException {
		return taskQueueDao.getTaskQueueByUrl(url);
	}

	@Override
	public Integer queryTaskQueueCount() throws CoreException {
		return taskQueueDao.getTaskQueueCount();
	}

	@Override
	public boolean updateTaskQueue(TaskQueue taskQueue) throws CoreException {
		return taskQueueDao.updateTaskQueue(taskQueue);
	}
	
	@Override
	public boolean updateTaskQueueTimeout(TaskQueue taskQueue) throws CoreException {
		return taskQueueDao.updateTaskQueueTimeout(taskQueue);
	}

	@Override
	public boolean updateTaskQueueVisited(TaskQueue taskQueue) throws CoreException {
		return taskQueueDao.updateTaskQueueVisited(taskQueue);
	}

	@Override
	public boolean updateTaskQueueDownload(TaskQueue taskQueue) throws CoreException {
		return taskQueueDao.updateTaskQueueDownload(taskQueue);
	}

	@Override
	public boolean deleteTaskQueue(String url) throws CoreException {
		return taskQueueDao.deleteTaskQueue(url);
	}

	@Override
	public TaskQueue queryTopTaskQueue() throws CoreException {
		return taskQueueDao.getTopTaskQueue();
	}

	@Override
	public Integer queryTaskQueueTimeout(String url) throws CoreException {
		return taskQueueDao.getTaskQueueTimeout(url);
	}

	@Override
	public boolean deleteAll() throws CoreException {
		return taskQueueDao.deleteAll();
	}

	@Override
	public boolean updateTaskQueueStatusCode(TaskQueue taskQueue) throws CoreException {
		return taskQueueDao.updateTaskQueueStatusCode(taskQueue);
	}
	
	@Override
	public TaskQueue queryDownloadTaskQueueWithOne() throws CoreException {
		return taskQueueDao.getDownloadTaskQueueWithOne();
	}
	
	@Override
	public TaskQueue queryByLastModifiedAndUrl(String url, String lastModified)throws CoreException {
		return taskQueueDao.getByUrlAndLastModified(url, lastModified);
	}
	
	@Override
	public boolean isVisited(TaskQueue taskQueue) throws CoreException {
		TaskQueue tq = queryTaskQueueByUrl(taskQueue.getUrl());
		if((tq != null) && (tq.isVisited()==true) && (tq.getStatusCode() == 200)){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean deleteTaskQueueByCrawlerId(String crawlerId) throws CoreException {
		taskQueueDao.deleteTaskQueueByCrawlerId(crawlerId);
		return true;
	}
}
