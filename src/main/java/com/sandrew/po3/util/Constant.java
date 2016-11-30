/***************************************************************************************************
 * <pre>
 * FILE : Constant.java
 * CLASS : Constant
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
 **************************************************************************************************/
/**
 * $Id: Constant.java,v 0.1 2010-4-15 下午02:37:44 SuMMeR Exp $
 */

package com.sandrew.po3.util;

/**
 * Function :
 * 
 * @author : SuMMeR CreateDate : 2010-4-15
 * @version :
 */
public interface Constant
{
	/**
	 * 数据库类型
	 */
	final String DATABASE_TYPE_ORACLE = "oracle";

	final String DATABASE_TYPE_DB2 = "db2";

	final String DATABASE_TYPE_MSSQL = "mssql";
	
	final String DATABASE_TYPE_MYSQL = "mysql";

	/**
	 *  数据库配置参数
	 */
	// 数据库配置文件的位置
	final int NODE_DB_CONFIG = 0;

	// 绝对路径的位置
	final int NODE_REAL_PATH = 0;

	// PO包名的位置
	final int NODE_PACKAGE_NAME = 0;

	// 数据库配置文件解析参数
	final String DB_DRIVER = "DB_DRIVER";

	final String DB_URL = "DB_URL";

	final String DB_USER = "DB_USER";

	final String DB_PASSWORD = "DB_PASSWORD";

	// 将全部表生成PO的标识
	final String DB_TABLES_ALL = "ALL";

	/**
	 * 生成java类型常量
	 */
	// SCALE为0
	final int JAVA_TYPE_SCALE_0 = 0;

	// PRECISION为0
	final int JAVA_TYPE_PRECISION_0 = 0;

	// Integer类型的最大长度
	final int JAVA_TYPE_INTEGER_MAX = 9;

	// Long类型的最小长度
	final int JAVA_TYPE_LONG_MIN = 9;

	// Long类型的最大长度
	final int JAVA_TYPE_LONG_MAX = 19;

	// Long类型的最大长度
	final int JAVA_TYPE_FLOAT_MAX = 8;

	// Long类型的最大小数位
	final int JAVA_TYPE_FLOAT_SCALE_MAX = 4;

	/**
	 * 引用的类
	 */
	final int IMPORT_CLASS_YES = 1;

	final int IMPORT_CLASS_NO = 0;

	final String DBINFO_BEAN = "DBInfo";

	/**
	 * Procedure及Function
	 */
	// 参数前缀
	final String PROD_FUNC_PARAMS_PREFIX = "(";

	// 参数后缀
	final String PROD_FUNC_PARAMS_SUFFIX = ")";

	// Function前缀
	final String FUNCTION_PREFIX = "{? = call ";

	// Procedure前缀
	final String PROCEDURE_PREFIX = "{ call ";

	// Procedure及Function后缀
	final String PROD_FUNC_SUFFIX = "}";
	
	
	/**
	 * 	PO
	 */
	final String PO_SUFFIX = "PO";
}
