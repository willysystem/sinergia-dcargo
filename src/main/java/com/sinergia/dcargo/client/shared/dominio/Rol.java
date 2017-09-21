package com.sinergia.dcargo.client.shared.dominio;

import javax.persistence.Table;

/**
 * @generated
 */
@javax.persistence.Entity
@Table(name="rol")
public class Rol implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -1256890310L;
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
	@javax.persistence.OneToMany(mappedBy = "rol")
	private java.util.Set<Usuario> usuarios = new java.util.HashSet<Usuario>();
	
	/**
	 * @generated
	 */
	@javax.persistence.ManyToMany(mappedBy = "roles")
	private java.util.Set<Aplicacion> aplicaciones = new java.util.HashSet<Aplicacion>();

	/**
	 * @generated
	 */
	public Rol() {
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Rol" + " id=" + id + " nombre=" + nombre;
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
	public java.util.Set<Usuario> getUsuarios() {
		return this.usuarios;
	}

	/**
	 * @generated
	 */
	public void setUsuarios(java.util.Set<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * @generated
	 */
	public void addUsuarios(Usuario usuarios) {
		getUsuarios().add(usuarios);
		usuarios.setRol(this);
	}

	/**
	 * @generated
	 */
	public void removeUsuarios(Usuario usuarios) {
		getUsuarios().remove(usuarios);
		usuarios.setRol(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Aplicacion> getAplicaciones() {
		return this.aplicaciones;
	}

	/**
	 * @generated
	 */
	public void setAplicaciones(java.util.Set<Aplicacion> aplicaciones) {
		this.aplicaciones = aplicaciones;
	}

	
}