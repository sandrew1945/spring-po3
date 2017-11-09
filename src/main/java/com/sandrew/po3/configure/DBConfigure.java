/***************************************************************************************************
 * <pre>
 * FILE : DBConfigure.java
 * CLASS : DBConfigure
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
 * 		  |2010-5-19| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 **************************************************************************************************/

package com.sandrew.po3.configure;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.sandrew.po3.util.POUtil;

/**
 * Function :
 * 
 * @author : SuMMeR CreateDate : 2010-5-19
 * @version :
 */
public class DBConfigure
{
	/**
	 * @uml.property name="defTxnMng"
	 */
	private String defTxnMng;

	/**
	 * @uml.property name="defTxnType"
	 */
	private String defTxnType;

	/**
	 * @uml.property name="defDataSource"
	 */
	private String defDataSource;

	/**
	 * @uml.property name="noContainedTxnMng"
	 */
	private String noContainedTxnMng;

	/**
	 * @uml.property name="defJdbcExtractor"
	 */
	private NativeJdbcExtractor defJdbcExtractor;

	/**
	 * @uml.property name="defDBType"
	 */
	private String defDBType;

	public String getDefTxnMng()
	{
		return defTxnMng;
	}

	public void setDefTxnMng(String defTxnMng)
	{
		this.defTxnMng = defTxnMng;
	}

	public String getDefTxnType()
	{
		return defTxnType;
	}

	public void setDefTxnType(String defTxnType)
	{
		this.defTxnType = defTxnType;
	}

	public String getDefDataSource()
	{
		return defDataSource;
	}

	public void setDefDataSource(String defDataSource)
	{
		this.defDataSource = defDataSource;
	}

	public String getNoContainedTxnMng()
	{
		return noContainedTxnMng;
	}

	public void setNoContainedTxnMng(String noContainedTxnMng)
	{
		this.noContainedTxnMng = noContainedTxnMng;
	}

	public NativeJdbcExtractor getDefJdbcExtractor()
	{
		return defJdbcExtractor;
	}

	public void setDefJdbcExtractor(NativeJdbcExtractor defJdbcExtractor)
	{
		this.defJdbcExtractor = defJdbcExtractor;
	}

	public String getDefDBType()
	{
		return defDBType;
	}

	public void setDefDBType(String defDBType)
	{
		this.defDBType = defDBType;
	}

	/**
	 * 	toString方法,用来查看配置信息
	 */
	public String toString()
	{
		StringBuilder sB = new StringBuilder();
		Class<?> cls = this.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++)
		{
			Field field = fields[i];
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
