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
import com.sinergia.dcargo.client.shared.ServicioCuenta;
import com.sinergia.dcargo.client.shared.ServicioGuia;
import com.sinergia.dcargo.client.shared.ServicioMovimiento;
import com.sinergia.dcargo.client.shared.ServicioUsuario;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.Cuenta;
import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Item;
import com.sinergia.dcargo.client.shared.dominio.MovimientoIngreso;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dominio.Unidad;
import com.sinergia.dcargo.client.shared.dominio.Usuario;
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
	
	@EJB private ServicioCliente    serviceCliente;
	@EJB private OficinaServicio    oficinaServicio;
	@EJB private ServicioMovimiento servicioMovimiento;
	@EJB private ServicioCuenta     servicioCuenta;
	@EJB private ServicioUsuario    servicioUsuario;
	
	@Inject 
	private Logger log;
	
	final private Hashtable<Character, String> estados = new Hashtable<>();
	
	final private Hashtable<Character, String> estadosPago = new Hashtable<>();
	
	public ServicioGuiaImpl() {
		super(Guia.class);
		//estados.put('P', "Pendiente");
		estados.put('R', "Remitido");
		estados.put('E', "Entregado");
		estados.put('A', "Anulado");
		
		//estadosPago.put('C', "Cancelado");
		estadosPago.put('Z', "Origen-Destino");
		estadosPago.put('O', "Origen");
		estadosPago.put('D', "Destino");
		
	}
	
	@Override
	public void guardar(Guia guia) throws Exception {
		merge(guia);
	}

	@Override
	public Boolean borrar(Long id) {

		return null;
	}


	@SuppressWarnings("deprecation")
	@Override
	public List<Guia> buscarGuias(Guia guia) {
		
		Integer nroGuia = guia.getNroGuia();
		nroGuia = nroGuia == null ? 0: nroGuia;
		
		Integer nroConocimiento = guia.getNroConocimiento();
		nroConocimiento = nroConocimiento == null ? 0: nroConocimiento;
		
		String remite = guia.getRemitente() == null ? "": guia.getRemitente().getNombre();
		String consignatario = guia.getConsignatario() == null ? "" : guia.getConsignatario().getNombre();
		String origen = guia.getOficinaOrigen() == null ? "": guia.getOficinaOrigen().getNombre();
		String destino = guia.getOficinaDestino() == null ? "": guia.getOficinaDestino().getNombre();
		String nroFactura = guia.getNroFactura() == null ? "": guia.getNroFactura();
		Character estado = getEstado(guia.getEstadoDescripcion()); 
		
		HashMap<String, Object> parametros = new HashMap<>(); 
		String where = " c.estado <> :estadoPendiente AND";
		if(0 != nroGuia){
			where = where + " c.nroGuia = :nroGuia AND";
			parametros.put("nroGuia", nroGuia);
		}
		if(0 != nroConocimiento){
			where = where + " c.conocimiento.nroConocimiento = :nroConocimiento AND";
			parametros.put("nroConocimiento", nroConocimiento);
		}
		
		
		if(!"".equals(remite)){
			List<Cliente> clientes = serviceCliente.buscarClientesPorNombre(remite);
			if(!clientes.isEmpty()) {
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
		if(guia.getFechaIni() != null && guia.getFechaFin() != null) {
			guia.getFechaIni().setHours(0);
			guia.getFechaIni().setMinutes(0);
			guia.getFechaIni().setSeconds(0);
			guia.getFechaFin().setHours(23);
			guia.getFechaFin().setMinutes(59);
			guia.getFechaFin().setSeconds(59);
			where = where + " c.fechaRegistro BETWEEN :fechaIni AND :fechaFin AND";
			parametros.put("fechaIni", guia.getFechaIni());
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
		if(guia.getExcluirGuiasExistentesEnConocimiento()) {
			where = where + " c.conocimiento = null AND";
		}
		
		String query = null;
		String select = "SELECT c FROM Guia c";
		if("".equals(where)) {
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		log.info("-> query: " + query);
		
		Query q = em.createQuery(query);
		for (Entry<String, Object> e: parametros.entrySet()) {
			if(e.getValue() instanceof Date){
				q.setParameter(e.getKey(), (Date)e.getValue(), TemporalType.TIMESTAMP);
			} else {
				q.setParameter(e.getKey(), e.getValue());
			}
			log.info("->" + e.getKey() + ": " + e.getValue());
		}
		q.setParameter("estadoPendiente", 'P');
		
		
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
		gDTO.setEstadoDescripcion(getEstadoDescripcion(guiaP.getEstado()));
		gDTO.setEstadoPagoDescripcion(getEstadoPagoDescripcion(guiaP.getEstadoPago()));
		gDTO.setPagoOrigen(guiaP.getPagoOrigen());
		gDTO.setSaldoDestino(guiaP.getSaldoDestino());
		gDTO.setTotalPeso(guiaP.getTotalPeso());
		gDTO.setTotalGuia(guiaP.getTotalGuia());
		gDTO.setTotalCantidad(guiaP.getTotalCantidad());
		gDTO.setResumenContenido(guiaP.getResumenContenido());
		gDTO.setFechaEntrega(guiaP.getFechaEntrega());
		gDTO.setNroFacturaEntrega(guiaP.getNroFacturaEntrega());
		gDTO.setObservaciones(guiaP.getObservaciones());
		
		Cliente remite1 = null;
		if(guiaP.getRemitente() != null) {
			remite1 = new Cliente();
			remite1.setId(guiaP.getRemitente().getId());
			remite1.setNombre(guiaP.getRemitente().getNombre());
			remite1.setTelefono(guiaP.getRemitente().getTelefono());
			gDTO.setRemitente(remite1);
		}
		
		Cliente consignatario1 = null; 
		if(guiaP.getConsignatario() != null){
			consignatario1 = new Cliente();
			consignatario1.setId(guiaP.getConsignatario().getId());
			consignatario1.setNombre(guiaP.getConsignatario().getNombre());
			consignatario1.setTelefono(guiaP.getConsignatario().getTelefono());
			consignatario1.setCi(guiaP.getConsignatario().getCi());
			consignatario1.setDireccion(guiaP.getConsignatario().getDireccion());
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
		
		Conocimiento conocimiento = null;
		if(guiaP.getConocimiento() != null) {
			conocimiento = new Conocimiento();
			conocimiento.setNroConocimiento(guiaP.getConocimiento().getNroConocimiento());
			gDTO.setConocimiento(conocimiento);
		}
		
		return gDTO;
	}
	
	
	@Override
	public Guia nuevaGuia() throws Exception {
		
		Guia guia = new Guia();
		//guia.setNroGuia(generarNroGuia());
		//guia.setNroGuia();
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
		guiaP.setNroNotaEntrega(guia.getNroNotaEntrega());
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
		gDTO.setNroNotaEntrega(guiaP.getNroNotaEntrega());
		gDTO.setPagoOrigen(guiaP.getPagoOrigen());
		gDTO.setSaldoDestino(guiaP.getSaldoDestino());
		gDTO.setTotalGuia(guiaP.getTotalGuia());
		gDTO.setPagadoOrigen(guiaP.getPagadoOrigen());
		gDTO.setPagadoDestino(guiaP.getPagadoDestino());
		gDTO.setEntregaConsignatario(guiaP.getEntregaConsignatario());
		
		if(guiaP.getEntregaConsignatario() != null) {
			if(guiaP.getEntregaConsignatario()) {
				gDTO.setNombreClienteEntrega(guiaP.getConsignatario().getNombre());
				gDTO.setCiEntrega(guiaP.getConsignatario().getNit());
			} else {
				gDTO.setNombreClienteEntrega(guiaP.getNombreClienteEntrega());
				gDTO.setCiEntrega(guiaP.getCiEntrega());
			}
		}
		
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
			
//			if(itemP.getPrecio() != null){
//				Precio pDTO = new Precio();
//				pDTO.setId(itemP.getPrecio().getId());
//				pDTO.setPrecio(itemP.getPrecio().getPrecio());
//				pDTO.setDescripcion(itemP.getPrecio().getDescripcion());
//				iDTO.setPrecioMonto(itemP.getPrecio().getDescripcion());
//				iDTO.setPrecio(pDTO);
//			} else iDTO.setPrecioMonto("");
			
			iDTO.setPrecio(itemP.getPrecio());
			
			iDTO.setTotal(itemP.getTotal());
			
			items.add(iDTO);
		}
		gDTO.setItems(new HashSet<>(items));
		
		return gDTO;
	}

	@Override
	public void guardartotal(Long idGuia, Double total) throws Exception {
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
	
//	private Character getEstadoPago(String estadoDescripcion) {
//		for (Map.Entry<Character, String> e: estadosPago.entrySet()) {
//			if(e.getValue().equals(estadoDescripcion)) return e.getKey();
//		}
//		return null;
//	}
	
	private String getEstadoDescripcion(Character estado) {
	   for (Map.Entry<Character, String> e: estados.entrySet()) {
			if(e.getKey() == estado) return e.getValue();
	   }
	   return null;
	}
	
	private String getEstadoPagoDescripcion(Character estado) {
	   for (Map.Entry<Character, String> e: estadosPago.entrySet()) {
		   if(e.getKey() == estado) return e.getValue();
	   }
	   return null;
	}

	@Override
	public void cambiarEstado(Long idGuia, String estadoDescripcion) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setEstado(getEstado(estadoDescripcion));

		if(estadoDescripcion.charAt(0) == 'R') {
			// Origen
			if(guia.getPagoOrigen() > 0D && guia.getSaldoDestino() == 0D) {
				guia.setEstadoPago('O');
			}
			
			// Destino
			if(guia.getPagoOrigen() == 0D && guia.getSaldoDestino() > 0D) {
				guia.setEstadoPago('D');
			}
			
			// Origen-Destino
			if(guia.getPagoOrigen() > 0D && guia.getSaldoDestino() > 0D) {
				guia.setEstadoPago('Z');
			}
			
		}
		
		if(estadoDescripcion.charAt(0) == 'E') {
			guia.setFechaEntrega(new Date());
		}
		
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

	@Override
	public void guardarNombreClienteEntrega(Long idGuia, String nombreClienteEntrega) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setNombreClienteEntrega(nombreClienteEntrega);
		em.merge(guia);
	}

	@Override
	public void guardarCiEntrega(Long idGuia, String ciEntrega) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setCiEntrega(ciEntrega);
		em.merge(guia);
	}

	@Override
	public void guardarNroFacturaEntrega(Long idGuia, String nroFacturaEntrega) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setNroFacturaEntrega(nroFacturaEntrega);
		em.merge(guia);
	}

	@Override
	public void guardarNotaEntrega(Long idGuia, String notaEntrega) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setNotaEntrega(notaEntrega);
		em.merge(guia);
	}
	
	@Override
	public void guardarNroNotaEntrega(Long idGuia, String nroNotaEntrega) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setNroNotaEntrega(nroNotaEntrega);
		em.merge(guia);
	}

//	@Override
//	public void guardarFechaEntrega(Long idGuia, DateParam fechaEntrega) throws Exception {
//		Guia guia = buscarPorId(idGuia);
//		guia.setFechaEntrega(fechaEntrega.getDate());
//		em.merge(guia);
//	}

	@Override
	public void guardarEntregaConsignatario(Long idGuia, Boolean entregaConsignatario) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setEntregaConsignatario(entregaConsignatario);
		em.merge(guia);
	}

	@Override
	public Integer pagarOrigen(Long idGuia, Double monto, String glosa) throws Exception {
		
		String userName = sctx.getCallerPrincipal().getName();
		Usuario user    = servicioUsuario.buscarPorUsuario(userName); 
		Oficina oficina = user.getOffice();
		Cuenta cuenta = oficina.getCuentaIngresoOrigen(); 
		
		//Cuenta cuenta = servicioCuenta.getCuentaIngresoPorNroCuenta(1000);
		Guia guia = buscarPorId(idGuia);
		guia.setPagadoOrigen(true);
		guia = merge(guia);
		MovimientoIngreso miP = servicioMovimiento.nuevoMovimientoIngreso();
		miP = em.find(MovimientoIngreso.class, miP.getId());
		miP.setFechaRegistro(new Date());
		miP.setMonto(monto);
		miP.setGuiaPagoOrigen(guia);
		miP.setGlosa(glosa);
		miP.setCuenta(cuenta);
		miP.setEstado("V".charAt(0));
		guia.setMovimientoIngresoOrigen(miP);
		em.merge(miP);
		
		return miP.getNroComprobante();
	}
	
	@Override
	public void quitarPagoOrigen(Long idGuia) throws Exception {
		Guia guia = buscarPorId(idGuia);
		MovimientoIngreso movIngreso = guia.getMovimientoIngresoOrigen();
		MovimientoIngreso movIngresoP = em.find(MovimientoIngreso.class, movIngreso.getId());
		movIngresoP.setGuiaPagoOrigen(null);
		guia.setPagadoOrigen(false);
		guia.setMovimientoIngresoOrigen(null);
		em.merge(guia);
		movIngresoP = em.merge(movIngresoP);
		em.remove(movIngresoP);
	}
	
	@Override
	public Integer pagarDestino(Long idGuia, Double monto, String glosa) throws Exception {
		//Cuenta cuenta = servicioCuenta.getCuentaIngresoPorNroCuenta(1000);
		String userName = sctx.getCallerPrincipal().getName();
		Usuario user    = servicioUsuario.buscarPorUsuario(userName); 
		Oficina oficina = user.getOffice();
		Cuenta cuenta = oficina.getCuentaIngresoDestino(); 
		
		Guia guia = buscarPorId(idGuia);
		guia.setPagadoDestino(true);
		guia = merge(guia);
		
		
		MovimientoIngreso miP = servicioMovimiento.nuevoMovimientoIngreso();
		miP = em.find(MovimientoIngreso.class, miP.getId());
		miP.setFechaRegistro(new Date());
		miP.setMonto(monto);
		miP.setGuiaPagoDestino(guia);
		miP.setGlosa(glosa);
		miP.setCuenta(cuenta);
		miP.setEstado("V".charAt(0));
		guia.setMovimientoIngresoDestino(miP);
		em.merge(miP);
		
		return miP.getNroComprobante();
	}
	
	@Override
	public void quitarPagoDestino(Long idGuia) throws Exception {
		Guia guia = buscarPorId(idGuia);
		MovimientoIngreso movIngreso = guia.getMovimientoIngresoDestino();
		MovimientoIngreso movIngresoP = em.find(MovimientoIngreso.class, movIngreso.getId());
		movIngresoP.setGuiaPagoDestino(null);
		guia.setPagadoDestino(false);
		guia.setMovimientoIngresoDestino(null);
		em.merge(guia);
		movIngresoP = em.merge(movIngresoP);
		em.remove(movIngresoP);
	}
	
	@Override
	public Integer generarNroGuia(Long idGuia) {
		Query query = em.createQuery("SELECT MAX(g.nroGuia) FROM Guia g");
		Object object = query.getSingleResult();
		String numero = "0";
		if(object != null) numero = object.toString();
		Integer nroGuia = Integer.valueOf(numero) + 1;
		Guia guia = buscarPorId(idGuia);
		guia.setNroGuia(nroGuia);
		
		// Guardar Glosa 
		if(guia.getPagadoOrigen() != null) 
			if(guia.getPagadoOrigen()) {
				String glosa = "Ingreso en el Origen: " + guia.getOficinaOrigen().getNombre() + " -> " + guia.getOficinaDestino().getNombre() + " con Nro Guia: " + guia.getNroGuia();
				guia.getMovimientoIngresoOrigen().setGlosa(glosa);
			}
		if(guia.getPagadoDestino() != null) 	
			if(guia.getPagadoDestino()) {
				String glosa = "Ingreso en el Detino: " + guia.getOficinaOrigen().getNombre() + " -> " + guia.getOficinaDestino().getNombre() + " con Nro Guia: " + guia.getNroGuia();
				guia.getMovimientoIngresoDestino().setGlosa(glosa);
			}
		merge(guia);
		
		return nroGuia;
	}

	@Override
	public void guardarObservaciones(Long idGuia, String observaciones) throws Exception {
		Guia guia = buscarPorId(idGuia);
		guia.setObservaciones(observaciones);
		em.merge(guia);
	}
}
