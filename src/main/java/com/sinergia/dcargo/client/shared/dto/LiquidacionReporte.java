package com.sinergia.dcargo.client.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class LiquidacionReporte implements Serializable {

	private static final long serialVersionUID = 1979290065727993548L;
	
	// ID of Guia
	private Long id;
	
	private Integer nro;
	
	private String fecha;
	
	private String nroConocimiento;
	
	private String nroGuia;
	
	private String cobroOrigen;
	
	private String cobroDestino;
	
	private String fleteDestino;
	
	private Long    idOrigenBusqueda;
	private Long    iddDestinoBusqueda;
	private Date    fechaInicioBusqueda;
	private Date    fechaDestinoBusqueda;
	private Integer porcentajeDestinoBusqueda;

	public Integer getNro() {
		return nro;
	}

	public void setNro(Integer nro) {
		this.nro = nro;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getNroConocimiento() {
		return nroConocimiento;
	}

	public void setNroConocimiento(String nroConocimiento) {
		this.nroConocimiento = nroConocimiento;
	}

	public String getNroGuia() {
		return nroGuia;
	}

	public void setNroGuia(String nroGuia) {
		this.nroGuia = nroGuia;
	}

	public String getCobroOrigen() {
		return cobroOrigen;
	}

	public void setCobroOrigen(String cobroOrigen) {
		this.cobroOrigen = cobroOrigen;
	}

	public String getCobroDestino() {
		return cobroDestino;
	}

	public void setCobroDestino(String cobroDestino) {
		this.cobroDestino = cobroDestino;
	}

	public String getFleteDestino() {
		return fleteDestino;
	}

	public void setFleteDestino(String fleteDestino) {
		this.fleteDestino = fleteDestino;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdOrigenBusqueda() {
		return idOrigenBusqueda;
	}

	public void setIdOrigenBusqueda(Long idOrigenBusqueda) {
		this.idOrigenBusqueda = idOrigenBusqueda;
	}

	public Long getIddDestinoBusqueda() {
		return iddDestinoBusqueda;
	}

	public void setIddDestinoBusqueda(Long iddDestinoBusqueda) {
		this.iddDestinoBusqueda = iddDestinoBusqueda;
	}

	public Date getFechaInicioBusqueda() {
		return fechaInicioBusqueda;
	}

	public void setFechaInicioBusqueda(Date fechaInicioBusqueda) {
		this.fechaInicioBusqueda = fechaInicioBusqueda;
	}

	public Date getFechaDestinoBusqueda() {
		return fechaDestinoBusqueda;
	}

	public void setFechaDestinoBusqueda(Date fechaDestinoBusqueda) {
		this.fechaDestinoBusqueda = fechaDestinoBusqueda;
	}

	public Integer getPorcentajeDestinoBusqueda() {
		return porcentajeDestinoBusqueda;
	}

	public void setPorcentajeDestinoBusqueda(Integer porcentajeDestinoBusqueda) {
		this.porcentajeDestinoBusqueda = porcentajeDestinoBusqueda;
	}

	@Override
	public String toString() {
		return "LiquidacionReporte [id=" + id + ", nro=" + nro + ", fecha=" + fecha + ", nroConocimiento="
				+ nroConocimiento + ", nroGuia=" + nroGuia + ", cobroOrigen=" + cobroOrigen + ", cobroDestino="
				+ cobroDestino + ", fleteDestino=" + fleteDestino + ", idOrigenBusqueda=" + idOrigenBusqueda
				+ ", iddDestinoBusqueda=" + iddDestinoBusqueda + ", fechaInicioBusqueda=" + fechaInicioBusqueda
				+ ", fechaDestinoBusqueda=" + fechaDestinoBusqueda + ", porcentajeDestinoBusqueda="
				+ porcentajeDestinoBusqueda + "]";
	}
	
}
