/**********************************************************************
* <pre>
* FILE : Test.java
* CLASS : Test
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
* $Id: Test.java,v 0.1 2016年11月25日 上午9:11:50 Administrator Exp $
*/

package com.sandrew.threadlocal;

/**
 * Function    : 
 * @author     : Administrator
 * CreateDate  : 2016年11月25日
 * @version    :
 */
public class Test
{

	/**
	 * Function    : 
	 * LastUpdate  : 2016年11月25日
	 * @param args
	 */
	public static void main(String[] args)
	{
		ThreadRuner runer = new ThreadRuner();
		for(int i = 0 ; i < 10 ; i++)
		{
			Thread t = new Thread(runer);
			t.start();
		}
	}

}
