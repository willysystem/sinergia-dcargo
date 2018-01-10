package com.sinergia.dcargo.client.local.view;

public enum GuiaAccion {
	
	NUEVO("Nueva Guía"),
	CONSULTAR("Consultar Guía"),
	MODIFICAR("Modificar Guía"),
	ANULAR("Anular Guía"),
	REPORTE("Reportes Guía"),
	ENTREGA("Entrega Guía"),
	SALIR("Salir Guía");
	
	private String titulo;
	
	GuiaAccion(String titulo){
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
}
