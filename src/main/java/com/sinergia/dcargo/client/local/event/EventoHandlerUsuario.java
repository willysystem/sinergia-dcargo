package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface EventoHandlerUsuario extends EventHandler {
	
   void onLogin(EventoUsuario event);
   
}