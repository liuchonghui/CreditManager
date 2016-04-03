package com.sina.engine.base.db4o;

import java.io.Serializable;

import com.db4o.query.Predicate;

public class Db4oModel<T>  extends Predicate<T> implements Serializable,Db4oInterface<T>{

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	@Override
	public void objectUpdate(T object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean match(T model) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
