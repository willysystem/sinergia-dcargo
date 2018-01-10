package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoIngresoBusqueda extends GwtEvent<EventoHandlerIngresoBusqueda> {

	public static Type<EventoHandlerIngresoBusqueda> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerIngresoBusqueda> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerIngresoBusqueda handler) {
		handler.ejecutar(this);
	}
}