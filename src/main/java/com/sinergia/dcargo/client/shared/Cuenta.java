package com.sinergia.dcargo.client.shared;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;

/**
 * @generated
 */
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@javax.persistence.Entity
@Table(name="cuenta") 
//@JsonIdentityInfo(
//		  generator = ObjectIdGenerators.PropertyGenerator.class, 
//		  property = "id")
public class Cuenta implements java.io.Serializable/*, TreeStore.TreeNode<Cuenta>*/ {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -918333955L;
	/**
	 * @generated
	 */
	@javax.persistence.Id
	@javax.persistence.GeneratedValue
	private Long id;
	/**
	 * @generated
	 */
	private Integer nroCuenta;
	/**
	 * @generated
	 */
	private String descripcion;

	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private Integer nro;

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "cuenta", cascade=CascadeType.REMOVE)
	//@JsonBackReference
	@JsonIgnore 
	private java.util.Set<Cuenta> subCuentas = new java.util.HashSet<Cuenta>();
	/**
	 * @generated
	 */
	@javax.persistence.ManyToOne
	//@JsonManagedReference
	private Cuenta cuenta;

	/**
	 * @generated
	 */
	@javax.persistence.OneToMany(mappedBy = "cuenta")
	private java.util.Set<Movimiento> movimientos = new java.util.HashSet<Movimiento>();

	/**
	 * @generated
	 */
	@javax.persistence.Transient
	private String cuentaPadreDescripcion;

	/**
	 * @generated
	 */
	public Cuenta() {
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
	public Integer getNroCuenta() {
		return this.nroCuenta;
	}

	/**
	 * @generated
	 */
	public void setNroCuenta(Integer nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	/**
	 * @generated
	 */
	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * @generated
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Cuenta" + " id=" + id + " nroCuenta=" + nroCuenta
				+ " descripcion=" + descripcion + " nro=" + nro
				+ " cuentaPadreDescripcion=" + cuentaPadreDescripcion;
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
	public java.util.Set<Cuenta> getSubCuentas() {
		return this.subCuentas;
	}

	/**
	 * @generated
	 */
	public void setSubCuentas(java.util.Set<Cuenta> subCuentas) {
		this.subCuentas = subCuentas;
	}

	/**
	 * @generated
	 */
	public void addSubCuentas(Cuenta subCuentas) {
		getSubCuentas().add(subCuentas);
		subCuentas.setCuenta(this);
	}

	/**
	 * @generated
	 */
	public void removeSubCuentas(Cuenta subCuentas) {
		getSubCuentas().remove(subCuentas);
		subCuentas.setCuenta(null);
	}

	/**
	 * @generated
	 */
	public Cuenta getCuenta() {
		return this.cuenta;
	}

	/**
	 * @generated
	 */
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @generated
	 */
	public java.util.Set<Movimiento> getMovimientos() {
		return this.movimientos;
	}

	/**
	 * @generated
	 */
	public void setMovimientos(java.util.Set<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	/**
	 * @generated
	 */
	public void addMovimientos(Movimiento movimientos) {
		getMovimientos().add(movimientos);
		movimientos.setCuenta(this);
	}

	/**
	 * @generated
	 */
	public void removeMovimientos(Movimiento movimientos) {
		getMovimientos().remove(movimientos);
		movimientos.setCuenta(null);
	}

	/**
	 * @generated
	 */
	public String getCuentaPadreDescripcion() {
		return this.cuentaPadreDescripcion;
	}

	/**
	 * @generated
	 */
	public void setCuentaPadreDescripcion(String cuentaPadreDescripcion) {
		this.cuentaPadreDescripcion = cuentaPadreDescripcion;
	}

//	@Override
//	public List<? extends TreeNode<Cuenta>> getChildren() {
//		return null;
//	}
//
//	@Override
//	public Cuenta getData() {
//		return this;
//	}

}