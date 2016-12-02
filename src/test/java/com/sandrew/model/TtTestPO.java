package com.sandrew.model;

import java.util.Date;
import com.sandrew.po3.bean.PO;
import com.sandrew.po3.annotations.ColumnName;
import com.sandrew.po3.annotations.TableName;

@TableName("tt_test")
public class TtTestPO extends PO
{
	@ColumnName(value="id", autoIncrement=true)
	private Integer id;
	@ColumnName(value="name", autoIncrement=false)
	private String name;
	@ColumnName(value="age", autoIncrement=false)
	private Integer age;
	@ColumnName(value="weight", autoIncrement=false)
	private Double weight;
	@ColumnName(value="birthday", autoIncrement=false)
	private Date birthday;

	public Integer getId()
	{
		return this.id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Integer getAge()
	{
		return this.age;
	}
	public void setAge(Integer age)
	{
		this.age = age;
	}
	public Double getWeight()
	{
		return this.weight;
	}
	public void setWeight(Double weight)
	{
		this.weight = weight;
	}
	public Date getBirthday()
	{
		return this.birthday;
	}
	public void setBirthday(Date birthday)
	{
		this.birthday = birthday;
	}

}
