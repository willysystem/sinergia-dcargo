package com.sinergia.dcargo.client.shared;

import org.jboss.errai.common.client.api.annotations.Portable;

/**
 * @generated
 */
@javax.persistence.Entity
@Portable
public class User extends Person implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -1105196257L;

	/**
	 * @generated
	 */
	private String user;

	/**
	 * @generated
	 */
	private String password;

	/**
	 * @generated
	 */
	private java.util.Date expirationDate;

	/**
	 * @generated
	 */
	private Boolean active;

	/**
	 * @generated
	 */
	private Boolean administrator;

	/**
	 * @generated
	 */
	public String toString() {
		return "User" + " user=" + user + " password=" + password
				+ " expirationDate=" + expirationDate + " active=" + active
				+ " administrator=" + administrator;
	}

	/**
	 * @generated
	 */
	public User() {
	}

	/**
	 * @generated
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @generated
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @generated
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @generated
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @generated
	 */
	public java.util.Date getExpirationDate() {
		return this.expirationDate;
	}

	/**
	 * @generated
	 */
	public void setExpirationDate(java.util.Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @generated
	 */
	public Boolean getActive() {
		return this.active;
	}

	/**
	 * @generated
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @generated
	 */
	public Boolean getAdministrator() {
		return this.administrator;
	}

	/**
	 * @generated
	 */
	public void setAdministrator(Boolean administrator) {
		this.administrator = administrator;
	}
}