package com.sandrew.po3;

import java.util.List;

import org.junit.Test;

import com.sandrew.model.TmRolePO;
import com.sandrew.po3.callback.POCallBack;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class SqlSelectTest
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
			sql.append("role_id, role_code, role_name, role_type, role_status, is_delete, create_date\n");
			sql.append("from tm_role\n");
			sql.append("where 1 = 1\n");
			
			List<TmRolePO> list = session.select(sql.toString(), null, new POCallBack<TmRolePO>(TmRolePO.class));
			session.commit();
			
			System.out.println("id ------------------------:" + list.size());
			for (TmRolePO tmRolePO : list)
			{
				System.out.println("role =======" + tmRolePO);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
