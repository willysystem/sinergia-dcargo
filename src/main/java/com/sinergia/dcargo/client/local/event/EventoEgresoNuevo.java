package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoEgresoNuevo extends GwtEvent<EventoHandlerEgresoNuevo> {

	public static Type<EventoHandlerEgresoNuevo> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerEgresoNuevo> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerEgresoNuevo handler) {
		handler.ejecutar(this);
	}
}