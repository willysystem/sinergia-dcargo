package com.sinergia.dcargo.client.shared;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "cliente")
public class Cliente implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 1342176997L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;
	/**
	 * @generated
	 */
	@javax.persistence.Column(unique = true)
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
	@javax.persistence.Column(unique = true)
	private String nit;
	/**
	 * @generated
	 */
	private String ci;
	/**
	 * @generated
	 */
	private Integer codigo;
	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Integer nro;
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "remitente")
	private java.util.Set<Guia> guiasRemitente = new java.util.HashSet<Guia>();
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "consignatario")
	private java.util.Set<Guia> guiasConsignatario = new java.util.HashSet<Guia>();
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
	public Cliente() {
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
	public java.util.Set<Guia> getGuiasRemitente() {
		return this.guiasRemitente;
	}

	/**
	 * @generated
	 */
	public void setGuiasRemitente(java.util.Set<Guia> guiasRemitente) {
		this.guiasRemitente = guiasRemitente;
	}

	/**
	 * @generated
	 */
	public void addGuiasRemitente(Guia guiasRemitente) {
		getGuiasRemitente().add(guiasRemitente);
		guiasRemitente.setRemitente(this);
	}

	/**
	 * @generated
	 */
	public void removeGuiasRemitente(Guia guiasRemitente) {
		getGuiasRemitente().remove(guiasRemitente);
		guiasRemitente.setRemitente(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Guia> getGuiasConsignatario() {
		return this.guiasConsignatario;
	}

	/**
	 * @generated
	 */
	public void setGuiasConsignatario(java.util.Set<Guia> guiasConsignatario) {
		this.guiasConsignatario = guiasConsignatario;
	}

	/**
	 * @generated
	 */
	public void addGuiasConsignatario(Guia guiasConsignatario) {
		getGuiasConsignatario().add(guiasConsignatario);
		guiasConsignatario.setConsignatario(this);
	}

	/**
	 * @generated
	 */
	public void removeGuiasConsignatario(Guia guiasConsignatario) {
		getGuiasConsignatario().remove(guiasConsignatario);
		guiasConsignatario.setConsignatario(null);
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
	public String toString() {
		return "Cliente" + " id=" + id + " nombre=" + nombre + " direccion="
				+ direccion + " telefono=" + telefono + " nit=" + nit + " ci="
				+ ci + " codigo=" + codigo + " nro=" + nro + " estado="
				+ estado + " estadoDescripcion=" + estadoDescripcion;
	}
}