package com.sinergia.dcargo.client.local.view;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.LlamadaRemotaVacia;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Item;
import com.sinergia.dcargo.client.shared.Oficina;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;

@Singleton
public class VistaGuiaAccion extends DialogBox implements Carga {

	private GuiaAccion guiaAccion;
	private Guia guiaSeleccionada = null;
	
	@Inject
	private GridItem2 gridItem2;
	
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

	private MultiWordSuggestOracle clienteOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();

	private HTML nroGuiaLabel = new HTML("<b>Nro Guia:</b>");
	private Label nroGuiaValorLabel = new Label("Sin Valor");

	private HTML nroFacturaLabel = new HTML("<b>Nro. Factura:</b>");
	private TextBox nroFacturaTextBox = new TextBox();
	private Label nroFacturaLabelValue = new Label("");
	

	private HTML fechaLabel = new HTML("<b>Fecha:</b>");
	private Label fechaValorLabel = new Label("9999/99/99");

	private HTML remiteLabel = new HTML("<b>Remitemte*:</b>");
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

	private Button nuevoClienteButton = new Button("Nuevo Cliente");
	
	private HTML adjuntoLabel = new HTML("<b>Adjunto:</b>");
	private TextBox adjuntoTextBox = new TextBox();
	private Label adjuntoLabelVale = new Label();
	
	private HTML resumenLabel = new HTML("<b>Resumen/C:</b>");
	private TextBox resumenTextBox = new TextBox();
	private Label resumenLabelValue = new Label();

	private HTML nroEntregaLabel = new HTML("<b>Nro de nota de entrega:</b>");
	private TextBox nroEntregaTextBox = new TextBox();
	private Label nroEntregaLabelValue = new Label();
	
	private HTML pagoOrigenLabel = new HTML("<b>Pago Origen*:</b>");
	private DoubleBox pagoOrigenTextBox = new DoubleBox();
	private Label pagoOrigenLabelValue = new Label();
	
	private HTML pagoDestinoLabel = new HTML("<b>Pago Destino*:</b>");
	private DoubleBox pagoDestinoTextBox = new DoubleBox();
	private Label pagoDestinoLabelValue = new Label();
	
	private Button remitirBtn = new Button("Remitir");
	private Button imprimirBtn = new Button("Imprimir");
	//private Button pendienteBtn = new Button("Fijar en pendiente");
	private Button salirBtn = new Button("Salir");
	private HTML estadoHTML  = new HTML();

	public VistaGuiaAccion() {
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
		setText(guiaAccion.getTitulo());

		guiaSeleccionada.setFecha(adminParametros.getDateParam().getDate());
		fechaValorLabel.setText(adminParametros.getDateParam().getFormattedValue());

		/// 1. DATOS GENERALES
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		vpNorte.setWidth("100%");

		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);

		// Campos
		layout.setWidget(1, 0, nroGuiaLabel);      cellFormatter.setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layout.setWidget(1, 1, nroGuiaValorLabel); cellFormatter.setHorizontalAlignment(1, 1, HasHorizontalAlignment.ALIGN_LEFT);
		
		layout.setWidget(1, 2, nroFacturaLabel);   cellFormatter.setHorizontalAlignment(1, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		layout.setWidget(1, 4, fechaLabel);        cellFormatter.setHorizontalAlignment(1, 4, HasHorizontalAlignment.ALIGN_RIGHT);
		layout.setWidget(1, 5, fechaValorLabel);   cellFormatter.setHorizontalAlignment(1, 5, HasHorizontalAlignment.ALIGN_LEFT);
		
		layout.setWidget(2, 0, remiteLabel);       cellFormatter.setHorizontalAlignment(2, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		 
		layout.setWidget(2, 2, telefonoRemiteLabel);      cellFormatter.setHorizontalAlignment(2, 2, HasHorizontalAlignment.ALIGN_RIGHT); 
		layout.setWidget(2, 3, telefonoRemiteValorLabel); cellFormatter.setHorizontalAlignment(2, 3, HasHorizontalAlignment.ALIGN_LEFT);
		
		layout.setWidget(2, 4, origenLabel);   cellFormatter.setHorizontalAlignment(2, 4, HasHorizontalAlignment.ALIGN_RIGHT);
		
		layout.setWidget(3, 0, consigantarioLabel); cellFormatter.setHorizontalAlignment(3, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		
		layout.setWidget(3, 2, telefonoConsignaLabel);      cellFormatter.setHorizontalAlignment(3, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		layout.setWidget(3, 3, telefonoConsignaValorLabel); cellFormatter.setHorizontalAlignment(3, 3, HasHorizontalAlignment.ALIGN_LEFT);
		
		layout.setWidget(3, 4, destinoLabel);               cellFormatter.setHorizontalAlignment(3, 4, HasHorizontalAlignment.ALIGN_RIGHT);
		
		layout.setWidget(4, 0, direccionLabel);             cellFormatter.setHorizontalAlignment(4, 0, HasHorizontalAlignment.ALIGN_RIGHT);
		layout.setWidget(4, 1, direccionValorLabel);        cellFormatter.setHorizontalAlignment(4, 1, HasHorizontalAlignment.ALIGN_LEFT); 
		
		if(guiaAccion == GuiaAccion.NUEVO || guiaAccion == GuiaAccion.MODIFICAR){
			layout.setWidget(1, 3, nroFacturaTextBox); nroFacturaTextBox.setValue("");
			layout.setWidget(2, 1, remiteSuggestBox);  remiteSuggestBox.setValue(""); 
			layout.setWidget(2, 5, origenSuggestBox);  origenSuggestBox.setValue("");
			layout.setWidget(3, 1, consignatarioSuggestBox); consignatarioSuggestBox.setValue(""); 
			layout.setWidget(3, 5, destinoSuggestBox);       destinoSuggestBox.setValue("");   
			layout.setWidget(4, 2, nuevoClienteButton);      
		} 
		if(guiaAccion == GuiaAccion.CONSULTAR) {
			layout.setWidget(1, 3, nroFacturaLabelValue);
			cellFormatter.setHorizontalAlignment(1, 3, HasHorizontalAlignment.ALIGN_LEFT);
			nroFacturaLabelValue.setText(guiaSeleccionada.getNroFactura());
			layout.setWidget(2, 1, remiteLabelValue);
			if(guiaSeleccionada.getRemitente() != null) remiteLabelValue.setText(guiaSeleccionada.getRemitente().getNombre()); else remiteLabelValue.setText(""); 
			layout.setWidget(2, 5, origenLabelValue);
			if(guiaSeleccionada.getOficinaOrigen() != null) origenLabelValue.setText(guiaSeleccionada.getOficinaOrigen().getNombre()); else origenLabelValue.setText(""); 
			layout.setWidget(3, 1, consignatarioLabelValue);
			if(guiaSeleccionada.getConsignatario() != null) consignatarioLabelValue.setText(guiaSeleccionada.getConsignatario().getNombre()); else consignatarioLabelValue.setText(""); 
			layout.setWidget(3, 5, destinoLabelValue);
			if(guiaSeleccionada.getOficinaDestino() != null) destinoLabelValue.setText(guiaSeleccionada.getOficinaDestino().getNombre()); else destinoLabelValue.setText(""); 
		}
		if(guiaAccion == GuiaAccion.MODIFICAR) {
			nroFacturaTextBox.setValue(guiaSeleccionada.getNroFactura());
			if(guiaSeleccionada.getRemitente() != null) remiteSuggestBox.setValue(guiaSeleccionada.getRemitente().getNombre()); else remiteSuggestBox.setText("");
			if(guiaSeleccionada.getOficinaOrigen() != null) origenSuggestBox.setValue(guiaSeleccionada.getOficinaOrigen().getNombre()); else origenSuggestBox.setValue("");
			if(guiaSeleccionada.getConsignatario() != null) consignatarioSuggestBox.setValue(guiaSeleccionada.getConsignatario().getNombre()); else consignatarioSuggestBox.setValue("");
			if(guiaSeleccionada.getOficinaDestino() != null) destinoSuggestBox.setValue(guiaSeleccionada.getOficinaDestino().getNombre()); else destinoSuggestBox.setValue("");
		}
		
		cellFormatter.setHorizontalAlignment(1, 3, HasHorizontalAlignment.ALIGN_LEFT);
		cellFormatter.setHorizontalAlignment(2, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cellFormatter.setHorizontalAlignment(2, 5, HasHorizontalAlignment.ALIGN_LEFT);
		cellFormatter.setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cellFormatter.setHorizontalAlignment(3, 1, HasHorizontalAlignment.ALIGN_LEFT);
		cellFormatter.setHorizontalAlignment(3, 5, HasHorizontalAlignment.ALIGN_LEFT);
		
		vpNorte.add(layout);

		/// 2. CARGA

		/// 3. DATOS ADICIONALES
		
		// 3.1. 
		VerticalPanel surPanel = new VerticalPanel();
		surPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		surPanel.setWidth("100%");
		
		FlexTable layout2 = new FlexTable();
		layout2.setCellSpacing(6);
		FlexCellFormatter cellFormatter2 = layout2.getFlexCellFormatter();
		cellFormatter2.setColSpan(0, 0, 2);
		cellFormatter2.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		layout2.setWidget(1, 0, adjuntoLabel);
		layout2.setWidget(2, 0, resumenLabel);
		layout2.setWidget(3, 0, nroEntregaLabel);
		layout2.setWidget(1, 6, pagoOrigenLabel);
		layout2.setWidget(2, 6, pagoDestinoLabel);
		
		if(guiaAccion == GuiaAccion.NUEVO || guiaAccion == GuiaAccion.MODIFICAR){
			layout2.setWidget(1, 1, adjuntoTextBox);     adjuntoTextBox.setValue("");
			layout2.setWidget(2, 1, resumenTextBox);     resumenTextBox.setValue(""); 
			layout2.setWidget(3, 1, nroEntregaTextBox);  nroEntregaTextBox.setValue(""); 
			layout2.setWidget(1, 7, pagoOrigenTextBox);  pagoOrigenTextBox.setValue(null);
			layout2.setWidget(2, 7, pagoDestinoTextBox); pagoDestinoTextBox.setValue(null);
		} if(guiaAccion == GuiaAccion.CONSULTAR){
			layout2.setWidget(1, 1, adjuntoLabelVale);
			adjuntoLabelVale.setText(guiaSeleccionada.getAdjunto());
			layout2.setWidget(2, 1, resumenLabelValue);
			resumenLabelValue.setText(guiaSeleccionada.getResumenContenido());
			layout2.setWidget(3, 1, nroEntregaLabelValue);
			nroEntregaLabelValue.setText(guiaSeleccionada.getNotaEntrega());
			layout2.setWidget(1, 7, pagoOrigenLabelValue);
			pagoOrigenLabelValue.setText(guiaSeleccionada.getPagoOrigen()+"");
			layout2.setWidget(2, 7, pagoDestinoLabelValue);
			pagoDestinoLabelValue.setText(guiaSeleccionada.getSaldoDestino()+"");
		} if(guiaAccion == GuiaAccion.MODIFICAR){
			adjuntoTextBox.setValue(guiaSeleccionada.getAdjunto());
			resumenTextBox.setValue(guiaSeleccionada.getResumenContenido());
			nroEntregaTextBox.setValue(guiaSeleccionada.getNotaEntrega());
			pagoOrigenTextBox.setValue(guiaSeleccionada.getPagoOrigen());
			pagoDestinoTextBox.setValue(guiaSeleccionada.getSaldoDestino());
		}
		
		surPanel.add(layout2);
		
		// 3.2. Acciones
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		
		if(guiaAccion == GuiaAccion.NUEVO) {
			horizontalPanelButton.add(remitirBtn);
			horizontalPanelButton.add(imprimirBtn);
			horizontalPanelButton.add(salirBtn);
		} 
		if(guiaAccion == GuiaAccion.MODIFICAR || guiaAccion == GuiaAccion.CONSULTAR) {
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
		dock.add(gridItem2, DockPanel.CENTER);
		dock.add(surPanel, DockPanel.SOUTH);

		setWidget(dock);
		
		cargarOracles();
		agregarEscuchadores();
		center();
		
		gridItem2.setVistaGuiaAccion(this);
	}

	public void mostrar(GuiaAccion guiaAccion, final Guia guia) {
		this.guiaAccion = guiaAccion;
		GWT.log("guiaAccion: " + guiaAccion);
		if(guiaAccion == GuiaAccion.NUEVO || guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR) {
			if(guiaAccion == GuiaAccion.NUEVO) {
				cargador.center();
				fijarEstadoGuia("Creando guia ... ", "green");
				servicioGuia.nuevaGuia(new MethodCallback<Guia>() {
					@Override
					public void onSuccess(Method method, Guia response) {
						guiaSeleccionada = response;
						gridItem2.setGuiaSeleccionada(guiaSeleccionada);
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
			} else if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR) {
				cargador.center();
				servicioGuia.consultarGuia(guia.getId(), new LlamadaRemota<Guia>("No se pude hallar información de la Guia", true) {
					@Override
					public void onSuccess(Method method, Guia response) {
						guiaSeleccionada = response;
						gridItem2.setGuiaSeleccionada(guiaSeleccionada);
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
		nroGuiaValorLabel.setText(guiaSeleccionada.getNroGuia()+"");
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
		
		remiteSuggestBox.addSelectionHandler(e->{
			String remitenteName = e.getSelectedItem().getReplacementString();
			Cliente cliente = adminParametros.buscarClientePorNombre(remitenteName);
			telefonoRemiteValorLabel.setText(cliente.getTelefono());
			guiaSeleccionada.setRemitente(cliente);
			GWT.log("valueremitenteName: " + remitenteName);
			
			Cliente clienteTemp = new Cliente();
			clienteTemp.setId(cliente.getId());
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setRemitente(clienteTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarRemitente(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Remitente", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
				
			});
			
		});
		consignatarioSuggestBox.addSelectionHandler(e->{
			String consignatarioName = e.getSelectedItem().getReplacementString();
			Cliente cliente = adminParametros.buscarClientePorNombre(consignatarioName);
			telefonoConsignaValorLabel.setText(cliente.getTelefono());
			guiaSeleccionada.setConsignatario(cliente);
			GWT.log("valueconsignatario: " + consignatarioName);
			
			Cliente clienteTemp = new Cliente();
			clienteTemp.setId(cliente.getId());
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setConsignatario(clienteTemp);
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarConsignatario(guiaTemp, new LlamadaRemota<Void>("No se puede guardar Consignatario", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}
				
			});
			
		});
		origenSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
			guiaSeleccionada.setOficinaOrigen(oficina);
			
			Oficina oficinaTemp = new Oficina();
			oficinaTemp.setId(oficina.getId());
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setOficinaOrigen(oficinaTemp);
			servicioGuia.guardarOrigen(guiaTemp, new LlamadaRemotaVacia<>("No se puede guardar Origen", false));
		});
		destinoSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
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
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setNotaEntrega(nroEntregaTextBox.getValue());
			
			fijarEstadoGuiaEspera();
			servicioGuia.guardarNroEntrega(guiaTemp, new LlamadaRemota<Void>("", false){
				@Override
				public void onSuccess(Method method, Void response) {
					fijarEstadoGuiaCargado();
				}});
		});
		pagoOrigenTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setPagoOrigen(pagoOrigenTextBox.getValue());
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
					
					fijarEstadoGuiaCargado();
				}
			});
		});
		pagoDestinoTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setSaldoDestino(pagoDestinoTextBox.getValue());
			
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
					pagoOrigenTextBox.setValue(pagoOrigen);
					
					Guia guiaTemp = new Guia();
					guiaTemp.setId(guiaSeleccionada.getId());
					guiaTemp.setPagoOrigen(pagoOrigen);
					servicioGuia.guardarPagoOrigen(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
					guiaSeleccionada.setPagoOrigen(pagoOrigen);
					
					fijarEstadoGuiaCargado();
				}
			});
			
			
		});
		
		remitirBtn.addClickHandler(e -> {
			if(validarParaRemitir()){
				cargador.center();
				servicioGuia.cambiarEstado(guiaSeleccionada.getId(), "Remitido", new LlamadaRemota<Void>("No se pudo aceptar la Guia", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						mensajeExito.mostrar("Guia remitida existosamente con nro: " + guiaSeleccionada.getNroGuia());
						mensajeExito.center();
						VistaGuiaAccion.this.cargador.hide();
					}
				});
			} else {
				VistaGuiaAccion.this.mensajeAviso.mostrar("Requiere llenar los campos obligatorios");
			}
		});
		
		imprimirBtn.addClickHandler(e->{
			GWT.log("guiaSeleccionada.getItems(): " + guiaSeleccionada.getItems().size());
			String items[][] = new String[7][guiaSeleccionada.getItems().size()];
			int k = 0;
			int bultos = 0;
			Double peso = 0D;
			Double total = 0D;
			
			for (Item i: guiaSeleccionada.getItems()) {
				items[k][0] = Integer.toString(k+1);
				items[k][1] = i.getCantidad()+"";
				items[k][2] = i.getContenido();
				items[k][3] = i.getPeso()+"";
				items[k][4] = i.getUnidad().getAbreviatura()+"";
				items[k][5] = i.getPrecio().getDescripcion();
				items[k][6] = i.getTotal()+"";
				k++;
				
				bultos = bultos + i.getCantidad();
				peso = peso + i.getPeso();
				total = total = i.getTotal();
				
			}
			
			String ciRemitente = "";
			if(guiaSeleccionada.getRemitente() == null) ciRemitente = "";
			else ciRemitente = guiaSeleccionada.getRemitente().getCi();
			
			imprimirPDF.generarPDFGuia(fechaValorLabel.getText(),          nroGuiaValorLabel.getText(),
					                   remiteSuggestBox.getValue(),        telefonoRemiteValorLabel.getText(),  origenSuggestBox.getText(),
					                   consignatarioSuggestBox.getValue(), telefonoConsignaValorLabel.getText(),destinoSuggestBox.getValue(), 
					                   items, bultos+"", peso+"", total+"",  
					                   resumenTextBox.getValue(), pagoOrigenTextBox.getValue()+"", pagoDestinoTextBox.getValue()+"", 
					                   ciRemitente
		     );
		});
		
		salirBtn.addClickHandler(e -> {
			hide();
		});
		
		nuevoClienteButton.addClickHandler(e -> {
			vistaClienteAccion.setVistaGuiaAccion(this);
			vistaClienteAccion.mostrar(ClienteAccion.NUEVO_DESDE_GUIA, null);
		});
		
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
	
	private boolean validarParaRemitir() {
		
		// remite
		GWT.log("-->remiteSuggestBox.getValue(): " + remiteSuggestBox.getValue());
		String remite = remiteSuggestBox.getValue();
		boolean remiteValido = false;
		for (Cliente c: adminParametros.getClientes()) {
			if(c.getNombre().equals(remite)) {remiteValido = true; break;} 
		}
		
		// consignatario
		GWT.log("-->consignatarioSuggestBox.getValue(): " + consignatarioSuggestBox.getValue());
		String consignatario = consignatarioSuggestBox.getValue();
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
		GWT.log("-->pagoOrigenTextBox.getValue(): " + pagoOrigenTextBox.getValue());
		Double pagoOrigen = pagoOrigenTextBox.getValue();
		
		// pago destino
		GWT.log("-->pagoDestinoTextBox.getValue(): " + pagoDestinoTextBox.getValue());
		Double pagoDestino = pagoDestinoTextBox.getValue();
		
		if(!remiteValido) 		 return false;
		if(!consignatarioValido) return false;
		if(!origenValido)        return false;
		if(!destinoValido)       return false;
		if(pagoOrigen == null)  return false;
		if(pagoDestino == null)  return false;
		
		return true;
	}

}
