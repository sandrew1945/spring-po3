/**********************************************************************
 * <pre>
 * FILE : DAOException.java
 * CLASS : DAOException
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
 * 		  |2010-4-16| SuMMeR| Created |
 * DESCRIPTION:
 * </pre>
 ***********************************************************************/
/**
 * 
 */

package com.sandrew.po3.exception;

/**
 * Function : PO3持久层异常
 * 
 * @author : SuMMeR CreateDate : 2010-4-16
 * @version :
 */
public class UnsupportedMethodException extends RuntimeException
{
	public UnsupportedMethodException(String msg)
	{
		super(msg);
	}

	public UnsupportedMethodException(Throwable cause)
	{
		super(cause);
	}

	public UnsupportedMethodException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
