package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerIngresoNuevo extends EventHandler {
	
   void ejecutar(EventoIngresoNuevo event);
   
}