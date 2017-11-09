package com.sandrew.po3;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class CommonUtilImplTest
{

	@Test
	public void test()
	{
		DBUtil dbu = CommonDBUtilImpl.getInstance();
		dbu.initialize("/DataAccessContext.xml");
		
		Session session = SessionFactory.getInstance();
	}

}
