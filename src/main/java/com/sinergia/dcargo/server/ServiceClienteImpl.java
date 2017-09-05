package com.sinergia.dcargo.server;


import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.ServicioCliente;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.Resultado;
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
		super(Cliente.class);
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
		where = where + " c.estado <> :estado AND";
		
		String query = null;
		String select = "SELECT c FROM Cliente c";
		if("".equals(where)){
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		
		Query q = em.createQuery(query);
		q.setParameter("estado", 'E');
		
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
	
	@Override
	public Cliente nuevoCliente() {
		Cliente clienteP = new Cliente();
		clienteP.setEstado('E');
		clienteP = em.merge(clienteP);
		
		Cliente cliente = new Cliente();
		cliente.setId(clienteP.getId());
		cliente.setEstado(clienteP.getEstado());
		
		return cliente;
	}

	@Override
	public void guardarNombres(Long idCliente, String nombres) throws Exception {
		Cliente clienteP = buscarPorId(idCliente);
    	clienteP.setNombre(nombres);
    	em.merge(clienteP);
	}

	@Override
	public void guardarDireccion(Long idCliente, String direccion) throws Exception {
		Cliente clienteP = buscarPorId(idCliente);
		clienteP.setDireccion(direccion);
		em.merge(clienteP);
	}


	@Override
	public void guardarTelefono(Long idCliente, String telefono) throws Exception {
		Cliente clienteP = buscarPorId(idCliente);
		clienteP.setTelefono(telefono);
		em.merge(clienteP);
	}


	@Override
	public void guardarNit(Long idCliente, String nit) throws Exception {
		Cliente clienteP = buscarPorId(idCliente);
		clienteP.setNit(nit);
		em.merge(clienteP);
	}


	@Override
	public void guardarCi(Long idCliente, String ci) throws Exception {
		Cliente clienteP = buscarPorId(idCliente);
		clienteP.setCi(ci);
		em.merge(clienteP);
	}


	@Override
	public void cambiarEstado(Long idCliente, String estado) throws Exception {
		Cliente clienteP = buscarPorId(idCliente);
		clienteP.setEstado(estado.charAt(0));
		em.merge(clienteP);
	}
	
	@Override
	public Resultado esUnicoNombreCon(String nombre) throws Exception {
		List<Cliente> clientes = getTodos();
		for (Cliente cP : clientes) {
			if(cP.getNombre() != null){
				if (cP.getNombre().equals(nombre)) {
					Resultado res = new Resultado();
					if(cP.getEstado() == 'P' || cP.getEstado() == 'E'){
						em.remove(buscarPorId(cP.getId()));
						//em.flush();
						res.setVariableBoolean(true);
					} else res.setVariableBoolean(false); 
					return res;
				}
			}
		}
		Resultado res = new Resultado();
		res.setVariableBoolean(true);
		return res;
	}

	@Override
	public Resultado esUnicoNitCon(String nit) throws Exception {
		List<Cliente> clientes = getTodos();
		for (Cliente cP : clientes) {
			if(cP.getNit() != null){
				if (cP.getNit().equals(nit)){
					Resultado res = new Resultado();
					if(cP.getEstado() == 'P' || cP.getEstado() == 'E'){
						em.remove(buscarPorId(cP.getId()));
						//em.flush();
						res.setVariableBoolean(true);
					} else res.setVariableBoolean(false); 
					return res;
				}
			}
		}
		Resultado res = new Resultado();
		res.setVariableBoolean(true);
		return res;
	}
	
}
