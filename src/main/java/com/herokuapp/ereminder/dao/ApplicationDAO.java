package com.herokuapp.ereminder.dao;

import java.util.List;

import javax.persistence.TypedQuery;

public interface ApplicationDAO {
	
	public List findAll(Class clazz);
	
	public <T> TypedQuery<T> createNativeQuery(String query, Class<? extends T> clazz);
	
	public void runQuery(String query);

	public <T> T findById(Class<? extends T> entityClass, Object key);

	public <T> T  add(T entity);
}
