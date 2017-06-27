package com.sinergia.dcargo.client.shared;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "persona")
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class Persona implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -196851561L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue
	private Long id;
	/**
	 * @generated
	 */
	private String apellidos;
	/**
	 * @generated
	 */
	private String nombres;
	/**
	 * @generated
	 */
	private Integer nro;

	/**
	 * @generated
	 */
	public Persona() {
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
	public String getApellidos() {
		return this.apellidos;
	}

	/**
	 * @generated
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @generated
	 */
	public String getNombres() {
		return this.nombres;
	}

	/**
	 * @generated
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	/**
	 * @generated
	 */
	public Integer getNro() {
		return this.nro;
	}

	/**
	 * @generated
	 */
	public void setNro(Integer nro) {
		this.nro = nro;
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Persona" + " id=" + id + " apellidos=" + apellidos
				+ " nombres=" + nombres + " nro=" + nro;
	}
}