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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.UtilDCargo;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioConocimientoCliente;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.event.EventoHome;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
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
import com.sencha.gxt.widget.core.client.button.IconButton;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.RemoveEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.sinergia.dcargo.client.local.presenter.PresentadorConocimientoNuevo.Display;

@Singleton
public class VistaConocimientoAccion /*extends DialogBox*/ implements Carga, Display {

	@Inject	private AdminParametros adminParametros;
	@Inject private Logger          log;
	@Inject private Cargador        cargador;
	@Inject	private MensajeExito    mensajeExito;
	@Inject	private MensajeAviso    mensajeAviso;
	@Inject	private MensajeError    mensajeError;
	@Inject	private ImprimirPDF     imprimirPDF;
	@Inject private UtilDCargo      utilDCargo;
	@Inject private ServicioGuiaCliente servicioGuia;
	@Inject private ServicioConocimientoCliente servicioConocimiento;
	@Inject private VistaTransportistaAccion vistaTransportistaAccion;
	@Inject protected MainContentPresenter.Display mainContentView;
	@Inject private HandlerManager eventBus;
	
	private ConocimientoAccion conocimientoAccion;
	private Conocimiento conocimientoSeleccionado;
	private Boolean isDialog = null;
	private DialogBox dialog = new DialogBox();
	
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

	//private Button nuevoTransportistaButton = new Button("Nuevo Transportista");
	private Button nuevoOficinaButtonOne = new Button("Nuevo");
	private Button nuevoOficinaButtonTwo = new Button("Nuevo");
	
	private HTML multaLabel = new HTML("<b>Una multa de Bs*:</b>");
	private DoubleBox multaTextBox = new DoubleBox();
	private Label multaLabelVale = new Label();
	
	private HTML       diasLabel      = new HTML("<b>Por dia pasado los dias*: </b>");
	private IntegerBox diasTextBox    = new IntegerBox();
	private Label      diasLabelValue = new Label();
	
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
	
	private HTML  estadoLabel      = new HTML("<b>Estado:</b>");
	private Label estadoLabelValue = new Label("");
	
	private Button guardarBtn = new Button("Guardar");
	private Button guardarBorradorBtn = new Button("Guardar Borrador");
	
	private Button imprimirInternoBtn = new Button("Imprimir Interno");
	private Button imprimirExternoBtn = new Button("Imprimir Externo");
	private Button salirBtn  = new Button("Salir");
	private Button inicioBtn = new Button("Inicio");
	
	private HTML estadoHTML  = new HTML();
	
//	private TabLayoutPanel tabPanel;
	
	private Button buscarGuiasButton = new Button("Buscar Guias");
	
	DateBox fechaIniBusquedaGuia = new DateBox();
	DateBox fechaFinBusquedaGuia = new DateBox();
	
	Widget propietarioValue   = null;
	Widget conductorValue     = null;
	Widget multaValue         = null;
	Widget diasValue          = null;
	Widget origenValue        = null;
	Widget destinoValue       = null;
	Widget observacionesValue = null;
	Widget adjuntoValue       = null;
	Widget aclaracionValue    = null;
	Widget fleteValue         = null;
	Widget acuentaValue       = null;
	Widget pagoOrigenValue    = null;
	Widget pagoDestinoValue   = null;
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
	
	DockPanel dock = null;

	private void construirGUI() {
		
		nroConocimientoValorLabel.setText(conocimientoSeleccionado.getNroConocimiento() == null ? "" : ""+conocimientoSeleccionado.getNroConocimiento());
		origenSuggestBox.setWidth("100px");
		destinoSuggestBox.setWidth("100px");
		propietarioSuggestBox.setWidth("150px");
		conductorSuggestBox.setWidth("150px");
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
		
		//guardarBtn.setEnabled(false);
		if(conocimientoSeleccionado == null) {
			imprimirExternoBtn.setEnabled(false);
			imprimirInternoBtn.setEnabled(false);
		} else {
			if(conocimientoSeleccionado.getEstadoDescripcion() == null || conocimientoSeleccionado.getEstadoDescripcion().equals("")) {
				imprimirExternoBtn.setEnabled(false);
				imprimirInternoBtn.setEnabled(false);
			} else {
				if(conocimientoSeleccionado.getEstadoDescripcion().charAt(0) == 'V') {
					imprimirExternoBtn.setEnabled(true);
					imprimirInternoBtn.setEnabled(true);
				} else {
					imprimirExternoBtn.setEnabled(false);
					imprimirInternoBtn.setEnabled(false);
				}
			}
		}
		
		// Values
		conocimientoSeleccionado.setFecha(adminParametros.getDateParam().getDate());
		fechaRegistroValorLabel.setText(adminParametros.getDateParam().getFormattedValue());
		estadoLabelValue.setText(conocimientoSeleccionado.getEstadoDescripcion());

		if(isDialog) {
			dialog.setGlassEnabled(true);
			//dialog.setModal(true);
			dialog.setAnimationEnabled(false);
			dialog.setText(conocimientoAccion.getTitulo());
			
		}
		
		// Titulo
		VerticalPanel vpTituloNorte = new VerticalPanel();
		if(conocimientoAccion == ConocimientoAccion.NUEVO) vpTituloNorte.add(new HTML("<center class='tituloModulo'>Nuevo Conocimiento</center>"));
		vpTituloNorte.setHeight("20px");
		vpTituloNorte.setWidth("100%");
		
		// Datos Conocimiento
		HorizontalPanel hpConociTransportPanel = new HorizontalPanel();
		FlexTable layoutConstante = new FlexTable();
		layoutConstante.setCellSpacing(0);
		FlexCellFormatter cellFormatter1 = layoutConstante.getFlexCellFormatter();
		cellFormatter1.setColSpan(0, 0, 2);
		cellFormatter1.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		layoutConstante.setWidget(0, 0, new HTML("<div class='tituloFormulario'>Datos Conocimiento</div>"));
		layoutConstante.setWidget(1, 0, nroConocimientoLabel);      //cellFormatter1.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(1, 1, nroConocimientoValorLabel); //cellFormatter1.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(2, 0, fechaRegistroLabel);        //cellFormatter1.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(2, 1, fechaRegistroValorLabel);   //cellFormatter1.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(3, 0, origenLabel);               //cellFormatter1.setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(3, 1, origenValue);          //cellFormatter1.setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(4, 0, destinoLabel);              //cellFormatter1.setHorizontalAlignment(0, 6, HasHorizontalAlignment.ALIGN_RIGHT);
		layoutConstante.setWidget(4, 1, destinoValue);              //cellFormatter1.setHorizontalAlignment(0, 7, HasHorizontalAlignment.ALIGN_LEFT);
		layoutConstante.setWidget(5, 0, estadoLabel);
		layoutConstante.setWidget(5, 1, estadoLabelValue);
		
		//layoutConstante.setWidget(0, 8, new HTML("<pre>  </pre>"));
		
		DecoratorPanel decPanelConocimiento = new DecoratorPanel();
		decPanelConocimiento.setWidget(layoutConstante);
		decPanelConocimiento.setStyleName("formularioAgrupado");
		hpConociTransportPanel.add(decPanelConocimiento);
		hpConociTransportPanel.add(new HTML("<pre>  </pre>"));
		
		// Datos Transportista
		FlexTable layoutTrans = new FlexTable();
		layoutTrans.setCellSpacing(0);
		FlexCellFormatter cellFormatterTrans = layoutTrans.getFlexCellFormatter();
		cellFormatterTrans.setColSpan(0, 0, 2);
		cellFormatterTrans.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		layoutTrans.setWidget(0, 0, new HTML("<div class='tituloFormulario'>Datos Transportista</div>"));
		
		FlexTable layoutTransDatos1 = new FlexTable();
		layoutTransDatos1.setWidget(0, 0, new FlexTableForm(new HTML("<b>Yo: </b>"), propietarioValue));
		layoutTransDatos1.setWidget(1, 0, new FlexTableForm(new HTML("<pre>  </pre>"), conductorValue));
		layoutTrans.setWidget(1, 0, layoutTransDatos1);
		
		FlexTable layoutTransDatos2 = new FlexTable();
		layoutTransDatos2.setWidget(0, 0, new FlexTableForm(vecinoLabel, vecinoLabelValor));
		layoutTransDatos2.setWidget(1, 0, new FlexTableForm(ciLabel, ciLabelValor));
		layoutTransDatos2.setWidget(2, 0, new FlexTableForm(domicilioEnLabel, domicilioLabelValue));
		layoutTransDatos2.setWidget(3, 0, new FlexTableForm(telefonoEnLabel, telefonoLabelValue));
		layoutTransDatos2.setWidget(4, 0, new FlexTableForm(marcaLabel, marcaLabelValor));
		layoutTransDatos2.setWidget(0, 1, new FlexTableForm(colorLabel, colorLabelValor));
		layoutTransDatos2.setWidget(1, 1, new FlexTableForm(placaLabel, placaLabelValor));
		layoutTransDatos2.setWidget(2, 1, new FlexTableForm(brevetLabel, brevetLabelValor));
		layoutTransDatos2.setWidget(3, 1, new FlexTableForm(multaLabel, multaValue));
		layoutTransDatos2.setWidget(4, 1, new FlexTableForm(diasLabel, diasValue));
		layoutTrans.setWidget(1, 1, layoutTransDatos2);
			
		DecoratorPanel decPanelTransDato = new DecoratorPanel();
		decPanelTransDato.setWidget(layoutTrans);
		decPanelTransDato.setStyleName("formularioAgrupado");
		hpConociTransportPanel.add(decPanelTransDato);
		hpConociTransportPanel.add(new HTML("<pre>  </pre>"));
		
		// Costos
//		FlexTable cuerpo32 = new FlexTable();
//		cuerpo32.setCellSpacing(0);
//		FlexCellFormatter cellFormatterCosto = cuerpo32.getFlexCellFormatter();
//		cellFormatterCosto.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
//		cuerpo32.setWidget(0, 0, new HTML("<div class='tituloFormulario'>Costos</div>"));
//		cuerpo32.setWidget(0, 0, fleteLabel);           cellFormatterCosto.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
//		cuerpo32.setWidget(0, 1, fleteValue);       cellFormatterCosto.setHorizontalAlignment(0, 1, HasHorizontalAlignment.ALIGN_LEFT);
//		cuerpo32.setWidget(1, 0, acuentaLabel);         cellFormatterCosto.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
//		cuerpo32.setWidget(1, 1, acuentaValue);     cellFormatterCosto.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
//		cuerpo32.setWidget(2, 0, pagoOrigenLabel);      cellFormatterCosto.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
//		cuerpo32.setWidget(2, 1, pagoOrigenValue);  cellFormatterCosto.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
//		cuerpo32.setWidget(3, 0, pagoDestinoLabel);     cellFormatterCosto.setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
//		cuerpo32.setWidget(3, 1, pagoDestinoValue); cellFormatterCosto.setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
//		
//		DecoratorPanel decPanelCosto = new DecoratorPanel();
//		decPanelCosto.setWidget(cuerpo32);
//		decPanelCosto.setStyleName("formularioAgrupado");
//		hpConociTransportPanel.add(decPanelCosto);
		
		
		// Contenedor general
		VerticalPanel centerPanel = new VerticalPanel();
		centerPanel.setWidth("100%");
		centerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		centerPanel.add(hpConociTransportPanel);
		centerPanel.add(getCentro2());
		centerPanel.add(getCentro3());
		
		
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
			horizontalPanelButton.add(guardarBorradorBtn);
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(imprimirInternoBtn);
			horizontalPanelButton.add(imprimirExternoBtn);
			if(conocimientoAccion == ConocimientoAccion.NUEVO) horizontalPanelButton.add(inicioBtn);
			if(conocimientoAccion == ConocimientoAccion.MODIFICAR) horizontalPanelButton.add(salirBtn);
			
			buscarGuiasButton.setVisible(true);
		} 
		if(conocimientoAccion == ConocimientoAccion.CONSULTAR) {
			horizontalPanelButton.add(imprimirInternoBtn);
			horizontalPanelButton.add(imprimirExternoBtn);
			horizontalPanelButton.add(salirBtn);
			
			buscarGuiasButton.setVisible(false);
		}
		
		horizontalPanel.add(horizontalPanelButton);
		//horizontalPanel.add(estadoHTML);
		surPanel.add(horizontalPanel);
		surPanel.add(estadoHTML);
		
		if(dock == null) agregarEscuchadores();
		
		dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(vpTituloNorte, DockPanel.NORTH);
		dock.add(centerPanel, DockPanel.CENTER);
		dock.add(surPanel, DockPanel.SOUTH);

		cargarOracles();
		datosIniciales();
		
		
		// Permisos de usuario
		if(conocimientoAccion == ConocimientoAccion.MODIFICAR || conocimientoAccion == ConocimientoAccion.CONSULTAR) {
			origenSuggestBox.setValue(conocimientoSeleccionado.getOficinaOrigen().getNombre());
		} else {
			origenSuggestBox.setValue(adminParametros.getUsuario().getOffice().getNombre());
			guardarOrigen(adminParametros.getUsuario().getOffice());
		}
		
		if(adminParametros.getUsuario().getAdministrador()) origenSuggestBox.setEnabled(true);
		else origenSuggestBox.setEnabled(false);
		
		if(isDialog) {
			dialog.clear();
			//dialog.add(dock);
			dialog.setWidget(dock);
			dialog.center();
		} else {
			dock.setWidth("98%");
			mainContentView.getCentralPanel().add(dock);
		}
		
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
						construirGUI();
						VistaConocimientoAccion.this.cargador.hide();
					}
				});
				
			} else if(conocimientoAccion == ConocimientoAccion.CONSULTAR) {
				cargador.center();
				servicioConocimiento.consultarConocimiento(conocimientoSeleccionado.getId(), new LlamadaRemota<Conocimiento>("No se puede hallar información del Conocimiento", true) {
					@Override
					public void onSuccess(Method method, Conocimiento response) {
						conocimientoSeleccionado = response;
						log.info("conocimientoSeleccionado.getGuias().size(): " + response.getGuias().size());
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
			origenValue = origenSuggestBox;
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
				if(conocimientoSeleccionado.getOficinaDestino() != null) 
					destino = conocimientoSeleccionado.getOficinaDestino().getNombre();  
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
			
			
			propietarioLabelValue.setText(conocimientoSeleccionado.getTransportistaPropietario()== null ? "" : conocimientoSeleccionado.getTransportistaPropietario().getNombre());
			propietarioValue = propietarioLabelValue;
			conductorLabelValue.setText(conocimientoSeleccionado.getTransportistaConductor() == null ? "" : conocimientoSeleccionado.getTransportistaConductor().getNombre());
			conductorValue = conductorLabelValue;
			llenarDatosConductor();
			multaLabelVale.setText(conocimientoSeleccionado.getMulta()+"");
			multaValue = multaLabelVale;
			diasLabelValue.setText(conocimientoSeleccionado.getDias()+"");
			diasValue = diasLabelValue;
			origenLabelValue.setText(conocimientoSeleccionado.getOficinaOrigen().getNombre());
			origenValue = origenLabelValue;
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
	
	private VerticalPanel getCentro3() {
		
		VerticalPanel vpCentro1 = new VerticalPanel();
		vpCentro1.setHeight("20px");

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
		
		adjuntoValue.setWidth("300px");
		aclaracionValue.setWidth("300px");
		
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
		gridOrigen.setHeight(100);
		gridOrigen.setBorders(true);
		vp.add(gridOrigen);
		
		Grid<Guia> gridDestino = new Grid<>(storeDestino, createColumnList());
		gridDestino.setHeight(100);
		gridDestino.setBorders(true);
		vp.add(gridDestino);
		
		DualListField<Guia, String> dualList = new DualListField<Guia, String>(storeOrigen, storeDestino, guiaPropiedad.remitente(), new TextCell());
		dualList.addValidator(new EmptyValidator<List<Guia>>());
		dualList.setWidth("1000px");
		
		DecoratorPanel decPanelGuias = new DecoratorPanel();
		decPanelGuias.setWidget(dualList);
		decPanelGuias.setStyleName("formularioAgrupado");
		//vp.add(decPanelGuias);
		
		
		dualList.getAllLeftButton().addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				IconButton iconButton = ((IconButton)event.getSource());
				log.info("  getAllLeftButton: source: " + iconButton);
				Object o = iconButton.getData("items");
				log.info("  getAllLeftButton: source: " + iconButton);
				log.info("  getAllLeftButton: o" + o);
			}
		});
		
		dualList.getAllLeftButton().addSelectHandler(e -> {
			log.info("AllLeftButton: " + e.getSource().getClass());
		});
		dualList.getAllRightButton().addSelectHandler(e -> {
			log.info("AllRightButton: " + e.getSource().getClass());
		});
		
		dualList.getLeftButton().addSelectHandler(e -> {
			log.info("getLeftButton(): " + e.getSource().getClass());
		});
		dualList.getRightButton().addSelectHandler(e -> {
			log.info("getRightButton(): " + e.getSource().getClass());
			
		});
		
		
//		dualList.getDropTargetToField().addDropHandler(new DndDropHandler() {
//			@Override
//			public void onDrop(DndDropEvent event) {
//				log.info("onDrop");
//			}
//		});
		
		
		dualList.addAddHandler(new AddHandler() {
			@Override
			public void onAdd(AddEvent event) {
				log.info("onAdd");
			}
		});
		dualList.addRemoveHandler(new RemoveHandler() {
			@Override
			public void onRemove(RemoveEvent event) {
				log.info("onRemove");
			}
		});
		
		
		
		//dualList.add
		
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
					log.info("añadiento nueva guia");
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
		
//		ColumnConfig<Guia, Date> fechaEntregaColumn = new ColumnConfig<>(guiaPropiedad.fechaEntrega());
//		fechaEntregaColumn.setHeader(SafeHtmlUtils.fromSafeConstant("Fecha Entrega"));
//		fechaEntregaColumn.setSortable(true);
//		columns.add(fechaEntregaColumn); 
		
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
		for (Oficina of: adminParametros.getOficinas()) {
			if(of.getNombre() != null)
				oficinaOracle.add(of.getNombre());
		}
	}
	
	void agregarEscuchadores(){
		
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
			Transportista transportista = adminParametros.buscarTransportistaPorNombre(nombreConductor);//getTransportistaPorNombre(nombreConductor);
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
			GWT.log("acuenta: entro");
			Double acuenta = acuentaDoubleBox.getValue();
			GWT.log("acuenta: " + acuenta);
			// Guardar
			fijarEstadoGuiaEspera();
			servicioConocimiento.guardarAcuenta(conocimientoSeleccionado.getId(), acuenta, false, new LlamadaRemota<Void>("No se puede guardar acuenta", false) {
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
					
					Double flete       = fleteDoubleBox.getValue()      == null ? 0.0 : fleteDoubleBox.getValue();
					Double aCuenta     = acuentaDoubleBox.getValue()    == null ? 0.0 : acuentaDoubleBox.getValue();
					Double pagoOrigen  = pagoOrigenDoubleBox.getValue() == null ? 0.0 : pagoOrigenDoubleBox.getValue();
					Double pagoDestino = flete - aCuenta - pagoOrigen;
					pagoDestinoDoubleBox.setValue(pagoDestino);
					
					guardarPagoDestino();
					
					fijarEstadoGuiaCargado();
				}
			});
		});
		pagoDestinoDoubleBox.addValueChangeHandler(e -> {
			guardarPagoDestino();
		});
		
		
		guardarBorradorBtn.addClickHandler(e -> {
			cargador.center();
			servicioConocimiento.cambiarEstado(conocimientoSeleccionado.getId(), "B", new LlamadaRemota<Void>("No se pudo aceptar la Guia", true) {
				@Override
				public void onSuccess(Method method, Void response) {
					VistaConocimientoAccion.this.cargador.hide();
				}
			});
		});
		
		guardarBtn.addClickHandler(e -> {
			if(validarConocimiento()){
				cargador.center();
				servicioConocimiento.cambiarEstado(conocimientoSeleccionado.getId(), "V", new LlamadaRemota<Void>("No se pudo aceptar la Guia", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						servicioConocimiento.generarNroConocimiento(conocimientoSeleccionado.getId(), new LlamadaRemota<Integer>("", true) {
							@Override
							public void onSuccess(Method method, Integer response) {
								conocimientoSeleccionado.setNroConocimiento(response);
								conocimientoSeleccionado.setEstadoDescripcion("Vigente");
								nroConocimientoValorLabel.setText(response+"");
								guardarBtn.setEnabled(false);
								imprimirInternoBtn.setEnabled(true);
								imprimirExternoBtn.setEnabled(true);
								
								// Guardar movimiento egreso
								Double acuenta = acuentaDoubleBox.getValue();
								GWT.log("acuenta: " + acuenta);

								fijarEstadoGuiaEspera();
								servicioConocimiento.guardarAcuenta(conocimientoSeleccionado.getId(), acuenta, true, new LlamadaRemota<Void>("No se puede guardar acuenta", false) {
									@Override
									public void onSuccess(Method method, Void response) {
										conocimientoSeleccionado.setAcuenta(acuenta);
										fijarEstadoGuiaCargado();
										mensajeExito.mostrar("Conocimiento existosamente Guardado: " + nroConocimientoValorLabel.getText());
										VistaConocimientoAccion.this.cargador.hide();
									}
								});
							}
						});
					}
				});
			} 
		});
		
		imprimirInternoBtn.addClickHandler(e -> {
			
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
			items[k][1] = "Totales";
			items[k][3] = utilDCargo.validarNullParaMostrar(totalPeso);
			items[k][4] = utilDCargo.validarNullParaMostrar(totalBultos);
			items[k][6] = utilDCargo.validarNullParaMostrar(totalOrigen);
			items[k][7] = utilDCargo.validarNullParaMostrar(totalDestino);
			
			String fecha           = utilDCargo.validarNullParaMostrarMedium(conocimientoSeleccionado.getFechaRegistro());  
			String nroConocimiento = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getNroConocimiento());
			String conductor       = conocimientoSeleccionado.getTransportistaConductor() == null ? "" : utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getTransportistaConductor().getNombre());
			String origen          = conocimientoSeleccionado.getOficinaOrigen()  == null ? "" : utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getOficinaOrigen().getNombre());
			String destino         = conocimientoSeleccionado.getOficinaDestino() == null ? "" : utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getOficinaDestino().getNombre());
			
			String marca = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getTransportistaConductor().getMarca());
			String color = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getTransportistaConductor().getColor());
			String placa = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getTransportistaConductor().getPlaca()); 
			String vehiculo = marca + "  " + color + "  " + placa;
			
			String adjunto    = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getAdjunto());
			String aclaracion = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getAclaracion());		
			
			String flete   = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getFlete());
			String aCuenta = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getAcuenta());
			String pagoOrigen  = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getPagoOrigen());
			String pagoDestino = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getPagoDestino());
			
			imprimirPDF.generarPDFConocimiento(
					fecha, nroConocimiento, conductor, origen, vehiculo, destino, 
			        items, 
			        adjunto, aclaracion, 
			        flete, aCuenta, pagoOrigen, pagoDestino
			);
		     
		});
		
		imprimirExternoBtn.addClickHandler(e -> {
			String ciudadOficina    = utilDCargo.validarNullParaMostrar(utilDCargo.getCiudad());
			String direccionOficina = utilDCargo.validarNullParaMostrar(utilDCargo.getDireccion());
			String telefonoOficina  = utilDCargo.validarNullParaMostrar(utilDCargo.getTelefono());
			
			Transportista trans = conocimientoSeleccionado.getTransportistaPropietario();
			String propietatarioNombre  = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getNombre()     == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getNombre());
			String propietarioCi        = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getCi()         == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getCi());
 			String propietarioTelefono  = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getTelefono()   == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getTelefono());
 			String propietarioVecinoDe  = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getVecino_de()  == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getVecino_de());
 			String propietarioDomicilio = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getDireccion()  == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getDireccion());
 			String propietarioPlaca     = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getPlaca()      == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getPlaca());
 			String propietarioColor     = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getColor()      == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getColor());
 			String propietarioMarca     = trans == null ? "SIN DATOS" : (conocimientoSeleccionado.getTransportistaPropietario().getMarca()      == null ? "SIN DATOS" : conocimientoSeleccionado.getTransportistaPropietario().getMarca());
 			String multa                = trans == null ? "SIN DATOS" : (utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getMulta()) == null ? "SIN DATOS" : utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getMulta()));
 			String dias                 = trans == null ? "SIN DATOS" : (utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getDias())  == null ? "SIN DATOS" : utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getDias()));
 			
			String parrafo1 = "<div style =\"text-align:justify\">Yo: " + propietatarioNombre + ", vecino de " + propietarioVecinoDe + " con Carnet de identidad " + propietarioCi + ", en adelante EL TRANSPORTISTA " + 
			    " con domicilio en " + propietarioDomicilio + ", telefono " + propietarioTelefono + " soy propietario y/o conductor del camion marca " + propietarioMarca + " color " + propietarioColor + 
			    " y placa " + propietarioPlaca + ". Declaro haber recibido de los señores DCargo los bultos que abajo se detallan en buenas condiciones y comprometiendome a entregar a los mencionados en las mismas condificiones " +
			    " a los señores Trasp.XXXXXX. En caso de demora pagar una multa Bs." + multa + ".- por cada dia que transcurra, pasado los " + dias + " de la firma del presente conocimiento.</div>";
			String parrafo2 = "<div><b>Observaciones: </b> La carga va bíen revisada por el transportista y en perfectas condiciones.</div>";
			String parrafo3 = "<div style =\"text-align:justify\">El TRANSPORTISTA responde por los daños y perjuicios que resultaren por el deterioro, perdida parcial o total de la carga asignada, obligandome en toda forma de derecho a su persona. los bienes habidos y por haber y parcialmente con el camion transportador como tambien renuncia a su domicilio y demás leyes que pudieran favorecer en jucio o fuera de el firmado mancomunadalmente y solidariamente el presente documento.</div>";
			
			String nroConocimienoto = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getNroConocimiento());
			
			String items[][] = new String[conocimientoSeleccionado.getGuias().size()+1][5];
			int k = 0;
			Double pesoTotal = 0.0;
			Integer cantidad = 0;
			log.info(" imprimir: conocimientoSeleccionado.getGuias(): " + conocimientoSeleccionado.getGuias().size());
			for (Guia g : conocimientoSeleccionado.getGuias()) {
				items[k][0] = g.getConsignatario()    == null ? "" : utilDCargo.validarNullParaMostrar(g.getConsignatario().getNombre());
				items[k][1] = g.getNroGuia()          == null ? "" : utilDCargo.validarNullParaMostrar(g.getNroGuia());
				items[k][2] = g.getTotalPeso()        == null ? "" : utilDCargo.validarNullParaMostrar(g.getTotalPeso());
				items[k][3] = g.getTotalCantidad()    == null ? "" : utilDCargo.validarNullParaMostrar(g.getTotalCantidad());
				items[k][4] = g.getResumenContenido() == null ? "" : utilDCargo.validarNullParaMostrar(g.getResumenContenido());
				pesoTotal   = pesoTotal + (g.getTotalPeso()     == null ? 0.0 : g.getTotalPeso());
				cantidad    = cantidad  + (g.getTotalCantidad() == null ? 0   : g.getTotalCantidad());
				k++;
			}
			
			String pesoTotalS     = utilDCargo.validarNullParaMostrar(pesoTotal);
			String cantidadTotalS = cantidad + "";
			
			items[k][0] = "Totales:";
			items[k][2] = pesoTotalS;
			items[k][3] = cantidadTotalS;
			
			String origen = conocimientoSeleccionado.getOficinaOrigen() == null ? "" : conocimientoSeleccionado.getOficinaOrigen().getNombre();
			String fecha  = utilDCargo.validarNullParaMostrarMedium(adminParametros.getDateParam().getDate());		
			
			String adjunto    =  utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getAdjunto());
			String aclaracion =  utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getAclaracion());
					
			String flete     = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getFlete());
			String acuenta   = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getAcuenta());
			String enDestino = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getPagoOrigen());
			String saldo     = utilDCargo.validarNullParaMostrar(conocimientoSeleccionado.getPagoDestino());
			
			imprimirPDF.generarPDFExternoConocimiento(ciudadOficina, direccionOficina, telefonoOficina,
					   nroConocimienoto,
					   parrafo1, parrafo2, parrafo3,
					   origen, fecha, 
					   items, 
					   adjunto, aclaracion,
					   flete, acuenta, enDestino, saldo, 
					   propietatarioNombre);
			});
		
		inicioBtn.addClickHandler(e -> {
			eventBus.fireEvent(new EventoHome());
		});
		salirBtn.addClickHandler(e -> {
			dialog.hide();
		});
		
		nuevoOficinaButtonOne.addClickHandler(e -> {
			vistaTransportistaAccion.setVistaConocimentoAccion(this);
			log.info("nuevoOficinaButtonOne");
			vistaTransportistaAccion.mostrar(TransportistaAccion.NUEVO_DESDE_CONOCIMIENTO, null);
		});
		nuevoOficinaButtonTwo.addClickHandler(e -> {
			vistaTransportistaAccion.setVistaConocimentoAccion(this);
			log.info("nuevoOficinaButtonTwo");
			vistaTransportistaAccion.mostrar(TransportistaAccion.NUEVO_DESDE_CONOCIMIENTO, null);
		});
		
		buscarGuiasButton.addClickHandler(e -> {
			
			if( destinoSuggestBox.getValue() == null || destinoSuggestBox.getValue().isEmpty()){
				mensajeAviso.mostrar("Necesitar elegir un destino");
				return;
			}
			
			Guia guia =  new Guia();
			guia.setEstadoDescripcion("Remitido");
			
			Oficina oficinaOrigen = adminParametros.buscarOficinaPorNombre(origenSuggestBox.getText());
			guia.setOficinaOrigen(oficinaOrigen);
			log.info("--> oficinaOrigen: " + oficinaOrigen);
			
			Oficina oficinaDestino = adminParametros.buscarOficinaPorNombre(destinoSuggestBox.getValue());
			guia.setOficinaDestino(oficinaDestino);
			log.info("--> oficinaDestino: " + oficinaDestino);
			guia.setExcluirGuiasExistentesEnConocimiento(true);
			
			fijarEstadoGuiaEspera();
			servicioGuia.buscarGuias(guia, new LlamadaRemota<List<Guia>>("No se ejecuto correctamente la búsqueda de guias", true){
				@Override
				public void onSuccess(Method method, List<Guia> response) {
					log.info("--> Búsqueda Guías: " + response);
					
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
		log.info("  nombrePropietario: "+ nombrePropietario);
		
		boolean propietarioValido = false;
		Transportista transportista = adminParametros.buscarTransportistaPorNombre(nombrePropietario);
		if(transportista != null) propietarioValido = true;
		
//		for (Transportista c: adminParametros.getTransportistas()) {
//			if(c.getNombre().equals(nombrePropietario)) {propietarioValido = true; break;} 
//		}
		log.info("--> nombrePropietario:" + nombrePropietario + ": " + propietarioValido);
		if(!propietarioValido) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("El propietario no es valido");
			return false;
		}
		
		// Conductor
		String nombreConductor = conocimientoSeleccionado.getTransportistaConductor().getNombre();
		boolean conductorValido = false;
		Transportista conductor = adminParametros.buscarTransportistaPorNombre(nombreConductor);
		if(conductor != null) conductorValido = true;
//		for (Transportista c: adminParametros.getTransportistas()) {
//			if(c.getNombre().equals(nombreConductor)) {conductorValido = true; break;} 
//		}
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
		String destino = conocimientoSeleccionado.getOficinaDestino().getNombre();
		boolean destinoValido = false;
		for (Oficina o : adminParametros.getOficinas()) {
			if(o.getNombre().equals(destino)) {destinoValido = true; break;}
		}
		log.info("--> destino:" + destino  + ": " + destinoValido);
		if(!destinoValido) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Destino no valido");
			return false;
		}
		
		// Origen y destino
		if(origen.equals(destino)) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Origen y destino no pueden ser iguales");
			return false;
		}
		
		// Multa
		Double multa = conocimientoSeleccionado.getMulta();
		if(multa == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Escriba una multa");
			return false;
		}
		log.info("--> multa:" + multa);
		
		// Dias
		Integer dias = conocimientoSeleccionado.getDias();
		if(dias == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Introduzca número de dias");
			return false;
		}
		log.info("--> dias:" + dias);
		
		// Flete 
		Double flete = conocimientoSeleccionado.getFlete();
		if(flete == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Flete esta vacio");
			return false;
		}
		log.info("--> flete:" + flete);
		
		// Acuenta
		Double acuenta = conocimientoSeleccionado.getAcuenta();
		if(acuenta == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("A cuenta esta vacio");
			return false;
		}
		log.info("--> acuenta:" + acuenta);
		
		// Pagar en Origen
		Double pagoOrigen = conocimientoSeleccionado.getPagoOrigen();
		if(pagoOrigen == null) {
			VistaConocimientoAccion.this.mensajeAviso.mostrar("Pago en Origen esta vacio");
			return false;
		}
		log.info("--> pagoOrigen:" + pagoOrigen);
		
		// Pagar en Destino
		Double pagoDestino = conocimientoSeleccionado.getPagoDestino();
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
				//conocimientoSeleccionado.getGuias().addAll(response);
			}
		});
	
	}
	
//	private Transportista getTransportistaPorNombre(String nombre){
//		List<Transportista> transportistas = adminParametros.getTransportistas();
//		for(Transportista t: transportistas){
//			if(t.getNombre().equals(nombre)) return t;
//		}
//		return null;
//	}
	
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
		origenSuggestBox.setText("");
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
		if(conductor == null) return;
		
		vecinoLabelValor.setText(conductor.getVecino_de());
		ciLabelValor.setText(conductor.getCi());
		domicilioLabelValue.setText(conductor.getDireccion());
		telefonoLabelValue.setText(conductor.getTelefono());
		marcaLabelValor.setText(conductor.getMarca());
		colorLabelValor.setText(conductor.getColor());
		placaLabelValor.setText(conductor.getPlaca());
		brevetLabelValor.setText(conductor.getBrevetCi());
	}
	
	private void guardarPagoDestino() {
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
	}
	
	public Boolean getIsDialog() {
		return isDialog;
	}
	
	public void setIsDialog(Boolean isDialog) {
		this.isDialog = isDialog;
	}




}
