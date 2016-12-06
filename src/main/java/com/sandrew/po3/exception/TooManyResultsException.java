/**********************************************************************
* <pre>
* FILE : EBException.java
* CLASS : EBException
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
* 		  |2009-11-2| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: ActionException.java,v 1.1 2013/07/31 08:32:47 xin.jin Exp $
*/
package com.sandrew.po3.exception;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2009-11-2
 * @version    :
 */
public class TooManyResultsException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4042594687623349469L;

	public TooManyResultsException()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public TooManyResultsException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public TooManyResultsException(String message)
	{
		super(message);
		// TODO Auto-generated constructor stub
	}

	public TooManyResultsException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}
}
