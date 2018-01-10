package com.sinergia.dcargo.client.local.presenter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.view.ConocimientoAccion;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;

@Singleton
public class PresentadorConocimientoNuevo implements Presenter {

	@Inject
	public Display display;

	@Inject
	private Logger log;
		
	public interface Display {
		public void mostrar(ConocimientoAccion conocimientoAccion, final Conocimiento conocimiento);
		public void setIsDialog(Boolean isDialog);
	}
	
	public PresentadorConocimientoNuevo() {
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
		display.mostrar(ConocimientoAccion.NUEVO, null);
	}

	
}
