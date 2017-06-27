package com.sinergia.dcargo.client.local.presenter;

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
import com.sinergia.dcargo.client.local.api.ServicioUsuarioCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.Usuario;

@Singleton
public class PresentadorCambioContrasenia implements Presenter {

	@Inject
	public Display display;
	
	@Inject
	public MainContentPresenter mainContentPresenter;
	
	@Inject
	private Logger log;
	
	private ServicioUsuarioCliente servicioUsuarioCliente = GWT.create(ServicioUsuarioCliente.class);

	public interface Display {

		void viewIU();
		HasClickHandlers getBottonCambiar();
		String getContraseniaNuevaUno();
		String getContraseniaNuevaDos();
		String getContraseniaAnterior();
			
	}
	
	
	public PresentadorCambioContrasenia() {
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

		this.display.getBottonCambiar().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String contraseniaAnterior = display.getContraseniaAnterior();
				String contraseniaNuevaUno = display.getContraseniaNuevaUno();
				String contraseniaNuevaDos = display.getContraseniaNuevaDos();
				
				// Validacion 
				if(contraseniaAnterior == null || contraseniaAnterior.equals("")) { new MensajeAviso("Contraseña anterior esta vacio").show(); return; }
				if(contraseniaNuevaUno == null || contraseniaNuevaUno.equals("")) { new MensajeAviso("Nueva contraseña esta vacio").show(); return; }
				if(contraseniaNuevaDos == null || contraseniaNuevaDos.equals("")) { new MensajeAviso("Reitere nueva contraseña esta vacio").show(); return; }
				
				if(!contraseniaNuevaUno.equals(contraseniaNuevaDos)){
					new MensajeAviso("Las coontraseñas nuevas no son iguales").show();
					return ;
				}
				log.info("contraseniaAnterior: " + contraseniaAnterior);
				log.info("contraseniaNuevaUno: " + contraseniaNuevaUno);
				log.info("contraseniaNuevaDos: " + contraseniaNuevaDos);
				
				servicioUsuarioCliente.cambiarContrasenia(mainContentPresenter.getUsuario().getId(), contraseniaAnterior, contraseniaNuevaUno, new MethodCallback<Usuario>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						new MensajeError("No se pude cambiar contraseña", exception).show();
					}
					@Override
					public void onSuccess(Method method, Usuario response) {
						new MensajeExito("Se cambio exitosamente su contraseña");
					}
				});
			}
		});
	}
}
