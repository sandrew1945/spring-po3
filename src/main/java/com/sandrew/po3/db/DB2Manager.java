package com.sandrew.po3.db;

import java.sql.Connection;
import java.util.HashMap;

import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Function    : 管理应用的Connection和Transaction
 * @author     : SuMMeR
 * CreateDate  : 2010-5-21
 * @version    :
 */
public class DB2Manager
{

	private static ThreadLocal<HashMap<Object, Object>> localVar = new ThreadLocal<HashMap<Object, Object>>()
	{
		@Override
		protected synchronized HashMap<Object, Object> initialValue()
		{
			return new HashMap<Object, Object>(5, 1);
		}
	};

	/**
	 * 
	 * Function    : 获得当前线程指定名称的数据库链接，如果不存在则创建 如果线程没有受事务控制，则创建事务。
	 * LastUpdate  : 2010-5-21
	 * @param dbName
	 * @param txnName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public static Connection getConnection(String dbName, String txnName, int timeout) throws Exception
	{
		Connection conn = (Connection) get(dbName);
		if (null == conn)
		{
			DBUtil util = CommonDBUtilImpl.getInstance();
			if (!isExistTxn())
			{
				beginTxn(txnName, timeout);
			}
			conn = util.getConnection();
			set(dbName, conn);
		}
		return conn;
	}
	
	/**
	 * 
	 * Function    : 创建当前线程事务,如果存在则不创建,否则创建
	 * LastUpdate  : 2010-5-21
	 * @param txnName
	 * @param timeOut
	 */
	public static void beginTxn(String txnName, int timeout)
	{
		//获取当前现成事务
		Object obj = get(txnName);
		if (null == obj && !isExistTxn())
		{
			// 不存在
			DBUtil util = CommonDBUtilImpl.getInstance();
			obj = util.getTransaction(txnName, timeout);
			set(txnName, obj);
		}
	}

	/**
	 * 
	 * Function    : 结束当前线程的默认事务
	 * LastUpdate  : 2010-5-21
	 * @param isCommit
	 */
	public static void endTxn(boolean isCommit)
	{
		DBUtil util = CommonDBUtilImpl.getInstance();
		util.closeTransaction(get(util.getDefTxnManager()), isCommit);
	}

	/**
	 * 
	 * Function    : 结束当前线程指定名称的事务
	 * LastUpdate  : 2010-5-21
	 * @param txnName
	 * @param isCommit
	 */
	public static void endTxn(String txnName, boolean isCommit)
	{
		DBUtil util = CommonDBUtilImpl.getInstance();
		util.closeTransaction(get(txnName), isCommit);
	}

	/**
	 * 
	 * Function    : 清除当前线程副本中所有数据
	 * LastUpdate  : 2010-5-21
	 */
	public static void cleanTxn()
	{
		HashMap<Object, Object> map = localVar.get();
		for (Object obj : map.values())
		{
			if (obj instanceof DefaultTransactionStatus)
			{
				DefaultTransactionStatus status = (DefaultTransactionStatus) obj;
				if (!status.isCompleted())
				{
					throw new RuntimeException("clean Transaction Exception: Transaction " + obj + " close error!");
				}
			}
		}
		clean();
	}

	/**
	 * 
	 * Function    : 从ThreadLocal获取对象
	 * LastUpdate  : 2010-5-21
	 * @param key
	 * @return
	 */
	private static Object get(Object key)
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
	private static void set(Object key, Object value)
	{
		HashMap<Object, Object> map = localVar.get();
		map.put(key, value);
	}

	/**
	 * 
	 * Function    : 清除当前线程保存的副本信息
	 * LastUpdate  : 2010-5-21
	 */
	private static void clean()
	{
		localVar.get().clear();
		//localVar.set(null);
	}

	/**
	 * 
	 * Function    : 当前线程中是否存在事务
	 * LastUpdate  : 2010-5-21
	 * @return
	 */
	private static boolean isExistTxn()
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
}
