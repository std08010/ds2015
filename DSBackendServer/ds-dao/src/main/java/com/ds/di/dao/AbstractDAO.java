package com.ds.di.dao;

import java.io.Serializable;
import java.util.List;

import com.ds.di.model.BaseEntity;

/**
 * @author Altin Cipi
 *
 */
public interface AbstractDAO<T extends BaseEntity, PK extends Serializable>
{
	public void create(T t);

	public void delete(PK id);
	
	public void delete(T t);
	
	public T update(T t);
	
	public T createOrUpdate(T t);
	
	public T refresh(T t);

	public T find(PK id);
	
	public List<T> findAll();
	
	public T findByAttribute(String attrName, Object value);
	
	public List<T> findListByAttribute(String attrName, Object value);
	
	public List<T> findAllByOrder(String property, boolean ascending);
}
