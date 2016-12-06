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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.sandrew.po3.bean.PageResult;
import com.sandrew.po3.callback.DAOCallback;
import com.sandrew.po3.db.DBManager;
import com.sandrew.po3.exception.POException;
import com.sandrew.po3.exception.UnsupportedMethodException;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-5-21
 * @version    :
 */
public class MySqlSession extends DefaultSession
{

	private static Logger logger = LogManager.getLogger(MySqlSession.class);

	protected DBManager dbManager = null;

	/**
	 *  构造器
	 * @param dbName
	 * @param txnName
	 * @param timeout
	 */
	private MySqlSession(DBManager dbManager, NativeJdbcExtractor extractor)
	{
		super(dbManager, extractor);
	}

	public MySqlSession()
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
	public static MySqlSession getInstance(DBManager dbManager, NativeJdbcExtractor extractor)
	{
		return new MySqlSession(dbManager, extractor);
	}
	
	

	public <T> PageResult<T> pageQuery(String sql, List<Object> params, DAOCallback<T> callback, int pageSize, int curPage) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PageResult<T> ps = new PageResult<T>();
		ps.setCurPage(curPage);
		ps.setPageSize(pageSize);
		ps.setTotalRecords(count(sql, params));
		// 根据SQL设置总记录数
		// 根据每页的记录条数及总记录数设置分页情况
		StringBuilder sb = new StringBuilder();
		sb.append(sql);
		sb.append(" LIMIT ?, ?");
		if (null == params)
		{
			params = new ArrayList<Object>();
		}
		// 设置分页SQL的参数
		params.add(new Integer(pageSize * (curPage - 1)));
		params.add(new Integer(pageSize));
		List<T> records = select(sb.toString(), params, callback);
		ps.setRecords(records);
		// 获取总页数
		// 算法	总记录数 / 每页大小 + 如果 总记录数 % 每页大小 为 0 则 + 0 否则 + 1
		if (pageSize == 0)
		{
			throw new POException("pageSize can't be zero");
		}
		int pageCount = ps.getTotalRecords() / pageSize + (ps.getTotalRecords() % pageSize == 0 ? 0 : 1);
		ps.setTotalPages(pageCount);
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#insertForLob(java.lang.String, java.util.List)
	 */
	public int insertForLob(String sql, List<Object> params)
	{
		throw new UnsupportedMethodException("不支持该方法");
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#updateForLob(java.lang.String, java.util.List)
	 */
	public int updateForLob(String sql, List<Object> params) throws POException
	{
		throw new UnsupportedMethodException("MySQL不支持该方法");
	}
	
	
	

	@Override
	public Integer getIntegerPK(String sequenceName) throws POException
	{
		throw new UnsupportedMethodException("MySQL数据库不支持该方法");
	}

	@Override
	public Long getLongPK(String sequenceName) throws POException
	{
		throw new UnsupportedMethodException("MySQL数据库不支持该方法");
	}

	@Override
	public String getStringPK(String sequenceName) throws POException
	{
		throw new UnsupportedMethodException("MySQL数据库不支持该方法");
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
