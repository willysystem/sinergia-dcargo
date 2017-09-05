package com.sinergia.dcargo.client.local.presenter;

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
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.view.Cargador;
import com.sinergia.dcargo.client.shared.dominio.Movimiento;

@Singleton
public class PresentadorMovimiento implements Presenter {

	@Inject
	public Display display;

	@Inject
	private Logger log;
	
	@Inject
	private ServicioMovimientoCliente servicioTransportistas;
	
	@Inject
	private Cargador cargador;

	public interface Display {

		void viewIU();
		HasClickHandlers getBuscarButton();
		void cargarDataUI(List<Movimiento> movimientos);
		Movimiento getParametrosBusqueda();

	}
	
	public PresentadorMovimiento() {
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
		bind();
	}

	public void bind() {
		this.display.getBuscarButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Movimiento cliente = display.getParametrosBusqueda();
				log.info("cliente: "+ cliente);
				cargador.center();
				servicioTransportistas.buscarMovimientos(cliente, new MethodCallback<List<Movimiento>>() {
				@Override
				public void onSuccess(Method method, List<Movimiento> response) {
					showClientesData(response);
					cargador.hide();
				}
				@Override
				public void onFailure(Method method, Throwable exception) {
					log.info("Error al guardar: " + exception.getMessage());
					new MensajeError("Error al guardar", exception).show();
				}
			});
			}
		});
	}
	
	int i = 1;
	private void showClientesData(List<Movimiento> clientes) {
		for (Movimiento cliente: clientes) {
			cliente.setNro(i++);
		}
		i = 1;
		display.cargarDataUI(clientes);
	}
	
}
