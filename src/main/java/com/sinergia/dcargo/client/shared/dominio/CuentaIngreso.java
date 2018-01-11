package com.sinergia.dcargo.client.shared.dominio;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "cuenta_ingreso")
public class CuentaIngreso extends Cuenta implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 436809204L;
	/**
	 * @generated
	 */
	@javax.persistence.OneToOne
	private Oficina oficinaIngreso;
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

	public Oficina getOficinaIngreso() {
		return oficinaIngreso;
	}

	public void setOficinaIngreso(Oficina oficinaIngreso) {
		this.oficinaIngreso = oficinaIngreso;
	}


}