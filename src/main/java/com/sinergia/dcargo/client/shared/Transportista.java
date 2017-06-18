package com.sinergia.dcargo.client.shared;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "transportista")
public class Transportista implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -373976981L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;

	/**
	 * @generated
	 */
	private Integer codigo;
	/**
	 * @generated
	 */
	private String brevet_ci;
	/**
	 * @generated
	 */
	private String nombre;
	/**
	 * @generated
	 */
	private String direccion;
	/**
	 * @generated
	 */
	private String telefono;
	/**
	 * @generated
	 */
	private String placa;
	/**
	 * @generated
	 */
	private String marca;
	/**
	 * @generated
	 */
	private String color;
	/**
	 * @generated
	 */
	private String capacidad;
	/**
	 * @generated
	 */
	private String vecino_de;
	/**
	 * @generated
	 */
	private String ci;
	/**
	 * @generated
	 */
	private String nit;

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "transportista")
	private java.util.Set<Conocimiento> conocimientos = new java.util.HashSet<Conocimiento>();

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "transportistaChofer")
	private java.util.Set<Conocimiento> conocimientosChofer = new java.util.HashSet<Conocimiento>();

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
	public Transportista() {
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
	public String toString() {
		return "Transportista" + " id=" + id + " codigo=" + codigo
				+ " brevet_ci=" + brevet_ci + " nombre=" + nombre
				+ " direccion=" + direccion + " telefono=" + telefono
				+ " placa=" + placa + " marca=" + marca + " color=" + color
				+ " capacidad=" + capacidad + " vecino_de=" + vecino_de
				+ " ci=" + ci + " nit=" + nit + " estado=" + estado
				+ " estadoDescripcion=" + estadoDescripcion;
	}

	/**
	 * @generated
	 */
	public Integer getCodigo() {
		return this.codigo;
	}

	/**
	 * @generated
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @generated
	 */
	public String getBrevet_ci() {
		return this.brevet_ci;
	}

	/**
	 * @generated
	 */
	public void setBrevet_ci(String brevet_ci) {
		this.brevet_ci = brevet_ci;
	}

	/**
	 * @generated
	 */
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * @generated
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @generated
	 */
	public String getDireccion() {
		return this.direccion;
	}

	/**
	 * @generated
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @generated
	 */
	public String getTelefono() {
		return this.telefono;
	}

	/**
	 * @generated
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @generated
	 */
	public String getPlaca() {
		return this.placa;
	}

	/**
	 * @generated
	 */
	public void setPlaca(String placa) {
		this.placa = placa;
	}

	/**
	 * @generated
	 */
	public String getMarca() {
		return this.marca;
	}

	/**
	 * @generated
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}

	/**
	 * @generated
	 */
	public String getColor() {
		return this.color;
	}

	/**
	 * @generated
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @generated
	 */
	public String getCapacidad() {
		return this.capacidad;
	}

	/**
	 * @generated
	 */
	public void setCapacidad(String capacidad) {
		this.capacidad = capacidad;
	}

	/**
	 * @generated
	 */
	public String getVecino_de() {
		return this.vecino_de;
	}

	/**
	 * @generated
	 */
	public void setVecino_de(String vecino_de) {
		this.vecino_de = vecino_de;
	}

	/**
	 * @generated
	 */
	public String getCi() {
		return this.ci;
	}

	/**
	 * @generated
	 */
	public void setCi(String ci) {
		this.ci = ci;
	}

	/**
	 * @generated
	 */
	public String getNit() {
		return this.nit;
	}

	/**
	 * @generated
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "transportista")
	public java.util.Set<Conocimiento> getConocimientos() {
		return this.conocimientos;
	}

	/**
	 * @generated
	 */
	public void setConocimientos(java.util.Set<Conocimiento> conocimientos) {
		this.conocimientos = conocimientos;
	}

	/**
	 * @generated
	 */
	public void addConocimientos(Conocimiento conocimientos) {
		getConocimientos().add(conocimientos);
		conocimientos.setTransportista(this);
	}

	/**
	 * @generated
	 */
	public void removeConocimientos(Conocimiento conocimientos) {
		getConocimientos().remove(conocimientos);
		conocimientos.setTransportista(null);
	}

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "transportistaChofer")
	public java.util.Set<Conocimiento> getConocimientosChofer() {
		return this.conocimientosChofer;
	}

	/**
	 * @generated
	 */
	public void setConocimientosChofer(
			java.util.Set<Conocimiento> conocimientosChofer) {
				this.conocimientosChofer = conocimientosChofer;
			}

	/**
	 * @generated
	 */
	public void addConocimientosChofer(Conocimiento conocimientosChofer) {
		getConocimientosChofer().add(conocimientosChofer);
		conocimientosChofer.setTransportistaChofer(this);
	}

	/**
	 * @generated
	 */
	public void removeConocimientosChofer(Conocimiento conocimientosChofer) {
		getConocimientosChofer().remove(conocimientosChofer);
		conocimientosChofer.setTransportistaChofer(null);
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
	@javax.persistence.Transient
	public String getEstadoDescripcion() {
		return this.estadoDescripcion;
	}

	/**
	 * @generated
	 */
	public void setEstadoDescripcion(String estadoDescripcion) {
		this.estadoDescripcion = estadoDescripcion;
	}


}