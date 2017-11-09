package com.sandrew.po3;

import org.junit.Test;

import com.sandrew.model.TtTestPO;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class POUpdateTest
{

	@Test
	public void test()
	{
		try
		{
			DBUtil dbu = CommonDBUtilImpl.getInstance();
			dbu.initialize("classpath:**/DataAccessContext.xml");
			Session session = SessionFactory.getInstance();
			TtTestPO cond = new TtTestPO();
			cond.setId(new Integer(103));
			
			TtTestPO value = new TtTestPO();
			value.setAge(new Integer(101));
			value.setName("eeeeee");
			int count = session.update(cond, value);
			session.commit();
			
			System.out.println("id ------------------------:" + count);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
