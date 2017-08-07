package com.sinergia.dcargo.client.shared;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name="cuenta_egreso")
//@JsonIdentityInfo(
//		  generator = ObjectIdGenerators.PropertyGenerator.class, 
//		  property = "id")
public class CuentaEgreso extends Cuenta implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -1076406322L;
	/**
	 * @generated
	 */
	public CuentaEgreso() {
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "CuentaEgreso";
	}
}