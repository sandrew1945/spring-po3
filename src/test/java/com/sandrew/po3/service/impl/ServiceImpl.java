/**********************************************************************
* <pre>
* FILE : LoginService.java
* CLASS : LoginService
*
* AUTHOR : SuMMeR
*
* FUNCTION : TODO
*
*
*======================================================================
* CHANGE HISTORY LOG
*----------------------------------------------------------------------
* MOD. NO.| DATE | NAME | REASON | CHANGE REQ.
*----------------------------------------------------------------------
* 		  |2014年5月3日| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: LoginService.java,v 0.1 2014年5月3日 上午10:58:34 SuMMeR Exp $
*/

package com.sandrew.po3.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.sandrew.po3.Session;
import com.sandrew.po3.SessionFactory;
import com.sandrew.po3.service.IService;

/**
 * Function    : 
 * @author     : SuMMeR
 * CreateDate  : 2014年5月3日
 * @version    :
 */
@Service
public class ServiceImpl implements IService
{

	/**
	 * @liutt
	 * 修改编辑用户信息
	 * @param user
	 * @param avatar
	 * @param aclUser
	 * @return
	 * @throws Exception 
	 * @throws ServiceException
	 */
	public void updateUserInfo() throws Exception
	{
		try
		{
			Session session = SessionFactory.getInstance();
			String sql = "INSERT INTO tt_test (name, age, weight, birthday) VALUES ('test7', 30, 123.12, SYSDATE())";
			session.insert(sql, null);
			
			String sql2 = "INSERT INTO tt_test (name, age, weight, birthday) VALUES ('btest7', 30, 123.12, SYSDATE())";
			session.insert(sql2, null);
		}
		catch (Exception e)
		{
			throw new Exception();
		}

	}

	public void queryInfo() throws Exception
	{
		try
		{
			Session session = SessionFactory.getInstance();
			String sql = "select * from tt_test where id = 1";
			System.out.println("records count :" + session.select(sql, null).size());
		}
		catch (Exception e)
		{
			throw new Exception();
		}
	}

	public void updateUserName(Integer id, String name) throws Exception
	{
		try
		{
			Session session = SessionFactory.getInstance();
			String sql = "update tt_test SET name = ? WHERE id = ?";
			List<Object> params = new ArrayList<Object>();
			params.add(name);
			params.add(id);
			Random r = new Random();
			Thread.sleep(r.nextInt(1000));
			session.update(sql, params);
		}
		catch (Exception e)
		{
			throw new Exception();
		}
	}
	
	

}
