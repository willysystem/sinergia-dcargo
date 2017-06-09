package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoCliente extends GwtEvent<EventoHandlerCliente> {

	public static Type<EventoHandlerCliente> TYPE = new Type<EventoHandlerCliente>();

	@Override
	public Type<EventoHandlerCliente> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerCliente handler) {
		handler.onLogin(this);
	}
}