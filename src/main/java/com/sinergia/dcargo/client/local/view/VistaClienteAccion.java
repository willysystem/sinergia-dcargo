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
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.LlamadaRemotaVacia;
import com.sinergia.dcargo.client.local.api.ServicioClienteCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.client.shared.dominio.Cliente;

@Singleton
public class VistaClienteAccion extends DialogBox {
	
	@Inject
	private Logger log;
	 
//	@Inject 
//	private Cargador cargador;
	
	@Inject
	private MensajeExito mensajeExito;
	
	@Inject
	private MensajeAviso mensajeAviso;
	
	@Inject
	private ServicioClienteCliente servicioCliente;
	
	@Inject
	private AdminParametros adminParametros;
	
	//@Inject
	private VistaGuiaAccion vistaGuiaAccion;
	
	public void setVistaGuiaAccion(VistaGuiaAccion vistaGuiaAccion) {
		this.vistaGuiaAccion = vistaGuiaAccion;
	}

	private ClienteAccion clienteAccion;
	private Cliente clienteSeleccionado;
	
	private HTML nombresLabel       = new HTML("<b>Nombres*:</b>");
	private Label nombresLabelValue = new Label("");
	private TextBox nombresTextBox  = new TextBox();
	
	private HTML direccionLabel       = new HTML("<b>Dirección:</b>");
	private Label direccionLabelValue = new Label("");
	private TextBox direccionTextBox  = new TextBox();
	
	private HTML telefonoLabel       = new HTML("<b>Telefono:</b>");
	private Label telefonoLabelValue = new Label("");
	private TextBox telefonoTextBox  = new TextBox();

	private HTML ciLabel      = new HTML("<b>C.I.:</b>");
	private Label ciLabelValue = new Label("");
	private TextBox ciTextBox  = new TextBox();
	
	private HTML nitLabel      = new HTML("<b>NIT*:</b>");
	private Label nitLabelValue = new Label("");
	private TextBox nitTextBox  = new TextBox();
	//private HTML estado = new HTML();
	
	private Button guardarBtn = new Button("Guardar");
	private Button salirBtn = new Button("Salir");
	
	public VistaClienteAccion() {
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
		setText(clienteAccion.getTitulo());
		
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Campos
		layout.setWidget(1,0, nombresLabel);
		layout.setWidget(2,0, direccionLabel);
		layout.setWidget(3,0, telefonoLabel);
		layout.setWidget(4,0, ciLabel);
		layout.setWidget(5,0, nitLabel);		
		if(clienteAccion == ClienteAccion.NUEVO || clienteAccion == ClienteAccion.NUEVO_DESDE_GUIA || clienteAccion == ClienteAccion.MODIFICAR){
			nombresTextBox.setValue("");
			direccionTextBox.setValue("");
			telefonoTextBox.setValue("");
			nitTextBox.setValue("");
			ciTextBox.setValue("");
			layout.setWidget(1,1, nombresTextBox);
			layout.setWidget(2,1, direccionTextBox);
			layout.setWidget(3,1, telefonoTextBox);
			layout.setWidget(4,1, ciTextBox);
			layout.setWidget(5,1, nitTextBox);
		} 
		if(clienteAccion == ClienteAccion.CONSULTAR) {
			if(clienteSeleccionado.getNombre() == null) nombresLabelValue.setText(""); 
			else nombresLabelValue.setText(clienteSeleccionado.getNombre());
			
			if(clienteSeleccionado.getDireccion() == null) direccionLabelValue.setText(""); 
			else direccionLabelValue.setText(clienteSeleccionado.getDireccion());
			
			if(clienteSeleccionado.getTelefono() == null) telefonoLabelValue.setText(""); 
			else telefonoLabelValue.setText(clienteSeleccionado.getTelefono());
			
			if(clienteSeleccionado.getCi() == null) ciLabelValue.setText(""); 
			else ciLabelValue.setText(clienteSeleccionado.getCi());
			
			if(clienteSeleccionado.getNit() == null) nitLabelValue.setText(""); 
			else nitLabelValue.setText(clienteSeleccionado.getNit());
			
			layout.setWidget(1,1, nombresLabelValue);
			layout.setWidget(2,1, direccionLabelValue);
			layout.setWidget(3,1, telefonoLabelValue);
			layout.setWidget(4,1, ciLabelValue);
			layout.setWidget(5,1, nitLabelValue);
		} 
		if(clienteAccion == ClienteAccion.MODIFICAR) {
			if(clienteSeleccionado.getNombre() == null) nombresTextBox.setValue(""); 
			else nombresTextBox.setValue(clienteSeleccionado.getNombre());
			
			if(clienteSeleccionado.getDireccion() == null) direccionTextBox.setValue(""); 
			else direccionTextBox.setValue(clienteSeleccionado.getDireccion());
			
			if(clienteSeleccionado.getTelefono() == null) telefonoTextBox.setValue(""); 
			else telefonoTextBox.setValue(clienteSeleccionado.getTelefono());
			
			if(clienteSeleccionado.getCi() == null) ciTextBox.setValue(""); 
			else ciTextBox.setValue(clienteSeleccionado.getCi());
			
			if(clienteSeleccionado.getNit() == null) nitTextBox.setValue(""); 
			else nitTextBox.setValue(clienteSeleccionado.getNit());
		}
		
		// Acciones
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		
		if(clienteAccion == ClienteAccion.NUEVO || clienteAccion == ClienteAccion.NUEVO_DESDE_GUIA || clienteAccion == ClienteAccion.MODIFICAR) {
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(salirBtn);
		}
		if(clienteAccion == ClienteAccion.CONSULTAR){
			horizontalPanelButton.add(salirBtn);
		}
		
		
		horizontalPanel.add(horizontalPanelButton);
		//horizontalPanel.add(estado);
		
		DockPanel dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(layout, DockPanel.CENTER);
		dock.add(horizontalPanel, DockPanel.SOUTH);
		
		setWidget(dock);
		
		center();
		
		nombresTextBox.addValueChangeHandler(e -> {
			final String nombre = nombresTextBox.getValue();
			servicioCliente.esUnicoNombreCon(nombre, new LlamadaRemota<Resultado>("Error la verificar si nombre es único", false){
				@Override
				public void onSuccess(Method method, Resultado response1) {
					log.info("esUnicoNombreCon " + nombre + ", response = " + response1);
					if(response1.isVariableBoolean()) servicioCliente.guardarNombres(clienteSeleccionado.getId(), nombre, new LlamadaRemotaVacia<>("Error al guardar nombres", false));
					else {
						nombresTextBox.setValue("");
						mensajeAviso.mostrar("El nombre ya existe");
					}
				}} 
			);
		});
		direccionTextBox.addValueChangeHandler(e ->{
			servicioCliente.guardarDireccion(clienteSeleccionado.getId(), direccionTextBox.getValue(), new LlamadaRemotaVacia<>("Error al guardar dirección", false));
		});
		telefonoTextBox.addValueChangeHandler(e -> {
			servicioCliente.guardarTelefono(clienteSeleccionado.getId(), telefonoTextBox.getValue(), new LlamadaRemotaVacia<>("Error al guardar telefono", false));
		});
		
		ciTextBox.addValueChangeHandler(e -> {
			String ci = ciTextBox.getValue();
			servicioCliente.guardarCi(clienteSeleccionado.getId(), ci, new LlamadaRemotaVacia<>("Error al guardar CI", false));
			//nitTextBox.setValue(ci);
			//guardarNit(ci);
		});
		
		nitTextBox.addValueChangeHandler(e -> {
			String nit = nitTextBox.getValue();
			guardarNit(nit);
		});
		
		guardarBtn.addClickHandler(e -> {
			boolean validos = validarCampos();
			if(validos)
				servicioCliente.cambiarEstado(clienteSeleccionado.getId(), "V", new LlamadaRemota<Void>("Error al guardar CI", false){
					@Override
					public void onSuccess(Method method, Void response) {
						mensajeExito.mostrar("Guardado Exitosamente");
						VistaClienteAccion.this.hide();
						if(clienteAccion == ClienteAccion.NUEVO_DESDE_GUIA) {
							GWT.log("VistaClienteAccion.this.adminParametros: " + VistaClienteAccion.this.adminParametros);
							VistaClienteAccion.this.adminParametros.poblarParametros(vistaGuiaAccion);
							//vistaGuiaAccion.cargarOracles();
						}
					}}
				);
			else 
				mensajeAviso.mostrar("Llenar los campos obligatorios (*)");
		});
		
		salirBtn.addClickHandler(e -> this.hide());
	}
	
	public void mostrar(ClienteAccion clienteAccion, final Cliente cliente){
		this.clienteAccion = clienteAccion;
		this.clienteSeleccionado = cliente;
		GWT.log("clienteAccion:" + clienteAccion);
		if(clienteAccion == ClienteAccion.NUEVO || clienteAccion == ClienteAccion.NUEVO_DESDE_GUIA){
			servicioCliente.nuevoCliente(new LlamadaRemota<Cliente>("No se pude crear nueva guia",true) {
				@Override
				public void onSuccess(Method method, Cliente response) {
					log.info("Cliente creado: " + response.getId());
					VistaClienteAccion.this.clienteSeleccionado = response;
				}
			});
		}
		
		construirGUI();
	}
	
	private void guardarNit(final String nit){
		servicioCliente.esUnicoNitCon(nit, new LlamadaRemota<Resultado>("Error la verificar si NIT es único", false){
			@Override
			public void onSuccess(Method method, Resultado response1) {
				log.info("esNitNombreCon " + nit + ", response = " + response1);
				if(response1.isVariableBoolean()) servicioCliente.guardarNit(clienteSeleccionado.getId(), nit, new LlamadaRemotaVacia<>("Error al guardar NIT", false));
				else {
					nitTextBox.setValue("");
					mensajeAviso.mostrar("El NIT ya existe");
				}
			}} 
		);
	}
	
	private boolean validarCampos(){
		String nombres = nombresTextBox.getValue();
		String nit = nitTextBox.getValue();
		if(nombres.isEmpty()) return false;
		if(nit.isEmpty()) return false;
		return true;
	}
	
}
