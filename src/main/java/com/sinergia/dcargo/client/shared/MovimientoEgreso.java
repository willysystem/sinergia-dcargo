package com.sinergia.dcargo.client.shared;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name="movimiento_egreso")
public class MovimientoEgreso extends Movimiento implements
		java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 2009189339L;

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Conocimiento conocimiento;

	/**
	 * @generated
	 */
	public MovimientoEgreso() {
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "MovimientoEgreso";
	}

	/**
	 * @generated
	 */
	public Conocimiento getConocimiento() {
		return this.conocimiento;
	}

	/**
	 * @generated
	 */
	public void setConocimiento(Conocimiento conocimiento) {
		this.conocimiento = conocimiento;
	}
}