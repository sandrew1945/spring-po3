package com.sandrew.po3.callback;

import java.sql.ResultSet;

public interface DAOCallback<T>
{
	public T wrapper(ResultSet rs, int index);
}
