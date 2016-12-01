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
public class TransationDBManager extends AbstractDBManager
{
	

	public TransationDBManager(String dbName, String txnName, int timeout)
	{
		super(dbName, txnName, timeout);
	}
	

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
	public Connection getConnection() throws Exception
	{
		Connection conn = (Connection) get(dbName);
		if (null == conn)
		{
			DBUtil util = CommonDBUtilImpl.getInstance();
			if (!isExistTxn())
			{
				beginTxn();
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
	public void beginTxn()
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
	public void endTxn(boolean isCommit)
	{
		DBUtil util = CommonDBUtilImpl.getInstance();
		util.closeTransaction(get(txnName), isCommit);
	}


	@Override
	public void cleanTxn() throws Exception
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

}
