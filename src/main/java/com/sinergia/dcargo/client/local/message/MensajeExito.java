package com.sinergia.dcargo.client.local.message;

import javax.inject.Singleton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

@Singleton
public class MensajeExito extends DialogBox {
	
	private Button aceptarButton;
	
	public MensajeExito() {
		super();
	}
	
	public void mostrar(String mensaje){
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Mensaje");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label label = new Label();
		label.setText(mensaje);
		label.setPixelSize(250, 50);
		verticalPanel.add(label);
		
		aceptarButton = new Button("Aceptar");
		aceptarButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MensajeExito.this.hide();
			}
		});
		verticalPanel.add(aceptarButton);
		
		setWidget(verticalPanel);
		center();

	}
	
	public void mostrar(String mensaje, final HasClickHandlers button){
		mostrar(mensaje);
		aceptarButton.addClickHandler( e -> ((Button)button).click());
	}
	
	public MensajeExito(String mensaje){
		super();
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Mensaje");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label label = new Label();
		label.setText(mensaje);
		label.setPixelSize(250, 50);
		verticalPanel.add(label);
		
		Button aceptarButton = new Button("Aceptar");
		aceptarButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MensajeExito.this.hide();
			}
		});
		verticalPanel.add(aceptarButton);
		
		setWidget(verticalPanel);
		center();
	} 
	
}
