/***************************************************************************************************
 * <pre>
* FILE : POMapping.java
* CLASS : POMapping
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
* 		  |2010-8-30| SuMMeR| Created |
* DESCRIPTION:
* </pre>
 **************************************************************************************************/
/**
 * $Id: POMapping.java,v 0.1 2010-8-30 下午02:45:24 SuMMeR Exp $
 */

package com.sandrew.po3.common;

import java.lang.reflect.Field;
import java.util.LinkedList;

import com.sandrew.po3.bean.PO;
import com.sandrew.po3.util.POUtil;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-8-30
 * @version    :
 */
public class POMapping
{
	// 数据库表名
	private String tableName;

	// 数据库表列名
	private LinkedList<String> colNames = new LinkedList<String>();

	// 数据库表列类型
	private LinkedList<Class> colTypes = new LinkedList<Class>();

	private Class<?> clz;

	public POMapping(PO po)
	{
		// 获取po的类
		clz = po.getClass();
		// 获取PO类类名(不包含包)
		String clzName = clz.getSimpleName();
		// 通过类名解析表明
		tableName = POUtil.getTabNameByPOName(clzName);
		// 获取PO的属性字段
		Field[] field = clz.getDeclaredFields();
		// 遍历field获取表列名及类型
		for (int i = 0; i < field.length; i++)
		{
			// 根据属性字段名字解析表列名
			String fieldName = field[i].getName();
			String colName = POUtil.getColNameByFieldName(fieldName);
			colNames.add(colName);
			// 根据属性字段名字解吸表列类型
			Class<?> colType = field[i].getType();
			colTypes.add(colType);
		}
	}

	/**
	 * 
	 * Function    : 获取列的数量
	 * LastUpdate  : 2010-8-30
	 * @return
	 */
	public int getColSize()
	{
		return colNames.size();
	}

	/**
	 * 
	 * Function    : 返回第idx列的列名
	 * LastUpdate  : 2010-8-30
	 * @param idx
	 * @return
	 */
	public String getColName(int idx)
	{
		return colNames.get(idx);
	}

	/**
	 * 
	 * Function    : 返回第idx列的类型
	 * LastUpdate  : 2010-8-30
	 * @param idx
	 * @return
	 */
	public Class<?> getColType(int idx)
	{
		return colTypes.get(idx);
	}

	public String getTableName()
	{
		return tableName;
	}

	/**
	 * Function    : 
	 * LastUpdate  : 2010-8-30
	 * @param args
	 */
	public static void main(String[] args)
	{

	}
}
