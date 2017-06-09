package com.sinergia.dcargo.server;


import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.ServicioCliente;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.server.dao.Dao;
import com.sinergia.dcargo.server.util.Utilitario;

/**
 * 
 * @author willy
 */
@Stateless
public class ServiceClienteImpl extends Dao<Cliente> implements ServicioCliente {

	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	public ServiceClienteImpl() {
		super(new Cliente());
	}
	

	@Override
	public List<Cliente> getTodos() {
		List<Cliente> usuarios = findAll(Cliente.class);
		
		// Serializacion
		List<Cliente> usuariosDTO = Utilitario.mapper(usuarios, Cliente.class);
		
		return usuariosDTO;
	}

	@Override
	public void guardar(Cliente cliente) throws Exception {
		
	}

	@Override
	public Boolean borrar(Long id) {

		return null;
	}


	@Override
	public List<Cliente> buscarClientes(Cliente cliente) {
		
		String nombre = cliente.getNombre();
		nombre = nombre == null ? "": nombre; 
		String direccion = cliente.getDireccion();
		direccion = direccion == null ? "": direccion;
		String nit = cliente.getNit();
		nit = nit == null ? "": nit;
		String telefono = cliente.getTelefono();
		telefono = telefono == null ? "": telefono;
		String ci = cliente.getCi();
		ci = ci == null ? "": ci;
		Integer codigo = cliente.getCodigo();
		codigo = codigo == null ? 0: codigo;
		
		String where = "";
		if(!"".equals(nombre)){
			where = "c.nombre LIKE '%" + nombre + "%' AND";
		} 
		if(!"".equals(direccion)){
			where = where + " c.direccion LIKE '%" + direccion + "%' AND";
		}
		if(!"".equals(nit)){
			where = where + " c.nit LIKE '%" + nit + "%' AND";
		}
		if(!"".equals(telefono)){
			where = where + " c.telefono LIKE '%" + telefono + "%' AND";
		}
		if(!"".equals(ci)){
			where = where + " c.ci LIKE '%" + ci + "%' AND";
		}
		if(0 != codigo){
			where = where + " c.codigo = " + codigo + " AND";
		}
		String query = null;
		String select = "SELECT c FROM Cliente c";
		if("".equals(where)){
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		
		Query q = em.createQuery(query);
		
		@SuppressWarnings("unchecked")
		List<Cliente> clientes = q.getResultList();
		List<Cliente> clientesDTO = Utilitario.mapper(clientes, Cliente.class);
		
		return clientesDTO;
	}
	
	public List<Cliente> buscarClientesPorNombre(String nombre){
		String q = "SELECT c FROM Cliente c WHERE c.nombre =:nombre";
		Query query = em.createQuery(q, Cliente.class);
		query.setParameter("nombre", nombre);
		@SuppressWarnings("unchecked")
		List<Cliente> clientes = query.getResultList();
		return clientes;
	}
	

}
