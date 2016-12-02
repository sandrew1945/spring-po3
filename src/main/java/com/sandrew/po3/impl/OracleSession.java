/***************************************************************************************************
 * <pre> FILE : OracleSession.java CLASS : OracleSession AUTHOR : SuMMeR FUNCTION : TODO
 * ====================================================================== CHANGE HISTORY LOG
 * ---------------------------------------------------------------------- MOD. NO.| DATE | NAME |
 * REASON | CHANGE REQ. ----------------------------------------------------------------------
 * |2010-5-21| SuMMeR| Created | DESCRIPTION: </pre>
 **************************************************************************************************/
/**
 * $Id: OracleSession.java,v 0.1 2010-5-21 下午09:07:55 SuMMeR Exp $
 */

package com.sandrew.po3.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.sandrew.po3.db.DBManager;
import com.sandrew.po3.exception.POException;
import com.sandrew.po3.util.LobHandler;
import com.sandrew.po3.util.OracleLobHandler;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-5-21
 * @version    :
 */
public class OracleSession extends DefaultSession
{

	private static Logger logger = LogManager.getLogger(OracleSession.class);

	protected DBManager dbManager = null;

	/**
	 *  构造器
	 * @param dbName
	 * @param txnName
	 * @param timeout
	 */
	private OracleSession(DBManager dbManager, NativeJdbcExtractor extractor)
	{
		super(dbManager, extractor);
	}

	public OracleSession()
	{

	}

	/**
	 * 
	 * Function    : 实例化方法
	 * LastUpdate  : 2010-5-21
	 * @param dbName
	 * @param txnName
	 * @param timeout
	 * @return
	 */
	public static OracleSession getInstance(DBManager dbManager, NativeJdbcExtractor extractor)
	{
		return new OracleSession(dbManager, extractor);
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#insertForLob(java.lang.String, java.util.List)
	 */
	public int insertForLob(String sql, List<Object> params)
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PreparedStatement ps = null;
		LobHandler handler = null;
		try
		{
			Connection conn = dbManager.getConnection();

			Connection conToUse = null;
			if (nativeJdbcExtractor != null)
			{
				conToUse = nativeJdbcExtractor.getNativeConnection(conn);
			}
			ps = conToUse.prepareStatement(sql);
			PreparedStatement psToUse = null;
			if (nativeJdbcExtractor != null)
			{
				psToUse = nativeJdbcExtractor.getNativePreparedStatement(ps);
			}
			if (null != params && params.size() > 0)
			{
				handler = new OracleLobHandler(conToUse);
				for (int i = 0; i < params.size(); i++)
				{
					setParamForLob(psToUse, i + 1, params.get(i), handler);
				}
			}
			return psToUse.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("update error!");
		}
		finally
		{
			try
			{
				handler.releaseLobs();
				closeResultSetAndStatment(null, ps);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#updateForLob(java.lang.String, java.util.List)
	 */
	public int updateForLob(String sql, List<Object> params) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PreparedStatement ps = null;
		LobHandler handler = null;
		try
		{
			Connection conn = dbManager.getConnection();
			Connection conToUse = null;
			//			NativeJdbcExtractor extractor = new WebSphereNativeJdbcExtractor();
			if (nativeJdbcExtractor != null)
			{
				conToUse = nativeJdbcExtractor.getNativeConnection(conn);
			}
			ps = conToUse.prepareStatement(sql);
			PreparedStatement psToUse = null;
			if (nativeJdbcExtractor != null)
			{
				psToUse = nativeJdbcExtractor.getNativePreparedStatement(ps);
			}
			if (null != params && params.size() > 0)
			{
				handler = new OracleLobHandler(conToUse);
				for (int i = 0; i < params.size(); i++)
				{
					setParamForLob(psToUse, i + 1, params.get(i), handler);
				}
			}
			return psToUse.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("update error!");
		}
		finally
		{
			closeResultSetAndStatment(null, ps);
		}
	}

	private void setParamForLob(PreparedStatement ps, int idx, Object obj, LobHandler handler) throws IOException, Exception
	{
		try
		{
			if (obj instanceof java.util.Date)
			{
				ps.setTimestamp(idx, new Timestamp(((java.util.Date) obj).getTime()));
				return;
			}
			if (obj instanceof InputStream)
			{
				InputStream is = (InputStream) obj;
				handler.setBlobAsBinaryStream(ps, idx, is, is.available());
				return;
			}
			if (obj instanceof java.sql.Clob)
			{
				ps.setClob(idx, (java.sql.Clob) obj);
				return;
			}
			if (obj instanceof java.math.BigDecimal)
			{
				ps.setBigDecimal(idx, (java.math.BigDecimal) obj);
				return;
			}
			if (obj instanceof java.math.BigInteger)
			{
				ps.setBigDecimal(idx, new BigDecimal((BigInteger) obj));
				return;
			}
			if (obj instanceof java.lang.Boolean)
			{
				ps.setInt(idx, (Boolean) obj ? 0 : 1);
				return;
			}
			if (obj instanceof java.lang.Integer)
			{
				ps.setInt(idx, ((Integer) obj).intValue());
				return;
			}
			if (obj instanceof java.lang.Long)
			{
				ps.setLong(idx, ((Long) obj).longValue());
				return;
			}
			if (obj instanceof java.lang.String)
			{
				ps.setString(idx, obj.toString());
				return;
			}
			ps.setObject(idx, obj);
		}
		catch (SQLException e)
		{
			logger.error("ps set params error!", e);
			throw new POException("ps set params error!", e);
		}
	}

	public NativeJdbcExtractor getNativeJdbcExtractor()
	{
		return nativeJdbcExtractor;
	}

	public void setNativeJdbcExtractor(NativeJdbcExtractor nativeJdbcExtractor)
	{
		this.nativeJdbcExtractor = nativeJdbcExtractor;
	}
}
