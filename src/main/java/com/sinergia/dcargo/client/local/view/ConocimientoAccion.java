package com.sinergia.dcargo.client.local.view;

public enum ConocimientoAccion {
	
	NUEVO("Nuevo Conocimiento"),
	CONSULTAR("Consultar Conocimiento"),
	MODIFICAR("Modificar Conocimiento"),
	ANULAR("Anular Conocimiento"),
	REPORTE("Reportes Conocimiento"),
	ENTREGA("Entrega Conocimiento"),
	SALIR("Salir");
	
	private String titulo;
	
	ConocimientoAccion(String titulo){
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
}
