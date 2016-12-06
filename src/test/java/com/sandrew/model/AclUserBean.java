/**********************************************************************
* <pre>
* FILE : AclUserBean.java
* CLASS : AclUserBean
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
* 		  |2010-11-11| SuMMeR| Created |
* DESCRIPTION:
* </pre>
***********************************************************************/
/**
* $Id: AclUserBean.java,v 1.1 2013/07/31 08:32:41 xin.jin Exp $
*/

package com.sandrew.model;

import java.util.Locale;

/**
 * Function    : 系统用户
 * @author     : SuMMeR
 * CreateDate  : 2010-11-11
 * @version    :
 */
public class AclUserBean
{
	private Integer userId;

	private String userCode;

	private String userName;

	private Locale locale;

	private Integer roleId;

	private String roleName;

	private String roleCode;

	private Integer companyId;
	
	private Integer sex;
	
	private Integer userType;
	
	private String phone;
	
	private String mobile;
	
	private String email;
	
	private String avatarPath;
	
	private String userStatus;

	public Integer getUserId()
	{
		return userId;
	}

	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}

	public String getUserCode()
	{
		return userCode;
	}

	public void setUserCode(String userCode)
	{
		this.userCode = userCode;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public Integer getRoleId()
	{
		return roleId;
	}

	public void setRoleId(Integer roleId)
	{
		this.roleId = roleId;
	}

	public String getRoleName()
	{
		return roleName;
	}

	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}

	public String getRoleCode()
	{
		return roleCode;
	}

	public void setRoleCode(String roleCode)
	{
		this.roleCode = roleCode;
	}

	public Integer getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(Integer companyId)
	{
		this.companyId = companyId;
	}

	public Integer getSex()
	{
		return sex;
	}

	public void setSex(Integer sex)
	{
		this.sex = sex;
	}

	public Integer getUserType()
	{
		return userType;
	}

	public void setUserType(Integer userType)
	{
		this.userType = userType;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getAvatarPath()
	{
		return avatarPath;
	}

	public void setAvatarPath(String avatarPath)
	{
		this.avatarPath = avatarPath;
	}

	public String getUserStatus()
	{
		return userStatus;
	}

	public void setUserStatus(String userStatus)
	{
		this.userStatus = userStatus;
	}

}
