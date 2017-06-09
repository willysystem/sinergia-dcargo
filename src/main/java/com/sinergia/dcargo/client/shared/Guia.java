package com.sinergia.dcargo.client.shared;

import javax.persistence.CascadeType;
import javax.persistence.GenerationType;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "guia")
public class Guia implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -309215877L;
	/**
	 * @generated
	 */
	private Long id;
	/**
	 * @generated
	 */
	private Integer nroGuia;

	/**
	 * @generated
	 */
	private Double totalPeso;
	/**
	 * @generated
	 */
	private Integer totalCantidad;
	/**
	 * @generated
	 */
	private String adjunto;

	/**
	 * @generated
	 */
	private String nroFactura;
	/**
	 * @generated
	 */
	private java.util.Date fechaRegistro;

	/**
	 * @generated
	 */
	private Double totalMinimo;
	/**
	 * @generated
	 */
	private java.util.Date fechaEntrega;
	/**
	 * @generated
	 */
	private String ciEntrega;

	/**
	 * @generated
	 */
	private Conocimiento conocimiento;
	/**
	 * @generated
	 */
	private TipoPago tipoPago;

	/**
	 * @generated
	 */
	private String novedadEntrega;
	/**
	 * @generated
	 */
	private String resumenContenido;
	/**
	 * @generated
	 */
	private Double saldoDestino;
	/**
	 * @generated
	 */
	private Double pagoOrigen;

	/**
	 * @generated
	 */
	private java.util.Date fecha;

	/**
	 * @generated
	 */
	private Double totalGuia;

	/**
	 * @generated
	 */
	private Boolean activo;

	/**
	 * @generated
	 */
	private String notaEntrega;

	/**
	 * @generated
	 */
	private Integer nro;

	/**
	 * @generated
	 */
	private Cliente remitente;
	/**
	 * @generated
	 */
	private Cliente consignatario;

	/**
	 * @generated
	 */
	private Usuario usuarioRegistro;
	/**
	 * @generated
	 */
	private Usuario usuarioEntrega;

	/**
	 * @generated
	 */
	private Oficina oficinaOrigen;

	/**
	 * @generated
	 */
	private Oficina oficinaDestino;

	/**
	 * @generated
	 */
	private java.util.Set<Item> items = new java.util.HashSet<Item>();

	/**
	 * @generated
	 */
	public Guia() {
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Guia" + " id=" + id + " nroGuia=" + nroGuia + " totalPeso="
				+ totalPeso + " totalCantidad=" + totalCantidad + " adjunto="
				+ adjunto + " activo=" + activo + " nroFactura=" + nroFactura
				+ " fechaRegistro=" + fechaRegistro + " totalMinimo="
				+ totalMinimo + " fechaEntrega=" + fechaEntrega + " ciEntrega="
				+ ciEntrega + " novedadEntrega=" + novedadEntrega
				+ " resumenContenido=" + resumenContenido + " saldoDestino="
				+ saldoDestino + " pagoOrigen=" + pagoOrigen + " fecha="
				+ fecha + " totalGuia=" + totalGuia + " notaEntrega="
				+ notaEntrega + " nro=" + nro;
	}

	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
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
	public Integer getNroGuia() {
		return this.nroGuia;
	}

	/**
	 * @generated
	 */
	public void setNroGuia(Integer nroGuia) {
		this.nroGuia = nroGuia;
	}

	/**
	 * @generated
	 */
	public Double getTotalPeso() {
		return this.totalPeso;
	}

	/**
	 * @generated
	 */
	public void setTotalPeso(Double totalPeso) {
		this.totalPeso = totalPeso;
	}

	/**
	 * @generated
	 */
	public Integer getTotalCantidad() {
		return this.totalCantidad;
	}

	/**
	 * @generated
	 */
	public void setTotalCantidad(Integer totalCantidad) {
		this.totalCantidad = totalCantidad;
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
	public String getNroFactura() {
		return this.nroFactura;
	}

	/**
	 * @generated
	 */
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
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
	public Double getTotalMinimo() {
		return this.totalMinimo;
	}

	/**
	 * @generated
	 */
	public void setTotalMinimo(Double totalMinimo) {
		this.totalMinimo = totalMinimo;
	}

	/**
	 * @generated
	 */
	public java.util.Date getFechaEntrega() {
		return this.fechaEntrega;
	}

	/**
	 * @generated
	 */
	public void setFechaEntrega(java.util.Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}

	/**
	 * @generated
	 */
	public String getCiEntrega() {
		return this.ciEntrega;
	}

	/**
	 * @generated
	 */
	public void setCiEntrega(String ciEntrega) {
		this.ciEntrega = ciEntrega;
	}

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	public Conocimiento getConocimiento() {
		return this.conocimiento;
	}

	/**
	 * @generated
	 */
	public void setConocimiento(Conocimiento conocimiento) {
		this.conocimiento = conocimiento;
	}

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	public TipoPago getTipoPago() {
		return this.tipoPago;
	}

	/**
	 * @generated
	 */
	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	/**
	 * @generated
	 */
	public String getNovedadEntrega() {
		return this.novedadEntrega;
	}

	/**
	 * @generated
	 */
	public void setNovedadEntrega(String novedadEntrega) {
		this.novedadEntrega = novedadEntrega;
	}

	/**
	 * @generated
	 */
	public String getResumenContenido() {
		return this.resumenContenido;
	}

	/**
	 * @generated
	 */
	public void setResumenContenido(String resumenContenido) {
		this.resumenContenido = resumenContenido;
	}

	/**
	 * @generated
	 */
	public Double getSaldoDestino() {
		return this.saldoDestino;
	}

	/**
	 * @generated
	 */
	public void setSaldoDestino(Double saldoDestino) {
		this.saldoDestino = saldoDestino;
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
	public Double getTotalGuia() {
		return this.totalGuia;
	}

	/**
	 * @generated
	 */
	public void setTotalGuia(Double totalGuia) {
		this.totalGuia = totalGuia;
	}

	/**
	 * @generated
	 */
	public Boolean getActivo() {
		return this.activo;
	}

	/**
	 * @generated
	 */
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}

	/**
	 * @generated
	 */
	public String getNotaEntrega() {
		return this.notaEntrega;
	}

	/**
	 * @generated
	 */
	public void setNotaEntrega(String notaEntrega) {
		this.notaEntrega = notaEntrega;
	}

	/**
	 * @generated
	 */
	@javax.persistence.Transient
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
	@javax.persistence.ManyToOne
	public Cliente getRemitente() {
		return this.remitente;
	}

	/**
	 * @generated
	 */
	public void setRemitente(Cliente remitente) {
		this.remitente = remitente;
	}

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	public Cliente getConsignatario() {
		return this.consignatario;
	}

	/**
	 * @generated
	 */
	public void setConsignatario(Cliente consignatario) {
		this.consignatario = consignatario;
	}

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	public Usuario getUsuarioRegistro() {
		return this.usuarioRegistro;
	}

	/**
	 * @generated
	 */
	public void setUsuarioRegistro(Usuario usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	public Usuario getUsuarioEntrega() {
		return this.usuarioEntrega;
	}

	/**
	 * @generated
	 */
	public void setUsuarioEntrega(Usuario usuarioEntrega) {
		this.usuarioEntrega = usuarioEntrega;
	}

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
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
	@javax.persistence.ManyToOne
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
	@javax.persistence.OneToMany(mappedBy = "guia")
	public java.util.Set<Item> getItems() {
		return this.items;
	}

	/**
	 * @generated
	 */
	public void setItems(java.util.Set<Item> items) {
		this.items = items;
	}

	/**
	 * @generated
	 */
	public void addItems(Item items) {
		getItems().add(items);
		items.setGuia(this);
	}

	/**
	 * @generated
	 */
	public void removeItems(Item items) {
		getItems().remove(items);
		items.setGuia(null);
	}
}