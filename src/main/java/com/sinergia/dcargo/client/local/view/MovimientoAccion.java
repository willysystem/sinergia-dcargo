package com.sinergia.dcargo.client.local.view;

public enum MovimientoAccion {
	
	NUEVO("Nuevo Movimiento"),
	//NUEVO_DESDE_CONOCIMIENTO("Nuevo Transportista"),
	CONSULTAR("Consultar Movimiento"),
	MODIFICAR("Modificar Movimiento"),
	//REPORTE("Reportes Guia"),
	ELIMINAR("Eliminar Movimiento"),
	SALIR("Salir Movimiento");
	
	private String titulo;
	
	MovimientoAccion(String titulo){
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
}
