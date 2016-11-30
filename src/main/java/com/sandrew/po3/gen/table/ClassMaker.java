/*******************************************************************************
 * <pre>
* FILE : ClassMaker.java
* CLASS : ClassMaker
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

package com.sandrew.po3.gen.table;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-4-26
 * @version    :
 */
public interface ClassMaker
{
	/**
	 *  
	 * Function    : 根据数据库字段属性生成JAVA类型
	 * LastUpdate  : 2010-4-26
	 * @param colName
	 * @param colType
	 * @param colPrecision
	 * @param colScale
	 * @return
	 */
	public Class<?> getJavaType(String colName, int colType, int colPrecision, int colScale) throws Exception;

	/**
	 * 
	 * Function    : 根据colPrecision和colScale属性生成Java类型,主要是区分Integer,Long,Float及Double
	 * LastUpdate  : 2010-4-26
	 * @param colPrecision
	 * @param colScale
	 * @return
	 */
	public Class<?> getJaveTypeByPrecisionAndScale(int colPrecision, int colScale);
}
