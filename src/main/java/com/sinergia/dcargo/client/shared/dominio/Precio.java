package com.sinergia.dcargo.client.shared.dominio;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "precio")
public class Precio implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -548936453L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;
	/**
	 * @generated
	 */
	private String descripcion;
	/**
	 * @generated
	 */
	private Double precio;
	/**
	 * @generated
	 */
	public Precio() {
	}

	/**
	 * @generated
	 */
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
	public Double getPrecio() {
		return this.precio;
	}

	/**
	 * @generated
	 */
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Precio" + " id=" + id + " descripcion=" + descripcion
				+ " precio=" + precio;
	}
}