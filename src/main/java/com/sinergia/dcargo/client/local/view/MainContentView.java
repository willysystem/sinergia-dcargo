package com.sinergia.dcargo.client.local.view;

import java.util.HashMap;

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
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.event.EventoCambiarContrasenia;
import com.sinergia.dcargo.client.local.event.EventoCliente;
import com.sinergia.dcargo.client.local.event.EventoConocimiento;
import com.sinergia.dcargo.client.local.event.EventoConocimientoNuevo;
import com.sinergia.dcargo.client.local.event.EventoCuentas;
import com.sinergia.dcargo.client.local.event.EventoDeudasPorCobrar;
import com.sinergia.dcargo.client.local.event.EventoEgresoBusqueda;
import com.sinergia.dcargo.client.local.event.EventoEgresoNuevo;
import com.sinergia.dcargo.client.local.event.EventoGuia;
import com.sinergia.dcargo.client.local.event.EventoGuiaNuevo;
import com.sinergia.dcargo.client.local.event.EventoIngresoBusqueda;
import com.sinergia.dcargo.client.local.event.EventoIngresoNuevo;
import com.sinergia.dcargo.client.local.event.EventoLiquidacionCarga;
import com.sinergia.dcargo.client.local.event.EventoMovimiento;
import com.sinergia.dcargo.client.local.event.EventoTransportista;
import com.sinergia.dcargo.client.local.event.EventoUsuario;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.shared.dominio.Aplicacion;
import com.sinergia.dcargo.client.shared.dominio.Usuario;

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
	
	@Inject
	private AdminParametros adminParametros;
	
	private SimplePanel panelCentral;
	
	private DockPanel mainContainer = new DockPanel();
	
	private Label userLabelValue = new Label();
	private Label placeLabelValue = new Label();
	private Label dateLabelValue = new Label();
	private Label fullNameLabelValue = new Label();
	
	private HashMap<String, MenuItem> menus = new HashMap<>();
	
	//@Inject
	//private VistaGuiaAccion vistaGuiaAccion;
	
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
		mainContainer = new DockPanel();
		//mainContainer.clear();
		
		mainContainer.setWidth("100%");
		mainContainer.setHeight("100%");
		mainContainer.setSpacing(2);
		
		mainContainer.setHorizontalAlignment(DockPanel.ALIGN_CENTER);
		mainContainer.add(getMainMenu(), DockPanel.NORTH);
		mainContainer.add(getContentBody() , DockPanel.CENTER);
		mainContainer.add(getStatusBar(), DockPanel.SOUTH);
		
		container.clear();
	    container.add(mainContainer);
		//container.add(getMainMenu());
	    
	}
	
	private MenuBar getMainMenu() {
		 
		MenuBar menu = new MenuBar();
	    menu.setAutoOpen(true);
	    menu.setAnimationEnabled(true);
	    menu.setStyleName("menu");
	       
	    // Guia
	    MenuBar menuBarGuia = new MenuBar(true);
	    menuBarGuia.setAnimationEnabled(true);
	    menuBarGuia.setStyleName("subMenu");
	    
	    // Nueva GUIA 
	    MenuItem nuevaGuiaMenuItem = new MenuItem("Nuevo", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoGuiaNuevo());
				//vistaGuiaAccion.mostrar(GuiaAccion.NUEV	O, null);
			}
		});
	    nuevaGuiaMenuItem.setStyleName("subMenuItem");
	    menuBarGuia.addItem(nuevaGuiaMenuItem);
	    
	    // Buscar GUIA
	    MenuItem buscarGuiaMenuItem = new MenuItem("Buscar", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoGuia());
			}
		});
	    buscarGuiaMenuItem.setStyleName("subMenuItem");
	    menuBarGuia.addItem(buscarGuiaMenuItem);
	    
	    MenuItem menuGuia = new MenuItem("Guia", menuBarGuia);
	    menuGuia.setStylePrimaryName("menu-title");
	    menu.addItem(menuGuia);
	    
	    //// Conocimiento
	    MenuBar menuBarConocmiento = new MenuBar(true);
	    menuBarConocmiento.setAnimationEnabled(true);
	    menuBarConocmiento.setStyleName("subMenu");
	    
	    // Nueva Conocimiento 
	    MenuItem nuevoConocimientoMenuItem = new MenuItem("Nuevo", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoConocimientoNuevo());
			}
		});
	    nuevoConocimientoMenuItem.setStyleName("subMenuItem");
	    menuBarConocmiento.addItem(nuevoConocimientoMenuItem);
	    
	    // Buscar Conocimiento
	    MenuItem buscarConocimientoMenuItem = new MenuItem("Buscar", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoConocimiento());
			}
		});
	    buscarConocimientoMenuItem.setStyleName("subMenuItem");
	    menuBarConocmiento.addItem(buscarConocimientoMenuItem);
	    
	    MenuItem menuConocimiento = new MenuItem("Conocimiento", menuBarConocmiento);
	    menuConocimiento.setStylePrimaryName("menu-title");
	    menuConocimiento.addStyleName("menu-title_2nd");
	    menu.addItem(menuConocimiento);	    
	    
	    //// Caja
//	    MenuItem cajaMenuBar =  new MenuItem("Caja", new Command() {
//			@Override
//			public void execute() {
//				eventBus.fireEvent(new EventoMovimiento());
//			}
//		});
//	    cajaMenuBar.setStylePrimaryName("menu-title");
//	    cajaMenuBar.addStyleName("menu-title_3rd");
//	    menu.addItem(cajaMenuBar);
//	    cajaMenuBar.setVisible(false);
//	    menus.put("caja", cajaMenuBar);
//	    
	    MenuBar menuBarCaja = new MenuBar(true);
	    menuBarCaja.setAnimationEnabled(true);
	    menuBarCaja.setStyleName("subMenu");
	    
	    // Nuevo Ingreso 
	    MenuItem nuevoIngresoMenuItem = new MenuItem("Nuevo Movimiento", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoIngresoNuevo());
			}
		});
	    nuevoIngresoMenuItem.setStyleName("subMenuItem");
	    menuBarCaja.addItem(nuevoIngresoMenuItem);
	    
	    // Buscar Ingreso
	    MenuItem buscarIngresoMenuItem = new MenuItem("Buscar Movimiento", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoIngresoBusqueda());
			}
		});
	    buscarIngresoMenuItem.setStyleName("subMenuItem");
	    menuBarCaja.addItem(buscarIngresoMenuItem);
	    
	    MenuItem menuMovimiento = new MenuItem("Caja", menuBarCaja);
	    menuMovimiento.setStylePrimaryName("menu-title");
	    menuMovimiento.addStyleName("menu-title_3rd");
	    menu.addItem(menuMovimiento);	    
	    
	    // Nuevo Egreso 
//	    MenuItem nuevoEgresoMenuItem = new MenuItem("Nuevo Egreso", new Command() {
//			@Override
//			public void execute() {
//				eventBus.fireEvent(new EventoEgresoNuevo());
//			}
//		});
//	    nuevoEgresoMenuItem.setStyleName("subMenuItem");
//	    menuBarCaja.addItem(nuevoEgresoMenuItem);
	    
	    // Buscar Egreso
//	    MenuItem busquedaEgresoMenuItem = new MenuItem("Buscar Egreso", new Command() {
//			@Override
//			public void execute() {
//				eventBus.fireEvent(new EventoEgresoBusqueda());
//			}
//		});
//	    busquedaEgresoMenuItem.setStyleName("subMenuItem");
//	    menuBarCaja.addItem(busquedaEgresoMenuItem);
	    
	    // Liquidaciones
	    MenuItem cargaLiquidacion = new MenuItem("Liquidación de carga", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoLiquidacionCarga());
			}
		});
	    cargaLiquidacion.setVisible(false);
	    cargaLiquidacion.setStyleName("subMenuItem");
	    menus.put("liquidacionCarga", cargaLiquidacion);
	    
	    MenuItem deudasLiquidacion = new MenuItem("Deudas por cobrar", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoDeudasPorCobrar());
			}
		});
	    deudasLiquidacion.setStyleName("subMenuItem");
	    deudasLiquidacion.setVisible(false);
	    menus.put("liquidacionDeudasCobrar", deudasLiquidacion);
	    
	    MenuBar liquidacionesMenuBar = new MenuBar(true);
	    liquidacionesMenuBar.setStyleName("subMenu");
	    liquidacionesMenuBar.setAnimationEnabled(true);
	    liquidacionesMenuBar.addItem(cargaLiquidacion);
	    liquidacionesMenuBar.addItem(deudasLiquidacion);
	    //liquidacionesMenuBar.addItem(actualizarNotaEntregaLiquidacion);
	    MenuItem menuLiquidaciones = new MenuItem("Liquidaciones", liquidacionesMenuBar);
	    menuLiquidaciones.setVisible(false);
	    menuLiquidaciones.setStylePrimaryName("menu-title");
	    menuLiquidaciones.addStyleName("menu-title_4th");
	    
	    menus.put("liquidacion", menuLiquidaciones);
	    menu.addItem(menuLiquidaciones);
	    //liquidacionesMenuBar.setVisible(false);
	    //menus.put("liquidacionDeudasCobrar", liquidacionesMenuBar);
	    
	    // Registro de Datos
	    MenuBar registroDatosMenuBar = new MenuBar(true);
	    registroDatosMenuBar.setStyleName("subMenu");
	    MenuItem clientesRegistroDatos = new MenuItem("Clientes", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoCliente());
			}
		});
	    clientesRegistroDatos.setStyleName("subMenuItem");
	    clientesRegistroDatos.setVisible(false);
	    menus.put("registroCliente", clientesRegistroDatos);
	    MenuItem transportistasRegistroDatos = new MenuItem("Transportistas",  new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoTransportista());
			}
		});
	    transportistasRegistroDatos.setStyleName("subMenuItem");
	    transportistasRegistroDatos.setVisible(false);
	    menus.put("registroTransportistas", transportistasRegistroDatos);
	    MenuItem cuentasIngresoRegistroDatos = new MenuItem("Cuentas", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoCuentas());
			}
		});
	    cuentasIngresoRegistroDatos.setStyleName("subMenuItem");
	    cuentasIngresoRegistroDatos.setVisible(false);
	    menus.put("registroCuentas", cuentasIngresoRegistroDatos);
	    
	    registroDatosMenuBar.setAnimationEnabled(true);
	    registroDatosMenuBar.addItem(clientesRegistroDatos);
	    registroDatosMenuBar.addItem(transportistasRegistroDatos);
	    registroDatosMenuBar.addItem(cuentasIngresoRegistroDatos);
	    MenuItem menuRegistro = new MenuItem("Registro de Datos", registroDatosMenuBar);
	    menuRegistro.setStylePrimaryName("menu-title");
	    menuRegistro.addStyleName("menu-title_5th");
	    menu.addItem(menuRegistro);
	    menuRegistro.setVisible(false);
	    menus.put("registro", menuRegistro);
	    
	    // Administración
	    MenuBar admMenuBar = new MenuBar(true);
	    admMenuBar.setStyleName("subMenu");
	    MenuItem usuariosAdministracion = new MenuItem("Usuarios", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoUsuario());
			}
		});
	    usuariosAdministracion.setStyleName("subMenuItem");
	    usuariosAdministracion.setVisible(false);
	    menus.put("admUsuarios", usuariosAdministracion);
	    MenuItem contrasenaAdministracion = new MenuItem("Cambio de contraseña", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new EventoCambiarContrasenia());
			}
		});
	    contrasenaAdministracion.setStyleName("subMenuItem");
	    contrasenaAdministracion.setVisible(false);
	    menus.put("admContrasena", contrasenaAdministracion);
	    MenuItem salirAdministracion = new MenuItem("Salir", new Command() {
		      													  public void execute() {
		      														 Window.open("logout", "_self", null);
		      													  }
															  });
	    salirAdministracion.setStyleName("subMenuItem");
	    salirAdministracion.setVisible(false);
	    menus.put("admSalir", salirAdministracion);
	    
	    admMenuBar.setAnimationEnabled(true);
	    admMenuBar.addItem(usuariosAdministracion);
	    admMenuBar.addItem(contrasenaAdministracion);
	    admMenuBar.addItem(salirAdministracion);
	    MenuItem admItem = new MenuItem("Administración", admMenuBar);
	    admItem.setVisible(false);
	    admItem.setStylePrimaryName("menu-title");
	    admItem.addStyleName("menu-title_6th");
	    menus.put("adm", admItem);
	    menu.addItem(admItem);
	    
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
		horizontalPanel.setStyleName("barraEstado");
		//horizontalPanel.setWidth("100%");
		horizontalPanel.setHeight("40px");
		
		// user 
		HorizontalPanel  userHorizontalPanel = new HorizontalPanel();
		//userHorizontalPanel.setWidth("25%");
		userHorizontalPanel.setHeight("100%");
		userHorizontalPanel.setStyleName("barraEstadoItem");
		userHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
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
		//fullNameHorizontalPanel.setWidth("25%");
		fullNameHorizontalPanel.setHeight("100%");
		fullNameHorizontalPanel.setStyleName("barraEstadoItem");
		fullNameHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
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
		//placeHorizontalPanel.setWidth("25%");
		placeHorizontalPanel.setHeight("100%");
		placeHorizontalPanel.setStyleName("barraEstadoItem");
		placeHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
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
		//dateHorizontalPanel.setWidth("25%");
		dateHorizontalPanel.setHeight("100%");
		dateHorizontalPanel.setStyleName("barraEstadoItem");
		dateHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
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
	
	@Override
	public void renderMenu() {
		log.info("renderMenu()");
		Usuario usuario = adminParametros.getUsuario();
		log.info("    usuario: " + usuario);
		log.info("    usuario.getRol(): " + usuario.getRol());
		log.info("    usuario.getRol().getAplicaciones(): " + usuario.getRol().getAplicaciones().size());
		for (Aplicacion	app : usuario.getRol().getAplicaciones()) {
			log.info("    app.getNombre(): " + app.getNombre());
			Object obj = menus.get(app.getNombre());
			log.info("    obj: " + (obj == null));
			if(obj instanceof MenuBar)  ((MenuBar)obj).setVisible(true);
			if(obj instanceof MenuItem) ((MenuItem)obj).setVisible(true);
			log.info("    app.getAplicacion1(): " + app.getAplicacion1());
			if(app.getAplicacion1() != null) {
				Object objPadre = menus.get(app.getAplicacion1().getNombre());
				log.info("         app.getAplicacion1().getNombre(): " + app.getAplicacion1().getNombre());
				log.info("         objPadre: " + (objPadre == null));
				if(obj instanceof MenuBar)  ((MenuBar)objPadre).setVisible(true);
				if(obj instanceof MenuItem) ((MenuItem)objPadre).setVisible(true);
			}
		}
		
		menus.get("admSalir").setVisible(true);
	}
	
}
