package com.sandrew.po3;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import com.sandrew.model.TtTestPO;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DB2Manager;
import com.sandrew.po3.db.DBUtil;

public class POInsertTest
{

	@Test
	public void test()
	{
		try
		{
			DBUtil dbu = CommonDBUtilImpl.getInstance();
			dbu.initialize("/DataAccessContext.xml");
			Session session = SessionFactory.getInstance();
			TtTestPO po = new TtTestPO();
			po.setAge(new Integer(10));
			po.setBirthday(new Date());
			po.setName("test");
			po.setWeight(new Double(100.2));
			session.insert(po);
			DB2Manager.endTxn(true);
		}
		catch (Exception e)
		{
			DB2Manager.endTxn(false);
			e.printStackTrace();
		}
		finally
		{
			DB2Manager.cleanTxn();
		}
		
	}

}
