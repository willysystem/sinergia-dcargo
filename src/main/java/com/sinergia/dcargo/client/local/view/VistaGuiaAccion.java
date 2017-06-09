package com.sinergia.dcargo.client.local.view;

import java.util.HashSet;

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
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Oficina;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;

@Singleton
public class VistaGuiaAccion extends DialogBox {

	private GuiaAccion guiaAccion;
	private Guia guiaSeleccionada = null;
	
//	@Inject
//	private GridItem1 gridItem;
	
	@Inject
	private GridItem2 gridItem2;
	
	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	private Logger log;
	 
	@Inject 
	private Cargador cargador;
	
	//private ServicioItemCliente servicioItem = GWT.create(ServicioItemCliente.class);
	private ServicioGuiaCliente servicioGuia = GWT.create(ServicioGuiaCliente.class);

	private MultiWordSuggestOracle clienteOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();

	private HTML nroGuiaLabel = new HTML("<b>Nro Guia:</b>");
	private Label nroGuiaValorLabel = new Label("Sin Valor");

	private HTML nroFacturaLabel = new HTML("<b>Nro. Factura:</b>");
	private TextBox nroFacturaTextBox = new TextBox();
	private Label nroFacturaLabelValue = new Label("");
	

	private HTML fechaLabel = new HTML("<b>Fecha:</b>");
	private Label fechaValorLabel = new Label("9999/99/99");

	private HTML remiteLabel = new HTML("<b>Remitemte:</b>");
	private SuggestBox remiteSuggestBox = new SuggestBox(clienteOracle);
	private Label remiteLabelValue = new Label("");

	private HTML consigantarioLabel = new HTML("<b>Consignatario:</b>");
	private SuggestBox consignatarioSuggestBox = new SuggestBox(clienteOracle);
	private Label consignatarioLabelValue = new Label("");

	private HTML telefonoRemiteLabel = new HTML("<b>Telefono:</b>");
	private Label telefonoRemiteValorLabel = new Label();

	private HTML telefonoConsignaLabel = new HTML("<b>Telefono:</b>");
	private Label telefonoConsignaValorLabel = new Label();

	private HTML origenLabel = new HTML("<b>Origen:</b>");
	private SuggestBox origenSuggestBox = new SuggestBox(oficinaOracle);
	private Label origenLabelValue = new Label("");

	private HTML destinoLabel = new HTML("<b>Destino:</b>");
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
	
	private HTML pagoOrigenLabel = new HTML("<b>Pago Origen:</b>");
	private DoubleBox pagoOrigenTextBox = new DoubleBox();
	private Label pagoOrigenLabelValue = new Label();
	
	private HTML pagoDestinoLabel = new HTML("<b>Pago Destino:</b>");
	private DoubleBox pagoDestinoTextBox = new DoubleBox();
	private Label pagoDestinoLabelValue = new Label();
	
	private Button aceptarBtn = new Button("Aceptar");
	private Button cancelarBtn = new Button("Cancelar");
	//private Button salirBtn = new Button("Salir");
	

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
		horizontalPanelButton.add(aceptarBtn);
		horizontalPanelButton.add(cancelarBtn);
		//horizontalPanelButton.add(salirBtn);
		horizontalPanel.add(horizontalPanelButton);
		surPanel.add(horizontalPanel);
		
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
	}

	public void mostrar(GuiaAccion guiaAccion, final Guia guia) {
		this.guiaAccion = guiaAccion;
		GWT.log("guiaAccion: " + guiaAccion);
		if(guiaAccion == GuiaAccion.NUEVO || guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR) {
			if(guiaAccion == GuiaAccion.NUEVO) {
				cargador.center();
				servicioGuia.nuevaGuia(new MethodCallback<Guia>() {
					@Override
					public void onSuccess(Method method, Guia response) {
						guiaSeleccionada = response;
						gridItem2.setGuiaSeleccionada(guiaSeleccionada);
						log.info("guia: " + guiaSeleccionada);
						construirGUI();
						cargador.hide();
					}
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al generar nroGuia: " + exception.getMessage());
						new MensajeError("Error al generar nroGuia", exception).show();
						cargador.hide();
					}
				});
			} else if(guiaAccion == GuiaAccion.CONSULTAR || guiaAccion == GuiaAccion.MODIFICAR) {
				//GWT.log("consultar guia: " + guia.toString());
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
						new MensajeError(mensajeError, exception).show();
						VistaGuiaAccion.this.cargador.hide();
					}
				});
			}
		}
		
	}
	
//	private void guardar() {
//		guiaSeleccionada.setItems(new HashSet<Item>(gridItem2.getItems()));
//		log.info("guardar guia.items: " + gridItem.getItems());
//		log.info("guardar guia: " + guiaSeleccionada);
//		log.info("guardar guia.remitente: " + guiaSeleccionada.getRemitente());
//		log.info("guardar guia.consignatario: " + guiaSeleccionada.getConsignatario());
//		log.info("guardar guia.oficinaOrigen: " + guiaSeleccionada.getOficinaOrigen());
//		log.info("guardar guia.oficinaDestino: " + guiaSeleccionada.getOficinaDestino());
//		for (Item i :gridItem2.getItems()) {
//			log.info("   item: " + i);
//		}
//		cargador.center();
//		servicioGuia.guardar(guiaSeleccionada, new MethodCallback<Void>() {
//			@Override
//			public void onSuccess(Method method, Void response) {
//				new MensajeExito("Guia guardada exitosamente ...").show();
//				cargador.hide();
//			}
//			@Override
//			public void onFailure(Method method, Throwable exception) {
//				log.info("Error al guardar Guia: " + exception.getMessage());
//				new MensajeError("Error al guardar Guia", exception).show();
//				cargador.hide();
//			}
//
//		});
//	}
	
	void cargarOracles() {
		for (Cliente cliente: adminParametros.getClientes()) {
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
			servicioGuia.guardarNroFactura(guiaTemp, new LlamadaRemota<Void>("", false) {
				@Override
				public void onSuccess(Method method, Void response) { }
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
			servicioGuia.guardarRemitente(guiaTemp, new LlamadaRemotaVacia<>("No se puede guardar Remitente", false));
			
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
			servicioGuia.guardarConsignatario(guiaTemp, new LlamadaRemotaVacia<>("No se puede guardar Consignatario", false));
			
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
			servicioGuia.guardarDestino(guiaTemp, new LlamadaRemotaVacia<>("No se puede guardar Destino", false));
		});
		adjuntoTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setAdjunto(adjuntoTextBox.getValue());
			servicioGuia.guardarAdjunto(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
		});
		resumenTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setResumenContenido(resumenTextBox.getValue());
			servicioGuia.guardarResumen(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
		});
		nroEntregaTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setNotaEntrega(nroEntregaTextBox.getValue());
			servicioGuia.guardarNroEntrega(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
		});
		pagoOrigenTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setPagoOrigen(pagoOrigenTextBox.getValue());
			servicioGuia.guardarPagoOrigen(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
		});
		pagoDestinoTextBox.addValueChangeHandler(e->{
			Guia guiaTemp = new Guia();
			guiaTemp.setId(guiaSeleccionada.getId());
			guiaTemp.setSaldoDestino(pagoDestinoTextBox.getValue());
			servicioGuia.guardarPagoDestino(guiaTemp, new LlamadaRemotaVacia<Void>("", false));
		});
		
		aceptarBtn.addClickHandler(e -> {
			cargador.center();
			servicioGuia.cambiarEstado(guiaSeleccionada.getId(), true, new LlamadaRemota<Void>("No se pudo aceptar la Guia", true) {
				@Override
				public void onSuccess(Method method, Void response) {
					new MensajeExito("Guia guardada existosamente con nro: " + guiaSeleccionada.getNroGuia()).show();
					VistaGuiaAccion.this.cargador.hide();
					VistaGuiaAccion.this.hide();
				}
			});
		});
		cancelarBtn.addClickHandler(e -> hide());
		//salirBtn.addClickHandler(e -> hide());
		
	}

}
