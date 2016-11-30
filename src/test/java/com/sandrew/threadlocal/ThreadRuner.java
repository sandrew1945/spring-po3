/**********************************************************************
* <pre>
* FILE : ThreadRuner.java
* CLASS : ThreadRuner
*
* AUTHOR : Administrator
*
* FUNCTION : TODO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2016年11月25日| Administrator| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: ThreadRuner.java,v 0.1 2016年11月25日 上午9:02:08 Administrator Exp $
*/

package com.sandrew.threadlocal;

import java.util.Random;

/**
 * Function    : 
 * @author     : Administrator
 * CreateDate  : 2016年11月25日
 * @version    :
 */
public class ThreadRuner implements Runnable
{

	public void run()
	{
		ThreadLocalManager manager = new ThreadLocalManager();
		Random r = new Random();
		int rand = r.nextInt(600000);
		manager.set(Thread.currentThread().getName(), Thread.currentThread().getName() + "&" + rand);
		while (true)
		{
			System.out.println("线程:"+Thread.currentThread().getName()+"的值为：" + manager.get(Thread.currentThread().getName()));
			int rand2 = r.nextInt(2000);
			System.out.println("=================" + manager.localVar);
			try
			{
				Thread.sleep(rand2);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

}
