package com.sandrew.threadlocal;

import org.junit.Test;

public class ThreadLocalTest
{

	@Test
	public void test()
	{
		ThreadRuner runer = new ThreadRuner();
		for(int i = 0 ; i < 10 ; i++)
		{
			Thread t = new Thread(runer);
			t.start();
		}
	}

}
