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
	
	private Class<E> clazz;
	
	public Dao(Class<E> clazz) {
		this.clazz = clazz;
	}
	
	public E  buscarPorId(Long id){
		return em.find(clazz, id);
		 
	}
	
	public void persist(E e)  {
		em.persist(e);
	}
	
	public E merge(E e) {
		return em.merge(e);
	} 
	
	@TransactionAttribute(TransactionAttributeType.NEVER)
	public <T> List<T>  findAll(Class<T> clazz) {
		
	    CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(clazz);
		q.select(q.from(clazz)) ;
		
		return em.createQuery(q).getResultList();
	}
	
	public void delete(Class<E> e, Object id){
		E deleteEntity = em.find(e, id);
		em.remove(deleteEntity);
	}
	
}