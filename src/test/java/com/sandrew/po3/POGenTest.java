package com.sandrew.po3;

import java.util.List;

import org.junit.Test;

import com.sandrew.model.TtTestPO;
import com.sandrew.po3.callback.POCallBack;
import com.sandrew.po3.db.CommonDBUtilImpl;
import com.sandrew.po3.db.DBUtil;
import com.sandrew.po3.util.POGenerator;

public class POGenTest
{

	@Test
	public void test()
	{
		try
		{
			POGenerator gen = new POGenerator();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
