package com.sandrew.model;

import java.util.Date;
import com.sandrew.po3.bean.PO;

public class TtTestPO extends PO
{
	private Integer id2;
	private Integer id1;
	private Integer id;
	private String name;
	private Integer age;
	private Double weight;
	private Date birthday;

	public Integer getId2()
	{
		return this.id2;
	}
	public void setId2(Integer id2)
	{
		this.id2 = id2;
	}
	public Integer getId1()
	{
		return this.id1;
	}
	public void setId1(Integer id1)
	{
		this.id1 = id1;
	}
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
