package com.sinergia.dcargo.client.shared;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name="cuenta_ingreso")
//@JsonIdentityInfo(
//		  generator = ObjectIdGenerators.PropertyGenerator.class, 
//		  property = "id")
public class CuentaIngreso extends Cuenta implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 436809204L;
	/**
	 * @generated
	 */
	public CuentaIngreso() {
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "CuentaIngreso";
	}
}