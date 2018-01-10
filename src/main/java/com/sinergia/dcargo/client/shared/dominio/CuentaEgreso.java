package com.sinergia.dcargo.client.shared.dominio;

/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "cuenta_egreso")
public class CuentaEgreso extends Cuenta implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -1076406322L;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Oficina oficina;
	/**
	 * @generated
	 */
	@javax.persistence.OneToOne
	private Oficina oficinaEgresoaCuenta;
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

	/**
	 * @generated
	 */
	public Oficina getOficina() {
		return this.oficina;
	}

	/**
	 * @generated
	 */
	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	/**
	 * @generated
	 */
	public Oficina getOficinaEgresoaCuenta() {
		return this.oficinaEgresoaCuenta;
	}

	/**
	 * @generated
	 */
	public void setOficinaEgresoaCuenta(Oficina oficinaEgresoaCuenta) {
		this.oficinaEgresoaCuenta = oficinaEgresoaCuenta;
	}
}