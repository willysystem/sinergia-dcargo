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
import com.sinergia.dcargo.client.local.api.ServicioClienteCliente;
import com.sinergia.dcargo.client.local.api.ServicioConocimientoCliente;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.api.ServicioItemCliente;
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.api.ServicioOficinaCliente;
import com.sinergia.dcargo.client.local.api.ServicioPrecioCliente;
import com.sinergia.dcargo.client.local.api.ServicioTransportistasCliente;
import com.sinergia.dcargo.client.local.api.ServicioUnidadCliente;
import com.sinergia.dcargo.client.local.api.ServicioUsuarioCliente;

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
		
        appController.go(RootLayoutPanel.get());
	}
	
	@Produces
	private HandlerManager produceEventBus() {
	  return eventBus;
	}
	

	@Produces
	public ServicioUsuarioCliente servicioUsuarioCliente() {
		return GWT.create(ServicioUsuarioCliente.class);
	}
	
	@Produces
	public ServicioItemCliente servicioItemCliente() {
		return GWT.create(ServicioItemCliente.class);
	}

	@Produces
	public ServicioGuiaCliente servicioGuiaCliente() {
		return GWT.create(ServicioGuiaCliente.class);
	}

	@Produces
	public ServicioClienteCliente servicioClienteCliente() {
		return GWT.create(ServicioClienteCliente.class);
	}

	@Produces
	public ServicioOficinaCliente servicioOficinaCliente() {
		return GWT.create(ServicioOficinaCliente.class);
	}

	@Produces
	public ServicioConocimientoCliente servicioConocimientoCliente() {
		return GWT.create(ServicioConocimientoCliente.class);
	}

	@Produces
	public ServicioTransportistasCliente servicioTransportistasCliente() {
		return GWT.create(ServicioTransportistasCliente.class);
	}
	
	@Produces
	public ServicioUnidadCliente servicioUnidadCliente() {
		return GWT.create(ServicioUnidadCliente.class);
	}
	
	@Produces
	public ServicioPrecioCliente servicioPrecioCliente() {
		return GWT.create(ServicioPrecioCliente.class);
	}

	@Produces
	public ServicioCuentaCliente servicioCuentaCliente() {
		return GWT.create(ServicioCuentaCliente.class);
	}
	
	@Produces
	public ServicioMovimientoCliente servicioMovimientoCliente() {
		return GWT.create(ServicioMovimientoCliente.class);
	}

	
}
