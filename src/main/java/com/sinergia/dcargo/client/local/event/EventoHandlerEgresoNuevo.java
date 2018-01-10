package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerEgresoNuevo extends EventHandler {
	
   void ejecutar(EventoEgresoNuevo event);
   
}