package com.sinergia.dcargo.server;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.sinergia.dcargo.client.shared.ServicioUnidad;
import com.sinergia.dcargo.client.shared.dominio.Unidad;
import com.sinergia.dcargo.server.dao.Dao;
import com.sinergia.dcargo.server.util.Utilitario;

/**
 * 
 * @author willy
 */
@Stateless
public class ServicioUnidadImpl extends Dao<Unidad> implements ServicioUnidad {

	public ServicioUnidadImpl() {
		super(Unidad.class);
	}
	
	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;

	@Override
	public List<Unidad> getTodasUnidades() {
		List<Unidad> unidades = findAll(Unidad.class);
		List<Unidad> unidadesDTO = Utilitario.mapper(unidades, Unidad.class); 
		return unidadesDTO;
	}

	@Override
	public void guardar(Unidad unidad) throws Exception {
		
	}

}
