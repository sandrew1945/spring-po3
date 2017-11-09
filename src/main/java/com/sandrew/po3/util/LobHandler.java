/**********************************************************************
 * <pre> FILE : LobCreator.java CLASS : LobCreator AUTHOR : SuMMeR FUNCTION : TODO
 * ====================================================================== CHANGE HISTORY LOG
 * ---------------------------------------------------------------------- MOD. NO.| DATE | NAME |
 * REASON | CHANGE REQ. ----------------------------------------------------------------------
 * |2011-12-21| SuMMeR| Created | DESCRIPTION: </pre>
 ***********************************************************************/
/**
 * $Id: LobCreator.java,v 0.1 2011-12-21 下午02:57:54 SuMMeR Exp $
 */

package com.sandrew.po3.util;

import java.io.InputStream;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2011-12-21
 * @version    :
 */
public interface LobHandler
{
	final Map durationSessionConstants = new HashMap(2);

	final Map modeReadWriteConstants = new HashMap(2);

	/**
	 * 
	 * Function    : 创建一个Lob对象,并将根据LobCallback做相应操作
	 * LastUpdate  : 2011-12-21
	 * @param con
	 * @param ps
	 * @param clob
	 * @param callback
	 * @return
	 * @throws Exception
	 */
	//	public Object createLob(Connection con, PreparedStatement ps, boolean clob, LobCallback callback) throws Exception;

	/**
	 * 
	 * Function    : 释放Lob对象
	 * LastUpdate  : 2011-12-21
	 * @param lob
	 * @throws Exception
	 */
	public void releaseLobs() throws Exception;

	/**
	 * 
	 * Function    : 用输入流填充BLOB
	 * LastUpdate  : 2011-12-21
	 * @param ps
	 * @param paramIndex
	 * @param is
	 * @param contentLength
	 * @throws SQLException
	 */
	public void setBlobAsBinaryStream(PreparedStatement ps, int paramIndex, final InputStream is, int contentLength) throws Exception;

	/**
	 * 
	 * Function    : 用字符填充CLOB
	 * LastUpdate  : 2011-12-21
	 * @param con
	 * @param ps
	 * @param paramIndex
	 * @param characterStream
	 * @param contentLength
	 * @throws Exception
	 */
	public void setClobAsCharacterStream(PreparedStatement ps, int paramIndex, final Reader characterStream, int contentLength) throws Exception;
}
