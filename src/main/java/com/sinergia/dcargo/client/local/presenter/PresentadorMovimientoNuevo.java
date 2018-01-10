package com.sinergia.dcargo.client.local.presenter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.view.MovimientoAccion;
import com.sinergia.dcargo.client.shared.dominio.Movimiento;

@Singleton
public class PresentadorMovimientoNuevo implements Presenter /*PresenterMovimientoNuevo*/ {

	@Inject
	public Display display;

	@Inject
	private Logger log;
		
	public interface Display {
		public void mostrar(MovimientoAccion conocimientoAccion, final Movimiento conocimiento);
		public void setIsDialog(Boolean isDialog);
	}
	
	public PresentadorMovimientoNuevo() {
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
		display.setIsDialog(false);
		display.mostrar(MovimientoAccion.NUEVO_MOVIMIENTO, null);
	}

//	@Override
//	public void go(HasWidgets container, MovimientoAccion movimientoAccion) {
//		log.info(this.getClass().getSimpleName() + ".go()" );
//		display.setIsDialog(false);
//		display.mostrar(movimientoAccion, null);
//	}

	
}
