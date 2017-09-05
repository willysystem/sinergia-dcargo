package com.sinergia.dcargo.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.ServicioMovimiento;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.Cuenta;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Movimiento;
import com.sinergia.dcargo.client.shared.dominio.MovimientoEgreso;
import com.sinergia.dcargo.client.shared.dominio.MovimientoIngreso;
import com.sinergia.dcargo.client.shared.dominio.TipoCuenta;

/**
 * @author willy
 */
@Stateless
public class ServicioMovimientoImpl implements ServicioMovimiento {

	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	
	final private Hashtable<Character, String> estados = new Hashtable<>();
	
	public ServicioMovimientoImpl() {
		estados.put('P', "Pendiente");
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

	@Override
	public List<Movimiento> buscarMovimientos(Movimiento mov) throws Exception {
		
		// Obteniendo Datos
		Integer nroComprobante = mov.getNroComprobante();
		nroComprobante = nroComprobante == null ? 0: nroComprobante;
		
		Date fechaInicio = mov.getFechaRegistroIni();
		Date fechaFin = mov.getFechaRegistroFin();
		
		Long idGuia = null; 
		Long idConocimiento = null;
		if(mov.getTipoCuenta() == TipoCuenta.INGRESO) {
			if(((MovimientoIngreso)mov).getGuia() != null)
				idGuia = ((MovimientoIngreso)mov).getGuia().getId();
		} else if(mov.getTipoCuenta() == TipoCuenta.EGRESO) {
			if(((MovimientoEgreso)mov).getConocimiento() != null)
				idConocimiento = ((MovimientoEgreso)mov).getConocimiento().getId();
		} else {
			
		}
		
		Long idSubCuenta = null;
		if(mov.getCuenta() != null)
			idSubCuenta = mov.getCuenta().getId();
		
		Character estado = mov.getEstado();
		
		// Armando query
		HashMap<String, Object> parametros = new HashMap<>(); 
		String where = "";
		
		if(0 != nroComprobante){
			where = "c.nroComprobante = :nroComprobante AND";
			parametros.put("nroComprobante", nroComprobante);
		}
		if(fechaInicio != null){
			where = "c.fechaRegistro >= :fechaInicio AND";
			parametros.put("fechaInicio", fechaInicio);
		}
		if(fechaFin != null){
			where = "c.fechaRegistro <= :fechaFin AND";
			parametros.put("fechaFin", fechaFin);
		}
		if(idGuia != null)
			if(idGuia != 0){
				where = "c.guia.id = :idGuia AND";
				parametros.put("idGuia", idGuia);
			}
		
		if(idConocimiento != null)
			if(idConocimiento != 0) {
				where = "c.conocimiento.id = :idConocimiento AND";
				parametros.put("idConocimiento", idConocimiento);
			}
		
		if(idSubCuenta != null) 
			if(idSubCuenta != 0) {
				where = "c.cuenta.id = :idSubCuenta AND";
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
		for (Entry<String, Object> e: parametros.entrySet()) q.setParameter(e.getKey(), e.getValue());
		
		System.out.println("-> query: " + query);
		@SuppressWarnings("unchecked")
		List<Movimiento> movimientos = q.getResultList();
		
		List<Movimiento> movimientosDTO = new ArrayList<>();
		for (Movimiento movP :movimientos) movimientosDTO.add(serializarParaBusqueda(movP)); 
		
		return movimientosDTO;
		
	}
	
	@Override
	public  MovimientoIngreso nuevoMovimientoIngreso() throws Exception {
		
		String entidad = "MovimientoIngreso";
		Query query = em.createQuery("SELECT MAX(g.nroComprobante) FROM " + entidad + " g");
		Object object = query.getSingleResult();
		String numero = "0";
		if(object != null) numero = object.toString();
		Integer nroComprobante = Integer.valueOf(numero) + 1;
		
		MovimientoIngreso mov = new MovimientoIngreso();
		mov.setNroComprobante(nroComprobante);
		mov.setFechaRegistro(new Date());
		mov.setEstado('P');
		
		Movimiento movP = em.merge(mov);
		mov.setId(movP.getId());
		
		return mov;
	}
	
	@Override
	public MovimientoEgreso nuevoMovimientoEgreso() throws Exception {
		String entidad = "MovimientoEgreso";
		Query query = em.createQuery("SELECT MAX(g.nroComprobante) FROM " + entidad + " g");
		Object object = query.getSingleResult();
		String numero = "0";
		if(object != null) numero = object.toString();
		Integer nroComprobante = Integer.valueOf(numero) + 1;
		
		MovimientoEgreso mov = new MovimientoEgreso();
		mov.setNroComprobante(nroComprobante);
		mov.setFechaRegistro(new Date());
		mov.setEstado('P');
		
		Movimiento movP = em.merge(mov);
		mov.setId(movP.getId());
		
		return mov;
	}

	@Override
	public void guardarFechaRegistro(Long idMovimiento, Date fechaRegistro, TipoCuenta tipoCuenta) throws Exception {
		Movimiento movP = buscarPor(idMovimiento, tipoCuenta);
		movP.setFechaRegistro(fechaRegistro);
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
		em.merge(movP);
	}
	
	@Override
	public void guardarGuia(Long idMovimiento, Long idGuia) throws Exception {
		MovimientoIngreso movP = em.find(MovimientoIngreso.class, idMovimiento);
		Guia guia = em.find(Guia.class, idGuia);
		movP.setGuia(guia);
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
		movTO.setTipoCuenta((mov instanceof MovimientoEgreso) ? TipoCuenta.INGRESO : TipoCuenta.EGRESO);
		movTO.setMonto(mov.getMonto());
		movTO.setGlosa(mov.getGlosa());
		movTO.setEstado(mov.getEstado());
		movTO.setEstadoDescripcion(estados.get(mov.getEstado()));
		
		if(mov instanceof MovimientoIngreso) {
			
			if(((MovimientoIngreso)mov).getGuia() == null)                 throw new Exception("Guia nula"); 
			if(((MovimientoIngreso)mov).getGuia().getOficinaOrigen() == null) throw new Exception("Guia Origen nulo");             			
			if(((MovimientoIngreso)mov).getGuia().getOficinaDestino() == null) throw new Exception("Guia Destino nulo");
			
			mov.setOrigen(((MovimientoIngreso)mov).getGuia().getOficinaOrigen().getNombre());
			mov.setDestino(((MovimientoIngreso)mov).getGuia().getOficinaDestino().getNombre());
		
//				Guia guia = new Guia();
//				guia.setId(((MovimientoIngreso)mov).getGuia().getId());
//				if(((MovimientoIngreso)mov).getGuia().getPagoOrigen() != null) guia.setPagoOrigen(((MovimientoIngreso)mov).getGuia().getPagoOrigen());
//				if(((MovimientoIngreso)mov).getGuia().getSaldoDestino() != null) guia.setSaldoDestino(((MovimientoIngreso)mov).getGuia().getSaldoDestino());
//				
//				if(((MovimientoIngreso)mov).getGuia().getOficinaOrigen() != null) {
//					Oficina  origen = new Oficina(); 
//					origen.setId(((MovimientoIngreso)mov).getGuia().getOficinaOrigen().getId());
//					origen.setNombre(((MovimientoIngreso)mov).getGuia().getOficinaOrigen().getNombre());
//					guia.setOficinaOrigen(origen);
//				}
//				if(((MovimientoIngreso)mov).getGuia().getOficinaDestino() != null) {
//					Oficina destino = new Oficina();
//					destino.setId(((MovimientoIngreso)mov).getGuia().getOficinaDestino().getId());
//					destino.setNombre(((MovimientoIngreso)mov).getGuia().getOficinaDestino().getNombre());
//					guia.setOficinaOrigen(destino);
//				}
//				((MovimientoIngreso)movTO).setGuia(guia);

			
			if(((MovimientoIngreso) mov).getGuia() != null) 
				movTO.setNroGuiOrConocimiento(((MovimientoIngreso) mov).getGuia().getNroGuia()+"");
			
		} else {
			
			if(((MovimientoEgreso)mov).getConocimiento() == null)                 throw new Exception("Conocimiento nula"); 
			if(((MovimientoEgreso)mov).getConocimiento().getOficinaOrigen() == null) throw new Exception("Conocimiento Origen nulo");             			
			if(((MovimientoEgreso)mov).getConocimiento().getOficinaDestino() == null) throw new Exception("Conocimiento Destino nulo");
			
			mov.setOrigen(((MovimientoEgreso)mov).getConocimiento().getOficinaOrigen().getNombre());
			mov.setDestino(((MovimientoEgreso)mov).getConocimiento().getOficinaDestino().getNombre());
			
//			if(((MovimientoEgreso)mov).getConocimiento() != null){
//				Conocimiento con = new Conocimiento();
//				con.setId(((MovimientoEgreso)mov).getConocimiento().getId());
//				if(((MovimientoEgreso)mov).getConocimiento().getPagoOrigen() != null) con.setPagoOrigen(((MovimientoEgreso)mov).getConocimiento().getPagoOrigen());
//				if(((MovimientoEgreso)mov).getConocimiento().getPagoDestino() != null) con.setPagoDestino(((MovimientoEgreso)mov).getConocimiento().getPagoDestino());
//				
//				if(((MovimientoEgreso)mov).getConocimiento().getOficinaOrigen() != null) {
//					Oficina  origen = new Oficina(); 
//					origen.setId(((MovimientoEgreso)mov).getConocimiento().getOficinaOrigen().getId());
//					origen.setNombre(((MovimientoEgreso)mov).getConocimiento().getOficinaOrigen().getNombre());
//					con.setOficinaOrigen(origen);
//				}
//				if(((MovimientoEgreso)mov).getConocimiento().getOficinaDestino() != null) {
//					Oficina destino = new Oficina();
//					destino.setId(((MovimientoEgreso)mov).getConocimiento().getOficinaDestino().getId());
//					destino.setNombre(((MovimientoEgreso)mov).getConocimiento().getOficinaDestino().getNombre());
//					con.setOficinaOrigen(destino);
//				}
//				((MovimientoEgreso)movTO).setConocimiento(con);
//			}
			if(((MovimientoEgreso) mov).getConocimiento() != null) 
				movTO.setNroGuiOrConocimiento(((MovimientoEgreso) mov).getConocimiento().getNroConocimiento()+"");
		}
		
		if(mov.getCuenta() != null) {
			Cuenta cuenta = new Cuenta();
			cuenta.setId(mov.getCuenta().getId());
			cuenta.setNroCuenta(mov.getCuenta().getNroCuenta());
			cuenta.setDescripcion(mov.getCuenta().getDescripcion());
			movTO.setCuenta(cuenta);
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

}
