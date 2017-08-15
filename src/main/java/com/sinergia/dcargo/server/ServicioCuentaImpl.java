package com.sinergia.dcargo.server;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.Cuenta;
import com.sinergia.dcargo.client.shared.CuentaEgreso;
import com.sinergia.dcargo.client.shared.CuentaIngreso;
import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.client.shared.ServicioCuenta;
import com.sinergia.dcargo.client.shared.TipoCuenta;
import com.sinergia.dcargo.server.dao.Dao;

/**
 * 
 * @author willy
 */
@Stateless
public class ServicioCuentaImpl extends Dao<Cuenta> implements ServicioCuenta {
	
	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	public ServicioCuentaImpl() {
		super(Cuenta.class);
	}

	public List<? extends Cuenta> getTodasCuentas(TipoCuenta tipoCuenta) throws Exception{
	     String tipo2 = tipoCuenta == TipoCuenta.INGRESO ? "CuentaIngreso" : "CuentaEgreso";
	     String sql = "SELECT c FROM " + tipo2 + " c WHERE c.cuenta is null";
	     Query q = this.em.createQuery(sql);
	     List<Cuenta> cuentasP = q.getResultList();
	     ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>();
	     for (Cuenta cP1 : cuentasP) {
	         Cuenta c = tipoCuenta == TipoCuenta.INGRESO ? new CuentaIngreso() : new CuentaEgreso();
	         c.setId(cP1.getId());
	         c.setNroCuenta(cP1.getNroCuenta());
	         c.setDescripcion(cP1.getDescripcion());
//	         for (Cuenta cP2 : cP1.getSubCuentas()) {
//	             Cuenta c2 = tipoCuenta == TipoCuenta.INGRESO ? new CuentaIngreso() : new CuentaEgreso();
//	             c2.setId(cP2.getId());
//	             c2.setNroCuenta(cP2.getNroCuenta());
//	             c2.setDescripcion(cP2.getDescripcion());
//	             c.getSubCuentas().add(c2);
//	         }
	         cuentas.add(c);
	     }
	     return cuentas;
	}
	
	@SuppressWarnings("unchecked")
	public List<CuentaIngreso> getTodasCuentasIngreso() throws Exception {
//		List<CuentaIngreso> cis = new ArrayList<>();
//		CuentaIngreso c1 = new CuentaIngreso();
//		cis.add(c1);
//		CuentaIngreso c2 = new CuentaIngreso();
//		cis.add(c2);
//		return cis;
		
		
		return (List<CuentaIngreso>) getTodasCuentas(TipoCuenta.INGRESO);
	}
	
	@SuppressWarnings("unchecked")
	public List<CuentaEgreso> getTodasCuentasEgreso() throws Exception {
		return (List<CuentaEgreso>)getTodasCuentas(TipoCuenta.EGRESO);
	}

	@Override
	public CuentaEgreso nuevaCuentaEgreso() throws Exception {
		CuentaEgreso cuentaDTO = new CuentaEgreso();
		CuentaEgreso cuentaP = em.merge(new CuentaEgreso());
		cuentaDTO.setId(cuentaP.getId());
		return cuentaDTO;
	}
	
	@Override
	public void guardarNroCuenta(Long cuentaId, Integer nroCuenta, TipoCuenta tipoCuenta) throws Exception {
		Cuenta cuenta = null;
		if(TipoCuenta.INGRESO == tipoCuenta)
			cuenta = em.find(CuentaIngreso.class, cuentaId);
		if(TipoCuenta.EGRESO == tipoCuenta)
			cuenta = em.find(CuentaEgreso.class, cuentaId);
		cuenta.setNroCuenta(nroCuenta);
		em.merge(cuenta);
	}
	
	@Override
	public void guardarDescripcion(Long cuentaId, String descripcion, TipoCuenta tipoCuenta) throws Exception {
		Cuenta cuenta = null;
		if(TipoCuenta.INGRESO == tipoCuenta)
			cuenta = em.find(CuentaIngreso.class, cuentaId);
		if(TipoCuenta.EGRESO == tipoCuenta)
			cuenta = em.find(CuentaEgreso.class, cuentaId);
		cuenta.setDescripcion(descripcion);
		em.merge(cuenta);
		
	}

	@Override
	public CuentaIngreso nuevoCuentaIngreso() throws Exception {
		CuentaIngreso cuentaDTO = new CuentaIngreso();
		CuentaIngreso cuentaP = em.merge(new CuentaIngreso());
		cuentaDTO.setId(cuentaP.getId());
		return cuentaDTO;
	}

	@Override
	public void borrarCuenta(Long cuentaId) throws Exception {
		Cuenta cP = em.find(Cuenta.class, cuentaId);
		if(cP.getSubCuentas().isEmpty()){
			cP.setCuenta(null);
		} else {
			
		}
		
		em.remove(cP);
	}

	@Override
	public Resultado esUnicoNroCuentaCon(TipoCuenta type, Integer nroCuenta) throws Exception {
		if(type == TipoCuenta.INGRESO) {
			return esUnicoNroCuentaIngreso(nroCuenta, getTodasCuentasIngreso());
		}
		if(type == TipoCuenta.EGRESO) {
			return esUnicoNroCuentaIngreso(nroCuenta, getTodasCuentas(TipoCuenta.EGRESO));
		}
		return null;
	}
	
	public Resultado esUnicoNroCuentaIngreso(Integer nroCuenta, List<? extends Cuenta> cuentas) throws Exception {
		Resultado resultado = new Resultado();
		for (Cuenta cP: cuentas) {
			if(cP.getNroCuenta() != null){
				if (cP.getNroCuenta() == nroCuenta) {
					resultado.setVariableBoolean(false);
					return resultado;
				}
			}
		}
		resultado.setVariableBoolean(true);
		return resultado;
	}

	@Override
	public void guardarCuentaPadre(Long cuentaId, Long idPadre, TipoCuenta tipoCuenta) throws Exception {
		// Hijo
		Cuenta cuenta = null;
		if(TipoCuenta.INGRESO == tipoCuenta)
			cuenta = em.find(CuentaIngreso.class, cuentaId);
		if(TipoCuenta.EGRESO == tipoCuenta)
			cuenta = em.find(CuentaEgreso.class, cuentaId);
		
		// Padre
		Cuenta cuentaPadre = null;
		if(idPadre != null){
			if(TipoCuenta.INGRESO == tipoCuenta)
				cuentaPadre = em.find(CuentaIngreso.class, idPadre);
			if(TipoCuenta.EGRESO == tipoCuenta)
				cuentaPadre = em.find(CuentaEgreso.class, idPadre);
			cuenta.setCuenta(cuentaPadre);
		} else {
			cuenta.setCuenta(null);
		}
		em.merge(cuenta);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CuentaIngreso> getSubCuentasIngreso(Long cuentaIngresoId) throws Exception {
		Cuenta cuenta = em.find(CuentaIngreso.class, cuentaIngresoId);
		return (List<CuentaIngreso>) getSubCuentas(cuenta, TipoCuenta.INGRESO);
	}
	
	public List<? extends Cuenta> getSubCuentas(Cuenta cuenta, TipoCuenta tipoCuenta) throws Exception {
		List<Cuenta> subCuentas = new ArrayList<Cuenta>();
        for (Cuenta cP2 : cuenta.getSubCuentas()) {
        	Cuenta c2 = tipoCuenta == TipoCuenta.INGRESO ? new CuentaIngreso() : new CuentaEgreso();
        	c2.setId(cP2.getId());
        	c2.setNroCuenta(cP2.getNroCuenta());
        	c2.setDescripcion(cP2.getDescripcion());
        	subCuentas.add(c2);
        }
		return subCuentas;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CuentaEgreso> getSubCuentasEgreso(Long cuentaEgresoId) throws Exception {
		Cuenta cuenta = em.find(CuentaEgreso.class, cuentaEgresoId);
		return (List<CuentaEgreso>) getSubCuentas(cuenta, TipoCuenta.EGRESO);
	}
	

//	@Override
//	public List<CuentaIngreso> getTodasCuentasIngresoPadre() throws Exception {
//		String q = "SELECT e FROM CuentaIngreso e WHERE e.cuenta <> null";
//		Query query = em.createQuery(q);
//		@SuppressWarnings("unchecked")
//		List<CuentaIngreso> cuentas = query.getResultList();
//		List<CuentaIngreso> cuentasDTO = new ArrayList<>(); 
//		for (CuentaIngreso cI : cuentas) {
//			CuentaIngreso cDTO = new CuentaIngreso();
//			cDTO.setId(cI.getId());
//			cDTO.setNro(cI.getNro());
//			cDTO.setDescripcion(cI.getDescripcion());
//			cuentasDTO.add(cDTO);
//		}
//		return cuentasDTO;
//	}
//
//	@Override
//	public List<CuentaEgreso> getTodasCuentasEgresoPadre() throws Exception {
//		String q = "SELECT e FROM CuentaEgreso e WHERE e.cuenta <> null";
//		Query query = em.createQuery(q);
//		@SuppressWarnings("unchecked")
//		List<CuentaEgreso> cuentas = query.getResultList();
//		List<CuentaEgreso> cuentasDTO = new ArrayList<>(); 
//		for (CuentaEgreso cI : cuentas) {
//			CuentaEgreso cDTO = new CuentaEgreso();
//			cDTO.setId(cI.getId());
//			cDTO.setNro(cI.getNro());
//			cDTO.setDescripcion(cI.getDescripcion());
//			cuentasDTO.add(cDTO);
//		}
//		return cuentasDTO;
//	}

}
