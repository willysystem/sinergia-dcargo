package com.sinergia.dcargo.client.shared.dominio;

import javax.persistence.Transient;

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
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
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
	@javax.persistence.ManyToOne
	private Conocimiento conocimiento;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private TipoPago tipoPago;
	/**
	 * @generated
	 */
	private String nombreClienteEntrega;
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
	private String notaEntrega;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Integer nro;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Cliente remitente;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Cliente consignatario;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Usuario usuarioRegistro;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Usuario usuarioEntrega;
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
	@javax.persistence.OneToMany(mappedBy = "guia")
	private java.util.Set<Item> items = new java.util.HashSet<Item>();
	/**
	 * @generated
	 */
	private Character estado;
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

	@javax.persistence.OneToOne
	private MovimientoIngreso movimientoIngresoOrigen;
	
	@javax.persistence.OneToOne
	private MovimientoIngreso movimientoIngresoDestino;
	
	private Boolean pagadoOrigen;
	
	private Boolean pagadoDestino;
	
	private String nroFacturaEntrega;
	
	private Boolean entregaConsignatario;

	@Transient
	private boolean excluirGuiasExistentesEnConocimiento;
	
	/**
	 * @generated
	 */
	public Guia() {
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
	public TipoPago getTipoPago() {
		return this.tipoPago;
	}

	/**
	 * @generated
	 */
	public void setTipoPago(TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	public String getNombreClienteEntrega() {
		return nombreClienteEntrega;
	}

	public void setNombreClienteEntrega(String nombreClienteEntrega) {
		this.nombreClienteEntrega = nombreClienteEntrega;
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
	public String toString() {
		return "Guia" + " id=" + id + " nroGuia=" + nroGuia + " totalPeso="
				+ totalPeso + " totalCantidad=" + totalCantidad + " adjunto="
				+ adjunto + " nroFactura=" + nroFactura + " fechaRegistro="
				+ fechaRegistro + " totalMinimo=" + totalMinimo
				+ " fechaEntrega=" + fechaEntrega + " ciEntrega=" + ciEntrega
				+ " novedadEntrega=" + nombreClienteEntrega + " resumenContenido="
				+ resumenContenido + " saldoDestino=" + saldoDestino
				+ " pagoOrigen=" + pagoOrigen + " fecha=" + fecha
				+ " totalGuia=" + totalGuia + " notaEntrega=" + notaEntrega
				+ " nro=" + nro + " estado=" + estado + " estadoDescripcion="
				+ estadoDescripcion + " fechaIni=" + fechaIni + " fechaFin="
				+ fechaFin;
	}

	public String getNroFacturaEntrega() {
		return nroFacturaEntrega;
	}

	public void setNroFacturaEntrega(String nroFacturaEntrega) {
		this.nroFacturaEntrega = nroFacturaEntrega;
	}

	public Boolean getEntregaConsignatario() {
		return entregaConsignatario;
	}

	public void setEntregaConsignatario(Boolean entregaConsignatario) {
		this.entregaConsignatario = entregaConsignatario;
	}

	public Boolean getPagadoOrigen() {
		return pagadoOrigen;
	}

	public void setPagadoOrigen(Boolean pagadoOrigen) {
		this.pagadoOrigen = pagadoOrigen;
	}

	public Boolean getPagadoDestino() {
		return pagadoDestino;
	}

	public void setPagadoDestino(Boolean pagadoDestino) {
		this.pagadoDestino = pagadoDestino;
	}

	public MovimientoIngreso getMovimientoIngresoOrigen() {
		return movimientoIngresoOrigen;
	}

	public void setMovimientoIngresoOrigen(MovimientoIngreso movimientoIngresoOrigen) {
		this.movimientoIngresoOrigen = movimientoIngresoOrigen;
	}

	public MovimientoIngreso getMovimientoIngresoDestino() {
		return movimientoIngresoDestino;
	}

	public void setMovimientoIngresoDestino(MovimientoIngreso movimientoIngresoDestino) {
		this.movimientoIngresoDestino = movimientoIngresoDestino;
	}

	public boolean getExcluirGuiasExistentesEnConocimiento() {
		return excluirGuiasExistentesEnConocimiento;
	}

	public void setExcluirGuiasExistentesEnConocimiento(boolean excluirGuiasExistentesEnConocimiento) {
		this.excluirGuiasExistentesEnConocimiento = excluirGuiasExistentesEnConocimiento;
	}
	
}