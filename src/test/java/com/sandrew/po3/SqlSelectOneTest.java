package com.sandrew.po3;

import java.util.List;

import org.junit.Test;

import com.sandrew.model.TmRolePO;
import com.sandrew.po3.callback.POCallBack;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class SqlSelectOneTest
{

	@Test
	public void test()
	{
		try
		{
			DBUtil dbu = CommonDBUtilImpl.getInstance();
			dbu.initialize("classpath:**/DataAccessContext.xml");
			Session session = SessionFactory.getInstance();
			StringBuilder sql = new StringBuilder();
			sql.append("select\n");
			sql.append("*\n");
			sql.append("from tm_role\n");
			sql.append("where 1 = 1\n");
//			sql.append("and role_id = 5");
			
			TmRolePO role = session.selectOne(sql.toString(), null, new POCallBack<TmRolePO>(TmRolePO.class));
			session.commit();
			
			System.out.println("id ------------------------:" + role.getRoleName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
