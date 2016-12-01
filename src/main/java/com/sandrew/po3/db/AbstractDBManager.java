/**********************************************************************
* <pre>
* FILE : AbstractDBManager.java
* CLASS : AbstractDBManager
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
* $Id: AbstractDBManager.java,v 0.1 2016年11月25日 下午2:08:57 Administrator Exp $
*/

package com.sandrew.po3.db;

import java.sql.Connection;
import java.util.HashMap;

import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Function    : 
 * @author     : Administrator
 * CreateDate  : 2016年11月25日
 * @version    :
 */
public abstract class AbstractDBManager implements DBManager
{
	
	protected String dbName = null;

	protected String txnName = null;

	protected int timeout = -1;
	
	protected AbstractDBManager(String dbName, String txnName, int timeout)
	{
		this.dbName = dbName;
		this.txnName = txnName;
		this.timeout = timeout;
	}

	protected static ThreadLocal<HashMap<Object, Object>> localVar = new ThreadLocal<HashMap<Object, Object>>()
	{
		@Override
		protected synchronized HashMap<Object, Object> initialValue()
		{
			return new HashMap<Object, Object>(5, 1);
		}
	};
	

	/**
	 * 
	 * Function    : 从ThreadLocal获取对象
	 * LastUpdate  : 2010-5-21
	 * @param key
	 * @return
	 */
	protected Object get(Object key)
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
	protected void set(Object key, Object value)
	{
		HashMap<Object, Object> map = localVar.get();
		map.put(key, value);
	}

	/**
	 * 
	 * Function    : 清除当前线程保存的副本信息
	 * LastUpdate  : 2010-5-21
	 */
	protected void clean()
	{
		localVar.get().clear();
	}

	/**
	 * 
	 * Function    : 当前线程中是否存在事务
	 * LastUpdate  : 2010-5-21
	 * @return
	 */
	protected boolean isExistTxn()
	{
		boolean isExistTxn = false;
		HashMap<Object, Object> map = localVar.get();
		for (Object obj : map.values())
		{
			if (obj instanceof DefaultTransactionStatus)
			{
				isExistTxn = true;
			}
		}
		return isExistTxn;
	}
	
	/**
	 * 
	 * Function    : 清除当前线程副本中所有数据
	 * LastUpdate  : 2010-5-21
	 */
	public abstract void cleanTxn() throws Exception;


	/* (non-Javadoc)
	 * @see com.sandrew.po3.db.DBManager#getConnection(java.lang.String, java.lang.String, int)
	 */
	public abstract Connection getConnection() throws Exception;

	/* (non-Javadoc)
	 * @see com.sandrew.po3.db.DBManager#beginTxn(java.lang.String, int)
	 */
	public abstract void beginTxn();

	/* (non-Javadoc)
	 * @see com.sandrew.po3.db.DBManager#endTxn(boolean)
	 */
	public abstract void endTxn(boolean isCommit);
}
