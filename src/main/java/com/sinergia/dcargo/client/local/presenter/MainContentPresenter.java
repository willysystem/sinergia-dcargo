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
import com.sinergia.dcargo.client.shared.DateParam;
import com.sinergia.dcargo.client.shared.Usuario;

@Singleton
public class MainContentPresenter implements Presenter {

	@Inject
	private Logger log;
	
	@Inject
	private AdminParametros adminParametros;
	
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
	
	private ServicioUsuarioCliente userServiceClient;

	public MainContentPresenter() {
		GWT.log(this.getClass().getSimpleName() + "()");
	}
	
	@PostConstruct
	public void postConstruct() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
		userServiceClient = GWT.create(ServicioUsuarioCliente.class);
	}
	
	@AfterInitialization
	public void after() {
		log.info("@AfterInitialization: " + MainContentPresenter.class.getSimpleName());
		userServiceClient = GWT.create(ServicioUsuarioCliente.class);
	}
	
	@Inject
	private Display display;
	
	@Override
	public void go(HasWidgets container) {
		
		//bind();
		
		display.showMainContent(container); 
		
		userServiceClient.getCurrentUser(new MethodCallback<Usuario>() {
			@Override
			public void onSuccess(Method method, Usuario response) {
				usuario = response;
				display.setCurrentUser(response);
			}
			@Override
			public void onFailure(Method method, Throwable exception) {
				
			}
		});
		
		userServiceClient.getSeverDate(new MethodCallback<DateParam>() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				log.error("" + exception.getMessage());
				log.error("" + exception.getCause().getMessage());
			}
			@Override
			public void onSuccess(Method method, DateParam response) {
				log.info("getSeverDate(): " + response.getFormattedValue());
				//DateTimeFormat formatDate = DateTimeFormat.getFormat("EEEE dd/MM/yyyy");
				//String date = formatDate.format(response.getFormattedValue());
				adminParametros.setDateParam(response);
				display.setCurrentDate(response.getFormattedValue());
			}
		});
		
	}

}
