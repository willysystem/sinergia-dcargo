package com.sinergia.dcargo.client.shared.dominio;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;
import org.hibernate.envers.RevisionEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name="movimiento_ingreso")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
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

	@JsonIgnore
	@javax.persistence.OneToOne(mappedBy = "movimientoIngresoDestino")
	private Guia guiaPagoDestino;

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

	public Guia getGuiaPagoOrigen() {
		return guiaPagoOrigen;
	}

	public void setGuiaPagoOrigen(Guia guiaPagoOrigen) {
		this.guiaPagoOrigen = guiaPagoOrigen;
	}

	public Guia getGuiaPagoDestino() {
		return guiaPagoDestino;
	}

	public void setGuiaPagoDestino(Guia guiaPagoDestino) {
		this.guiaPagoDestino = guiaPagoDestino;
	}

	
	
}