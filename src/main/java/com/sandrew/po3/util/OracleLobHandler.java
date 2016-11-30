/**********************************************************************
 * <pre> FILE : OracleLobCreator.java CLASS : OracleLobCreator AUTHOR : SuMMeR FUNCTION : TODO
 * ====================================================================== CHANGE HISTORY LOG
 * ---------------------------------------------------------------------- MOD. NO.| DATE | NAME |
 * REASON | CHANGE REQ. ----------------------------------------------------------------------
 * |2011-12-21| SuMMeR| Created | DESCRIPTION: </pre>
 ***********************************************************************/
/**
 * $Id: OracleLobCreator.java,v 0.1 2011-12-21 下午03:00:15 SuMMeR Exp $
 */

package com.sandrew.po3.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import com.sandrew.po3.db.LobCallback;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2011-12-21
 * @version    :
 */
public class OracleLobHandler implements LobHandler
{
	private static Logger logger = LogManager.getLogger(OracleLobHandler.class);

	/* (non-Javadoc)
	 * @see com.autosys.po3.util.LobCreator#createLob()
	 */
	public OracleLobHandler(Connection con)
	{
		try
		{
			blobClass = con.getClass().getClassLoader().loadClass(BLOB_CLASS_NAME);
			clobClass = con.getClass().getClassLoader().loadClass(CLOB_CLASS_NAME);

			durationSessionConstants.put(blobClass, BLOB.DURATION_SESSION);
			durationSessionConstants.put(clobClass, CLOB.DURATION_SESSION);
			modeReadWriteConstants.put(blobClass, BLOB.MODE_READWRITE);
			modeReadWriteConstants.put(clobClass, CLOB.MODE_READWRITE);
		}
		catch (ClassNotFoundException e)
		{
			logger.error("Init OracleLobHandler error", e);
			e.printStackTrace();
		}
//		catch (SQLException e)
//		{
//			logger.error("Init OracleLobHandler error", e);
//			e.printStackTrace();
//		}
	}

	public void setBlobAsBinaryStream(PreparedStatement ps, int paramIndex, final InputStream is, int contentLength) throws Exception
	{
		if (is != null)
		{
			Connection con = ps.getConnection();
			Blob blob = (Blob) createLob(con, ps, false, new LobCallback()
			{
				public void populateLob(Object lob) throws Exception
				{
					Method methodToInvoke = lob.getClass().getMethod("getBinaryOutputStream", (Class[]) null);
					OutputStream out = (OutputStream) methodToInvoke.invoke(lob, (Object[]) null);
					FileCopyUtils.copy(is, out);
				}
			});
			ps.setBlob(paramIndex, blob);
			if (logger.isDebugEnabled())
			{
				logger.debug("Set binary stream for Oracle BLOB with length " + blob.length());
			}
		}
		else
		{
			ps.setBlob(paramIndex, is);
			logger.debug("Set Oracle BLOB to null");
		}
	}

	public void setClobAsCharacterStream(PreparedStatement ps, int paramIndex, final Reader characterStream, int contentLength) throws Exception
	{

		if (characterStream != null)
		{
			Connection con = ps.getConnection();
			Clob clob = (Clob) createLob(con, ps, true, new LobCallback()
			{
				public void populateLob(Object lob) throws Exception
				{
					Method methodToInvoke = lob.getClass().getMethod("getCharacterOutputStream", (Class[]) null);
					Writer writer = (Writer) methodToInvoke.invoke(lob, (Object[]) null);
					FileCopyUtils.copy(characterStream, writer);
				}
			});
			ps.setClob(paramIndex, clob);
			if (logger.isDebugEnabled())
			{
				logger.debug("Set character stream for Oracle CLOB with length " + clob.length());
			}
		}
		else
		{
			ps.setClob(paramIndex, characterStream);
			logger.debug("Set Oracle CLOB to null");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.util.LobCreator#createLob(java.sql.Connection, java.sql.PreparedStatement, boolean, com.autosys.po3.db.LobCallback)
	 */
	private Object createLob(Connection con, PreparedStatement ps, boolean clob, LobCallback callback) throws Exception
	{

		Object lob = prepareLob(con, clob ? clobClass : blobClass);
		callback.populateLob(lob);
		this.createdLobs.add(lob);
		lob.getClass().getMethod("close", (Class[]) null).invoke(lob, (Object[]) null);
		if (logger.isDebugEnabled())
		{
			logger.debug("Created new Oracle " + (clob ? "CLOB" : "BLOB"));
		}
		return lob;
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.util.LobCreator#releaseLob(java.lang.Object)
	 */
	public void releaseLobs() throws Exception
	{
		try
		{
			for (Iterator it = this.createdLobs.iterator(); it.hasNext();)
			{
				/*
				BLOB blob = (BLOB) it.next();
				blob.freeTemporary();
				*/
				Object lob = it.next();
				Method freeTemporary = lob.getClass().getMethod("freeTemporary", new Class[0]);
				freeTemporary.invoke(lob, new Object[0]);
				it.remove();
			}
		}
		catch (InvocationTargetException ex)
		{
			logger.error("Could not free Oracle LOB", ex.getTargetException());
		}
		catch (Exception ex)
		{
			throw new DataAccessResourceFailureException("Could not free Oracle LOB", ex);
		}
	}

	/**
	 * 
	 * Function    : 准备Lob对象,设置持续时间及可读写性
	 * LastUpdate  : 2011-12-21
	 * @param con
	 * @param lobClass
	 * @return
	 * @throws Exception
	 */
	private Object prepareLob(Connection con, Class lobClass) throws Exception
	{
		//durationSessionConstants.get(lobClass);
		Method createTemporary;
		createTemporary = lobClass.getMethod("createTemporary", new Class[] { Connection.class, boolean.class, int.class });
		Object lob = createTemporary.invoke(null, new Object[] { con, cache, durationSessionConstants.get(lobClass) });
		Method open = lobClass.getMethod("open", new Class[] { int.class });
		open.invoke(lob, new Object[] { modeReadWriteConstants.get(lobClass) });
		return lob;
	}

	private Boolean cache = Boolean.TRUE;

	private Class<?> blobClass;

	private Class<?> clobClass;

	private static final String BLOB_CLASS_NAME = "oracle.sql.BLOB";

	private static final String CLOB_CLASS_NAME = "oracle.sql.CLOB";
	

	private final List createdLobs = new LinkedList();

//	private NativeJdbcExtractor extractor;
}
