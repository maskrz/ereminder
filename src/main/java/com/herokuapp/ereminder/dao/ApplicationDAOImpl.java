package com.herokuapp.ereminder.dao;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

@Repository("applicationDAO")
public class ApplicationDAOImpl implements ApplicationDAO {

	/** entity manager */
	@PersistenceContext(name = "fbPU")
	private EntityManager entityManager;

	@SuppressWarnings("unchecked")
	@Override
	public List findAll(Class clazz) {
		StringBuilder query = new StringBuilder();	
		query
		.append("SELECT e.* ")
		.append("FROM ").append(getEntityName(clazz)).append(" e order by ")
		.append(getEntityName(clazz))
		.append("_date");
		return createNativeQuery(query.toString(), clazz).getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> TypedQuery<T> createNativeQuery(String query, Class<? extends T> clazz){
		return (TypedQuery<T>) getEntityManager().createNativeQuery(query, clazz);
	}
	
	private String getEntityName(Class<?> entityClass) throws PersistenceException{
		Annotation entityAnnotation = entityClass.getAnnotation(Entity.class);
		if(entityAnnotation!=null){
			return ((Entity)entityAnnotation).name();
		}
		else{
			throw new PersistenceException("No Entity class: " + entityClass.getName());
		}
	}

	@Override
	public void runQuery(String query) {
		getEntityManager().createNativeQuery(query).executeUpdate();		
	}

	@Override
	public <T> T findById(Class<? extends T> entityClass, Object key) {
		if (key != null) {
			return getEntityManager().find(entityClass, key);
		}
		return null;
	}
	
	@Override
	public <T> T add(T entity) {
		getEntityManager().persist(entity);
		return entity;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
