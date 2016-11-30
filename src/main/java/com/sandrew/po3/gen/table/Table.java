/***************************************************************************************************
 * <pre>
* FILE : Table.java
* CLASS : Table
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
 **************************************************************************************************/

package com.sandrew.po3.gen.table;

import java.util.List;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-4-26
 * @version    :
 */
public class Table
{
	// 表名
	private String tableName;

	// PO类名
	private String poName;

	// 字段集合
	private List<Field> fields;

	public String getTableName()
	{
		return tableName;
	}

	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public List<Field> getFields()
	{
		return fields;
	}

	public void setFields(List<Field> fields)
	{
		this.fields = fields;
	}

	public void addFields(Field field)
	{
		this.fields.add(field);
	}

	/**
	 * 
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append(tableName);
		sb.append(fields.toString());
		return sb.toString();
	}

	public String getPoName()
	{
		return poName;
	}

	public void setPoName(String poName)
	{
		this.poName = poName;
	}
}
