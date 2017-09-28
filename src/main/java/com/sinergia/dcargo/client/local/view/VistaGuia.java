package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Oficina;

@Singleton
public class VistaGuia extends View<Guia> implements PresentadorGuia.Display {
	
	@Inject
	private VistaGuiaAccion vistaGuiaAccion;
	
	@Inject
	private ServicioGuiaCliente servicioGuiaCliente;
	
	@Inject
	private Cargador cargador;
	
	@Inject	private MensajeExito mensajeExito;
	@Inject private MensajeAviso mensajeAviso;
	@Inject private MensajeError mensajeError;
	
	@Inject
	private ServicioGuiaCliente servicioGuia;
	
	@Inject
	private ImprimirPDF imprimirPDF;
	
	@Inject
	private AdminParametros adminParametros;
	
	private VistaElegirGuiaDialogBox vistaElegirGuiaDialogBox;
	
	private MultiWordSuggestOracle clienteOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();
	
	private IntegerBox nroGuia = new IntegerBox();
	
	private SuggestBox remiteSuggestBox        = new SuggestBox(clienteOracle);
	private SuggestBox consignatarioSuggestBox = new SuggestBox(clienteOracle);
	
	private SuggestBox origenSuggestBox  = new SuggestBox(oficinaOracle);
	private SuggestBox destinoSuggestBox = new SuggestBox(oficinaOracle);
	
	private DateBox fechaIniDateBox = new DateBox();
	private DateBox fechaFinDateBox = new DateBox();
	
	private TextBox nroFacturaOrigenTextBox = new TextBox();
	private TextBox nroFacturaDestinoTextBox = new TextBox();
	
	private ListBox estadoListBox = new ListBox();
	
	private Button buscarBtn = new Button("Buscar");
	
	private Button nuevoBtn = new Button("Nuevo");
	private Button consultarBtn = new Button("Consultar");	
	private Button modificarBtn = new Button("Modificar");
	private Button anularBtn = new Button("Anular");
	private Button imprimirBtn = new Button("Imprimir Búsqueda");
	private Button imprimirGuiaBtn = new Button("Imprimir Guia");
	private Button entregaBtn = new Button("Entrega");
	private Button salirBtn = new Button("Salir");
	
	private Button seleccionBtn = new Button("Seleccion");
	private Button salirSelecionBtn = new Button("Salir");
	
	public VistaGuia() {
		super(10);
	}
	
	public VistaGuia(int paging) {
		super(paging);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public IsWidget viewIU(boolean esDialogBox) {
		
		nroFacturaOrigenTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				GWT.log("nroFacturaOrigenTextBox.value: " + event.getValue());
			}
		});
		
		fechaIniDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		fechaFinDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.setWidth("100%");
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Guias</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.add(hpTitulo);
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		vpNorte.setWidth("100%");
		
		FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    // Campos
	    layout.setWidget(1, 0, new HTML("<b>Remite: </b>"));
	    layout.setWidget(1, 1, remiteSuggestBox);
	    layout.setWidget(2, 0, new HTML("<b>Consignatario: </b>"));
	    layout.setWidget(2, 1, consignatarioSuggestBox);
	    
	    layout.setWidget(1, 2, new HTML("<b>Origen: </b>"));
	    layout.setWidget(1, 3, origenSuggestBox);
	    layout.setWidget(2, 2, new HTML("<b>Destino: </b>"));
	    layout.setWidget(2, 3, destinoSuggestBox);
	  
	    layout.setWidget(1, 4, new HTML("<b>Fecha Inicio: </b>"));
	    layout.setWidget(1, 5, fechaIniDateBox);
	    layout.setWidget(2, 4, new HTML("<b>Fecha Fin: </b>"));
	    layout.setWidget(2, 5, fechaFinDateBox);
	    
//	    layout.setWidget(1, 6, new HTML("<b>Factura origen: </b>"));
//	    layout.setWidget(1, 7, nroFacturaOrigenTextBox);
//	    layout.setWidget(2, 6, new HTML("<b>Fecha destino: </b>"));
//	    layout.setWidget(2, 7, nroFacturaDestinoTextBox);
	    
	    layout.setWidget(3, 0, new HTML("<b>Nro Guia: </b>"));
	    layout.setWidget(3, 1, nroGuia);
	    
	    layout.setWidget(3, 2, new HTML("<b>Estado: </b>"));
	    layout.setWidget(3, 3, estadoListBox);
	    
	    layout.setWidget(3, 4, buscarBtn);
	    
	    vpNorte.add(layout);
	    
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
		horizontalPanelButton.add(nuevoBtn);
		horizontalPanelButton.add(consultarBtn);
		horizontalPanelButton.add(modificarBtn);
		horizontalPanelButton.add(anularBtn);
		//horizontalPanelButton.add(imprimirGuiaBtn);
		horizontalPanelButton.add(entregaBtn);
		horizontalPanelButton.add(imprimirBtn);
		
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
		
		//mainContentView.getCentralPanel().add(dock);
		
		cargarEstadosListBox();
		implementarAcciones();
		
		if(esDialogBox) {
			accionPresentador();
			return dock;
		} else mainContentView.getCentralPanel().add(dock);
		
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
		Cliente remitente = new Cliente();
		remitente.setNombre(remiteSuggestBox.getValue());
		Cliente consignatario = new Cliente();
		consignatario.setNombre(consignatarioSuggestBox.getValue());
		Oficina origen = new Oficina();
		origen.setNombre(origenSuggestBox.getValue());
		Oficina destino = new Oficina();
		destino.setNombre(destinoSuggestBox.getValue());
		Guia guia = new Guia();
		guia.setNroGuia(nroGuia.getValue());
		guia.setRemitente(remitente);
		guia.setConsignatario(consignatario);
		guia.setOficinaOrigen(origen);
		guia.setOficinaDestino(destino);
		guia.setFechaIni(fechaIniDateBox.getValue());
		guia.setFechaFin(fechaFinDateBox.getValue());
		//guia.setNroFactura(nroFacturaOrigenTextBox.getValue());
		//guia.setNroFacturaEntrega(nroFacturaDestinoTextBox.getValue());
		guia.setEstadoDescripcion(estadoListBox.getSelectedValue());
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
						entregaBtn.setEnabled(true);
					}
					
				}
			}
		});
		
		buscarBtn.addClickHandler(e -> ((SingleSelectionModel<Guia>)grid.getSelectionModel()).clear());
		nuevoBtn.addClickHandler(e -> vistaGuiaAccion.mostrar(GuiaAccion.NUEVO, null));
		consultarBtn.addClickHandler(e -> {
			consultarGuia();
		});
		modificarBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null)
				mensajeAviso.mostrar("Seleccione la Guia que decea consultar");
			else 
				vistaGuiaAccion.mostrar(GuiaAccion.MODIFICAR, guia);
		});
		anularBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null)
				mensajeAviso.mostrar("Seleccione la Guia que decea anular");
			else {
				VistaGuia.this.cargador.center();
				servicioGuia.cambiarEstado(guia.getId(), "Anulado", new LlamadaRemota<Void>("No se pudo anular la Guia", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						VistaGuia.this.cargador.hide();
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
			log.info("Salir: "+ com.google.gwt.core.client.GWT.getHostPageBaseURL());
			Window.Location.assign(com.google.gwt.core.client.GWT.getHostPageBaseURL());
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
		servicioGuiaCliente.getEstados(new LlamadaRemota<List<EstadoGuia>>("No se puede obtener estados", false){
			@Override
			public void onSuccess(Method method, List<EstadoGuia> response) {
				GWT.log("estados:" + response);
				estadoListBox.clear();
				estadoListBox.addItem("Todos");
				for (EstadoGuia e : response) {
					estadoListBox.addItem(e.getEstadoDescripcion());
				}
				estadoListBox.setSelectedIndex(2);
			}
		});
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
			vistaGuiaAccion.mostrar(GuiaAccion.CONSULTAR, guia);
		}
			
	}
}
