/***************************************************************************************************
 * <pre>
* FILE : POCallBack.java
* CLASS : POCallBack
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
* 		  |2010-9-1| SuMMeR| Created |
* DESCRIPTION:
* </pre>
 **************************************************************************************************/
/**
 * $Id: POCallBack.java,v 0.1 2010-9-1 ����04:41:24 SuMMeR Exp $
 */

package com.sandrew.po3.callback;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.sandrew.po3.bean.PO;
import com.sandrew.po3.common.POMapping;
import com.sandrew.po3.exception.POException;
import com.sandrew.po3.util.POUtil;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-9-1
 * @version    :
 */
public class POCallBack<T extends PO> implements DAOCallback<T>
{
	private Class<T> clz = null;

	public POCallBack(Class<T> clz)
	{
		this.clz = clz;
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.callback.DAOCallback#wrapper(java.sql.ResultSet, int)
	 */
	public T wrapper(ResultSet rs, int index)
	{
		try
		{
			// 先将ResultSet转成Map再进行绑定
			Map<String, Object> resultMap = getResultMap(rs);

			T po = this.clz.newInstance();
			POMapping mapping = new POMapping(po);
			for (int i = 0; i < mapping.getColSize(); i++)
			{
				Object value = resultMap.get(mapping.getColName(i));
				if (null != value)
				{
					POUtil.invokeSetMethodByField(po, mapping.getPropertyName(i), value);
				}
				/*
				Object value = POUtil.getValue(rs, mapping.getColName(i), mapping.getColType(i));
				//POUtil.invokeSetMethodByField(po, POUtil.getAttributeNameByFieldName(mapping.getColName(i)), value);
				POUtil.invokeSetMethodByField(po, mapping.getPropertyName(i), value);
				*/
			}
			return po;
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
			throw new POException("POCallBack 调用失败", e);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			throw new POException("POCallBack 调用失败", e);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new POException("POCallBack 调用失败", e);
		}

	}

	/**
	 * 
	 * Function    : 将ResultSet转成Map
	 * LastUpdate  : 2016年12月7日
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Map<String, Object> getResultMap(ResultSet rs) throws SQLException
	{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ResultSetMetaData meta = rs.getMetaData();
		for (int i = 1; i <= meta.getColumnCount(); i++)
		{
			String key = meta.getColumnName(i);
			Object value = rs.getObject(key);
			resultMap.put(key, value);
		}
		return resultMap;
	}

}
