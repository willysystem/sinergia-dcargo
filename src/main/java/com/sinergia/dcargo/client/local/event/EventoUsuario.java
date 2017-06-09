package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoUsuario extends GwtEvent<EventoHandlerUsuario> {

	public static Type<EventoHandlerUsuario> TYPE = new Type<EventoHandlerUsuario>();

	@Override
	public Type<EventoHandlerUsuario> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerUsuario handler) {
		handler.onLogin(this);
	}
}