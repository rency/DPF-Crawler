package org.rency.crawler.dao.impl;

import java.util.List;

import org.rency.crawler.beans.TaskQueue;
import org.rency.crawler.dao.TaskQueueDao;
import org.rency.utils.common.CONST;
import org.rency.utils.dao.BasicDao;
import org.rency.utils.exceptions.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("taskQueueDao")
public class TaskQueueDaoImpl implements TaskQueueDao {

	private static final Logger logger = LoggerFactory.getLogger(TaskQueueDaoImpl.class);
	
	@Autowired
	@Qualifier("hibernateDao")
	private BasicDao basicDao;
	
	private String ENTITY = TaskQueue.class.getName();
	private final String CRAWLERID = "crawlerId";
	private final String URL = "url";
	private final String IS_VISITED = "visited";
	private final String IS_DOWNLOAD = "download";
	private final String TIMEOUT = "timeout";
	private final String STATUSCODE = "statusCode";
	private final String LASTMODIFIED = "lastModified";
	private final String EXECDATE = "execDate";
	
	@Override
 	public boolean save(TaskQueue taskQueue) throws CoreException {
		try{
			return basicDao.save(taskQueue);
		}catch(CoreException e){
			logger.error("exec save error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public TaskQueue getTaskQueueByUrl(String url) throws CoreException {
		try{
			return basicDao.get(TaskQueue.class, url);
		}catch(CoreException e){
			logger.error("exec getTaskQueueByUrl error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public TaskQueue getTopTaskQueue() throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+IS_VISITED+"=? and model."+TIMEOUT+"=? order by model."+EXECDATE;
		try{
			List<TaskQueue> list = basicDao.findByHQL(queryString, new Object[]{false,CONST.RETRY_COUNT});
			if(list != null && list.size() > 0){
				return list.get(0);
			}
			return null;
		}catch(CoreException e){
			logger.error("exec getTaskQueueByUrl["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Integer getTaskQueueCount() throws CoreException {
		try{
			return basicDao.getCount(TaskQueue.class);
		}catch(CoreException e){
			logger.error("exec getTaskQueueCount error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public Integer getTaskQueueTimeout(String url) throws CoreException {
		try{
			TaskQueue taskQueue = getTaskQueueByUrl(url);
			if(taskQueue != null){
				return taskQueue.getTimeout();
			}
			return CONST.RETRY_COUNT;
		}catch(CoreException e){
			logger.error("exec getTaskQueueCount error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public TaskQueue getDownloadTaskQueueWithOne() throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+IS_DOWNLOAD+"=? and model."+IS_VISITED+"=? and model."+TIMEOUT+"=? and model."+STATUSCODE+"=? order by asc model."+EXECDATE+" limit 1";
		try{
			List<TaskQueue> list = basicDao.findByHQL(queryString, new Object[]{false,true,0,200});
			if(list != null && list.size() > 0){
				return list.get(0);
			}
			return null;
		}catch(CoreException e){
			logger.error("exec getDownloadTaskQueueWithOne["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public TaskQueue getByUrlAndLastModified(String url, String lastModified)throws CoreException {
		String queryString = "from "+ENTITY+" as model where model."+URL+"=? and model."+LASTMODIFIED+"=? order by asc model."+EXECDATE+" limit 1";
		try{
			List<TaskQueue> list = basicDao.findByHQL(queryString, new Object[]{false,true,0,200});
			if(list != null && list.size() > 0){
				return list.get(0);
			}
			return null;
		}catch(CoreException e){
			logger.error("exec getByUrlAndLastModified["+queryString+"] error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean updateTaskQueue(TaskQueue taskQueue) throws CoreException {
		try{
			return basicDao.update(taskQueue);
		}catch(CoreException e){
			logger.error("exec updateTaskQueue error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean updateTaskQueueTimeout(TaskQueue taskQueue) throws CoreException {
		try{
			return basicDao.updateByProperty(ENTITY, URL, taskQueue.getUrl(), TIMEOUT, taskQueue.getTimeout());
		}catch(CoreException e){
			logger.error("exec updateTaskQueueTimeout error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean updateTaskQueueVisited(TaskQueue taskQueue) throws CoreException {
		try{
			return basicDao.updateByProperty(ENTITY, URL, taskQueue.getUrl(), IS_VISITED, taskQueue.isVisited());
		}catch(CoreException e){
			logger.error("exec updateTaskQueueVisited error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean updateTaskQueueDownload(TaskQueue taskQueue) throws CoreException {
		try{
			return basicDao.updateByProperty(ENTITY, URL, taskQueue.getUrl(), IS_DOWNLOAD, taskQueue.isDownload());
		}catch(CoreException e){
			logger.error("exec updateTaskQueueDownload error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean updateTaskQueueStatusCode(TaskQueue taskQueue)throws CoreException {
		try{
			return basicDao.updateByProperty(ENTITY, URL, taskQueue.getUrl(), STATUSCODE, taskQueue.getStatusCode());
		}catch(CoreException e){
			logger.error("exec updateTaskQueueStatusCode error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteTaskQueue(String url) throws CoreException {
		try{
			return basicDao.deleteByProperty(ENTITY, URL, url);
		}catch(CoreException e){
			logger.error("exec deleteTaskQueue error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteTaskQueueByCrawlerId(String crawlerId)throws CoreException {
		try{
			return basicDao.deleteByProperty(ENTITY, CRAWLERID, crawlerId);
		}catch(CoreException e){
			logger.error("exec deleteTaskQueueByCrawlerId error.",e);
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public boolean deleteAll() throws CoreException {
		try{
			return basicDao.deleteAll(TaskQueue.class);
		}catch(CoreException e){
			logger.error("exec delete all error."+e);
			e.printStackTrace();
			throw e;
		}
	}

}