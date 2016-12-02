package com.sandrew.po3;

import org.junit.Test;

import com.sandrew.model.TtTestPO;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class PODeleteTest
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
			po.setAge(new Integer(123));
			po.setName("ccccccc");
			po.setWeight(new Double(13.54));
			int count = session.delete(po);
			session.commit();
			
			System.out.println("id ------------------------:" + count);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
