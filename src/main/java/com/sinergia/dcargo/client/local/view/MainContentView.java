package com.sinergia.dcargo.client.local.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.sinergia.dcargo.client.local.event.EventoCambiarContrasenia;
import com.sinergia.dcargo.client.local.event.EventoCliente;
import com.sinergia.dcargo.client.local.event.EventoConocimiento;
import com.sinergia.dcargo.client.local.event.EventoCuentas;
import com.sinergia.dcargo.client.local.event.EventoGuia;
import com.sinergia.dcargo.client.local.event.EventoMovimiento;
import com.sinergia.dcargo.client.local.event.EventoTransportista;
import com.sinergia.dcargo.client.local.event.EventoUsuario;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.shared.Usuario;

/**
 * @author Willy Hurtado Vela 
 *         willysystems@gmail.com
 */
@Singleton
public class MainContentView extends ResizeComposite implements MainContentPresenter.Display {

	@Inject
	private HandlerManager eventBus;
	
	@Inject
	private Logger log;
	
	private SimplePanel panelCentral;
	
	private DockPanel mainContainer = new DockPanel();
	
	private Label userLabelValue = new Label();
	private Label placeLabelValue = new Label();
	private Label dateLabelValue = new Label();
	private Label fullNameLabelValue = new Label();
	
	public MainContentView() {
		GWT.log(MainContentView.class.getSimpleName() + "()");
	}
	
	@PostConstruct
	public void postContruct() {
		log.info("@PostConstruct: " + MainContentView.class.getSimpleName());
	}
	
	@AfterInitialization
	public void init(){
	    log.info("@AfterInitialization: " + MainContentView.class.getSimpleName());
	}

	@Override
	public void showMainContent(HasWidgets container) {
		
		mainContainer.setWidth("100%");
		mainContainer.setHeight("100%");
		mainContainer.setSpacing(2);
		
		mainContainer.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		mainContainer.add(getMainMenu(), DockPanel.NORTH);
		mainContainer.add(getContentBody() , DockPanel.CENTER);
		mainContainer.add(getStatusBar(), DockPanel.SOUTH);
	    container.add(mainContainer);
	    
	}
	
	private MenuBar getMainMenu() {
		
	    Command menuCommand = new Command() {
		      public void execute() {
		    	  
		      }
		};
		 
		MenuBar menu = new MenuBar();
	    menu.setAutoOpen(true);
	    menu.setAnimationEnabled(true);
	   
	    MenuBar guiasMenuBar = new MenuBar(true);
	    guiasMenuBar.setAnimationEnabled(true);
	   
	    // Guías
	    MenuItem menuGuia = new MenuItem("Guías", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoGuia());
			}
		});
	    menu.addItem(menuGuia);
	    
	    // Conocimiento
	    MenuItem conocimientoMenuBar =  new MenuItem("Conocimiento", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoConocimiento());
			}
		});
	    menu.addItem(conocimientoMenuBar);
	    
	    // Liquidaciones
	    MenuItem cargaLiquidacion = new MenuItem("Liquidación de carga", menuCommand);
	    MenuItem deudasLiquidacion = new MenuItem("Deudas por cobrar", menuCommand);
	    MenuItem actualizarNotaEntregaLiquidacion = new MenuItem("Actualizar nota de entrega", menuCommand);
	    
	    MenuBar liquidacionesMenuBar = new MenuBar(true);
	    liquidacionesMenuBar.setAnimationEnabled(true);
	    liquidacionesMenuBar.addItem(cargaLiquidacion);
	    liquidacionesMenuBar.addItem(deudasLiquidacion);
	    liquidacionesMenuBar.addItem(actualizarNotaEntregaLiquidacion);
	    menu.addItem(new MenuItem("Liquidaciones", liquidacionesMenuBar));
	    
	    // Caja
	    MenuItem cajaMenuBar =  new MenuItem("Caja", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoMovimiento());
			}
		});
	    menu.addItem(cajaMenuBar);
	    
	    // Registro de Datos
	    MenuBar registroDatosMenuBar = new MenuBar(true);
	    MenuItem clientesRegistroDatos = new MenuItem("Clientes", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoCliente());
			}
		});
	    MenuItem transportistasRegistroDatos = new MenuItem("Transportistas",  new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoTransportista());
			}
		});
	    MenuItem cuentasIngresoRegistroDatos = new MenuItem("Cuentas de ingreso y egreso", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoCuentas());
			}
		});
	    
	    registroDatosMenuBar.setAnimationEnabled(true);
	    registroDatosMenuBar.addItem(clientesRegistroDatos);
	    registroDatosMenuBar.addItem(transportistasRegistroDatos);
	    registroDatosMenuBar.addItem(cuentasIngresoRegistroDatos);
	    menu.addItem(new MenuItem("Registro de Datos", registroDatosMenuBar));
	    
	    // Administración
	    MenuBar admMenuBar = new MenuBar(true);
	    
	    MenuItem usuariosAdministracion = new MenuItem("Usuarios", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoUsuario());
			}
		});
	    //MenuItem fechaAdministracion = new MenuItem("Actualizar fecha del sistema", menuCommand);
	    MenuItem contrasenaAdministracion = new MenuItem("Cambio de contraseña", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoCambiarContrasenia());
			}
		});
	    MenuItem salirAdministracion = new MenuItem("Salir", new Command() {
		      													  public void execute() {
		      														 Window.open("logout", "_self", null);
		      													  }
															  });
	    admMenuBar.setAnimationEnabled(true);
	    admMenuBar.addItem(usuariosAdministracion);
	    //admMenuBar.addItem(fechaAdministracion);
	    admMenuBar.addItem(contrasenaAdministracion);
	    admMenuBar.addItem(salirAdministracion);
	    menu.addItem(new MenuItem("Administración", admMenuBar));
	    
	    return menu;
	}

	public SimplePanel getCentralPanel(){
		if(panelCentral != null) {
			panelCentral.clear();
		}
		
		return panelCentral;
	}
	
	private Widget getContentBody() {
		
		final int minusCentralPanel = 100;
		
		Window.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				int height = Window.getClientHeight()-minusCentralPanel;
				panelCentral.setHeight(height + "px");
			}
		});
		
		int height = Window.getClientHeight()-minusCentralPanel;
		
		// Panel central
		panelCentral = new ScrollPanel();
		//panelCentral.setStyleName("cw-DockPanel");
		panelCentral.setHeight(height + "px");
		
		// Scroll
		//ScrollPanel scroller = new ScrollPanel(panelCentral);
		
		
		Image logoDCargo = new Image();
		GWT.log("GWT.getHostPageBaseURL(): " + GWT.getHostPageBaseURL());
		logoDCargo.setUrl(GWT.getHostPageBaseURL() + "images/dcargo_logo.png");
		
		panelCentral.add(logoDCargo);
		
		return panelCentral;
		
	}
	
	private HorizontalPanel getStatusBar() {
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		
		// user 
		HorizontalPanel  userHorizontalPanel = new HorizontalPanel();
		userHorizontalPanel.setWidth("150px");
		//userHorizontalPanel.setBorderWidth(1);
		Label userLabel = new Label("Usuario: ");
		userLabel.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		userLabel.setStyleName("labelStatus");
		userLabelValue.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		userHorizontalPanel.add(userLabel);
		userHorizontalPanel.add(new HTML("&nbsp;"));
		userHorizontalPanel.add(userLabelValue);
		horizontalPanel.add(userHorizontalPanel);
		
		// Full name
		HorizontalPanel  fullNameHorizontalPanel = new HorizontalPanel();
		fullNameHorizontalPanel.setWidth("300px");
		//fullNameHorizontalPanel.setBorderWidth(1);
		Label fullNameLabel = new Label("Nombre: ");
		fullNameLabel.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		fullNameLabel.setStyleName("labelStatus");
		fullNameLabelValue.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		fullNameHorizontalPanel.add(fullNameLabel);
		fullNameHorizontalPanel.add(new HTML("&nbsp;"));
		fullNameHorizontalPanel.add(fullNameLabelValue);
		horizontalPanel.add(fullNameHorizontalPanel);
		
		// Place
		HorizontalPanel placeHorizontalPanel =  new HorizontalPanel();
		placeHorizontalPanel.setWidth("230px");
		//placeHorizontalPanel.setBorderWidth(1);
		Label placeLabel = new Label("Lugar de trabajo: ");
		placeLabel.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		placeLabel.setStyleName("labelStatus");
		placeLabelValue.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		placeHorizontalPanel.add(placeLabel);
		placeHorizontalPanel.add(new HTML("&nbsp;"));
		placeHorizontalPanel.add(placeLabelValue);
		horizontalPanel.add(placeHorizontalPanel);
		
		// Date
		HorizontalPanel dateHorizontalPanel =  new HorizontalPanel();
		dateHorizontalPanel.setWidth("180px");
		//dateHorizontalPanel.setBorderWidth(1);
		Label dateLabel = new Label("Fecha: ");
		dateLabel.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		dateLabel.setStyleName("labelStatus");
		dateLabelValue.setAutoHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		dateHorizontalPanel.add(dateLabel);
		dateHorizontalPanel.add(new HTML("&nbsp;"));
		dateHorizontalPanel.add(dateLabelValue);
		horizontalPanel.add(dateHorizontalPanel);
		
		return horizontalPanel;
		
	}

	@Override
	public void setCurrentUser(Usuario user) {
		userLabelValue.setText(user.getNombreUsuario());
		fullNameLabelValue.setText(user.getNombres() + " " + user.getApellidos());
		//log.info("user.getOffice(): " + user.getOffice());
		placeLabelValue.setText(user.getOffice().getNombre());
	}

	@Override
	public void setCurrentDate(String date) {
		dateLabelValue.setText(date);
		
	}
	
}
