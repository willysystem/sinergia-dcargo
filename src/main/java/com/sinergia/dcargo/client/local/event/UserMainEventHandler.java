package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.EventHandler;

public interface UserMainEventHandler extends EventHandler {
	
   void onLogin(UserMainEvent event);
   
}