package com.sinergia.dcargo.client.local.view;

import javax.annotation.PostConstruct;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.sinergia.dcargo.client.local.presenter.LoginPresenter;

public class LoginView extends DialogBox implements LoginPresenter.Display {

	private boolean authenticated = false;
	
	private Button enterButton = new Button("Ingresar");
	
	private TextBox userTextBox = new TextBox();
	
	private PasswordTextBox passwordTextBox = new PasswordTextBox();
	
	public LoginView(){
		
	}
	
	@PostConstruct
	public void init() {
		
		userTextBox.setName("j_username");
		passwordTextBox.setName("j_password");
		
		FormPanel formPanel = new FormPanel(); 
		formPanel.setAction("j_security_check");
		
	    setGlassEnabled(true);
	    setAnimationEnabled(true);
	    ensureDebugId("login");
	    setModal(true);
	    setText("D´Cargo");
	    
	    FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    layout.setWidth("300px");
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
	    
	    layout.setHTML(0, 0, "<b>Autentificación</b>");
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    layout.setHTML(1, 0, "Usuario: ");
	    layout.setWidget(1, 1, userTextBox);
	    layout.setHTML(2, 0, "Contraseña: ");
	    layout.setWidget(2, 1, passwordTextBox);
	    
	    layout.setWidget(3, 1, enterButton);
	    
	    formPanel.setWidget(layout);
	    
	    setWidget(formPanel);
	    
	    
	    enterButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				authenticated = true;
				
			}
		});  
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public HasValue<String> getUserName() {
		return userTextBox;
	}

	@Override
	public HasValue<String> getPassword() {
		return passwordTextBox;
	}

	@Override
	public HasClickHandlers getEnterButton() {
		return enterButton;
	}

	@Override
	public void showLogin() {
		center();
	}

	@Override
	public void hideLogin() {
		this.authenticated = true;
		hide();
	}
	
}
