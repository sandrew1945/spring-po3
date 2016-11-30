/***************************************************************************************************
 * <pre>
* FILE : DBUtil.java
* CLASS : DBUtil
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
* 		  |2010-5-21| SuMMeR| Created |
* DESCRIPTION:
* </pre>
 **************************************************************************************************/

package com.sandrew.po3.db;

import java.sql.Connection;

import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.sandrew.po3.configure.DBConfigure;

/**
 * Function    : 获取数据库连接,事务的工具类接口
 * @author     : SuMMeR
 * CreateDate  : 2010-5-21
 * @version    :
 */
public interface DBUtil
{
	/**
	 * 
	 * Function    : 根据配置文件初始化
	 * LastUpdate  : 2016年11月29日
	 * @param path
	 */
	public void initialize(String path);
	
	/**
	 * 
	 * Function    : 根据DBConfigure对象初始化
	 * LastUpdate  : 2016年11月29日
	 * @param dbConf
	 */
	public void initialize(ApplicationContext context);

	/**
	 * 
	 * Function    : 获取连接
	 * LastUpdate  : 2016年11月29日
	 * @return
	 * @throws Exception
	 */
	public Connection getConnection() throws Exception;

	/**
	 * 
	 * Function    : 关闭连接
	 * LastUpdate  : 2016年11月29日
	 * @param conn
	 */
	public void closeConnection(Connection conn);

	/**
	 * 
	 * Function    : 获取事务
	 * LastUpdate  : 2016年11月29日
	 * @return
	 */
	public Object getTransaction();

	/**
	 * 
	 * Function    : 获取事务
	 * LastUpdate  : 2016年11月29日
	 * @param txnName
	 * @param timeout
	 * @return
	 */
	public Object getTransaction(String txnName, int timeout);

	/**
	 * 
	 * Function    : 关闭事务
	 * LastUpdate  : 2016年11月29日
	 * @param txnStatus
	 * @param isCommit
	 */
	public void closeTransaction(Object txnStatus, boolean isCommit);

	/**
	 * 
	 * Function    : 提交事务
	 * LastUpdate  : 2016年11月29日
	 * @param txnName
	 * @param txnStatus
	 */
	public void commitTransaction(String txnName, Object txnStatus);

	/**
	 * 
	 * Function    : 回滚事务
	 * LastUpdate  : 2016年11月29日
	 * @param txnName
	 * @param txnStatus
	 */
	public void rollbackTransaction(String txnName, Object txnStatus);

	public String getDefDataSource();

	public String getDefTxnManager();
	
	public String getDefTxnType();

	public String getNoContainedTxnMng();
	
	public String getDefDBType();
	
	public NativeJdbcExtractor getDefJdbcExtractor();
	/**
	 * 
	 * Function    : 验证是否需要PO框架控制事务
	 * 				  如果配置defTxnMng那么自行控制事务，否则使用上下文事务统一控制
	 * LastUpdate  : 2016年11月29日
	 * @return
	 */
	public boolean needTransation();
}
