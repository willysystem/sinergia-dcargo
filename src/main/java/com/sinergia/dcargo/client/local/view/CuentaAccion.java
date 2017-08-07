package com.sinergia.dcargo.client.local.view;

public enum CuentaAccion {
	
	NUEVO_CUENTA("Nueva Cuenta Ingreso"),
	NUEVO_SUB_CUENTA("Nuevo Sub Cuenta Ingreso"),
	CONSULTAR("Consultar Cuenta"),
	MODIFICAR_CUENTA("Modificar Cuenta"),
	MODIFICAR_SUB_CUENTA("Modificar Sub Cuenta"),
	ELIMINAR("Eliminar Cuenta"),
	SALIR("Salir Cuenta");
	
	private String titulo;
	
	CuentaAccion(String titulo){
		this.titulo = titulo;
	}

	public String getTitulo() {
		return titulo;
	}
	
}
