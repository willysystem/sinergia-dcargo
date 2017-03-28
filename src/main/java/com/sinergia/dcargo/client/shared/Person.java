package com.sinergia.dcargo.client.shared;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

import org.jboss.errai.common.client.api.annotations.Portable;


/**
 * @generated
 */
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@javax.persistence.Entity
@Portable
public class Person implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -1392364119L;
	/**
	 * @generated
	 */
	private Long id;

	/**
	 * @generated
	 */
	private String surnames;
	/**
	 * @generated
	 */
	private String names;

	/**
	 * @generated
	 */
	private Integer nro;

	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue
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
		return "Person" + " id=" + id + " surnames=" + surnames + " names="
				+ names + " nro=" + nro;
	}

	/**
	 * @generated
	 */
	public Person() {
	}

	/**
	 * @generated
	 */
	public String getSurnames() {
		return this.surnames;
	}

	/**
	 * @generated
	 */
	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	/**
	 * @generated
	 */
	public String getNames() {
		return this.names;
	}

	/**
	 * @generated
	 */
	public void setNames(String names) {
		this.names = names;
	}

	/**
	 * @generated
	 */
	@Transient
	public Integer getNro() {
		return this.nro;
	}

	/**
	 * @generated
	 */
	public void setNro(Integer nro) {
		this.nro = nro;
	}
}