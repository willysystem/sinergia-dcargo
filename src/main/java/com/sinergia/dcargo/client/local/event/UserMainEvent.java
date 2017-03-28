package com.sinergia.dcargo.client.local.event;

import com.google.gwt.event.shared.GwtEvent;

public class UserMainEvent extends GwtEvent<UserMainEventHandler> {

	public static Type<UserMainEventHandler> TYPE = new Type<UserMainEventHandler>();

	@Override
	public Type<UserMainEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserMainEventHandler handler) {
		handler.onLogin(this);
	}
}