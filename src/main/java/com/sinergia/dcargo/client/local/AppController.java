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
import com.sinergia.dcargo.client.local.event.EventoConocimientoNuevo;
import com.sinergia.dcargo.client.local.event.EventoCuentas;
import com.sinergia.dcargo.client.local.event.EventoDeudasPorCobrar;
import com.sinergia.dcargo.client.local.event.EventoEgresoNuevo;
import com.sinergia.dcargo.client.local.event.EventoGuia;
import com.sinergia.dcargo.client.local.event.EventoGuiaNuevo;
import com.sinergia.dcargo.client.local.event.EventoHome;
import com.sinergia.dcargo.client.local.event.EventoIngresoBusqueda;
import com.sinergia.dcargo.client.local.event.EventoIngresoNuevo;
import com.sinergia.dcargo.client.local.event.EventoLiquidacionCarga;
import com.sinergia.dcargo.client.local.event.EventoMovimiento;
import com.sinergia.dcargo.client.local.event.EventoTransportista;
import com.sinergia.dcargo.client.local.event.EventoUsuario;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.local.presenter.PresentadorCambioContrasenia;
import com.sinergia.dcargo.client.local.presenter.PresentadorClientes;
import com.sinergia.dcargo.client.local.presenter.PresentadorConocimiento;
import com.sinergia.dcargo.client.local.presenter.PresentadorConocimientoNuevo;
import com.sinergia.dcargo.client.local.presenter.PresentadorCuentas;
import com.sinergia.dcargo.client.local.presenter.PresentadorDeudasPorCobrar;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuiaNuevo;
import com.sinergia.dcargo.client.local.presenter.PresentadorLiquidacionCarga;
import com.sinergia.dcargo.client.local.presenter.PresentadorMovimiento;
import com.sinergia.dcargo.client.local.presenter.PresentadorMovimientoNuevo;
import com.sinergia.dcargo.client.local.presenter.PresentadorTransportistas;
import com.sinergia.dcargo.client.local.presenter.Presenter;
import com.sinergia.dcargo.client.local.presenter.PresenterMovimientoBuscar;
import com.sinergia.dcargo.client.local.presenter.PresenterMovimientoNuevo;
import com.sinergia.dcargo.client.local.presenter.UserPresenter;
import com.sinergia.dcargo.client.local.view.MovimientoAccion;

@ApplicationScoped
public class AppController implements com.sinergia.dcargo.client.local.presenter.Presenter, ValueChangeHandler<String> {

	@Inject private HandlerManager eventBus;
	@Inject private Logger log;
	@Inject private MainContentPresenter mainContentPresenter;
	@Inject private UserPresenter userMainPresenter;
	@Inject	private PresentadorCambioContrasenia preCambioContrasenia;
	@Inject	private PresentadorClientes presentadorClientes;
	@Inject	private PresentadorGuia presentadorGuia;
	@Inject	private PresentadorGuiaNuevo presentadorGuiaNuevo;
	@Inject	private PresentadorConocimiento presentadorConocimiento;
	@Inject	private PresentadorConocimientoNuevo presentadorConocimientoNuevo;
	@Inject	private PresentadorTransportistas presentadorTransportistas;
	@Inject	private PresentadorCuentas presentadorCuentas;
	//@Inject	private PresentadorMovimiento presentadorMovimiento;
	@Inject	private PresentadorLiquidacionCarga presentadorLiquidacionCarga;
	@Inject	private PresentadorDeudasPorCobrar presentadorDeudasPorCobrar;
	@Inject	private PresentadorMovimientoNuevo presentadorMovimientoNuevo;
	@Inject	private PresentadorMovimiento presentadorMovimiento;

	
	
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
		
		eventBus.addHandler(EventoHome.TYPE, e -> History.newItem("home"));
		eventBus.addHandler(EventoUsuario.TYPE, e -> History.newItem("users")); 
		eventBus.addHandler(EventoCambiarContrasenia.TYPE, e -> History.newItem("contrasenia")); 
		eventBus.addHandler(EventoCliente.TYPE, e -> History.newItem("clientes"));
		eventBus.addHandler(EventoGuia.TYPE, e -> History.newItem("guias"));
		eventBus.addHandler(EventoGuiaNuevo.TYPE, e -> History.newItem("guiasNuevo"));
		eventBus.addHandler(EventoConocimiento.TYPE, e -> History.newItem("conocimiento"));
		eventBus.addHandler(EventoConocimientoNuevo.TYPE, e -> History.newItem("conocimientoNuevo"));
		eventBus.addHandler(EventoTransportista.TYPE, e -> History.newItem("transportista"));
		eventBus.addHandler(EventoCuentas.TYPE, e -> History.newItem("cuentas"));
		eventBus.addHandler(EventoIngresoNuevo.TYPE, e -> History.newItem("ingresoNuevo"));
		eventBus.addHandler(EventoIngresoBusqueda.TYPE, e -> History.newItem("ingresoBusqueda"));
		eventBus.addHandler(EventoEgresoNuevo.TYPE, e -> History.newItem("egresoNuevo"));
		eventBus.addHandler(EventoLiquidacionCarga.TYPE, e -> History.newItem("liquidacionCarga"));
		eventBus.addHandler(EventoDeudasPorCobrar.TYPE, e -> History.newItem("deudasporcobrar"));
		
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
			} else if (token.equals("guiasNuevo")) {
				presenter = presentadorGuiaNuevo;
			} else if (token.equals("conocimiento")) {
				presenter = presentadorConocimiento;
			} else if (token.equals("conocimientoNuevo")) {
				presenter = presentadorConocimientoNuevo;
			} else if (token.equals("Nuevo")) {
				presenter = presentadorConocimientoNuevo;
			} else if (token.equals("transportista")) {
				presenter = presentadorTransportistas;
			} else if (token.equals("cuentas")) {
				presenter = presentadorCuentas;
			} else if (token.equals("ingresoNuevo")) {
				presenter = presentadorMovimientoNuevo;
//				PresenterMovimientoNuevo presenterMov = (PresenterMovimientoNuevo) presentadorIngresoNuevo;
//				presenterMov.go(container, MovimientoAccion.NUEVO_INGRESO);
//				return;
			} else if (token.equals("ingresoBusqueda")) {
				presenter = presentadorMovimiento;
//				PresenterMovimientoBuscar presenterMov = (PresenterMovimientoBuscar) presentadorMovimiento;
//				presenterMov.go(container, MovimientoAccion.NUEVO_INGRESO);
//				return;
				
			} else if (token.equals("liquidacionCarga")) {
				presenter = presentadorLiquidacionCarga;
			}  else if (token.equals("deudasporcobrar")) {
				presenter = presentadorDeudasPorCobrar;
			}
			if (presenter != null) {
				presenter.go(container);
			}
		}
	}
}