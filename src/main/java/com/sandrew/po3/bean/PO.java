/**
 * 
 */
package com.sandrew.po3.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sandrew.po3.util.POUtil;

/**
 * @author SuMMeR
 */
@SuppressWarnings("serial")
public class PO implements DataBean
{
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
