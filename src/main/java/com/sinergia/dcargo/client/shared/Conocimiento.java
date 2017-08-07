package com.sinergia.dcargo.client.shared;

import javax.persistence.TemporalType;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "conocimiento")
public class Conocimiento implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1927983240L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "conocimiento")
	private java.util.Set<Guia> guias = new java.util.HashSet<Guia>();
	/**
	 * @generated
	 */
	private Integer nroConocimiento;
	/**
	 * @generated
	 */
	private java.util.Date fecha;
	/**
	 * @generated
	 */
	private Double multa;
	/**
	 * @generated
	 */
	private Integer dias;
	/**
	 * @generated
	 */
	private String observacion;
	/**
	 * @generated
	 */
	private String adjunto;
	/**
	 * @generated
	 */
	private Character estado;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Oficina oficinaOrigen;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Oficina oficinaDestino;
	/**
	 * @generated
	 */
	private Double flete;
	/**
	 * @generated
	 */
	private Double acuenta;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Usuario usuario;
	/**
	 * @generated
	 */
	@javax.persistence.Temporal(value = TemporalType.TIMESTAMP)
	private java.util.Date fechaRegistro;
	/**
	 * @generated
	 */
	private String adjunto2;
	/**
	 * @generated
	 */
	private String aclaracion;
	/**
	 * @generated
	 */
	private String aclaracion2;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Integer nro;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private String estadoDescripcion;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private java.util.Date fechaIni;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private java.util.Date fechaFin;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Transportista transportistaPropietario;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Transportista transportistaConductor;

	/**
	 * @generated
	 */
	private Double pagoOrigen;
	/**
	 * @generated
	 */
	private Double pagoDestino;

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "conocimiento")
	private java.util.Set<MovimientoEgreso> movimientosEgreso = new java.util.HashSet<MovimientoEgreso>();

	/**
	 * @generated
	 */
	public Conocimiento() {
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
	public java.util.Set<Guia> getGuias() {
		return this.guias;
	}

	/**
	 * @generated
	 */
	public void setGuias(java.util.Set<Guia> guias) {
		this.guias = guias;
	}

	/**
	 * @generated
	 */
	public void addGuias(Guia guias) {
		getGuias().add(guias);
		guias.setConocimiento(this);
	}

	/**
	 * @generated
	 */
	public void removeGuias(Guia guias) {
		getGuias().remove(guias);
		guias.setConocimiento(null);
	}

	/**
	 * @generated
	 */
	public Integer getNroConocimiento() {
		return this.nroConocimiento;
	}

	/**
	 * @generated
	 */
	public void setNroConocimiento(Integer nroConocimiento) {
		this.nroConocimiento = nroConocimiento;
	}

	/**
	 * @generated
	 */
	public java.util.Date getFecha() {
		return this.fecha;
	}

	/**
	 * @generated
	 */
	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * @generated
	 */
	public Double getMulta() {
		return this.multa;
	}

	/**
	 * @generated
	 */
	public void setMulta(Double multa) {
		this.multa = multa;
	}

	/**
	 * @generated
	 */
	public Integer getDias() {
		return this.dias;
	}

	/**
	 * @generated
	 */
	public void setDias(Integer dias) {
		this.dias = dias;
	}

	/**
	 * @generated
	 */
	public String getObservacion() {
		return this.observacion;
	}

	/**
	 * @generated
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * @generated
	 */
	public String getAdjunto() {
		return this.adjunto;
	}

	/**
	 * @generated
	 */
	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
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
	public Oficina getOficinaOrigen() {
		return this.oficinaOrigen;
	}

	/**
	 * @generated
	 */
	public void setOficinaOrigen(Oficina oficinaOrigen) {
		this.oficinaOrigen = oficinaOrigen;
	}

	/**
	 * @generated
	 */
	public Oficina getOficinaDestino() {
		return this.oficinaDestino;
	}

	/**
	 * @generated
	 */
	public void setOficinaDestino(Oficina oficinaDestino) {
		this.oficinaDestino = oficinaDestino;
	}

	/**
	 * @generated
	 */
	public Double getFlete() {
		return this.flete;
	}

	/**
	 * @generated
	 */
	public void setFlete(Double flete) {
		this.flete = flete;
	}

	/**
	 * @generated
	 */
	public Double getAcuenta() {
		return this.acuenta;
	}

	/**
	 * @generated
	 */
	public void setAcuenta(Double acuenta) {
		this.acuenta = acuenta;
	}

	/**
	 * @generated
	 */
	public Usuario getUsuario() {
		return this.usuario;
	}

	/**
	 * @generated
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
	public String getAdjunto2() {
		return this.adjunto2;
	}

	/**
	 * @generated
	 */
	public void setAdjunto2(String adjunto2) {
		this.adjunto2 = adjunto2;
	}

	/**
	 * @generated
	 */
	public String getAclaracion() {
		return this.aclaracion;
	}

	/**
	 * @generated
	 */
	public void setAclaracion(String aclaracion) {
		this.aclaracion = aclaracion;
	}

	/**
	 * @generated
	 */
	public String getAclaracion2() {
		return this.aclaracion2;
	}

	/**
	 * @generated
	 */
	public void setAclaracion2(String aclaracion2) {
		this.aclaracion2 = aclaracion2;
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
	public java.util.Date getFechaIni() {
		return this.fechaIni;
	}

	/**
	 * @generated
	 */
	public void setFechaIni(java.util.Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	/**
	 * @generated
	 */
	public java.util.Date getFechaFin() {
		return this.fechaFin;
	}

	/**
	 * @generated
	 */
	public void setFechaFin(java.util.Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @generated
	 */
	public Transportista getTransportistaPropietario() {
		return this.transportistaPropietario;
	}

	/**
	 * @generated
	 */
	public void setTransportistaPropietario(
			Transportista transportistaPropietario) {
				this.transportistaPropietario = transportistaPropietario;
			}

	/**
	 * @generated
	 */
	public Transportista getTransportistaConductor() {
		return this.transportistaConductor;
	}

	/**
	 * @generated
	 */
	public void setTransportistaConductor(Transportista transportistaConductor) {
		this.transportistaConductor = transportistaConductor;
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Conocimiento" + " id=" + id + " nroConocimiento="
				+ nroConocimiento + " fecha=" + fecha + " multa=" + multa
				+ " dias=" + dias + " observacion=" + observacion + " adjunto="
				+ adjunto + " estado=" + estado + " flete=" + flete
				+ " acuenta=" + acuenta + " pagoOrigen=" + pagoOrigen
				+ " pagoDestino=" + pagoDestino + " fechaRegistro="
				+ fechaRegistro + " adjunto2=" + adjunto2 + " aclaracion="
				+ aclaracion + " aclaracion2=" + aclaracion2 + " nro=" + nro
				+ " estadoDescripcion=" + estadoDescripcion + " fechaIni="
				+ fechaIni + " fechaFin=" + fechaFin;
	}

	/**
	 * @generated
	 */
	public Double getPagoOrigen() {
		return this.pagoOrigen;
	}

	/**
	 * @generated
	 */
	public void setPagoOrigen(Double pagoOrigen) {
		this.pagoOrigen = pagoOrigen;
	}

	/**
	 * @generated
	 */
	public Double getPagoDestino() {
		return this.pagoDestino;
	}

	/**
	 * @generated
	 */
	public void setPagoDestino(Double pagoDestino) {
		this.pagoDestino = pagoDestino;
	}

	/**
	 * @generated
	 */
	public java.util.Set<MovimientoEgreso> getMovimientosEgreso() {
		return this.movimientosEgreso;
	}

	/**
	 * @generated
	 */
	public void setMovimientosEgreso(
			java.util.Set<MovimientoEgreso> movimientosEgreso) {
		this.movimientosEgreso = movimientosEgreso;
	}

	/**
	 * @generated
	 */
	public void addMovimientosEgreso(MovimientoEgreso movimientosEgreso) {
		getMovimientosEgreso().add(movimientosEgreso);
		movimientosEgreso.setConocimiento(this);
	}

	/**
	 * @generated
	 */
	public void removeMovimientosEgreso(MovimientoEgreso movimientosEgreso) {
		getMovimientosEgreso().remove(movimientosEgreso);
		movimientosEgreso.setConocimiento(null);
	}
}