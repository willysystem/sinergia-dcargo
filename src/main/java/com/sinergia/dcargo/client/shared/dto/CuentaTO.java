package com.sinergia.dcargo.client.shared.dto;

import java.util.List;

import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.TreeStore.TreeNode;

/**
 * @generated
 */
public class CuentaTO implements java.io.Serializable, TreeStore.TreeNode<CuentaTO> {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -918333955L;
	/**
	 * @generated
	 */
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
	private Integer nro;

	/**
	 * @generated
	 */
	private java.util.Set<CuentaTO> subCuentas = new java.util.HashSet<CuentaTO>();
	/**
	 * @generated
	 */
	private CuentaTO cuenta;

	/**
	 * @generated
	 */
	private String cuentaPadreDescripcion;

	/**
	 * @generated
	 */
	public CuentaTO() {
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
		return "CuentaTO" + " id=" + id + " nroCuenta=" + nroCuenta
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
	public java.util.Set<CuentaTO> getSubCuentas() {
		return this.subCuentas;
	}

	/**
	 * @generated
	 */
	public void setSubCuentas(java.util.Set<CuentaTO> subCuentas) {
		this.subCuentas = subCuentas;
	}

	/**
	 * @generated
	 */
	public void addSubCuentas(CuentaTO subCuentas) {
		getSubCuentas().add(subCuentas);
		subCuentas.setCuenta(this);
	}

	/**
	 * @generated
	 */
	public void removeSubCuentas(CuentaTO subCuentas) {
		getSubCuentas().remove(subCuentas);
		subCuentas.setCuenta(null);
	}

	/**
	 * @generated
	 */
	public CuentaTO getCuenta() {
		return this.cuenta;
	}

	/**
	 * @generated
	 */
	public void setCuenta(CuentaTO cuenta) {
		this.cuenta = cuenta;
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

	@Override
	public List<? extends TreeNode<CuentaTO>> getChildren() {
		return null;
	}

	@Override
	public CuentaTO getData() {
		return this;
	}

}