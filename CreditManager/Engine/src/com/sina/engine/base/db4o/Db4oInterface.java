package com.sina.engine.base.db4o;

public interface Db4oInterface<T> {
	/**
	 * 更新对象
	 * @param object
	 */
    public void objectUpdate(T object);
    
}
