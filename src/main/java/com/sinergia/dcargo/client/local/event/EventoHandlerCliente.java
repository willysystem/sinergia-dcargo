package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerCliente extends EventHandler {
	
   void onLogin(EventoCliente event);
   
}