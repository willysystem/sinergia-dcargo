package com.sinergia.dcargo.client.shared.dominio;

import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @generated
 */
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@javax.persistence.Entity
@Table(name="movimiento") 
public class Movimiento implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 249894858L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue
	private Long id;
	/**
	 * @generated
	 */
	private Integer nroComprobante;
	/**
	 * @generated
	 */
	private java.util.Date fechaRegistro;

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Cuenta cuenta;

	/**
	 * @generated
	 */
	private String glosa;
	/**
	 * @generated
	 */
	private Double monto;

	/**
	 * @generated
	 */
	private Character estado;
	/**
	 * @generated
	 */
	private String estadoDescripcion;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private java.util.Date fechaRegistroIni;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private java.util.Date fechaRegistroFin;

	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private TipoCuenta tipoCuenta;

	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Integer nro;
	
	@Transient
	private String nroGuiOrConocimiento;
	
	@Transient
	public String origen;
	
	@Transient
	public String destino;

	@javax.persistence.Transient
	private Integer nroCuentaPadre;
	
	@javax.persistence.Transient
	private String descripcionPadre;
	
	@javax.persistence.Transient
	private Double pagoOrigen;
	
	@javax.persistence.Transient
	private Double pagoDestino;
	
	/**
	 * @generated
	 */
	public Movimiento() {
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
	public Integer getNroComprobante() {
		return this.nroComprobante;
	}

	/**
	 * @generated
	 */
	public void setNroComprobante(Integer nroComprobante) {
		this.nroComprobante = nroComprobante;
	}

	/**
	 * @generated
	 */
	public java.util.Date getFechaRegistro() {
		return this.fechaRegistro;
	}

	/**
	 * @generated
	 */
	public void setFechaRegistro(java.util.Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Movimiento" + " id=" + id + " nroComprobante=" + nroComprobante
				+ " fechaRegistro=" + fechaRegistro + " glosa=" + glosa
				+ " monto=" + monto + " estado=" + estado
				+ " estadoDescripcion=" + estadoDescripcion
				+ " fechaRegistroIni=" + fechaRegistroIni
				+ " fechaRegistroFin=" + fechaRegistroFin + " nro=" + nro;
	}

	/**
	 * @generated
	 */
	public Cuenta getCuenta() {
		return this.cuenta;
	}

	/**
	 * @generated
	 */
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @generated
	 */
	public String getGlosa() {
		return this.glosa;
	}

	/**
	 * @generated
	 */
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}

	/**
	 * @generated
	 */
	public Double getMonto() {
		return this.monto;
	}

	/**
	 * @generated
	 */
	public void setMonto(Double monto) {
		this.monto = monto;
	}

	/**
	 * @generated
	 */
	public Character getEstado() {
		return this.estado;
	}

	/**
	 * @generated
	 */
	public void setEstado(Character estado) {
		this.estado = estado;
	}

	/**
	 * @generated
	 */
	public String getEstadoDescripcion() {
		return this.estadoDescripcion;
	}

	/**
	 * @generated
	 */
	public void setEstadoDescripcion(String estadoDescripcion) {
		this.estadoDescripcion = estadoDescripcion;
	}

	/**
	 * @generated
	 */
	public java.util.Date getFechaRegistroIni() {
		return this.fechaRegistroIni;
	}

	/**
	 * @generated
	 */
	public void setFechaRegistroIni(java.util.Date fechaRegistroIni) {
		this.fechaRegistroIni = fechaRegistroIni;
	}

	/**
	 * @generated
	 */
	public java.util.Date getFechaRegistroFin() {
		return this.fechaRegistroFin;
	}

	/**
	 * @generated
	 */
	public void setFechaRegistroFin(java.util.Date fechaRegistroFin) {
		this.fechaRegistroFin = fechaRegistroFin;
	}

	/**
	 * @generated
	 */
	public TipoCuenta getTipoCuenta() {
		return this.tipoCuenta;
	}

	/**
	 * @generated
	 */
	public void setTipoCuenta(TipoCuenta tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
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

	public String getNroGuiOrConocimiento() {
		return nroGuiOrConocimiento;
	}

	public void setNroGuiOrConocimiento(String nroGuiOrConocimiento) {
		this.nroGuiOrConocimiento = nroGuiOrConocimiento;
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

	public Integer getNroCuentaPadre() {
		return nroCuentaPadre;
	}

	public void setNroCuentaPadre(Integer nroCuentaPadre) {
		this.nroCuentaPadre = nroCuentaPadre;
	}

	public String getDescripcionPadre() {
		return descripcionPadre;
	}

	public void setDescripcionPadre(String descripcionPadre) {
		this.descripcionPadre = descripcionPadre;
	}

	public Double getPagoOrigen() {
		return pagoOrigen;
	}

	public void setPagoOrigen(Double pagoOrigen) {
		this.pagoOrigen = pagoOrigen;
	}

	public Double getPagoDestino() {
		return pagoDestino;
	}

	public void setPagoDestino(Double pagoDestino) {
		this.pagoDestino = pagoDestino;
	}
	
	
}