package com.sinergia.dcargo.client.local;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.event.EventoCambiarContrasenia;
import com.sinergia.dcargo.client.local.event.EventoCliente;
import com.sinergia.dcargo.client.local.event.EventoConocimiento;
import com.sinergia.dcargo.client.local.event.EventoCuentas;
import com.sinergia.dcargo.client.local.event.EventoGuia;
import com.sinergia.dcargo.client.local.event.EventoMovimiento;
import com.sinergia.dcargo.client.local.event.EventoTransportista;
import com.sinergia.dcargo.client.local.event.EventoUsuario;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.local.presenter.PresentadorCambioContrasenia;
import com.sinergia.dcargo.client.local.presenter.PresentadorClientes;
import com.sinergia.dcargo.client.local.presenter.PresentadorConocimiento;
import com.sinergia.dcargo.client.local.presenter.PresentadorCuentas;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia;
import com.sinergia.dcargo.client.local.presenter.PresentadorMovimiento;
import com.sinergia.dcargo.client.local.presenter.PresentadorTransportistas;
import com.sinergia.dcargo.client.local.presenter.Presenter;
import com.sinergia.dcargo.client.local.presenter.UserPresenter;

@ApplicationScoped
public class AppController implements com.sinergia.dcargo.client.local.presenter.Presenter, ValueChangeHandler<String> {

	@Inject
	private HandlerManager eventBus;

	@Inject
	private Logger log;

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
	@Inject
	private PresentadorConocimiento presentadorConocimiento;
	@Inject
	private PresentadorTransportistas presentadorTransportistas;
	@Inject
	private PresentadorCuentas presentadorCuentas;
	@Inject
	private PresentadorMovimiento presentadorMovimiento;

	private HasWidgets container;

	public AppController() {
		GWT.log(this.getClass().getSimpleName() + "()");
	}

	@PostConstruct
	public void postContruct() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}

	@AfterInitialization
	public void init() {
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
	}

	public void bind() {

		History.addValueChangeHandler(this);
		
		eventBus.addHandler(EventoUsuario.TYPE, e -> History.newItem("users")); 
		eventBus.addHandler(EventoCambiarContrasenia.TYPE, e -> History.newItem("contrasenia")); 
		eventBus.addHandler(EventoCliente.TYPE, e -> History.newItem("clientes"));
		eventBus.addHandler(EventoGuia.TYPE, e -> History.newItem("guias")); 
		eventBus.addHandler(EventoConocimiento.TYPE, e -> History.newItem("conocimiento"));
		eventBus.addHandler(EventoTransportista.TYPE, e -> History.newItem("transportista"));
		eventBus.addHandler(EventoCuentas.TYPE, e -> History.newItem("cuentas"));
		eventBus.addHandler(EventoMovimiento.TYPE, e -> History.newItem("movimientos"));
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
			} else if (token.equals("home")) {
				presenter = mainContentPresenter;
			} else if (token.equals("users")) {
				presenter = userMainPresenter;
			} else if (token.equals("clientes")) {
				presenter = presentadorClientes;
			} else if (token.equals("guias")) {
				presenter = presentadorGuia;
			} else if (token.equals("conocimiento")) {
				presenter = presentadorConocimiento;
			} else if (token.equals("transportista")) {
				presenter = presentadorTransportistas;
			} else if (token.equals("cuentas")) {
				presenter = presentadorCuentas;
			} else if (token.equals("movimientos")) {
				presenter = presentadorMovimiento;
			}

			if (presenter != null) {
				presenter.go(container);
			}
		}
	}
}