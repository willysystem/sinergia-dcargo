package com.sinergia.dcargo.client.shared.dominio;

import javax.persistence.Table;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @generated
 */
@Table(name="movimiento_egreso")
@AuditTable(value="movimiento_egreso_aud")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@javax.persistence.Entity
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
	@JsonIgnore
	private Conocimiento conocimiento;

	/**
	 * @generated
	 */
	@javax.persistence.OneToOne(mappedBy = "movimientoEgresoAcuenta")
	@JsonIgnore
	private Conocimiento conocimientoAcuenta;

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

	/**
	 * @generated
	 */
	public Conocimiento getConocimientoAcuenta() {
		return this.conocimientoAcuenta;
	}

	/**
	 * @generated
	 */
	public void setConocimientoAcuenta(Conocimiento conocimientoAcuenta) {
		this.conocimientoAcuenta = conocimientoAcuenta;
	}
}