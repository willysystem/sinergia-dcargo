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
import com.sinergia.dcargo.client.local.api.ServicioConocimientoCliente;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.view.Cargador;
import com.sinergia.dcargo.client.shared.Conocimiento;
import com.sinergia.dcargo.client.shared.Transportista;

@Singleton
public class PresentadorConocimiento implements Presenter {

	@Inject
	public Display display;

	@Inject
	private Logger log;
	
	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	private Cargador cargador;
	
	@Inject
	private ServicioConocimientoCliente servicioConocimiento;
	
	private List<Transportista> transportistas;
	
	public interface Display {
		
		void viewIU();
		HasClickHandlers getBuscarButton();
		void cargarDataUI(List<Conocimiento> conocimientos);
		Conocimiento getParametrosBusqueda();
		void fijarOracleParaTransportistas(List<String> palabras);
		
	}
	
	public PresentadorConocimiento() {
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
		log.info(this.getClass().getSimpleName() + ".go()2" );
		
		transportistas = adminParametros.getTransportistas();
		
		List<String> palabras = new ArrayList<>();
		for (Transportista cli :transportistas) {
			if(cli.getNombre() != null){
				palabras.add(cli.getNombre());
			}
		}
		log.info(this.getClass().getSimpleName() + ".go()3: ");
		
		display.fijarOracleParaTransportistas(palabras);
		log.info(this.getClass().getSimpleName() + ".go()4" );
		bind();

	}

	public void bind() {
		
		this.display.getBuscarButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Conocimiento conocimiento = display.getParametrosBusqueda();
				log.info("conocimiento: "+ conocimiento);
				cargador.center();
				servicioConocimiento.buscarConocimiento(conocimiento, new MethodCallback<List<Conocimiento>>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al traer Guias: " + exception.getMessage());
						cargador.hide();
						new MensajeError("Error al traer Guias: ", exception).show();
					}
					@Override
					public void onSuccess(Method method, List<Conocimiento> response) {
						showGuiaData(response);
						cargador.hide();
					}
				});
			}
		});
		

	}
	
	int i = 1;
	private void showGuiaData(List<Conocimiento> conocimientos) {
		for (Conocimiento guia: conocimientos) {
			guia.setNro(i++);
		}
		i = 1;
		display.cargarDataUI(conocimientos);
	}
	
}
