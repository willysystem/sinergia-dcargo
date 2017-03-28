package com.sinergia.dcargo.client.shared;

import java.io.Serializable;

import javax.persistence.Entity;

import org.jboss.errai.common.client.api.annotations.Portable;

@Entity
@Portable
public class Persona implements Serializable {

	
	private static final long serialVersionUID = -8704027295195729641L;
	private String nombre;
	
	private Long id;

	public Persona(){
		
	}
	
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
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
	
}

