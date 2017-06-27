package com.sinergia.dcargo.client.local.view;

public enum TransportistaAccion {
	
	NUEVO("Nuevo Transportista"),
	NUEVO_DESDE_CONOCIMIENTO("Nuevo Transportista"),
	CONSULTAR("Consultar Transportista"),
	MODIFICAR("Modificar Transportista"),
	//REPORTE("Reportes Guia"),
	ELIMINAR("Eliminar Transportista"),
	SALIR("Salir Transportista");
	
	private String titulo;
	
	TransportistaAccion(String titulo){
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
}
