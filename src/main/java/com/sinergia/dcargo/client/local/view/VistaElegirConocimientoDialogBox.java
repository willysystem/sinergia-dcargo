package com.sinergia.dcargo.client.local.view;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.user.client.ui.DialogBox;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.local.presenter.PresentadorConocimiento.Display;

@Singleton
public class VistaElegirConocimientoDialogBox extends DialogBox {
	
	@Inject
	private Display vistaConocimiento;
	
	private Conocimiento conocimientoSeleccionada;
	
	private VistaMovimientoAccion vistaMovimientoAccion;
	
	public VistaElegirConocimientoDialogBox(){
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Elegir Guía");
	}
	
	public void mostrar() {
		vistaConocimiento.setVistaElegirConocimientoDialogBox(this);
		clear(); 
		add(vistaConocimiento.viewIU(true));
		center();
	}

	public Conocimiento getConocimientoSeleccionada() {
		return conocimientoSeleccionada;
	}

	public void setConocimientoSeleccionada(Conocimiento conocimientoSeleccionada) {
		this.conocimientoSeleccionada = conocimientoSeleccionada;
		vistaMovimientoAccion.mostrarConocimientoSeleccionado(conocimientoSeleccionada);
		this.hide();
	}

	public void setVistaMovimientoAccion(VistaMovimientoAccion vistaMovimientoAccion) {
		this.vistaMovimientoAccion = vistaMovimientoAccion;
	}
	
}
