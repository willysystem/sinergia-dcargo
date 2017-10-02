package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoHome extends GwtEvent<EventoHandlerHome> {

	public static Type<EventoHandlerHome> TYPE = new Type<EventoHandlerHome>();

	@Override
	public Type<EventoHandlerHome> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerHome handler) {
		handler.onLogin(this);
	}
}