package com.sinergia.dcargo.client.local.view;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioConocimientoCliente;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dominio.Transportista;
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
	private Button nuevoOficinaButtonOne = new Button("Nuevo");
	private Button nuevoOficinaButtonTwo = new Button("Nuevo");
	
	private HTML multaLabel = new HTML("<b>Una multa de Bs*:</b>");
	private DoubleBox multaTextBox = new DoubleBox();
	private Label multaLabelVale = new Label();
	
	private HTML diasLabel = new HTML("<b>Por dia pasado los dias*: </b>");
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
	
	private HTML fleteLabel = new HTML("<b>Flete convenio Bs*: </b>");
	private DoubleBox fleteDoubleBox = new DoubleBox();
	private Label fleteLabelValue = new Label();
	
	private HTML acuentaLabel = new HTML("<b>A cuenta Bs*: </b>");
	private DoubleBox acuentaDoubleBox = new DoubleBox();
	private Label acuentaLabelValue = new Label();
	
	private HTML pagoOrigenLabel = new HTML("<b>Por pagar en Origen*: </b>");
	private DoubleBox pagoOrigenDoubleBox = new DoubleBox();
	private Label pagoOrigenLabelValue = new Label();
	
	private HTML pagoDestinoLabel = new HTML("<b>Por pagar en Destino*: </b>");
	private DoubleBox pagoDestinoDoubleBox = new DoubleBox();
	private Label pagoDestinoLabelValue = new Label();
	
	private Button aceptarBtn = new Button("Aceptar");
	private Button imprimirBtn = new Button("Imprimir");
	private Button salirBtn = new Button("Salir");
	private HTML estadoHTML  = new HTML();
	
	private TabLayoutPanel tabPanel;
	
	private Button buscarGuiasButton = new Button("Buscar Guias");
	
	DateBox fechaIniBusquedaGuia = new DateBox();
	DateBox fechaFinBusquedaGuia = new DateBox();
	
	private Conocimiento conocimientoSeleccionado;
	
	
	Widget propietarioValue = null;
	Widget conductorValue = null;
	Widget multaValue = null;
	Widget diasValue = null;
	Widget destinoValue = null;
	Widget observacionesValue = null;
	Widget adjuntoValue = null;
	Widget aclaracionValue = null;
	Widget fleteValue = null;
	Widget acuentaValue = null;
	Widget pagoOrigenValue = null;
	Widget pagoDestinoValue = null;
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
		prepararComponentes();
		vecinoLabelValor.setWidth("150px");
		ciLabelValor.setWidth("150px");
		fleteDoubleBox.setWidth("50px");
		acuentaDoubleBox.setWidth("50px");
		pagoOrigenDoubleBox.setWidth("50px");
		pagoDestinoDoubleBox.setWidth("50px");
		observacionesTextArea.setWidth("300px");
		adjuntoTextArea.setWidth("300px");
		aclaracionTextArea.setWidth("300px");
		multaTextBox.setWidth("30px");
		diasTextBox.setWidth("20px");
		
		
		conocimientoSeleccionado.setFecha(adminParametros.getDateParam().getDate());
		fechaRegistroValorLabel.setText(adminParametros.getDateParam().getFormattedValue());

		// Formulario
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.setHeight("20px");
		vpNorte.setWidth("100%");
		
		HTML htmlCenter = new HTML("<pre>                                                          </pre>");
		htmlCenter.setWidth("100%");
		
		FlexTable layoutConstante = new FlexTable();
		layoutConstante.setCellSpacing(0);
		FlexCellFormatter cellFormatter1 = layoutConstante.getFlexCellFormatter();
		layoutConstante.setWidget(0, 0, nroConocimientoLabel);      cellFormatter1.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(0, 1, nroConocimientoValorLabel); cellFormatter1.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(0, 2, new HTML("<pre>   </pre>"));
		layoutConstante.setWidget(0, 3, origenLabel);        cellFormatter1.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(0, 4, origenLabelValue);   cellFormatter1.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(0, 5, new HTML("<pre>   </pre>"));
		layoutConstante.setWidget(0, 6, destinoLabel);        cellFormatter1.setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(0, 7, destinoValue);   cellFormatter1.setHorizontalAlignment(0, 7, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(0, 8, new HTML("<pre>  </pre>"));
		//layoutConstante.setWidget(0, 2, htmlCenter);        cellFormatter1.setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER);
		layoutConstante.setWidget(0, 9, fechaRegistroLabel);        cellFormatter1.setHorizontalAlignment(0, 9, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(0, 10, fechaRegistroValorLabel);   cellFormatter1.setHorizontalAlignment(0, 10, HasHorizontalAlignment.ALIGN_LEFT);
		
		vpNorte.add(layoutConstante);
		
		tabPanel = new TabLayoutPanel(2.5, Unit.EM);
		tabPanel.setAnimationDuration(1000);
		tabPanel.getElement().getStyle().setMarginBottom(10.0, Unit.PX);
		tabPanel.setWidth("800px");
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
		
		// 3.2. Acciones
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		
		if(conocimientoAccion == ConocimientoAccion.NUEVO || conocimientoAccion == ConocimientoAccion.MODIFICAR) {
			horizontalPanelButton.add(aceptarBtn);
			horizontalPanelButton.add(imprimirBtn);
			horizontalPanelButton.add(salirBtn);
		} 
		if(conocimientoAccion == ConocimientoAccion.CONSULTAR) {
			horizontalPanelButton.add(imprimirBtn);
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
		limpiarCampos();
		
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
			} else if(conocimientoAccion == ConocimientoAccion.MODIFICAR) {
				cargador.center();
				servicioConocimiento.consultarConocimiento(conocimientoSeleccionado.getId(), new LlamadaRemota<Conocimiento>("No se puede hallar información del Conocimiento", true) {
					@Override
					public void onSuccess(Method method, Conocimiento response) {
						conocimientoSeleccionado = response;
						servicioConocimiento.cambiarEstado(conocimientoSeleccionado.getId(), "P", new LlamadaRemota<Void>("No se puede cambiar de estado", true) {
							@Override
							public void onSuccess(Method method, Void response) {
								construirGUI();
								VistaConocimientoAccion.this.cargador.hide();
							}
						});
					}
				});
				
			} else if(conocimientoAccion == ConocimientoAccion.CONSULTAR) {
				cargador.center();
				servicioConocimiento.consultarConocimiento(conocimientoSeleccionado.getId(), new LlamadaRemota<Conocimiento>("No se puede hallar información del Conocimiento", true) {
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
	
	private void prepararComponentes() {
		if(conocimientoAccion == ConocimientoAccion.NUEVO || conocimientoAccion == ConocimientoAccion.MODIFICAR){
			propietarioValue = new VerticalPanelEx(propietarioLabel, propietarioSuggestBox, nuevoOficinaButtonOne);
			conductorValue =    new VerticalPanelEx(conductorLabel,   conductorSuggestBox,   nuevoOficinaButtonTwo);
			multaValue = multaTextBox;
			diasValue = diasTextBox;
			destinoValue = destinoSuggestBox;
			observacionesValue = observacionesTextArea;
			adjuntoValue = adjuntoTextArea;
			aclaracionValue = aclaracionTextArea;
			fleteValue = fleteDoubleBox;
			acuentaValue = acuentaDoubleBox;
			pagoOrigenValue = pagoOrigenDoubleBox;
			pagoDestinoValue = pagoDestinoDoubleBox;
			if(conocimientoAccion == ConocimientoAccion.MODIFICAR){
				
				String propietario = "";
				if(conocimientoSeleccionado.getTransportistaPropietario() != null) propietario = conocimientoSeleccionado.getTransportistaPropietario().getNombre();
				propietarioSuggestBox.setValue(propietario);
				
				String conductor = "";
				if(conocimientoSeleccionado.getTransportistaConductor() != null) conductor = conocimientoSeleccionado.getTransportistaConductor().getNombre();
				conductorSuggestBox.setValue(conductor);
				
				llenarDatosConductor();
				
				multaTextBox.setValue(conocimientoSeleccionado.getMulta());
				diasTextBox.setValue(conocimientoSeleccionado.getDias());
				
				String destino = "";
				if(conocimientoSeleccionado.getOficinaDestino() != null) destino = conocimientoSeleccionado.getOficinaDestino().getNombre();  
				destinoSuggestBox.setValue(destino);
				
				observacionesTextArea.setValue(conocimientoSeleccionado.getObservacion());
				
				adjuntoTextArea.setValue(conocimientoSeleccionado.getAdjunto());
				aclaracionTextArea.setValue(conocimientoSeleccionado.getAclaracion());
				fleteDoubleBox.setValue(conocimientoSeleccionado.getFlete());
				acuentaDoubleBox.setValue(conocimientoSeleccionado.getAcuenta());
				pagoOrigenDoubleBox.setValue(conocimientoSeleccionado.getPagoOrigen());
				pagoDestinoDoubleBox.setValue(conocimientoSeleccionado.getPagoDestino());
				storeDestino.addAll(conocimientoSeleccionado.getGuias());
			}
			
		} else if(conocimientoAccion == ConocimientoAccion.CONSULTAR) {
			propietarioLabelValue.setText(conocimientoSeleccionado.getTransportistaPropietario().getNombre());
			propietarioValue = propietarioLabelValue;
			conductorLabelValue.setText(conocimientoSeleccionado.getTransportistaConductor().getNombre());
			conductorValue = conductorLabelValue;
			llenarDatosConductor();
			multaLabelVale.setText(conocimientoSeleccionado.getMulta()+"");
			multaValue = multaLabelVale;
			diasLabelValue.setText(conocimientoSeleccionado.getDias()+"");
			diasValue = diasLabelValue;
			destinoLabelValue.setText(conocimientoSeleccionado.getOficinaDestino().getNombre());
			destinoValue = destinoLabelValue;
			observacionesLabelValue.setText(conocimientoSeleccionado.getObservacion());
			observacionesValue = observacionesLabelValue;
			adjuntoLabelValue.setText(conocimientoSeleccionado.getAdjunto());
			adjuntoValue = adjuntoLabelValue;
			aclaracionLabelValue.setText(conocimientoSeleccionado.getAclaracion());
			aclaracionValue = aclaracionLabelValue;
			fleteLabelValue.setText(conocimientoSeleccionado.getFlete()+"");
			fleteValue = fleteLabelValue;
			acuentaLabelValue.setText(conocimientoSeleccionado.getAcuenta()+"");
			acuentaValue = acuentaLabelValue;
			pagoOrigenLabelValue.setText(conocimientoSeleccionado.getPagoOrigen()+"");
			pagoOrigenValue = pagoOrigenLabelValue;
			pagoDestinoLabelValue.setText(conocimientoSeleccionado.getPagoDestino()+"");
			pagoDestinoValue = pagoDestinoLabelValue;
			storeDestino.addAll(conocimientoSeleccionado.getGuias());
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
		layoutCuerpo1.add(new FlexTableForm(new HTML("<b>Yo: </b>"), propietarioValue));
		layoutCuerpo1.add(new HTML("<pre> </pre>"));
		layoutCuerpo1.add(new FlexTableForm(new HTML(""), conductorValue));
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
		vpCentro1.add(layoutCuerpo15);
		
		HorizontalPanel layoutCuerpo16 = new HorizontalPanel();
		layoutCuerpo16.add(new FlexTableForm(colorLabel, colorLabelValor));
		layoutCuerpo16.add(new HTML("<pre> </pre>"));
		layoutCuerpo16.add(new FlexTableForm(placaLabel, placaLabelValor));
		layoutCuerpo16.add(new HTML("<pre> </pre>"));
		layoutCuerpo16.add(new FlexTableForm(brevetLabel, brevetLabelValor));
		vpCentro1.add(layoutCuerpo16);
		
		HorizontalPanel layoutCuerpo2 = new HorizontalPanel();
		layoutCuerpo2.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layoutCuerpo2.add(new FlexTableForm(multaLabel, multaValue));
		layoutCuerpo2.add(new HTML("<pre> </pre>"));
		layoutCuerpo2.add(new FlexTableForm(diasLabel, diasValue));
//		layoutCuerpo2.add(new HTML("<pre> </pre>"));
//		layoutCuerpo2.add(new FlexTableForm(destinoLabel, destinoValue));
		vpCentro1.add(layoutCuerpo2);
		
		HorizontalPanel cuerpo3 = new HorizontalPanel();
		vpCentro1.add(cuerpo3);		
		
		FlexTable cuerpo31 = new FlexTable();
		cuerpo31.setCellSpacing(0);
		FlexCellFormatter cellFormatter31 = cuerpo31.getFlexCellFormatter();
		cuerpo31.setWidget(0, 0, observacionesLabel);    cellFormatter31.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo31.setWidget(0, 1, observacionesValue); cellFormatter31.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo31.setWidget(1, 0, adjuntoLabel);          cellFormatter31.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo31.setWidget(1, 1, adjuntoValue);       cellFormatter31.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo31.setWidget(2, 0, aclaracionLabel);       cellFormatter31.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo31.setWidget(2, 1, aclaracionValue);    cellFormatter31.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo3.add(cuerpo31);
		
		cuerpo3.add(new HTML("<pre>      </pre>"));
		
		FlexTable cuerpo32 = new FlexTable();
		cuerpo32.setCellSpacing(0);
		FlexCellFormatter cellFormatter32 = cuerpo32.getFlexCellFormatter();
		cuerpo32.setWidget(0, 0, fleteLabel);           cellFormatter32.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(0, 1, fleteValue);       cellFormatter32.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo32.setWidget(1, 0, acuentaLabel);         cellFormatter32.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(1, 1, acuentaValue);     cellFormatter32.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo32.setWidget(2, 0, pagoOrigenLabel);      cellFormatter32.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(2, 1, pagoOrigenValue);  cellFormatter32.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cuerpo32.setWidget(3, 0, pagoDestinoLabel);     cellFormatter32.setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		cuerpo32.setWidget(3, 1, pagoDestinoValue); cellFormatter32.setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
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
			log.info("e.getData(): " + e.getData());
			@SuppressWarnings("unchecked")
			List<Guia> guias = (List<Guia>)e.getData();
			fijarEstadoGuiaEspera();
			servicioConocimiento.quitarGuia(conocimientoSeleccionado.getId(), guias.get(0).getId(), new LlamadaRemota<Void>("", true){
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.getGuias().remove(guias.get(0));
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
			log.info("--> guias: " + guias);
			fijarEstadoGuiaEspera();
			servicioConocimiento.adicionarGuia(conocimientoSeleccionado.getId(), guias.get(0).getId(), new LlamadaRemota<Void>("", true){
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.getGuias().add(guias.get(0));
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
			String nombrePropietario = e.getSelectedItem().getReplacementString();
			
			// Guardar
			Transportista propietario = adminParametros.buscarTransportistaPorNombre(nombrePropietario);
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarPropietario(conocimientoSeleccionado.getId(), propietario.getId(), new LlamadaRemota<Void>("No se puede guardar propietario", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setTransportistaPropietario(propietario);
					fijarEstadoGuiaCargado();
				}
			});
		});
		
		conductorSuggestBox.addSelectionHandler(e -> {
			String nombreConductor = e.getSelectedItem().getReplacementString();
			Transportista conductor = adminParametros.buscarTransportistaPorNombre(nombreConductor);
			
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarConductor(conocimientoSeleccionado.getId(), conductor.getId(), new LlamadaRemota<Void>("No se puede guardar propietario", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setTransportistaConductor(conductor);
					fijarEstadoGuiaCargado();
				}
			});
			
			// Actualizar datos
			Transportista transportista = getTransportistaPorNombre(nombreConductor);
			vecinoLabelValor.setText(transportista.getVecino_de());
			ciLabelValor.setText(transportista.getCi());
			domicilioLabelValue.setText(transportista.getDireccion());
			telefonoLabelValue.setText(transportista.getTelefono());
			marcaLabelValor.setText(transportista.getMarca());	
			colorLabelValor.setText(transportista.getColor());
			placaLabelValor.setText(transportista.getPlaca());
			brevetLabelValor.setText(transportista.getBrevetCi());
			
		});
		origenSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficinaOrigen = adminParametros.buscarOficinaPorNombre(origenNombre);
			// Guardar
			guardarOrigen(oficinaOrigen);
		});
		destinoSuggestBox.addSelectionHandler(e->{
			String nombre = e.getSelectedItem().getReplacementString();
			Oficina oficinaDestino = adminParametros.buscarOficinaPorNombre(nombre);
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarOficinaDestino(conocimientoSeleccionado.getId(), oficinaDestino.getId(), new LlamadaRemota<Void>("No se puede guardar destino", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setOficinaDestino(oficinaDestino);
					fijarEstadoGuiaCargado();
				}
			});
		});
		multaTextBox.addValueChangeHandler(e -> {
			Double multa = multaTextBox.getValue();
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarMulta(conocimientoSeleccionado.getId(), multa, new LlamadaRemota<Void>("No se puede guardar multa", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setMulta(multa);
					fijarEstadoGuiaCargado();
				}
			});
		});
		diasTextBox.addValueChangeHandler(e -> {
			Integer dias = diasTextBox.getValue();
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarDias(conocimientoSeleccionado.getId(), dias, new LlamadaRemota<Void>("No se puede guardar dias", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setDias(dias);
					fijarEstadoGuiaCargado();
				}
			});
		});
		observacionesTextArea.addValueChangeHandler(e -> {
			String observaciones = observacionesTextArea.getValue();
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarObservacion(conocimientoSeleccionado.getId(), observaciones, new LlamadaRemota<Void>("No se puede guardar observación", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setObservacion(observaciones);
					fijarEstadoGuiaCargado();
				}
			});
		});
		adjuntoTextArea.addValueChangeHandler(e -> {
			String adjunto = adjuntoTextArea.getValue();
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarAdjunto(conocimientoSeleccionado.getId(), adjunto, new LlamadaRemota<Void>("No se puede guardar adjunto", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setAdjunto(adjunto);
					fijarEstadoGuiaCargado();
				}
			});
		});
		aclaracionTextArea.addValueChangeHandler(e -> {
			String aclaracion = aclaracionTextArea.getValue();
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarAclaracion(conocimientoSeleccionado.getId(), aclaracion, new LlamadaRemota<Void>("No se puede guardar aclaración", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setAclaracion(aclaracion);
					fijarEstadoGuiaCargado();
				}
			});
		});
		fleteDoubleBox.addValueChangeHandler(e -> {
			Double flete = fleteDoubleBox.getValue();
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarFlete(conocimientoSeleccionado.getId(), flete, new LlamadaRemota<Void>("No se puede guardar flete", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setFlete(flete);
					fijarEstadoGuiaCargado();
				}
			});
		});
		acuentaDoubleBox.addValueChangeHandler(e -> {
			Double acuenta = acuentaDoubleBox.getValue();
			GWT.log("acuenta: " + acuenta);
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarAcuenta(conocimientoSeleccionado.getId(), acuenta, new LlamadaRemota<Void>("No se puede guardar acuenta", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setAcuenta(acuenta);
					fijarEstadoGuiaCargado();
				}
			});
		});
		pagoOrigenDoubleBox.addValueChangeHandler(e -> {
			Double pagoOrigen = pagoOrigenDoubleBox.getValue();
			GWT.log("pagoOrigen: " + pagoOrigen);
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarPagoOrigen(conocimientoSeleccionado.getId(), pagoOrigen, new LlamadaRemota<Void>("No se puede guardar pago origen", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setPagoOrigen(pagoOrigen);
					fijarEstadoGuiaCargado();
				}
			});
		});
		pagoDestinoDoubleBox.addValueChangeHandler(e -> {
			Double pagoDestino = pagoDestinoDoubleBox.getValue();
			GWT.log("pagoDestino: " + pagoDestino);
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarPagoDestino(conocimientoSeleccionado.getId(), pagoDestino, new LlamadaRemota<Void>("No se puede guardar pago origen", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					conocimientoSeleccionado.setPagoDestino(pagoDestino);
					fijarEstadoGuiaCargado();
				}
			});
		});
		
		aceptarBtn.addClickHandler(e -> {
			if(validarConocimiento()){
				cargador.center();
				servicioConocimiento.cambiarEstado(conocimientoSeleccionado.getId(), "V", new LlamadaRemota<Void>("No se pudo aceptar la Guia", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						mensajeExito.mostrar("Conocimiento existosamente Guardado: " + conocimientoSeleccionado.getNroConocimiento());
						VistaConocimientoAccion.this.cargador.hide();
					}
				});
			} 
//			else {
//				VistaConocimientoAccion.this.mensajeAviso.mostrar("Requiere llenar los campos obligatorios");
//			}
		});
		
		imprimirBtn.addClickHandler(e -> {
			
//			validarConocimiento();
			
			if(!validarConocimiento()) {
				return ;
			}
			
			String[][] items = new String[8][conocimientoSeleccionado.getGuias().size()];
			int k = 0;
			Double totalPeso = 0D;
			int totalBultos = 0;
			Double totalOrigen = 0D;
			Double totalDestino = 0D;
			
			for (Guia g: conocimientoSeleccionado.getGuias()) {
				items[k][0] = Integer.toString(k+1);
				items[k][1] = g.getConsignatario().getNombre();
				items[k][2] = g.getNroGuia()+"";
				items[k][3] = g.getTotalPeso()+"";
				log.info("--> g.getTotalPeso(): " + g.getTotalPeso());
				totalPeso = totalPeso + g.getTotalPeso();
				items[k][4] = g.getTotalCantidad() + "";
				totalBultos = totalBultos + g.getTotalCantidad();
				items[k][5] = g.getResumenContenido();
				items[k][6] = g.getPagoOrigen() + "";
				totalOrigen = totalOrigen + g.getPagoOrigen();
				items[k][7] = g.getSaldoDestino() + "";
				totalDestino = totalDestino + g.getSaldoDestino();
				k++;
			}
			
			String fecha = conocimientoSeleccionado.getFechaRegistro().toString();  
			String nroConocimiento = conocimientoSeleccionado.getNroConocimiento().toString();
			String conductor = conocimientoSeleccionado.getTransportistaConductor().getNombre();
			String origen = conocimientoSeleccionado.getOficinaOrigen().getNombre();
			String destino = conocimientoSeleccionado.getOficinaDestino().getNombre();
			
			
			
			String marca = conocimientoSeleccionado.getTransportistaConductor().getMarca();
			String color = conocimientoSeleccionado.getTransportistaConductor().getColor();
			String placa = conocimientoSeleccionado.getTransportistaConductor().getPlaca(); 
			String vehiculo = marca + "  " + color + "  " + placa;
			
			String adjunto = conocimientoSeleccionado.getAdjunto() == null ? "": conocimientoSeleccionado.getAdjunto();
			String aclaracion = conocimientoSeleccionado.getAclaracion() == null ? "": conocimientoSeleccionado.getAclaracion();		
			
			imprimirPDF.generarPDFConocimiento(
					fecha, nroConocimiento, conductor, origen, vehiculo, destino, 
			        items, totalPeso.toString() , totalBultos + "", totalOrigen.toString(), totalDestino.toString(),
			        adjunto, aclaracion, 
			        conocimientoSeleccionado.getFlete()+"", conocimientoSeleccionado.getAcuenta().toString(), conocimientoSeleccionado.getPagoOrigen().toString(), conocimientoSeleccionado.getPagoDestino().toString()
			    );
		     
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
			
			// Validate
			if( fechaIni == null || fechaFin == null){
				mensajeAviso.mostrar("Necesitar elegir un intervalo de fecha");
				return;
			}
			if( destinoSuggestBox.getValue() == null || destinoSuggestBox.getValue().isEmpty()){
				mensajeAviso.mostrar("Necesitar elegir un destino");
				return;
			}
			
			
			Guia guia =  new Guia();
			guia.setFechaIni(fechaIni);
			guia.setFechaFin(fechaFin);
			guia.setEstadoDescripcion("Remitido");
			
			Oficina oficinaOrigen = adminParametros.buscarOficinaPorNombre(origenLabelValue.getText());
			guia.setOficinaOrigen(oficinaOrigen);
			log.info("--> oficinaOrigen: " + oficinaOrigen);
			
			Oficina oficinaDestino = adminParametros.buscarOficinaPorNombre(destinoSuggestBox.getValue());
			guia.setOficinaDestino(oficinaDestino);
			log.info("--> oficinaDestino: " + oficinaDestino);
			
			fijarEstadoGuiaEspera();
			servicioGuia.buscarGuias(guia, new LlamadaRemota<List<Guia>>("No se ejecuto correctamente la búsqueda de guias", true){
				@Override
				public void onSuccess(Method method, List<Guia> response) {
					log.info("--> Búsqueda Guias: " + response);
					
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
	
	private boolean validarConocimiento() {
		
		// Propietario
		if(conocimientoSeleccionado.getTransportistaPropietario() == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Tiene que elegir un propietario");
			return false;
		}
		String nombrePropietario = conocimientoSeleccionado.getTransportistaPropietario().getNombre();
		
		boolean propietarioValido = false;
		for (Transportista c: adminParametros.getTransportistas()) {
			if(c.getNombre().equals(nombrePropietario)) {propietarioValido = true; break;} 
		}
		log.info("--> nombrePropietario:" + nombrePropietario + ": " + propietarioValido);
		if(!propietarioValido) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("El propietario no es valido");
			return false;
		}
		
		// Conductor
		String nombreConductor = conductorSuggestBox.getValue();
		boolean conductorValido = false;
		for (Transportista c: adminParametros.getTransportistas()) {
			if(c.getNombre().equals(nombreConductor)) {conductorValido = true; break;} 
		}
		log.info("--> nombreConductor:" + nombreConductor  + ": " + conductorValido);
		if(!conductorValido) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("El conductor no es valido");
			return false;
		}
		
		// Origen
		String origen = conocimientoSeleccionado.getOficinaOrigen().getNombre();
		boolean origenValido = false;
		for (Oficina o : adminParametros.getOficinas()) {
			if(o.getNombre().equals(origen)) {origenValido = true; break;}
		}
		log.info("--> origen:" + origen  + ": " + origenValido);
		if(!origenValido) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Origen no valido");
			return false;
		}
		
		// Destino
		String destino = destinoSuggestBox.getValue();
		boolean destinoValido = false;
		for (Oficina o : adminParametros.getOficinas()) {
			if(o.getNombre().equals(destino)) {destinoValido = true; break;}
		}
		log.info("--> destino:" + destino  + ": " + destinoValido);
		if(!destinoValido) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Destino no valido");
			return false;
		}
		
		// Multa
		Double multa = multaTextBox.getValue();
		if(multa == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Escriba una multa");
			return false;
		}
		log.info("--> multa:" + multa);
		
		// Dias
		Integer dias = diasTextBox.getValue();
		if(dias == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Introduzca número de dias");
			return false;
		}
		log.info("--> dias:" + dias);
		
		// Flete 
		Double flete = fleteDoubleBox.getValue();
		if(flete == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Flete esta vacio");
			return false;
		}
		log.info("--> flete:" + flete);
		
		// Acuenta
		Double acuenta = acuentaDoubleBox.getValue();
		if(acuenta == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("A cuenta esta vacio");
			return false;
		}
		log.info("--> acuenta:" + acuenta);
		
		// Pagar en Origen
		Double pagoOrigen = pagoOrigenDoubleBox.getValue();
		if(pagoOrigen == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Pago en Origen esta vacio");
			return false;
		}
		log.info("--> pagoOrigen:" + pagoOrigen);
		
		// Pagar en Destino
		Double pagoDestino = pagoDestinoDoubleBox.getValue();
		if(pagoDestino == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Pago en Destino esta vacio");
			return false;
		}
		log.info("--> pagoDestino:" + pagoDestino);
		
		if(flete != (acuenta + pagoOrigen + pagoDestino)) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("El Flete debe ser igual a la suma de a cuenta, por pagar en origen y por pagar en el destino");
			return false;
		}
		
//		if(!propietarioValido) 	return false;
//		if(!conductorValido)    return false;
//		if(!origenValido)       return false;
//		if(!destinoValido)      return false;
//		if(multa == null)       return false;
//		if(dias == null)        return false;
//		if(flete == null)       return false;
//		if(acuenta == null)     return false;
//		if(pagoOrigen == null)  return false;
//		if(pagoDestino == null) return false;
		
		log.info("--> storeDestino.size():" + storeDestino.size());
		if(storeDestino.size() == 0) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Elija las guias");
			return false;
		}
		
		return true;
	}
	
	private void datosIniciales(){
		servicioConocimiento.getGuias(conocimientoSeleccionado.getId(), new LlamadaRemota<List<Guia>>("Error al obtener Guias del Conocimiento", false) {
			@Override
			public void onSuccess(Method method, List<Guia> response) {
				storeDestino.clear();
				storeDestino.addAll(response);
				conocimientoSeleccionado.getGuias().addAll(response);
			}
		});
		origenLabelValue.setText(adminParametros.getUsuario().getOffice().getNombre());
		guardarOrigen(adminParametros.getUsuario().getOffice());
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
	
	private void guardarOrigen(Oficina oficinaOrigen) {
		fijarEstadoGuiaEspera();
		servicioConocimiento.guardarOficinaOrigen(conocimientoSeleccionado.getId(), oficinaOrigen.getId(), new LlamadaRemota<Void>("No se puede guardar origen", false) {
			@Override
			public void onSuccess(Method method, Void response) {
				conocimientoSeleccionado.setOficinaOrigen(oficinaOrigen);
				fijarEstadoGuiaCargado();
			}
		});
	}
	
	private void limpiarCampos(){
		nroConocimientoValorLabel.setText("");
		fechaRegistroValorLabel.setText("");
		origenLabelValue.setText("");
		origenSuggestBox.setValue("");
		propietarioLabelValue.setText("");
		propietarioSuggestBox.setValue("");
		conductorLabelValue.setText("");
		conductorSuggestBox.setValue("");
		vecinoLabelValor.setText("");
		ciLabelValor.setText("");
		domicilioLabelValue.setText("");
		telefonoLabelValue.setText("");
		marcaLabelValor.setText("");
		colorLabelValor.setText("");
		placaLabelValor.setText("");
		brevetLabelValor.setText("");
		multaLabelVale.setText("");
		multaTextBox.setText("");
		diasLabelValue.setText("");
		diasTextBox.setText("");
		destinoLabelValue.setText("");
		destinoSuggestBox.setValue("");
		observacionesLabelValue.setText("");
		observacionesTextArea.setValue("");
		adjuntoLabelValue.setText("");
		adjuntoTextArea.setValue("");
		aclaracionLabelValue.setText("");
		aclaracionTextArea.setValue("");
		fleteLabelValue.setText("");
		fleteDoubleBox.setText("");
		acuentaLabelValue.setText("");
		acuentaDoubleBox.setText("");
		pagoOrigenLabelValue.setText("");
		pagoOrigenDoubleBox.setText("");
		pagoDestinoLabelValue.setText("");
		pagoDestinoDoubleBox.setText("");
		storeOrigen.clear();
		storeDestino.clear();
		
	}
	
	private void llenarDatosConductor(){
		Transportista conductor = conocimientoSeleccionado.getTransportistaConductor();
		vecinoLabelValor.setText(conductor.getVecino_de());
		ciLabelValor.setText(conductor.getCi());
		domicilioLabelValue.setText(conductor.getDireccion());
		telefonoLabelValue.setText(conductor.getTelefono());
		marcaLabelValor.setText(conductor.getMarca());
		colorLabelValor.setText(conductor.getColor());
		placaLabelValor.setText(conductor.getPlaca());
		brevetLabelValor.setText(conductor.getBrevetCi());
	}
}
