package com.sandrew.po3.service;

/**
 * Function    : 
 * @author     : zhao.feng
 * CreateDate  : 2010-11-5
 * @version    :
 */
public interface IService
{
	/**
	 * 修改编辑用户信息
	 * @param user
	 * @param avatar
	 * @param aclUser
	 * @return
	 * @throws ServiceException
	 */
	public void updateUserInfo() throws Exception;
	
	public void queryInfo() throws Exception;
	
	public void updateUserName(Integer id, String name) throws Exception;
}
