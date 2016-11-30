/*******************************************************************************
 * <pre>
 * FILE : Configure.java
 * CLASS : Configure
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
 ******************************************************************************/
/**
 * $Id: Configure.java,v 0.1 2010-4-15 下午02:31:35 SuMMeR Exp $
 */

package com.sandrew.po3.gen.configure;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.sandrew.po3.util.POUtil;

/**
 * Function : 连接数据库的配置信息和所要生成PO的数据库表名称,单例模式
 * 
 * @author : SuMMeR CreateDate : 2010-4-15
 * @version :
 */
public class Configure
{
	// 数据库驱动
	private String jdbcDriver;

	// 数据库地址
	private String jdbcUrl;

	// 数据库用户名
	private String jdbcUser;

	// 数据库密码
	private String jdbcPassword;

	// 数据库种类，如oracle,db2,mssql,mysql
	private String dataBaseType;

	// 生成的PO所在的包名
	private String packageName;

	// 继承父类的包名
	private String fatherPackage;

	// 生成PO的绝对路径
	private String realPath;

	// 需要生成PO的表名
	private List<String> tables;

	private static Configure Conf = new Configure();

	private Configure()
	{
		tables = new ArrayList<String>();
	}

	/**
	 * 构造器 Function : LastUpdate : 2010-4-15
	 * 
	 * @return
	 */
	public static Configure getInstance()
	{
		new Configure();
		return Conf;
	}

	public String getJdbcUrl()
	{
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl)
	{
		this.jdbcUrl = jdbcUrl;
	}

	public String getJdbcUser()
	{
		return jdbcUser;
	}

	public void setJdbcUser(String jdbcUser)
	{
		this.jdbcUser = jdbcUser;
	}

	public String getJdbcPassword()
	{
		return jdbcPassword;
	}

	public void setJdbcPassword(String jdbcPassword)
	{
		this.jdbcPassword = jdbcPassword;
	}

	public String getDataBaseType()
	{
		return dataBaseType;
	}

	public void setDataBaseType(String dataBaseType)
	{
		this.dataBaseType = dataBaseType;
	}

	public String getPackageName()
	{
		return packageName;
	}

	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	public String getFatherPackage()
	{
		return fatherPackage;
	}

	public void setFatherPackage(String fatherPackage)
	{
		this.fatherPackage = fatherPackage;
	}

	public String getJdbcDriver()
	{
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver)
	{
		this.jdbcDriver = jdbcDriver;
	}

	public String getRealPath()
	{
		return realPath;
	}

	public void setRealPath(String realPath)
	{
		this.realPath = realPath;
	}

	public void addTable(String tableName)
	{
		tables.add(tableName);
	}

	public List<String> getTables()
	{
		return tables;
	}

	public void setTables(List<String> tables)
	{
		this.tables = tables;
	}

	/**
	 * 重写的toString方法
	 */
	public String toString()
	{
		StringBuilder sB = new StringBuilder();
		Class<?> cls = this.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			Field field = fields[i];
			// 如果字段是自己本身,那么跳过不处理
			if (field.getType().getName().equals(this.getClass().getName()))
			{
				continue;
			}
			String fieldName = field.getName();
			sB.append(fieldName + " : ");
			String methodName = POUtil.getMethodOfGetByFieldName(fieldName);
			try
			{
				Method meth = cls.getMethod(methodName, null);
				Object[] arglist = new Object[0];
				Object value = meth.invoke(this, arglist);
				if (null != value)
				{
					sB.append(value.toString());
				}
				else
				{
					sB.append("");
				}
				sB.append("\r\n");
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
			catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
		}
		return sB.toString();
	}
}
