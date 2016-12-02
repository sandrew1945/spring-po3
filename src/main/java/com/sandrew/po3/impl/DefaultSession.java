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

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.PublicKey;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.nativejdbc.NativeJdbcExtractor;

import com.sandrew.po3.Session;
import com.sandrew.po3.annotations.ColumnName;
import com.sandrew.po3.bean.PO;
import com.sandrew.po3.bean.PageResult;
import com.sandrew.po3.callback.DAOCallback;
import com.sandrew.po3.common.POMapping;
import com.sandrew.po3.common.POTypes;
import com.sandrew.po3.db.DBManager;
import com.sandrew.po3.exception.POException;
import com.sandrew.po3.exception.UnsupportedMethodException;
import com.sandrew.po3.sql.SqlCreator;
import com.sandrew.po3.sql.impl.DefaultSqlCreatorImpl;
import com.sandrew.po3.util.Constant;
import com.sandrew.po3.util.LobHandler;
import com.sandrew.po3.util.POUtil;
import com.sandrew.po3.util.Parameters;

import oracle.jdbc.driver.OracleCallableStatement;
import oracle.sql.BLOB;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-5-21
 * @version    :
 */
public class DefaultSession implements Session
{

	protected DBManager dbManager = null;

	protected NativeJdbcExtractor nativeJdbcExtractor;

	private static Logger logger = LogManager.getLogger(DefaultSession.class);

	/**
	 *  构造器
	 * @param dbName
	 * @param txnName
	 * @param timeout
	 */
	protected DefaultSession(DBManager dbManager, NativeJdbcExtractor extractor)
	{
		this.dbManager = dbManager;
		this.nativeJdbcExtractor = extractor;
	}

	public DefaultSession()
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
	public static DefaultSession getInstance(DBManager dbManager, NativeJdbcExtractor extractor)
	{
		return new DefaultSession(dbManager, extractor);
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.Session#callFunction()
	 */
	public Object callFunction(String functionName, List<Object> ins, int outType)
	{
		CallableStatement cast = null;
		try
		{
			// 拼装SQL
			String sql = getProdOrFuncSql(functionName, ins, null, false);
			logger.debug("SQL =====>" + sql);
			// 获取Connection
			Connection conn = dbManager.getConnection();
			cast = conn.prepareCall(sql);
			// 注册输出参数类型
			cast.registerOutParameter(1, outType);
			// 注册输入参数
			setProdOrFuncParameters(cast, ins, null, false);
			cast.execute();
			// 执行Function后获取输出参数
			List<Integer> outs = new LinkedList<Integer>();
			outs.add(outType);
			List<Object> retList = getProdOrFuncOutParams(cast, ins, outs, false);
			return retList.get(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("callFunction error!");
		}
		finally
		{
			closeResultSetAndStatment(null, cast);
		}
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.Session#callProcedure()
	 */
	public List<Object> callProcedure(String procedureName, List<Object> ins, List<Integer> outs)
	{
		CallableStatement cast = null;
		try
		{
			// 拼装SQL
			String sql = getProdOrFuncSql(procedureName, ins, outs, true);
			logger.debug("SQL =====>" + sql);
			// 获取Connection
			Connection conn = dbManager.getConnection();
			cast = conn.prepareCall(sql);
			// 注册输入及输出参数
			setProdOrFuncParameters(cast, ins, outs, true);
			cast.execute();
			// 执行Procedure后获取输出参数
			return getProdOrFuncOutParams(cast, ins, outs, true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("callFunction error!");
		}
		finally
		{
			closeResultSetAndStatment(null, cast);
		}
	}

	/*
	 * 	特殊的Procedure调用,只返回一个CURSOR
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#callProcedure(java.lang.String, java.util.List, com.autosys.po3.callback.DAOCallback)
	 */
	public <T> List<T> callProcedure(String procedureName, List<Object> ins, DAOCallback<T> callback)
	{
		List<T> list = new LinkedList<T>();
		try
		{
			// 输出参数为一个CURSOR
			List<Integer> outs = new ArrayList<Integer>();
			outs.add(POTypes.CURSOR);
			// 执行存储过程
			List<Object> retList = callProcedure(procedureName, ins, outs);
			// 返回一个ResultSet的List
			ResultSet rs = (ResultSet) retList.get(0);
			while (rs.next())
			{
				list.add(callback.wrapper(rs, list.size() + 1));
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return list;
	}

	/*
	 *  删除
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#delete(java.lang.String, java.util.List)
	 */
	public int delete(String sql, List<Object> params) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PreparedStatement ps = null;
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			return ps.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("delete error!");
		}
		finally
		{
			closeResultSetAndStatment(null, ps);
		}
	}

	/*
	 *  删除
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#delete(com.autosys.po3.bean.PO)
	 */
	public int delete(PO po)
	{
		// 获取PO的POMapping
		POMapping mapping = new POMapping(po);
		// 获取SQL
		SqlCreator creator = new DefaultSqlCreatorImpl();
		String sql = creator.deleteCreator(mapping, po);
		// 封装参数List
		List<Object> params = POUtil.encapParams(mapping, po);
		return delete(sql, params);
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.Session#getIntegerPK(java.lang.String)
	 */
	public Integer getIntegerPK(String sequenceName)
	{
		return new Integer(getStringPK(sequenceName));
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.Session#getLongPK(java.lang.String)
	 */
	public Long getLongPK(String sequenceName)
	{
		return new Long(getStringPK(sequenceName));
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.Session#getStringPK(java.lang.String)
	 */
	public String getStringPK(String sequenceName)
	{
		return getPK(sequenceName).toString();
	}

	/**
	 * 
	 * Function    : 根据SequenceName获取下个序列号,仅供getIntegerPK(),getLongPK(),getStringPK()调用
	 * LastUpdate  : 2010-6-25
	 * @param sequenceName
	 * @return	   : Object
	 */
	private Object getPK(String sequenceName)
	{
		Object seqNext = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		sql.append(sequenceName);
		sql.append(".NEXTVAL FROM DUAL");
		List<HashMap<String, Object>> list = select(sql.toString(), null);
		if (null != list && list.size() > 0)
		{
			seqNext = (list.get(0)).get("NEXTVAL");
		}
		return seqNext;
	}

	/* 
	 *  插入
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#insert(java.lang.String)
	 */
	public int insert(String sql, List<Object> params)
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PreparedStatement ps = null;
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			return ps.executeUpdate();
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

	/**
	 * 
	 * Function    : MySql与SqlServer获取自动增长主键的插入方法，不适用于Oracle
	 * LastUpdate  : 2016年12月2日
	 * @param sql
	 * @param params
	 * @param po
	 * @return
	 */
	private int insert(String sql, List<Object> params, PO po)
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PreparedStatement ps = null;
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			int count = ps.executeUpdate();
			
			// 执行完insert操作，获取生成的键值并赋值到PO中
			ResultSet rs = ps.getGeneratedKeys();
			// 如果有自增键值
			if (rs.next())
			{
				// 获取PO的全部字段，并将key值赋给自动增长列
				Class<? extends PO> clz = po.getClass();
				Field[] fields = clz.getDeclaredFields();
				int columnIndex = 1;
				for (int i = 0; i < fields.length; i++)
				{
					ColumnName columnName = fields[i].getAnnotation(ColumnName.class);
					// 如果列为自增并且该列还未赋值，则将自增值保存到PO中
					if ((true == columnName.autoIncrement()) && (null == POUtil.invokeGetMethodByField(po, fields[i].getName())))
					{
						int incrementKey = rs.getInt(columnIndex);
						POUtil.invokeSetMethodByField(po, fields[i].getName(), incrementKey);
						columnIndex++;
					}
				}
			}
			return count;
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
	 * @see com.autosys.po3.Session#insert(com.autosys.po3.bean.PO)
	 */
	public int insert(PO po)
	{
		// 获取PO的POMapping
		POMapping mapping = new POMapping(po);
		// 获取SQL
		SqlCreator creator = new DefaultSqlCreatorImpl();
		String sql = creator.insertCreator(mapping, po);
		// 封装参数List
		List<Object> params = POUtil.encapParams(mapping, po);
		return insert(sql, params, po);
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#insertForBatch(java.lang.String, java.util.List)
	 */
	public int[] insertForBatch(String sql, List<Parameters> parameters) throws POException
	{
		logger.debug("SQL =====>" + sql);
		PreparedStatement ps = null;
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			Iterator<Parameters> it = parameters.iterator();
			while (it.hasNext())
			{
				Parameters param = it.next();
				while (param.hasNext())
				{
					setParam(ps, param.getIndex(), param.getValue());
				}
				ps.addBatch();
			}
			return ps.executeBatch();
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
				if (null != ps)
				{
					ps.clearBatch();
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			closeResultSetAndStatment(null, ps);
		}
	}

	/* (non-Javadoc)
	 * @see com.autosys.po3.Session#pageQuery(java.lang.String)
	 */
	/*
	public <T> PageResult<T> pageQuery(String sql, List<Object> params, DAOCallback<T> callback, int pageSize, int curPage) throws DAOException
	{
		long totalS = System.currentTimeMillis();
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PageResult<T> ps = new PageResult<T>();
		ps.setCurPage(curPage);
		ps.setPageSize(pageSize);
		// 根据SQL设置总记录数
		long countS = System.currentTimeMillis();
		ps.setTotalRecords(18321);
		long countE = System.currentTimeMillis();
		logger.debug("PO3 TMP LOG COUNT COST =====>" +(countE - countS));
		// 根据每页的记录条数及总记录数设置分页情况
	//		StringBuilder sb = new StringBuilder();
	//		sb.append("SELECT * FROM (");
	//		sb.append("SELECT A.* ,ROWNUM R FROM (");
	//		sb.append(sql);
	//		sb.append(" ) A WHERE ROWNUM <= ? ");
	//		sb.append(") B WHERE R > ?");
		
		
		StringBuilder sb= new StringBuilder();
		sb.append("SELECT * FROM (SELECT A.* ,ROWNUM R FROM (select besi_part_id,\n" );
		sb.append("       parts_no,\n" );
		sb.append("       parts_name,\n" );
		sb.append("       is_imp,\n" );
		sb.append("       is_buyer,\n" );
		sb.append("       buyer_code,\n" );
		sb.append("       buyer_name,\n" );
		sb.append("       bussiness_code,\n" );
		sb.append("       is_supplier,\n" );
		sb.append("       supplier_code,\n" );
		sb.append("       supplier_name,\n" );
		sb.append("       is_partgroup,\n" );
		sb.append("       is_capacity,\n" );
		sb.append("       car_type,\n" );
		sb.append("       sy_factory,\n" );
		sb.append("       cc_factory\n" );
		sb.append("  from (\n" );
		sb.append("\n" );
		sb.append("        select tbp.besi_part_id, --主零件Id\n" );
		sb.append("                tbp.parts_no, --零件号\n" );
		sb.append("                tbp.parts_name, --零件名称\n" );
		sb.append("                (case\n" );
		sb.append("                  when tvp.v_parts_id is not null and tvp.status=10021001 then\n" );
		sb.append("                   1\n" );
		sb.append("                  else\n" );
		sb.append("                   0\n" );
		sb.append("                end) as is_imp, --是否是重点件\n" );
		sb.append("                (case\n" );
		sb.append("                  when buyer.user_id is not null and buyer.user_stat=10011001 then                   1\n" );
		sb.append("                  else\n" );
		sb.append("                   0\n" );
		sb.append("                end) is_buyer, --是否分配采购员\n" );
		sb.append("                buyer.user_code as buyer_code, --采购员Code\n" );
		sb.append("                buyer.user_name as buyer_name, --采购员名称\n" );
		sb.append("                buyer.bussiness_code as bussiness_code, --采购员代码\n" );
		sb.append("                (case\n" );
		sb.append("                  when vender.user_id is not null and vender.user_stat=10011001 then\n" );
		sb.append("                   1\n" );
		sb.append("                  else\n" );
		sb.append("                   0\n" );
		sb.append("                end) is_supplier, --是否分配供应商\n" );
		sb.append("                vender.user_code as supplier_code, --供应商代码\n" );
		sb.append("                vender.user_name as supplier_name, --供应商名称\n" );
		sb.append("                (case\n" );
		sb.append("                  when f.cc is not null and f.is_delete=10021001 then\n" );
		sb.append("                   1\n" );
		sb.append("                  else\n" );
		sb.append("                   0\n" );
		sb.append("                end) is_partgroup, --是否分配零件组\n" );
		sb.append("                (case\n" );
		sb.append("                  when f.cc is null then\n" );
		sb.append("                   0\n" );
		sb.append("                  when f.cc = 0 then\n" );
		sb.append("                   0\n" );
		sb.append("                  when f.cc = 1 and f.is_delete=10021001 then\n" );
		sb.append("                   1\n" );
		sb.append("                end) is_capacity, --是否维护能力\n" );
		sb.append("                d.car_type, --车型\n" );
		sb.append("                e.sy_factory, --使用工厂\n" );
		sb.append("                e.cc_factory --筹措工厂\n" );
		sb.append("          from (SELECT * FROM TM_BESI_PARTS WHERE status=10011001) tbp\n" );
		sb.append("          left join TM_V_PARTS tvp\n" );
		sb.append("            on tbp.besi_part_id = tvp.besi_part_id\n" );
		sb.append("          left join (select byr.besi_part_id,\n" );
		sb.append("                            byr.user_code,\n" );
		sb.append("                            byr.user_name,\n" );
		sb.append("                            byr.user_id,\n" );
		sb.append("                            byr.user_stat,\n" );
		sb.append("                            tbc.bussiness_code\n" );
		sb.append("                       from (select trbp.besi_part_id,\n" );
		sb.append("                                    btu.user_code,\n" );
		sb.append("                                    btu.user_name,\n" );
		sb.append("                                    btu.user_id,\n" );
		sb.append("                                    btu.user_stat,\n" );
		sb.append("                                    btu.bussiness_code\n" );
		sb.append("                               from TR_BUYER_PARTS trbp\n" );
		sb.append("                              inner join TM_USER btu\n" );
		sb.append("                                 on trbp.user_id = btu.user_id and btu.user_stat=10011001) byr\n" );
		sb.append("                      left join tm_bussiness_code tbc\n" );
		sb.append("                         on byr.bussiness_code = tbc.bussiness_code_id) buyer\n" );
		sb.append("            on tbp.besi_part_id = buyer.besi_part_id\n" );
		sb.append("          left join (select tpv.besi_part_id,\n" );
		sb.append("                            vtu.user_id,\n" );
		sb.append("                            vtu.user_code,\n" );
		sb.append("                            vtu.user_name,\n" );
		sb.append("                            vtu.user_stat                       from TR_PART_VENDOR tpv\n" );
		sb.append("                      inner join TM_USER vtu\n" );
		sb.append("                         on tpv.user_id = vtu.user_id and vtu.user_stat=10011001) vender\n" );
		sb.append("            on tbp.besi_part_id = vender.besi_part_id\n" );
		sb.append("          left join (select tpgd.besi_part_id,\n" );
		sb.append("                            (case\n" );
		sb.append("                              when tpgd.capacity > 0 then\n" );
		sb.append("                               1\n" );
		sb.append("                              else\n" );
		sb.append("                               0\n" );
		sb.append("                            end) cc,\n" );
		sb.append("                            tpg.is_delete                       from TT_PARTS_GROUP_DETAIL tpgd\n" );
		sb.append("                      inner join TT_PARTS_GROUP tpg\n" );
		sb.append("                         on tpgd.part_group_id = tpg.parts_group_id and tpg.is_delete=10021002 ) f\n" );
		sb.append("            on tbp.besi_part_id = f.besi_part_id\n" );
		sb.append("          left join M_VIEW_CAR_TYPE d\n" );
		sb.append("            on tbp.besi_part_id = d.besipartid\n" );
		sb.append("          left join M_VIEW_FACTORY e\n" );
		sb.append("            on tbp.besi_part_id = e.besi_part_id)\n" );
		sb.append(" where 1 = 1 ) A WHERE ROWNUM <= ? ) B WHERE R > ?");
	
		
		
		
		
		if (null == params)
		{
			params = new ArrayList<Object>();
		}
		// 设置分页SQL的参数
		params.add(new Integer(curPage * pageSize));
		params.add(new Integer((curPage - 1) * pageSize));
		long recordS = System.currentTimeMillis();
		List<T> records = select(sb.toString(), params, callback);
		long recordE = System.currentTimeMillis();
		logger.debug("PO3 TMP LOG RECORD COST =====>" +(recordE - recordS));
		ps.setRecords(records);
		// 获取总页数
		// 算法	总记录数 / 每页大小 + 如果 总记录数 % 每页大小 为 0 则 + 0 否则 + 1
		if (pageSize == 0)
		{
			throw new DAOException("pageSize can't be zero");
		}
		int pageCount = ps.getTotalRecords() / pageSize + (ps.getTotalRecords() % pageSize == 0 ? 0 : 1);
		ps.setTotalPages(pageCount);
		long totalE = System.currentTimeMillis();
		logger.debug("PO3 TMP LOG TOTAL COST =====>" +(totalE - totalS));
		return ps;
	}
	*/

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
		sb.append("SELECT * FROM (");
		sb.append("SELECT A.* ,ROWNUM R FROM (");
		sb.append(sql);
		sb.append(" ) A WHERE ROWNUM <= ? ");
		sb.append(") B WHERE R > ?");
		if (null == params)
		{
			params = new ArrayList<Object>();
		}
		// 设置分页SQL的参数
		params.add(new Integer(curPage * pageSize));
		params.add(new Integer((curPage - 1) * pageSize));
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
		long totalE = System.currentTimeMillis();
		return ps;
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#pageQuery(java.lang.String, java.util.List, int, int)
	 */
	public PageResult<HashMap<String, Object>> pageQuery(String sql, List<Object> params, int pageSize, int curPage, String filePath, boolean download) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PageResult<HashMap<String, Object>> ps = new PageResult<HashMap<String, Object>>();
		ps.setCurPage(curPage);
		ps.setPageSize(pageSize);
		// 根据SQL设置总记录数
		ps.setTotalRecords(count(sql, params));
		// 根据每页的记录条数及总记录数设置分页情况
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM (");
		sb.append("SELECT A.* ,ROWNUM R FROM (");
		sb.append(sql);
		sb.append(" ) A WHERE ROWNUM <= ? ");
		sb.append(") B WHERE R > ?");
		if (null == params)
		{
			params = new ArrayList<Object>();
		}
		// 设置分页SQL的参数
		params.add(new Integer(curPage * pageSize));
		params.add(new Integer((curPage - 1) * pageSize));
		List<HashMap<String, Object>> records = selectForLob(sb.toString(), params, filePath, download);
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
	 *  查询,返回结果为以列名为Key的HashMap
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#select(java.lang.String)
	 */
	public List<HashMap<String, Object>> select(String sql, List<Object> params) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<HashMap<String, Object>> results = null;
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			// 如果设置参数,设置查询参数
			if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			rs = ps.executeQuery();
			results = new ArrayList<HashMap<String, Object>>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCounts = rsmd.getColumnCount();
			while (rs.next())
			{
				HashMap<String, Object> result = new HashMap<String, Object>();
				for (int i = 1; i <= columnCounts; i++)
				{
					// TODO Fix by weibin 2011-12-26 support other column type 
					//result.put(rsmd.getColumnName(i), rs.getString(i));
					result.put(rsmd.getColumnName(i), getValueByType(rs, i, rsmd.getColumnType(i)));
				}
				results.add((HashMap<String, Object>) result);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("update error!");
		}
		finally
		{
			closeResultSetAndStatment(rs, ps);
		}
		return results;
	}

	/**
	 * 
	 * Function    : 支持BLOB的查询(临时使用,需要完善)
	 * LastUpdate  : 2010-10-2
	 * @param sql
	 * @param params
	 * @return
	 * @throws POException
	 */
	private List<HashMap<String, Object>> selectForLob(String sql, List<Object> params, String filePath, boolean download) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<HashMap<String, Object>> results = null;
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			// 如果设置参数,设置查询参数
			if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			rs = ps.executeQuery();
			results = new ArrayList<HashMap<String, Object>>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnCounts = rsmd.getColumnCount();
			while (rs.next())
			{
				HashMap<String, Object> result = new HashMap<String, Object>();
				for (int i = 1; i <= columnCounts; i++)
				{
					// 如果此字段为BLOB,则存储一个A连接
					if (rsmd.getColumnTypeName(i).equals("BLOB"))
					{
						if (download)
						{
							BLOB blob = (BLOB) rs.getBlob(i);
							BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
							File f = new File(filePath);
							if (!f.exists())
							{
								f.mkdirs();
							}
							FileOutputStream out = new FileOutputStream(filePath + File.separator + blob.toString());
							byte[] temp = new byte[1024];
							int c = -1;
							while ((c = in.read(temp)) != -1)
							{
								out.write(temp, 0, c);
							}
							out.flush();
							out.close();
							in.close();
							result.put(rsmd.getColumnName(i), "<a href='../../tempDoc/" + blob.toString() + "'>[" + rsmd.getColumnName(i) + "]</a>");
						}
						else
						{
							result.put(rsmd.getColumnName(i), rsmd.getColumnName(i));
						}
					}
					else
					{
						result.put(rsmd.getColumnName(i), rs.getString(i));
					}
				}
				results.add((HashMap<String, Object>) result);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("update error!");
		}
		finally
		{
			closeResultSetAndStatment(rs, ps);
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#select(com.autosys.po3.bean.PO)
	 */
	public List<HashMap<String, Object>> select(PO po) throws POException
	{
		// 获取PO的POMapping
		POMapping mapping = new POMapping(po);
		// 获取SQL
		SqlCreator creator = new DefaultSqlCreatorImpl();
		String sql = creator.selectCreator(mapping, po);
		List<Object> params = POUtil.encapParams(mapping, po);
		return select(sql, params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#selectForOrder(com.autosys.po3.bean.PO, java.lang.String, java.lang.String[])
	 */
	public List<HashMap<String, Object>> selectForOrder(PO po, String order, String... colNames) throws POException
	{
		// 获取PO的POMapping
		POMapping mapping = new POMapping(po);
		// 获取SQL
		SqlCreator creator = new DefaultSqlCreatorImpl();
		String sql = creator.selectCreatorForOrder(mapping, po, order, colNames);
		List<Object> params = POUtil.encapParams(mapping, po);
		return select(sql, params);
	}

	/*
	 *  查询,返回结果为DAOCallback中设定的bean
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#select(java.lang.String, com.autosys.po3.callback.DAOCallback)
	 */
	public <T> List<T> select(String sql, List<Object> params, DAOCallback<T> callback) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		ResultSet rs = null;
		PreparedStatement ps = null;
		List<T> list = new LinkedList<T>();
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			// 如果设置参数,设置查询参数
			if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			rs = ps.executeQuery();
			while (rs.next())
			{
				list.add(callback.wrapper(rs, list.size() + 1));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException("update error!");
		}
		finally
		{
			closeResultSetAndStatment(rs, ps);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#select(com.autosys.po3.bean.PO, com.autosys.po3.callback.DAOCallback)
	 */
	public <T> List<T> select(PO po, DAOCallback<T> callback) throws POException
	{
		// 获取PO的POMapping
		POMapping mapping = new POMapping(po);
		// 获取SQL
		SqlCreator creator = new DefaultSqlCreatorImpl();
		String sql = creator.selectCreator(mapping, po);
		List<Object> params = POUtil.encapParams(mapping, po);
		return select(sql, params, callback);
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#selectForOrder(com.autosys.po3.bean.PO, com.autosys.po3.callback.DAOCallback, java.lang.String, java.lang.String[])
	 */
	public <T> List<T> selectForOrder(PO po, DAOCallback<T> callback, String order, String... colNames) throws POException
	{
		// 获取PO的POMapping
		POMapping mapping = new POMapping(po);
		// 获取SQL
		SqlCreator creator = new DefaultSqlCreatorImpl();
		String sql = creator.selectCreatorForOrder(mapping, po, order, colNames);
		List<Object> params = POUtil.encapParams(mapping, po);
		return select(sql, params, callback);
	}

	/* 
	 *  更新 
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#update(java.lang.String)
	 */
	public int update(String sql, List<Object> params) throws POException
	{
		logger.debug("SQL =====>" + sql + " ; params:" + params);
		PreparedStatement ps = null;
		try
		{
			Connection conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			if (null != params && params.size() > 0)
			{
				for (int i = 0; i < params.size(); i++)
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			return ps.executeUpdate();
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

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#update(com.autosys.po3.bean.PO)
	 */
	public int update(PO cond, PO value) throws POException
	{
		// 获取PO的POMapping
		POMapping mapping = new POMapping(cond);
		// 获取SQL
		SqlCreator creator = new DefaultSqlCreatorImpl();
		String sql = creator.updateCreator(mapping, cond, value);
		// 封装参数List
		// 值参数
		LinkedList<Object> params = POUtil.encapParams(mapping, value, cond);
		// 条件参数
		return update(sql, params);
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#updateForLob(java.lang.String, java.util.List)
	 */
	public int updateForLob(String sql, List<Object> params) throws POException
	{
		throw new UnsupportedMethodException("不支持该方法");
	}

	/*
	 * (non-Javadoc)
	 * @see com.sandrew.po3.Session#commit()
	 */
	public void commit() throws POException
	{
		try
		{
			dbManager.endTxn(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException(e.getMessage(), e);
		}
		finally
		{
			try
			{
				dbManager.cleanTxn();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new POException(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.sandrew.po3.Session#rollback()
	 */
	public void rollback()
	{
		try
		{
			dbManager.endTxn(false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new POException(e.getMessage(), e);
		}
		finally
		{
			try
			{
				dbManager.cleanTxn();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new POException(e.getMessage(), e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#readBlob(java.lang.String, java.lang.String, java.util.List)
	 */
	public byte[] readBlob(String colName, String sql, List<Object> params) throws POException
	{
		if (colName == null || sql == null || params == null || params.size() == 0)
			throw new POException("parameters illegal!");
		logger.info("selectSql===>" + sql + ",params=" + params);
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = dbManager.getConnection();
			ps = conn.prepareStatement(sql);
			for (int i = 0; i < params.size(); i++)
			{
				if (!(params.get(i) instanceof InputStream))
				{
					setParam(ps, i + 1, params.get(i));
				}
			}
			rs = ps.executeQuery();
			if (rs.next())
			{
				BLOB blob = (BLOB) rs.getBlob(colName);
				BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] temp = new byte[1024];
				int c = -1;
				while ((c = in.read(temp)) != -1)
				{
					out.write(temp, 0, c);
				}
				out.flush();
				out.close();
				in.close();
				return out.toByteArray();
			}
			else
			{
				return null;
			}
		}
		catch (POException e)
		{
			logger.error(e.getMessage(), e);
			throw new POException(e);
		}
		catch (SQLException e)
		{
			logger.error(e.getMessage(), e);
			throw new POException(e);
		}
		catch (IOException e)
		{
			logger.error("read blob io error!" + e.getMessage(), e);
			throw new POException(e);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new POException(e);
		}
		finally
		{
			closeResultSetAndStatment(rs, ps);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.Session#getPrimaryByTabName(java.lang.String)
	 */
	/*
	private List<String> getPrimaryByTabName(String tabName) throws DAOException
	{
		Connection conn;
		List<String> primaryKeys = null;
		try
		{
			conn = DBManager.getConnection(dbName, txnName, timeout);
			DatabaseMetaData dmd = conn.getMetaData();
			ResultSet rs = dmd.getPrimaryKeys(null, null, "TM_DO_PRI");
			primaryKeys = new ArrayList<String>();
			while (rs.next())
			{
				primaryKeys.add(rs.getString("COLUMN_NAME"));
			}
			return primaryKeys;
		}
		catch (Exception e)
		{
			throw new DAOException("get Primary Key error!", e);
		}
	}
	*/
	/**
	 * 
	 * Function    : 关闭ResultSet,Statement
	 * LastUpdate  : 2010-5-21
	 * @param rs
	 * @param st
	 * @throws POException
	 */
	protected void closeResultSetAndStatment(ResultSet rs, Statement st) throws POException
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
			if (st != null)
			{
				st.close();
			}
		}
		catch (SQLException e)
		{
			throw new POException(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * Function    : 设置PreparedStatement的参数
	 * LastUpdate  : 2010-6-10
	 * @param ps
	 * @param idx
	 * @param obj
	 * @throws POException
	 */
	private void setParam(PreparedStatement ps, int idx, Object obj) throws POException
	{
		try
		{
			if (obj instanceof java.util.Date)
			{
				ps.setTimestamp(idx, new Timestamp(((java.util.Date) obj).getTime()));
				return;
			}
			if (obj instanceof java.sql.Blob)
			{
				ps.setBlob(idx, (java.sql.Blob) obj);
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

	/**
	 * 
	 * Function    : 获取查询的记录数
	 * LastUpdate  : 2010-6-10
	 * @param sql
	 * @param params
	 * @return
	 * @throws POException
	 */
	private int count(String sql, List<Object> params) throws POException
	{
		int count = 0;
		sql = "Select COUNT(*) as COUNT FROM (" + sql + ")";
		logger.info("SQL===>" + sql + ",params===>" + params);
		try
		{
			List<HashMap<String, Object>> list = select(sql, params);
			if (null != list && list.size() > 0)
			{
				count = Integer.valueOf((String) list.get(0).get("COUNT")).intValue();
			}
			return count;
		}
		catch (Exception e)
		{
			throw new POException("count(*) execute error!", e);
		}
	}

	/**
	 * 
	 * Function    : 生成调用Procedure或Function的SQL
	 * LastUpdate  : 2010-6-24
	 * @param functionName
	 * @param ins
	 * @param outs
	 * @param isProcedure
	 * @return
	 */
	private String getProdOrFuncSql(String functionName, List<Object> ins, List<Integer> outs, boolean isProcedure)
	{
		StringBuilder sB = new StringBuilder();
		if (isProcedure)
		{
			// 拼装存储过程的SQL
			sB.append(Constant.PROCEDURE_PREFIX).append(functionName);
			sB.append(getProdOrFuncParameters(ins, outs));
		}
		else
		{
			// 拼装Function的SQL
			sB.append(Constant.FUNCTION_PREFIX).append(functionName);
			sB.append(getProdOrFuncParameters(ins, outs));
		}
		sB.append(Constant.PROD_FUNC_SUFFIX);
		return sB.toString();
	}

	/**
	 * 
	 * Function    : 拼装Procedure或Function SQL的参数部分
	 * LastUpdate  : 2010-6-12
	 * @param ins
	 * @param outs
	 * @return
	 */
	private String getProdOrFuncParameters(List<Object> ins, List<Integer> outs)
	{
		StringBuilder sB = new StringBuilder();
		sB.append(Constant.PROD_FUNC_PARAMS_PREFIX);
		if (null != ins && ins.size() > 0)
		{
			for (int i = 0; i < ins.size(); i++)
			{
				if (hasParametersHead(sB.toString()))
				{
					sB.append(",?");
				}
				else
				{
					sB.append("?");
				}
			}
		}
		if (null != outs && outs.size() > 0)
		{
			for (int i = 0; i < outs.size(); i++)
			{
				if (hasParametersHead(sB.toString()))
				{
					sB.append(",?");
				}
				else
				{
					sB.append("?");
				}
			}
		}
		sB.append(Constant.PROD_FUNC_PARAMS_SUFFIX);
		return sB.toString();
	}

	/**
	 * 
	 * Function    : 设置Procedure或Function的输入,输出参数
	 * LastUpdate  : 2010-6-25
	 * @param cast
	 * @param ins
	 * @param outs
	 * @throws SQLException 
	 */
	private void setProdOrFuncParameters(CallableStatement cast, List<Object> ins, List<Integer> outs, boolean isProcedure) throws SQLException
	{
		if (null == cast)
		{
			throw new POException("CallableStatement isn't null");
		}
		int paramsPos = 1;
		if (null != ins)
		{
			for (int i = 0; i < ins.size(); i++)
			{
				if (isProcedure)
				{
					setParam(cast, paramsPos, ins.get(i));
				}
				else
				{
					setParam(cast, paramsPos + 1, ins.get(i));
				}
				paramsPos++;
			}
		}
		if (null != outs)
		{
			for (int i = 0; i < outs.size(); i++)
			{
				if (isProcedure)
				{
					registerOutParameter(cast, paramsPos, outs.get(i).intValue(), isProcedure);
				}
				else
				{
					throw new POException("Function can't set output parameters");
				}
				paramsPos++;
			}
		}
	}

	private void registerOutParameter(CallableStatement cast, int idx, int type, boolean isProcedure) throws SQLException
	{
		if (cast == null)
		{
			throw new POException("Register Out Parameter Error!");
		}

		switch (type)
		{
			case POTypes.INTEGER:
				cast.registerOutParameter(idx, POTypes.INTEGER);
				break;
			case POTypes.BIGINT:
				cast.registerOutParameter(idx, POTypes.BIGINT);
				break;
			case POTypes.ARRAY:
				break;
			case POTypes.BINARY:
				break;
			case POTypes.BIT:
				break;
			case POTypes.BLOB:
				// cast.registerOutParameter(idx, POTypes.BLOB);
				break;
			case POTypes.BOOLEAN:
				cast.registerOutParameter(idx, POTypes.BOOLEAN);
				break;
			case POTypes.CHAR:
				cast.registerOutParameter(idx, POTypes.CHAR);
				break;
			case POTypes.CLOB:
				break;
			case POTypes.DATALINK:
				break;
			case POTypes.DATE:
				cast.registerOutParameter(idx, POTypes.DATE);
				break;
			case POTypes.DECIMAL:
				cast.registerOutParameter(idx, POTypes.DECIMAL);
				break;
			case POTypes.DISTINCT:
				break;
			case POTypes.DOUBLE:
				cast.registerOutParameter(idx, POTypes.DOUBLE);
				break;
			case POTypes.FLOAT:
				cast.registerOutParameter(idx, POTypes.FLOAT);
				break;
			case POTypes.JAVA_OBJECT:
				break;
			case POTypes.LONGVARBINARY:
				break;
			case POTypes.LONGVARCHAR:
				break;
			case POTypes.NULL:
				cast.registerOutParameter(idx, POTypes.NULL);
				break;
			case POTypes.NUMERIC:
				cast.registerOutParameter(idx, POTypes.NUMERIC);
				break;
			case POTypes.OTHER:
				break;
			case POTypes.REAL:
				cast.registerOutParameter(idx, POTypes.REAL);
				break;
			case POTypes.REF:
				cast.registerOutParameter(idx, POTypes.REF);
				break;
			case POTypes.SMALLINT:
				cast.registerOutParameter(idx, POTypes.SMALLINT);
				break;
			case POTypes.STRUCT:
				// cast.registerOutParameter(idx, POTypes.STRUCT);
				break;
			case POTypes.TIME:
				cast.registerOutParameter(idx, POTypes.TIME);
				break;
			case POTypes.TIMESTAMP:
				cast.registerOutParameter(idx, POTypes.TIMESTAMP);
				break;
			case POTypes.TINYINT:
				break;
			case POTypes.VARBINARY:
				break;
			case POTypes.VARCHAR:
				cast.registerOutParameter(idx, POTypes.VARCHAR);
				break;
			case POTypes.CURSOR:
				cast.registerOutParameter(idx, POTypes.CURSOR);
				break;
		}
	}

	/**
	 * 
	 * Function    : 验证参数是否开始拼装
	 * LastUpdate  : 2010-6-12
	 * @param paramters
	 * @return
	 */
	private boolean hasParametersHead(String paramters)
	{
		return paramters.startsWith(Constant.PROD_FUNC_PARAMS_PREFIX + "?");
	}

	/**
	 * 
	 * Function    : 获取Procedure或Function的输出
	 * LastUpdate  : 2010-6-12
	 * @param cast
	 * @param ins
	 * @param outs
	 * @param isProcedure
	 * @return
	 * @throws SQLException
	 */
	private List<Object> getProdOrFuncOutParams(CallableStatement cast, List<Object> ins, List<Integer> outs, boolean isProcedure) throws SQLException
	{
		if (outs != null)
		{
			List<Object> retList = new LinkedList<Object>();
			int begin = ins == null ? 0 : ins.size();
			for (int i = 0; i < outs.size(); i++)
			{
				int index = begin + i + 1;
				switch (outs.get(i))
				{
					case POTypes.ARRAY:
						break;
					case POTypes.BIGINT:
						if (!isProcedure)
						{
							retList.add(i, cast.getLong(1));
						}
						else
						{
							retList.add(i, cast.getLong(index));
						}
						break;
					case POTypes.BINARY:
						break;
					case POTypes.BIT:
						break;
					case POTypes.BLOB:
						break;
					case POTypes.BOOLEAN:
						if (!isProcedure)
						{
							retList.add(i, cast.getBoolean(1));
						}
						else
						{
							retList.add(i, cast.getBoolean(index));
						}
						break;
					case POTypes.CHAR:
						if (!isProcedure)
						{
							retList.add(i, cast.getString(1));
						}
						else
						{
							retList.add(i, cast.getString(index));
						}
						retList.add(i, cast.getString(index));
						break;
					case POTypes.CLOB:
						break;
					case POTypes.DATALINK:
						break;
					case POTypes.DATE:
						if (!isProcedure)
						{
							retList.add(i, cast.getDate(1));
						}
						else
						{
							retList.add(i, cast.getDate(index));
						}
						break;
					case POTypes.DECIMAL:
						if (!isProcedure)
						{
							retList.add(i, cast.getBigDecimal(1));
						}
						else
						{
							retList.add(i, cast.getBigDecimal(index));
						}
						break;
					case POTypes.DISTINCT:
						break;
					case POTypes.DOUBLE:
						if (!isProcedure)
						{
							retList.add(i, cast.getDouble(1));
						}
						else
						{
							retList.add(i, cast.getDouble(index));
						}
						break;
					case POTypes.FLOAT:
						if (!isProcedure)
						{
							retList.add(i, cast.getFloat(1));
						}
						else
						{
							retList.add(i, cast.getFloat(index));
						}
						break;
					case POTypes.INTEGER:
						if (!isProcedure)
						{
							retList.add(i, cast.getInt(1));
						}
						else
						{
							retList.add(i, cast.getInt(index));
						}
						break;
					case POTypes.JAVA_OBJECT:
						break;
					case POTypes.LONGVARBINARY:
						break;
					case POTypes.LONGVARCHAR:
						break;
					case POTypes.NULL:// null值的处理
						retList.add(i, null);
						break;
					case POTypes.NUMERIC:
						if (!isProcedure)
						{
							retList.add(i, cast.getBigDecimal(1));
						}
						else
						{
							retList.add(i, cast.getBigDecimal(index));
						}
						break;
					case POTypes.OTHER:
						break;
					case POTypes.REAL:
						if (!isProcedure)
						{
							retList.add(i, cast.getFloat(1));
						}
						else
						{
							retList.add(i, cast.getFloat(index));
						}
						break;
					case POTypes.REF:
						break;
					case POTypes.SMALLINT:
						if (!isProcedure)
						{
							retList.add(i, cast.getInt(1));
						}
						else
						{
							retList.add(i, cast.getInt(index));
						}
						break;
					case POTypes.STRUCT:
						break;
					case POTypes.TIME:
						if (!isProcedure)
						{
							retList.add(i, cast.getTime(1));
						}
						else
						{
							retList.add(i, cast.getTime(index));
						}
						break;
					case POTypes.TIMESTAMP:
						if (!isProcedure)
						{
							retList.add(i, cast.getTimestamp(1));
						}
						else
						{
							retList.add(i, cast.getTimestamp(index));
						}
						break;
					case POTypes.TINYINT:
						break;
					case POTypes.VARBINARY:
						break;
					case POTypes.VARCHAR:
						if (!isProcedure)
						{
							retList.add(i, cast.getString(1));
						}
						else
						{
							retList.add(i, cast.getString(index));
						}
						break;
					case POTypes.CURSOR:
						if (!isProcedure)
						{
							throw new RuntimeException("Function can't return cursor");
						}
						else
						{
							ResultSet rs = ((OracleCallableStatement) cast).getCursor(index);
							retList.add(i, rs);
						}
						break;
				}
			}
			return retList;
		}
		else
		{
			return null;
		}
	}

	/**
	 * 
	 * Function    : 根据数据库字端类型,获取值
	 * LastUpdate  : 2011-12-26
	 * @param rs
	 * @param idx
	 * @param type
	 * @return
	 * @throws SQLException
	 */
	private Object getValueByType(ResultSet rs, int idx, int type) throws SQLException
	{
		if (rs == null)
		{
			throw new POException("Get Value Error!");
		}

		switch (type)
		{
			case POTypes.INTEGER:
				return rs.getInt(idx);
			case POTypes.BIGINT:
				return rs.getInt(idx);
			case POTypes.ARRAY:
				break;
			case POTypes.BINARY:
				break;
			case POTypes.BIT:
				break;
			case POTypes.BLOB:
				return rs.getBlob(idx);
			case POTypes.BOOLEAN:
				break;
			case POTypes.CHAR:
				break;
			case POTypes.CLOB:
				return rs.getClob(idx);
			case POTypes.DATALINK:
				break;
			case POTypes.DATE:
				return rs.getTime(idx);
			case POTypes.DECIMAL:
				break;
			case POTypes.DISTINCT:
				break;
			case POTypes.DOUBLE:
				return rs.getDouble(idx);
			case POTypes.FLOAT:
				rs.getFloat(idx);
				break;
			case POTypes.JAVA_OBJECT:
				break;
			case POTypes.LONGVARBINARY:
				break;
			case POTypes.LONGVARCHAR:
				break;
			case POTypes.NULL:
				return rs.getString(idx);
			case POTypes.NUMERIC:
				return rs.getString(idx);
			case POTypes.OTHER:
				break;
			case POTypes.REAL:
				break;
			case POTypes.REF:
				break;
			case POTypes.SMALLINT:
				break;
			case POTypes.STRUCT:
				// cast.registerOutParameter(idx, POTypes.STRUCT);
				break;
			case POTypes.TIME:
				return rs.getTimestamp(idx);
			case POTypes.TIMESTAMP:
				return rs.getTimestamp(idx);
			case POTypes.TINYINT:
				break;
			case POTypes.VARBINARY:
				break;
			case POTypes.VARCHAR:
				return rs.getString(idx);
		}
		return null;
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
