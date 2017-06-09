package com.sinergia.dcargo.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.fusesource.restygwt.client.Defaults;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootLayoutPanel;

@EntryPoint
public class DCargo {
	
	@Inject
	private AppController appController;
	
	@Inject
	private Logger log;
	
	//@Inject
	//private VistaNuevaGuia vistaNuevaGuia;
	
	private HandlerManager eventBus = new HandlerManager(null);
	
	public DCargo() {
		GWT.log(this.getClass().getSimpleName() + "()");
	}

	@PostConstruct
	public void postContruct() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}
	
	@AfterInitialization
	public void init(){
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
		Defaults.setServiceRoot(GWT.getHostPageBaseURL());
	    
		//vistaNuevaGuia.mostrar();
		
        appController.go(RootLayoutPanel.get());
	}
	
	@Produces
	private HandlerManager produceEventBus() {
	  return eventBus;
	}

}
