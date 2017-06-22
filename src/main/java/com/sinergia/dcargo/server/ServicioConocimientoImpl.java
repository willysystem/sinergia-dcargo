package com.sinergia.dcargo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.ServicioCliente;
import com.sinergia.dcargo.client.shared.ServicioConocimiento;
import com.sinergia.dcargo.client.shared.ServicioGuia;
import com.sinergia.dcargo.client.shared.Transportista;
import com.sinergia.dcargo.client.shared.Conocimiento;
import com.sinergia.dcargo.client.shared.EstadoGuia;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Oficina;
import com.sinergia.dcargo.client.shared.OficinaServicio;
import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.server.dao.Dao;

/**
 * @author willy
 */
@Stateless
public class ServicioConocimientoImpl extends Dao<Conocimiento> implements ServicioConocimiento {

	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	@EJB
	private ServicioCliente serviceCliente;
	
	@EJB
	private OficinaServicio oficinaServicio;
	
	@EJB
	private ServicioGuia servicioGuia; 
	
	final private Hashtable<Character, String> estados = new Hashtable<>();
	
	public ServicioConocimientoImpl() {
		super(new Conocimiento());
		estados.put('P', "Pendiente");
		estados.put('V', "Vigente");
		estados.put('E', "Entregado");
		estados.put('A', "Anulado");
	}
	
	@Override
	public List<Conocimiento> buscarConocimiento(Conocimiento conocimiento) {
		Integer nroConocimiento = conocimiento.getNroConocimiento();
		nroConocimiento = nroConocimiento == null ? 0: nroConocimiento;
		
		Date fechaInicio = conocimiento.getFechaIni();
		
		Date fechaFin    = conocimiento.getFechaFin();
		
		Transportista propietario  = conocimiento.getTransportistaPropietario();
		Long idPropietario = propietario.getId() == null ? 0 : propietario.getId();
		
		Transportista conductor = conocimiento.getTransportistaConductor();
		Long idConductor = conductor.getId() == null ? 0 : conductor.getId();

		Oficina oficinaOrigen = conocimiento.getOficinaOrigen();
		Long idOficinaOrigen = oficinaOrigen.getId() == null ? 0 : oficinaOrigen.getId(); 
		
		Oficina oficinaDestino = conocimiento.getOficinaDestino();
		Long idOficinaDestino = oficinaDestino.getId() == null ? 0 : oficinaDestino.getId();
		
		Character estado = getEstado(conocimiento.getEstadoDescripcion());
		
		HashMap<String, Object> parametros = new HashMap<>(); 
		String where = "";
		
		if(0 != nroConocimiento){
			where = "c.nroConocimiento = :nroConocimiento AND";
			parametros.put("nroGuia", nroConocimiento);
		} 
		if(fechaInicio != null){
			where = "c.fechaRegistro >= :fechaInicio AND";
			parametros.put("fechaInicio", fechaInicio);
		}
		if(fechaFin != null){
			where = "c.fechaRegistro <= :fechaFin AND";
			parametros.put("fechaFin", fechaFin);
		}
		if(idPropietario != 0){
			where = "c.transportistaPropietario.id = :idPropietario AND";
			parametros.put("idPropietario", idPropietario);
		}
		if(idConductor != 0){
			where = "c.transportistaConductor.id = :idConductor AND";
			parametros.put("idConductor", idConductor);
		}
		if(idOficinaOrigen != 0) {
			where = "c.oficinaOrigen.id = :idOficinaOrigen AND";
			parametros.put("idOficinaOrigen", idOficinaOrigen);
		}
		if(idOficinaDestino != 0) {
			where = "c.oficinaDestino.id = :idOficinaDestino AND";
			parametros.put("idOficinaDestino", idOficinaDestino);
		}
		if(estado != null){
			where = where + " c.estado = :estado AND";
			parametros.put("estado", estado);
		}
		
		String query = null;
		String select = "SELECT c FROM Conocimiento c";
		if("".equals(where)){
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		
		Query q = em.createQuery(query);
		for (Entry<String, Object> e: parametros.entrySet()) q.setParameter(e.getKey(), e.getValue()); 
		
		
		System.out.println("-> query: " + query);
		@SuppressWarnings("unchecked")
		List<Conocimiento> conocimientos = q.getResultList();
		
		List<Conocimiento> conocimientosDTO = new ArrayList<>();
		for (Conocimiento conocimientoP : conocimientos) conocimientosDTO.add(serializarParaBusqueda(conocimientoP)); 
		
		return conocimientosDTO;
	}
	
	@Override
	public Conocimiento nuevoConocimiento() throws Exception {
		
		Query query = em.createQuery("SELECT MAX(g.nroConocimiento) FROM Conocimiento g");
		Object object = query.getSingleResult();
		String numero = "0";
		if(object != null) numero = object.toString();
		Integer nroConocimiento = Integer.valueOf(numero) + 1;
		
		Conocimiento c = new Conocimiento();
		c.setNroConocimiento(nroConocimiento);
		c.setFechaRegistro(new Date());
		c.setEstado('P');
		
		Conocimiento cP = merge(c);
		c.setId(cP.getId());
		return c;
	}

	@Override
	public Conocimiento consultarConocimiento(Long idConocimiento) {
		Conocimiento cP = buscarPorId(idConocimiento);
		Conocimiento cDTO = serializarParaBusqueda(cP);
		cDTO.setMulta(cP.getMulta());
		cDTO.setDias(cP.getDias());
		cDTO.setObservacion(cP.getObservacion());
		cDTO.setAdjunto(cP.getAdjunto());
		cDTO.setAdjunto2(cP.getAdjunto2());
		cDTO.setFlete(cP.getFlete());
		cDTO.setAcuenta(cP.getAcuenta());
		cDTO.setEndestino(cP.getEndestino());
		cDTO.setSaldo(cP.getSaldo());
		cDTO.setAclaracion(cP.getAclaracion());
		cDTO.setAclaracion2(cP.getAclaracion2());
		
		List<Guia> guias = new ArrayList<>();
		for (Guia gP : cP.getGuias()) {
			Guia gDTO = servicioGuia.serializarParaBusqueda(gP);
			guias.add(gDTO);
		}
		
		if(cP.getOficinaOrigen() != null){
			Oficina oficinaOrigen = new Oficina();
			oficinaOrigen.setId(cP.getOficinaOrigen().getId());
			oficinaOrigen.setNombre(cP.getOficinaOrigen().getNombre());
			cDTO.setOficinaOrigen(oficinaOrigen);
		}
		
		if(cP.getOficinaDestino() != null){
			Oficina oficinaDestino = new Oficina();
			oficinaDestino.setId(cP.getOficinaDestino().getId());
			oficinaDestino.setNombre(cP.getOficinaDestino().getNombre());
			cDTO.setOficinaDestino(oficinaDestino);
		}
		
		if(cP.getTransportistaPropietario() != null){
			Transportista propietario = new Transportista();
			propietario.setId(cP.getTransportistaPropietario().getId());
			propietario.setNombre(cP.getTransportistaPropietario().getNombre());
			cDTO.setTransportistaPropietario(propietario);
		}
			
		if(cP.getTransportistaConductor() != null){
			Transportista conductor = new Transportista();
			conductor.setId(cP.getTransportistaConductor().getId());
			conductor.setNombre(cP.getTransportistaConductor().getNombre());
			cDTO.setTransportistaConductor(conductor);
		}
		
		return cDTO;
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

	@Override
	public void guardarFechaRegistro(Long idConocimiento, Date fechaRegistro) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setFechaRegistro(fechaRegistro);
		merge(cP);
	}

	@Override
	public void guardarPropietario(Long idConocimiento, Long idPropietario) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setTransportistaPropietario(em.find(Transportista.class, idPropietario));
		merge(cP);
	}

	@Override
	public void guardarConductor(Long idConocimiento, Long idConductor) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setTransportistaConductor(em.find(Transportista.class, idConductor));
		merge(cP);
	}

	@Override
	public void guardarOficinaOrigen(Long idConocimiento, Long idOficinaOrigen) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setOficinaOrigen(em.find(Oficina.class, idOficinaOrigen));
		merge(cP);
	}

	@Override
	public void guardarOficinaDestino(Long idConocimiento, Long idOficinaDestino) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setOficinaDestino(em.find(Oficina.class, idOficinaDestino));
		merge(cP);
	}

	@Override
	public void guardarMulta(Long idConocimiento, Double multa) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setMulta(multa);
		merge(cP);
	}

	@Override
	public void guardarDias(Long idConocimiento, Integer dias) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setDias(dias);
		merge(cP);
	}

	@Override
	public void guardarObservacion(Long idConocimiento, String observacion) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setObservacion(observacion);
		merge(cP);
	}

	@Override
	public void guardarAdjunto(Long idConocimiento, String adjunto) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setAdjunto(adjunto);
		merge(cP);
	}

	@Override
	public void guardarAdjunto2(Long idConocimiento, String adjunto2) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setAdjunto2(adjunto2);
		merge(cP);
	}

	@Override
	public void guardarAclaracion(Long idConocimiento, String aclaracion) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setAclaracion(aclaracion);
		merge(cP);
	}

	@Override
	public void guardarAclaracion2(Long idConocimiento, String aclaracion2) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setAclaracion2(aclaracion2);
		merge(cP);
	}

	@Override
	public void guardarFlete(Long idConocimiento, Double flete) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setFlete(flete);
		merge(cP);
	}

	@Override
	public void guardarAcuenta(Long idConocimiento, Double acuenta) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setAcuenta(acuenta);
		merge(cP);
	}

	@Override
	public void guardarEnDestino(Long idConocimiento, Double enDestino) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setEndestino(enDestino);
		merge(cP);
	}

	@Override
	public void guardarSaldo(Long idConocimiento, Double saldo) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setSaldo(saldo);
		merge(cP);
	}

	@Override
	public void cambiarEstado(Long idConocimiento, String estado) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		cP.setEstado(estado.charAt(0));
		merge(cP);
	}

	@Override
	public Resultado borrar(Long id) throws Exception {
		return null;
	}
	
	public Conocimiento serializarParaBusqueda(Conocimiento conocimiento){
		Conocimiento cDTO = new Conocimiento();
		cDTO.setId(conocimiento.getId());
		cDTO.setNroConocimiento(conocimiento.getNroConocimiento());
		cDTO.setFechaRegistro(conocimiento.getFechaRegistro());
		
		if(conocimiento.getTransportistaPropietario() != null){
			Transportista transportistaPropietario = new Transportista();
			transportistaPropietario.setId(conocimiento.getTransportistaPropietario().getId());
			transportistaPropietario.setNombre(conocimiento.getTransportistaPropietario().getNombre());
			cDTO.setTransportistaPropietario(transportistaPropietario);
		}
		if(conocimiento.getTransportistaConductor() != null){
			Transportista transportistaConductor = new Transportista();
			transportistaConductor.setId(conocimiento.getTransportistaConductor().getId());
			transportistaConductor.setNombre(conocimiento.getTransportistaConductor().getNombre());
			cDTO.setTransportistaConductor(transportistaConductor);
		}
		if(conocimiento.getOficinaOrigen() != null){
			Oficina oficinaOrigen = new Oficina();
			oficinaOrigen.setId(conocimiento.getOficinaOrigen().getId());
			oficinaOrigen.setNombre(conocimiento.getOficinaOrigen().getNombre());
			cDTO.setOficinaOrigen(oficinaOrigen);
		}
		if(conocimiento.getOficinaDestino() != null){
			Oficina oficinaDestino = new Oficina();
			oficinaDestino.setId(conocimiento.getOficinaDestino().getId());
			oficinaDestino.setNombre(conocimiento.getOficinaDestino().getNombre());
			cDTO.setOficinaOrigen(oficinaDestino);
		}
		cDTO.setEstado(conocimiento.getEstado());
		cDTO.setEstadoDescripcion(estados.get(conocimiento.getEstado()));
		
		return cDTO;
	}

	@Override
	public void adicionarGuia(Long idConocimiento, Long idGuia) throws Exception {
		Conocimiento cP = buscarPorId(idConocimiento);
		Guia gP = em.find(Guia.class, idGuia);
		gP.setConocimiento(cP);
		em.merge(gP);
		
	}

	@Override
	public void quitarGuia(Long idConocimiento, Long idGuia) throws Exception {
		//Conocimiento cP = buscarPorId(idConocimiento);
		Guia gP = em.find(Guia.class, idGuia);
		gP.setConocimiento(null);
		em.merge(gP);
	}

	@Override
	public List<Guia> getGuias(Long idConocimiento) throws Exception {
		Conocimiento conocimiento = buscarPorId(idConocimiento);
		Set<Guia> guiasP = conocimiento.getGuias();
		List<Guia> guias = new ArrayList<>();
		for (Guia guiaP : guiasP) {
			Guia guiaDTO = servicioGuia.serializarParaBusqueda(guiaP);
			guias.add(guiaDTO);
		}
		return guias;
	}
	
	private Character getEstado(String estadoDescripcion) {
		for (Map.Entry<Character, String> e: estados.entrySet()) {
			if(e.getValue().equals(estadoDescripcion)) return e.getKey();
		}
		return null;
	}
	
	
}
