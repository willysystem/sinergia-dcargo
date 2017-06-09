package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoCambiarContrasenia extends GwtEvent<EventoHandlerCambiarContrasenia> {

	public static Type<EventoHandlerCambiarContrasenia> TYPE = new Type<EventoHandlerCambiarContrasenia>();

	@Override
	public Type<EventoHandlerCambiarContrasenia> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerCambiarContrasenia handler) {
		handler.onCambioContrasenia(this);
	}
}