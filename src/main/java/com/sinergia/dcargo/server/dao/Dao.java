package com.sinergia.dcargo.server.dao;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

/**
 * Clase genérica para todos de la cual hereda todos los DAOs del proyecto. Además este 
 * inyecta <code>EntityManager</code> y lo hace disponible a todos los DAOs
 * @author Willy Hurtado
 * @param <E>
 */
@Stateless
public abstract class Dao<E> {
	
	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	private E entity;
	
	public Dao(E e) {
		this.entity = e;
	}
	
	@SuppressWarnings("unchecked")
	public E  buscarPorId(Long id){
		E e = (E) em.find(entity.getClass(), id);
		return e; 
	}
	
	public void persist(E e)  {
		em.persist(e);
	}
	
	public E merge(E e) {
		E merge = em.merge(e);
		return merge; 
	} 
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public List<E> findAll(Class<E> clazz) {
		
	    CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<E> q = cb.createQuery(clazz);
		q.select(q.from(clazz)) ;
		
		return em.createQuery(q).getResultList();
	}
	
	public void delete(Class<E> e, Object id){
		E deleteEntity = em.find(e, id);
		em.remove(deleteEntity);
	}
	
}