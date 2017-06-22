package com.sinergia.dcargo.client.local.view;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

public class FlexTableForm extends FlexTable {

	private Widget titulo;
	private Widget box;
	
	public FlexTableForm(Widget titulo, Widget box){
		super();
		this.titulo = titulo;
		this.box = box;
		//add(titulo);
		//add(box);
		setWidget(0, 0, titulo); getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		setWidget(0, 1, box);    getCellFormatter().setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		//setWidth("100px");
		setCellSpacing(0);
		setCellPadding(0);
		
	}

	public Widget getTitulo() {
		return titulo;
	}

	public void setTitulo(Widget titulo) {
		this.titulo = titulo;
	}

	public Widget getBox() {
		return box;
	}

	public void setBox(Widget box) {
		this.box = box;
	}
	
}
