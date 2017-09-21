package com.sinergia.dcargo.client.shared.dominio;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "funcion")
public class Funcion implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -28063825L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue
	private Long id;

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Aplicacion aplicacion;
	/**
	 * @generated
	 */
	private String nombre;
	/**
	 * @generated
	 */
	private String titulo;

	/**
	 * @generated
	 */
	public Funcion() {
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
	public String toString() {
		return "Funcion" + " id=" + id + " nombre=" + nombre + " titulo="
				+ titulo;
	}

	/**
	 * @generated
	 */
	public Aplicacion getAplicacion() {
		return this.aplicacion;
	}

	/**
	 * @generated
	 */
	public void setAplicacion(Aplicacion aplicacion) {
		this.aplicacion = aplicacion;
	}

	/**
	 * @generated
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * @generated
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @generated
	 */
	public String getTitulo() {
		return this.titulo;
	}

	/**
	 * @generated
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
}