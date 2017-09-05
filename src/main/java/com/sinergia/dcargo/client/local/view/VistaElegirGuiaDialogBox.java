package com.sinergia.dcargo.client.local.view;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.user.client.ui.DialogBox;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia.Display;

@Singleton
public class VistaElegirGuiaDialogBox extends DialogBox {
	
	@Inject
	private Display vistaGuia;
	
	private Guia guiaSeleccionada;
	
	private VistaMovimientoAccion vistaMovimientoAccion;
	
	public VistaElegirGuiaDialogBox(){
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Elegir Guia");
	}
	
	public void mostrar() {
		vistaGuia.setVistaElegirGuiaDialogBox(this);
		clear();
		add(vistaGuia.viewIU(true));
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
