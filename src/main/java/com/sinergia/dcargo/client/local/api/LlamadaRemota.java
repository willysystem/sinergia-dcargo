package com.sinergia.dcargo.client.local.api;

import javax.inject.Inject;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.view.Cargador;

public abstract class LlamadaRemota<R> implements MethodCallback<R> {
	
	@Inject
	protected Cargador cargador;
	
//	@Inject
//	protected Logger log;

	protected String mensajeError;
	protected boolean ocultarCargador;
	
	public LlamadaRemota(String mensajeError, boolean ocultarCargador) {
		this.mensajeError = mensajeError;
		this.ocultarCargador = ocultarCargador;
	}
	
	@Override
	public void onFailure(Method method, Throwable exception) {
		GWT.log(mensajeError + ": " + exception.getMessage());
		new MensajeError(mensajeError, exception).show();
		if(ocultarCargador) cargador.hide();
	}

}
