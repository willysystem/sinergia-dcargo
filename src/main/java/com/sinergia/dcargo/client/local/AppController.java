package com.sinergia.dcargo.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.event.EventoHandlerCambiarContrasenia;
import com.sinergia.dcargo.client.local.event.EventoHandlerCliente;
import com.sinergia.dcargo.client.local.event.EventoHandlerGuia;
import com.sinergia.dcargo.client.local.api.ServicioClienteCliente;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.api.ServicioItemCliente;
import com.sinergia.dcargo.client.local.event.EventoCambiarContrasenia;
import com.sinergia.dcargo.client.local.event.EventoCliente;
import com.sinergia.dcargo.client.local.event.EventoGuia;
import com.sinergia.dcargo.client.local.event.EventoUsuario;
import com.sinergia.dcargo.client.local.event.EventoHandlerUsuario;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.local.presenter.PresentadorCambioContrasenia;
import com.sinergia.dcargo.client.local.presenter.PresentadorClientes;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia;
import com.sinergia.dcargo.client.local.presenter.Presenter;
import com.sinergia.dcargo.client.local.presenter.UserPresenter;

@ApplicationScoped
//@Singleton
public class AppController implements com.sinergia.dcargo.client.local.presenter.Presenter, ValueChangeHandler<String> {

  @Inject
  private HandlerManager eventBus;

  @Inject
  private Logger log;
  
  @Inject
  private AdminParametros adminParametros;
  
  @Inject
  private MainContentPresenter mainContentPresenter;
  @Inject
  private UserPresenter userMainPresenter;
  @Inject
  private PresentadorCambioContrasenia preCambioContrasenia;
  @Inject
  private PresentadorClientes presentadorClientes;
  @Inject
  private PresentadorGuia presentadorGuia;

  
  private HasWidgets container;

  public AppController() {
	  GWT.log(this.getClass().getSimpleName() + "()");
  }
  
  @PostConstruct
  public void postContruct() {
	  log.info("@PostConstruct: " + this.getClass().getSimpleName());
  }
  
  @AfterInitialization
  public void init(){
	  log.info("@AfterInitialization: " + this.getClass().getSimpleName());
  }
  
  public void bind() {
	  
    History.addValueChangeHandler(this);

    eventBus.addHandler(EventoUsuario.TYPE, new EventoHandlerUsuario() {
		@Override
		public void onLogin(EventoUsuario event) {
			History.newItem("users");
		}
	});   
    eventBus.addHandler(EventoCambiarContrasenia.TYPE, new EventoHandlerCambiarContrasenia(){
		@Override
		public void onCambioContrasenia(EventoCambiarContrasenia event) {
			History.newItem("contrasenia");
		}
    });
    eventBus.addHandler(EventoCliente.TYPE, new EventoHandlerCliente() {
		@Override
		public void onLogin(EventoCliente event) {
			History.newItem("clientes");
		}
	});
    eventBus.addHandler(EventoGuia.TYPE, new EventoHandlerGuia() {
		@Override
		public void onLogin(EventoGuia event) {
			History.newItem("guias");
		}
	});
    
  }
  
  private void doHome(){
	  History.newItem("home");  
  }

  public void go(final HasWidgets container) {
    this.container = container;
    bind();
   
    if ("".equals(History.getToken())) {
      History.newItem("home");
    } else {
      History.fireCurrentHistoryState();
    }
  }

  public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();
    log.info("onValueChange: token: " + token);
    
    if (token != null) {
      Presenter presenter = null;

      if (token.equals("contrasenia")) {
    	  presenter = preCambioContrasenia;
        //IOCBeanDef<ContactsPresenter> bean = manager.lookupBean(ContactsPresenter.class);
        //if (bean != null) {
//          presenter = loginPresenter;
        //}
      } else if (token.equals("home")) {
    	  presenter =  mainContentPresenter;
      } else if (token.equals("users")) {
    	  presenter =  userMainPresenter;
      } else if (token.equals("clientes")) {
    	  presenter =  presentadorClientes;
      } else if (token.equals("guias")) {
    	  presenter =  presentadorGuia;
      } 
      
      
//    } else if (token.equals("add") || token.equals("edit")) {
//        IOCBeanDef<EditContactPresenter> bean = manager.lookupBean(EditContactPresenter.class);
//        if (bean != null) {
//          presenter = bean.getInstance();
//        }
//    }
      
      if (presenter != null) {
        presenter.go(container);
      }
    }
    
  }
  
  @Produces
  public ServicioItemCliente servicioItemCliente(){
	  ServicioItemCliente servicioItem = GWT.create(ServicioItemCliente.class);
	  return servicioItem;
  }
  
  @Produces
  public ServicioGuiaCliente servicioGuiaCliente(){
	  ServicioGuiaCliente servicioItem = GWT.create(ServicioGuiaCliente.class);
	  return servicioItem;
  }
  
  @Produces
  public ServicioClienteCliente servicioClienteCliente(){
	  ServicioClienteCliente servicio = GWT.create(ServicioClienteCliente.class);
	  return servicio;
  }
  
  
}