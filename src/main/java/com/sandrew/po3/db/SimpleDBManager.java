package com.sandrew.po3.db;

import java.sql.Connection;

/**
 * Function    : 管理应用的Connection和Transaction
 * @author     : SuMMeR
 * CreateDate  : 2010-5-21
 * @version    :
 */
public class SimpleDBManager extends AbstractDBManager
{

	public SimpleDBManager(String dbName, String txnName, int timeout)
	{
		super(dbName, txnName, timeout);
	}
	
	/**
	 * 
	 * Function    : 获得当前线程指定名称的数据库链接，如果不存在则创建
	 * LastUpdate  : 2010-5-21
	 * @param dbName
	 * @param txnName
	 * @param timeout
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception
	{
		Connection conn = (Connection) get(dbName);
		if (null == conn)
		{
			DBUtil util = CommonDBUtilImpl.getInstance();
			conn = util.getConnection();
			conn.setAutoCommit(false);
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
	public void beginTxn()
	{
		// do nothing!
	}

	/**
	 * 
	 * Function    : 结束当前线程的默认事务
	 * LastUpdate  : 2010-5-21
	 * @param isCommit
	 */
	public void endTxn(boolean isCommit)
	{
		// do nothing!
	}
}
