package com.sandrew.po3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sandrew.po3.service.IService;

public class SpringThreadTest
{
	public static void main(String[] args)
	{
		SpringThreadTest stt = new SpringThreadTest();
		stt.multithreadRun();
	}
	
	public void multithreadRun()
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/DataAccessContext.xml");
		IService service = (IService) ctx.getBean("userManagerService");
		try
		{
//			service.updateUserInfo();
//			service.queryInfo();
			for (int i = 1; i <= 7; i++)
			{
				ThreadTester tester = new ThreadTester(i, service);
				Thread t = new Thread(tester);
				t.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public class ThreadTester implements Runnable
	{
		private Integer id;
		private IService service;


		public ThreadTester(Integer id, IService service)
		{
			this.id = id;
			this.service = service;
		}


		public void run()
		{
			try
			{
				String name = Thread.currentThread().getName();
				System.out.println("第"+id+"条的名字为："+ name);
				this.service.updateUserName(id, name);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}

}
