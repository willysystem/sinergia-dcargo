package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoIngresoNuevo extends GwtEvent<EventoHandlerIngresoNuevo> {

	public static Type<EventoHandlerIngresoNuevo> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerIngresoNuevo> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerIngresoNuevo handler) {
		handler.ejecutar(this);
	}
}