package com.sinergia.dcargo.client.shared;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name="movimiento_ingreso")
public class MovimientoIngreso extends Movimiento implements
		java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1600994183L;

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Guia guia;

	/**
	 * @generated
	 */
	public MovimientoIngreso() {
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "MovimientoIngreso";
	}

	/**
	 * @generated
	 */
	public Guia getGuia() {
		return this.guia;
	}

	/**
	 * @generated
	 */
	public void setGuia(Guia guia) {
		this.guia = guia;
	}
}