package com.sinergia.dcargo.client.local.view;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VerticalPanelEx extends VerticalPanel {

	public VerticalPanelEx(Widget ... widgets){
		for (Widget widget : widgets) {
			add(widget);
		} 
		setWidth("100%");
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	}
	
}
