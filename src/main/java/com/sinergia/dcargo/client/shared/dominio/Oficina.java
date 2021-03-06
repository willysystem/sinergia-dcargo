package com.sinergia.dcargo.client.shared.dominio;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "oficina")
public class Oficina implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -1064520202L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	private Long id;
	/**
	 * @generated
	 */
	private String nombre;
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "office")
	private java.util.Set<Usuario> users = new java.util.HashSet<Usuario>();
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "oficinaOrigen")
	private java.util.Set<Conocimiento> conocimientosOrigen = new java.util.HashSet<Conocimiento>();
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "oficinaDestino")
	private java.util.Set<Conocimiento> conocimientosDestino = new java.util.HashSet<Conocimiento>();
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "oficinaOrigen")
	private java.util.Set<Guia> guiasOrigen = new java.util.HashSet<Guia>();
	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "oficinaDestino")
	private java.util.Set<Guia> guiasDestino = new java.util.HashSet<Guia>();

	/**
	 * @generated
	 */
	@javax.persistence.OneToOne(mappedBy = "oficina")
	private CuentaEgreso cuentasEgreso;

	/**
	 * @generated
	 */
	@javax.persistence.OneToOne(mappedBy = "oficinaIngreso")
	private CuentaIngreso cuentaIngreso;

	/**
	 * @generated
	 */
	public Oficina() {
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
	public java.util.Set<Usuario> getUsers() {
		return this.users;
	}

	/**
	 * @generated
	 */
	public void setUsers(java.util.Set<Usuario> users) {
		this.users = users;
	}

	/**
	 * @generated
	 */
	public void addUsers(Usuario users) {
		getUsers().add(users);
		users.setOffice(this);
	}

	/**
	 * @generated
	 */
	public void removeUsers(Usuario users) {
		getUsers().remove(users);
		users.setOffice(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Conocimiento> getConocimientosOrigen() {
		return this.conocimientosOrigen;
	}

	/**
	 * @generated
	 */
	public void setConocimientosOrigen(
			java.util.Set<Conocimiento> conocimientosOrigen) {
				this.conocimientosOrigen = conocimientosOrigen;
			}

	/**
	 * @generated
	 */
	public void addConocimientosOrigen(Conocimiento conocimientosOrigen) {
		getConocimientosOrigen().add(conocimientosOrigen);
		conocimientosOrigen.setOficinaOrigen(this);
	}

	/**
	 * @generated
	 */
	public void removeConocimientosOrigen(Conocimiento conocimientosOrigen) {
		getConocimientosOrigen().remove(conocimientosOrigen);
		conocimientosOrigen.setOficinaOrigen(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Conocimiento> getConocimientosDestino() {
		return this.conocimientosDestino;
	}

	/**
	 * @generated
	 */
	public void setConocimientosDestino(
			java.util.Set<Conocimiento> conocimientosDestino) {
				this.conocimientosDestino = conocimientosDestino;
			}

	/**
	 * @generated
	 */
	public void addConocimientosDestino(Conocimiento conocimientosDestino) {
		getConocimientosDestino().add(conocimientosDestino);
		conocimientosDestino.setOficinaDestino(this);
	}

	/**
	 * @generated
	 */
	public void removeConocimientosDestino(Conocimiento conocimientosDestino) {
		getConocimientosDestino().remove(conocimientosDestino);
		conocimientosDestino.setOficinaDestino(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Guia> getGuiasOrigen() {
		return this.guiasOrigen;
	}

	/**
	 * @generated
	 */
	public void setGuiasOrigen(java.util.Set<Guia> guiasOrigen) {
		this.guiasOrigen = guiasOrigen;
	}

	/**
	 * @generated
	 */
	public void addGuiasOrigen(Guia guiasOrigen) {
		getGuiasOrigen().add(guiasOrigen);
		guiasOrigen.setOficinaOrigen(this);
	}

	/**
	 * @generated
	 */
	public void removeGuiasOrigen(Guia guiasOrigen) {
		getGuiasOrigen().remove(guiasOrigen);
		guiasOrigen.setOficinaOrigen(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<Guia> getGuiasDestino() {
		return this.guiasDestino;
	}

	/**
	 * @generated
	 */
	public void setGuiasDestino(java.util.Set<Guia> guiasDestino) {
		this.guiasDestino = guiasDestino;
	}

	/**
	 * @generated
	 */
	public void addGuiasDestino(Guia guiasDestino) {
		getGuiasDestino().add(guiasDestino);
		guiasDestino.setOficinaDestino(this);
	}

	/**
	 * @generated
	 */
	public void removeGuiasDestino(Guia guiasDestino) {
		getGuiasDestino().remove(guiasDestino);
		guiasDestino.setOficinaDestino(null);
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Oficina" + " id=" + id + " nombre=" + nombre;
	}

	/**
	 * @generated
	 */
	public CuentaEgreso getCuentasEgreso() {
		return this.cuentasEgreso;
	}

	/**
	 * @generated
	 */
	public void setCuentasEgreso(CuentaEgreso cuentasEgreso) {
		this.cuentasEgreso = cuentasEgreso;
	}

	/**
	 * @generated
	 */
	public CuentaIngreso getCuentaIngreso() {
		return this.cuentaIngreso;
	}

	/**
	 * @generated
	 */
	public void setCuentaIngreso(CuentaIngreso cuentaIngreso) {
		this.cuentaIngreso = cuentaIngreso;
	}
}
