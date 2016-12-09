/**********************************************************************
* <pre>
* FILE : SessionFactoryInitializingBean.java
* CLASS : SessionFactoryInitializingBean
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
* 		  |2016年11月29日| Administrator| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: SessionFactoryInitializingBean.java,v 0.1 2016年11月29日 下午5:26:28 Administrator Exp $
*/

package com.sandrew.po3.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

/**
 * Function    : 与Spring集成时的初始化类
 * @author     : Administrator
 * CreateDate  : 2016年11月29日
 * @version    :
 */
public class SessionFactoryInitializingBean implements ApplicationListener<ContextRefreshedEvent>
{
	private static ApplicationContext applicationContext = null;
	
	private static final Log logger = LogFactory.getLog(SessionFactoryInitializingBean.class);


	public void onApplicationEvent(ContextRefreshedEvent event)
	{
		if(null == applicationContext)
		{
			logger.info("PO3 Start Initialize.");
			applicationContext = event.getApplicationContext();
			DBUtil dbu = CommonDBUtilImpl.getInstance();
			dbu.initialize(applicationContext);
			logger.info("PO3 Initialize Completed.");
		}

	}


}
