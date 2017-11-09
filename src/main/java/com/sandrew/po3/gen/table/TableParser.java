/*******************************************************************************
 * <pre>
* FILE : TableParser.java
* CLASS : TableParser
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
* 		  |2010-4-23| SuMMeR| Created |
* DESCRIPTION:
* </pre>
 ******************************************************************************/

package com.sandrew.po3.gen.table;

import java.util.List;

import com.sandrew.po3.gen.configure.Configure;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-4-23
 * @version    :
 */
public interface TableParser
{
	public List<Table> parserTable(Configure conf) throws Exception;
}
