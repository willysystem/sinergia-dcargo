package com.sinergia.dcargo.client.local.view;

import javax.inject.Singleton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

@Singleton
public class Cargador extends PopupPanel {
	
	public Cargador(){
		super(false, true);
		
		Image image = new Image(AppBundle.INSTANCE.loading());
		image.setPixelSize(32, 32);
		//VerticalPanel vp = new VerticalPanel();
		//vp.add(image);
		//vp.add(new HTML("<center><h4>Un Momento ...</h4></center>"));
		//add(vp);
		setWidget(image);
		setGlassEnabled(true); 
	}
	
	public interface AppBundle extends ClientBundle {

	    @Source("ajax-loader.gif")
	    ImageResource loading();

	    public static final AppBundle INSTANCE = GWT.create(AppBundle.class);

	}
}
