package com.sinergia.dcargo.client.local.view;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

@Singleton
public class Cargador extends PopupPanel {
	
	private HTML estadoHTML  = new HTML("");
	
	public Cargador(){
		super(false, true);
		
		Image image = new Image(AppBundle.INSTANCE.loading());
		image.setPixelSize(32, 32);
		setWidget(image);
		setGlassEnabled(true); 
	}
	
	public void fijarEstadoGuiaEspera(){
		fijarEstadoGuia("Actualizado ...", "red");
	}
	
	public void fijarEstadoGuiaCargado(){
		fijarEstadoGuia("Actualizado", "green");
	}
	
	private void fijarEstadoGuia(String mensaje, String color) {
		estadoHTML.setHTML("<h5 style='color:" + color + "'>" + mensaje + "</h5>");
	}
	
	public HTML getEstadoHTML() {
		return estadoHTML;
	}

	public interface AppBundle extends ClientBundle {

	    @Source("ajax-loader.gif")
	    ImageResource loading();

	    public static final AppBundle INSTANCE = GWT.create(AppBundle.class);

	}
}
