package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerCuentas extends EventHandler {
	
   void ejecutar(EventoCuentas event);
   
}