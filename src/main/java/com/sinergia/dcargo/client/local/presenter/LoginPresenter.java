package com.sinergia.dcargo.client.local.presenter;

import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.event.LoginEvent;

/**
 * 
 * @author willy
 */
public class LoginPresenter implements Presenter {

	Boolean autoLogin = false;
	String user = "900009";
	String pass = "EPLACER";		
	
	public interface Display {
		
		HasValue<String> getUserName();

		HasValue<String> getPassword();

		HasClickHandlers getEnterButton();
		
		void showLogin();
		
		void hideLogin();
	}
	
	@Inject
	private Display display;
	
	@Inject
	private HandlerManager eventBus;

	public LoginPresenter() {

	}

	@Override
	public void go(HasWidgets container) {
	
		bind();
		
		display.showLogin(); 

	}

	public void bind() {
		this.display.getEnterButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//doSave();
				//Window.alert("hi");
				display.hideLogin();
				eventBus.fireEvent(new LoginEvent());
			}
		});
	}

}
