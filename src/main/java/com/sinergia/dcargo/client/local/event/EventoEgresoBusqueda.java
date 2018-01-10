package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoEgresoBusqueda extends GwtEvent<EventoHandlerEgresoBusqueda> {

	public static Type<EventoHandlerEgresoBusqueda> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerEgresoBusqueda> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerEgresoBusqueda handler) {
		handler.ejecutar(this);
	}
}