package com.sandrew.po3;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sandrew.po3.service.IService;

public class SpringInitTest
{

	@Test
	public void test()
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/DataAccessContext.xml");
		IService service = (IService) ctx.getBean("userManagerService");
		try
		{
//			service.updateUserInfo();
			service.queryInfo();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
