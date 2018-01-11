package com.sinergia.dcargo.server.util;

import javax.inject.Singleton;

@Singleton
public class UtilDCargoServer {
	
	public String validarNullParaMostrar(String valor) {
		return valor == null ? "" : valor; 
	}
	
	public String validarNullParaMostrar(Integer valor) {
		return valor == null ? "" : Integer.toString(valor); 
	}
	
}
