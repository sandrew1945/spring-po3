/*******************************************************************************
 * <pre>
* FILE : OracleClassMaker.java
* CLASS : OracleClassMaker
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
* 		  |2010-4-26| SuMMeR| Created |
* DESCRIPTION:
* </pre>
 ******************************************************************************/
/**
 * $Id: OracleClassMaker.java,v 0.1 2010-4-26 ����11:41:18 SuMMeR Exp $
 */

package com.sandrew.po3.gen.table.impl;

import java.sql.Types;

import com.sandrew.po3.gen.table.ClassMaker;
import com.sandrew.po3.util.Constant;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-4-26
 * @version    :
 */
public class DefaultClassMaker implements ClassMaker
{

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.table.ClassMaker#getJavaType(java.lang.String, int,
	 *      int, int)
	 */
	public Class<?> getJavaType(String colName, int colType, int colPrecision, int colScale) throws Exception
	{
		Class cls = null;
		switch (colType)
		{
			case Types.ARRAY:
				break;
			case Types.BIGINT:
				cls = Long.class;
				break;
			case Types.BINARY:
				break;
			case Types.BIT:
				break;
			case Types.BLOB:
				cls = java.sql.Blob.class;
				break;
			case Types.BOOLEAN:
				cls = Boolean.class;
				break;
			case Types.CHAR:
				cls = String.class;
				break;
			case Types.CLOB:
				cls = java.sql.Clob.class;
				break;
			case Types.DATALINK:
				break;
			case Types.DATE:
				cls = java.util.Date.class;
				break;
			case Types.DECIMAL:
				cls = getJaveTypeByPrecisionAndScale(colPrecision, colScale);
				break;
			case Types.DISTINCT:
				break;
			case Types.DOUBLE:
				cls = Double.class;
				break;
			case Types.FLOAT:
				cls = Float.class;
				break;
			case Types.INTEGER:
				cls = Integer.class;
				break;
			case Types.JAVA_OBJECT:
				break;
			case Types.LONGVARBINARY:
				break;
			case Types.LONGVARCHAR:
				break;
			case Types.NULL:
				break;
			case Types.NUMERIC:
				cls = getJaveTypeByPrecisionAndScale(colPrecision, colScale);
				break;
			case Types.OTHER:
				break;
			case Types.REAL:
				cls = Float.class;
				break;
			case Types.REF:
				break;
			case Types.SMALLINT:
				cls = Integer.class;
				break;
			case Types.STRUCT:
				break;
			case Types.TIME:
				cls = java.util.Date.class;
				break;
			case Types.TIMESTAMP:
				cls = java.util.Date.class;
				break;
			case Types.TINYINT:
				cls = Integer.class;
				break;
			case Types.VARBINARY:
				break;
			case Types.VARCHAR:
				cls = String.class;
				break;
		}
		if (null != cls)
		{
			return cls;
		}
		else
		{
			throw new Exception("Not supported column type! colName=" + colName + " type=" + colType + " precision=" + colPrecision + " Scale=" + colScale);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.table.ClassMaker#getJaveTypeByPrecisionAndScale(int,
	 *      int)
	 */
	public Class<?> getJaveTypeByPrecisionAndScale(int colPrecision, int colScale)
	{
		if (colScale == Constant.JAVA_TYPE_SCALE_0 && colPrecision >= Constant.JAVA_TYPE_PRECISION_0)
		{
			// 小数点后为0位,整数部分>=0,则JavaType为Integer或Long
			if (colPrecision <= Constant.JAVA_TYPE_INTEGER_MAX)
			{
				return Integer.class;
			}
			else if (colPrecision > Constant.JAVA_TYPE_LONG_MIN && colPrecision <= Constant.JAVA_TYPE_LONG_MAX)
			{
				return Long.class;
			}
		}
		else if (colScale > Constant.JAVA_TYPE_SCALE_0 && colPrecision > Constant.JAVA_TYPE_PRECISION_0)
		{
			// 小数点后位数大于0,整数部分>0,则JavaType为Float或Double
			if (colPrecision - colScale < Constant.JAVA_TYPE_FLOAT_MAX && colScale < Constant.JAVA_TYPE_FLOAT_SCALE_MAX)
			{
				return Float.class;
			}
			else
			{
				return Double.class;
			}
		}
		return null;
	}

}
