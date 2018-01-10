package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoGuiaNuevo extends GwtEvent<EventoHandlerGuiaNuevo> {

	public static Type<EventoHandlerGuiaNuevo> TYPE = new Type<EventoHandlerGuiaNuevo>();

	@Override
	public Type<EventoHandlerGuiaNuevo> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerGuiaNuevo handler) {
		handler.onLogin(this);
	}
}