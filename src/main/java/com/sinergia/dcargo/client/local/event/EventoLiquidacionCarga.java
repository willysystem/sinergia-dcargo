package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class EventoLiquidacionCarga extends GwtEvent<EventoHandlerLiquidacionjCarga> {

	public static Type<EventoHandlerLiquidacionjCarga> TYPE = new Type<EventoHandlerLiquidacionjCarga>();

	@Override
	public Type<EventoHandlerLiquidacionjCarga> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(EventoHandlerLiquidacionjCarga handler) {
		handler.onLogin(this);
	}
}