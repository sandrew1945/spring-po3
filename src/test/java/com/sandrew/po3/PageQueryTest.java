package com.sandrew.po3;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.sandrew.model.AclUserBean;
import com.sandrew.po3.bean.PageResult;
import com.sandrew.po3.callback.DAOCallback;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;

public class PageQueryTest
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
			sql.append("SELECT\n");
			sql.append("		tu.user_id,\n");
			sql.append("		tu.user_code,\n");
			sql.append("		tu.user_name,\n");
			sql.append("		tu.sex,\n");
			sql.append("		tu.user_status,\n");
			sql.append("		tu.phone,\n");
			sql.append("		tu.mobile,\n");
			sql.append("		tu.email,\n");
			sql.append("		tr.role_code,\n");
			sql.append("		group_concat(tr.role_name) AS role_name\n");
			sql.append("	FROM\n");
			sql.append("		tm_user AS tu\n");
			sql.append("	LEFT JOIN tr_user_role AS tur ON tu.user_id = tur.user_id\n");
			sql.append("	LEFT JOIN (\n");
			sql.append("			SELECT\n");
			sql.append("				tm_role.role_id,\n");
			sql.append("				tm_role.role_code,\n");
			sql.append("				tm_role.role_name\n");
			sql.append("			FROM\n");
			sql.append("				tm_role\n");
			sql.append("			WHERE tm_role.is_delete = 10031002\n");
			sql.append("			AND   tm_role.role_status = 10031002\n");
			sql.append("		) AS tr ON tr.role_id = tur.role_id\n");
			sql.append("	WHERE\n");
			sql.append("		1 = 1\n");
			sql.append("AND tu.is_delete = 10031002\n");
			sql.append("AND tu.user_type = 20111001\n");
			sql.append("GROUP BY user_code,is_delete");
			
			PageResult<AclUserBean> result = session.pageQuery(sql.toString(), null, getAclUserCallback(), 10, 1);
			System.out.println("================== " + result.getTotalRecords());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	private static DAOCallback<AclUserBean> getAclUserCallback()
	{
		return new DAOCallback<AclUserBean>()
		{
			public AclUserBean wrapper(ResultSet rs, int idx)
			{
				AclUserBean bean = new AclUserBean();
				try
				{
					bean.setUserId(rs.getInt("user_id"));
					bean.setUserCode(rs.getString("user_code"));
					bean.setUserName(rs.getString("user_name"));
					bean.setSex(rs.getInt("sex"));
					bean.setUserStatus(rs.getString("user_status"));
					bean.setPhone(rs.getString("phone"));
					bean.setMobile(rs.getString("mobile"));
					bean.setEmail(rs.getString("email"));
					bean.setRoleCode(rs.getString("role_code"));
					bean.setRoleName(rs.getString("role_name"));
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
				return bean;
			}
		};
	}

}
