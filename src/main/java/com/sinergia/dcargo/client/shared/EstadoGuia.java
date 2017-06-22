package com.sinergia.dcargo.client.shared;

import java.io.Serializable;

public class EstadoGuia implements Serializable {
	
	private static final long serialVersionUID = 10775504840530685L;
	private String estadoDescripcion;

	public String getEstadoDescripcion() {
		return estadoDescripcion;
	}

	public void setEstadoDescripcion(String estadoDescripcion) {
		this.estadoDescripcion = estadoDescripcion;
	}
	
	
}
