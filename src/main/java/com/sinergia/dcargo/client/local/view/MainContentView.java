package com.sinergia.dcargo.client.local.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sinergia.dcargo.client.local.event.UserMainEvent;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;

@Singleton
public class MainContentView extends DockPanel implements MainContentPresenter.Display {

	private int widthNavigator  = Window.getClientWidth();
	private int heightNavigator = Window.getClientHeight();
	
	@Inject
	private HandlerManager eventBus;
	
	private SimplePanel panelCentral;
	
	@PostConstruct
	public void init() {
		setStyleName("cw-DockPanel");
	    setSpacing(4);
	    setHorizontalAlignment(DockPanel.ALIGN_CENTER);
	    
	}

	@Override
	public void showMainContent(HasWidgets container) {
		
		add(getMainMenu(), DockPanel.NORTH);
	    add(getContentBody() , DockPanel.CENTER);
	    add(getStatusBar(), DockPanel.SOUTH);
	    container.add(this);
	    
	}
	
	private MenuBar getMainMenu() {
		
	    Command menuCommand = new Command() {
		      private int curPhrase = 0;
		      public void execute() {
		    	  eventBus.fireEvent(new UserMainEvent());
		      }
		};
		 
		MenuBar menu = new MenuBar();
	    menu.setAutoOpen(true);
	    //menu.setWidth("" + (widthNavigator - 30) + "px");
	    menu.setAnimationEnabled(true);

	    // Guías
	    MenuBar guiasMenuBar = new MenuBar(true);
	    guiasMenuBar.setAnimationEnabled(true);
	    
	    MenuItem menuGuia = new MenuItem("Guías", guiasMenuBar);
	    menu.addItem(menuGuia);
	    
	    MenuItem nuevaGuiaMenuItem     = new MenuItem("Nuevo", menuCommand);
	    MenuItem consultarGuiaMenuItem = new MenuItem("Consultar", menuCommand);
	    MenuItem modificarGuiaMenuItem = new MenuItem("Modificar", menuCommand);
	    MenuItem anularGuiaMenuItem    = new MenuItem("Anular", menuCommand);
	    MenuItem reporteGuiaMenuItem   = new MenuItem("Reporte", menuCommand);
	    MenuItem entregaGuiaMenuItem   = new MenuItem("Entrega", menuCommand);
	    MenuItem guiaNuevaMenuItem     = new MenuItem("Guía* (Nueva Opción)", menuCommand);
	    
	    guiasMenuBar.addItem(nuevaGuiaMenuItem);
	    guiasMenuBar.addItem(consultarGuiaMenuItem);
	    guiasMenuBar.addItem(modificarGuiaMenuItem);
	    guiasMenuBar.addItem(anularGuiaMenuItem);
	    guiasMenuBar.addItem(reporteGuiaMenuItem);
	    guiasMenuBar.addItem(entregaGuiaMenuItem);
	    guiasMenuBar.addItem(guiaNuevaMenuItem);
	    
	    
	    // Conocimiento
	    MenuBar conocimientoMenuBar = new MenuBar(true);
	    conocimientoMenuBar.setAnimationEnabled(true);
	    
	    MenuItem registroConocimientoMenuItem = new MenuItem("Registro", menuCommand);
	    MenuItem consultarConocimientoMenuItem = new MenuItem("Consultar", menuCommand);
	    MenuItem modificarConocimientoMenuItem = new MenuItem("Modificar", menuCommand);
	    MenuItem anularConocimientoMenuItem = new MenuItem("Anular", menuCommand);
	    MenuItem reporteConocimientoMenuItem = new MenuItem("Reporte", menuCommand);
	    MenuItem actualizarDatosConocimientoMenuItem = new MenuItem("Actualizar datos complementarios", menuCommand);
	    MenuItem nuevoConocimientoMenuItem = new MenuItem("Conocimiento* (Nuevo)", menuCommand);
	    
	    
	    conocimientoMenuBar.addItem(registroConocimientoMenuItem);
	    conocimientoMenuBar.addItem(consultarConocimientoMenuItem);
	    conocimientoMenuBar.addItem(modificarConocimientoMenuItem);
	    conocimientoMenuBar.addItem(anularConocimientoMenuItem);
	    conocimientoMenuBar.addItem(reporteConocimientoMenuItem);
	    conocimientoMenuBar.addItem(actualizarDatosConocimientoMenuItem);
	    conocimientoMenuBar.addItem(nuevoConocimientoMenuItem);
	    menu.addItem(new MenuItem("Conocimiento", conocimientoMenuBar));
	    
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
	    MenuItem ingresosCaja = new MenuItem("Ingresos", menuCommand);
	    MenuItem egresosCaja = new MenuItem("Egresos", menuCommand);
	    MenuItem reportesCaja = new MenuItem("Reportes", menuCommand);
	    
	    MenuBar cajaMenuBar = new MenuBar(true);
	    cajaMenuBar.setAnimationEnabled(true);
	    cajaMenuBar.addItem(ingresosCaja);
	    cajaMenuBar.addItem(egresosCaja);
	    cajaMenuBar.addItem(reportesCaja);
	    menu.addItem(new MenuItem("Caja", cajaMenuBar));
	    
	    // Registro de Datos
	    MenuBar registroDatosMenuBar = new MenuBar(true);
	    MenuItem clientesRegistroDatos = new MenuItem("Clientes", menuCommand);
	    MenuItem transportistasRegistroDatos = new MenuItem("Transportistas", menuCommand);
	    MenuItem cuentasIngresoRegistroDatos = new MenuItem("Cuentas de ingreso", menuCommand);
	    MenuItem cuentasEgresoRegistroDatos = new MenuItem("Cuentas de egreso", menuCommand);
	    
	    registroDatosMenuBar.setAnimationEnabled(true);
	    registroDatosMenuBar.addItem(clientesRegistroDatos);
	    registroDatosMenuBar.addItem(transportistasRegistroDatos);
	    registroDatosMenuBar.addItem(cuentasIngresoRegistroDatos);
	    registroDatosMenuBar.addItem(cuentasEgresoRegistroDatos);
	    menu.addItem(new MenuItem("Registro de Datos", registroDatosMenuBar));
	    
	    // Administración
	    MenuBar admMenuBar = new MenuBar(true);
	    
	    MenuItem usuariosAdministracion = new MenuItem("Usuarios", new Command() {
			@Override
			public void execute() {
				eventBus.fireEvent(new UserMainEvent());
			}
		});
	    MenuItem fechaAdministracion = new MenuItem("Actualizar fecha del sistema", menuCommand);
	    MenuItem contrasenaAdministracion = new MenuItem("Cambio de contraseña", menuCommand);
	    
	    admMenuBar.setAnimationEnabled(true);
	    admMenuBar.addItem(usuariosAdministracion);
	    admMenuBar.addItem(fechaAdministracion);
	    admMenuBar.addItem(contrasenaAdministracion);
	    menu.addItem(new MenuItem("Administración", admMenuBar));
	    
	    return menu;
	}

	public SimplePanel getCentralPanel(){
		return panelCentral;
	}
	
	private DecoratorPanel getContentBody() {
		
		panelCentral = new SimplePanel();
		panelCentral.setWidth("" + (widthNavigator - 60) + "px");
		panelCentral.setHeight("" + (heightNavigator - 105) + "px");
		
		DecoratorPanel bodyDecoratorPanel = new DecoratorPanel();
	    bodyDecoratorPanel.add(panelCentral);
		
		return bodyDecoratorPanel;
		
	}
	
	private HorizontalPanel getStatusBar() {
		
		Label userLabel      = new Label("Usuario: ");
		Label userLabelValue = new Label();
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("" + (widthNavigator - 30) + "px");
		horizontalPanel.add(userLabel);
		horizontalPanel.add(userLabelValue);
		
		return horizontalPanel;
		
	}
	
}
