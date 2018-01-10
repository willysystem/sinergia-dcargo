package com.sinergia.dcargo.client.shared.dominio;

import javax.persistence.Transient;

/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "item")
public class Item implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -309157368L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;
	/**
	 * @generated
	 */
	private Integer nroSecuencial;
	/**
	 * @generated
	 */
	private Integer cantidad;
	/**
	 * @generated
	 */
	private String contenido;
	/**
	 * @generated
	 */
	private Double peso;
	/**
	 * @generated
	 */
	private Double monto;
	/**
	 * @generated
	 */
	private Boolean activo;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Guia guia;
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Unidad unidad;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Integer nro;
	/**
	 * @generated
	 */
	private Double precio;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Boolean valido;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private String unidadTitulo;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private String precioMonto;
	/**
	 * @generated
	 */
	private Double total;

	@Transient
	private String eliminar;
	
	/**
	 * @generated
	 */
	public Item() {
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
	public Integer getNroSecuencial() {
		return this.nroSecuencial;
	}

	/**
	 * @generated
	 */
	public void setNroSecuencial(Integer nroSecuencial) {
		this.nroSecuencial = nroSecuencial;
	}

	/**
	 * @generated
	 */
	public Integer getCantidad() {
		return this.cantidad;
	}

	/**
	 * @generated
	 */
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @generated
	 */
	public String getContenido() {
		return this.contenido;
	}

	/**
	 * @generated
	 */
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

	/**
	 * @generated
	 */
	public Double getPeso() {
		return this.peso;
	}

	/**
	 * @generated
	 */
	public void setPeso(Double peso) {
		this.peso = peso;
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
	public Guia getGuia() {
		return this.guia;
	}

	/**
	 * @generated
	 */
	public void setGuia(Guia guia) {
		this.guia = guia;
	}

	/**
	 * @generated
	 */
	public Unidad getUnidad() {
		return this.unidad;
	}

	/**
	 * @generated
	 */
	public void setUnidad(Unidad unidad) {
		this.unidad = unidad;
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
	public Double getPrecio() {
		return this.precio;
	}

	/**
	 * @generated
	 */
	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	/**
	 * @generated
	 */
	public Boolean getValido() {
		return this.valido;
	}

	/**
	 * @generated
	 */
	public void setValido(Boolean valido) {
		this.valido = valido;
	}

	/**
	 * @generated
	 */
	public String getUnidadTitulo() {
		return this.unidadTitulo;
	}

	/**
	 * @generated
	 */
	public void setUnidadTitulo(String unidadTitulo) {
		this.unidadTitulo = unidadTitulo;
	}

	/**
	 * @generated
	 */
	public String getPrecioMonto() {
		return this.precioMonto;
	}

	/**
	 * @generated
	 */
	public void setPrecioMonto(String precioMonto) {
		this.precioMonto = precioMonto;
	}

	/**
	 * @generated
	 */
	public Double getTotal() {
		return this.total;
	}

	/**
	 * @generated
	 */
	public void setTotal(Double total) {
		this.total = total;
	}
	
	public String toString() {
		return "";
	}

	public String getEliminar() {
		return eliminar;
	}

	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	
	
}