package com.sinergia.dcargo.client.local.view;

import javax.inject.Singleton;

import com.google.gwt.user.client.ui.DialogBox;
import com.sinergia.dcargo.client.shared.dominio.Guia;

@Singleton
public class EngregaGuiaDialogBox extends DialogBox {
	
	private Guia guiaSeleccionada;
	
	private VistaMovimientoAccion vistaMovimientoAccion;
	
	public EngregaGuiaDialogBox(){
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Entrega de Guia");
	}
	
	public void mostrar() {
		//clear();
		center();
	}

	public Guia getGuiaSeleccionada() {
		return guiaSeleccionada;
	}

	public void setGuiaSeleccionada(Guia guiaSeleccionada) {
		this.guiaSeleccionada = guiaSeleccionada;
		vistaMovimientoAccion.mostrarGuiaSeleccionada(guiaSeleccionada);
		this.hide();
	}

	public void setVistaMovimientoAccion(VistaMovimientoAccion vistaMovimientoAccion) {
		this.vistaMovimientoAccion = vistaMovimientoAccion;
	}
	
}
