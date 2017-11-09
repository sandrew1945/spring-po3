/***************************************************************************************************
 * <pre> FILE : OracleUtilImpl.java CLASS : OracleUtilImpl AUTHOR : SuMMeR FUNCTION : TODO
 * ====================================================================== CHANGE HISTORY LOG
 * ---------------------------------------------------------------------- MOD. NO.| DATE | NAME |
 * REASON | CHANGE REQ. ----------------------------------------------------------------------
 * |2010-5-21| SuMMeR| Created | DESCRIPTION: </pre>
 **************************************************************************************************/
/**
 * $Id: OracleUtilImpl.java,v 0.1 2010-5-21 上午10:19:50 SuMMeR Exp $
 */

package com.sandrew.po3.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sandrew.po3.configure.DBConfigure;
import com.sandrew.po3.exception.POException;
import com.sandrew.po3.util.Constant;

/**
 * Function    : 获取数据库连接,事物工具类
 * @author     : SuMMeR
 * CreateDate  : 2010-5-21
 * @version    :
 */
public class CommonDBUtilImpl implements DBUtil
{

	private static final Log logger = LogFactory.getLog(CommonDBUtilImpl.class);

	private DBConfigure dbConf = null;

	private static DBUtil dbUtil = new CommonDBUtilImpl();

	private ApplicationContext context = null;

	// add 2011-9-15
	//private Resource resource = null;

	private CommonDBUtilImpl()
	{
	}

	/**
	 * 
	 * Function    : 初始化方法(单例)
	 * LastUpdate  : 2016年11月29日
	 * @return
	 */
	public static DBUtil getInstance()
	{
		if (null == dbUtil)
		{
			init();
		}
		return dbUtil;
	}

	public static synchronized void init()
	{
		if (null == dbUtil)
		{
			dbUtil = new CommonDBUtilImpl();
		}
	}

	/**
	 *  
	 * Function    : 初始化方法,初始化所需要的参数,需要在应用启动时加载
	 * LastUpdate  : 2010-5-21
	 * @param classPathResource
	 */
	public void initialize(String classPathResource)
	{
		context = new ClassPathXmlApplicationContext(classPathResource);
		this.dbConf = (DBConfigure) context.getBean(Constant.DBINFO_BEAN);
		logger.debug(this.dbConf);
		
		// modi 2011-9-15
		//this.resource = new ClassPathResource(classPathResource);
//		Resource resource = new ClassPathResource(classPathResource);
//		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
//		BeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
//		reader.loadBeanDefinitions(resource);
//		this.springBeanFactory = beanFactory;
//		this.dbConf = (DBConfigure) springBeanFactory.getBean(Constant.DBINFO_BEAN);
//		logger.debug(this.dbConf);
	}
	

	public void initialize(ApplicationContext context)
	{
		this.dbConf = (DBConfigure)context.getBean("DBInfo");
		this.context = context;
		logger.debug(this.dbConf);
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.db.DBUtil#closeConnection(java.sql.Connection)
	 */
	public void closeConnection(Connection conn)
	{
		try
		{
			if (null != conn && !conn.isClosed())
			{
				conn.close();
				logger.info("Connection :" + conn + " is closed!");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RuntimeException("closs Connection:" + conn + " error!");
		}
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.db.DBUtil#closeTransaction(java.lang.Object, boolean)
	 */
	public void closeTransaction(Object txnStatus, boolean isCommit)
	{
		if (txnStatus == null)
		{
			logger.warn("txn is null!");
			return;
		}
//		PlatformTransactionManager txnManager = (PlatformTransactionManager) springBeanFactory.getBean(dbConf.getDefTxnMng());
		PlatformTransactionManager txnManager = (PlatformTransactionManager) context.getBean(dbConf.getDefTxnMng());
		TransactionStatus status = (TransactionStatus) txnStatus;
		if (status.isCompleted())
		{
			return;
		}
		if (isCommit)
		{
			logger.debug(" Commit txn : " + txnStatus.toString());
			txnManager.commit(status);
			//commitTransaction(dbConf.getDefTxnMng(), txnStatus);
		}
		else
		{
			logger.debug(" Rollback txn : " + txnStatus.toString());
			txnManager.rollback(status);
			//rollbackTransaction(dbConf.getDefTxnMng(), txnStatus);
		}
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.db.DBUtil#commitTransaction(java.lang.String, java.lang.Object)
	 */
	public void commitTransaction(String txnName, Object txnStatus)
	{
//		PlatformTransactionManager txnManager = (PlatformTransactionManager) springBeanFactory.getBean(txnName);
		PlatformTransactionManager txnManager = (PlatformTransactionManager) context.getBean(txnName);
		TransactionStatus status = (TransactionStatus) txnStatus;
		if (status.isCompleted())
		{
			return;
		}
		txnManager.commit(status);
		logger.info("Transaction is commit!");
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.db.DBUtil#getConnection()
	 */
	public Connection getConnection() throws Exception
	{
		try
		{
//			DataSource ds = (DataSource) springBeanFactory.getBean(dbConf.getDefDataSource());
			DataSource ds = (DataSource) context.getBean(dbConf.getDefDataSource());
			Connection conn = DataSourceUtils.getConnection(ds);
			logger.info("getConnection success! Connection :" + conn);
			return conn;
		}
		catch (Exception e)
		{
			throw new RuntimeException("get connection error!", e);
		}
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.db.DBUtil#getTransaction()
	 */
	public Object getTransaction()
	{
		// 调用getTransaction(String txnManagerName, int timeout)
		logger.info("getTransaction success!");
		return getTransaction(dbConf.getDefTxnMng(), -1);
	}

	/**
	 * 
	 * Function    : 获取事务,可以设置超时时间,仅供getTransaction()调用
	 * LastUpdate  : 2010-5-21
	 * @param txnManager
	 * @param timeout
	 * @return
	 */
	public Object getTransaction(String txnManagerName, int timeout)
	{
//		PlatformTransactionManager txnManager = (PlatformTransactionManager) springBeanFactory.getBean(txnManagerName);
		PlatformTransactionManager txnManager = (PlatformTransactionManager) context.getBean(txnManagerName);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		if (timeout > 0)
		{
			def.setTimeout(timeout);
		}
		Object txnStatus = txnManager.getTransaction(def);
		logger.info("Start Transaction name =============>" + txnStatus);
		return txnStatus;
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.db.DBUtil#rollbackTransaction(java.lang.String, java.lang.Object)
	 */
	public void rollbackTransaction(String txnName, Object txnStatus)
	{
//		PlatformTransactionManager txnManager = (PlatformTransactionManager) springBeanFactory.getBean(txnName);
		PlatformTransactionManager txnManager = (PlatformTransactionManager) context.getBean(txnName);
		TransactionStatus status = (TransactionStatus) txnStatus;
		txnManager.rollback(status);
		logger.info("Transaction is rollback!");
	}

	public String getDefDataSource()
	{
		return dbConf.getDefDataSource();
	}

	public String getDefTxnManager()
	{
		return dbConf.getDefTxnMng();
	}

	public String getDefTxnType()
	{
		return dbConf.getDefTxnType();
	}

	public String getNoContainedTxnMng()
	{
		return dbConf.getNoContainedTxnMng();
	}

	public String getDefDBType()
	{
		return dbConf.getDefDBType();
	}

	public NativeJdbcExtractor getDefJdbcExtractor()
	{
		return dbConf.getDefJdbcExtractor();
	}

	/**
	 * 
	 * Function    : 验证是否需要PO框架控制事务
	 * 				  如果配置defTxnMng那么自行控制事务，否则使用上下文事务统一控制
	 * LastUpdate  : 2016年11月29日
	 * @return
	 */
	public boolean needTransation()
	{
		if ("jdbc".equalsIgnoreCase(dbConf.getDefTxnType()))
		{
			return true;
		}
		else if ("managed".equalsIgnoreCase(dbConf.getDefTxnType()))
		{
			return false;
		}
		else
		{
			throw new POException("不支持的defTxnType");
		}
		
	}
}
