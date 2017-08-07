package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoMovimiento extends GwtEvent<EventoHandlerMovimiento> {

	public static Type<EventoHandlerMovimiento> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerMovimiento> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerMovimiento handler) {
		handler.ejecutar(this);
	}
}