package com.sinergia.dcargo.client.shared.dto;

import java.io.Serializable;
import java.util.Date;

public class DeudasReporte implements Serializable {

	private static final long serialVersionUID = 1979290065727993548L;
	
	// ID of Guia
	private Long id;
	
	private Integer nro;
	
	private String fecha;
	
	private String nroGuia;
	
	private String origen;
	
	private String destino;
	
	private String deudasClientes;
	
	private String deudasMonto;
	
	private String ingresosFecha;
	
	private String ingresosNroComprobante;
	
	private String ingresosAcuenta;
	
	private String ingresosSaldo;
	
	// Búsqueda
	private Long    idOrigenBusqueda;
	private Long    iddDestinoBusqueda;
	private Date    fechaInicioBusqueda;
	private Date    fechaDestinoBusqueda;
	private Long    idCliente;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getNroGuia() {
		return nroGuia;
	}

	public void setNroGuia(String nroGuia) {
		this.nroGuia = nroGuia;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getDeudasClientes() {
		return deudasClientes;
	}

	public void setDeudasClientes(String deudasClientes) {
		this.deudasClientes = deudasClientes;
	}

	public String getDeudasMonto() {
		return deudasMonto;
	}

	public void setDeudasMonto(String deudasMonto) {
		this.deudasMonto = deudasMonto;
	}

	public String getIngresosFecha() {
		return ingresosFecha;
	}

	public void setIngresosFecha(String ingresosFecha) {
		this.ingresosFecha = ingresosFecha;
	}

	public String getIngresosNroComprobante() {
		return ingresosNroComprobante;
	}

	public void setIngresosNroComprobante(String ingresosNroComprobante) {
		this.ingresosNroComprobante = ingresosNroComprobante;
	}

	public String getIngresosAcuenta() {
		return ingresosAcuenta;
	}

	public void setIngresosAcuenta(String ingresosAcuenta) {
		this.ingresosAcuenta = ingresosAcuenta;
	}

	public String getIngresosSaldo() {
		return ingresosSaldo;
	}

	public void setIngresosSaldo(String ingresosSaldo) {
		this.ingresosSaldo = ingresosSaldo;
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

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}
	
}
