/**********************************************************************
* <pre>
* FILE : LobCallback.java
* CLASS : LobCallback
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
* 		  |2011-12-21| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: LobCallback.java,v 0.1 2011-12-21 下午03:29:50 SuMMeR Exp $
*/

package com.sandrew.po3.db;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2011-12-21
 * @version    :
 */
public interface LobCallback
{
	
	/**
	 * 
	 * Function    : 用指定的内容构建BLOB/CLOB
	 * LastUpdate  : 2011-12-21
	 * @param lob
	 * @throws Exception
	 */
	void populateLob(Object lob) throws Exception;
}
