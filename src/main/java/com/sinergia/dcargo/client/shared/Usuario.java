package com.sinergia.dcargo.client.shared;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "usuario")
public class Usuario extends Persona implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = 348744729L;

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	private Oficina office;

	/**
	 * @generated
	 */
	private String contrasenia;

	/**
	 * @generated
	 */
	private java.util.Date fechaExpiracion;

	/**
	 * @generated
	 */
	private Boolean administrador;

	/**
	 * @generated
	 */
	private String nombreUsuario;

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "usuario")
	private java.util.Set<Conocimiento> conocimientos = new java.util.HashSet<Conocimiento>();

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "usuarioRegistro")
	private java.util.Set<Guia> guiasRegistro = new java.util.HashSet<Guia>();

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "usuarioEntrega")
	private java.util.Set<Guia> guiasEntrega = new java.util.HashSet<Guia>();

	/**
	 * @generated
	 */
	private Character estado;

	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private String descripcionEstado;

	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Boolean activo;

	/**
	 * @generated
	 */
	public String toString() {
		return "Usuario" + " nombreUsuario=" + nombreUsuario + " contrasenia="
				+ contrasenia + " fechaExpiracion=" + fechaExpiracion
				+ " estado=" + estado + " administrador=" + administrador
				+ " descripcionEstado=" + descripcionEstado + " activo="
				+ activo;
	}

	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	public Oficina getOffice() {
		return this.office;
	}

	/**
	 * @generated
	 */
	public void setOffice(Oficina office) {
		this.office = office;
	}

	/**
	 * @generated
	 */
	public Usuario() {
	}

	/**
	 * @generated
	 */
	public String getContrasenia() {
		return this.contrasenia;
	}

	/**
	 * @generated
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * @generated
	 */
	public java.util.Date getFechaExpiracion() {
		return this.fechaExpiracion;
	}

	/**
	 * @generated
	 */
	public void setFechaExpiracion(java.util.Date fechaExpiracion) {
		this.fechaExpiracion = fechaExpiracion;
	}

	/**
	 * @generated
	 */
	public Boolean getAdministrador() {
		return this.administrador;
	}

	/**
	 * @generated
	 */
	public void setAdministrador(Boolean administrador) {
		this.administrador = administrador;
	}

	/**
	 * @generated
	 */
	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	/**
	 * @generated
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "usuario")
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
		conocimientos.setUsuario(this);
	}

	/**
	 * @generated
	 */
	public void removeConocimientos(Conocimiento conocimientos) {
		getConocimientos().remove(conocimientos);
		conocimientos.setUsuario(null);
	}

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "usuarioRegistro")
	public java.util.Set<Guia> getGuiasRegistro() {
		return this.guiasRegistro;
	}

	/**
	 * @generated
	 */
	public void setGuiasRegistro(java.util.Set<Guia> guiasRegistro) {
		this.guiasRegistro = guiasRegistro;
	}

	/**
	 * @generated
	 */
	public void addGuiasRegistro(Guia guiasRegistro) {
		getGuiasRegistro().add(guiasRegistro);
		guiasRegistro.setUsuarioRegistro(this);
	}

	/**
	 * @generated
	 */
	public void removeGuiasRegistro(Guia guiasRegistro) {
		getGuiasRegistro().remove(guiasRegistro);
		guiasRegistro.setUsuarioRegistro(null);
	}

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "usuarioEntrega")
	public java.util.Set<Guia> getGuiasEntrega() {
		return this.guiasEntrega;
	}

	/**
	 * @generated
	 */
	public void setGuiasEntrega(java.util.Set<Guia> guiasEntrega) {
		this.guiasEntrega = guiasEntrega;
	}

	/**
	 * @generated
	 */
	public void addGuiasEntrega(Guia guiasEntrega) {
		getGuiasEntrega().add(guiasEntrega);
		guiasEntrega.setUsuarioEntrega(this);
	}

	/**
	 * @generated
	 */
	public void removeGuiasEntrega(Guia guiasEntrega) {
		getGuiasEntrega().remove(guiasEntrega);
		guiasEntrega.setUsuarioEntrega(null);
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
	public String getDescripcionEstado() {
		return this.descripcionEstado;
	}

	/**
	 * @generated
	 */
	public void setDescripcionEstado(String descripcionEstado) {
		this.descripcionEstado = descripcionEstado;
	}

	/**
	 * @generated
	 */
	@javax.persistence.Transient
	public Boolean getActivo() {
		return this.activo;
	}

	/**
	 * @generated
	 */
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}