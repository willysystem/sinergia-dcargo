package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerIngresoBusqueda extends EventHandler {
	
   void ejecutar(EventoIngresoBusqueda event);
   
}