package com.sinergia.dcargo.client.local.presenter;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.view.MovimientoAccion;

public abstract interface PresenterMovimientoBuscar extends Presenter {
	
  public abstract void go(final HasWidgets container, MovimientoAccion movimientoAccion);
  
}