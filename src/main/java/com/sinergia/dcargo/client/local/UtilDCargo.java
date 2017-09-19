package com.sinergia.dcargo.client.local;

import java.util.Date;

import javax.inject.Singleton;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.inject.Inject;

@Singleton
public class UtilDCargo {
	
	@Inject
	private AdminParametros adminParametros; 
	
	public String getCiudad() {
		String ciudad    = "";
		String cuidadUsuario = adminParametros.getUsuario().getOffice().getNombre().toLowerCase(); 
		if(cuidadUsuario.contains("santa cruz")) ciudad    = "Santa Cruz";
		if(cuidadUsuario.contains("cochabamba")) ciudad    = "Cochabamba";
		if(cuidadUsuario.contains("alto"))       ciudad = "El Alto"; 
		return ciudad;
	}
	
	public String getDireccion() {
		String direccion = "";
		String cuidadUsuario = adminParametros.getUsuario().getOffice().getNombre().toLowerCase(); 
		if(cuidadUsuario.contains("santa cruz")) direccion = "Av. Alemana entre 6º y 7º Anillo";
		if(cuidadUsuario.contains("cochabamba")) direccion = "Av. Melchor Perez S#350"; 
		if(cuidadUsuario.contains("alto")) direccion = "Av. Tiahuanacu y calle 147"; 
		return direccion;
	}
	
	public String getTelefono() {
		String telefono  = "";
		String cuidadUsuario = adminParametros.getUsuario().getOffice().getNombre().toLowerCase(); 
		if(cuidadUsuario.contains("santa cruz")) telefono  = "Telf. 3414233 - 70716183";
		if(cuidadUsuario.contains("cochabamba")) telefono  = "Telf, 4588797-4409915";
		if(cuidadUsuario.contains("alto"))       telefono  = "Telf. 2765050 - 70718912";
		return telefono;
	}

	DateTimeFormat dtfSmall  = DateTimeFormat.getFormat("yyyy-MM-dd");
	DateTimeFormat dtfMedium = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
	NumberFormat   dff       = NumberFormat.getFormat("00.00");
	
	public String validarNullParaMostrarSmall(Date valor) {
		return valor == null ? "" : dtfSmall.format(valor); 
	}
	
	public String validarNullParaMostrarMedium(Date valor) {
		return valor == null ? "" : dtfMedium.format(valor);
	}
	
	public String validarNullParaMostrar(String valor) {
		return valor == null ? "" : valor; 
	}
	
	public String validarNullParaMostrar(Double valor) {
		return valor == null ? "" : dff.format(valor); 
	}
	
	public String validarNullParaMostrar(Integer valor) {
		return valor == null ? "" : Integer.toString(valor); 
	}
	
}
