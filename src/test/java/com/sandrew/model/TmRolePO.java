package com.sandrew.model;

import java.util.Date;
import com.sandrew.po3.bean.PO;
import com.sandrew.po3.annotations.ColumnName;
import com.sandrew.po3.annotations.TableName;
@TableName("tm_role")
public class TmRolePO extends PO
{
	@ColumnName(value="role_id", autoIncrement=true)
	private Integer roleId;
	@ColumnName(value="role_code", autoIncrement=false)
	private String roleCode;
	@ColumnName(value="role_name", autoIncrement=false)
	private String roleName;
	@ColumnName(value="role_type", autoIncrement=false)
	private Integer roleType;
	@ColumnName(value="role_status", autoIncrement=false)
	private Integer roleStatus;
	@ColumnName(value="is_delete", autoIncrement=false)
	private Integer isDelete;
	@ColumnName(value="create_by", autoIncrement=false)
	private Integer createBy;
	@ColumnName(value="create_date", autoIncrement=false)
	private Date createDate;
	@ColumnName(value="update_by", autoIncrement=false)
	private Integer updateBy;
	@ColumnName(value="update_date", autoIncrement=false)
	private Date updateDate;

	public Integer getRoleId()
	{
		return this.roleId;
	}
	public void setRoleId(Integer roleId)
	{
		this.roleId = roleId;
	}
	public String getRoleCode()
	{
		return this.roleCode;
	}
	public void setRoleCode(String roleCode)
	{
		this.roleCode = roleCode;
	}
	public String getRoleName()
	{
		return this.roleName;
	}
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
	public Integer getRoleType()
	{
		return this.roleType;
	}
	public void setRoleType(Integer roleType)
	{
		this.roleType = roleType;
	}
	public Integer getRoleStatus()
	{
		return this.roleStatus;
	}
	public void setRoleStatus(Integer roleStatus)
	{
		this.roleStatus = roleStatus;
	}
	public Integer getIsDelete()
	{
		return this.isDelete;
	}
	public void setIsDelete(Integer isDelete)
	{
		this.isDelete = isDelete;
	}
	public Integer getCreateBy()
	{
		return this.createBy;
	}
	public void setCreateBy(Integer createBy)
	{
		this.createBy = createBy;
	}
	public Date getCreateDate()
	{
		return this.createDate;
	}
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	public Integer getUpdateBy()
	{
		return this.updateBy;
	}
	public void setUpdateBy(Integer updateBy)
	{
		this.updateBy = updateBy;
	}
	public Date getUpdateDate()
	{
		return this.updateDate;
	}
	public void setUpdateDate(Date updateDate)
	{
		this.updateDate = updateDate;
	}

}
