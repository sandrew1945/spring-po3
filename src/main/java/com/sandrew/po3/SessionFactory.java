/***************************************************************************************************
 * <pre>
 * FILE : POFactory.java
 * CLASS : POFactory
 *
 * AUTHOR : SuMMeR
 *
 * FUNCTION : TODO
 *
 *
 *======================================================================
 * CHANGE HISTORY LOG
 *----------------------------------------------------------------------
 * MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
 *----------------------------------------------------------------------
 * 		  |2010-4-16| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 **************************************************************************************************/
/**
 * $Id: POFactory.java,v 0.1 2010-4-16 下午01:58:55 SuMMeR Exp $
 */

package com.sandrew.po3;

import org.apache.log4j.Logger;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBManager;
import com.sandrew.po3.db.DBUtil;
import com.sandrew.po3.db.SimpleDBManager;
import com.sandrew.po3.db.TransationDBManager;
import com.sandrew.po3.exception.POException;
import com.sandrew.po3.impl.DefaultSession;
import com.sandrew.po3.impl.OracleSession;
import com.sandrew.po3.util.Constant;

/**
 * Function :
 * 
 * @author : SuMMeR CreateDate : 2010-4-16
 * @version :
 */
public class SessionFactory
{

	private static Logger logger = Logger.getLogger(SessionFactory.class);

	/**
	 * 创建无参的POFactory对象，所有的参数均采用系统默认配置。
	 * 
	 * @return POFactory对象引用。
	 */
	public final static Session getInstance()
	{

		DBUtil util = CommonDBUtilImpl.getInstance();
		return openSession(util, -1);
	}

	public static Session openSession(DBUtil util, int timeout)
	{
		String dbType = util.getDefDBType();
		String dbName = util.getDefDataSource();
		String txnName = util.getDefTxnManager();
		NativeJdbcExtractor extractor = util.getDefJdbcExtractor();
		DBManager dbManager = null;
		// 验证是否需要PO框架自行控制事务
		if (util.needTransation())
		{
			dbManager = new TransationDBManager(dbName, txnName, timeout);
		}
		else
		{
			dbManager = new SimpleDBManager(dbName, txnName, timeout);
		}
		
		logger.debug("使用的事务管理器:" + dbManager.getClass().getName());
		
		if (Constant.DATABASE_TYPE_ORACLE.equalsIgnoreCase(dbType))
		{
			return OracleSession.getInstance(dbManager, extractor);
		}
		else if (Constant.DATABASE_TYPE_MSSQL.equalsIgnoreCase(dbType))
		{
			return DefaultSession.getInstance(dbManager, extractor);
		}
		else if (Constant.DATABASE_TYPE_MYSQL.equalsIgnoreCase(dbType))
		{
			return DefaultSession.getInstance(dbManager, extractor);
		}
		else
		{
			logger.error("Not supported Database : " + dbType);
			throw new POException("Not supported Database : " + dbType);
		}
	}

}
