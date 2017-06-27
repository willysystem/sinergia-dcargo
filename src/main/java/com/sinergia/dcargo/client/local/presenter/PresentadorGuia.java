package com.sinergia.dcargo.client.local.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.view.Cargador;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Oficina;

@Singleton
public class PresentadorGuia implements Presenter {

	@Inject
	public Display display;

	@Inject
	private Logger log;
	
	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	private Cargador cargador;
	
	@Inject
	private MensajeError mensajeError;
	
	@Inject
	private ServicioGuiaCliente servicioGuia; // = GWT.create(ServicioGuiaCliente.class);

    List<Cliente> clientes = null;
	List<Oficina> oficinas = null;
	
	
	public interface Display {
		
		void viewIU();
		HasClickHandlers getBuscarButton();
		void cargarDataUI(List<Guia> clientes);
		Guia getParametrosBusqueda();
		void fijarOracleParaClientes(List<String> palabras);
		void fijarOracleParaOficina(List<String> palabras);
		
	}
	
	
	public PresentadorGuia() {
		GWT.log(this.getClass().getSimpleName() + "()");
	}
	
	@PostConstruct
	public void postConstruct(){
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}
	
	@AfterInitialization
	public void after() {
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
	}

	@Override
	public void go(HasWidgets container) {
		log.info(this.getClass().getSimpleName() + ".go()" );
		display.viewIU();
		
		clientes = adminParametros.getClientes();
		log.info("clientes.size: " + clientes.size());
		List<String> palabras = new ArrayList<>();
		for (Cliente cli : clientes) {
			palabras.add(cli.getNombre());
		}
		display.fijarOracleParaClientes(palabras);
		
		oficinas = adminParametros.getOficinas();
		log.info("oficinas.size: " + oficinas.size());
		List<String> palabras1 = new ArrayList<>();
		for (Oficina oficina : oficinas) {
			palabras1.add(oficina.getNombre());
		}
		display.fijarOracleParaOficina(palabras1);
		
		bind();

	}

	public void bind() {
		
		this.display.getBuscarButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Guia guia = display.getParametrosBusqueda();
				log.info("guia parametro búsqueda: "+ guia);
				//PresentadorGuia.this.cargador.center();
				servicioGuia.buscarGuias(guia, new MethodCallback<List<Guia>>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al traer Guias: " + exception.getMessage());
						//cargador.hide();
						mensajeError.mostrar("Error al traer Guias: ", exception);
					}
					@Override
					public void onSuccess(Method method, List<Guia> response) {
						log.info("response: " + response);
						showGuiaData(response);
						cargador.hide();
						
					}
				});
			}
		});
	}
	
	int i = 1;
	private void showGuiaData(List<Guia> guias) {
		for (Guia guia: guias) {
			guia.setNro(i++);
		}
		i = 1;
		display.cargarDataUI(guias);
	}
	
}
