package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.event.EventoHome;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Oficina;

@Singleton
public class VistaGuia2 extends View<Guia> implements PresentadorGuia.Display {
	
	@Inject private VistaGuiaAccion vistaGuiaAccion;
	@Inject private Cargador cargador;
	@Inject	private MensajeExito mensajeExito;
	@Inject private MensajeAviso mensajeAviso;
	@Inject private MensajeError mensajeError;
	@Inject private ServicioGuiaCliente servicioGuia;
	@Inject private ImprimirPDF imprimirPDF;
	@Inject private AdminParametros adminParametros;
	@Inject private HandlerManager eventBus;
	
	private VistaElegirGuiaDialogBox vistaElegirGuiaDialogBox;
	
	private MultiWordSuggestOracle clienteOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();
	
	private ListBox    comboBusquedaListBox   = new ListBox(); 
	private TextBox    cajaBusquedaTextBox    = new TextBox();
	private SuggestBox cajaBusquedaSuggestBox = new SuggestBox(clienteOracle);
	private Widget     cajaBusquedaWidget;
	
	//private ListBox estadoListBox = new ListBox();
	private ListBox estadoPagoListBox = new ListBox();
	
	private Button buscarBtn = new Button("Buscar");
	
	private DateBox fechaIniDateBox = new DateBox();
	private DateBox fechaFinDateBox = new DateBox();
	
	private SuggestBox origenSuggestBox  = new SuggestBox(oficinaOracle);
	private SuggestBox destinoSuggestBox = new SuggestBox(oficinaOracle);
	
	private CheckBox sinConocimientoCheckBox = new CheckBox();	
	
	//private Button nuevoBtn = new Button("Nuevo");
	private Button consultarBtn = new Button("Consultar");	
	private Button modificarBtn = new Button("Modificar");
	private Button anularBtn = new Button("Anular");
	private Button imprimirBtn = new Button("Imprimir Búsqueda");
	private Button imprimirGuiaBtn = new Button("Imprimir Guia");
	private Button entregaBtn = new Button("Entregar");
	private Button salirBtn = new Button("Inicio");
	
	private Button seleccionBtn = new Button("Seleccion");
	private Button salirSelecionBtn = new Button("Salir");
	
	private FlexTable layoutMain;
	
	public VistaGuia2() {
		super(10);
	}
	
	public VistaGuia2(int paging) {
		super(paging);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public IsWidget viewIU(boolean esDialogBox) {
		log.info("VistaGuia2.viewUI()");
		
		habilitarOpcionesBusqueda(false);
		
		cajaBusquedaWidget = cajaBusquedaTextBox;
		
		origenSuggestBox.setWidth("120px");
		destinoSuggestBox.setWidth("120px");
		fechaIniDateBox.setWidth("80px");
		fechaFinDateBox.setWidth("80px");
		fechaIniDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		fechaFinDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		
		sinConocimientoCheckBox.setText("Sin Conocimiento");
		
		comboBusquedaListBox.clear();
		comboBusquedaListBox.addItem("Número de Guia", "0");
		comboBusquedaListBox.addItem("Número de Conocimiento", "1");
		comboBusquedaListBox.addItem("Número de Factura", "2");
		comboBusquedaListBox.addItem("Origen-Destino", "3");
		comboBusquedaListBox.addItem("Nombre Remitente", "4");
		comboBusquedaListBox.addItem("Nombre Consignatario", "5");
		cajaBusquedaWidget.setWidth("250px");
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.setWidth("100%");
		hpTitulo.add(new HTML("<center class='tituloModulo'>Búsqueda de Guias</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.add(hpTitulo);
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		vpNorte.setWidth("100%");
		
		// Campos de Busqueda
		layoutMain = new FlexTable();
	    layoutMain.setCellSpacing(6);
	    layoutMain.setWidget(0, 0, new HTML("<b>Seleccione categoria de búsqueda: </b>"));
	    layoutMain.setWidget(0, 1, comboBusquedaListBox);
	    layoutMain.setWidget(0, 2, cajaBusquedaWidget);
	    layoutMain.setWidget(0, 3, buscarBtn);
	    vpNorte.add(layoutMain);
	    
	    FlexTable layoutComodin = new FlexTable();
	    layoutComodin.setCellSpacing(6);
	    
	    layoutComodin.setWidget(0, 0, new HTML("Origen:"));
	    layoutComodin.setWidget(0, 1, origenSuggestBox);
	    layoutComodin.setWidget(0, 2, new HTML("Destino:"));
	    layoutComodin.setWidget(0, 3, destinoSuggestBox);
	    layoutComodin.setWidget(0, 4, new HTML("Desde:"));
	    layoutComodin.setWidget(0, 5, fechaIniDateBox);
	    layoutComodin.setWidget(0, 6, new HTML("Hasta:"));
	    layoutComodin.setWidget(0, 7, fechaFinDateBox);
	    layoutComodin.setWidget(0, 8, new HTML("Tipo de Pago:"));
	    layoutComodin.setWidget(0, 9, estadoPagoListBox);
	    layoutComodin.setWidget(0, 10,sinConocimientoCheckBox);
	    
	    DecoratorPanel decPanel = new DecoratorPanel();
	    decPanel.setWidget(layoutComodin);
	    vpNorte.add(decPanel);
	    
		defaultUI();
		
		// NroGuia
		TextColumn<Guia> nroGuiaColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getNroGuia() + "";
			}
		};
		grid.setColumnWidth(nroGuiaColmun, 10, Unit.PX);
		grid.addColumn(nroGuiaColmun, "Guia");
		
		// Remite
		TextColumn<Guia> remiteColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getRemitente() == null? "":entity.getRemitente().getNombre();
			}
		};
		grid.setColumnWidth(remiteColmun, 30, Unit.PX);
		grid.addColumn(remiteColmun, "Remite");
		
		// Consignatario
		TextColumn<Guia> consignatarioColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getConsignatario() == null? "":entity.getConsignatario().getNombre();
			}
		};
		grid.setColumnWidth(consignatarioColmun, 30, Unit.PX);
		grid.addColumn(consignatarioColmun, "Consignatario");
		
		// Origen
		TextColumn<Guia> origenColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getOficinaOrigen() == null?"":entity.getOficinaOrigen().getNombre();
			}
		};
		grid.setColumnWidth(origenColmun, 20, Unit.PX);
		grid.addColumn(origenColmun, "Origen");
		
		// Destino
		TextColumn<Guia> destinoColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getOficinaDestino() == null?"":entity.getOficinaDestino().getNombre();
			}
		};
		grid.setColumnWidth(destinoColmun, 20, Unit.PX);
		grid.addColumn(destinoColmun, "Destino");
		
		// Fecha Recepcion
		TextColumn<Guia> fechaRegistroColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				if(entity.getFechaRegistro() != null){
					return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(entity.getFechaRegistro());
				}
				return "";	
 			}
		};
		grid.setColumnWidth(fechaRegistroColmun, 20, Unit.PX);
		grid.addColumn(fechaRegistroColmun, "Registro");
		
		// Fecha Entrega
		TextColumn<Guia> fechaEntregaColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				if(entity.getFechaEntrega() != null){
					return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(entity.getFechaEntrega());
				}
				return "";
			}
		};
		grid.setColumnWidth(fechaEntregaColmun, 15, Unit.PX);
		grid.addColumn(fechaEntregaColmun, "Entrega");
		
		// Factura Origen
		TextColumn<Guia> facturaOrigenColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getNroFactura();
			}
		};
		grid.setColumnWidth(facturaOrigenColmun, 15, Unit.PX);
		grid.addColumn(facturaOrigenColmun, "Fact. Origen");		
		
		// Factura Destino
		TextColumn<Guia> facturaDestinoColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getNroFacturaEntrega();
			}
		};
		grid.setColumnWidth(facturaDestinoColmun, 15, Unit.PX);
		grid.addColumn(facturaDestinoColmun, "Fact. Destino");			
		
		// Estado
		TextColumn<Guia> activoColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				if(entity.getEstadoDescripcion() != null){
					return entity.getEstadoDescripcion();
				}
				return "";
			}
		};
		grid.setColumnWidth(activoColmun, 10, Unit.PX);
		grid.addColumn(activoColmun, "Estado");
		
		//grid.setWidth("1000px");
		grid.setHeight("300px");
		
		VerticalPanel vpGrid = new VerticalPanel();
		vpGrid.setWidth("100%");
		vpGrid.add(grid);
		
		HorizontalPanel horizontalPanelPager = new HorizontalPanel();
		horizontalPanelPager.setWidth("100%");
		horizontalPanelPager.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanelPager.add(simplePager);
		vpGrid.add(horizontalPanelPager);
		
		/// ACCIONES
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
//		horizontalPanelButton.add(nuevoBtn);
		horizontalPanelButton.add(consultarBtn);
		log.info("adminParametros.getUsuario().getAdministrador(): " + adminParametros.getUsuario().getAdministrador());
		if(adminParametros.getUsuario().getAdministrador())
			horizontalPanelButton.add(modificarBtn);
		horizontalPanelButton.add(anularBtn);
		//horizontalPanelButton.add(imprimirGuiaBtn);
		horizontalPanelButton.add(entregaBtn);
		//horizontalPanelButton.add(imprimirBtn);
		
		horizontalPanelButton.add(salirBtn);
		horizontalPanel.add(horizontalPanelButton);
		
		/// ACCION para seleccionar
		HorizontalPanel horizontalPanelSelect = new HorizontalPanel();
		horizontalPanelSelect.setWidth("100%");
		horizontalPanelSelect.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButtonSelect = new HorizontalPanel();
		horizontalPanelButtonSelect.setSpacing(5);
		horizontalPanelButtonSelect.add(seleccionBtn);
		horizontalPanelButtonSelect.add(salirSelecionBtn);
		horizontalPanelSelect.add(horizontalPanelButtonSelect);
		
		// Layout general
		DockPanel dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		
		if(esDialogBox) dock.add(horizontalPanelSelect, DockPanel.SOUTH);
		else            dock.add(horizontalPanel, DockPanel.SOUTH);
		
		cargarEstadosListBox();
		implementarAcciones();
		
		if(esDialogBox) {
			accionPresentador();
			return dock;
		} else mainContentView.getCentralPanel().add(dock);
		
		// Permisos de usuario
//		destinoSuggestBox.setValue(adminParametros.getUsuario().getOffice().getNombre());
//		fechaIniDateBox.setValue(adminParametros.getDateParam().getDate());
//		fechaFinDateBox.setValue(adminParametros.getDateParam().getDate());
			
		return null;
		
	}
	
	@Override
	protected Object getKeyItem(Guia item) {
		return item.getId();
	}
	@Override
	protected String getNro(Guia entity) {
		return entity.getNro()+"";
	}

	@Override
	public void cargarDataUI(List<Guia> clientes) {
		dataProvider.getList().clear();
		dataProvider.setList(clientes);
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public Guia getParametrosBusqueda() {
		
		log.info(" Opción elegida: "+ comboBusquedaListBox.getSelectedValue());
		
		// Validar y setting values
		if((comboBusquedaListBox.getSelectedValue().equals("1") || comboBusquedaListBox.getSelectedValue().equals("2")) &&  
			 cajaBusquedaTextBox.getValue().equals("")) {
			mensajeAviso.mostrar("Parámetro numerico vacio");
			return null;
		}
		if((comboBusquedaListBox.getSelectedValue().equals("4") || comboBusquedaListBox.getSelectedValue().equals("5")) &&  
			 cajaBusquedaSuggestBox.getValue().equals("")) {
			mensajeAviso.mostrar("Parámetro vacio");
			return null;
		}
		
		Integer valueBusqueda = null;
		if(comboBusquedaListBox.getSelectedValue().equals("0") || comboBusquedaListBox.getSelectedValue().equals("1") || comboBusquedaListBox.getSelectedValue().equals("2")) {
		//if(!comboBusquedaListBox.getSelectedValue().equals("3")) {
			String value = cajaBusquedaTextBox.getValue();
			try {
				valueBusqueda = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				mensajeAviso.mostrar("Término de busqueda no es número");  
				return null;
			}
		}
		
		if(comboBusquedaListBox.getSelectedValue().equals("4") || comboBusquedaListBox.getSelectedValue().equals("5") ) {
			String value = cajaBusquedaSuggestBox.getValue();
			Cliente valueEntity = adminParametros.buscarClientePorNombre(value); 
			if(valueEntity == null) {
				mensajeAviso.mostrar("Término de busqueda no puede ser vacio o incompleto");
			}
		}
		
		//Construyento valores para la busqueda
		Guia guia = new Guia();
		if(comboBusquedaListBox.getSelectedValue().equals("0")) {  // Nro Guia
			guia.setNroGuia(valueBusqueda); 
		} else if (comboBusquedaListBox.getSelectedValue().equals("1")) {  // Nro Conocimiento
			guia.setNroConocimiento(valueBusqueda);
		} else if (comboBusquedaListBox.getSelectedValue().equals("2")) {  // Nro Factura
			guia.setNroFactura(valueBusqueda+"");
		} else { // Otros casos
			// validar 
			log.info("fechaIniDateBox.getValue(): " + fechaIniDateBox.getValue());
			log.info("fechaFinDateBox.getValue(): " + fechaFinDateBox.getValue());
			if((fechaIniDateBox.getValue() == null && fechaFinDateBox.getValue() != null) ||
			   (fechaIniDateBox.getValue() != null && fechaFinDateBox.getValue() == null)) {
				mensajeAviso.mostrar("Intervalo de fecha debe ser completado");
				return null;
			}
			
			log.info("origenSuggestBox.getValue(): " + origenSuggestBox.getValue());
			log.info("destinoSuggestBox.getValue(): " + destinoSuggestBox.getValue());
			if (comboBusquedaListBox.getSelectedValue().equals("3")) {  // Origen - Destino
				if(
				   //(origenSuggestBox.getValue().equals("") && !destinoSuggestBox.getValue().equals("")) ||
				   //(!origenSuggestBox.getValue().equals("") && destinoSuggestBox.getValue().equals(""))  || 
				   (origenSuggestBox.getValue().equals("") && destinoSuggestBox.getValue().equals(""))) {
					mensajeAviso.mostrar("Uno de los valores de origen o destino deben ser llenados");
					return null;
				}
				String origenNombre  = origenSuggestBox.getValue();
				Oficina origen = adminParametros.buscarOficinaPorNombre(origenNombre);
				guia.setOficinaOrigen(origen);
				
				String destinoNombre = destinoSuggestBox.getValue();
				Oficina destino = adminParametros.buscarOficinaPorNombre(destinoNombre);
				guia.setOficinaDestino(destino);
				
				Date fechaDesde = fechaIniDateBox.getValue();
				guia.setFechaIni(fechaDesde);
				Date fechaHasta = fechaFinDateBox.getValue();
				guia.setFechaFin(fechaHasta);
				
				guia.setExcluirGuiasExistentesEnConocimiento(sinConocimientoCheckBox.getValue());
			}
			if (comboBusquedaListBox.getSelectedValue().equals("4") || comboBusquedaListBox.getSelectedValue().equals("5")) {  // remitente-consignatario
				if(comboBusquedaListBox.getSelectedValue().equals("4")) {
					String remitenteNombre = cajaBusquedaSuggestBox.getValue();
					Cliente remitente = adminParametros.buscarClientePorNombre(remitenteNombre);
					guia.setRemitente(remitente);
				}
				if(comboBusquedaListBox.getSelectedValue().equals("5")) {
					String consignatarioNombre = cajaBusquedaSuggestBox.getValue();
					Cliente consignatario = adminParametros.buscarClientePorNombre(consignatarioNombre);
					guia.setConsignatario(consignatario);
				}
			}
		}
		
		return guia;
	}

	@Override
	public void fijarOracleParaClientes(List<String> palabras) {
		for (String s : palabras) {
			if(s != null)
				clienteOracle.add(s);
		}
	}

	@Override
	public void fijarOracleParaOficina(List<String> palabras) {
		oficinaOracle.addAll(palabras);
	}
	
	@SuppressWarnings("unchecked")
	private void implementarAcciones() {
		
		comboBusquedaListBox.addChangeHandler(e -> {
			cajaBusquedaTextBox.setValue(null);
			cajaBusquedaSuggestBox.setValue("");
			origenSuggestBox.setValue(null);
			destinoSuggestBox.setValue(null);
			fechaIniDateBox.setValue(null);
			fechaFinDateBox.setValue(null);
			sinConocimientoCheckBox.setValue(null);
			
			// Nro Guia // Nro Conocimiento // Nro Factura
			if(comboBusquedaListBox.getSelectedValue().equals("0") || comboBusquedaListBox.getSelectedValue().equals("1") || comboBusquedaListBox.getSelectedValue().equals("2")) {   
				habilitarOpcionesBusqueda(false);
				cajaBusquedaWidget = cajaBusquedaTextBox;
				layoutMain.setWidget(0, 2, cajaBusquedaWidget);
				cajaBusquedaTextBox.setEnabled(true); 
			}  else if (comboBusquedaListBox.getSelectedValue().equals("3")) {
				cajaBusquedaTextBox.setEnabled(false);
				cajaBusquedaSuggestBox.setEnabled(false);
				habilitarOpcionesBusqueda(true);
			} else if (comboBusquedaListBox.getSelectedValue().equals("4") || comboBusquedaListBox.getSelectedValue().equals("5")) {
				cajaBusquedaWidget = cajaBusquedaSuggestBox;
				layoutMain.setWidget(0, 2, cajaBusquedaWidget);
				cajaBusquedaSuggestBox.setEnabled(true);
				habilitarOpcionesBusqueda(false);
				sinConocimientoCheckBox.setEnabled(true);
			} 
			
		});
		
		grid.addDomHandler(new DoubleClickHandler() {
		    @Override
		    public void onDoubleClick(DoubleClickEvent event) { 
		    	consultarGuia();
		    }    
		}, DoubleClickEvent.getType());
		
		grid.getSelectionModel().addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
				if(guia != null) {
					String estado = guia.getEstadoDescripcion();
					if(estado.charAt(0) == 'A') {
						anularBtn.setEnabled(false);
						modificarBtn.setEnabled(false);
						entregaBtn.setEnabled(false);
					} 
					if(estado.charAt(0) == 'E') {
						anularBtn.setEnabled(true);
						modificarBtn.setEnabled(true);
						entregaBtn.setEnabled(false);
					}
					if(estado.charAt(0) == 'R') {
						anularBtn.setEnabled(true);
						modificarBtn.setEnabled(true);
						if(!adminParametros.getUsuario().getAdministrador())
							if(guia.getOficinaDestino().getNombre().equals(adminParametros.getUsuario().getOffice().getNombre()))
								entregaBtn.setEnabled(true);
							else 
								entregaBtn.setEnabled(false); 
						else	
							entregaBtn.setEnabled(true);
					}
					
				}
			}
		});
		
		buscarBtn.addClickHandler(e -> ((SingleSelectionModel<Guia>)grid.getSelectionModel()).clear());
//		nuevoBtn.addClickHandler(e -> vistaGuiaAccion.mostrar(GuiaAccion.NUEVO, null));
		consultarBtn.addClickHandler(e -> {
			consultarGuia();
		});
		modificarBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null)
				mensajeAviso.mostrar("Seleccione la Guia que decea consultar");
			else {
				vistaGuiaAccion.setIsDialog(true);
				vistaGuiaAccion.mostrar(GuiaAccion.MODIFICAR, guia);
			}
				
		});
		anularBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null)
				mensajeAviso.mostrar("Seleccione la Guia que decea anular");
			else {
				VistaGuia2.this.cargador.center();
				servicioGuia.cambiarEstado(guia.getId(), "Anulado", new LlamadaRemota<Void>("No se pudo anular la Guia", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						VistaGuia2.this.cargador.hide();
						mensajeExito.mostrar("Guia anulada existosamente, con nro: " + guia.getNroGuia(), buscarBtn);
						
					}
				});
			}
		});
		entregaBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null) {
				mensajeAviso.mostrar("Seleccione la Guia que decea entregar");
				return ;
			}
			vistaGuiaAccion.setIsDialog(true);
			vistaGuiaAccion.mostrar(GuiaAccion.ENTREGA, guia);
		});
		imprimirGuiaBtn.addClickHandler(e -> {
			log.info("e.getClass().getSimpleName()" + e.getClass().getSimpleName());
			
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null) {
				mensajeAviso.mostrar("Seleccione la Guia para imprimir");
				return ;
			}
			vistaGuiaAccion.imprimirGuia(guia);
		});
		
		imprimirBtn.addClickHandler(e -> {
			log.info("--> imprimir Busqueda Guia: ");
			List<Guia> guias = dataProvider.getList();
			log.info("--> guias: " + guias);
			String[][] guiasImprimir = new String[guias.size()][11];
			int k = 0;
			for (Guia guia : guias) {
				String remite = "";
				if(guia.getRemitente()!=null) remite = guia.getRemitente().getNombre();
				String consignatario = "";
				if(guia.getConsignatario()!=null) consignatario = guia.getConsignatario().getNombre(); 
				GWT.log("  k:" + k);
				guiasImprimir[k][0]  = (k+1)+"";
				guiasImprimir[k][1]  = guia.getNroGuia()+"";
				guiasImprimir[k][2]  = remite;
				guiasImprimir[k][3]  = consignatario;
				guiasImprimir[k][4]  = guia.getOficinaOrigen()==null?"":guia.getOficinaOrigen().getNombre();
				guiasImprimir[k][5]  = guia.getOficinaDestino()==null?"":guia.getOficinaDestino().getNombre();
				guiasImprimir[k][6]  = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(guia.getFechaRegistro());
				guiasImprimir[k][7]  = "";//guia.getFechaEntrega()+"";
				guiasImprimir[k][8]  = guia.getNroFactura() == null ? "" : guia.getNroFactura();
				guiasImprimir[k][9]  = "";
				guiasImprimir[k][10] = guia.getEstadoDescripcion();
				k++;
			}
			imprimirPDF.generarImpresionBusquedaGuias(guiasImprimir);
		});
		salirBtn.addClickHandler(e -> {
			eventBus.fireEvent(new EventoHome());
		});
		
		anularBtn.setEnabled(true);
		imprimirBtn.setEnabled(true);
		entregaBtn.setEnabled(false);
		
		salirSelecionBtn.addClickHandler(e -> {
			vistaElegirGuiaDialogBox.hide();
		});
		
		seleccionBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null) {
				mensajeAviso.mostrar("Seleccione un Conocimiento");
				return ;
			}
			vistaElegirGuiaDialogBox.setGuiaSeleccionada(guia);
		});
	}
	
	private void cargarEstadosListBox() {
		
		estadoPagoListBox.clear();
		estadoPagoListBox.addItem("", "0");
		estadoPagoListBox.addItem("Origen", "O");
		estadoPagoListBox.addItem("Destino", "D");
		
		
//		servicioGuiaCliente.getEstados(new LlamadaRemota<List<EstadoGuia>>("No se puede obtener estados", false){
//			@Override
//			public void onSuccess(Method method, List<EstadoGuia> response) {
//				GWT.log("estados:" + response);
//				estadoListBox.clear();
//				estadoListBox.addItem("Todos");
//				for (EstadoGuia e : response) {
//					estadoListBox.addItem(e.getEstadoDescripcion());
//				}
//				estadoListBox.setSelectedIndex(2);
//			}
//		});
	}
	
	private void accionPresentador() {
		log.info(this.getClass().getSimpleName() + ".go()" );
		//viewIU(false);
		
		List<Cliente> clientes = adminParametros.getClientes();
		log.info("clientes.size: " + clientes.size());
		List<String> palabras = new ArrayList<>();
		for (Cliente cli : clientes) {
			palabras.add(cli.getNombre());
		}
		fijarOracleParaClientes(palabras);
		
		List<Oficina> oficinas = adminParametros.getOficinas();
		log.info("oficinas.size: " + oficinas.size());
		List<String> palabras1 = new ArrayList<>();
		for (Oficina oficina : oficinas) {
			palabras1.add(oficina.getNombre());
		}
		fijarOracleParaOficina(palabras1);
		
		buscarBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Guia guia = getParametrosBusqueda();
				log.info("guia parametro búsqueda 1: "+ guia);
				//PresentadorGuia.this.cargador.center();
				servicioGuia.buscarGuias(guia, new MethodCallback<List<Guia>>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al traer Guias: " + exception.getMessage());
						//cargador.hide();
						mensajeError.mostrar("Error al traer Guias: ", exception);
					}
					@Override
					public void onSuccess(Method method, List<Guia> response) {
						log.info("response: " + response);
						showGuiaData(response);
						cargador.hide();
						
					}
				});
			}
		});

	}
	
	int i = 1;
	private void showGuiaData(List<Guia> guias) {
		for (Guia guia: guias) {
			guia.setNro(i++);
		}
		i = 1;
		cargarDataUI(guias);
	}
	
	public void setVistaElegirGuiaDialogBox(VistaElegirGuiaDialogBox vistaElegirGuiaDialogBox) {
		this.vistaElegirGuiaDialogBox = vistaElegirGuiaDialogBox;
	}
	
	
	private void consultarGuia() {
		@SuppressWarnings("unchecked")
		Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
		if(guia == null)
			mensajeAviso.mostrar("Seleccione la Guia que decea consultar");
		else {
			//vistaGuiaAccion = new VistaGuiaAccion();
			vistaGuiaAccion.setIsDialog(true);
			vistaGuiaAccion.mostrar(GuiaAccion.CONSULTAR, guia);
		}
			
	}
	
	private void habilitarOpcionesBusqueda(boolean enabled) {
		origenSuggestBox.setEnabled(enabled);
		destinoSuggestBox.setEnabled(enabled);
		fechaIniDateBox.setEnabled(enabled);
		fechaFinDateBox.setEnabled(enabled);
		estadoPagoListBox.setEnabled(enabled);
		sinConocimientoCheckBox.setEnabled(enabled);
	}
}
