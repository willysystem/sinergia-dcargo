package com.sinergia.dcargo.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class DCargo {
	
	//@Inject
	//private MainContainer mainContainer;
	
	@Inject
	private AppController appController;
	
	private HandlerManager eventBus = new HandlerManager(null);

	@PostConstruct
	public void init() {
		appController.go(RootPanel.get());
	}
	
	@Produces
	private HandlerManager produceEventBus() {
	  return eventBus;
	}

}
