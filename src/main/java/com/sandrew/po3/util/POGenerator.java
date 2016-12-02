/***************************************************************************************************
 * <pre>
* FILE : POGenerator.java
* CLASS : POGenerator
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
* 		  |2010-4-20| SuMMeR| Created |
* DESCRIPTION:
* </pre>
 **************************************************************************************************/
/**
 * $Id: POGenerator.java,v 0.1 2010-4-20 下午11:12:52 SuMMeR Exp $
 */

package com.sandrew.po3.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import com.sandrew.po3.annotations.ColumnName;
import com.sandrew.po3.db.DBConnection;
import com.sandrew.po3.db.JdbcConnection;
import com.sandrew.po3.gen.configure.Configure;
import com.sandrew.po3.gen.configure.ConfigureFileParser;
import com.sandrew.po3.gen.configure.xml.XmlParser;
import com.sandrew.po3.gen.table.Field;
import com.sandrew.po3.gen.table.Table;
import com.sandrew.po3.gen.table.TableParser;
import com.sandrew.po3.gen.table.impl.DefaultTableParser;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2010-4-20
 * @version    :
 */
public class POGenerator
{
	// 文件Comment
	private String fileComments = "";

	// 类所引用的package
	private String packAndImp = "package {0};\n\n" + "{1,choice,0#|1#import java.util.Date;\n}" + "{2,choice,0#|1#import java.sql.Blob;\n}" + "{3,choice,0#|1#import java.sql.Clob;\n}"
			+ "import com.sandrew.po3.bean.PO;\n";

	// 类的头部
	private String classHead = "@TableName(\"{0}\")\npublic class {1} extends PO\n" + "'{'" + "\n";

	// 属性
	private String attribute = "\t@ColumnName(value=\"{0}\", autoIncrement={1})\n\tprivate {2} {3};\n";

	// getXXX方法
	private String getMethod = "\tpublic {0} {1}()\n\t" + "'{'\n\t\t" + "return this.{2};" + "\n\t}\n";

	// setXXX方法
	private String setMethod = "\tpublic void {0}({1} {2})\n" + "\t'{'\n" + "\t\tthis.{2} = {2};\n" + "\t}\n";

	// 类的尾部
	private String classTail = "}";

	//private string 
	private Configure conf;

	/**
	 * 
	 * Function    : 生成PO类
	 * LastUpdate  : 2010-4-30
	 * @param databaseType
	 * @throws Exception 
	 */
	private void gen() throws Exception
	{
		// 获取配置信息
		ConfigureFileParser parser = new XmlParser();
		conf = parser.parser();
		DBConnection dc = null;
		TableParser tableParser = null;
		// 获取数据库连接
		if (Constant.DATABASE_TYPE_ORACLE.equalsIgnoreCase(conf.getDataBaseType()))
		{
			dc = new JdbcConnection(conf);
			tableParser = new DefaultTableParser();
		}
		else if (Constant.DATABASE_TYPE_DB2.equalsIgnoreCase(conf.getDataBaseType()))
		{
			// TODO 未实现
		}
		else if (Constant.DATABASE_TYPE_MSSQL.equalsIgnoreCase(conf.getDataBaseType()))
		{
			dc = new JdbcConnection(conf);
			tableParser = new DefaultTableParser();
		}
		else if (Constant.DATABASE_TYPE_MYSQL.equalsIgnoreCase(conf.getDataBaseType()))
		{
			dc = new JdbcConnection(conf);
			tableParser = new DefaultTableParser();
		}
		else
		{
			throw new Exception("暂时不支持" + conf.getDataBaseType() + "数据库");
		}

		// 生成PO
		List<Table> tableList = tableParser.parserTable(conf);
		Iterator<Table> it = tableList.iterator();
		while (it.hasNext())
		{
			Table table = it.next();
			StringBuilder poFile = new StringBuilder();
			// 判断都需要那些引用
			int impDate = Constant.IMPORT_CLASS_NO;
			int impBlob = Constant.IMPORT_CLASS_NO;
			int impClob = Constant.IMPORT_CLASS_NO;

			// 属性
			StringBuilder attributes = new StringBuilder();
			// setXXX,setXXX方法
			StringBuilder methods = new StringBuilder();

			List<Field> fields = table.getFields();
			Iterator<Field> fieldIt = fields.iterator();
			while (fieldIt.hasNext())
			{
				Field field = fieldIt.next();
				// 遍历表中所有字段，判断所需要引用
				Class<?> cls = field.getFieldType();
				if ((java.util.Date.class).getName().equals(cls.getName()))
				{
					impDate = Constant.IMPORT_CLASS_YES;
				}
				if ((java.sql.Clob.class).getName().equals(cls.getName()))
				{
					impClob = Constant.IMPORT_CLASS_YES;
				}
				if ((java.sql.Blob.class).getName().equals(cls.getName()))
				{
					impBlob = Constant.IMPORT_CLASS_YES;
				}
				// 遍历表中所有字段,获取PO的属性集合
				attributes.append(MessageFormat.format(attribute, field.getFieldName(), field.isAutoIncrement(), field.getFieldType().getSimpleName(), field.getAttributeName()));
				// 获取所有字段的setXXX和getXXX方法
				methods.append(MessageFormat.format(getMethod, field.getFieldType().getSimpleName(), POUtil.getMethodOfGetByFieldName(field.getAttributeName()), field.getAttributeName()));
				methods.append(MessageFormat.format(setMethod, POUtil.getMethodOfSetByFieldName(field.getAttributeName()), field.getFieldType().getSimpleName(), field.getAttributeName()));
			}
			// 生成包及引用
			poFile.append(MessageFormat.format(packAndImp, conf.getPackageName(), impDate, impBlob, impClob)).append("\n");
			// 生成Class头
			poFile.append(MessageFormat.format(classHead, table.getTableName(), table.getPoName()));
			// 生成属性
			poFile.append(attributes.toString()).append("\n");
			// 生成setXXX和getXXX方法
			poFile.append(methods).append("\n");
			// 生成Class尾
			poFile.append(classTail);
			writeFile(table, poFile.toString());
		}
	}

	private void writeFile(Table table, String poFile)
	{
		// 用BufferedWriter包装为了可以缓冲输出，增加性能
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(new BufferedWriter(new FileWriter(conf.getRealPath() + File.separator + table.getPoName() + ".java")));
			writer.println(poFile);
			System.out.println(table.getPoName() + "生成完毕!");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (null != writer)
			{
				writer.close();
			}
		}

	}

	/**
	 * Function    : 
	 * LastUpdate  : 2010-4-20
	 * @param args
	 */
	public static void main(String[] args)
	{
		POGenerator generator = new POGenerator();
		try
		{
			System.out.println("开始生成PO");
			generator.gen();
			System.out.println("生成PO完毕");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
