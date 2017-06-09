package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoGuia extends GwtEvent<EventoHandlerGuia> {

	public static Type<EventoHandlerGuia> TYPE = new Type<EventoHandlerGuia>();

	@Override
	public Type<EventoHandlerGuia> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerGuia handler) {
		handler.onLogin(this);
	}
}