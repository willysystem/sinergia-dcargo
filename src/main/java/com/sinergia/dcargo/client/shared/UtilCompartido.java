package com.sinergia.dcargo.client.shared;

import java.util.ArrayList;
import java.util.List;

import com.sinergia.dcargo.client.local.CuentaEgresoTO;
import com.sinergia.dcargo.client.local.CuentaIngresoTO;

public class UtilCompartido {
	
	public static List<CuentaIngresoTO> toDTOIngreso(List<CuentaIngreso> cuentas) {		
		List<CuentaIngresoTO> list = new ArrayList<>(); 
		for (CuentaIngreso cP1 : cuentas) {
			CuentaIngresoTO d = new CuentaIngresoTO();
			d.setId(cP1.getId());
			d.setNroCuenta(cP1.getNroCuenta());
			d.setDescripcion(cP1.getDescripcion());
			for (Cuenta cP2 : cP1.getSubCuentas()) {	
				CuentaIngresoTO c2 = new CuentaIngresoTO();
				c2.setId(cP2.getId());
				c2.setNroCuenta(cP2.getNroCuenta());
				c2.setDescripcion(cP2.getDescripcion());
				c2.setCuenta(d);
				d.getSubCuentas().add(c2);
			}
			list.add(d);
		}
		return list;
	}
	
	public static CuentaIngresoTO toSubCuentaIngreso(CuentaIngreso cP2){
		CuentaIngresoTO c2 = new CuentaIngresoTO();
		c2.setId(cP2.getId());
		c2.setNroCuenta(cP2.getNroCuenta());
		c2.setDescripcion(cP2.getDescripcion());
		return c2;
	}
	
	public static List<CuentaEgresoTO> toDTOEgreso(List<CuentaEgreso> cuentas) {		
		List<CuentaEgresoTO> list = new ArrayList<>(); 
		for (CuentaEgreso cP1 : cuentas) {
			CuentaEgresoTO d = new CuentaEgresoTO();
			d.setId(cP1.getId());
			d.setNroCuenta(cP1.getNroCuenta());
			d.setDescripcion(cP1.getDescripcion());
			for (Cuenta cP2 : cP1.getSubCuentas()) {	
				CuentaEgresoTO c2 = new CuentaEgresoTO();
				c2.setId(cP2.getId());
				c2.setNroCuenta(cP2.getNroCuenta());
				c2.setDescripcion(cP2.getDescripcion());
				c2.setCuenta(d);
				d.getSubCuentas().add(c2);
			}
			list.add(d);
		}
		return list;
	}
	
	
	
}
