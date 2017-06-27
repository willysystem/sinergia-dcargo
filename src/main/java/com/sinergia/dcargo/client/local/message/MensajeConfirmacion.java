package com.sinergia.dcargo.client.local.message;

import javax.inject.Singleton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * 
 * @author willy
 */
@Singleton 
public class MensajeConfirmacion extends DialogBox {
	
	public MensajeConfirmacion(){
		super();
	}
	
	public MensajeConfirmacion(String mensaje, ClickHandler clickHandlerAceptar){
		super();
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Mensaje");
		
		//
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label label = new Label();
		label.setText(mensaje);
		label.setPixelSize(250, 50);
		verticalPanel.add(label);
		
		
		HorizontalPanel horizontalPanel2 = new HorizontalPanel();
		horizontalPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Button aceptarButton = new Button("Aceptar");
		aceptarButton.addClickHandler(clickHandlerAceptar);
		horizontalPanel2.add(aceptarButton);
		
		Button cancelarButton = new Button("Cancelar");
		cancelarButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MensajeConfirmacion.this.hide();
			}
		});
		horizontalPanel2.add(cancelarButton);
		
		verticalPanel.add(horizontalPanel2);
		
		setWidget(verticalPanel);
		//center();
	} 
	
	public void mostrar(String mensaje, ClickHandler clickHandlerAceptar){
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Mensaje");
		
		//
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label label = new Label();
		label.setText(mensaje);
		label.setPixelSize(250, 50);
		verticalPanel.add(label);
		
		
		HorizontalPanel horizontalPanel2 = new HorizontalPanel();
		horizontalPanel2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Button aceptarButton = new Button("Aceptar");
		aceptarButton.addClickHandler(clickHandlerAceptar);
		horizontalPanel2.add(aceptarButton);
		
		Button cancelarButton = new Button("Cancelar");
		cancelarButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MensajeConfirmacion.this.hide();
			}
		});
		horizontalPanel2.add(cancelarButton);
		
		verticalPanel.add(horizontalPanel2);
		
		setWidget(verticalPanel);
		center();
	}
	
}
