package com.sinergia.dcargo.client.local.presenter;

import javax.inject.Inject;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;

public class MainContentPresenter implements Presenter {

	public interface Display {
		void showMainContent(HasWidgets container);
		HasWidgets getCentralPanel();
	}
	
	@Inject
	private Display display;
	
	@Inject
	private HandlerManager eventBus;

	public MainContentPresenter() {

	}

	@Override
	public void go(HasWidgets container) {
		//bind();
		display.showMainContent(container); 

	}

//	public void bind() {
//		this.display.getEnterButton().addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				//doSave();
//				//Window.alert("hi");
//				display.hideLogin();
//				eventBus.fireEvent(new LoginEvent());
//			}
//		});
//	}

}
