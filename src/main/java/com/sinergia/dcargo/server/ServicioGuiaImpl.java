package com.sinergia.dcargo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.slf4j.Logger;

import com.sinergia.dcargo.client.shared.ServicioCliente;
import com.sinergia.dcargo.client.shared.ServicioGuia;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Item;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dominio.Precio;
import com.sinergia.dcargo.client.shared.dominio.Unidad;
import com.sinergia.dcargo.client.shared.OficinaServicio;
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
	
	@Inject 
	private Logger log;
	
	final private Hashtable<Character, String> estados = new Hashtable<>();
	
	public ServicioGuiaImpl() {
		super(Guia.class);
		estados.put('P', "Pendiente");
		estados.put('R', "Remitido");
		estados.put('E', "Entregado");
		estados.put('A', "Anulado");
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
		Character estado = getEstado(guia.getEstadoDescripcion()); 
		
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
		if(guia.getFechaIni() != null){
			where = where + " c.fechaRegistro >= :fechaIni AND";
			parametros.put("fechaIni", guia.getFechaIni());
		}
		if(guia.getFechaFin() != null){
			where = where + " c.fechaRegistro <= :fechaFin AND";
			parametros.put("fechaFin", guia.getFechaFin());
		}
		if(!"".equals(nroFactura)){
			where = where + " c.nroFactura = :nroFactura AND";
			parametros.put("nroFactura", guia.getNroFactura());
		}
		if(estado != null){
			where = where + " c.estado = :estado AND";
			parametros.put("estado", estado);
		}
		
		String query = null;
		String select = "SELECT c FROM Guia c";
		if("".equals(where)){
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		log.info("-> query: " + query);
		
		Query q = em.createQuery(query);
		for (Entry<String, Object> e: parametros.entrySet()) {
			if(e.getValue() instanceof Date){
				q.setParameter(e.getKey(), (Date)e.getValue(), TemporalType.DATE);
			} else {
				q.setParameter(e.getKey(), e.getValue());
			}
			log.info("->" + e.getKey() + ": " + e.getValue());
		}
		
		@SuppressWarnings("unchecked")
		List<Guia> guias = q.getResultList();
		
		List<Guia> guiasDTO = new ArrayList<>(); //Utilitario.mapper(guias, Guia.class);
		for (Guia guiaP : guias) {
			guiasDTO.add(serializarParaBusqueda(guiaP));
		}
		
		return guiasDTO;
	}

	@Override
	public Guia serializarParaBusqueda(Guia guiaP) {
		Guia gDTO = new Guia();
		gDTO.setId(guiaP.getId());
		gDTO.setNroGuia(guiaP.getNroGuia());
		gDTO.setFechaRegistro(guiaP.getFechaRegistro());
		gDTO.setFechaEntrega(guiaP.getFechaEntrega());
		gDTO.setNroFactura(guiaP.getNroFactura());
		gDTO.setEstadoDescripcion(getDescripcion(guiaP.getEstado()));
		gDTO.setPagoOrigen(guiaP.getPagoOrigen());
		gDTO.setSaldoDestino(guiaP.getSaldoDestino());
		gDTO.setTotalPeso(guiaP.getTotalPeso());
		gDTO.setTotalGuia(guiaP.getTotalGuia());
		gDTO.setTotalCantidad(guiaP.getTotalCantidad());
		gDTO.setResumenContenido(guiaP.getResumenContenido());
		
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
		Query query = em.createQuery("SELECT MAX(g.nroGuia) FROM Guia g");
		Object object = query.getSingleResult();
		String numero = "0";
		if(object != null) numero = object.toString();
		Integer nroGuia = Integer.valueOf(numero) + 1;
		
		Guia guia = new Guia();
		guia.setNroGuia(nroGuia);
		guia.setFechaRegistro(new Date());
		guia.setEstado('P');
		
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
		gDTO.setTotalGuia(guiaP.getTotalGuia());
		
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
	public void guardarTotal(Long idGuia, Double total) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setTotalGuia(total);
		em.merge(guia);
	}
	
	@Override
	public List<EstadoGuia> getEstados() throws Exception {
		List<EstadoGuia> estadosDTO = new ArrayList<>();
		for (Map.Entry<Character, String> e: estados.entrySet()) {
			EstadoGuia eg = new EstadoGuia();
			eg.setEstadoDescripcion(e.getValue());
			estadosDTO.add(eg);
		}
		return estadosDTO;
	}
	
	private Character getEstado(String estadoDescripcion) {
		for (Map.Entry<Character, String> e: estados.entrySet()) {
			if(e.getValue().equals(estadoDescripcion)) return e.getKey();
		}
		return null;
	}
	
	private String getDescripcion(Character estado) {
		for (Map.Entry<Character, String> e: estados.entrySet()) {
			if(e.getKey() == estado) return e.getValue();
		}
		return null;
	}

	@Override
	public void cambiarEstado(Long idGuia, String estadoDescripcion) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setEstado(getEstado(estadoDescripcion));
		em.merge(guia);
	}

	@Override
	public void guardarPesoTotal(Long idGuia, Double pesoTotal) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setTotalPeso(pesoTotal);
		em.merge(guia);
	}

	@Override
	public void guardarBultosTotal(Long idGuia, Integer bultosTotal) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setTotalCantidad(bultosTotal);
		em.merge(guia);
	}
	
}
