package com.sandrew.po3;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;
import com.sandrew.po3.util.Parameters;

public class BatchInsertTest
{

	@Test
	public void test()
	{
		try
		{
			DBUtil dbu = CommonDBUtilImpl.getInstance();
			dbu.initialize("classpath:**/DataAccessContext.xml");
			Session session = SessionFactory.getInstance();
			String sql = "INSERT INTO tt_test (name, age, weight, birthday) VALUES (? ,? ,? ,SYSDATE())";
			List<Parameters> parameters = new ArrayList<Parameters>();
			for (int i = 0 ; i < 40000 ; i++)
			{
				Parameters param = new Parameters();
				param.add("batch insert test" + i);
				param.add(new Integer(i));
				param.add(new Double(100.1d + i));
				parameters.add(param);
			}
			session.insertForBatch(sql, parameters);
			session.commit();
//			DB2Manager.endTxn(true);
//			TransationDBManager manager = new TransationDBManager();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
