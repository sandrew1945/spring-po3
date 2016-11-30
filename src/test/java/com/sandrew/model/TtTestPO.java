package com.sandrew.model;

import java.util.Date;
import com.sandrew.po3.bean.PO;

public class TtTestPO extends PO
{
	private Integer id;
	private String name;
	private Integer age;
	private Double weight;
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
