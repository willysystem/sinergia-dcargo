package com.sinergia.dcargo.client.local.view;

public enum MovimientoAccion {
	
	NUEVO_MOVIMIENTO("Nuevo Movimiento"),
	BUSCAR_MOVIMIEINTO("Nuevo Movimiento"),
	
//	NUEVO_INGRESO("Nuevo Ingreso"),
//	NUEVO_EGRESO("Nuevo Engreso"),
//	BUSCAR_INGRESO("Buscar Ingreso"),
//	BUSCAR_EGRESO("Buscar Engreso"),
	
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
