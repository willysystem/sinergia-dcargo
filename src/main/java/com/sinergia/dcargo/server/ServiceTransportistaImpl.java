package com.sinergia.dcargo.server;


import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.ServicioTransportista;
import com.sinergia.dcargo.client.shared.Transportista;
import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.server.dao.Dao;
import com.sinergia.dcargo.server.util.Utilitario;

/**
 * 
 * @author willy
 */
@Stateless
public class ServiceTransportistaImpl extends Dao<Transportista> implements ServicioTransportista {

	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	public ServiceTransportistaImpl() {
		super(Transportista.class);
	}

	@Override
	public List<Transportista> getTodos() {
		
		List<Transportista> transportistas = findAll(Transportista.class);
		
		// Serializacion
		List<Transportista> transportistasDTO = Utilitario.mapper(transportistas, Transportista.class);
		
		return transportistasDTO;
	}
	
	@Override
	public List<Transportista> buscarTransportista(Transportista transportista) {
		
		String nombre = transportista.getNombre();
		nombre = nombre == null ? "": nombre;
		
		String ci = transportista.getCi();
		ci = ci == null ? "": ci;
		
		String direccion = transportista.getDireccion();
		direccion = direccion == null ? "": direccion;
		
		String telefono = transportista.getTelefono();
		telefono = telefono == null ? "": telefono;
		
		String placa = transportista.getPlaca();
		placa = placa == null ? "": placa;
		
		String marca = transportista.getMarca();
		marca = marca == null ? "": marca;
		
		String color = transportista.getColor();
		color = color == null ? "": color;
		
		String vecinoDe = transportista.getVecino_de();
		vecinoDe = vecinoDe == null ? "": vecinoDe;
		
		String where = "";
		if(!"".equals(nombre)) 
			where = "c.nombre LIKE '%" + nombre + "%' AND";
		if(!"".equals(ci))
			where = where + " c.ci LIKE '%" + ci + "%' AND";
		if(!"".equals(direccion))
			where = where + " c.direccion LIKE '%" + direccion + "%' AND";
		if(!"".equals(telefono))
			where = where + " c.telefono LIKE '%" + telefono + "%' AND";
		if(!"".equals(placa))
			where = where + " c.placa LIKE '%" + placa + "%' AND";
		if(!"".equals(marca))
			where = where + " c.marca LIKE '%" + marca + "%' AND";
		if(!"".equals(color)) 
			where = where + " c.color LIKE '%" + color + "%' AND";
		if(!"".equals(vecinoDe))
			where = where + " c.vecino_de LIKE '%" + vecinoDe + "%' AND";
		
		String query = null;
		String select = "SELECT c FROM Transportista c";
		if("".equals(where)){
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		
		Query q = em.createQuery(query);
		
		@SuppressWarnings("unchecked")
		List<Transportista> transportistas = q.getResultList();
		List<Transportista> transportistasDTO = Utilitario.mapper(transportistas, Transportista.class);
		
		return transportistasDTO;
	}

	@Override
	public Transportista nuevoTransportista() {
		Transportista transportistaP = new Transportista();
		transportistaP.setEstado('E');
		transportistaP = em.merge(transportistaP);
		
		Transportista transportista = new Transportista();
		transportista.setId(transportistaP.getId());
		transportista.setEstado(transportistaP.getEstado());
		
		return transportista;
	}

	@Override
	public void guardarBrevet(Long idTransportista, String brevet) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setBrevetCi(brevet);
		em.merge(transportistaP);
	}

	@Override
	public void guardarNombre(Long idTransportista, String nombre) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setNombre(nombre);
		em.merge(transportistaP);
	}

	@Override
	public void guardarDireccion(Long idTransportista, String direccion) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setDireccion(direccion);
		em.merge(transportistaP);
	}

	@Override
	public void guardarTelefono(Long idTransportista, String telefono) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setTelefono(telefono);
		em.merge(transportistaP);
	}

	@Override
	public void guardarPlaca(Long idTransportista, String placa) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setPlaca(placa);
		em.merge(transportistaP);
	}

	@Override
	public void guardarMarca(Long idTransportista, String marca) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setMarca(marca);
		em.merge(transportistaP);
	}

	@Override
	public void guardarColor(Long idTransportista, String color) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setColor(color);
		em.merge(transportistaP);
	}

	@Override
	public void guardarCapacidad(Long idTransportista, String capacidad) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setCapacidad(capacidad);
		em.merge(transportistaP);
	}

	@Override
	public void guardarVecinoDe(Long idTransportista, String vecinoDe) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setVecino_de(vecinoDe);
		em.merge(transportistaP);
	}

	@Override
	public void guardarCi(Long idTransportista, String ci) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setCi(ci);
		em.merge(transportistaP);
	}

	@Override
	public void guardarNit(Long idTransportista, String nit) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setNit(nit);
		em.merge(transportistaP);
	}

	@Override
	public void cambiarEstado(Long idTransportista, String estado) throws Exception {
		Transportista transportistaP = buscarPorId(idTransportista);
		transportistaP.setEstado(estado.charAt(0));
		em.merge(transportistaP);
	}

	@Override
	public Resultado esUnicoBrevetCon(String breved) throws Exception {
		List<Transportista> clientes = getTodos();
		for (Transportista cP : clientes) {
			if(cP.getBrevetCi() != null){
				if (cP.getBrevetCi().equals(breved)) {
					Resultado res = new Resultado();
					if(cP.getEstado() == 'P' || cP.getEstado() == 'E'){
						em.remove(buscarPorId(cP.getId()));
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
	public void borrar(Long id) {
		Transportista tP = buscarPorId(id);
		tP.setConocimientosConductor(null);
		tP.setConocimientosPropietario(null);
		em.remove(tP);
	}
	
}
