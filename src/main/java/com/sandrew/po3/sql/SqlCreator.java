/***************************************************************************************************
 * <pre>
* FILE : SqlCreator.java
* CLASS : SqlCreator
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
 * $Id: SqlCreator.java,v 0.1 2010-8-30 ����05:30:34 SuMMeR Exp $
 */

package com.sandrew.po3.sql;

import com.sandrew.po3.bean.PO;
import com.sandrew.po3.common.POMapping;

/**
 * Function    : SQL生成接口
 * @author     : SuMMeR
 * CreateDate  : 2010-8-30
 * @version    :
 */
public interface SqlCreator
{
	public String selectCreator(POMapping mapping, PO po);

	public String updateCreator(POMapping mapping, PO cond, PO value);

	public String deleteCreator(POMapping mapping, PO po);

	public String insertCreator(POMapping mapping, PO po);

	public String selectCreatorForOrder(POMapping mapping, PO po, String order, String... colName);
}
