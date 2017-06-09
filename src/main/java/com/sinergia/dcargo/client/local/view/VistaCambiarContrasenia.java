package com.sinergia.dcargo.client.local.view;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.local.presenter.PresentadorCambioContrasenia;

/**
 * @author willy
 */
@Singleton
public class VistaCambiarContrasenia implements PresentadorCambioContrasenia.Display {

	private Button bottonCambiar = new Button("Guardar");
	
	private PasswordTextBox textBoxContraseniaAnterior = new PasswordTextBox();
	private PasswordTextBox textBoxContraseniaNuevaUno = new PasswordTextBox();
	private PasswordTextBox textBoxContraseniaNuevaDos = new PasswordTextBox();
	
	@Inject
	protected MainContentPresenter.Display mainContentView;
	
	public VistaCambiarContrasenia() {
		
	}
	
	@Override
	public void viewIU() {
		
		// Create a table to layout the form options
	    FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

	    // Add a title to the form
	    layout.setHTML(0, 0, "Cambio de Contraseña");
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

	    // Add some standard form options
	    layout.setHTML(1, 0, "Anterios contraseña");
	    layout.setWidget(1, 1, textBoxContraseniaAnterior);
	    layout.setHTML(2, 0, "Nueva contraseña");
	    layout.setWidget(2, 1, textBoxContraseniaNuevaUno);
	    layout.setHTML(3, 0, "Reitere nueva conseña");
	    layout.setWidget(3, 1, textBoxContraseniaNuevaDos);
	    layout.setWidget(4, 1, bottonCambiar);
	    // Wrap the content in a DecoratorPanel
	    DecoratorPanel decPanel = new DecoratorPanel();
	    decPanel.setWidget(layout);
		
		mainContentView.getCentralPanel().add(decPanel);
	}

	@Override
	public HasClickHandlers getBottonCambiar() {
		return bottonCambiar;
	}

	@Override
	public String getContraseniaNuevaUno() {
		return textBoxContraseniaNuevaUno.getValue();
	}

	@Override
	public String getContraseniaNuevaDos() {
		return textBoxContraseniaNuevaDos.getValue();
	}

	@Override
	public String getContraseniaAnterior() {
		return textBoxContraseniaAnterior.getValue();
	}

}