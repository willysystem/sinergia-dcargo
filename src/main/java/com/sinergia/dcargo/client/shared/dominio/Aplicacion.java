package com.sinergia.dcargo.client.shared.dominio;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "aplicacion")
public class Aplicacion implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 258624068L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue
	private Long id;

	/**
	 * @generated
	 */
	private String nombre;
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "aplicacion")
	private java.util.Set<Funcion> funciones = new java.util.HashSet<Funcion>();
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "aplicacion1")
	private java.util.Set<Aplicacion> subAplicaciones = new java.util.HashSet<Aplicacion>();
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Aplicacion aplicacion1;

	/**
	 * @generated
	 */
	private String titulo;

	/**
	 * @generated
	 */
	/**
	 * @generated
	 */
	@javax.persistence.ManyToMany
	private java.util.Set<Rol> roles = new java.util.HashSet<Rol>();
	/**
	 * @generated
	 */
	public Aplicacion() {
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
		return "Aplicacion" + " id=" + id + " nombre=" + nombre + " titulo="
				+ titulo;
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
	public java.util.Set<Funcion> getFunciones() {
		return this.funciones;
	}

	/**
	 * @generated
	 */
	public void setFunciones(java.util.Set<Funcion> funciones) {
		this.funciones = funciones;
	}

	/**
	 * @generated
	 */
	public void addFunciones(Funcion funciones) {
		getFunciones().add(funciones);
		funciones.setAplicacion(this);
	}

	/**
	 * @generated
	 */
	public void removeFunciones(Funcion funciones) {
		getFunciones().remove(funciones);
		funciones.setAplicacion(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Aplicacion> getSubAplicaciones() {
		return this.subAplicaciones;
	}

	/**
	 * @generated
	 */
	public void setSubAplicaciones(java.util.Set<Aplicacion> subAplicaciones) {
		this.subAplicaciones = subAplicaciones;
	}

	/**
	 * @generated
	 */
	public void addSubAplicaciones(Aplicacion subAplicaciones) {
		getSubAplicaciones().add(subAplicaciones);
		subAplicaciones.setAplicacion1(this);
	}

	/**
	 * @generated
	 */
	public void removeSubAplicaciones(Aplicacion subAplicaciones) {
		getSubAplicaciones().remove(subAplicaciones);
		subAplicaciones.setAplicacion1(null);
	}

	/**
	 * @generated
	 */
	public Aplicacion getAplicacion1() {
		return this.aplicacion1;
	}

	/**
	 * @generated
	 */
	public void setAplicacion1(Aplicacion aplicacion1) {
		this.aplicacion1 = aplicacion1;
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

	public java.util.Set<Rol> getRoles() {
		return roles;
	}

	public void setRoles(java.util.Set<Rol> roles) {
		this.roles = roles;
	}

	
	
}