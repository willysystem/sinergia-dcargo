package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerTransportista extends EventHandler {
	
   void ejecutar(EventoTransportista event);
   
}