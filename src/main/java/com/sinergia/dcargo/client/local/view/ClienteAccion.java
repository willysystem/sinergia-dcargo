package com.sinergia.dcargo.client.local.view;

public enum ClienteAccion {
	
	NUEVO("Nuevo Cliente"),
	NUEVO_DESDE_GUIA("Nuevo Cliente"),
	CONSULTAR("Consultar Cliente"),
	MODIFICAR("Modificar Cliente"),
	//REPORTE("Reportes Guia"),
	ELIMINAR("Eliminar Cliente"),
	SALIR("Salir Guía");
	
	private String titulo;
	
	ClienteAccion(String titulo){
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
}
