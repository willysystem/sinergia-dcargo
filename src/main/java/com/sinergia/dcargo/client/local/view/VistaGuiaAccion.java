package com.sinergia.dcargo.client.local.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.UtilDCargo;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.LlamadaRemotaVacia;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.event.EventoHome;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeConfirmacion;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Item;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuiaNuevo.Display;

@SuppressWarnings("deprecation")
@Singleton
public class VistaGuiaAccion extends SimplePanel /*extends DialogBox*/ implements Carga, Display {

	@Inject private GridItem3 gridItem3;
	@Inject private AdminParametros adminParametros;
	@Inject private Logger log;
	@Inject private Cargador cargador;
	//@Inject private MensajeExito        mensajeExito;
	@Inject private MensajeAviso        mensajeAviso;
	@Inject private MensajeError        mensajeError;
	@Inject private MensajeConfirmacion mensajeConfirmacion;
	@Inject private UtilDCargo          utilDCargo;
	@Inject private ImprimirPDF         imprimirPDF;
	@Inject private VistaClienteAccion  vistaClienteAccion;
	@Inject private ServicioGuiaCliente servicioGuia;
	@Inject private HandlerManager      eventBus;
	@Inject protected MainContentPresenter.Display mainContentView;
	
	private GuiaAccion guiaAccion;
	private Guia guiaSeleccionada = null;
	private Boolean isDialog = null;
	private DialogBox dialog = new DialogBox();

	private MultiWordSuggestOracle clienteOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();

	private HTML nroGuiaLabel = new HTML("<b>Nro Guia:</b>");
	private Label nroGuiaValorLabel = new Label("Sin Valor");

	private HTML nroFacturaLabel = new HTML("<b>Nro. Factura:</b>");
	private TextBox nroFacturaTextBox = new TextBox();
	private Label nroFacturaLabelValue = new Label("");
	

	private HTML fechaLabel = new HTML("<b>Fecha:</b>");
	private Label fechaValorLabel = new Label("9999/99/99");

	private HTML remiteLabel = new HTML("<b>Remitente*:</b>");
	private SuggestBox remiteSuggestBox = new SuggestBox(clienteOracle);
	private Label remiteLabelValue = new Label("");

	private HTML consigantarioLabel = new HTML("<b>Consignatario*:</b>");
	private SuggestBox consignatarioSuggestBox = new SuggestBox(clienteOracle);
	private Label consignatarioLabelValue = new Label("");

	private HTML telefonoRemiteLabel = new HTML("<b>Telefono:</b>");
	private Label telefonoRemiteValorLabel = new Label();

	private HTML telefonoConsignaLabel = new HTML("<b>Telefono:</b>");
	private Label telefonoConsignaValorLabel = new Label();

	private HTML origenLabel = new HTML("<b>Origen*:</b>");
	private SuggestBox origenSuggestBox = new SuggestBox(oficinaOracle);
	private Label origenLabelValue = new Label("");

	private HTML destinoLabel = new HTML("<b>Destino*:</b>");
	private SuggestBox destinoSuggestBox = new SuggestBox(oficinaOracle);
	private Label destinoLabelValue = new Label("");

	private HTML direccionLabel = new HTML("<b>Dirección:</b>");
	private Label direccionValorLabel = new Label();

	private Button nuevoRemitenteButton     = new Button("Nuevo Remitente");
	private Button nuevoConsignatarioButton = new Button("Nuevo Consignatario");
	
	private HTML adjuntoLabel = new HTML("<b>Adjunto:</b>");
	private TextBox adjuntoTextBox = new TextBox();
	private Label adjuntoLabelVale = new Label();
	
	private HTML resumenLabel = new HTML("<b>Resumen/C:</b>");
	private TextBox resumenTextBox = new TextBox();
	private Label resumenLabelValue = new Label();

	private HTML nroEntregaLabel = new HTML("<b>Nro de nota de entrega:</b>");
	private TextBox nroEntregaTextBox = new TextBox();
	private Label nroEntregaLabelValue = new Label();
	
	private HTML pagoTotalLabel = new HTML("<b>Pago Total*:</b>");
	private DoubleBox pagoTotalTextBox = new DoubleBox();
	private Label pagoTotalLabelValue = new Label();
	
	private HTML pagoOrigenLabel = new HTML("<b>Pago Origen*:</b>");
	private DoubleBox pagoOrigenTextBox = new DoubleBox();
	private Label pagoOrigenLabelValue = new Label();
	
	private HTML pagoDestinoLabel = new HTML("<b>Pago Destino*:</b>");
	private DoubleBox pagoDestinoTextBox = new DoubleBox();
	private Label pagoDestinoLabelValue = new Label();
	
	// Entrega
	private HTML       clienteEntregaLabel      = new HTML("<b>Nombre de quien recoje*:</b>");
	private SuggestBox clienteEntregaSuggestBox = new SuggestBox(clienteOracle);
	private Label      clienteEntregaLabelValue = new Label("");
	
	private HTML    ciEntregaLabel      = new HTML("<b>CI o NIT*:</b>");
	private TextBox ciEntregaTextBox = new TextBox();
	private Label   ciEntregaLabelValue = new Label("");
	
	private HTML    nroFacturaEntregaLabel      = new HTML("<b>Nro Factura:</b>");
	private TextBox nroFacturaEntregaTextBox    = new TextBox();
	private Label   nroFacturaEntregaLabelValue = new Label("");
	
	private HTML     notaEntregaLabel      = new HTML("<b>Comentario:</b>");
	private TextArea notaEntregaTextBox = new TextArea();
	private Label    notaEntregaLabelValue = new Label("");
	
	private HTML  fechaEntregaLabel      = new HTML("<b>Fecha:</b>");
	private Label fechaEntregaLabelValue = new Label("");
	
	private HTML  estadoLabel      = new HTML("<b>Estado:</b>");
	private Label estadoLabelValue = new Label("");
	
	private HTML  estadoPagoLabel      = new HTML("<b>Estado Pago:</b>");
	private Label estadoPagoLabelValue = new Label("");
	
	private HTML  nroConocimientoLabel      = new HTML("<b>Nro Conocimiento:</b>");
	private Label nroConocimientoLabelValue = new Label("");
	
	private HTML     obsLabel      = new HTML("<b>Observaciones:</b>");
	private TextArea obsTextBox    = new TextArea();
	private Label    obsLabelValue = new Label("");
	
//	private HTML     comentarioEntregaLabel      = new HTML("<b>Comentario:</b>");
//	private TextArea comentarioEntregaTextBox = new TextArea();
//	private Label    comentarioEntregaLabelValue = new Label("");
	
	//private CheckBox pagadoOrigenCheckBox = new CheckBox();
	//private CheckBox pagadoDestinoCheckBox = new CheckBox();
	
	private Button remitirBtn  = new Button("Remitir");
	private Button imprimirBtn = new Button("Imprimir");
	private Button entregaBtn  = new Button("Entregar");
	
	private Button homeBtn = new Button("Inicio");
	private Button salirBtn = new Button("Salir");
	private HTML estadoHTML = new HTML();
	
	
	// Validacion
	boolean remitenteValido     = false;
	boolean origenValido        = false;
	boolean consignatarioValido = false;
	boolean destinoValido       = false;
	boolean pagoTotalValido     = false;
	boolean pagoOrigenValido    = false;
	boolean pagoDestinoValido   = false;
	boolean almenosUnRegistro   = false;
	
	public VistaGuiaAccion() {
		super();
		GWT.log(this.getClass().getSimpleName() + "()");
		setStyleName("dialogo");
	}

	@PostConstruct
	protected void init() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}

	@AfterInitialization
	public void cargarDataUI() {
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
	}

	DockPanel                        dock = null;
	VerticalPanel horizontalPanelButton = null;
	
	private void construirGUI() {
		
		nroFacturaTextBox.setWidth("120px");
		nroFacturaTextBox.setStyleName("nroFactura");
		nuevoRemitenteButton.setStyleName("nuevoRemitenteConsignatarioButton");
		nuevoConsignatarioButton.setStyleName("nuevoRemitenteConsignatarioButton");
		obsTextBox.setWidth("600px");
		gridItem3.limpiarCampos();
		
		//remitirBtn.setStyleName("");
		
		horizontalPanelButton = new VerticalPanel();
		
		log.info("  remitirBtn.isVisible():" + remitirBtn.isVisible());
		
		if(isDialog) {
			dialog.setGlassEnabled(true);
			dialog.setAnimationEnabled(false);
			dialog.setText(guiaAccion.getTitulo());
		}
		
		nroGuiaValorLabel.setText(guiaSeleccionada.getNroGuia() == null ? "": ""+guiaSeleccionada.getNroGuia());
		if(guiaAccion == GuiaAccion.NUEVO)
			guiaSeleccionada.setFechaRegistro(adminParametros.getDateParam().getDate());
		fechaValorLabel.setText(DateTimeFormat.getFormat("yyyy-MM-dd H:mm:ss").format(guiaSeleccionada.getFechaRegistro()));
		

		/// 1. DATOS GENERALES
		VerticalPanel vpNorte = new VerticalPanel();
		
		if(guiaAccion == GuiaAccion.NUEVO)  vpNorte.add(new HTML("<center class='tituloModulo'>Nueva Guia</center>"));
		HorizontalPanel hpNorte = new HorizontalPanel();
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setWidth("100%");
		vpNorte.add(hpNorte);
		
	    
	    /// DATOS REMITENTE
	    FlexTable layoutRemitente = new FlexTable();
	    layoutRemitente.setCellSpacing(6);
	    FlexCellFormatter cellFormatterRemitente = layoutRemitente.getFlexCellFormatter();
	    
	    layoutRemitente.setWidget(0, 0, new HTML("<div class='tituloFormulario'>Datos Remitente</div>"));
	    cellFormatterRemitente.setColSpan(0, 0, 2);
	    cellFormatterRemitente.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    layoutRemitente.setWidget(1, 0, remiteLabel);
	    layoutRemitente.setWidget(2, 0, telefonoRemiteLabel);
	    layoutRemitente.setWidget(2, 1, telefonoRemiteValorLabel); telefonoRemiteValorLabel.setText("");
	    layoutRemitente.setWidget(3, 0, origenLabel);
	    layoutRemitente.setWidget(3, 2, nuevoRemitenteButton);
	    
	    DecoratorPanel decPanelRemitente = new DecoratorPanel();
	    decPanelRemitente.setWidget(layoutRemitente);
	    decPanelRemitente.setStyleName("formularioAgrupado");
	    
	    VerticalPanel vpRemitente = new VerticalPanel();
	    vpRemitente.add(decPanelRemitente);
	    //vpRemitente.add(nuevoRemitenteButton);
	    hpNorte.add(vpRemitente);
	    
	    hpNorte.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
	    
	    // DATOS CONSIGNATARIO
	    FlexTable layoutConsignatario = new FlexTable();
	    layoutConsignatario.setCellSpacing(6);
	    FlexCellFormatter cellFormatterConsignatario = layoutConsignatario.getFlexCellFormatter();
	    
	    layoutConsignatario.setWidget(0, 0, new HTML("<div class='tituloFormulario'>Datos Consignatario</div>"));
	    cellFormatterConsignatario.setColSpan(0, 0, 2);
	    cellFormatterConsignatario.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    layoutConsignatario.setWidget(1, 0, consigantarioLabel);
	    layoutConsignatario.setWidget(2, 0, telefonoConsignaLabel);
	    layoutConsignatario.setWidget(2, 1, telefonoConsignaValorLabel); telefonoConsignaValorLabel.setText("");
	    layoutConsignatario.setWidget(3, 0, destinoLabel);
	    layoutConsignatario.setWidget(4, 0, direccionLabel);
	    layoutConsignatario.setWidget(4, 1, direccionValorLabel); 
	    layoutConsignatario.setWidget(3, 3, nuevoConsignatarioButton);
	    
	    DecoratorPanel decPanelConsignatario = new DecoratorPanel();
	    decPanelConsignatario.setWidget(layoutConsignatario);
	    decPanelConsignatario.setStyleName("formularioAgrupado");
	    
	    VerticalPanel vpConsignatario = new VerticalPanel();
	    vpConsignatario.add(decPanelConsignatario);
	    //vpConsignatario.add(nuevoConsignatarioButton);
	    hpNorte.add(vpConsignatario);
	    hpNorte.add(new HTML("&nbsp;&nbsp;&nbsp;&nbsp;"));
		
	    /// DATOS GUIA
	    FlexTable layoutDatosGuia = new FlexTable();
	    layoutDatosGuia.setCellSpacing(6);
	    FlexCellFormatter cellFormatterDatosGuia = layoutDatosGuia.getFlexCellFormatter();
	    
	    layoutDatosGuia.setWidget(0, 0, new HTML("<div class='tituloFormulario'>Datos Guía</div>"));
	    cellFormatterDatosGuia.setColSpan(0, 0, 2);
	    cellFormatterDatosGuia.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    layoutDatosGuia.setWidget(1, 0, nroGuiaLabel);
	    layoutDatosGuia.setWidget(1, 1, nroGuiaValorLabel);
	    layoutDatosGuia.setWidget(2, 0, nroFacturaLabel);
	    //layoutDatosGuia.setWidget(2, 1, new TextBox());
	    layoutDatosGuia.setWidget(3, 0, fechaLabel);
	    layoutDatosGuia.setWidget(3, 1, fechaValorLabel);
	    layoutDatosGuia.setWidget(4, 0, new HTML("<br />"));
	    //layoutDatosGuia.setWidget(5, 0, new HTML("<br />"));
	    //layoutDatosGuia.setWidget(6, 0, new HTML("<br />"));
	    
	    DecoratorPanel decPanelDatosGuia = new DecoratorPanel();
	    decPanelDatosGuia.setWidget(layoutDatosGuia);
	    decPanelDatosGuia.setStyleName("formularioAgrupado");
	    hpNorte.add(decPanelDatosGuia);
	   
		//pagadoOrigenCheckBox.setValue(guiaSeleccionada.getPagadoOrigen());
		//pagadoDestinoCheckBox.setValue(guiaSeleccionada.getPagadoDestino());
		
		// Agregar componentes
		if(guiaAccion == GuiaAccion.NUEVO /*|| guiaAccion == GuiaAccion.MODIFICAR */){
			layoutRemitente.setWidget(1, 1, nroFacturaTextBox); nroFacturaTextBox.setValue("");
			layoutRemitente.setWidget(1, 1, remiteSuggestBox);  remiteSuggestBox.setValue("");
			layoutRemitente.setWidget(3, 1, origenSuggestBox);  remiteSuggestBox.setValue(""); 
			nuevoRemitenteButton.setVisible(true);
			
			layoutConsignatario.setWidget(1, 1, consignatarioSuggestBox); consignatarioSuggestBox.setValue("");
			layoutConsignatario.setWidget(3, 1, destinoSuggestBox);       destinoSuggestBox.setValue("");
			nuevoRemitenteButton.setVisible(true);
			
			layoutDatosGuia.setWidget(2, 1, nroFacturaTextBox);
			
			guardarOrigen();
			//pagadoOrigenCheckBox.setEnabled(true);
			//pagadoDestinoCheckBox.setEnabled(true);
			
		} 
		if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.ENTREGA || guiaAccion == GuiaAccion.MODIFICAR) {
			layoutDatosGuia.setWidget(2, 1, nroFacturaLabelValue); nroFacturaLabelValue.setText(guiaSeleccionada.getNroFactura());
			layoutDatosGuia.setWidget(4, 0, nroConocimientoLabel);
			layoutDatosGuia.setWidget(4, 1, nroConocimientoLabelValue);
			nroConocimientoLabelValue.setText(guiaSeleccionada.getConocimiento() == null ? "" : guiaSeleccionada.getConocimiento().getNroConocimiento()+"");
			layoutDatosGuia.setWidget(1, 2, estadoLabel);
			layoutDatosGuia.setWidget(1, 3, estadoLabelValue);
			layoutDatosGuia.setWidget(2, 2, estadoPagoLabel);
			layoutDatosGuia.setWidget(2, 3, estadoPagoLabelValue);
			estadoLabelValue.setText(guiaSeleccionada.getEstadoDescripcion());
			estadoPagoLabelValue.setText(guiaSeleccionada.getEstadoPagoDescripcion());
			
			if(guiaSeleccionada.getRemitente() != null) remiteLabelValue.setText(guiaSeleccionada.getRemitente().getNombre()); else remiteLabelValue.setText(""); 
			layoutRemitente.setWidget(1, 1, remiteLabelValue); 
			if(guiaSeleccionada.getOficinaOrigen() != null) origenLabelValue.setText(guiaSeleccionada.getOficinaOrigen().getNombre()); else origenLabelValue.setText(""); 
			layoutRemitente.setWidget(3, 1, origenLabelValue);
			nuevoRemitenteButton.setVisible(false);
			
			if(guiaSeleccionada.getConsignatario() != null) consignatarioLabelValue.setText(guiaSeleccionada.getConsignatario().getNombre()); else consignatarioLabelValue.setText(""); 
			layoutConsignatario.setWidget(1, 1, consignatarioLabelValue);
			if(guiaSeleccionada.getOficinaDestino() != null) destinoLabelValue.setText(guiaSeleccionada.getOficinaDestino().getNombre()); else destinoLabelValue.setText("");
			layoutConsignatario.setWidget(3, 1, destinoLabelValue);
			
			nuevoConsignatarioButton.setVisible(false);
			
			telefonoRemiteValorLabel.setText(guiaSeleccionada.getRemitente() == null ? "" : guiaSeleccionada.getRemitente().getTelefono());
			telefonoConsignaValorLabel.setText(guiaSeleccionada.getConsignatario() == null ? "" : guiaSeleccionada.getConsignatario().getTelefono());
			
			//pagadoOrigenCheckBox.setEnabled(false);
			//pagadoDestinoCheckBox.setEnabled(true);
			
			log.info("  guiaSeleccionada.getConsignatario().getDireccion(): " + guiaSeleccionada.getConsignatario().getDireccion());
			direccionValorLabel.setText(guiaSeleccionada.getConsignatario().getDireccion());
			
		}
//		if(guiaAccion == GuiaAccion.MODIFICAR) {
//			nroFacturaTextBox.setValue(guiaSeleccionada.getNroFactura());
//			if(guiaSeleccionada.getRemitente() != null) remiteSuggestBox.setValue(guiaSeleccionada.getRemitente().getNombre()); else remiteSuggestBox.setText("");
//			if(guiaSeleccionada.getOficinaOrigen() != null) origenSuggestBox.setValue(guiaSeleccionada.getOficinaOrigen().getNombre()); else origenSuggestBox.setText("");
//			if(guiaSeleccionada.getConsignatario() != null) consignatarioSuggestBox.setValue(guiaSeleccionada.getConsignatario().getNombre()); else consignatarioSuggestBox.setValue("");
//			if(guiaSeleccionada.getOficinaDestino() != null) destinoSuggestBox.setValue(guiaSeleccionada.getOficinaDestino().getNombre()); else destinoSuggestBox.setValue("");
//		}

		// 3.1. 
		VerticalPanel surPanel = new VerticalPanel();
		HorizontalPanel hpSurPanel = new HorizontalPanel();
		
		FlexTable layout2 = new FlexTable();
		layout2.setCellSpacing(6);
		FlexCellFormatter cellFormatter2 = layout2.getFlexCellFormatter();
		cellFormatter2.setColSpan(0, 0, 2);
		cellFormatter2.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		cellFormatter2.setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel resumenPanel   = new VerticalPanel();
		HorizontalPanel vPObs = new HorizontalPanel();
		
		layout2.setWidget(1, 0, adjuntoLabel);
		layout2.setWidget(2, 0, resumenLabel);
		layout2.setWidget(3, 0, nroEntregaLabel);
		layout2.setWidget(1, 6, pagoTotalLabel);
		layout2.setWidget(2, 6, pagoOrigenLabel);
		layout2.setWidget(3, 6, pagoDestinoLabel);
		//layout2.setWidget(2, 8, pagadoOrigenCheckBox);  pagadoOrigenCheckBox.setText("Pagado?");
		//layout2.setWidget(3, 8, pagadoDestinoCheckBox); pagadoDestinoCheckBox.setText("Pagado?");
		vPObs.add(obsLabel);
		VerticalPanel horizontalPanelButtons = new VerticalPanel();
		
		if(guiaAccion == GuiaAccion.NUEVO){
			layout2.setWidget(1, 1, adjuntoTextBox);     adjuntoTextBox.setValue("");
			layout2.setWidget(2, 1, resumenTextBox);     resumenTextBox.setValue(""); 
			layout2.setWidget(3, 1, nroEntregaTextBox);  nroEntregaTextBox.setValue(""); 
			
			layout2.setWidget(1, 7, pagoTotalTextBox); pagoTotalTextBox.setValue(null);
			layout2.setWidget(2, 7, pagoOrigenTextBox);  pagoOrigenTextBox.setValue(null);
			layout2.setWidget(3, 7, pagoDestinoTextBox); pagoDestinoTextBox.setValue(null);
			
			vPObs.add(obsTextBox);
			
			if(guiaAccion == GuiaAccion.NUEVO) obsTextBox.setText("");
				
			//pagadoOrigenCheckBox.setVisible(true);
			//pagadoOrigenCheckBox.setEnabled(false);
			//pagadoDestinoCheckBox.setVisible(false);
			
		} if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.ENTREGA || guiaAccion == GuiaAccion.MODIFICAR){
			layout2.setWidget(1, 1, adjuntoLabelVale);
			adjuntoLabelVale.setText(guiaSeleccionada.getAdjunto());
			layout2.setWidget(2, 1, resumenLabelValue);
			resumenLabelValue.setText(guiaSeleccionada.getResumenContenido());
			layout2.setWidget(3, 1, nroEntregaLabelValue);
			nroEntregaLabelValue.setText(guiaSeleccionada.getNroNotaEntrega());
			layout2.setWidget(1, 7, pagoTotalLabelValue);
			pagoTotalLabelValue.setText(guiaSeleccionada.getTotalGuia()+"");
			layout2.setWidget(2, 7, pagoOrigenLabelValue);
			pagoOrigenLabelValue.setText(guiaSeleccionada.getPagoOrigen()+"");
			if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR) {
				layout2.setWidget(3, 7, pagoDestinoLabelValue); pagoDestinoLabelValue.setText(guiaSeleccionada.getSaldoDestino()+"");
			} else if (guiaAccion == GuiaAccion.ENTREGA) {
				layout2.setWidget(3, 7, pagoDestinoTextBox); pagoDestinoTextBox.setValue(guiaSeleccionada.getSaldoDestino());
			}
			
			if(guiaAccion == GuiaAccion.MODIFICAR) {
				obsTextBox.setText(guiaSeleccionada.getObservaciones());
				vPObs.add(obsTextBox);
			} else {
				obsLabelValue.setText(guiaSeleccionada.getObservaciones());
				vPObs.add(obsLabelValue);
			}
			
			//pagadoOrigenCheckBox.setVisible(false);
			//pagadoDestinoCheckBox.setVisible(false);
			
			
			
		} if(/*guiaAccion == GuiaAccion.MODIFICAR ||*/ guiaAccion == GuiaAccion.ENTREGA){
			adjuntoTextBox.setValue(guiaSeleccionada.getAdjunto());
			resumenTextBox.setValue(guiaSeleccionada.getResumenContenido());
			nroEntregaTextBox.setValue(guiaSeleccionada.getNotaEntrega());
			pagoTotalTextBox.setValue(guiaSeleccionada.getTotalGuia());
			pagoOrigenTextBox.setValue(guiaSeleccionada.getPagoOrigen());
			pagoDestinoTextBox.setValue(guiaSeleccionada.getSaldoDestino());
			
			//pagadoOrigenCheckBox.setVisible(true);
			//pagadoDestinoCheckBox.setVisible(true);
		}
		if(guiaAccion == GuiaAccion.MODIFICAR) {
			
		}
		
		resumenPanel.add(layout2);
		resumenPanel.add(vPObs);
		
		VerticalPanel resumenAndEntregaPanel = new VerticalPanel();
		resumenAndEntregaPanel.add(resumenPanel);
		//hpSurPanel.add(resumenPanel);

		horizontalPanelButton = new VerticalPanel();
		horizontalPanelButton.setSpacing(5);
		
		horizontalPanelButton.clear();
		if(guiaAccion == GuiaAccion.NUEVO ) {
			horizontalPanelButton.add(remitirBtn);
			//horizontalPanelButton.add(imprimirBtn);
			if(guiaAccion == GuiaAccion.NUEVO) horizontalPanelButton.add(homeBtn);
			if(guiaAccion == GuiaAccion.MODIFICAR) horizontalPanelButton.add(salirBtn);
			remitirBtn.setEnabled(false);
			//imprimirBtn.setEnabled(false);
			gridItem3.setVisibleEditorGrid(true);
		} 
		if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR) {
			horizontalPanelButton.add(imprimirBtn); imprimirBtn.setText("Reimprimir");
			horizontalPanelButton.add(salirBtn);
			gridItem3.setVisibleEditorGrid(false);
		}
		if(guiaAccion == GuiaAccion.ENTREGA) {
			horizontalPanelButton.add(entregaBtn);
			//horizontalPanelButton.add(imprimirBtn);
			horizontalPanelButton.add(salirBtn);
			gridItem3.setVisibleEditorGrid(false);
		}
		
		horizontalPanelButtons.add(horizontalPanelButton);
		
		if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.ENTREGA) {
			FlexTable layoutEntrega = new FlexTable();
		    layoutEntrega.setCellSpacing(6);
		    layoutEntrega.setWidth("600px");
		    FlexCellFormatter cellFormatterEntrega = layoutEntrega.getFlexCellFormatter();
		    layoutEntrega.setHTML(0, 0, "<b>Datos de Entrega</b>");
		    cellFormatterEntrega.setColSpan(0, 0, 2);
		    cellFormatterEntrega.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

		    layoutEntrega.setWidget(1, 0, clienteEntregaLabel);
		    layoutEntrega.setWidget(1, 2, ciEntregaLabel);
		    layoutEntrega.setWidget(2, 0, nroFacturaEntregaLabel);
		    layoutEntrega.setWidget(2, 2, notaEntregaLabel);
		    layoutEntrega.setWidget(3, 0, fechaEntregaLabel);
		    
		    if(guiaAccion == GuiaAccion.CONSULTAR) {
			    layoutEntrega.setWidget(1, 1, clienteEntregaLabelValue);
			    layoutEntrega.setWidget(1, 3, ciEntregaLabelValue);
			    layoutEntrega.setWidget(2, 1, nroFacturaEntregaLabelValue);
			    layoutEntrega.setWidget(2, 3, notaEntregaLabelValue);
			    String formatted = guiaSeleccionada.getFechaEntrega() == null ? "" : DateTimeFormat.getFormat("yyyy-MM-dd H:mm:ss").format(guiaSeleccionada.getFechaEntrega());
			    fechaEntregaLabelValue.setText(formatted);
			    layoutEntrega.setWidget(3, 1, fechaEntregaLabelValue);
			    
			    clienteEntregaLabelValue.setText(guiaSeleccionada.getNombreClienteEntrega());
			    ciEntregaLabelValue.setText(guiaSeleccionada.getCiEntrega());
			    nroFacturaEntregaLabelValue.setText(guiaSeleccionada.getNroFacturaEntrega());
			    notaEntregaLabelValue.setText(guiaSeleccionada.getNotaEntrega());
			    
		    } else if (guiaAccion == GuiaAccion.ENTREGA) {
			    layoutEntrega.setWidget(1, 1, clienteEntregaSuggestBox);
			    layoutEntrega.setWidget(1, 3, ciEntregaTextBox);
			    layoutEntrega.setWidget(2, 1, nroFacturaEntregaTextBox);
			    layoutEntrega.setWidget(2, 3, notaEntregaTextBox);
			    layoutEntrega.setWidget(3, 1, fechaEntregaLabelValue);
			    String formatted = DateTimeFormat.getFormat("yyyy-MM-dd H:mm:ss").format(adminParametros.getDateParam().getDate());
			    log.info(" fechaEntrega: " + formatted);
			    fechaEntregaLabelValue.setText(formatted);
			    
		    }
		    
		    //layoutEntrega.setWidget(4, 0, obsLabelValue);
		    
		    DecoratorPanel decPanelEntrega = new DecoratorPanel();
		    decPanelEntrega.setWidget(layoutEntrega);
		    decPanelEntrega.setStyleName("formularioAgrupado");

		    resumenAndEntregaPanel.add(decPanelEntrega);
		    //hpSurPanel.add(layoutEntrega);
		
		}		
		horizontalPanelButtons.add(estadoHTML);
		hpSurPanel.add(resumenAndEntregaPanel);
		hpSurPanel.add(horizontalPanelButtons);
		hpSurPanel.setWidth("900px");
		
		
		surPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		surPanel.setWidth("100%");
		surPanel.add(hpSurPanel);
		
		if(guiaAccion == GuiaAccion.NUEVO ) gridItem3.setVisibleEditorGrid(true);;
		if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.ENTREGA || guiaAccion == GuiaAccion.MODIFICAR) gridItem3.setVisibleEditorGrid(false);
		if(guiaAccion == GuiaAccion.ENTREGA ) {
			clienteEntregaSuggestBox.setValue("");
			ciEntregaTextBox.setValue("");
			nroFacturaEntregaTextBox.setValue("");
			notaEntregaTextBox.setValue("");
		}
		
		if(dock == null) agregarEscuchadores();
		
		VerticalPanel vpGrid = new VerticalPanel();
		vpGrid.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpGrid.setWidth("100%");
		vpGrid.add(gridItem3);
		
		dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		dock.add(surPanel, DockPanel.SOUTH);
		
		setWidget(dock);
		
		
		log.info("   guiaSeleccionada.getEstadoDescripcion(): " + guiaSeleccionada.getEstadoDescripcion());
		if(guiaSeleccionada.getEstadoDescripcion() != null) remitirBtn.setVisible(false);
		else remitirBtn.setVisible(true);
		
		cargarOracles();
		
		if(isDialog) {
			dialog.clear();
			dialog.add(dock);
			dialog.center();
		} else {
			dock.setWidth("98%");
			mainContentView.getCentralPanel().add(dock);
		}
		
		gridItem3.setVistaGuiaAccion(this);
	}

	@Override
	public void mostrar(GuiaAccion guiaAccion, final Guia guia) {
		this.guiaAccion = guiaAccion;
		GWT.log("guiaAccion: " + guiaAccion);
		if(guiaAccion == GuiaAccion.NUEVO || guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR || guiaAccion == GuiaAccion.ENTREGA) {
			if(guiaAccion == GuiaAccion.NUEVO) {
				cargador.center();
				fijarEstadoGuia("Creando guia ... ", "green");
				servicioGuia.nuevaGuia(new MethodCallback<Guia>() {
					@Override
					public void onSuccess(Method method, Guia response) {
						guiaSeleccionada = response;
						gridItem3.setGuiaSeleccionada(guiaSeleccionada);
						log.info("guia: " + guiaSeleccionada);
						construirGUI();
						cargador.hide();
						fijarEstadoGuia("Nro de Guia generado", "green");
					}
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al generar nroGuia: " + exception.getMessage());
						mensajeError.mostrar("Error al generar nroGuia", exception);
						cargador.hide();
					}
				});
			} else if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR || guiaAccion == GuiaAccion.ENTREGA) {
				cargador.center();
				servicioGuia.consultarGuia(guia.getId(), new LlamadaRemota<Guia>("No se pude hallar información de la Guia", true) {
					@Override
					public void onSuccess(Method method, Guia response) {
						guiaSeleccionada = response;
						gridItem3.setGuiaSeleccionada(guiaSeleccionada);
						construirGUI();
						VistaGuiaAccion.this.cargador.hide();
					}
					@Override
					public void onFailure(Method method, Throwable exception) {
						GWT.log(mensajeError + ": " + exception.getMessage());
						mensajeErrorVentana.mostrar(mensajeError, exception);
						VistaGuiaAccion.this.cargador.hide();
					}
				});
			}
		}
		
	}
	
	public void fijarEstadoGuiaEspera(){
		fijarEstadoGuia("Actualizado ...", "red");
	}
	
	public void fijarEstadoGuiaCargado(){
		fijarEstadoGuia("Actualizado", "green");
	}
	
	private void fijarEstadoGuia(String mensaje, String color) {
		estadoHTML.setHTML("<h5 style='color:" + color + "'>" + mensaje + "</h5>");
	}
	
	public void cargarOracles() {
		for (Cliente cliente: adminParametros.getClientes()) {
			if(cliente.getNombre() != null)
				clienteOracle.add(cliente.getNombre());
		}
		for (Oficina oficina: adminParametros.getOficinas()) {
			oficinaOracle.add(oficina.getNombre());
		}
	}
	
	void agregarEscuchadores(){
		
		nroFacturaTextBox.addValueChangeHandler(e->{
			String nroFactura = e.getValue();	
			guiaSeleccionada.setNroFactura(nroFactura);
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setNroFactura(nroFactura);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarNroFactura(guiaTemp, new LlamadaRemota<Void>("", false) {
				@Override
				public void onSuccess(Method method, Void response) { 
					fijarEstadoGuiaCargado();
				}
			});
		 }
		);
		remiteSuggestBox.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				log.info("ddChangeListener");
				String remitenteName = remiteSuggestBox.getValue();
				Cliente cliente = adminParametros.buscarClientePorNombre(remitenteName);
				
				// Validacion
				if(remitenteName != null && cliente != null) remitenteValido = true;
				else { remitenteValido = false; }
				
				validarParaRemitir();
			}
		});
		remiteSuggestBox.addSelectionHandler( e-> {
			
			String remitenteName = e.getSelectedItem().getReplacementString();
			Cliente cliente = adminParametros.buscarClientePorNombre(remitenteName);
			
			// Validacion
			if(remitenteName != null && cliente != null) remitenteValido = true;
			else { remitenteValido = false; }
			
			telefonoRemiteValorLabel.setText(cliente.getTelefono());
			guiaSeleccionada.setRemitente(cliente);
			GWT.log("valueremitenteName: " + remitenteName);
			
			Cliente clienteTemp = new Cliente();
			clienteTemp.setId(cliente.getId());
			clienteTemp.setNombre(cliente.getNombre());
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setRemitente(clienteTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarRemitente(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Remitente", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
					validarParaRemitir();
				}
			});
		});
		consignatarioSuggestBox.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				log.info("ddChangeListener");
				String name = consignatarioSuggestBox.getValue();
				Cliente cliente = adminParametros.buscarClientePorNombre(name);
				
				// Validacion Consignatario
				if(name != null && cliente != null) consignatarioValido = true;
				else { consignatarioValido = false; }
				
				validarParaRemitir();
			}
		});
		consignatarioSuggestBox.addSelectionHandler(e->{
			String consignatarioName = e.getSelectedItem().getReplacementString();
			Cliente cliente = adminParametros.buscarClientePorNombre(consignatarioName);
			
			// Validacion Consignatario
			if(consignatarioName != null && cliente != null) consignatarioValido = true;
			else { consignatarioValido = false; }
			
			telefonoConsignaValorLabel.setText(cliente.getTelefono());
			direccionValorLabel.setText(cliente.getDireccion());
			guiaSeleccionada.setConsignatario(cliente);
			GWT.log("valueconsignatario: " + consignatarioName);
			
			Cliente clienteTemp = new Cliente();
			clienteTemp.setId(cliente.getId());
			clienteTemp.setNombre(cliente.getNombre());
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setConsignatario(clienteTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarConsignatario(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Consignatario", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
					validarParaRemitir();
				}
			});
			
		});
		origenSuggestBox.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				log.info("origenSuggestBox.addChangeListener");
				String origenName = origenSuggestBox.getValue();
				Oficina oficina = adminParametros.buscarOficinaPorNombre(origenName);
				
				// Validacion
				if(origenName != null && oficina != null) origenValido = true;
				else { origenValido = false; }
				
				validarParaRemitir();
			}
		});
		origenSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
			
			// Validacion Origen
			if(origenNombre != null && oficina != null) origenValido = true;
			else { origenValido = false; }
			
			guiaSeleccionada.setOficinaOrigen(oficina);
			
			Oficina oficinaTemp = new Oficina();
			oficinaTemp.setId(oficina.getId());
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setOficinaOrigen(oficinaTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarOrigen(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Origen", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
					validarParaRemitir();
				}
			});
		});
		destinoSuggestBox.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				String name = destinoSuggestBox.getValue();
				Oficina oficina = adminParametros.buscarOficinaPorNombre(name);
				
				// Validacion Destino
				if(name != null && oficina != null) destinoValido = true;
				else { destinoValido = false; }
				
				validarParaRemitir();
			}
		});
		destinoSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
			
			// Validacion Destino
			if(origenNombre != null && oficina != null) destinoValido = true;
			else { destinoValido = false; }
			
			guiaSeleccionada.setOficinaDestino(oficina);
			
			Oficina oficinaTemp = new Oficina();
			oficinaTemp.setId(oficina.getId());
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setOficinaDestino(oficinaTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarDestino(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Destino", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
					validarParaRemitir();
				}
			});
		});
		adjuntoTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setAdjunto(adjuntoTextBox.getValue());
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarAdjunto(guiaTemp, new LlamadaRemota<Void>("", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
			});
		});
		resumenTextBox.addValueChangeHandler(e->{
			guardarResumen();
		});
		nroEntregaTextBox.addValueChangeHandler(e->{
//			Guia guiaTemp = new Guia();
//			guiaTemp.setId(guiaSeleccionada.getId());
//			guiaTemp.setNroNotaEntrega(nroEntregaTextBox.getValue());
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarNroNotaEntrega(guiaSeleccionada.getId(), nroEntregaTextBox.getValue(), new LlamadaRemota<Void>("", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}});
		});
		pagoTotalTextBox.addValueChangeHandler(e->{
			pagarTotal(pagoTotalTextBox.getValue());
		});
		pagoOrigenTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setPagoOrigen(pagoOrigenTextBox.getValue());
			
			// Validacion Origen
			if(pagoOrigenTextBox.getValue() != null) pagoOrigenValido = true;
			else { pagoOrigenValido = false; }
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarPagoOrigen(guiaTemp, new LlamadaRemota<Void>("", false){
				@Override
				public void onSuccess(Method method, Void response) {
					guiaSeleccionada.setPagoOrigen(pagoOrigenTextBox.getValue());
					
					Double total       = guiaSeleccionada.getTotalGuia(); 
					Double pagoOrigen  = pagoOrigenTextBox.getValue();
					GWT.log("total: " + total);
					GWT.log("pagoOrigen: " + pagoOrigen);
					Double pagoDestino = total - pagoOrigen;
					pagoDestinoTextBox.setValue(pagoDestino);
					
					Guia guiaTemp = new Guia();
					guiaTemp.setId(guiaSeleccionada.getId());
					guiaTemp.setSaldoDestino(pagoDestino);
					servicioGuia.guardarPagoDestino(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
					guiaSeleccionada.setSaldoDestino(pagoDestino);
					
					// Habilitar/Desahilitar Pago Origen
					//Double pagoOrigenD = pagoOrigenTextBox.getValue();
					//if(pagoOrigenD == 0.0D) pagadoOrigenCheckBox.setEnabled(false);
					//else pagadoOrigenCheckBox.setEnabled(true);
					
					fijarEstadoGuiaCargado();
					
					// Validacion Pago destino
					if(pagoOrigenTextBox.getValue() != null) pagoDestinoValido = true;
					else { pagoDestinoValido = false; }
					
					validarParaRemitir();
				}
			});
		});
		pagoDestinoTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setSaldoDestino(pagoDestinoTextBox.getValue());
			
			// Validacion Pago destino
			if(pagoOrigenTextBox.getValue() != null) pagoDestinoValido = true;
			else { pagoDestinoValido = false; return; }
			validarParaRemitir();
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarPagoDestino(guiaTemp, new LlamadaRemota<Void>("", false){
				@Override
				public void onSuccess(Method method, Void response) {
					guiaSeleccionada.setSaldoDestino(pagoDestinoTextBox.getValue());
					
					Double total       = guiaSeleccionada.getTotalGuia(); 
					Double pagoDestino = pagoDestinoTextBox.getValue();

					
					
					GWT.log("total: " + total);
					GWT.log("pagoDestino: " + pagoDestino);
					Double pagoOrigen = total - pagoDestino;
					
					
					if(guiaAccion == GuiaAccion.ENTREGA) {
						// Total
						Double totalAux =  pagoOrigenTextBox.getValue() + pagoDestinoTextBox.getValue(); 
						pagarTotal(totalAux);
						guiaSeleccionada.setTotalGuia(totalAux);
						String totalAuxS = utilDCargo.validarNullParaMostrar(totalAux);
						pagoTotalLabelValue.setText(totalAuxS);
					} else {
						// Origen
						pagoOrigenTextBox.setValue(pagoOrigen);	
						Guia guiaTemp = new Guia();
						guiaTemp.setId(guiaSeleccionada.getId());
						guiaTemp.setPagoOrigen(pagoOrigen);
						servicioGuia.guardarPagoOrigen(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
						guiaSeleccionada.setPagoOrigen(pagoOrigen);
					}
					
					
					
					fijarEstadoGuiaCargado();
					
				}
			});
			
			
		});
		obsTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setSaldoDestino(pagoDestinoTextBox.getValue());
			
			fijarEstadoGuiaEspera();
			
			servicioGuia.guardarObservaciones(guiaSeleccionada.getId(), obsTextBox.getValue(), new LlamadaRemota<Void>("", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
			});			
		});
		
		remitirBtn.addClickHandler(e -> {
			if(validarParaRemitir()) {
				// Remitir
				mensajeConfirmacion.mostrar("Se va remitir la guía con un pago en origen de " + guiaSeleccionada.getPagoOrigen() + " Bs y en destino de " + guiaSeleccionada.getSaldoDestino() + " Bs. ¿Está de acuerdo?", new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						log.info("entro");	
						cargador.center();
						servicioGuia.cambiarEstado(guiaSeleccionada.getId(), "Remitido", new LlamadaRemota<Void>("No se pudo aceptar la Guia", true) {
							@Override
							public void onSuccess(Method method, Void response) {
								guiaSeleccionada.setEstadoDescripcion("Remitido");
								mensajeConfirmacion.hide();
								
								servicioGuia.generarNroGuia(guiaSeleccionada.getId(), new LlamadaRemota<Integer>("", true) {
									@Override
									public void onSuccess(Method method, Integer response) {
										guiaSeleccionada.setNroGuia(response);
										nroGuiaValorLabel.setText(response+"");
										remitirBtn.setVisible(false);
										
										// Generar comprobante de Pago
										Character estadoPago = determinarEstadoPago(guiaSeleccionada.getPagoOrigen(), guiaSeleccionada.getSaldoDestino());
										log.info("estadoPago:" + estadoPago );
										if(estadoPago == 'O' || estadoPago == 'Z') {
											servicioGuia.pagarOrigen(guiaSeleccionada.getId(), pagoOrigenTextBox.getValue(), "Pago Guia en el origen" + guiaSeleccionada.getNroGuia() , new LlamadaRemota<Integer>("No se pude pagar guia en el origen", false) {
												@Override
												public void onSuccess(Method method, Integer response) {
													guiaSeleccionada.setNroComprobantePagoOrigen(response);
													//mensajeAviso.mostrar("Exitosamente pagado en el origen");	
												}
											});
										} else {
											log.error("estadoPago: " + estadoPago);
										}
										
										mensajeAviso.mostrar("Guia remitida existosamente con nro: " + response);
										
										
										// Imprimir Guia
//										mensajeConfirmacion.mostrar("Guia remitida existosamente con nro: " + response + ", ¿Está listo para imprimir Guia?", new ClickHandler() {
//											@Override
//											public void onClick(ClickEvent event) {
//												imprimirGuia(guiaSeleccionada);
//												
//												// Imprimir Comprobante
//												if(estadoPago == 'O'  || estadoPago == 'Z') 
//												  mensajeConfirmacion.mostrar("¿Desea imprimir el comprobante?", new ClickHandler() {
//													@Override
//													public void onClick(ClickEvent event) {
//														String ciudad    = utilDCargo.getCiudad();
//														String direccion = utilDCargo.getDireccion();
//														String telefono  = utilDCargo.getTelefono();
//														
//														String titulo                = "COMPROBANTE DE INGRESO";
//														String numeroComprobante     = utilDCargo.validarNullParaMostrar(guiaSeleccionada.getNroComprobantePagoOrigen());
//														String fecha                 = adminParametros.getDateParam().getFormattedValue();
//														String nroGuiaOrConocimiento = utilDCargo.validarNullParaMostrar(guiaSeleccionada.getNroGuia());
//														String origen                = "";
//														String items[][] = new String[1][2];
//														
//														items[0][1] = "";
//														
//														String glosa = "Pago Guia en el origen" + guiaSeleccionada.getNroGuia();
//														String entregueConforme = "";
//														String recibiConforme = "";
//														imprimirPDF.reporteComprobante(
//																ciudad, direccion, telefono,
//																titulo, numeroComprobante, fecha, 
//																nroGuiaOrConocimiento, origen, 
//																items, glosa, 
//																entregueConforme, recibiConforme);
//														eventBus.fireEvent(new EventoHome());
//														mensajeConfirmacion.hide();
//													}
//												 }, new ClickHandler() {
//													@Override
//													public void onClick(ClickEvent event) {
//														eventBus.fireEvent(new EventoHome());
//														mensajeConfirmacion.hide();
//													}
//												 });
//												 else {
//													eventBus.fireEvent(new EventoHome());
//													mensajeConfirmacion.hide();
//												 }
//												
											}
//										}, new ClickHandler() {
//											@Override
//											public void onClick(ClickEvent event) {
//												eventBus.fireEvent(new EventoHome());
//												mensajeConfirmacion.hide();
//												log.info("cancelando impresion de guia");
//											}
//										});
//									}
								});
								VistaGuiaAccion.this.cargador.hide();
								eventBus.fireEvent(new EventoHome());
								//mensajeConfirmacion.hide();
							}
						});
					}
				});
			} else {
				VistaGuiaAccion.this.mensajeAviso.mostrar("Requiere llenar los campos obligatorioso posiblemente el origen y destino son iguales");
			}
		});
		
		imprimirBtn.addClickHandler(e->{
			log.info("e.getClass().getSimpleName(): " + e.getClass().getSimpleName());
			log.info("e.getSource(): " + ((Button)e.getSource()));
			log.info("horizontalPanelButton.getWidgetCount(): " + horizontalPanelButton.getWidgetCount());
			imprimirGuia(guiaSeleccionada);
		});
		
		clienteEntregaSuggestBox.addSelectionHandler(e -> {
			String remitenteName = e.getSelectedItem().getReplacementString();
			Cliente cliente = adminParametros.buscarClientePorNombre(remitenteName);
			if(cliente == null) {
				mensajeError.mostrar("Parametros Cliente corrupto", null);
				return;
			} 
			guiaSeleccionada.setNombreClienteEntrega(cliente.getNombre());
			guiaSeleccionada.setEntregaConsignatario(true);
			servicioGuia.guardarEntregaConsignatario(guiaSeleccionada.getId(), true, new LlamadaRemota<Void>("Error al guardar. Si es una entrega consignatario", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					ciEntregaTextBox.setValue(cliente.getCi() + " " + cliente.getNit());
					guiaSeleccionada.setEntregaConsignatario(true);
					ciEntregaTextBox.setEnabled(false);
				}
			});
		});
		clienteEntregaSuggestBox.addValueChangeHandler(e -> {
			String remitenteName = e.getValue(); //lectedItem().getReplacementString();
			final Cliente cliente = adminParametros.buscarClientePorNombre(remitenteName);
			if(cliente != null) {
				mensajeError.mostrar("Este cliente ya existe, elija de la lista sugerida", null);
				return;
			} 
			
			servicioGuia.guardarEntregaConsignatario(guiaSeleccionada.getId(), false, new LlamadaRemota<Void>("Error al guardar. Si es una entrega consignatario", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					guiaSeleccionada.setEntregaConsignatario(false);
					servicioGuia.guardarNombreClienteEntrega(guiaSeleccionada.getId(), remitenteName, new LlamadaRemota<Void>("Error al guardar nombre cliente de entrega", false) {
						@Override
						public void onSuccess(Method method, Void response) {
							guiaSeleccionada.setNombreClienteEntrega(remitenteName);
							ciEntregaTextBox.setEnabled(true);
						}
					});
				}
			});			
		});
		ciEntregaTextBox.addValueChangeHandler(e -> {
			servicioGuia.guardarCiEntrega(guiaSeleccionada.getId(), ciEntregaTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar ci o nit", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					guiaSeleccionada.setCiEntrega(ciEntregaTextBox.getValue());
				}
			});
		});
		nroFacturaEntregaTextBox.addValueChangeHandler(e -> {
			log.info("nroFacturaEntregaTextBox.getValue(): " + nroFacturaEntregaTextBox.getValue());
			servicioGuia.guardarNroFacturaEntrega(guiaSeleccionada.getId(), nroFacturaEntregaTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar ci o nit", false) {
				@Override
				public void onSuccess(Method method, Void response) {
				}
			});
		});
		
		notaEntregaTextBox.addValueChangeHandler(e -> {
			servicioGuia.guardarNotaEntrega(guiaSeleccionada.getId(), notaEntregaTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar ci o nit", false) {
				@Override
				public void onSuccess(Method method, Void response) {
				}
			});
		});
		
		nroEntregaTextBox.addValueChangeHandler(e -> {
			servicioGuia.guardarNroNotaEntrega(guiaSeleccionada.getId(), notaEntregaTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar ci o nit", false) {
				@Override
				public void onSuccess(Method method, Void response) {
				}
			});
		});
		
		entregaBtn.addClickHandler(e -> {
			if(!validarEntrega()) 	return; 
			
			mensajeConfirmacion.mostrar("¿Realmente decea Entregar esta Guía?", new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					servicioGuia.cambiarEstado(guiaSeleccionada.getId(), "Entregado", new LlamadaRemota<Void>("No se pudo guardar Estado", true) {
						@Override
						public void onSuccess(Method method, Void response) {
							guiaSeleccionada.setEstadoDescripcion("Entregado");
							
							// Generar comprobante de Pago
							Character estadoPago = determinarEstadoPago(guiaSeleccionada.getPagoOrigen(), guiaSeleccionada.getSaldoDestino());
							log.info("estadoPago:" + estadoPago );
							if(estadoPago == 'D' || estadoPago == 'Z') {
								servicioGuia.pagarDestino(guiaSeleccionada.getId(), pagoDestinoTextBox.getValue(), "Pago Guía en el origen" + guiaSeleccionada.getNroGuia() , new LlamadaRemota<Integer>("No se pude pagar guia en el origen", false) {
									@Override
									public void onSuccess(Method method, Integer response) {
										guiaSeleccionada.setNroComprobantePagoOrigen(response);
										//mensajeAviso.mostrar("Exitosamente pagado en el origen");	
									}
								});
							} else {
								log.error("estadoPago: " + estadoPago);
							}
							mensajeConfirmacion.hide();
							
							// Imprimir Comprobante
//							if(estadoPago == 'D'  || estadoPago == 'Z') 
//							  mensajeConfirmacion.mostrar("¿Desea imprimir el comprobante?", new ClickHandler() {
//								@Override
//								public void onClick(ClickEvent event) {
//									String ciudad    = utilDCargo.getCiudad();
//									String direccion = utilDCargo.getDireccion();
//									String telefono  = utilDCargo.getTelefono();
//									
//									String titulo                = "COMPROBANTE DE INGRESO";
//									String numeroComprobante     = utilDCargo.validarNullParaMostrar(guiaSeleccionada.getNroComprobantePagoOrigen());
//									String fecha                 = adminParametros.getDateParam().getFormattedValue();
//									String nroGuiaOrConocimiento = utilDCargo.validarNullParaMostrar(guiaSeleccionada.getNroGuia());
//									String origen                = "";
//									String items[][] = new String[1][2];
//									
//									items[0][1] = "";
//									
//									String glosa = "Pago Guia en el origen" + guiaSeleccionada.getNroGuia();
//									String entregueConforme = "";
//									String recibiConforme = "";
//									imprimirPDF.reporteComprobante(
//											ciudad, direccion, telefono,
//											titulo, numeroComprobante, fecha, 
//											nroGuiaOrConocimiento, origen, 
//											items, glosa, 
//											entregueConforme, recibiConforme);
//									eventBus.fireEvent(new EventoHome());
//									mensajeConfirmacion.hide();
//									dialog.hide();
//								}
//							 }, new ClickHandler() {
//								@Override
//								public void onClick(ClickEvent event) {
//									eventBus.fireEvent(new EventoHome());
//									mensajeConfirmacion.hide();
//									dialog.hide();
//								}
//							 });
//							 else {
//								eventBus.fireEvent(new EventoHome());
//								mensajeConfirmacion.hide();
//								dialog.hide();
//							 }
							
							eventBus.fireEvent(new EventoHome());
							mensajeConfirmacion.hide();
							dialog.hide();
						}
					});
				}
			});
		});
		
		homeBtn.addClickHandler(e -> {
			eventBus.fireEvent(new EventoHome());
		});
		salirBtn.addClickHandler(e -> {
			dialog.hide();
		});
		
		nuevoRemitenteButton.addClickHandler(e -> {
			vistaClienteAccion.setVistaGuiaAccion(this);
			vistaClienteAccion.mostrar(ClienteAccion.NUEVO_DESDE_GUIA, null);
		});
		
		nuevoConsignatarioButton.addClickHandler(e -> {
			vistaClienteAccion.setVistaGuiaAccion(this);
			vistaClienteAccion.mostrar(ClienteAccion.NUEVO_DESDE_GUIA, null);
		});
		
//		pagadoOrigenCheckBox.addValueChangeHandler(e -> {
//			if(e.getValue())
//				servicioGuia.pagarOrigen(guiaSeleccionada.getId(), pagoOrigenTextBox.getValue(), "Pago Guia en el origen" + guiaSeleccionada.getNroGuia() , new LlamadaRemota<Void>("No se pude pagar guia en el origen", false) {
//					@Override
//					public void onSuccess(Method method, Void response) {
//						mensajeAviso.mostrar("Exitosamente pagado en el origen");	
//					}
//				});
//			else 
//				servicioGuia.quitarPagoOrigen(guiaSeleccionada.getId(), new LlamadaRemota<Void>("No se pude pagar guia en el origen", false) {
//					@Override
//					public void onSuccess(Method method, Void response) {
//						mensajeAviso.mostrar("Exitosamente quitado el pago en el origen");	
//					}
//				});
//		});
//		pagadoDestinoCheckBox.addValueChangeHandler(e -> {
//			if(e.getValue())
//				servicioGuia.pagarDestino(guiaSeleccionada.getId(), pagoDestinoTextBox.getValue(), "Pago Guia en el destino" + guiaSeleccionada.getNroGuia() , new LlamadaRemota<Void>("No se pude pagar guia en el origen", false) {
//					@Override
//					public void onSuccess(Method method, Void response) {
//						mensajeAviso.mostrar("Exitosamente pagado en el destino");	
//					}
//				});
//			else 
//				servicioGuia.quitarPagoDestino(guiaSeleccionada.getId(), new LlamadaRemota<Void>("No se pude pagar guia en el destino", false) {
//					@Override
//					public void onSuccess(Method method, Void response) {
//						mensajeAviso.mostrar("Exitosamente quitado el pago en el destino");	
//					}
//				});
//		});
	}
	
	public void setResumen(String resumen){
		resumenTextBox.setValue(resumen);
		guardarResumen();
	}
	
	public void guardarResumen(){
		Guia guiaTemp = new Guia();
		guiaTemp.setId(guiaSeleccionada.getId());
		guiaTemp.setResumenContenido(resumenTextBox.getValue());
		servicioGuia.guardarResumen(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
	}
	
	public boolean validarParaRemitir() {
		
		log.info("--------------------------------------");
		log.info("remitenteValido: " + remitenteValido);
		log.info("origenValido: " + origenValido);
		log.info("consignatarioValido: " + consignatarioValido);
		log.info("destinoValido: " + destinoValido);
		log.info("pagoTotalValido: " + pagoTotalValido);
		log.info("pagoOrigenValido: " + pagoOrigenValido);
		log.info("pagoDestinoValido: " + pagoDestinoValido);
		log.info("almenosUnRegistro: " + almenosUnRegistro);
		
		//remite
		if(remitenteValido && origenValido && consignatarioValido && destinoValido &&  pagoTotalValido && pagoDestinoValido && almenosUnRegistro) { remitirBtn.setEnabled(true);
					//mensajeAviso.mostrar("Remiente no valido");
		} else remitirBtn.setEnabled(false);
		
		return true;
	}
	
	private boolean validarEntrega() {
		if(clienteEntregaSuggestBox.getValue() == null || clienteEntregaSuggestBox.getValue().equals("")) {
			mensajeAviso.mostrar("Debe llenar un nombre de cliente");
			return false;
		}
		if(ciEntregaTextBox.getValue() == null || ciEntregaTextBox.getValue().equals("")) {
			mensajeAviso.mostrar("Debe llenar un CI o NIT");
			return false;
		}
		// Para el Operador al Entregar validar que el destino es el mismo de la sucursal
//		if(!adminParametros.getUsuario().getAdministrador()) {
//			String oficinaUsuario = adminParametros.getUsuario().getOffice().getNombre();
//			String oficinaEntrega = guiaSeleccionada.getOficinaDestino().getNombre();
//			if(!oficinaUsuario.equals(oficinaEntrega)) {
//				mensajeAviso.mostrar("El Destino no corresponde a usuario ");
//				return false;
//			}
//		}
		return true;
	}
	
	private void guardarOrigen() {
		
		Oficina oficina = adminParametros.getUsuario().getOffice();
		guiaSeleccionada.setOficinaOrigen(oficina);
		
		Oficina oficinaTemp = new Oficina();
		oficinaTemp.setId(oficina.getId());
		Guia guiaTemp = new Guia();
		guiaTemp.setId(guiaSeleccionada.getId());
		guiaTemp.setOficinaOrigen(oficinaTemp);
		servicioGuia.guardarOrigen(guiaTemp, new LlamadaRemotaVacia<>("No se puede guardar Origen", false));
		
		origenSuggestBox.setValue(oficina.getNombre());
		
		// Validacion Origen
		if(origenSuggestBox.getValue() != null && oficina != null) origenValido = true;
		else { origenValido = false; }
		validarParaRemitir();
		
		
		if(adminParametros.getUsuario().getAdministrador()) origenSuggestBox.setEnabled(true);
		else origenSuggestBox.setEnabled(false);
	}
	
	public void imprimirGuia(Guia guiaSelecciondaPrint) {
		
		GWT.log("--> imprimir Guia: " + guiaSelecciondaPrint.getId());
		GWT.log("--> imprimir Guia.getItems(): " + guiaSelecciondaPrint.getItems().size());
		
		String items[][] = new String[guiaSelecciondaPrint.getItems().size()][7];
		int k = 0;
		int bultos = 0;
		Double peso = 0D;
		//Double total = pagoTotalTextBox.getValue();
		
		for (Item i: guiaSelecciondaPrint.getItems()) {
			//log.info("--" + k);
			log.info("--" + (k+1));
			items[k][0] = (k+1) + "";
			items[k][1] = utilDCargo.validarNullParaMostrar(i.getCantidad());
			items[k][2] = utilDCargo.validarNullParaMostrar(i.getContenido());
			items[k][3] = utilDCargo.validarNullParaMostrar(i.getPeso());
			items[k][4] = i.getUnidad() == null ? "" : utilDCargo.validarNullParaMostrar(i.getUnidad().getAbreviatura());
			items[k][5] = i.getPrecio() == null ? "" : utilDCargo.validarNullParaMostrar(i.getPrecio()) ;
			items[k][6] = i.getTotal()  == null ? "" : utilDCargo.validarNullParaMostrar(i.getTotal());
			k++;
			
			bultos = bultos + (i.getCantidad() == null ? 0 : i.getCantidad());
			peso = peso + (i.getPeso() == null ? 0.0 : i.getPeso());
			//total = i.getTotal();
			
		}
		
		String ciConsignatario = guiaSelecciondaPrint.getConsignatario() == null ? "" : utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getConsignatario().getCi());
		String nroGuia =  utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getNroGuia());
		
		String telefonoRemite = guiaSelecciondaPrint.getRemitente()     == null ? "" : utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getRemitente().getTelefono());
		String telefonoConsig = guiaSelecciondaPrint.getConsignatario() == null ? "" : utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getConsignatario().getTelefono());
		
		String remitente     = guiaSelecciondaPrint.getRemitente()      == null ? "" : utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getRemitente().getNombre()); 
		String origen        = guiaSelecciondaPrint.getOficinaOrigen()  == null ? "" : utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getOficinaOrigen().getNombre());
		String consignatario = guiaSelecciondaPrint.getConsignatario()  == null ? "" : utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getConsignatario().getNombre());
		String destino       = guiaSelecciondaPrint.getOficinaDestino() == null ? "" : utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getOficinaDestino().getNombre());
		
		String ciudad    = utilDCargo.validarNullParaMostrar(utilDCargo.getCiudad());
		String direccion = utilDCargo.validarNullParaMostrar(utilDCargo.getDireccion());
		String telefono  = utilDCargo.validarNullParaMostrar(utilDCargo.getTelefono());

		String fechaRegistro = utilDCargo.validarNullParaMostrarMedium(guiaSelecciondaPrint.getFechaRegistro());
		String pagoOrigen    = utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getPagoOrigen());
		String pagoDestino   = utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getSaldoDestino());
		String resumen       = utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getResumenContenido());
		
		String nroBultos   = utilDCargo.validarNullParaMostrar(bultos); 
		String pesoTotal   = utilDCargo.validarNullParaMostrar(peso);
		String totalPrecio = utilDCargo.validarNullParaMostrar(guiaSelecciondaPrint.getTotalGuia());
		
		String adjunto = utilDCargo.validarNullParaMostrar(adjuntoTextBox.getValue());
		
		String fechaEntrega = "";
		log.info("   guiaSelecciondaPrint.getEstadoDescripcion(): " + guiaSelecciondaPrint.getEstadoDescripcion());
		log.info("   guiaSelecciondaPrint.getEntregaConsignatario(): " + guiaSelecciondaPrint.getEntregaConsignatario());
		
		String consignatarioEntrega   = guiaSelecciondaPrint.getNombreClienteEntrega() == null ? "": guiaSelecciondaPrint.getNombreClienteEntrega();  
		ciConsignatario = guiaSelecciondaPrint.getCiEntrega() == null ? "" : guiaSelecciondaPrint.getCiEntrega();
		log.info("  consignatario: " + consignatario);
		log.info("  ciConsignatario: " + ciConsignatario);
		
		imprimirPDF.generarPDFGuia(ciudad, direccion, telefono, fechaRegistro,  nroGuia,
				remitente, telefonoRemite, origen, consignatario, telefonoConsig, destino, 
                items, nroBultos, pesoTotal, totalPrecio, resumen,
                pagoOrigen, pagoDestino, ciConsignatario, adjunto, fechaEntrega, consignatarioEntrega
		);
		
	}
	
	public void setTotalGuia(Double montoTotalGuia) {
		pagoTotalTextBox.setValue(montoTotalGuia);
		
		// Validacion Origen
		if(pagoTotalTextBox.getValue() != null) pagoTotalValido = true;
		else { pagoTotalValido = false; }
		validarParaRemitir();
	}

	public Boolean getIsDialog() {
		return isDialog;
	}
	
	@Override
	public void setIsDialog(Boolean isDialog) {
		this.isDialog = isDialog;
	}

	public boolean isAlmenosUnRegistro() {
		return almenosUnRegistro;
	}

	public void setAlmenosUnRegistro(boolean almenosUnRegistro) {
		this.almenosUnRegistro = almenosUnRegistro;
	}
	 
	private Character determinarEstadoPago(Double pagoOrigen, Double pagoDestino) {
		if(pagoOrigen  > 0 && pagoDestino  > 0) {
			return 'Z';
		} else if(pagoOrigen == 0 && pagoDestino > 0) {
			return 'D';
		} else if(pagoOrigen > 0 && pagoDestino == 0) {
			return 'O';
		}
		return null;
	}
	
	private void pagarTotal(final Double total) {
		// Validacion Total
		if(total != null) pagoTotalValido = true;
		else { pagoTotalValido = false; }
					
		servicioGuia.guardartotal(guiaSeleccionada.getId(), total, new LlamadaRemota<Void>("No se pudo guardar Total", false){
			@Override
			public void onSuccess(Method method, Void response) {
				guiaSeleccionada.setTotalGuia(total);
				if(guiaAccion == GuiaAccion.NUEVO) {
					pagoOrigenTextBox.setValue(null);	
					pagoDestinoTextBox.setValue(null);
				} 
//				else if(guiaAccion == GuiaAccion.ENTREGA) {
//					pagoOrigenTextBox.setValue(null);	
//					pagoDestinoTextBox.setValue(null);
//				}
				
				validarParaRemitir();
			}
		});
	}
	
}
