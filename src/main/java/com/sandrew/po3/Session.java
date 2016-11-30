/***************************************************************************************************
 * <pre>
* FILE : Session.java
* CLASS : Session
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
 **************************************************************************************************/
/**
 * $Id: Session.java,v 0.1 2010-4-16 下午02:18:12 SuMMeR Exp $
 */

package com.sandrew.po3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sandrew.po3.bean.PO;
import com.sandrew.po3.bean.PageResult;
import com.sandrew.po3.callback.DAOCallback;
import com.sandrew.po3.exception.POException;
import com.sandrew.po3.util.Parameters;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-4-16
 * @version    :
 */
public interface Session
{
	/**
	 * 
	 * Function    : 通过SQL的insert方法
	 * LastUpdate  : 2010-9-13
	 * @param sql		SQL
	 * @param params	参数集合
	 * @return			插入记录数
	 */
	public int insert(String sql, List<Object> params);

	/**
	 * 
	 * Function    : 通过SQL的insert方法(LOB版本)
	 * LastUpdate  : 2011-12-21
	 * @param sql
	 * @param params
	 * @return
	 */
	public int insertForLob(String sql, List<Object> params);

	/**
	 * 
	 * Function    : 通过PO的insert方法
	 * LastUpdate  : 2010-9-13
	 * @param po		PO对象
	 * @return			插入记录数
	 * @throws POException
	 */
	public int insert(PO po) throws POException;

	/**
	 * 
	 * Function    : 批量插入
	 * LastUpdate  : 2013-4-25
	 * @param sql
	 * @param parameters
	 * @return
	 * @throws POException
	 */
	public int[] insertForBatch(String sql, List<Parameters> parameters) throws POException;

	/**
	 * 
	 * Function    : 通过SQL的delete方法
	 * LastUpdate  : 2010-9-13
	 * @param sql		SQL
	 * @param params	参数集合
	 * @return			删除记录数
	 * @throws POException
	 */
	public int delete(String sql, List<Object> params) throws POException;

	/**
	 * 
	 * Function    : 通过PO的delete方法
	 * LastUpdate  : 2010-9-13
	 * @param po		PO对象
	 * @return			删除记录数
	 * @throws POException
	 */
	public int delete(PO po) throws POException;

	/**
	 * 
	 * Function    : 通过SQL的update方法
	 * LastUpdate  : 2010-9-13
	 * @param sql		SQL
	 * @param params	参数集合
	 * @return			更新记录数
	 * @throws POException
	 */
	public int update(String sql, List<Object> params) throws POException;

	/**
	 * 
	 * Function    : 通过PO的update操作
	 * LastUpdate  : 2010-9-13
	 * @param cond		作为where条件的PO对象
	 * @param value		作为更新值的PO对象
	 * @return			更新记录数
	 * @throws POException
	 */
	public int update(PO cond, PO value) throws POException;

	/**
	 * 
	 * Function    : 通过SQL的update方法(LOB版本)
	 * LastUpdate  : 2011-12-22
	 * @param sql
	 * @param params
	 * @return
	 * @throws POException
	 */
	public int updateForLob(String sql, List<Object> params) throws POException;

	/**
	 * 
	 * Function    : 通过SQL的select操作
	 * LastUpdate  : 2010-9-13
	 * @param sql		SQL
	 * @param params	参数集合
	 * @return			返回一个装载着Map的集合，Map的Key为表列名，Value为此列的值
	 * @throws POException
	 */
	public List<HashMap<String, Object>> select(String sql, List<Object> params) throws POException;

	/**
	 * 
	 * Function    : 通过PO的select操作
	 * LastUpdate  : 2010-9-13
	 * @param po		PO对象
	 * @return			返回一个装载着Map的集合，Map的Key为表列名，Value为此列的值
	 * @throws POException
	 */
	public List<HashMap<String, Object>> select(PO po) throws POException;

	/**
	 * 
	 * Function    : 通过PO的排序select操作
	 * LastUpdate  : 2010-9-13
	 * @param po		PO对象
	 * @param order		排序方式 ASC|DESC
	 * @param colNames	排序字段名，可多个
	 * @return
	 * @throws POException
	 */
	public List<HashMap<String, Object>> selectForOrder(PO po, String order, String... colNames) throws POException;

	/**
	 * 
	 * Function    : 通过SQL的select操作
	 * LastUpdate  : 2010-9-13
	 * @param <T>
	 * @param sql		SQL
	 * @param params	参数集合
	 * @param callback	回调函数，将结果集封装到PO或者自定义Bean里
	 * @return			装载着PO或自定义Bean的集合
	 * @throws POException
	 */
	public <T> List<T> select(String sql, List<Object> params, DAOCallback<T> callback) throws POException;

	/**
	 * 
	 * Function    : 通过PO的select操作
	 * LastUpdate  : 2010-9-13
	 * @param <T>
	 * @param po		PO对象
	 * @param callback	回调函数
	 * @return			装载着PO或自定义Bean的集合
	 * @throws POException
	 */
	public <T> List<T> select(PO po, DAOCallback<T> callback) throws POException;

	/**
	 * 
	 * Function    : 通过PO的排序select操作
	 * LastUpdate  : 2010-9-13
	 * @param <T>
	 * @param po		PO对象
	 * @param callback	回调函数
	 * @param order		排序方式 ASC|DESC
	 * @param colNames	排序字段，可多个
	 * @return
	 * @throws POException
	 */
	public <T> List<T> selectForOrder(PO po, DAOCallback<T> callback, String order, String... colNames) throws POException;

	/**
	 * 
	 * Function    : 分页查询操作
	 * LastUpdate  : 2010-9-13
	 * @param <T>
	 * @param sql		SQL
	 * @param params	参数集合
	 * @param callback	回调函数
	 * @param pageSize	页面显示记录数
	 * @param curPage	当前页
	 * @return			装载着PageResult对象的集合
	 * @throws POException
	 */
	public <T> PageResult<T> pageQuery(String sql, List<Object> params, DAOCallback<T> callback, int pageSize, int curPage) throws POException;

	/**
	 * 
	 * Function    : 分页查询操作(无CallBack)
	 * LastUpdate  : 2010-10-2
	 * @param <T>
	 * @param sql
	 * @param params
	 * @param pageSize
	 * @param curPage
	 * @return
	 * @throws POException
	 */
	public PageResult<HashMap<String, Object>> pageQuery(String sql, List<Object> params, int pageSize, int curPage, String filePath, boolean download) throws POException;

	/**
	 * 
	 * Function    : 调用Function
	 * LastUpdate  : 2010-9-13
	 * @param functionName	Function名称
	 * @param ins			输入参数集合
	 * @param outType		输出类型
	 * @return
	 */
	public Object callFunction(String functionName, List<Object> ins, int outType);

	/**
	 * 
	 * Function    : 调用Procedure
	 * LastUpdate  : 2010-9-13
	 * @param procedureName	Procedure名称
	 * @param ins			输入参数集合
	 * @param outs			输出参数集合
	 * @return				输出结果集合
	 */
	public List<Object> callProcedure(String procedureName, List<Object> ins, List<Integer> outs);

	/**
	 * 
	 * Function    : 特殊的Procedure调用,只返回一个CURSOR
	 * LastUpdate  : 2010-9-13
	 * @param <T>
	 * @param procedureName	Procedure名称
	 * @param ins			输入参数集合
	 * @param callback		回调函数
	 * @return				装载着PO或自定义bean的集合
	 */
	public <T> List<T> callProcedure(String procedureName, List<Object> ins, DAOCallback<T> callback);

	/**
	 * 
	 * Function    : 获取Long类型序列ID
	 * LastUpdate  : 2010-9-13
	 * @param sequenceName	sequence名称
	 * @return
	 */
	public Long getLongPK(String sequenceName);

	/**
	 * 
	 * Function    : 获取Integer类型序列ID
	 * LastUpdate  : 2010-9-13
	 * @param sequenceName	sequence名称
	 * @return
	 */
	public Integer getIntegerPK(String sequenceName);

	/**
	 * 
	 * Function    : 获取String类型序列ID
	 * LastUpdate  : 2010-9-13
	 * @param sequenceName	sequence名称
	 * @return
	 */
	public String getStringPK(String sequenceName);

	/**
	 * 
	 * Function    : 提交
	 * LastUpdate  : 2016年11月29日
	 */
	public void commit();

	/**
	 * 
	 * Function    : 回滚
	 * LastUpdate  : 2016年11月29日
	 */
	public void rollback();

	/**
	 * 
	 * Function    : 读取BLOB字段
	 * LastUpdate  : 2010-10-2
	 * @param colName
	 * @param sql
	 * @param params
	 * @return
	 */
	public byte[] readBlob(String colName, String sql, List<Object> params) throws POException;

	/**
	 * 
	 * Function    : 获取数据库表的主键
	 * LastUpdate  : 2010-10-3
	 * @param tabName
	 * @return
	 * @throws DAOException
	 */
	//public List<String> getPrimaryByTabName(String tabName) throws DAOException;;
}
