package com.sinergia.dcargo.client.local.view;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.LlamadaRemotaVacia;
import com.sinergia.dcargo.client.local.api.ServicioConocimientoCliente;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Conocimiento;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Item;
import com.sinergia.dcargo.client.shared.Oficina;
import com.sinergia.dcargo.client.shared.ServicioConocimiento;
import com.sinergia.dcargo.client.shared.Transportista;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

@Singleton
public class VistaConocimientoAccion extends DialogBox implements Carga {

	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	private Logger log;
	 
	@Inject 
	private Cargador cargador;
	
	@Inject
	private MensajeExito mensajeExito;
	
	@Inject
	private MensajeAviso mensajeAviso;
	
	@Inject
	private MensajeError mensajeError;
	
	@Inject
	private ImprimirPDF imprimirPDF;
	
	@Inject
	private VistaClienteAccion vistaClienteAccion;
	
	@Inject
	private ServicioGuiaCliente servicioGuia;
	
	@Inject
	private ServicioConocimientoCliente servicioConocimiento;

	private ConocimientoAccion conocimientoAccion;
	
	private MultiWordSuggestOracle oficinaOracle       = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle transportistaOracle = new MultiWordSuggestOracle();
	
	//private MultiWordSuggestOracle propietarioOracle = new MultiWordSuggestOracle();
	//private MultiWordSuggestOracle conductorOracle = new MultiWordSuggestOracle();
	
	private HTML nroConocimientoLabel = new HTML("<b>Nro Conocimiento: </b>");
	private Label nroConocimientoValorLabel = new Label("Sin Valor");

	private HTML fechaRegistroLabel = new HTML("<b>Fecha: </b>");
	private Label fechaRegistroValorLabel = new Label("9999/99/99");
	
	private HTML origenLabel = new HTML("<b>Origen*: </b>");
	private SuggestBox origenSuggestBox = new SuggestBox(oficinaOracle);
	private Label origenLabelValue = new Label("");
	
	private HTML destinoLabel = new HTML("<b>Destino*: </b>");
	private SuggestBox destinoSuggestBox = new SuggestBox(oficinaOracle);
	private Label destinoLabelValue = new Label("");
	
	private HTML propietarioLabel = new HTML("<b>Propietario*:</b>");
	private SuggestBox propietarioSuggestBox = new SuggestBox(transportistaOracle);
	private Label propietarioLabelValue = new Label("");

	private HTML conductorLabel = new HTML("<b>Conductor*: </b>");
	private SuggestBox conductorSuggestBox = new SuggestBox(transportistaOracle);
	private Label conductorLabelValue = new Label("");
	
	private HTML vecinoLabel = new HTML("<b>Vecino: </b>");
	private Label vecinoLabelValor = new Label();

	private HTML ciLabel = new HTML("<b>C.I.: </b>");
	private Label ciLabelValor = new Label();

	private HTML domicilioEnLabel = new HTML("<b>Domicilio en: </b>");
	private Label domicilioLabelValue = new Label();
	
	private HTML telefonoEnLabel = new HTML("<b>Teléfono: </b>");
	private Label telefonoLabelValue = new Label();
	
	private HTML marcaLabel = new HTML("<b>Marca: </b>");
	private Label marcaLabelValor = new Label();
	
	private HTML colorLabel = new HTML("<b>Color: </b>");
	private Label colorLabelValor = new Label();
	
	private HTML placaLabel = new HTML("<b>Placa: </b>");
	private Label placaLabelValor = new Label();
	
	private HTML brevetLabel = new HTML("<b>Brevet Nro.: </b>");
	private Label brevetLabelValor = new Label();

	private Button nuevoTransportistaButton = new Button("Nuevo Transportista");
	private Button nuevoOficinaButton = new Button("Nuevo Oficina");
	
	private HTML multaLabel = new HTML("<b>Una multa de Bs:</b>");
	private DoubleBox multaTextBox = new DoubleBox();
	private Label multaLabelVale = new Label();
	
	private HTML diasLabel = new HTML("<b>Por dia pasado los dias: </b>");
	private IntegerBox diasTextBox = new IntegerBox();
	private Label diasLabelValue = new Label();
	
	private HTML observacionesLabel = new HTML("<b>Observaciones: </b>");
	private TextArea observacionesTextArea = new TextArea();
	private Label observacionesLabelValue = new Label();
	
	private HTML adjuntoLabel = new HTML("<b>Adjunto: </b>");
	private TextArea adjuntoTextArea = new TextArea();
	private Label adjuntoLabelValue = new Label();
	
	private HTML aclaracionLabel = new HTML("<b>Aclaración: </b>");
	private TextArea aclaracionTextArea = new TextArea();
	private Label aclaracionLabelValue = new Label();
	
	private HTML fleteLabel = new HTML("<b>Flete convenio Bs: </b>");
	private DoubleBox fleteDoubleBox = new DoubleBox();
	private Label fleteLabelValue = new Label();
	
	private HTML acuentaLabel = new HTML("<b>A cuenta Bs: </b>");
	private DoubleBox acuentaDoubleBox = new DoubleBox();
	private Label acuentaLabelValue = new Label();
	
	private HTML pagoOrigenLabel = new HTML("<b>Por pagar en Origen: </b>");
	private DoubleBox pagoOrigenDoubleBox = new DoubleBox();
	private Label pagoOrigenLabelValue = new Label();
	
	private HTML pagoDestinoLabel = new HTML("<b>Por pagar en Destino: </b>");
	private DoubleBox pagoDestinoDoubleBox = new DoubleBox();
	private Label pagoDestinoLabelValue = new Label();
	
	private Button remitirBtn = new Button("Remitir");
	private Button imprimirBtn = new Button("Imprimir");
	private Button salirBtn = new Button("Salir");
	private HTML estadoHTML  = new HTML();
	
	private TabLayoutPanel tabPanel;
	
	private Button buscarGuiasButton = new Button("Buscar Guias");
	
	DateBox fechaIniBusquedaGuia = new DateBox();
	DateBox fechaFinBusquedaGuia = new DateBox();
	
	private Conocimiento conocimientoSeleccionado;
	
	//private Button pendienteBtn = new Button("Fijar en pendiente");

	interface GuiaPropiedad extends PropertyAccess<Guia> {
		@Path("id")
		ModelKeyProvider<Guia> id();
		@Path("nroGuia")
		ValueProvider<Guia, Integer> nroGuia();
		@Path("remitente.nombre")
		ValueProvider<Guia, String> remitente();
		@Path("consignatario.nombre")
		ValueProvider<Guia, String> consignatario();
		@Path("oficinaOrigen.nombre")
		ValueProvider<Guia, String> oficinaOrigen();
		@Path("oficinaDestino.nombre")
		ValueProvider<Guia, String> oficinaDestino();
		@Path("fechaRegistro")
		ValueProvider<Guia, Date> fechaRegistro();
		@Path("fechaEntrega")
		ValueProvider<Guia, Date> fechaEntrega();
		@Path("estadoDescripcion")
		ValueProvider<Guia, String> estadoDescripcion();
	} 
	
	private GuiaPropiedad guiaPropiedad = GWT.create(GuiaPropiedad.class);

	private ListStore<Guia> storeOrigen = new ListStore<>(guiaPropiedad.id());
	private ListStore<Guia> storeDestino = new ListStore<>(guiaPropiedad.id());
	
	public VistaConocimientoAccion() {
		super();
		GWT.log(this.getClass().getSimpleName() + "()");
	}

	@PostConstruct
	protected void init() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}

	@AfterInitialization
	public void cargarDataUI() {
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());		 
	}

	private void construirGUI() {

		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText(conocimientoAccion.getTitulo());
		
		// config
		vecinoLabelValor.setWidth("150px");
		ciLabelValor.setWidth("150px");
		
		multaTextBox.setWidth("30px");
		diasTextBox.setWidth("20px");
		
		conocimientoSeleccionado.setFecha(adminParametros.getDateParam().getDate());
		fechaRegistroValorLabel.setText(adminParametros.getDateParam().getFormattedValue());

		// Formulario
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.setHeight("20px");
		vpNorte.setWidth("100%");
		
		HTML htmlCenter = new HTML("<pre>                                                 </pre>");
		htmlCenter.setWidth("100%");
		
		FlexTable layoutConstante = new FlexTable();
		layoutConstante.setCellSpacing(0);
		FlexCellFormatter cellFormatter1 = layoutConstante.getFlexCellFormatter();
		//cellFormatter1.setColSpan(0, 0, 2);
		//cellFormatter1.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		layoutConstante.setWidget(0, 0, nroConocimientoLabel);      cellFormatter1.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(0, 1, nroConocimientoValorLabel); cellFormatter1.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(0, 2, htmlCenter);        cellFormatter1.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		layoutConstante.setWidget(0, 4, fechaRegistroLabel);        cellFormatter1.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(0, 5, fechaRegistroValorLabel);   cellFormatter1.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(1, 4, origenLabel);        cellFormatter1.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(1, 5, origenLabelValue);   cellFormatter1.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_LEFT);
		vpNorte.add(layoutConstante);
		
		
		tabPanel = new TabLayoutPanel(2.5, Unit.EM);
		tabPanel.setAnimationDuration(1000);
		tabPanel.getElement().getStyle().setMarginBottom(10.0, Unit.PX);
		tabPanel.setWidth("900px");
		tabPanel.setHeight("400px");
		
		VerticalPanel vpCentro1 = getCentro1();
		tabPanel.add(vpCentro1, "Datos generales");
		
		VerticalPanel vpCentro2 = getCentro2();
		tabPanel.add(vpCentro2, "Guias");
		tabPanel.selectTab(0);
		
		// 3.1. 
		VerticalPanel surPanel = new VerticalPanel();
		surPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		surPanel.setWidth("100%");
		
		FlexTable layout2 = new FlexTable();
		layout2.setCellSpacing(6);
		FlexCellFormatter cellFormatter2 = layout2.getFlexCellFormatter();
		cellFormatter2.setColSpan(0, 0, 2);
		cellFormatter2.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
//		layout2.setWidget(1, 0, multaLabel);
//		layout2.setWidget(2, 0, resumenLabel);
//		layout2.setWidget(3, 0, nroEntregaLabel);
//		layout2.setWidget(1, 6, pagoOrigenLabel);
//		layout2.setWidget(2, 6, pagoDestinoLabel);
		
		if(conocimientoAccion == ConocimientoAccion.NUEVO || conocimientoAccion == ConocimientoAccion.MODIFICAR){
//			layout2.setWidget(1, 1, multaTextBox);     multaTextBox.setValue("");
//			layout2.setWidget(2, 1, resumenTextBox);     resumenTextBox.setValue(""); 
//			layout2.setWidget(3, 1, nroEntregaTextBox);  nroEntregaTextBox.setValue(""); 
//			layout2.setWidget(1, 7, pagoOrigenTextBox);  pagoOrigenTextBox.setValue(null);
//			layout2.setWidget(2, 7, pagoDestinoTextBox); pagoDestinoTextBox.setValue(null);
		} if(conocimientoAccion == ConocimientoAccion.CONSULTAR){
			layout2.setWidget(1, 1, multaLabelVale);
//			multaLabelVale.setText(guiaSeleccionada.getAdjunto());
//			layout2.setWidget(2, 1, resumenLabelValue);
//			resumenLabelValue.setText(guiaSeleccionada.getResumenContenido());
//			layout2.setWidget(3, 1, nroEntregaLabelValue);
//			nroEntregaLabelValue.setText(guiaSeleccionada.getNotaEntrega());
//			layout2.setWidget(1, 7, pagoOrigenLabelValue);
//			pagoOrigenLabelValue.setText(guiaSeleccionada.getPagoOrigen()+"");
//			layout2.setWidget(2, 7, pagoDestinoLabelValue);
//			pagoDestinoLabelValue.setText(guiaSeleccionada.getSaldoDestino()+"");
		} if(conocimientoAccion == ConocimientoAccion.MODIFICAR){
//			multaTextBox.setValue(guiaSeleccionada.getAdjunto());
//			resumenTextBox.setValue(guiaSeleccionada.getResumenContenido());
//			nroEntregaTextBox.setValue(guiaSeleccionada.getNotaEntrega());
//			pagoOrigenTextBox.setValue(guiaSeleccionada.getPagoOrigen());
//			pagoDestinoTextBox.setValue(guiaSeleccionada.getSaldoDestino());
		}
		
		//surPanel.add(layout2);
		
		// 3.2. Acciones
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		
		if(conocimientoAccion == ConocimientoAccion.NUEVO) {
			horizontalPanelButton.add(remitirBtn);
			horizontalPanelButton.add(imprimirBtn);
			horizontalPanelButton.add(salirBtn);
		} 
		if(conocimientoAccion == ConocimientoAccion.MODIFICAR || conocimientoAccion == ConocimientoAccion.CONSULTAR) {
			horizontalPanelButton.add(salirBtn);
		}
		
		horizontalPanel.add(horizontalPanelButton);
		//horizontalPanel.add(estadoHTML);
		surPanel.add(horizontalPanel);
		surPanel.add(estadoHTML);
		
		DockPanel dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(tabPanel, DockPanel.CENTER);
		dock.add(surPanel, DockPanel.SOUTH);

		setWidget(dock);
		
		cargarOracles();
		agregarEscuchadores();
		datosIniciales();
		center();
		
	}
	
	public void mostrar(ConocimientoAccion conocimientoAccion, final Conocimiento conocimiento) {
		this.conocimientoSeleccionado = conocimiento;  
		this.conocimientoAccion = conocimientoAccion;
		GWT.log("guiaAccion: " + conocimientoAccion);
		if(conocimientoAccion == ConocimientoAccion.NUEVO || conocimientoAccion == ConocimientoAccion.CONSULTAR || conocimientoAccion == ConocimientoAccion.MODIFICAR) {
			if(conocimientoAccion == ConocimientoAccion.NUEVO) {
				cargador.center();
				fijarEstadoGuiaEspera();
				servicioConocimiento.nuevoConocimiento(new MethodCallback<Conocimiento>() {
					@Override
					public void onSuccess(Method method, Conocimiento response) {
						conocimientoSeleccionado = response;
						log.info("conocimientoSeleccionado: " + conocimientoSeleccionado);
						construirGUI();
						cargador.hide();
						fijarEstadoGuiaCargado();
					}
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al generar nroGuia: " + exception.getMessage());
						mensajeError.mostrar("Error al generar nroGuia", exception);
						cargador.hide();
					}
				});
			} else if(conocimientoAccion == ConocimientoAccion.CONSULTAR || conocimientoAccion == ConocimientoAccion.MODIFICAR) {
				cargador.center();
				servicioConocimiento.consultarConocimiento(conocimientoSeleccionado.getId(), new LlamadaRemota<Conocimiento>("No se pude hallar información del Conocimiento", true) {
					@Override
					public void onSuccess(Method method, Conocimiento response) {
						conocimientoSeleccionado = response;
						construirGUI();
						VistaConocimientoAccion.this.cargador.hide();
					}
				});
			}
		}
		
	}
	
	private VerticalPanel getCentro1() {
		
		VerticalPanel vpCentro1 = new VerticalPanel();
		vpCentro1.setHeight("20px");
		//vpCentro1.setWidth("100%");
		
		HorizontalPanel layoutCuerpo1 = new HorizontalPanel();
		//layoutCuerpo1.setWidth("1000px");
		layoutCuerpo1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layoutCuerpo1.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layoutCuerpo1.add(new FlexTableForm(new HTML("<b>Yo: </b>"), new VerticalPanelEx(new HTML("<b>Propietario: </b>"), propietarioSuggestBox, new Button("Nuevo"))));
		layoutCuerpo1.add(new HTML("<pre> </pre>"));
		layoutCuerpo1.add(new FlexTableForm(new HTML(""),            new VerticalPanelEx(new HTML("<b>Conductor: </b>"),   conductorSuggestBox,   new Button("Nuevo"))));
		layoutCuerpo1.add(new HTML("<pre> </pre>"));
		layoutCuerpo1.add(new FlexTableForm(vecinoLabel, vecinoLabelValor));
		layoutCuerpo1.add(new HTML("<pre> </pre>"));
		layoutCuerpo1.add(new FlexTableForm(ciLabel, ciLabelValor));
		vpCentro1.add(layoutCuerpo1);
		
		HorizontalPanel layoutCuerpo15 = new HorizontalPanel();
		layoutCuerpo15.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layoutCuerpo15.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layoutCuerpo15.add(new FlexTableForm(domicilioEnLabel, domicilioLabelValue));
		layoutCuerpo15.add(new HTML("<pre> </pre>"));
		layoutCuerpo15.add(new FlexTableForm(telefonoEnLabel, telefonoLabelValue));
		layoutCuerpo15.add(new HTML("<pre> </pre>"));
		layoutCuerpo15.add(new FlexTableForm(marcaLabel, marcaLabelValor));
		layoutCuerpo15.add(new HTML("<pre> </pre>"));
		layoutCuerpo15.add(new FlexTableForm(colorLabel, colorLabelValor));
		layoutCuerpo15.add(new HTML("<pre> </pre>"));
		layoutCuerpo15.add(new FlexTableForm(placaLabel, placaLabelValor));
		layoutCuerpo15.add(new HTML("<pre> </pre>"));
		layoutCuerpo15.add(new FlexTableForm(brevetLabel, brevetLabelValor));
		layoutCuerpo15.add(new HTML("<pre> </pre>"));
		vpCentro1.add(layoutCuerpo15);
		
		HorizontalPanel layoutCuerpo2 = new HorizontalPanel();
		layoutCuerpo2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		layoutCuerpo2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layoutCuerpo2.add(new FlexTableForm(multaLabel, multaTextBox));
		layoutCuerpo2.add(new HTML("<pre> </pre>"));
		layoutCuerpo2.add(new FlexTableForm(diasLabel, diasTextBox));
		layoutCuerpo2.add(new HTML("<pre> </pre>"));
		layoutCuerpo2.add(new FlexTableForm(destinoLabel, destinoSuggestBox));
		vpCentro1.add(layoutCuerpo2);
		
		HorizontalPanel cuerpo3 = new HorizontalPanel();
		vpCentro1.add(cuerpo3);		
		
		FlexTable cuerpo31 = new FlexTable();
		cuerpo31.setCellSpacing(0);
		FlexCellFormatter cellFormatter31 = cuerpo31.getFlexCellFormatter();
		cuerpo31.setWidget(0, 0, observacionesLabel);    cellFormatter31.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo31.setWidget(0, 1, observacionesTextArea); cellFormatter31.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo31.setWidget(1, 0, adjuntoLabel);          cellFormatter31.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo31.setWidget(1, 1, adjuntoTextArea);       cellFormatter31.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo31.setWidget(2, 0, aclaracionLabel);       cellFormatter31.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo31.setWidget(2, 1, aclaracionTextArea);    cellFormatter31.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo3.add(cuerpo31);
		
		FlexTable cuerpo32 = new FlexTable();
		cuerpo32.setCellSpacing(0);
		FlexCellFormatter cellFormatter32 = cuerpo32.getFlexCellFormatter();
		cuerpo32.setWidget(0, 0, fleteLabel);           cellFormatter32.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(0, 1, fleteDoubleBox);       cellFormatter32.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo32.setWidget(1, 0, acuentaLabel);         cellFormatter32.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(1, 1, acuentaDoubleBox);     cellFormatter32.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo32.setWidget(2, 0, pagoOrigenLabel);      cellFormatter32.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(2, 1, pagoOrigenDoubleBox);  cellFormatter32.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo32.setWidget(3, 0, pagoDestinoLabel);     cellFormatter32.setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(3, 1, pagoDestinoDoubleBox); cellFormatter32.setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo3.add(cuerpo32);
		
		return vpCentro1;
	}
	
	private VerticalPanel getCentro2(){
		
		VerticalPanel vp = new VerticalPanel();
		
		FlexTable filtro = getFiltro();
		vp.add(filtro);
		
		Grid<Guia> gridOrigen = new Grid<>(storeOrigen, createColumnList());
		gridOrigen.setHeight(200);
		vp.add(gridOrigen);
		
		Grid<Guia> gridDestino = new Grid<>(storeDestino, createColumnList());
		vp.add(gridDestino);
		
		GridDragSource<Guia> dragSourceOrigen = new GridDragSource<>(gridOrigen);
		dragSourceOrigen.setGroup("top");
		dragSourceOrigen.addDropHandler(e -> {
			log.info("Source: dragSourceOrigen");
		});
		GridDragSource<Guia> dragSourceDestino = new GridDragSource<>(gridDestino);
		dragSourceDestino.setGroup("top");
		dragSourceDestino.addDropHandler(e -> {
			log.info("Source: dragSourceDestino");
		});
		
		GridDropTarget<Guia> dropTargetOrigen = new GridDropTarget<>(gridOrigen);
		dropTargetOrigen.setGroup("top");
		dropTargetOrigen.addDropHandler(e -> {
			log.info("Target: dropTargetOrigen");
			//fijarEstadoGuiaEspera();
			log.info("e.getData(): " + e.getData());
			@SuppressWarnings("unchecked")
			List<Guia> guias = (List<Guia>)e.getData();
			fijarEstadoGuiaEspera();
			servicioConocimiento.quitarGuia(conocimientoSeleccionado.getId(), guias.get(0).getId(), new LlamadaRemota<Void>("", true){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
			});
			
		});
		
		GridDropTarget<Guia> dropTargetDestino = new GridDropTarget<>(gridDestino);
		dropTargetDestino.setGroup("top");
		dropTargetDestino.addDropHandler(e -> {
			log.info("Target: dropTargetDestino");
			log.info("e.getData().getClass(): " + e.getData().getClass());
			@SuppressWarnings("unchecked")
			List<Guia> guias = (List<Guia>)e.getData();
			fijarEstadoGuiaEspera();
			servicioConocimiento.adicionarGuia(conocimientoSeleccionado.getId(), guias.get(0).getId(), new LlamadaRemota<Void>("", true){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
			});
		});
		
		
		return vp;
	}
	
	private FlexTable getFiltro() {
		
		FlexTable flexTable = new FlexTable();
		flexTable.setCellSpacing(0);
		FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();
		flexTable.setHTML(0, 0, "Fecha Inicio: "); cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(0, 1, fechaIniBusquedaGuia);     cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.setHTML(0, 2, "Fecha Fin: "); cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		flexTable.setWidget(0, 3, fechaFinBusquedaGuia);     cellFormatter.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		flexTable.setWidget(0, 4, buscarGuiasButton);
		return flexTable;
	}
	
	private ColumnModel<Guia> createColumnList(){
		
		List<ColumnConfig<Guia, ?>>	columns = new ArrayList<>();
		
		
		ColumnConfig<Guia, Integer> nroGuiaColumn = new ColumnConfig<>(guiaPropiedad.nroGuia());
		nroGuiaColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Nro Guia"));
		nroGuiaColumn.setSortable(true);
		columns.add(nroGuiaColumn);
		
		ColumnConfig<Guia, String> remiteColumn = new ColumnConfig<>(guiaPropiedad.remitente());
		remiteColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Remite"));
		remiteColumn.setSortable(true);
		columns.add(remiteColumn);
		
		ColumnConfig<Guia, String> consignatarioColumn = new ColumnConfig<>(guiaPropiedad.consignatario());
		consignatarioColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Consignatario"));
		consignatarioColumn.setSortable(true);
		columns.add(consignatarioColumn);
		
		ColumnConfig<Guia, String> origenColumn = new ColumnConfig<>(guiaPropiedad.oficinaOrigen());
		origenColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Origen"));
		origenColumn.setSortable(true);
		columns.add(origenColumn);
		
		ColumnConfig<Guia, String> destinoColumn = new ColumnConfig<>(guiaPropiedad.oficinaDestino());
		destinoColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Destino"));
		destinoColumn.setSortable(true);
		columns.add(destinoColumn);
		
		ColumnConfig<Guia, Date> fechaRegistroColumn = new ColumnConfig<>(guiaPropiedad.fechaRegistro());
		fechaRegistroColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Fecha Registro"));
		fechaRegistroColumn.setSortable(true);
		columns.add(fechaRegistroColumn);
		
		ColumnConfig<Guia, Date> fechaEntregaColumn = new ColumnConfig<>(guiaPropiedad.fechaEntrega());
		fechaEntregaColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Fecha Entrega"));
		fechaEntregaColumn.setSortable(true);
		columns.add(fechaEntregaColumn);
		
		ColumnConfig<Guia, Date> estadoColumn = new ColumnConfig<>(guiaPropiedad.fechaEntrega());
		estadoColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Estado"));
		estadoColumn.setSortable(true);
		columns.add(estadoColumn);
		
		return new ColumnModel<>(columns);
	}
	
	public void cargarOracles() {
		for (Transportista cliente: adminParametros.getTransportistas()) {
			if(cliente.getNombre() != null)
				transportistaOracle.add(cliente.getNombre());
		}
		for (Oficina oficina: adminParametros.getOficinas()) {
			oficinaOracle.add(oficina.getNombre());
		}
	}
	
	void agregarEscuchadores(){
		
		nroConocimientoValorLabel.setText(conocimientoSeleccionado.getNroConocimiento() + "");
		
		propietarioSuggestBox.addSelectionHandler(e->{
			String remitenteName = e.getSelectedItem().getReplacementString();
			Cliente cliente = adminParametros.buscarClientePorNombre(remitenteName);
			vecinoLabelValor.setText(cliente.getTelefono());
			//conocimientoSeleccionado.setRemitente(cliente);
			GWT.log("valueremitenteName: " + remitenteName);
			
			Cliente clienteTemp = new Cliente();
			clienteTemp.setId(cliente.getId());
			Guia guiaTemp = new Guia();
			//guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setRemitente(clienteTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarRemitente(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Remitente", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
				
			});
			
		});
		//new Transportista().getDireccion()
		
		conductorSuggestBox.addSelectionHandler(e -> {
			
			String conductorName = e.getSelectedItem().getReplacementString();
			Transportista transportista = getTransportistaPorNombre(conductorName);
			vecinoLabelValor.setText(transportista.getVecino_de());
			ciLabelValor.setText(transportista.getCi());
			domicilioLabelValue.setText(transportista.getDireccion());
			telefonoLabelValue.setText(transportista.getTelefono());
			marcaLabelValor.setText(transportista.getMarca());	
			colorLabelValor.setText(transportista.getColor());
			placaLabelValor.setText(transportista.getPlaca());
			brevetLabelValor.setText(transportista.getBrevetCi());
			
//			Cliente cliente = adminParametros.buscarClientePorNombre(consignatarioName);
//			ciValorLabel.setText(cliente.getTelefono());
//			guiaSeleccionada.setConsignatario(cliente);
//			GWT.log("valueconsignatario: " + consignatarioName);
//			
//			Cliente clienteTemp = new Cliente();
//			clienteTemp.setId(cliente.getId());
//			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
//			guiaTemp.setConsignatario(clienteTemp);
//			
//			fijarEstadoGuiaEspera();
//			servicioGuia.guardarConsignatario(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Consignatario", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					fijarEstadoGuiaCargado();
//				}
//				
//			});
			
		});
		origenSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
//			guiaSeleccionada.setOficinaOrigen(oficina);
			
			Oficina oficinaTemp = new Oficina();
			oficinaTemp.setId(oficina.getId());
			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setOficinaOrigen(oficinaTemp);
			servicioGuia.guardarOrigen(guiaTemp, new LlamadaRemotaVacia<>("No se puede guardar Origen", false));
		});
		destinoSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
//			guiaSeleccionada.setOficinaDestino(oficina);
			
			Oficina oficinaTemp = new Oficina();
			oficinaTemp.setId(oficina.getId());
			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setOficinaDestino(oficinaTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarDestino(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Destino", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
			});
		});
		multaTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
//			guiaTemp.setAdjunto(multaTextBox.getValue());
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarAdjunto(guiaTemp, new LlamadaRemota<Void>("", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
			});
		});
//		resumenTextBox.addValueChangeHandler(e->{
//			guardarResumen();
//		});
//		nroEntregaTextBox.addValueChangeHandler(e->{
//			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
//			guiaTemp.setNotaEntrega(nroEntregaTextBox.getValue());
//			
//			fijarEstadoGuiaEspera();
//			servicioGuia.guardarNroEntrega(guiaTemp, new LlamadaRemota<Void>("", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					fijarEstadoGuiaCargado();
//				}});
//		});
//		pagoOrigenTextBox.addValueChangeHandler(e->{
//			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
//			guiaTemp.setPagoOrigen(pagoOrigenTextBox.getValue());
//			fijarEstadoGuiaEspera();
//			servicioGuia.guardarPagoOrigen(guiaTemp, new LlamadaRemota<Void>("", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					guiaSeleccionada.setPagoOrigen(pagoOrigenTextBox.getValue());
//					
//					Double total       = guiaSeleccionada.getTotalGuia(); 
//					Double pagoOrigen  = pagoOrigenTextBox.getValue();
//					GWT.log("total: " + total);
//					GWT.log("pagoOrigen: " + pagoOrigen);
//					Double pagoDestino = total - pagoOrigen;
//					pagoDestinoTextBox.setValue(pagoDestino);
//					
//					Guia guiaTemp = new Guia();
//					guiaTemp.setId(guiaSeleccionada.getId());
//					guiaTemp.setSaldoDestino(pagoDestino);
//					servicioGuia.guardarPagoDestino(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
//					guiaSeleccionada.setSaldoDestino(pagoDestino);
//					
//					fijarEstadoGuiaCargado();
//				}
//			});
//		});
//		pagoDestinoTextBox.addValueChangeHandler(e->{
//			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
//			guiaTemp.setSaldoDestino(pagoDestinoTextBox.getValue());
//			
//			fijarEstadoGuiaEspera();
//			servicioGuia.guardarPagoDestino(guiaTemp, new LlamadaRemota<Void>("", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					guiaSeleccionada.setSaldoDestino(pagoDestinoTextBox.getValue());
//					
//					Double total       = guiaSeleccionada.getTotalGuia(); 
//					Double pagoDestino = pagoDestinoTextBox.getValue();
//					GWT.log("total: " + total);
//					GWT.log("pagoDestino: " + pagoDestino);
//					Double pagoOrigen = total - pagoDestino;
//					pagoOrigenTextBox.setValue(pagoOrigen);
//					
//					Guia guiaTemp = new Guia();
//					guiaTemp.setId(guiaSeleccionada.getId());
//					guiaTemp.setPagoOrigen(pagoOrigen);
//					servicioGuia.guardarPagoOrigen(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
//					guiaSeleccionada.setPagoOrigen(pagoOrigen);
//					
//					fijarEstadoGuiaCargado();
//				}
//			});
//		});
		
		remitirBtn.addClickHandler(e -> {
			if(validarParaRemitir()){
				cargador.center();
//				servicioGuia.cambiarEstado(guiaSeleccionada.getId(), "Remitido", new LlamadaRemota<Void>("No se pudo aceptar la Guia", true) {
//					@Override
//					public void onSuccess(Method method, Void response) {
//						mensajeExito.mostrar("Guia remitida existosamente con nro: " + guiaSeleccionada.getNroGuia());
//						mensajeExito.center();
//						VistaConocimientoAccion.this.cargador.hide();
//					}
//				});
			} else {
				VistaConocimientoAccion.this.mensajeAviso.mostrar("Requiere llenar los campos obligatorios");
			}
		});
		
		imprimirBtn.addClickHandler(e->{
//			GWT.log("guiaSeleccionada.getItems(): " + guiaSeleccionada.getItems().size());
//			String items[][] = new String[7][guiaSeleccionada.getItems().size()];
			int k = 0;
			int bultos = 0;
			Double peso = 0D;
			Double total = 0D;
			
//			for (Item i: guiaSeleccionada.getItems()) {
//				items[k][0] = Integer.toString(k+1);
//				items[k][1] = i.getCantidad()+"";
//				items[k][2] = i.getContenido();
//				items[k][3] = i.getPeso()+"";
//				items[k][4] = i.getUnidad().getAbreviatura()+"";
//				items[k][5] = i.getPrecio().getDescripcion();
//				items[k][6] = i.getTotal()+"";
				k++;
				
//				bultos = bultos + i.getCantidad();
//				peso = peso + i.getPeso();
//				total = total = i.getTotal();
				
//			}
			
			String ciRemitente = "";
//			if(guiaSeleccionada.getRemitente() == null) ciRemitente = "";
//			else ciRemitente = guiaSeleccionada.getRemitente().getCi();
			
//			imprimirPDF.generarPDFGuia(fechaRegistroValorLabel.getText(),          nroConocimientoValorLabel.getText(),
//					                   propietarioSuggestBox.getValue(),        vecinoValorLabel.getText(),  origenSuggestBox.getText(),
//					                   conductorSuggestBox.getValue(), ciValorLabel.getText(),destinoSuggestBox.getValue(), 
//					                   items, bultos+"", peso+"", total+"",  
//					                   resumenTextBox.getValue(), pagoOrigenTextBox.getValue()+"", pagoDestinoTextBox.getValue()+"", 
//					                   ciRemitente
//		     );
		});
		
		salirBtn.addClickHandler(e -> {
			hide();
		});
		
		nuevoTransportistaButton.addClickHandler(e -> {
			//vistaClienteAccion.setVistaGuiaAccion(this);
			vistaClienteAccion.mostrar(ClienteAccion.NUEVO_DESDE_GUIA, null);
		});
		
		buscarGuiasButton.addClickHandler(e -> {
			Date fechaIni = fechaIniBusquedaGuia.getValue();
			Date fechaFin = fechaFinBusquedaGuia.getValue();
			if( fechaIni == null || fechaFin == null){
				mensajeAviso.mostrar("Necesitar elegir un intervalo de fecha");
				return;
			}
			
			Guia guia =  new Guia();
			guia.setFechaIni(fechaIni);
			guia.setFechaFin(fechaFin);
			guia.setEstadoDescripcion("Remitido");
			
			fijarEstadoGuiaEspera();
			servicioGuia.buscarGuias(guia, new LlamadaRemota<List<Guia>>("", true){
				@Override
				public void onSuccess(Method method, List<Guia> response) {
					
					List<Guia> guiasBorrar = new ArrayList<>();
					for (Guia guiaRespuesta : response) {
						if(storeDestino.findModel(guiaRespuesta) != null) guiasBorrar.add(guiaRespuesta);
					}
					
					response.removeAll(guiasBorrar);
					
					storeOrigen.clear();
					storeOrigen.addAll(response);
					
					fijarEstadoGuiaCargado();
				}
			});
		});
		
	}
	
	public void setResumen(String resumen){
//		resumenTextBox.setValue(resumen);
		guardarResumen();
	}
	
	public void guardarResumen(){
		Guia guiaTemp = new Guia();
//		guiaTemp.setId(guiaSeleccionada.getId());
		//guiaTemp.setResumenContenido(resumenTextBox.getValue());
		servicioGuia.guardarResumen(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
	}
	
	private boolean validarParaRemitir() {
		
		// remite
		GWT.log("-->remiteSuggestBox.getValue(): " + propietarioSuggestBox.getValue());
		String remite = propietarioSuggestBox.getValue();
		boolean remiteValido = false;
		for (Cliente c: adminParametros.getClientes()) {
			if(c.getNombre().equals(remite)) {remiteValido = true; break;} 
		}
		
		// consignatario
		GWT.log("-->consignatarioSuggestBox.getValue(): " + conductorSuggestBox.getValue());
		String consignatario = conductorSuggestBox.getValue();
		boolean consignatarioValido = false;
		for (Cliente c: adminParametros.getClientes()) {
			if(c.getNombre().equals(consignatario)) {consignatarioValido = true; break;} 
		}
		
		// origen
		GWT.log("-->origenSuggestBox.getValue(): " + origenSuggestBox.getValue());
		String origen = origenSuggestBox.getValue();
		boolean origenValido = false;
		for (Oficina o : adminParametros.getOficinas()) {
			if(o.getNombre().equals(origen)) {origenValido = true; break;}
		}
		
		// destino
		GWT.log("-->destinoSuggestBox.getValue(): " + destinoSuggestBox.getValue());
		String destino = destinoSuggestBox.getValue();
		boolean destinoValido = false;
		for (Oficina o : adminParametros.getOficinas()) {
			if(o.getNombre().equals(destino)) {destinoValido = true; break;}
		}
		
		// pago origen
//		GWT.log("-->pagoOrigenTextBox.getValue(): " + pagoOrigenTextBox.getValue());
//		Double pagoOrigen = pagoOrigenTextBox.getValue();
		
		// pago destino
//		GWT.log("-->pagoDestinoTextBox.getValue(): " + pagoDestinoTextBox.getValue());
//		Double pagoDestino = pagoDestinoTextBox.getValue();
		
		if(!remiteValido) 		 return false;
		if(!consignatarioValido) return false;
		if(!origenValido)        return false;
		if(!destinoValido)       return false;
//		if(pagoOrigen == null)  return false;
//		if(pagoDestino == null)  return false;
		
		return true;
	}
	
	private void datosIniciales(){
		servicioConocimiento.getGuias(conocimientoSeleccionado.getId(), new LlamadaRemota<List<Guia>>("", true) {
			@Override
			public void onSuccess(Method method, List<Guia> response) {
				//log.info("servicioConocimiento.getGuias -> response.size(): " + response.size());
				storeDestino.clear();
				//response.size();
				storeDestino.addAll(response);
			}
		});
	}
	
	private Transportista getTransportistaPorNombre(String nombre){
		List<Transportista> transportistas = adminParametros.getTransportistas();
		for(Transportista t: transportistas){
			if(t.getNombre().equals(nombre)) return t;
		}
		return null;
	}
	
	private void fijarEstadoGuiaEspera(){
		fijarEstadoGuia("Actualizado ...", "red");
	}
	
	private void fijarEstadoGuiaCargado(){
		fijarEstadoGuia("Actualizado", "green");
	}
	
	private void fijarEstadoGuia(String mensaje, String color) {
		estadoHTML.setHTML("<h5 style='color:" + color + "'>" + mensaje + "</h5>");
	}

}
