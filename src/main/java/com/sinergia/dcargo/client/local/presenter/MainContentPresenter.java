package com.sinergia.dcargo.client.local.presenter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.ServicioUsuarioCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.shared.dominio.Usuario;
import com.sinergia.dcargo.client.shared.dto.DateParam;

@Singleton
public class MainContentPresenter implements Presenter {

	@Inject
	private Logger log;
	
	@Inject
	private AdminParametros adminParametros;
	
	//@Inject
	private ServicioUsuarioCliente userService = GWT.create(ServicioUsuarioCliente.class);
	
	@Inject
	private MensajeAviso mensajeAviso;
	
	private Usuario usuario;
	
	public Usuario getUsuario() {
		return usuario;
	}

	public interface Display {
		void showMainContent(HasWidgets container);
		HasWidgets getCentralPanel();
		void setCurrentUser(Usuario user);
		void setCurrentDate(String date);
	}

	public MainContentPresenter() {
		GWT.log(this.getClass().getSimpleName() + "()");
	}
	
	@PostConstruct
	public void postConstruct() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}
	
	@AfterInitialization
	public void after() {
		log.info("@AfterInitialization: " + MainContentPresenter.class.getSimpleName());
	}
	
	@Inject
	private Display display;
	
	@Override
	public void go(HasWidgets container) {
		
		//bind();
		log.info("go: " + MainContentPresenter.class);
		
		display.showMainContent(container); 
		
		userService.getCurrentUser(new MethodCallback<Usuario>() {
			@Override
			public void onSuccess(Method method, Usuario response) {
				log.info("user: " + response);
				usuario = response;
				adminParametros.setUsuario(usuario);
				display.setCurrentUser(response);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				mensajeAviso.mostrar("Error obtener el usuario actual");
			}
		});
		
		userService.getSeverDate(new MethodCallback<DateParam>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				log.error("" + exception.getMessage());
				log.error("" + exception.getCause().getMessage());
			}
			@Override
			public void onSuccess(Method method, DateParam response) {
				log.info("getSeverDate(): " + response.getFormattedValue());
				adminParametros.setDateParam(response);
				display.setCurrentDate(response.getFormattedValue());
			}
		});
		
	}

}
