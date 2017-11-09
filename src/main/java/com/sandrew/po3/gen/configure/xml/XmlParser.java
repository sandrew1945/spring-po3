/***************************************************************************************************
 * <pre>
 * FILE : XmlParser.java
 * CLASS : XmlParser
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
 * $Id: XmlParser.java,v 0.1 2010-4-15 下午02:33:48 SuMMeR Exp $
 */

package com.sandrew.po3.gen.configure.xml;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.sandrew.po3.gen.configure.Configure;
import com.sandrew.po3.gen.configure.ConfigureFileParser;
import com.sandrew.po3.util.Constant;

/**
 * Function :
 * 
 * @author : SuMMeR CreateDate : 2010-4-15
 * @version :
 */
public class XmlParser implements ConfigureFileParser
{

	/*
	 * (non-Javadoc)
	 * @see com.autosys.po3.configure.ConfigureFileParser#parser(java.io.File)
	 */
	public Configure parser()
	{
		Configure conf = Configure.getInstance();
		conf = parserConfig();
		return conf;
	}

	/**
	 * 获取配置文件，调用getConfigureFile(String fileName) Function : LastUpdate :
	 * 2010-4-15
	 * 
	 * @return
	 * @throws DocumentException
	 */
	private Document getConfigureFile() throws DocumentException
	{
		return getConfigureFile("POConf.xml");
	}

	/**
	 * 获取配置文件 Function : LastUpdate : 2010-4-15
	 * 
	 * @param fileName
	 * @return
	 * @throws DocumentException
	 */
	private Document getConfigureFile(String fileName) throws DocumentException
	{
		// 获取配置文件的输入流
		InputStream is = ClassLoader.getSystemResourceAsStream(fileName);
		// File file = new File();
		SAXReader reader = new SAXReader();
		Document doc = reader.read(is);
		return doc;
	}

	/**
	 * Function : 解析XML配置文件 LastUpdate : 2010-4-16
	 * 
	 * @return
	 */
	private Configure parserConfig()
	{
		Configure conf = Configure.getInstance();
		try
		{
			Document doc = getConfigureFile();

			/*
			 * 获取,解析数据库连接信息
			 */

			List<Node> dbConfigList = doc.selectNodes("/PO_CONFIG/DB_CONNECTION");
			// 获取数据库类型、连接信息的结点
			Node dbConfigNode = dbConfigList.get(Constant.NODE_DB_CONFIG);
			// 获取数据库类型
			Element dbConfigElement = asElement(dbConfigNode);
			String dataBaseType = dbConfigElement.attribute("TYPE").getValue();
			conf.setDataBaseType(dataBaseType);
			// 获取数据库连接信息
			Iterator<Element> dbConfigProList = dbConfigElement.elementIterator("PROPERTY");
			while (dbConfigProList.hasNext())
			{
				Element proElement = dbConfigProList.next();
				String proElementName = proElement.attribute("NAME").getValue();
				String proElementValue = proElement.getText().trim();
				if (proElementName.equals(Constant.DB_DRIVER))
				{
					conf.setJdbcDriver(proElementValue);
				}
				else if (proElementName.equals(Constant.DB_URL))
				{
					conf.setJdbcUrl(proElementValue);
				}
				else if (proElementName.equals(Constant.DB_USER))
				{
					conf.setJdbcUser(proElementValue);
				}
				else if (proElementName.equals(Constant.DB_PASSWORD))
				{
					conf.setJdbcPassword(proElementValue);
				}
				else
				{
					throw new RuntimeException("解析文件错误");
				}
			}

			/*
			 * 获取,解析要生成PO的表信息
			 */
			// 解析父类PO的包名(由于PO3框架PO的包名已经确定,因此不再配置文件里配置,也无须解析)
			conf.setFatherPackage("com.autosys.po3.bean");

			// 解析生成PO的绝对路径
			List<Node> genPORealPathList = doc.selectNodes("/PO_CONFIG/GEN_PO_PATH");
			Node genPORealPathNode = genPORealPathList.get(Constant.NODE_REAL_PATH);
			Element genPORealPathElement = asElement(genPORealPathNode);
			String genPORealPath = genPORealPathElement.attribute("NAME").getValue();
			conf.setRealPath(genPORealPath);
			// 解析生成PO的包信息
			List<Node> poPackageList = doc.selectNodes("/PO_CONFIG/TABLES");
			Node poPackageNode = poPackageList.get(Constant.NODE_PACKAGE_NAME);
			Element poPackageElement = asElement(poPackageNode);
			String poPackage = poPackageElement.attribute("PACKAGE").getValue();
			conf.setPackageName(poPackage);

			// 解析要生成PO的表
			List<Node> poList = doc.selectNodes("/PO_CONFIG/TABLES/TABLE");
			for (int i = 0; i < poList.size(); i++)
			{
				Node poNode = poList.get(i);
				Element poElement = asElement(poNode);
				String po = poElement.attribute("NAME").getValue();
				conf.addTable(po);
			}
		}
		catch (DocumentException e)
		{
			e.printStackTrace();
		}
		return conf;
	}

	/**
	 * 
	 * Function    : 将Node转型为Element
	 * LastUpdate  : 2010-4-16
	 * @return
	 */
	private Element asElement(Node node)
	{
		return (Element) node;
	}

	public static void main(String[] args)
	{
		XmlParser parser = new XmlParser();
		try
		{
			Configure conf = parser.parserConfig();
			System.out.println(conf);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
