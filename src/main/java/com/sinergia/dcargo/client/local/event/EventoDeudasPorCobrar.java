package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoDeudasPorCobrar extends GwtEvent<EventoHandlerDeudasPorCobrar> {

	public static Type<EventoHandlerDeudasPorCobrar> TYPE = new Type<EventoHandlerDeudasPorCobrar>();

	@Override
	public Type<EventoHandlerDeudasPorCobrar> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerDeudasPorCobrar handler) {
		handler.onLogin(this);
	}
}