package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerEgresoBusqueda extends EventHandler {
	
   void ejecutar(EventoEgresoBusqueda event);
   
}