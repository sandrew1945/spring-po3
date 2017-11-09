/**********************************************************************
* <pre>
* FILE : ThreadLocalManager.java
* CLASS : ThreadLocalManager
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
* $Id: ThreadLocalManager.java,v 0.1 2016年11月25日 上午8:59:50 Administrator Exp $
*/

package com.sandrew.threadlocal;

import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Test;

/**
 * Function    : 
 * @author     : Administrator
 * CreateDate  : 2016年11月25日
 * @version    :
 */
public class ThreadLocalManager
{
	public static ThreadLocal<HashMap<String, String>> localVar = new ThreadLocal<HashMap<String, String>>()
	{
		@Override
		protected synchronized HashMap<String, String> initialValue()
		{
			return new HashMap<String, String>(10, 1);
		}
	};
	
	/**
	 * 
	 * Function    : 从ThreadLocal获取对象
	 * LastUpdate  : 2010-5-21
	 * @param key
	 * @return
	 */
	public Object get(String key)
	{
		return localVar.get().get(key);
	}

	/**
	 * 
	 * Function    : 将对象存到ThreadLocal
	 * LastUpdate  : 2010-5-21
	 * @param key
	 * @param value
	 */
	public void set(String key, String value)
	{
		HashMap<String, String> map = localVar.get();
		map.put(key, value);
	}

}
