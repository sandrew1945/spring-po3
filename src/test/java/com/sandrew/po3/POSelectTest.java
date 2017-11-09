package com.sandrew.po3;

import java.util.List;

import org.junit.Test;

import com.sandrew.model.TtTestPO;
import com.sandrew.po3.callback.POCallBack;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class POSelectTest
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
			List<TtTestPO> list = session.select(po, new POCallBack<TtTestPO>(TtTestPO.class));
			session.commit();
			
			System.out.println("id ------------------------:" + list.size());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
