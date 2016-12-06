package com.sandrew.po3;

import java.util.Date;

import org.junit.Test;

import com.sandrew.model.TtTestPO;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class POInsertTest
{

	@Test
	public void test()
	{
		try
		{
			DBUtil dbu = CommonDBUtilImpl.getInstance();
			dbu.initialize("classpath:**/DataAccessContext.xml");
			Session session = SessionFactory.getInstance();
			TtTestPO po = new TtTestPO();
			po.setAge(new Integer(10));
			po.setBirthday(new Date());
			po.setName("test2");
			po.setWeight(new Double(200.2));
			session.insert(po);
			session.commit();
			
			System.out.println("id ------------------------:" + po.getId());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
