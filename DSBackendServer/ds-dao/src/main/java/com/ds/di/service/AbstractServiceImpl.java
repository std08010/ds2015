/**
 * 
 */
package com.ds.di.service;

import java.io.Serializable;
import java.util.List;

import com.ds.di.dao.AbstractDAOImpl;
import com.ds.di.model.BaseEntity;

/**
 * @author Altin Cipi
 *
 */
public abstract class AbstractServiceImpl<T extends BaseEntity, PK extends Serializable> implements AbstractService<T, PK>
{
	public abstract AbstractDAOImpl<T, PK> getDAO();

	public AbstractServiceImpl()
	{
	}

	@Override
	public void create(T t)
	{
		this.getDAO().create(t);
	}

	@Override
	public void delete(PK id)
	{
		this.getDAO().delete(id);
	}

	@Override
	public T find(PK id)
	{
		return this.getDAO().find(id);
	}

	@Override
	public List<T> findAll()
	{
		return this.getDAO().findAll();
	}

	@Override
	public T update(T t)
	{
		return this.getDAO().update(t);
	}

	@Override
	public void delete(T t)
	{
		this.getDAO().delete(t);
	}

	@Override
	public T createOrUpdate(T t)
	{
		return this.getDAO().createOrUpdate(t);
	}

	@Override
	public T refresh(T t)
	{
		return this.getDAO().refresh(t);
	}

	@Override
	public T findByAttribute(String attrName, Object value)
	{
		return this.getDAO().findByAttribute(attrName, value);
	}

	@Override
	public List<T> findListByAttribute(String attrName, Object value)
	{
		return this.getDAO().findListByAttribute(attrName, value);
	}

	@Override
	public List<T> findAllByOrder(String property, boolean ascending)
	{
		return this.getDAO().findAllByOrder(property, ascending);
	}
}
