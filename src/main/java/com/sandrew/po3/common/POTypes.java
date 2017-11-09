/***************************************************************************************************
 * <pre>
* FILE : POTypes.java
* CLASS : POTypes
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
* 		  |2010-6-12| SuMMeR| Created |
* DESCRIPTION:
* </pre>
 **************************************************************************************************/
/**
 * $Id: POTypes.java,v 0.1 2010-6-12 ����03:16:45 SuMMeR Exp $
 */

package com.sandrew.po3.common;

import java.sql.Types;

import oracle.jdbc.OracleTypes;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-6-12
 * @version    :
 */
public class POTypes
{
	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>BIT</code>.
	 */
	public final static int BIT = Types.BIT;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>TINYINT</code>.
	 */
	public final static int TINYINT = Types.TINYINT;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>SMALLINT</code>.
	 */
	public final static int SMALLINT = Types.SMALLINT;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>INTEGER</code>.
	 */
	public final static int INTEGER = Types.INTEGER;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>BIGINT</code>.
	 */
	public final static int BIGINT = Types.BIGINT;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>FLOAT</code>.
	 */
	public final static int FLOAT = Types.FLOAT;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>REAL</code>.
	 */
	public final static int REAL = Types.REAL;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>DOUBLE</code>.
	 */
	public final static int DOUBLE = Types.DOUBLE;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>NUMERIC</code>.
	 */
	public final static int NUMERIC = Types.NUMERIC;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>DECIMAL</code>.
	 */
	public final static int DECIMAL = Types.DECIMAL;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>CHAR</code>.
	 */
	public final static int CHAR = Types.CHAR;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>VARCHAR</code>.
	 */
	public final static int VARCHAR = Types.VARCHAR;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>LONGVARCHAR</code>.
	 */
	public final static int LONGVARCHAR = Types.LONGVARCHAR;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>DATE</code>.
	 */
	public final static int DATE = Types.DATE;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>TIME</code>.
	 */
	public final static int TIME = Types.TIME;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>TIMESTAMP</code>.
	 */
	public final static int TIMESTAMP = Types.TIMESTAMP;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>BINARY</code>.
	 */
	public final static int BINARY = Types.BINARY;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>VARBINARY</code>.
	 */
	public final static int VARBINARY = Types.VARBINARY;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>LONGVARBINARY</code>.
	 */
	public final static int LONGVARBINARY = Types.LONGVARBINARY;

	/**
	 * <P>The constant in the Java programming language, sometimes referred
	 * to as a type code, that identifies the generic SQL type 
	 * <code>NULL</code>.
	 */
	public final static int NULL = Types.NULL;

	/**
	 * The constant in the Java programming language that indicates
	 * that the SQL type is database-specific and
	 * gets mapped to a Java object that can be accessed via
	 * the methods <code>getObject</code> and <code>setObject</code>.
	 */
	public final static int OTHER = Types.OTHER;

	/**
	 * The constant in the Java programming language, sometimes referred to
	 * as a type code, that identifies the generic SQL type
	 * <code>JAVA_OBJECT</code>.
	 * @since 1.2
	 */
	public final static int JAVA_OBJECT = Types.JAVA_OBJECT;

	/**
	 * The constant in the Java programming language, sometimes referred to
	 * as a type code, that identifies the generic SQL type
	 * <code>DISTINCT</code>.
	 * @since 1.2
	 */
	public final static int DISTINCT = Types.DISTINCT;

	/**
	 * The constant in the Java programming language, sometimes referred to
	 * as a type code, that identifies the generic SQL type
	 * <code>STRUCT</code>.
	 * @since 1.2
	 */
	public final static int STRUCT = Types.STRUCT;

	/**
	 * The constant in the Java programming language, sometimes referred to
	 * as a type code, that identifies the generic SQL type
	 * <code>ARRAY</code>.
	 * @since 1.2
	 */
	public final static int ARRAY = Types.ARRAY;

	/**
	 * The constant in the Java programming language, sometimes referred to
	 * as a type code, that identifies the generic SQL type
	 * <code>BLOB</code>.
	 * @since 1.2
	 */
	public final static int BLOB = Types.BLOB;

	/**
	 * The constant in the Java programming language, sometimes referred to
	 * as a type code, that identifies the generic SQL type
	 * <code>CLOB</code>.
	 * @since 1.2
	 */
	public final static int CLOB = Types.CLOB;

	/**
	 * The constant in the Java programming language, sometimes referred to
	 * as a type code, that identifies the generic SQL type
	 * <code>REF</code>.
	 * @since 1.2
	 */
	public final static int REF = Types.REF;
	

	/**
	 * The constant in the Java programming language, somtimes referred to
	 * as a type code, that identifies the generic SQL type <code>DATALINK</code>.
	 *
	 * @since 1.4
	 */
	public final static int DATALINK = Types.DATALINK;

	/**
	 * The constant in the Java programming language, somtimes referred to
	 * as a type code, that identifies the generic SQL type <code>BOOLEAN</code>.
	 *
	 * @since 1.4
	 */
	public final static int BOOLEAN = Types.BOOLEAN;
	
	/**
	 *  special type for Oracle's cursor
	 *  use for the procedure which return cursor
	 * The constant in the Java programming language, somtimes referred to
	 * as a type code, that identifies the generic SQL type <code>CURSOR</code>.
	 *
	 */
	public final static int CURSOR = OracleTypes.CURSOR;
	
	// Prevent instantiation
	private POTypes()
	{
	}

}
