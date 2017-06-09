package com.sinergia.dcargo.server;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.sinergia.dcargo.client.shared.Precio;
import com.sinergia.dcargo.client.shared.ServicioPrecio;
import com.sinergia.dcargo.server.dao.Dao;
import com.sinergia.dcargo.server.util.Utilitario;

/**
 * 
 * @author willy
 */
@Stateless
public class ServicioPrecioImpl extends Dao<Precio> implements ServicioPrecio {

	public ServicioPrecioImpl() {
		super(new Precio());
	}
	
	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;

	@Override
	public List<Precio> getTodosLosPrecios() {
		List<Precio> unidades = findAll(Precio.class);
		List<Precio> unidadesDTO = Utilitario.mapper(unidades, Precio.class); 
		return unidadesDTO;
	}

	@Override
	public void guardar(Precio unidad) throws Exception {
		
	}

}
