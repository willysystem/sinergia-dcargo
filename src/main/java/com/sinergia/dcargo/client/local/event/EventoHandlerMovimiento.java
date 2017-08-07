package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerMovimiento extends EventHandler {
	
   void ejecutar(EventoMovimiento event);
   
}