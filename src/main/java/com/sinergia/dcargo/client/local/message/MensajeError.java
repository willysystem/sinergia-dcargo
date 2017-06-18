package com.sinergia.dcargo.client.local.message;

import javax.inject.Singleton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

@Singleton
public class MensajeError extends DialogBox {
	
	public MensajeError(){
		super();
	}
	
	public MensajeError(String mensaje, Throwable throwable) {
		
		super();
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Mensaje de Error");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label label = new Label(mensaje);
		//label.setValue(mensaje);
		label.setPixelSize(350, 50);
		verticalPanel.add(label);
		
		String primaryCause = throwable.getMessage();
		String secondaryCause = "";
		if(throwable.getCause() != null){
			secondaryCause = throwable.getCause().getMessage();
		}
		String totalCause = primaryCause + "\n" + secondaryCause; 
		
		TextArea throwableTextArea = new TextArea();
		throwableTextArea.setPixelSize(350, 50);
		throwableTextArea.setValue(totalCause);
		verticalPanel.add(throwableTextArea);
		
		Button aceptarButton = new Button("Aceptar");
		aceptarButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MensajeError.this.hide();
			}
		});
		verticalPanel.add(aceptarButton);
		
		setWidget(verticalPanel);
		//center();
	} 
	
	public void mostrar(String mensaje, Throwable throwable){
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText("Mensaje de Error");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		Label label = new Label(mensaje);
		//label.setValue(mensaje);
		label.setPixelSize(350, 50);
		verticalPanel.add(label);
		
		String primaryCause = throwable.getMessage();
		String secondaryCause = "";
		if(throwable.getCause() != null){
			secondaryCause = throwable.getCause().getMessage();
		}
		String totalCause = primaryCause + "\n" + secondaryCause; 
		
		TextArea throwableTextArea = new TextArea();
		throwableTextArea.setPixelSize(350, 50);
		throwableTextArea.setValue(totalCause);
		verticalPanel.add(throwableTextArea);
		
		Button aceptarButton = new Button("Aceptar");
		aceptarButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MensajeError.this.hide();
			}
		});
		verticalPanel.add(aceptarButton);
		
		setWidget(verticalPanel);
	}
	
}
