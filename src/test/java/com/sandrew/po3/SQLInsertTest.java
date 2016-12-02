package com.sandrew.po3;

import org.junit.Test;

import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class SQLInsertTest
{

	@Test
	public void test()
	{
		try
		{
			DBUtil dbu = CommonDBUtilImpl.getInstance();
			dbu.initialize("classpath:**/DataAccessContext.xml");
			Session session = SessionFactory.getInstance();
			String sql = "INSERT INTO tt_test (id2,id1,name, age, weight, birthday) VALUES (1,1,'test7', 30, 123.12, SYSDATE())";
			System.out.println("====================begin");
			session.insert(sql, null);
			System.out.println("====================before commit");
			session.commit();
			System.out.println("====================end");
//			DB2Manager.endTxn(true);
//			TransationDBManager manager = new TransationDBManager();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
