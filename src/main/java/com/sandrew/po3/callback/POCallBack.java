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
			T po = this.clz.newInstance();
			POMapping mapping = new POMapping(po);
			for (int i = 0; i < mapping.getColSize(); i++)
			{
				Object value = POUtil.getValue(rs, mapping.getColName(i), mapping.getColType(i));
//				POUtil.invokeSetMethodByField(po, POUtil.getAttributeNameByFieldName(mapping.getColName(i)), value);
				POUtil.invokeSetMethodByField(po, mapping.getPropertyName(i), value);
			}
			return po;
		}
		catch (InstantiationException e)
		{
			throw new POException("POCallBack 调用失败", e);
		}
		catch (IllegalAccessException e)
		{
			throw new POException("POCallBack 调用失败", e);
		}

	}

}
