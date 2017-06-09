package com.sinergia.dcargo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.sinergia.dcargo.client.shared.ServicioCliente;
import com.sinergia.dcargo.client.shared.ServicioGuia;
import com.sinergia.dcargo.client.shared.Unidad;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Item;
import com.sinergia.dcargo.client.shared.Oficina;
import com.sinergia.dcargo.client.shared.OficinaServicio;
import com.sinergia.dcargo.client.shared.Precio;
import com.sinergia.dcargo.server.dao.Dao;

/**
 * @author willy
 */
@Stateless
public class ServicioGuiaImpl extends Dao<Guia> implements ServicioGuia {

	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	@EJB
	private ServicioCliente serviceCliente;
	
	@EJB
	private OficinaServicio oficinaServicio;
	
	public ServicioGuiaImpl() {
		super(new Guia());
	}
	
	@Override
	public void guardar(Guia guia) throws Exception {
		merge(guia);
	}

	@Override
	public Boolean borrar(Long id) {

		return null;
	}


	@Override
	public List<Guia> buscarGuias(Guia guia) {
		
		Integer nroGuia = guia.getNroGuia();
		nroGuia = nroGuia == null ? 0: nroGuia;
		String remite = guia.getRemitente() == null ? "": guia.getRemitente().getNombre();
		String consignatario = guia.getConsignatario() == null ? "" : guia.getConsignatario().getNombre();
		String origen = guia.getOficinaOrigen() == null ? "": guia.getOficinaOrigen().getNombre();
		String destino = guia.getOficinaDestino() == null ? "": guia.getOficinaDestino().getNombre();
		String nroFactura = guia.getNroFactura() == null ? "": guia.getNroFactura();
		
		HashMap<String, Object> parametros = new HashMap<>(); 
		String where = "";
		if(0 != nroGuia){
			where = "c.nroGuia = :nroGuia AND";
			parametros.put("nroGuia", nroGuia);
		} 
		if(!"".equals(remite)){
			List<Cliente> clientes = serviceCliente.buscarClientesPorNombre(remite);
			if(!clientes.isEmpty()){
				where = where + " c.remitente.id = :remitenteId AND";
				parametros.put("remitenteId", clientes.get(0).getId());
			} 
		}
		if(!"".equals(consignatario)){
			List<Cliente> clientes = serviceCliente.buscarClientesPorNombre(consignatario);
			if(!clientes.isEmpty()) {
				where = where + " c.consignatario.id = :consignatarioId AND";
				parametros.put("consignatarioId", clientes.get(0).getId());
			}
		}
		if(!"".equals(origen)){
			List<Oficina> ofis = oficinaServicio.buscarOficinaPorNombre(origen);
			if(!ofis.isEmpty()){
				where = where + " c.oficinaOrigen.id = :oficinaOrigenId AND";
				parametros.put("oficinaOrigenId", ofis.get(0).getId());
			}
		}
		if(!"".equals(destino)){
			List<Oficina> ofis = oficinaServicio.buscarOficinaPorNombre(destino);
			if(!ofis.isEmpty()){
				where = where + " c.oficinaDestino.id = :oficinaDestinoId AND";
				parametros.put("oficinaDestinoId", ofis.get(0).getId());
			}
		}
		if(guia.getFechaRegistro() != null){
			where = where + " c.fechaRegistro = :fechaRegistro AND";
			parametros.put("fechaRegistro", guia.getFechaRegistro());
		}
		if(guia.getFechaEntrega() != null){
			where = where + " c.fechaEntrega = :fechaEntrega AND";
			parametros.put("fechaEntrega", guia.getFechaEntrega());
		}
		if(!"".equals(nroFactura)){
			where = where + " c.nroFactura = :nroFactura AND";
			parametros.put("nroFactura", guia.getNroFactura());
		}
		if(guia.getActivo() != null){
			where = where + " c.activo = :activo AND";
			parametros.put("activo", guia.getActivo());
		}
		
		
		String query = null;
		String select = "SELECT c FROM Guia c";
		if("".equals(where)){
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		
		Query q = em.createQuery(query);
		for (Entry<String, Object> e: parametros.entrySet()) {
			if(e.getValue() instanceof Date){
				q.setParameter(e.getKey(), (Date)e.getValue(), TemporalType.DATE);
			} else {
				q.setParameter(e.getKey(), e.getValue());
			}
		}
		System.out.println("-> query: " + query);
		@SuppressWarnings("unchecked")
		List<Guia> guias = q.getResultList();
		
		List<Guia> guiasDTO = new ArrayList<>(); //Utilitario.mapper(guias, Guia.class);
		for (Guia guiaP : guias) {
			guiasDTO.add(serializarParaBusqueda(guiaP));
		}
		
		return guiasDTO;
	}

	private Guia serializarParaBusqueda(Guia guiaP) {
		Guia gDTO = new Guia();
		gDTO.setId(guiaP.getId());
		gDTO.setNroGuia(guiaP.getNroGuia());
		gDTO.setFechaRegistro(guiaP.getFechaRegistro());
		gDTO.setFechaEntrega(guiaP.getFechaEntrega());
		gDTO.setNroFactura(guiaP.getNroFactura());
		gDTO.setActivo(guiaP.getActivo());
		
		Cliente remite1 = null;
		if(guiaP.getRemitente() != null) {
			remite1 = new Cliente();
			remite1.setId(guiaP.getRemitente().getId());
			remite1.setNombre(guiaP.getRemitente().getNombre());
			gDTO.setRemitente(remite1);
		}
		
		Cliente consignatario1 = null; 
		if(guiaP.getConsignatario() != null){
			consignatario1 = new Cliente();
			consignatario1.setId(guiaP.getConsignatario().getId());
			consignatario1.setNombre(guiaP.getConsignatario().getNombre());
			gDTO.setConsignatario(consignatario1);
		}
		
		Oficina origen1 = null;
		if(guiaP.getOficinaOrigen() != null) {
			origen1 = new Oficina();
			origen1.setId(guiaP.getOficinaOrigen().getId());
			origen1.setNombre(guiaP.getOficinaOrigen().getNombre());
			gDTO.setOficinaOrigen(origen1);
		}
		
		Oficina destino1 = null;
		if(guiaP.getOficinaDestino() != null) {
			destino1 = new Oficina();
			destino1.setId(guiaP.getOficinaDestino().getId());
			destino1.setNombre(guiaP.getOficinaDestino().getNombre());
			gDTO.setOficinaDestino(destino1);
		}
		
		return gDTO;
	}
	
	
	@Override
	public Guia nuevaGuia() throws Exception {
		Guia guia = new Guia();
		Query query = em.createQuery("SELECT MAX(g.nroGuia) FROM Guia g");
		Integer nroGuia = Integer.valueOf(query.getSingleResult().toString()) + 1;
		guia.setNroGuia(nroGuia);
		guia.setActivo(false);
		Guia guiaP = merge(guia);
		guia.setId(guiaP.getId());
		return guia;
	}


	@Override
	public void guardarNroFactura(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		guiaP.setNroFactura(guia.getNroFactura());
		merge(guiaP);
	}


	@Override
	public void guardarRemitente(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		Cliente clienteOrigen = new Cliente();
		clienteOrigen.setId(guia.getRemitente().getId());
		guiaP.setRemitente(clienteOrigen);
		merge(guiaP);
	}


	@Override
	public void guardarConsignatario(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		Cliente clienteOrigen = new Cliente();
		clienteOrigen.setId(guia.getConsignatario().getId());
		guiaP.setConsignatario(clienteOrigen);
		merge(guiaP);
	}


	@Override
	public void guardarOrigen(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		Oficina oficina = new Oficina();
		oficina.setId(guia.getOficinaOrigen().getId());
		guiaP.setOficinaOrigen(oficina);
		merge(guiaP);
	}


	@Override
	public void guardarDestino(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		Oficina oficina = new Oficina();
		oficina.setId(guia.getOficinaDestino().getId());
		guiaP.setOficinaDestino(oficina);
		merge(guiaP);
	}


	@Override
	public void guardarAdjunto(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		guiaP.setAdjunto(guia.getAdjunto());
		merge(guiaP);
	}

	@Override
	public void guardarResumen(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		guiaP.setResumenContenido(guia.getResumenContenido());
		merge(guiaP);
	}

	@Override
	public void guardarNroEntrega(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		guiaP.setNotaEntrega(guia.getNotaEntrega());
		merge(guiaP);
	}

	@Override
	public void guardarPagoOrigen(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		guiaP.setPagoOrigen(guia.getPagoOrigen());
		merge(guiaP);		
	}
	
	@Override
	public void guardarPagoDestino(Guia guia) throws Exception {
		Guia guiaP = buscarPorId(guia.getId());
		guiaP.setSaldoDestino(guia.getSaldoDestino());
		merge(guiaP);		
	}


	@Override
	public Guia consultarGuia(Long idGuia) {
		Guia guiaP = buscarPorId(idGuia);
		Guia gDTO = serializarParaBusqueda(guiaP);
		gDTO.setAdjunto(guiaP.getAdjunto());
		gDTO.setResumenContenido(guiaP.getResumenContenido());
		gDTO.setNotaEntrega(guiaP.getNotaEntrega());
		gDTO.setPagoOrigen(guiaP.getPagoOrigen());
		gDTO.setSaldoDestino(guiaP.getSaldoDestino());
		
		List<Item> items = new ArrayList<>();
		for (Item itemP: guiaP.getItems()) {
			Item iDTO = new Item();
			iDTO.setId(itemP.getId());
			iDTO.setCantidad(itemP.getCantidad());
			iDTO.setContenido(itemP.getContenido());
			iDTO.setPeso(itemP.getPeso());
			
			if(itemP.getUnidad() != null){
				Unidad uDTO = new Unidad();
				uDTO.setId(itemP.getUnidad().getId());
				uDTO.setAbreviatura(itemP.getUnidad().getAbreviatura());
				iDTO.setUnidadTitulo(itemP.getUnidad().getAbreviatura());
				iDTO.setUnidad(uDTO);
			} else iDTO.setUnidadTitulo("");
			
			if(itemP.getPrecio() != null){
				Precio pDTO = new Precio();
				pDTO.setId(itemP.getPrecio().getId());
				pDTO.setPrecio(itemP.getPrecio().getPrecio());
				pDTO.setDescripcion(itemP.getPrecio().getDescripcion());
				iDTO.setPrecioMonto(itemP.getPrecio().getDescripcion());
				iDTO.setPrecio(pDTO);
			} else iDTO.setPrecioMonto("");
			
			iDTO.setTotal(itemP.getTotal());
			
			items.add(iDTO);
		}
		gDTO.setItems(new HashSet<>(items));
		
		return gDTO;
	}

	@Override
	public void cambiarEstado(Long idGuia, Boolean estado) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setActivo(estado);
		em.merge(guia);
	}

}
