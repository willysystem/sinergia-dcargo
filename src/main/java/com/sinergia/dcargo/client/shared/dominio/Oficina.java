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
	@javax.persistence.OneToOne(mappedBy = "oficinaIngresoOrigen")
	private CuentaIngreso cuentaIngresoOrigen;

	/**
	 * @generated
	 */
	@javax.persistence.OneToOne(mappedBy = "oficinaIngresoDestino")
	private CuentaIngreso cuentaIngresoDestino;
	/**
	 * @generated
	 */
	@javax.persistence.OneToOne(mappedBy = "oficinaEgresoaCuenta")
	private CuentaEgreso cuentaEgresoAcuenta;

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "oficina")
	private java.util.Set<CuentaEgreso> cuentasEgreso = new java.util.HashSet<CuentaEgreso>();

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "oficinaIngreso")
	private java.util.Set<CuentaIngreso> cuentasIngreso = new java.util.HashSet<CuentaIngreso>();

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
	public CuentaIngreso getCuentaIngresoOrigen() {
		return this.cuentaIngresoOrigen;
	}

	/**
	 * @generated
	 */
	public void setCuentaIngresoOrigen(
			CuentaIngreso cuentaIngresoOrigen) {
				this.cuentaIngresoOrigen = cuentaIngresoOrigen;
			}

	/**
	 * @generated
	 */
	public CuentaIngreso getCuentaIngresoDestino() {
		return this.cuentaIngresoDestino;
	}

	/**
	 * @generated
	 */
	public void setCuentaIngresoDestino(CuentaIngreso cuentaIngresoDestino) {
		this.cuentaIngresoDestino = cuentaIngresoDestino;
	}

	/**
	 * @generated
	 */
	public CuentaEgreso getCuentaEgresoAcuenta() {
		return this.cuentaEgresoAcuenta;
	}

	/**
	 * @generated
	 */
	public void setCuentaEgresoAcuenta(
			CuentaEgreso cuentaEgresoAcuenta) {
				this.cuentaEgresoAcuenta = cuentaEgresoAcuenta;
			}

	/**
	 * @generated
	 */
	public java.util.Set<CuentaEgreso> getCuentasEgreso() {
		return this.cuentasEgreso;
	}

	/**
	 * @generated
	 */
	public void setCuentasEgreso(java.util.Set<CuentaEgreso> cuentasEgreso) {
		this.cuentasEgreso = cuentasEgreso;
	}

	/**
	 * @generated
	 */
	public void addCuentasEgreso(CuentaEgreso cuentasEgreso) {
		getCuentasEgreso().add(cuentasEgreso);
		cuentasEgreso.setOficina(this);
	}

	/**
	 * @generated
	 */
	public void removeCuentasEgreso(CuentaEgreso cuentasEgreso) {
		getCuentasEgreso().remove(cuentasEgreso);
		cuentasEgreso.setOficina(null);
	}

	/**
	 * @generated
	 */
	public java.util.Set<CuentaIngreso> getCuentasIngreso() {
		return this.cuentasIngreso;
	}

	/**
	 * @generated
	 */
	public void setCuentasIngreso(java.util.Set<CuentaIngreso> cuentasIngreso) {
		this.cuentasIngreso = cuentasIngreso;
	}

	/**
	 * @generated
	 */
	public void addCuentasIngreso(CuentaIngreso cuentasIngreso) {
		getCuentasIngreso().add(cuentasIngreso);
		cuentasIngreso.setOficinaIngreso(this);
	}

	/**
	 * @generated
	 */
	public void removeCuentasIngreso(CuentaIngreso cuentasIngreso) {
		getCuentasIngreso().remove(cuentasIngreso);
		cuentasIngreso.setOficinaIngreso(null);
	}
}