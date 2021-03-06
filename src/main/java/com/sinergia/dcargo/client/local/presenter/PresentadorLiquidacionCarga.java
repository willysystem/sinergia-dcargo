package com.sinergia.dcargo.client.local.presenter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.view.Cargador;
import com.sinergia.dcargo.client.shared.dto.LiquidacionCargaReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionReporte;

@Singleton
public class PresentadorLiquidacionCarga implements Presenter {

	@Inject
	public Display display;

	@Inject
	private Logger log;
	
	@Inject
	private ServicioMovimientoCliente servicioMovimiento;
	
	@Inject
	private Cargador cargador;

	public interface Display {

		void viewIU();
		HasClickHandlers getBuscarButton();
		void cargarDataUI(LiquidacionCargaReporte movimientos);
		LiquidacionReporte getParametrosBusqueda();
		public boolean validar();
	}
	
	public PresentadorLiquidacionCarga() {
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
				if(display.validar()) {
					LiquidacionReporte liquidacionReporte = display.getParametrosBusqueda();
					log.info("liquidacionReporte: "+ liquidacionReporte);
					//cargador.center();
					servicioMovimiento.reporteLiquidacionCarga(liquidacionReporte, new LlamadaRemota<LiquidacionCargaReporte>("No se pudo hallar el reporte", false) {
						@Override
						public void onSuccess(Method method, LiquidacionCargaReporte response) {
							display.cargarDataUI(response);
						//	cargador.hide();
						}
					});
				}
			}
		});
	}
	
}
