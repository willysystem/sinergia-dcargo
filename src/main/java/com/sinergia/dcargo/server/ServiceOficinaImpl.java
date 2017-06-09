package com.sinergia.dcargo.server;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.Oficina;
import com.sinergia.dcargo.client.shared.OficinaServicio;
import com.sinergia.dcargo.server.dao.Dao;

@Stateless
public class ServiceOficinaImpl extends Dao<Oficina> implements OficinaServicio {

	public ServiceOficinaImpl() {
		super(new Oficina());
	}

	@Resource
	private SessionContext sctx;
	
	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;

	@SuppressWarnings("unchecked")
	@Override
	public List<Oficina> getAllOffices() {
		Query query = em.createQuery("SELECT e FROM Oficina e", Oficina.class);
		List<Oficina> offices = query.getResultList();
		List<Oficina> officesDTO = new ArrayList<Oficina>();
		for (Oficina office : offices) {
			Oficina officeDTO = new Oficina();
			officeDTO.setId(office.getId());
			officeDTO.setNombre(office.getNombre());
			officesDTO.add(officeDTO);
		}
		return officesDTO;
	}
	
	public List<Oficina> buscarOficinaPorNombre(String nombre){
		String q = "SELECT c FROM Oficina c WHERE c.nombre =:nombre";
		Query query = em.createQuery(q, Oficina.class);
		query.setParameter("nombre", nombre);
		@SuppressWarnings("unchecked")
		List<Oficina> oficinas = query.getResultList();
		return oficinas;
	}
}
