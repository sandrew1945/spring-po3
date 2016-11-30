/**********************************************************************
* <pre>
* FILE : DBConnection.java
* CLASS : DBConnection
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
* 		  |2010-4-15| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/

package com.sandrew.po3.db;

import java.sql.Connection;

import com.sandrew.po3.exception.POException;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-4-15
 * @version    :
 */
public interface DBConnection
{
	Connection getConn() throws POException;
	void closeConn(Connection conn) throws POException;
}
