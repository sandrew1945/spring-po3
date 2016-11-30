/**********************************************************************
 * <pre>
 * FILE : OracleJdbcConnection.java
 * CLASS : OracleJdbcConnection
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
 * 		  |2010-4-15| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * $Id: OracleJdbcConnection.java,v 0.1 2010-4-15 ����02:29:25 SuMMeR Exp $
 */

package com.sandrew.po3.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.sandrew.po3.exception.POException;
import com.sandrew.po3.gen.configure.Configure;

/**
 * Function : JDBC数据库连接，用于PO的生成
 * 
 * @author : SuMMeR CreateDate : 2010-4-15
 * @version :
 */
public class JdbcConnection implements DBConnection
{
	// 数据库驱动
	private String jdbcDriver = null;

	// 数据库地址
	private String jdbcUrl = null;

	// 数据库用户
	private String jdbcUser = null;

	// 数据库密码
	private String jdbcPwd = null;

	/**
	 * 构造方法
	 * 
	 * @param conf
	 */
	public JdbcConnection(Configure conf)
	{
		// 实例化时初始化参数
		initialize(conf);
	}

	/**
	 * 初始化参数 Function : LastUpdate : 2010-4-16
	 * 
	 * @param conf
	 */
	private void initialize(final Configure conf)
	{
		jdbcDriver = conf.getJdbcDriver();
		jdbcUrl = conf.getJdbcUrl();
		jdbcUser = conf.getJdbcUser();
		jdbcPwd = conf.getJdbcPassword();
	}

	/**
	 * 通过jdbc方式获取Connection (non-Javadoc)
	 * 
	 * @see com.autosys.po3.db.OracleConnection#getConn()
	 */
	public Connection getConn() throws POException
	{
		Connection conn = null;
		try
		{
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPwd);
		}
		catch (ClassNotFoundException e)
		{
			throw new POException("获取Connection失败，无法找到驱动文件", e);
		}
		// 与url指定的数据源建立连接
		catch (SQLException e)
		{
			throw new POException("获取Connection失败", e);
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 */
	public void closeConn(Connection conn) throws POException
	{
		try
		{
			if (null != conn && !conn.isClosed())
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new POException("数据库连接关闭失败");
		}
	}
}
