package com.sinergia.dcargo.client.shared;

import javax.persistence.GenerationType;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "tipopago")
public class TipoPago implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 62002274L;
	/**
	 * @generated
	 */
	private Long id;

	/**
	 * @generated
	 */
	private String descripcion;

	/**
	 * @generated
	 */
	private java.util.Set<Guia> guias = new java.util.HashSet<Guia>();

	/**
	 * @generated
	 */
	public TipoPago() {
	}

	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	/**
	 * @generated
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "TipoPago" + " id=" + id + " descripcion=" + descripcion;
	}

	/**
	 * @generated
	 */
	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * @generated
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "tipoPago")
	public java.util.Set<Guia> getGuias() {
		return this.guias;
	}

	/**
	 * @generated
	 */
	public void setGuias(java.util.Set<Guia> guias) {
		this.guias = guias;
	}

	/**
	 * @generated
	 */
	public void addGuias(Guia guias) {
		getGuias().add(guias);
		guias.setTipoPago(this);
	}

	/**
	 * @generated
	 */
	public void removeGuias(Guia guias) {
		getGuias().remove(guias);
		guias.setTipoPago(null);
	}
}