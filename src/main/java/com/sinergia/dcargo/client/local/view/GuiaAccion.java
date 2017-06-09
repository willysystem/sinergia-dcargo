package com.sinergia.dcargo.client.local.view;

public enum GuiaAccion {
	
	NUEVO("Nueva Guia"),
	CONSULTAR("Consultar Guia"),
	MODIFICAR("Modificar Guia"),
	ANULAR("Anular Guia"),
	REPORTE("Reportes Guia"),
	ENTREGA("Entrega Guia"),
	SALIR("Salir Guia");
	
	private String titulo;
	
	GuiaAccion(String titulo){
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
}
