package com.sandrew.po3;

import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;



public class Test
{

	/**
	 * Function    : 
	 * LastUpdate  : 2010-5-19
	 * @param args
	 */
	public static void main(String[] args)
	{
		DBUtil dbu = CommonDBUtilImpl.getInstance();
		dbu.initialize("/DataAccessContext.xml");

	}
}
