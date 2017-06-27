package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoTransportista extends GwtEvent<EventoHandlerTransportista> {

	public static Type<EventoHandlerTransportista> TYPE = new Type<>();

	@Override
	public Type<EventoHandlerTransportista> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerTransportista handler) {
		handler.ejecutar(this);
	}
}