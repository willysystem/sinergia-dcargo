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
	@javax.persistence.Column(unique = true)
	private String brevetCi;
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
	private Integer nro;
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "transportistaPropietario")
	private java.util.Set<Conocimiento> conocimientosPropietario = new java.util.HashSet<Conocimiento>();
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "transportistaConductor")
	private java.util.Set<Conocimiento> conocimientosConductor = new java.util.HashSet<Conocimiento>();

	/**
	 * @generated
	 */
	public Transportista() {
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
	public String getBrevetCi() {
		return this.brevetCi;
	}

	/**
	 * @generated
	 */
	public void setBrevetCi(String brevetCi) {
		this.brevetCi = brevetCi;
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
	public java.util.Set<Conocimiento> getConocimientosPropietario() {
		return this.conocimientosPropietario;
	}

	/**
	 * @generated
	 */
	public void setConocimientosPropietario(
			java.util.Set<Conocimiento> conocimientosPropietario) {
		this.conocimientosPropietario = conocimientosPropietario;
	}

	/**
	 * @generated
	 */
	public void addConocimientosPropietario(
			Conocimiento conocimientosPropietario) {
		getConocimientosPropietario().add(conocimientosPropietario);
		conocimientosPropietario.setTransportistaPropietario(this);
	}

	/**
	 * @generated
	 */
	public void removeConocimientosPropietario(
			Conocimiento conocimientosPropietario) {
		getConocimientosPropietario().remove(conocimientosPropietario);
		conocimientosPropietario.setTransportistaPropietario(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Conocimiento> getConocimientosConductor() {
		return this.conocimientosConductor;
	}

	/**
	 * @generated
	 */
	public void setConocimientosConductor(
			java.util.Set<Conocimiento> conocimientosConductor) {
		this.conocimientosConductor = conocimientosConductor;
	}

	/**
	 * @generated
	 */
	public void addConocimientosConductor(Conocimiento conocimientosConductor) {
		getConocimientosConductor().add(conocimientosConductor);
		conocimientosConductor.setTransportistaConductor(this);
	}

	/**
	 * @generated
	 */
	public void removeConocimientosConductor(Conocimiento conocimientosConductor) {
		getConocimientosConductor().remove(conocimientosConductor);
		conocimientosConductor.setTransportistaConductor(null);
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Transportista" + " id=" + id + " codigo=" + codigo
				+ " brevetCi=" + brevetCi + " nombre=" + nombre + " direccion="
				+ direccion + " telefono=" + telefono + " placa=" + placa
				+ " marca=" + marca + " color=" + color + " capacidad="
				+ capacidad + " vecino_de=" + vecino_de + " ci=" + ci + " nit="
				+ nit + " estado=" + estado + " estadoDescripcion="
				+ estadoDescripcion + " nro=" + nro;
	}
}