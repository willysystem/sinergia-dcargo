package com.sinergia.dcargo.client.local.view;

import javax.inject.Singleton;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.inject.Inject;
import com.sinergia.dcargo.client.local.presenter.PresentadorCuentas;

/**
 * 
 * @author Willy
 */
@Singleton
public class VistaCuentas implements PresentadorCuentas.Display  {

	@Inject MainContentView mainContentView;
	@Inject VistaCuentasIngreso vistaCuentasIngreso;
	@Inject VistaCuentasEgreso  vistaCuentasEgreso;
	public VistaCuentas() { }
	
	@Override
	public void viewIU() {
		
		HorizontalPanel hp = new HorizontalPanel();
		//hp.setSpacing(20);
		hp.add(vistaCuentasIngreso.viewIU());
		hp.add(new HTML("<pre>  </pre>"));
		hp.add(vistaCuentasEgreso.viewIU());
		
		mainContentView.getCentralPanel().add(hp);
		
	}

}
