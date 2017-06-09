package com.sinergia.dcargo.client.shared;


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
	private Guia guia;
	/**
	 * @generated
	 */
	private Unidad unidad;

	/**
	 * @generated
	 */
	private Integer nro;

	/**
	 * @generated
	 */
	private Precio precio;

	/**
	 * @generated
	 */
	private Boolean valido;

	/**
	 * @generated
	 */
	private String unidadTitulo;
	/**
	 * @generated
	 */
	private String precioMonto;

	/**
	 * @generated
	 */
	private Double total;

	/**
	 * @generated
	 */
	public Item() {
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
	@javax.persistence.ManyToOne
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
	@javax.persistence.ManyToOne
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
	public String toString() {
		return "Item" + " id=" + id + " nroSecuencial=" + nroSecuencial
				+ " cantidad=" + cantidad + " contenido=" + contenido
				+ " peso=" + peso + " monto=" + monto + " activo=" + activo
				+ " nro=" + nro + " valido=" + valido + " unidadTitulo="
				+ unidadTitulo + " precioMonto=" + precioMonto + " total="
				+ total;
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
	public Precio getPrecio() {
		return this.precio;
	}

	/**
	 * @generated
	 */
	public void setPrecio(Precio precio) {
		this.precio = precio;
	}

	/**
	 * @generated
	 */
	@javax.persistence.Transient
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
	@javax.persistence.Transient
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
	@javax.persistence.Transient
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
}