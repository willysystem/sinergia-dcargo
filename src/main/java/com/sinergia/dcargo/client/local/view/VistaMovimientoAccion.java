package com.sinergia.dcargo.client.local.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTML;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.api.ServicioTransportistasCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.client.shared.Transportista;

@Singleton
public class VistaMovimientoAccion extends DialogBox {
	
	@Inject
	private Logger log;
	 
	@Inject
	private MensajeExito mensajeExito;
	
	@Inject
	private MensajeAviso mensajeAviso;
	
	@Inject
	private MensajeError mensajeError;
	
	@Inject
	private ServicioMovimientoCliente servicioTransportista;
	
	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	private Cargador cargador;

	private TransportistaAccion transportistaAccion;
	private Transportista transportistaSeleccionado;
	
	private HTML nombresLabel       = new HTML("<b>Nombres*: </b>");
	private Label nombresLabelValue = new Label("");
	private TextBox nombresTextBox  = new TextBox();

	private HTML    brevetLabel      = new HTML("<b>Brevet*: </b>");
	private Label   brevetLabelValue = new Label("");
	private TextBox brevetTextBox  = new TextBox();
	
	private HTML    ciLabel      = new HTML("<b>C.I.*: </b>");
	private Label   cidLabelValue = new Label("");
	private TextBox ciTextBox  = new TextBox();
	
	private HTML direccionLabel       = new HTML("<b>Dirección*: </b>");
	private Label direccionLabelValue = new Label("");
	private TextBox direccionTextBox  = new TextBox();
	
	private HTML telefonoLabel       = new HTML("<b>Telefono*: </b>");
	private Label telefonoLabelValue = new Label("");
	private TextBox telefonoTextBox  = new TextBox();

	private HTML placaLabel       = new HTML("<b>Placa*: </b>");
	private Label placaLabelValue = new Label("");
	private TextBox placaTextBox  = new TextBox();
	
	private HTML marcaLabel       = new HTML("<b>Marca*: </b>");
	private Label marcaLabelValue = new Label("");
	private TextBox marcaTextBox  = new TextBox();
	
	private HTML colorLabel       = new HTML("<b>Color*: </b>");
	private Label colorLabelValue = new Label("");
	private TextBox colorTextBox  = new TextBox();
	
	private HTML vecinoDeLabel       = new HTML("<b>Vecino de*: </b>");
	private Label vecinoDeLabelValue = new Label("");
	private TextBox vecinoDeTextBox  = new TextBox();
	
	private Button guardarBtn = new Button("Guardar");
	private Button cancelarBtn = new Button("Cancelar");
	private Button salirBtn = new Button("Salir");
	
	private Widget nombreValue;
	private Widget brevetValue;
	private Widget ciValue;
	private Widget direccionValue;
	private Widget telefonoValue;
	private Widget placaValue;
	private Widget marcaValue;
	private Widget colorValue;
	private Widget vecinoDeValue;
	
	public VistaMovimientoAccion() {
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
		setText(transportistaAccion.getTitulo());
		
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		
		// Config
		nombresLabelValue.setText("");  nombresTextBox.setValue("");	
		brevetLabelValue.setText("");   brevetTextBox.setValue("");
		cidLabelValue.setText("");      ciTextBox.setValue("");
		direccionLabelValue.setText("");direccionTextBox.setValue("");
		telefonoLabelValue.setText(""); telefonoTextBox.setValue("");
		placaLabelValue.setText("");    placaTextBox.setValue("");
		marcaLabelValue.setText("");    marcaTextBox.setValue("");
		colorLabelValue.setText("");    colorTextBox.setValue("");
		vecinoDeLabelValue.setText(""); vecinoDeTextBox.setValue("");
		if(transportistaAccion == TransportistaAccion.NUEVO || transportistaAccion == TransportistaAccion.MODIFICAR){
			nombreValue    = nombresTextBox;	
			brevetValue    = brevetTextBox;
			ciValue        = ciTextBox;
			direccionValue = direccionTextBox;
			telefonoValue  = telefonoTextBox;
			placaValue     = placaTextBox;
			marcaValue     = marcaTextBox;
			colorValue     = colorTextBox;
			vecinoDeValue  = vecinoDeTextBox;
			if(transportistaAccion == TransportistaAccion.MODIFICAR) {
				nombresTextBox.setValue(transportistaSeleccionado.getNombre());	
				brevetTextBox.setValue(transportistaSeleccionado.getBrevetCi());
				ciTextBox.setValue(transportistaSeleccionado.getCi());
				direccionTextBox.setValue(transportistaSeleccionado.getDireccion());
				telefonoTextBox.setValue(transportistaSeleccionado.getTelefono());
				placaTextBox.setValue(transportistaSeleccionado.getPlaca());
				marcaTextBox.setValue(transportistaSeleccionado.getMarca());
				colorTextBox.setValue(transportistaSeleccionado.getColor());
				vecinoDeTextBox.setValue(transportistaSeleccionado.getVecino_de());
			}
		} else if(transportistaAccion == TransportistaAccion.CONSULTAR ) {
			nombreValue    = nombresLabelValue;	
			brevetValue    = brevetLabelValue;
			ciValue        = cidLabelValue;
			direccionValue = direccionLabelValue;
			telefonoValue  = telefonoLabelValue;
			placaValue     = placaLabelValue;
			marcaValue     = marcaLabelValue;
			colorValue     = colorLabelValue;
			vecinoDeValue  = vecinoDeLabelValue;
			
			nombresLabelValue.setText(transportistaSeleccionado.getNombre());	
			brevetLabelValue.setText(transportistaSeleccionado.getBrevetCi());
			cidLabelValue.setText(transportistaSeleccionado.getCi());
			direccionLabelValue.setText(transportistaSeleccionado.getDireccion());
			telefonoLabelValue.setText(transportistaSeleccionado.getTelefono());
			placaLabelValue.setText(transportistaSeleccionado.getPlaca());
			marcaLabelValue.setText(transportistaSeleccionado.getMarca());
			colorLabelValue.setText(transportistaSeleccionado.getColor());
			vecinoDeLabelValue.setText(transportistaSeleccionado.getVecino_de());
			
		} else mensajeError.mostrar("Error grave", null); 
		
		// Campos
		layout.setWidget(0,0, nombresLabel);
		layout.setWidget(0,1, nombreValue);
		layout.setWidget(1,0, brevetLabel);
		layout.setWidget(1,1, brevetValue);
		layout.setWidget(2,0, ciLabel);
		layout.setWidget(2,1, ciValue);
		layout.setWidget(3,0, direccionLabel);
		layout.setWidget(3,1, direccionValue);
		layout.setWidget(4,0, telefonoLabel);
		layout.setWidget(4,1, telefonoValue);
		layout.setWidget(5,0, placaLabel);
		layout.setWidget(5,1, placaValue);
		layout.setWidget(6,0, marcaLabel);
		layout.setWidget(6,1, marcaValue);
		layout.setWidget(7,0, colorLabel);
		layout.setWidget(7,1, colorValue);
		layout.setWidget(8,0, vecinoDeLabel);
		layout.setWidget(8,1, vecinoDeValue);
		layout.setWidget(9,1, cargador.getEstadoHTML());
		
		
		// Acciones
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		
		if(transportistaAccion == TransportistaAccion.NUEVO || transportistaAccion == TransportistaAccion.NUEVO_DESDE_CONOCIMIENTO) {
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(cancelarBtn);
		}
		if(transportistaAccion == TransportistaAccion.MODIFICAR){
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(salirBtn);
		}
		
		if(transportistaAccion == TransportistaAccion.CONSULTAR){
			horizontalPanelButton.add(salirBtn);
		}
		
		horizontalPanel.add(horizontalPanelButton);
		
		DockPanel dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(layout, DockPanel.CENTER);
		dock.add(horizontalPanel, DockPanel.SOUTH);
//		dock.add(cargador.getEstadoHTML(), DockPanel.SOUTH);
		
		setWidget(dock);
		
		center();
		
		nombresTextBox.addValueChangeHandler(e -> {
			cargador.fijarEstadoGuiaEspera();
//		    servicioTransportista.guardarNombre(transportistaSeleccionado.getId(), nombresTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar Nombre", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//			    }
//			 });
		   }
		); 
		brevetTextBox.addValueChangeHandler(e -> {
			final String brevet = brevetTextBox.getValue();
			cargador.fijarEstadoGuiaEspera();
//			servicioTransportista.esUnicoBrevetCon(brevet, new LlamadaRemota<Resultado>("Error la verificar si brevet es único", false){
//				@Override
//				public void onSuccess(Method method, Resultado response1) {
//					log.info("esUnicoBrevetCon " + brevet + ", response = " + response1);
//					if(response1.isVariableBoolean()) servicioTransportista.guardarBrevet(transportistaSeleccionado.getId(), brevet, new LlamadaRemota<Void>("Error al guardar brevet", false){
//						@Override
//						public void onSuccess(Method method, Void response) {
//							VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//						}});
//					else {
//						brevetTextBox.setValue("");
//						mensajeAviso.mostrar("El brevet ya existe");
//					}
//				}} 
//			);
		});
		ciTextBox.addValueChangeHandler(e -> {
			cargador.fijarEstadoGuiaEspera();
//			servicioTransportista.guardarCi(transportistaSeleccionado.getId(), ciTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar CI", false){
//			@Override
//			public void onSuccess(Method method, Void response) {
//				VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//			}});
		});
		
		direccionTextBox.addValueChangeHandler(e -> {
		   cargador.fijarEstadoGuiaEspera();
//		   servicioTransportista.guardarDireccion(transportistaSeleccionado.getId(), direccionTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar dirección", false){
//			@Override
//			public void onSuccess(Method method, Void response) {
//				VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//			}});
		});
		telefonoTextBox.addValueChangeHandler(e -> {
			cargador.fijarEstadoGuiaEspera();
//			servicioTransportista.guardarTelefono(transportistaSeleccionado.getId(), telefonoTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar telefono", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//				}
//			});
		});
		placaTextBox.addValueChangeHandler(e -> {
			cargador.fijarEstadoGuiaEspera();
//			servicioTransportista.guardarPlaca(transportistaSeleccionado.getId(), placaTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar placa", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//				}});
		});
		marcaTextBox.addValueChangeHandler(e -> {
			cargador.fijarEstadoGuiaEspera();
//			servicioTransportista.guardarMarca(transportistaSeleccionado.getId(), marcaTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar marca", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//				}});
		});
		colorTextBox.addValueChangeHandler(e -> {
			cargador.fijarEstadoGuiaEspera();
//			servicioTransportista.guardarColor(transportistaSeleccionado.getId(), colorTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar color", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//				}});
		});
		vecinoDeTextBox.addValueChangeHandler(e -> {
			cargador.fijarEstadoGuiaEspera();
//			servicioTransportista.guardarVecinoDe(transportistaSeleccionado.getId(), vecinoDeTextBox.getValue(), new LlamadaRemota<Void>("Error al guardar vecino", false){
//				@Override
//				public void onSuccess(Method method, Void response) {
//					VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
//				}});
		});
		
		guardarBtn.addClickHandler(e -> {
			if(!validarCampos()) mensajeAviso.mostrar("Llenar los campos obligatorios (*)");
			else hide(); 
		});
		cancelarBtn.addClickHandler(e -> { 
//		servicioTransportista.borrar(transportistaSeleccionado.getId(), new LlamadaRemota<Void>("Error al borrar Transportista", false){
//			@Override
//			public void onSuccess(Method method, Void response) {
//				hide();
//			}})
	}); 
		salirBtn.addClickHandler(e -> this.hide());
	}
	
	public void mostrar(TransportistaAccion transportistaAccion, final Transportista transportista){
		this.transportistaAccion = transportistaAccion;
		this.transportistaSeleccionado = transportista;
		GWT.log("TransportistaAccion:" + transportistaAccion);
		if(transportistaAccion == TransportistaAccion.NUEVO || transportistaAccion == TransportistaAccion.NUEVO_DESDE_CONOCIMIENTO) {
//			servicioTransportista.nuevoTransportista(new LlamadaRemota<Transportista>("No se pude crear nueva guia",true) {
//				@Override
//				public void onSuccess(Method method, Transportista response) {
//					log.info("Transportista creado: " + response.getId());
//					VistaMovimientoAccion.this.transportistaSeleccionado = response;
//				}
//			});
		}
		construirGUI();
	}
	
	private boolean validarCampos() {
		String nombres  = nombresTextBox.getValue();
		String brevet   = brevetTextBox.getValue();
		String ci       = ciTextBox.getValue();
		String direcion = direccionTextBox.getValue();
		String telefono = telefonoTextBox.getValue();
		String placa    = placaTextBox.getValue();
		String marca    = marcaTextBox.getValue();
		String color    = colorTextBox.getValue();
		String vecino   = vecinoDeTextBox.getValue();
		
		if(nombres.isEmpty()) return false;
		if(brevet.isEmpty()) return false;
		if(ci.isEmpty()) return false;
		if(direcion.isEmpty()) return false;
		if(telefono.isEmpty()) return false;
		if(placa.isEmpty()) return false;
		if(marca.isEmpty()) return false;
		if(color.isEmpty()) return false;
		if(vecino.isEmpty()) return false;
		
		return true;	
	}
	
}
