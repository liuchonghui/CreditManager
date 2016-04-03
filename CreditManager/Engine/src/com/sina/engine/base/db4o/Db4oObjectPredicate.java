package com.sina.engine.base.db4o;

import com.db4o.query.Predicate;

public class Db4oObjectPredicate<T> extends Predicate<T>{

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	private T myModel = null;
	
	public Db4oObjectPredicate (T myModel){
		this.myModel = myModel;
	}
	
	public T getModel(){
		return myModel;
	}

	@Override
	public boolean match(T model) {
		// TODO Auto-generated method stub
		return false;
	}

}
