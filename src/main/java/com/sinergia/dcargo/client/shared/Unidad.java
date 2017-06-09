package com.sinergia.dcargo.client.shared;


/**
 * @generated
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "unidad")
public class Unidad implements java.io.Serializable {
	/**
	 * @generated
	 */
	private static final long serialVersionUID = -409364916L;
	/**
	 * @generated
	 */
	private Long id;
	/**
	 * @generated
	 */
	private java.util.Set<Item> items = new java.util.HashSet<Item>();
	/**
	 * @generated
	 */
	private String abreviatura;

	/**
	 * @generated
	 */
	public Unidad() {
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
	@javax.persistence.OneToMany(mappedBy = "unidad")
	public java.util.Set<Item> getItems() {
		return this.items;
	}

	/**
	 * @generated
	 */
	public void setItems(java.util.Set<Item> items) {
		this.items = items;
	}

	/**
	 * @generated
	 */
	public void addItems(Item items) {
		getItems().add(items);
		items.setUnidad(this);
	}

	/**
	 * @generated
	 */
	public void removeItems(Item items) {
		getItems().remove(items);
		items.setUnidad(null);
	}

	/**
	 * @generated
	 */
	public String getAbreviatura() {
		return this.abreviatura;
	}

	/**
	 * @generated
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	/**
	 * @generated
	 */
	public String toString() {
		return "Unidad" + " id=" + id + " abreviatura=" + abreviatura;
	}
}