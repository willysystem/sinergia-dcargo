package com.sinergia.dcargo.client.local.presenter;

import java.util.List;

import javax.inject.Inject;

import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.shared.User;
import com.sinergia.dcargo.client.shared.UserService;

public class UserMainPresenter implements Presenter {

	public interface Display {
		
		void showUsers(List<User> response);
		
		HasClickHandlers getSaveButton();
		
		HasClickHandlers getNewButton();
		
		List<User> commitChangesLocal();
		
		void addNewUser(User user);
		
	}
	
	@Inject
	private Display display;
	
	@Inject
	private HandlerManager eventBus;
	
	@Inject
	private Caller<UserService> userService;

	public UserMainPresenter() {

	}

	int i = 1;
	
	@Override
	public void go(HasWidgets container) {
		
		userService.call(new RemoteCallback<List<User>>() {
			@Override
			public void callback(List<User> response) {
				i = 1;
				response.forEach(k -> k.setNro(i++));
				display.showUsers(response);
			}
		}).getAllUsers();
		
		//userService.call((final List<User> users) -> display.showUsers(users)).getAllUsers();
		bind();		 

	}

	public void bind() {
		
		this.display.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GWT.log("event: " + event);
				List<User> users = display.commitChangesLocal();
				userService.call(new RemoteCallback<Void>() {
					@Override
					public void callback(Void response) {
						Window.alert("Exitosamente");
					}
				}).saveAll(users);
			}
		});
		
		this.display.getNewButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				//GWT.log("new User: " + event);
				userService.call(new RemoteCallback<User>() {
					@Override
					public void callback(User response) {
						display.addNewUser(response);
					}
				}).newUser();
			}
		});
		
//		() {
//			public void onClick(ClickEvent event) {
//				//doSave();
//				//Window.alert("hi");
//				display.hideLogin();
//				eventBus.fireEvent(new LoginEvent());
//			}
//		});
		
	}

}
