package com.sinergia.dcargo.server;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import com.sinergia.dcargo.client.local.UtilDCargo;
import com.sinergia.dcargo.client.shared.ServicioMovimiento;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.Cuenta;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Movimiento;
import com.sinergia.dcargo.client.shared.dominio.MovimientoEgreso;
import com.sinergia.dcargo.client.shared.dominio.MovimientoIngreso;
import com.sinergia.dcargo.client.shared.dominio.TipoCuenta;
import com.sinergia.dcargo.client.shared.dto.DeudasPorCobrarReporte;
import com.sinergia.dcargo.client.shared.dto.DeudasReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionCargaReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionReporte;
import com.sinergia.dcargo.server.util.UtilDCargoServer;

/**
 * @author willy
 */
@Stateless
public class ServicioMovimientoImpl implements ServicioMovimiento {

	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	@Inject UtilDCargoServer utilDCargo;
	
	
	final private Hashtable<Character, String> estados = new Hashtable<>();
	
	public ServicioMovimientoImpl() {
		//estados.put('P', "Pendiente");
		estados.put('V', "Vigente");
		estados.put('A', "Anulado");
	}

	@Override
	public List<String> getEstados() throws Exception {
		List<String> estadosDTO = new ArrayList<>();
		for (Entry<Character, String> s :estados.entrySet()) {
			estadosDTO.add(s.getValue());
		}
		return estadosDTO;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Movimiento> buscarMovimientos(Movimiento mov) throws Exception {
		
		// Obteniendo Datos
		Integer nroComprobante = mov.getNroComprobante();
		nroComprobante = nroComprobante == null ? 0: nroComprobante;
		
		Date fechaInicio = mov.getFechaRegistroIni();
		Date fechaFin = mov.getFechaRegistroFin();
		
		if(fechaInicio != null) {
			fechaInicio.setHours(0);
			fechaInicio.setMinutes(0);
			fechaInicio.setSeconds(0);
		}
		if(fechaFin != null) {
			fechaFin.setHours(23);
			fechaFin.setMinutes(59);
			fechaFin.setSeconds(59);
		}
		
		Long idGuia = null; 
		Long idConocimiento = null;
		if(mov.getTipoCuenta() == TipoCuenta.INGRESO) {
			idGuia = mov.getIdGuia();
		} else if(mov.getTipoCuenta() == TipoCuenta.EGRESO) {
			idConocimiento = mov.getIdConocimiento();
		} else {
			
		}
		
		Long idSubCuenta = null;
		if(mov.getCuenta() != null)
			idSubCuenta = mov.getCuenta().getId();
		
		Character estado = mov.getEstado();
		
		// Armando query
		HashMap<String, Object> parametros = new HashMap<>(); 
		String where = " c.estado <> :estadoPendiente AND";
		
		if(!mov.getNombreUsuarioRev().equals("Todos")) {
			where = where + " c.nombreUsuarioRev = :nombreUsuarioRev AND";
		}
		
		if(0 != nroComprobante){
			where = where + " c.nroComprobante = :nroComprobante AND ";
			parametros.put("nroComprobante", nroComprobante);
		}
		if(fechaInicio != null && fechaFin != null){
			where = where + " c.fechaRegistro BETWEEN :fechaInicio AND :fechaFin AND ";
			parametros.put("fechaInicio", fechaInicio);
			parametros.put("fechaFin", fechaFin);
		}
		if(idGuia != null)
			if(idGuia != 0){
				where = where + " c.guia.id = :idGuia AND";
				parametros.put("idGuia", idGuia);
			}
		if(idConocimiento != null)
			if(idConocimiento != 0) {
				where = where + " c.conocimiento.id = :idConocimiento AND";
				parametros.put("idConocimiento", idConocimiento);
			}
		if(idSubCuenta != null) 
			if(idSubCuenta != 0) {
				where = where + " c.cuenta.id = :idSubCuenta AND";
				parametros.put("idSubCuenta", idSubCuenta);
			}
		if(estado != null){
			where = where + " c.estado = :estado AND";
			parametros.put("estado", estado);
		}
		String entidad = "";
		if(mov.getTipoCuenta() == null) entidad = "Movimiento";
		else {
			if(mov.getTipoCuenta() == TipoCuenta.INGRESO) entidad = "MovimientoIngreso";
			else entidad = "MovimientoEgreso";
		}
		
		String query = null;
		String select = "SELECT c FROM " + entidad + " c";
		if("".equals(where)){
			query = select;
		} else {
			where = where.substring(0, where.length() - 4);
			query = select + " WHERE " + where;
		}
		
		Query q = em.createQuery(query);
		for (Entry<String, Object> e: parametros.entrySet()) {
			if(e.getValue() instanceof Date) 
				q.setParameter(e.getKey(), (Date)e.getValue(), TemporalType.TIMESTAMP);
			else 
				q.setParameter(e.getKey(), e.getValue());
		}
		q.setParameter("estadoPendiente", 'P');
		if(!mov.getNombreUsuarioRev().equals("Todos")) {
			q.setParameter("nombreUsuarioRev", mov.getNombreUsuarioRev());
		}
		
		System.out.println("-> query: " + query);
		@SuppressWarnings("unchecked")
		List<Movimiento> movimientos = q.getResultList();
		
		List<Movimiento> movimientosDTO = new ArrayList<>();
		for (Movimiento movP :movimientos) movimientosDTO.add(serializarParaBusqueda(movP)); 
		
		return movimientosDTO;
		
	}
	
	@Override
	public  MovimientoIngreso nuevoMovimientoIngreso() throws Exception {
		
		MovimientoIngreso mov = new MovimientoIngreso();
		//mov.setNroComprobante(nroComprobante);
		mov.setFechaRegistro(new Date());
		mov.setEstado('P');
		mov.setNombreUsuarioRev(sctx.getCallerPrincipal().getName());
		
		Movimiento movP = em.merge(mov);
		mov.setId(movP.getId());
		
		
		return mov;
	}
	
	@Override
	public MovimientoEgreso nuevoMovimientoEgreso() throws Exception {
		
		MovimientoEgreso mov = new MovimientoEgreso();
//		mov.setNroComprobante(nroComprobante);
		mov.setFechaRegistro(new Date());
		mov.setEstado('P');
		mov.setNombreUsuarioRev(sctx.getCallerPrincipal().getName());
		
		Movimiento movP = em.merge(mov);
		mov.setId(movP.getId());
		
		return mov;
	}

	@Override
	public void guardarFechaRegistro(Long idMovimiento, Long fechaRegistro, TipoCuenta tipoCuenta) throws Exception {
		Movimiento movP = buscarPor(idMovimiento, tipoCuenta);
		movP.setFechaRegistro(new Date(fechaRegistro));
		em.merge(movP);
	}
	
	@Override
	public void guardarSubCuenta(Long idMovimiento, Long idSubCuenta, TipoCuenta tipoCuenta) throws Exception {
		Movimiento movP = buscarPor(idMovimiento, tipoCuenta);
		Cuenta cuenta = em.find(Cuenta.class, idSubCuenta); 
		movP.setCuenta(cuenta);
		em.merge(movP);
	}

	@Override
	public void guardarMonto(Long idMovimiento, Double monto, TipoCuenta tipoCuenta) throws Exception {
		Movimiento movP = buscarPor(idMovimiento, tipoCuenta);
		movP.setMonto(monto);
		movP.setNombreUsuarioRev(sctx.getCallerPrincipal().getName());
		em.merge(movP);
	}

	@Override
	public void guardarGlosa(Long idMovimiento, String glosa, TipoCuenta tipoCuenta) throws Exception {
		Movimiento movP = buscarPor(idMovimiento, tipoCuenta);
		movP.setGlosa(glosa);
		em.merge(movP);
	}

	@Override
	public void cambiarEstado(Long idMovimiento, String estado) throws Exception {
		Movimiento movP = em.find(Movimiento.class, idMovimiento);
		movP.setEstado(estado.charAt(0));
		
//		if(movP.getEstado() == 'V') {
//			if(movP instanceof MovimientoIngreso) {
//				Guia guia = ((MovimientoIngreso)movP).getGuia();
//				
//			} 
//			if(movP instanceof MovimientoEgreso) {
//				Conocimiento cono = ((MovimientoEgreso)movP).getConocimiento();
//				
//			} 
//			
//			movP.setOrigen(movP.get);
//		}
		
		em.merge(movP);
	}
	
	@Override
	public void guardarGuia(Long idMovimiento, Long idGuia) throws Exception {
		MovimientoIngreso movP = em.find(MovimientoIngreso.class, idMovimiento);
		Guia guia = em.find(Guia.class, idGuia);
		movP.setGuia(guia);
		//em.merge(movP);
		em.merge(guia);
		
	}

	@Override
	public void guardarConocimiento(Long idMovimiento, Long idConocimiento) throws Exception {
		MovimientoEgreso movP = em.find(MovimientoEgreso.class, idMovimiento);
		Conocimiento con = em.find(Conocimiento.class, idConocimiento);
		movP.setConocimiento(con);
		em.merge(movP);
	}
	
	private Movimiento serializarParaBusqueda(Movimiento mov) throws Exception{
		
		Movimiento movTO = null;
		if(mov instanceof MovimientoIngreso){
			movTO = new MovimientoIngreso();
			movTO.setTipoCuenta(TipoCuenta.INGRESO);
		} else  {
			movTO = new MovimientoEgreso();
			movTO.setTipoCuenta(TipoCuenta.EGRESO);
		}
		
		movTO.setId(mov.getId());
		movTO.setNroComprobante(mov.getNroComprobante());
		movTO.setFechaRegistro(mov.getFechaRegistro());
		movTO.setTipoCuenta((mov instanceof MovimientoIngreso) ? TipoCuenta.INGRESO : TipoCuenta.EGRESO);
		movTO.setMonto(mov.getMonto());
		movTO.setGlosa(mov.getGlosa());
		movTO.setEstado(mov.getEstado());
		movTO.setEstadoDescripcion(estados.get(mov.getEstado()));
		movTO.setNombreUsuarioRev(mov.getNombreUsuarioRev());
		
		if(mov instanceof MovimientoIngreso) {
			
			MovimientoIngreso movIngreso = ((MovimientoIngreso)mov);
			Guia guia = null;
			if(movIngreso.getGuiaPagoOrigen() != null) 
				guia = movIngreso.getGuiaPagoOrigen(); 
			if(movIngreso.getGuiaPagoDestino() != null)
				guia = movIngreso.getGuiaPagoDestino();
			if(movIngreso.getGuia() != null)
				guia = movIngreso.getGuia();
			
			
			if(guia == null) {
				movTO.setOrigen("");
				movTO.setDestino("");
			} else {
				if(guia.getOficinaOrigen() == null) {
					movTO.setOrigen("");
				} else if(guia.getOficinaDestino() == null) {
					movTO.setDestino("");
				} else {
					movTO.setOrigen(guia.getOficinaOrigen().getNombre());
					movTO.setDestino(guia.getOficinaDestino().getNombre());
				}
			}

			if(guia != null) {
				movTO.setPagoOrigen(guia.getPagoOrigen());
			    movTO.setPagoDestino(guia.getSaldoDestino());
			}
			
			if(guia != null) 
				movTO.setNroGuiOrConocimiento(guia.getNroGuia()+"");
			
		} else {
			
			MovimientoEgreso movEgreso = (MovimientoEgreso)mov; 
			
			if(movEgreso.getConocimiento() != null) {
				movTO.setOrigen(utilDCargo.validarNullParaMostrar(movEgreso.getConocimiento().getOficinaOrigen().getNombre()));
				movTO.setDestino(utilDCargo.validarNullParaMostrar(movEgreso.getConocimiento().getOficinaDestino().getNombre()));
			}
			
			if(movEgreso.getConocimientoAcuenta() != null) {
				movTO.setOrigen(utilDCargo.validarNullParaMostrar(movEgreso.getConocimientoAcuenta().getOficinaOrigen().getNombre()));
				movTO.setDestino(utilDCargo.validarNullParaMostrar(movEgreso.getConocimientoAcuenta().getOficinaDestino().getNombre()));
			}
			
			if(movEgreso.getConocimiento() != null) {
				movTO.setPagoOrigen(movEgreso.getConocimiento().getPagoOrigen());
			    movTO.setPagoDestino(movEgreso.getConocimiento().getPagoDestino());
			}
		    
			if(((MovimientoEgreso) mov).getConocimiento() != null) 
				movTO.setNroGuiOrConocimiento(((MovimientoEgreso) mov).getConocimiento().getNroConocimiento()+"");
			if(((MovimientoEgreso) mov).getConocimientoAcuenta() != null) 
				movTO.setNroGuiOrConocimiento(((MovimientoEgreso) mov).getConocimientoAcuenta().getNroConocimiento()+"");
		}
		
		if(mov.getCuenta() != null) {
			Cuenta cuenta = new Cuenta();
			cuenta.setId(mov.getCuenta().getId());
			cuenta.setNroCuenta(mov.getCuenta().getNroCuenta());
			cuenta.setDescripcion(mov.getCuenta().getDescripcion());
			movTO.setCuenta(cuenta);
			
	        if(mov.getCuenta().getCuenta() == null) {
	        	movTO.setNroCuentaPadre(mov.getCuenta().getNroCuenta());
		        movTO.setDescripcionPadre(mov.getCuenta().getDescripcion());
	        } else {
	        	movTO.setNroCuentaPadre(mov.getCuenta().getCuenta().getNroCuenta());
		        movTO.setDescripcionPadre(mov.getCuenta().getCuenta().getDescripcion());
	        }
		}
		
		
		
		return movTO;
	}
	
	private Movimiento buscarPor(Long idMovimiento, TipoCuenta tipoCuenta){
		Class<? extends Movimiento> clazz = null;
		if(tipoCuenta == TipoCuenta.INGRESO) clazz = MovimientoIngreso.class;
		else clazz = MovimientoEgreso.class;
		Movimiento movP = em.find(clazz, idMovimiento);
		return movP;
	}

	@SuppressWarnings("deprecation")
	@Override
	public LiquidacionCargaReporte reporteLiquidacionCarga(LiquidacionReporte liquidacionReporte) throws Exception {
		
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date fechaInicio = liquidacionReporte.getFechaInicioBusqueda();
		Date fechaFin = liquidacionReporte.getFechaDestinoBusqueda();
		
		System.out.println("fechaInicio: " + dt1.format(fechaInicio));
		System.out.println("fechaFin: " + dt1.format(fechaFin));
		
		Date fechaInicio1 = new Date();
		fechaInicio1.setYear(fechaInicio.getYear());
		fechaInicio1.setMonth(fechaInicio.getMonth());
		fechaInicio1.setDate(fechaInicio.getDate());
		fechaInicio1.setHours(0);
		fechaInicio1.setMinutes(0);
		fechaInicio1.setSeconds(0);
		
		Date fechaFin1 = new Date();
		fechaFin1.setYear(fechaFin.getYear());
		fechaFin1.setMonth(fechaFin.getMonth());
		fechaFin1.setDate(fechaFin.getDate());
		fechaFin1.setHours(23);
		fechaFin1.setMinutes(59);
		fechaFin1.setSeconds(59);
		
		System.out.println("fechaInicio1: " + dt1.format(fechaInicio1));
		System.out.println("fechaFin1: " + dt1.format(fechaFin1));
		
		Calendar calIni = new GregorianCalendar(fechaInicio.getYear() - 1900, fechaInicio.getMonth()+1, fechaInicio.getDate());
		Calendar calFin = new GregorianCalendar(fechaFin.getYear() - 1900, fechaFin.getMonth()+1, fechaFin.getDate());
		
		System.out.println("Ini - year: " + calIni.getTime().getYear() + " month: " + calIni.getTime().getMonth() + " date: " + calIni.getTime().getDate());
		System.out.println("Fin - year: " + calFin.getTime().getYear() + " month: " + calFin.getTime().getMonth() + " date: " + calFin.getTime().getDate());
		
		
		
		
		Long idOficinaOrigen  = liquidacionReporte.getIdOrigenBusqueda() == null ? 0 : liquidacionReporte.getIdOrigenBusqueda(); 
		Long idOficinaDestino = liquidacionReporte.getIddDestinoBusqueda() == null ? 0 : liquidacionReporte.getIddDestinoBusqueda();
		
		
		Integer porcentajeDestino = liquidacionReporte.getPorcentajeDestinoBusqueda();
		
		//String sql = "SELECT c FROM Conocimiento c WHERE c.fechaRegistro >= :fechaInicio AND c.fechaRegistro <= :fechaFin  AND " +
		String sql = "SELECT c FROM Conocimiento c WHERE c.fechaRegistro BETWEEN :fechaInicio AND :fechaFin AND " + 
				     "c.oficinaOrigen.id = :idOficinaOrigen AND c.oficinaDestino.id = :idOficinaDestino ORDER BY c.fechaRegistro ASC";
		Query query = em.createQuery(sql);
		query.setParameter("fechaInicio", fechaInicio1, TemporalType.TIMESTAMP);
		query.setParameter("fechaFin", fechaFin1, TemporalType.TIMESTAMP);
		query.setParameter("idOficinaOrigen", idOficinaOrigen);
		query.setParameter("idOficinaDestino", idOficinaDestino);
		//query.setParameter("estado", 'V');
		@SuppressWarnings("unchecked")
		List<Conocimiento> csP = query.getResultList();

		DecimalFormat df = new DecimalFormat("###,###.##" );
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		
		LiquidacionCargaReporte liquidacionCargaReporte = new LiquidacionCargaReporte();
		List<LiquidacionReporte> ls =  new ArrayList<LiquidacionReporte>();
		int nro = 1;
		Double sumaCobroOrigen  = 0.0;
		Double sumaCobroDestino = 0.0;
		Double sumaFleteDestino = 0.0;
		for (Conocimiento cP: csP) {
			int jg = 1;
			for (Guia gP: cP.getGuias()) {
				LiquidacionReporte lr;
				Double pagoDestinoConocimiento = cP.getPagoDestino() == null ? 0.0 : cP.getPagoDestino();
				if(jg++ == 1) {
					lr = new LiquidacionReporte();
					lr.setFecha(dt.format(cP.getFechaRegistro()));
					lr.setNroConocimiento(cP.getNroConocimiento()+"");
					lr.setFleteDestino(df.format(pagoDestinoConocimiento));
					sumaFleteDestino = sumaFleteDestino + pagoDestinoConocimiento;
				} else {
					lr = new LiquidacionReporte();
					lr.setFecha("");
					lr.setNroConocimiento("");
					lr.setFleteDestino("");
				}
				
				lr.setNro(nro++);
				lr.setNroGuia(gP.getNroGuia()+"");
				lr.setCobroOrigen(df.format(gP.getPagoOrigen()));
				sumaCobroOrigen = sumaCobroOrigen + gP.getPagoOrigen();
				lr.setCobroDestino(df.format(gP.getSaldoDestino()));
				sumaCobroDestino = sumaCobroDestino + gP.getSaldoDestino();
				ls.add(lr);
			}
		}
		liquidacionCargaReporte.setLiquidacionesReporte(ls);
		liquidacionCargaReporte.setNroTotalConocimientos(csP.size()+"");
		liquidacionCargaReporte.setNroTotalGuia((nro-1)+"");
		liquidacionCargaReporte.setTotalCobroOrigen(df.format(sumaCobroOrigen));
		liquidacionCargaReporte.setTotalCobroOrigenDouble(sumaCobroOrigen);
		liquidacionCargaReporte.setTotalCobroDestino(df.format(sumaCobroDestino));
		liquidacionCargaReporte.setTotalCobroDestinoDouble(sumaCobroDestino);
		liquidacionCargaReporte.setTotalFleteDestino(df.format(sumaFleteDestino));
		liquidacionCargaReporte.setTotalFleteDestinoDouble(sumaFleteDestino);
		
		return liquidacionCargaReporte;
		
	}
	
	@Override
	public DeudasPorCobrarReporte reporteDeudasPorCobrar(DeudasReporte deudasReporte) throws Exception {
		
		SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		Date fechaInicio = deudasReporte.getFechaInicioBusqueda();
		Date fechaFin = deudasReporte.getFechaDestinoBusqueda();
		
		System.out.println("fechaInicio: " + dt1.format(fechaInicio));
		System.out.println("fechaFin: " + dt1.format(fechaFin));
		
		Date fechaInicio1 = new Date();
		fechaInicio1.setYear(fechaInicio.getYear());
		fechaInicio1.setMonth(fechaInicio.getMonth());
		fechaInicio1.setDate(fechaInicio.getDate());
		fechaInicio1.setHours(0);
		fechaInicio1.setMinutes(0);
		fechaInicio1.setSeconds(0);
		
		Date fechaFin1 = new Date();
		fechaFin1.setYear(fechaFin.getYear());
		fechaFin1.setMonth(fechaFin.getMonth());
		fechaFin1.setDate(fechaFin.getDate());
		fechaFin1.setHours(23);
		fechaFin1.setMinutes(59);
		fechaFin1.setSeconds(59);
		
		System.out.println("fechaInicio1: " + dt1.format(fechaInicio1));
		System.out.println("fechaFin1: " + dt1.format(fechaFin1));
		
		Calendar calIni = new GregorianCalendar(fechaInicio.getYear() - 1900, fechaInicio.getMonth()+1, fechaInicio.getDate());
		Calendar calFin = new GregorianCalendar(fechaFin.getYear() - 1900, fechaFin.getMonth()+1, fechaFin.getDate());
		
		System.out.println("Ini - year: " + calIni.getTime().getYear() + " month: " + calIni.getTime().getMonth() + " date: " + calIni.getTime().getDate());
		System.out.println("Fin - year: " + calFin.getTime().getYear() + " month: " + calFin.getTime().getMonth() + " date: " + calFin.getTime().getDate());
		
		
		Long idOficinaOrigen  = deudasReporte.getIdOrigenBusqueda() == null ? 0 : deudasReporte.getIdOrigenBusqueda(); 
		//Long idOficinaDestino = deudasReporte.getIddDestinoBusqueda() == null ? 0 : deudasReporte.getIddDestinoBusqueda();
		
		
		Long idCliente = deudasReporte.getIdCliente();
		
		String queryAddCliente = "";
		if(idCliente != null) 
			queryAddCliente = " AND (c.remitente.id = :idCliente OR c.consignatario.id = :idCliente) ";
		
		//String sql = "SELECT c FROM Conocimiento c WHERE c.fechaRegistro >= :fechaInicio AND c.fechaRegistro <= :fechaFin  AND " +
		String sql = "SELECT c FROM Guia c WHERE c.fechaRegistro BETWEEN :fechaInicio AND :fechaFin AND " + 
				     //"c.oficinaOrigen.id = :idOficinaOrigen AND c.oficinaDestino.id = :idOficinaDestino " +
				     "c.oficinaOrigen.id = :idOficinaOrigen " +
				     queryAddCliente +
				     " ORDER BY c.fechaRegistro ASC";
		Query query = em.createQuery(sql);
		query.setParameter("fechaInicio", fechaInicio1, TemporalType.TIMESTAMP);
		query.setParameter("fechaFin", fechaFin1, TemporalType.TIMESTAMP);
		query.setParameter("idOficinaOrigen", idOficinaOrigen);
		//query.setParameter("idOficinaDestino", idOficinaDestino);
		if(idCliente != null)
		   query.setParameter("idCliente", idCliente);
		
		@SuppressWarnings("unchecked")
		List<Guia> csP = query.getResultList();

		DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		
		DeudasPorCobrarReporte deudasPorCobrarReporte = new DeudasPorCobrarReporte();
		//List<LiquidacionReporte> ls =  new ArrayList<LiquidacionReporte>();
		int nro = 1;
		Double totalDeudasMonto    = 0.0;
		Double totalIngresoAcuenta = 0.0;
		for (Guia gP: csP) {
			DeudasReporte d = new DeudasReporte();
			d.setId(gP.getId());
			d.setNro(nro++);
			d.setFecha(dt.format(gP.getFechaRegistro()));
			d.setNroGuia(gP.getNroGuia()+"");
			d.setOrigen(gP.getOficinaOrigen()==null ? "":gP.getOficinaOrigen().getNombre());
			d.setDestino(gP.getOficinaDestino()==null ? "":gP.getOficinaDestino().getNombre());
			String remitente = gP.getRemitente() == null ? "" : gP.getRemitente().getNombre();
			String consignatario = gP.getConsignatario() == null ? "" : gP.getConsignatario().getNombre();
			String clientes = "R:" + remitente + "\n" + " C:" + consignatario; 
			d.setDeudasClientes(clientes);
			d.setDeudasMonto(df.format(gP.getTotalGuia() == null ? 0.0 : gP.getTotalGuia()));
			
			MovimientoIngreso movIngreso = null;
			if(gP.getMovimientoIngresoOrigen() != null) movIngreso = gP.getMovimientoIngresoOrigen(); 
			else movIngreso = gP.getMovimientoIngresoDestino();	
			
			if(movIngreso != null) {
				String fechaIngreso =  movIngreso.getFechaRegistro() == null ? "" : dt.format(movIngreso.getFechaRegistro());
				d.setIngresosFecha(fechaIngreso);
				d.setIngresosNroComprobante(movIngreso.getNroGuiOrConocimiento());
				d.setIngresosAcuenta(df.format(movIngreso.getMonto()));
				d.setIngresosSaldo("");
			} else {
				d.setIngresosFecha("");
				d.setIngresosNroComprobante("");
				d.setIngresosAcuenta("");
				d.setIngresosSaldo("");
			}
			deudasPorCobrarReporte.getDeudasReporte().add(d);
		}
		deudasPorCobrarReporte.setMontoTotalDeudas(df.format(totalDeudasMonto));
		deudasPorCobrarReporte.setMontoTotalAcuenta(df.format(totalIngresoAcuenta));
		
		return deudasPorCobrarReporte;
		
	}
	
	@Override
	public Integer generarNroComprobanteIngreso(Long idMovimiento) throws Exception {
		String entidad = "MovimientoIngreso";
		Query query = em.createQuery("SELECT MAX(g.nroComprobante) FROM " + entidad + " g");
		Object object = query.getSingleResult();
		String numero = "0";
		if(object != null) numero = object.toString();
		Integer nroComprobante = Integer.valueOf(numero) + 1;
		MovimientoIngreso vi = em.find(MovimientoIngreso.class, idMovimiento);
		vi.setNroComprobante(nroComprobante);
		em.merge(vi);
		
		return nroComprobante;
	}
	
	@Override
	public Integer generarNroComprobanteEgreso(Long idMovimiento) throws Exception{
		String entidad = "MovimientoEgreso";
		Query query = em.createQuery("SELECT MAX(g.nroComprobante) FROM " + entidad + " g");
		Object object = query.getSingleResult();
		String numero = "0";
		if(object != null) numero = object.toString();
		Integer nroComprobante = Integer.valueOf(numero) + 1;
		MovimientoEgreso vi = em.find(MovimientoEgreso.class, idMovimiento);
		vi.setNroComprobante(nroComprobante);
		em.merge(vi);
		return nroComprobante;
	}
}
