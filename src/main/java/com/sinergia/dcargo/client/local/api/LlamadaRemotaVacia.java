package com.sinergia.dcargo.client.local.api;

import org.fusesource.restygwt.client.Method;

public class LlamadaRemotaVacia<R> extends LlamadaRemota<R>{

	public LlamadaRemotaVacia(String mensajeError, boolean ocultarCargador) {
		super(mensajeError, ocultarCargador);
	}

	@Override
	public void onSuccess(Method method, R response) {
		// cargador.fijarEstadoGuiaCargado();
	}

}
