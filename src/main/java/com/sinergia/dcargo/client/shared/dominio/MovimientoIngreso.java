package com.sinergia.dcargo.client.shared.dominio;

import javax.persistence.Table;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * @generated
 */
@Table(name="movimiento_ingreso")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@AuditTable(value="movimiento_ingreso_aud")
@javax.persistence.Entity
public class MovimientoIngreso extends Movimiento implements
		java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1600994183L;

	/**
	 * @generated
	 */
	@JsonIgnore
	@javax.persistence.OneToOne(mappedBy = "movimientoIngresoOrigen")
	private Guia guiaPagoOrigen;

	/**
	 * @generated
	 */
	@JsonIgnore
	@javax.persistence.OneToOne(mappedBy = "movimientoIngresoDestino")
	private Guia guiaPagoDestino;

	/**
	 * @generated
	 */
	@JsonIgnore
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
	public Guia getGuiaPagoOrigen() {
		return this.guiaPagoOrigen;
	}

	/**
	 * @generated
	 */
	public void setGuiaPagoOrigen(Guia guiaPagoOrigen) {
		this.guiaPagoOrigen = guiaPagoOrigen;
	}

	/**
	 * @generated
	 */
	public Guia getGuiaPagoDestino() {
		return this.guiaPagoDestino;
	}

	/**
	 * @generated
	 */
	public void setGuiaPagoDestino(Guia guiaPagoDestino) {
		this.guiaPagoDestino = guiaPagoDestino;
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