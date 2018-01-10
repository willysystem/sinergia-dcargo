package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoConocimientoNuevo extends GwtEvent<EventoHandlerConocimientoNuevo> {

	public static Type<EventoHandlerConocimientoNuevo> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerConocimientoNuevo> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerConocimientoNuevo handler) {
		handler.onLogin(this);
	}
}