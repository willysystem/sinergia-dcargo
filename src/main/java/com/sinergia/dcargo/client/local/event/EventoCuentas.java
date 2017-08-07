package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoCuentas extends GwtEvent<EventoHandlerCuentas> {

	public static Type<EventoHandlerCuentas> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerCuentas> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerCuentas handler) {
		handler.ejecutar(this);
	}
}