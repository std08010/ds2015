/**
 * 
 */
package com.ds.di.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ds.di.model.BaseEntity;
import com.ds.di.utils.lists.ListUtils;

/**
 * @author Altin Cipi
 *
 */
public abstract class AbstractDAOImpl<T extends BaseEntity, PK extends Serializable> implements AbstractDAO<T, PK>
{
	@PersistenceContext(unitName = "ds")
	@Qualifier(value = "entityManagerFactory")
	private EntityManager	entityManager;

	private Class<T>		entityClass;

	public AbstractDAOImpl(Class<T> entityClass)
	{
		this.entityClass = entityClass;
	}

	public AbstractDAOImpl()
	{
	}

	public EntityManager getEntityManager()
	{
		return this.entityManager;
	}

	public Session getSession()
	{
		if (this.getEntityManager() != null)
		{
			Session s = (Session) (this.getEntityManager().getDelegate());
			return s;
		}
		else
		{
			Logger.getLogger(entityClass).warn("Entity Manager is null");
			return null;
		}
	}

	/**
	 * Creates hiberante criteria
	 * 
	 * @param clazz
	 * @return
	 */
	public Criteria getCriteria()
	{
		return getSession().createCriteria(entityClass);
	}

	@Override
	public void create(T entity)
	{
		entity.setInsertedAt(DateTime.now());
		entity.setUpdatedAt(entity.getInsertedAt());
		this.getEntityManager().persist(entity);
	}

	@Override
	public void delete(PK id)
	{
		if (id == null)
		{
			Logger.getLogger(entityClass).warn("Delete Action. Null PK supplied");
			return;
		}

		T rec = this.find(id);
		if (rec != null)
		{
			Logger.getLogger(entityClass).warn("Delete Action. Record found for PK: " + id);
			this.delete(rec);
		}
	}

	@Override
	public void delete(T entity)
	{
		this.getEntityManager().remove(entity);
		this.getEntityManager().flush();
	}

	@Override
	public T update(T entity)
	{
		entity.setUpdatedAt(DateTime.now());
		return this.getEntityManager().merge(entity);
	}

	@Override
	public T createOrUpdate(T entity)
	{
		if (entity.getID() == null)
		{
			this.create(entity);
		}
		else
		{
			entity = this.update(entity);
		}

		return entity;
	}

	@Override
	public T refresh(T entity)
	{
		this.getEntityManager().refresh(entity);
		return entity;
	}

	@Override
	public T find(PK id)
	{
		return (T) this.getEntityManager().find(entityClass, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll()
	{
		Criteria criteria = this.getCriteria();
		return (List<T>) criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public T findByAttribute(String attrName, Object value)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq(attrName, value));
		List<T> lst = cr.list();

		return ListUtils.getFirstElement(lst);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findListByAttribute(String attrName, Object value)
	{
		Criteria cr = getCriteria();
		cr.add(Restrictions.eq(attrName, value));
		return cr.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAllByOrder(String property, boolean ascending)
	{
		Criteria criteria = getCriteria();
		criteria = this.addOrder(criteria, property, ascending);
		return criteria.list();
	}

	public Criteria addOrder(Criteria criteria, String property, boolean sortAsc)
	{
		if (null != property)
		{
			if (sortAsc)
			{
				criteria.addOrder(Property.forName(property).asc());
			}
			else
			{
				criteria.addOrder(Property.forName(property).desc());
			}
		}

		return criteria;
	}
}
