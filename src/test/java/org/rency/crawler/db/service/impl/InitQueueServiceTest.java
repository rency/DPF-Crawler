package org.rency.crawler.db.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.rency.crawler.service.CookieParamService;
import org.rency.crawler.service.TaskQueueService;
import org.rency.crawler.service.WebPageService;
import org.rency.utils.exceptions.CoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class InitQueueServiceTest {

	private TaskQueueService taskQueueService;
	private WebPageService webPageService;
	private CookieParamService cookieParamService;
	
	@Before
	public void before(){
		@SuppressWarnings("resource")
		ApplicationContext ctx = new FileSystemXmlApplicationContext("src/test/resources/applicationContext.xml");
		taskQueueService = ctx.getBean(TaskQueueService.class);
		webPageService = ctx.getBean(WebPageService.class);
		cookieParamService = ctx.getBean(CookieParamService.class);
	}
	
	@Test
	public void testAdd() throws CoreException {
		taskQueueService.deleteAll();
		webPageService.deleteAll();
		cookieParamService.deleteAll();
	}

}
