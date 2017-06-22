package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoConocimiento extends GwtEvent<EventoHandlerConocimiento> {

	public static Type<EventoHandlerConocimiento> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerConocimiento> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerConocimiento handler) {
		handler.onLogin(this);
	}
}