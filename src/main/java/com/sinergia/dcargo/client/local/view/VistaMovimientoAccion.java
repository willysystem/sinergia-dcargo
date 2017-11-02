package com.sinergia.dcargo.client.local.view;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.client.ui.HTML;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.UtilDCargo;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.LlamadaRemotaVacia;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.Cuenta;
import com.sinergia.dcargo.client.shared.dominio.CuentaEgreso;
import com.sinergia.dcargo.client.shared.dominio.CuentaIngreso;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Movimiento;
import com.sinergia.dcargo.client.shared.dominio.MovimientoEgreso;
import com.sinergia.dcargo.client.shared.dominio.MovimientoIngreso;
import com.sinergia.dcargo.client.shared.dominio.TipoCuenta;

@Singleton
public class VistaMovimientoAccion extends DialogBox {
	
	@Inject
	private Logger log;
	 
//	@Inject private MensajeExito mensajeExito;
	@Inject private MensajeAviso mensajeAviso;
	@Inject private MensajeError mensajeError;
	
	@Inject private VistaElegirGuiaDialogBox vistaElegirGuiaDialogBox;
	@Inject private VistaElegirConocimientoDialogBox vistaElegirConocimientoDialogBox;
	
	
	@Inject private ServicioMovimientoCliente servicioMovimiento;
	@Inject private ServicioCuentaCliente     servicioCuenta;
	
	@Inject
	private AdminParametros adminParametros;
	@Inject
	private UtilDCargo utilDCargo;
	
	@Inject
	private Cargador cargador;
	
	@Inject
	private ImprimirPDF imprimirPDF;

	private MovimientoAccion movimientoAccion;
	private Movimiento movimientoSeleccionado;
	
	//private TipoCuenta tipoMovimiento;
	
	private HTML    tipoMovimientoLabel      = new HTML("<b>Tipo Movimiento: </b>");
	private ListBox tipoMovimientoListBox    = new ListBox();
	private Label   tipoMovimientoLabelValue = new Label();
	
	private HTML  nroComprobanteLabel  = new HTML("<b>Nro Comprobante: </b>");
	private Label nroComprobanteValue  = new Label("");
	
	private HTML    fechaLabel      = new HTML("<b>Fecha*: </b>");
	private Label   fechaLabelValue = new Label("");
	private DateBox fechaTextBox    = new DateBox();

	private HTML    cuentaLabel      = new HTML("<b>Cuenta*: </b>");
	private Label   cuentaLabelValue = new Label("");
	private ListBox cuentaListBox    = new ListBox();
	
	private HTML    subCuentaLabel      = new HTML("<b>Sub Cuenta*: </b>");
	private Label   subCuentaLabelValue = new Label("");
	private ListBox subCuentaListBox    = new ListBox();
	
	private HTML      montoLabel      = new HTML("<b>Monto*: </b>");
	private Label     montoLabelValue = new Label("");
	private DoubleBox montoDoubleBox  = new DoubleBox(); 
	
	private HTML  nroGuiaOrConocimientoLabel = new HTML();
	private Label nroGuiaOrConocimientoValue = new Label(); 
	
	private Button seleccionarGuiaOrConocimientoBtn = new Button();
	
	private HTML  origenLabel = new HTML("<b>Origen:</b>");
	private Label origenValue = new Label(); 
	
	private HTML  destinoLabel = new HTML("<b>Destino:</b>");
	private Label destinoValue = new Label(); 
	
	private HTML  montoOrigenLabel = new HTML("<b>Monto origen:</b>");
	private Label montoOrigenValue = new Label();
	
	private HTML  saldoOrigenLabel = new HTML("<b>Saldo:</b>");
	private Label montoDestinoValue = new Label();
	
	private HTML     glosaLabel      = new HTML("<b>Glosa:</b>");
	private Label    glosaLabelValue = new Label();
	private TextArea glosaTextArea   = new TextArea();
	
	private Button guardarBtn  = new Button("Guardar");
	private Button imprimirBtn = new Button("Imprimir");
	private Button cancelarBtn = new Button("Cancelar");
	private Button salirBtn    = new Button("Salir");
	
	private Widget fechaValue;
	private Widget tipoMovimientoValue;
	private Widget cuentaValue;
	private Widget subCuentaValue;
	private Widget montoValue;
	private Widget glosaValue;
	
	
	private static String NRO_GUIA_LABEL = "<b>Nro Guia:</b>";
	private static String NRO_CONOCIMIENTO_LABEL = "<b>Nro Conocimiento:</b>";
	
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
	
	DockPanel dock = null;
	
	@SuppressWarnings("deprecation")
	private void construirGUI() {
		
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText(movimientoAccion.getTitulo());
		
		cargarDatosIniciales();
		
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		
		fechaTextBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		
		limpiarCampos();
		
		if(movimientoAccion == MovimientoAccion.NUEVO || movimientoAccion == MovimientoAccion.MODIFICAR){
			tipoMovimientoValue = tipoMovimientoListBox; 
			fechaValue          = fechaTextBox;	
			cuentaValue         = cuentaListBox;
			subCuentaValue      = subCuentaListBox;
			montoValue          = montoDoubleBox;
			glosaValue          = glosaTextArea;
			
			tipoMovimientoListBox.setEnabled(true);
			seleccionarGuiaOrConocimientoBtn.setVisible(true);
			
			if(movimientoAccion == MovimientoAccion.MODIFICAR) {
				fechaTextBox.setValue(movimientoSeleccionado.getFechaRegistro());
				log.info("     movimientoSeleccionado.getTipoCuenta(): " + movimientoSeleccionado.getTipoCuenta());
				
				tipoMovimientoListBox.setEnabled(false);
				
				log.info("   movimientoSeleccionado.getCuenta(): " + movimientoSeleccionado.getCuenta());
				log.info("   nroCuentaPadre: " + movimientoSeleccionado.getNroCuentaPadre());
				
				
				Integer nroCuentaPadre = movimientoSeleccionado.getNroCuentaPadre();
				
				List<? extends CuentaIngreso> cuentas = null;
				
				if(movimientoSeleccionado.getTipoCuenta() == TipoCuenta.INGRESO){
					
				    tipoMovimientoListBox.setItemSelected(1, true);
				    seleccionarGuiaOrConocimientoBtn.setTitle("Seleccionar Guia");
					seleccionarGuiaOrConocimientoBtn.setText("Seleccionar Guia*");
				    
				    cuentas = adminParametros.getCuentasIngreso();
					log.info("  - cuentas.size(): " + cuentas.size());
				}
				
				if(movimientoSeleccionado.getTipoCuenta() == TipoCuenta.EGRESO){
					
				    tipoMovimientoListBox.setItemSelected(1, true);
				    seleccionarGuiaOrConocimientoBtn.setTitle("Seleccionar Conocimiento");
					seleccionarGuiaOrConocimientoBtn.setText("Seleccionar Conocimiento*");
				    
				    cuentas = adminParametros.getCuentasIngreso();
					log.info("  - cuentas.size(): " + cuentas.size());
				}
				
				cuentaListBox.clear();
				cuentaListBox.addItem("", "0");
				int cuentaSelection = 0;
				int i = 1;
				for (Cuenta cuenta: cuentas) {
					log.info("    cuentaIngreso.getNroCuenta(): " + cuenta.getNroCuenta());
					if(cuenta.getNroCuenta().equals(nroCuentaPadre)) cuentaSelection = i;	
					cuentaListBox.addItem(cuenta.getNroCuenta() + " - " + cuenta.getDescripcion(), cuenta.getId() + "");
					i++;
				}
				
				log.info("    cuentaSelection: " + cuentaSelection);
				cuentaListBox.setItemSelected(cuentaSelection, true);
				
				llegarSubCuentas(movimientoSeleccionado.getCuenta().getNroCuenta());
				
				nroGuiaOrConocimientoValue.setText(utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getNroGuiOrConocimiento()));
				origenValue.setText(utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getOrigen()));
				destinoValue.setText(utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getDestino()));
				
				String pagoOrigenS  = utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getPagoOrigen());
				String pagoDestinoS = utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getPagoDestino());
				montoOrigenValue.setText(pagoOrigenS);
				montoDestinoValue.setText(pagoDestinoS);
				
				montoDoubleBox.setValue(movimientoSeleccionado.getMonto());
				glosaTextArea.setValue(movimientoSeleccionado.getGlosa());
				
			}
		} else if(movimientoAccion == MovimientoAccion.CONSULTAR ) {
			
			tipoMovimientoValue = tipoMovimientoLabelValue;
			fechaValue          = fechaLabelValue;
			cuentaValue         = cuentaLabelValue;
			subCuentaValue      = subCuentaLabelValue;
			montoValue          = montoLabelValue;
			glosaValue          = glosaLabelValue;
			
			seleccionarGuiaOrConocimientoBtn.setVisible(false);
			
	        // Valores	
			tipoMovimientoLabelValue.setText(movimientoSeleccionado.getTipoCuenta().name());
			String fechaRegistro = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(movimientoSeleccionado.getFechaRegistro());
			fechaLabelValue.setText(fechaRegistro);
			nroComprobanteValue.setText(movimientoSeleccionado.getNroGuiOrConocimiento());
			cuentaLabelValue.setText(movimientoSeleccionado.getNroCuentaPadre() + " - " + movimientoSeleccionado.getDescripcionPadre());
			subCuentaLabelValue.setText(movimientoSeleccionado.getCuenta().getNroCuenta() + " - " + movimientoSeleccionado.getCuenta().getDescripcion());
			nroGuiaOrConocimientoValue.setText(movimientoSeleccionado.getNroGuiOrConocimiento());
			origenValue.setText(movimientoSeleccionado.getOrigen());
			destinoValue.setText(movimientoSeleccionado.getDestino());
				
			String pagoOrigenS  = utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getPagoOrigen());
			String pagoDestinoS = utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getPagoDestino());
				
			montoOrigenValue.setText(pagoOrigenS);
			montoDestinoValue.setText(pagoDestinoS);
			
			String monto = utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getMonto());
			montoOrigenValue.setText(monto);
			
			glosaLabelValue.setText(movimientoSeleccionado.getGlosa());
			
		} else mensajeError.mostrar("Error grave", null); 
		
		// Campos
		layout.setWidget(0,0, tipoMovimientoLabel);
		layout.setWidget(0,1, tipoMovimientoValue);
		
		layout.setWidget(1,0, nroComprobanteLabel);
		layout.setWidget(1,1, nroComprobanteValue);
		layout.setWidget(1,2, fechaLabel);
		layout.setWidget(1,3, fechaValue);
		
		layout.setWidget(2,0, cuentaLabel);
		layout.setWidget(2,1, cuentaValue);
		layout.setWidget(2,2, subCuentaLabel);
		layout.setWidget(2,3, subCuentaValue);
		
		layout.setWidget(4,0, nroGuiaOrConocimientoLabel);
		layout.setWidget(4,1, nroGuiaOrConocimientoValue);
		
		layout.setWidget(5,0, seleccionarGuiaOrConocimientoBtn);
		
		layout.setWidget(4,2, origenLabel);
		layout.setWidget(4,3, origenValue);
		layout.setWidget(5,2, destinoLabel);
		layout.setWidget(5,3, destinoValue);
		
		layout.setWidget(4,4, montoOrigenLabel);
		layout.setWidget(4,5, montoOrigenValue);
		layout.setWidget(5,4, saldoOrigenLabel);
		layout.setWidget(5,5, montoDestinoValue);
		
		layout.setWidget(6,0, montoLabel);
		layout.setWidget(6,1, montoValue);
		
		layout.setWidget(7,0, glosaLabel);
		layout.setWidget(7,1, glosaValue);
		
		layout.setWidget(8,1, cargador.getEstadoHTML());
		
		
		// Acciones
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		
		if(movimientoAccion == MovimientoAccion.NUEVO ) {
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(imprimirBtn);
			horizontalPanelButton.add(salirBtn);
			
		}
		if(movimientoAccion == MovimientoAccion.MODIFICAR){
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(imprimirBtn);
			horizontalPanelButton.add(salirBtn);
		}
		
		if(movimientoAccion == MovimientoAccion.CONSULTAR){
			horizontalPanelButton.add(imprimirBtn);
			horizontalPanelButton.add(salirBtn);
		}
		
		horizontalPanel.add(horizontalPanelButton);
		
		if(dock == null) agregarEscuchadores();
		
		dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(layout, DockPanel.CENTER);
		dock.add(horizontalPanel, DockPanel.SOUTH);
//		dock.add(cargador.getEstadoHTML(), DockPanel.SOUTH);
		
		setWidget(dock);
		
		//agregarEscuchadores();
		
		setVisibleFormularioGuiaOrConocimiento(false);
		
		center();
		
		
	}
	
	private void cargarDatosIniciales() {
		
		tipoMovimientoListBox.clear();
		tipoMovimientoListBox.addItem("","0");
		tipoMovimientoListBox.addItem(TipoCuenta.INGRESO.name(), TipoCuenta.INGRESO.name());
		tipoMovimientoListBox.addItem(TipoCuenta.EGRESO.name(), TipoCuenta.EGRESO.name());
		
	}
	
	private void agregarEscuchadores(){
		
		nroGuiaOrConocimientoLabel.setHTML("<b>Nro Guia:</b>");
		tipoMovimientoListBox.addChangeHandler(e -> {
			String tipoMovimiento = tipoMovimientoListBox.getSelectedValue();
			log.info("tipoMovimiento: " + tipoMovimiento);
			limpiarCampos();
			if(tipoMovimiento == TipoCuenta.INGRESO.name()){
				log.info("INGRESO : " + new Date().getTime());
				seleccionarGuiaOrConocimientoBtn.setTitle("Seleccionar Guia");
				seleccionarGuiaOrConocimientoBtn.setText("Seleccionar Guia*");
				servicioMovimiento.nuevoMovimientoIngreso( new LlamadaRemota<MovimientoIngreso>("", true) {
					@Override
					public void onSuccess(Method method, MovimientoIngreso response) {
						movimientoSeleccionado = response;
						movimientoSeleccionado.setTipoCuenta(TipoCuenta.INGRESO);
						nroComprobanteValue.setText(movimientoSeleccionado.getNroComprobante() == null ? "" : movimientoSeleccionado.getNroComprobante() + "");
						fechaTextBox.setValue(movimientoSeleccionado.getFechaRegistro());
						List<CuentaIngreso> cuentas = adminParametros.getCuentasIngreso();
						log.info("  - cuentas.size(): " + cuentas.size());
						cuentaListBox.clear();
						cuentaListBox.addItem("", "0");
						for (CuentaIngreso cuentaIngreso : cuentas) 
							cuentaListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getId() + "");
					}
				} );
				nroGuiaOrConocimientoLabel.setHTML(NRO_GUIA_LABEL);
			} else if (tipoMovimiento == TipoCuenta.EGRESO.name()) {
				log.info("EGRESO");
				seleccionarGuiaOrConocimientoBtn.setTitle("Seleccionar Conocimiento");
				seleccionarGuiaOrConocimientoBtn.setText("Seleccionar Conocimiento*");
				servicioMovimiento.nuevoMovimientoEgreso( new LlamadaRemota<MovimientoEgreso>("", true) {
					@Override
					public void onSuccess(Method method, MovimientoEgreso response) {
						movimientoSeleccionado = response;
						movimientoSeleccionado.setTipoCuenta(TipoCuenta.EGRESO);
						nroComprobanteValue.setText(movimientoSeleccionado.getNroComprobante() == null ? "" : movimientoSeleccionado.getNroComprobante() + "");
						fechaTextBox.setValue(movimientoSeleccionado.getFechaRegistro());
						List<CuentaEgreso> cuentas = adminParametros.getCuentasEgreso();
						log.info("  - cuentas.size(): " + cuentas.size());
						cuentaListBox.clear();
						cuentaListBox.addItem("", "0");
						for (CuentaEgreso cuentaEgreso : cuentas) 
							cuentaListBox.addItem(cuentaEgreso.getNroCuenta() + " - " + cuentaEgreso.getDescripcion(), cuentaEgreso.getId() + "");
					}
				} );
				nroGuiaOrConocimientoLabel.setHTML(NRO_CONOCIMIENTO_LABEL);
			} else {
				cuentaListBox.clear();
				subCuentaListBox.clear();
			}
			subCuentaListBox.clear();
			VistaMovimientoAccion.this.center();
		});
		
		cuentaListBox.addChangeHandler(e -> {
			llegarSubCuentas(null);
		});
		
		subCuentaListBox.addChangeHandler(e -> { 
			Long subId = Long.parseLong(subCuentaListBox.getSelectedValue());
			servicioMovimiento.guardarSubCuenta(movimientoSeleccionado.getId(), subId, movimientoSeleccionado.getTipoCuenta(), new LlamadaRemotaVacia<Void>("Error al guardar sub cuenta", true) {
				@Override
				public void onSuccess(Method method, Void response) {
					
				}
			});
		});
		
		fechaTextBox.addValueChangeHandler(e -> {
			//cargador.fijarEstadoGuiaEspera();
			Date date = fechaTextBox.getValue();
			log.info(" fechaRegistro:" + date); 
			movimientoSeleccionado.setFechaRegistro(date);
		    servicioMovimiento.guardarFechaRegistro(movimientoSeleccionado.getId(), date.getTime(), movimientoSeleccionado.getTipoCuenta(),  new LlamadaRemota<Void>("Error al guardar fecha", false){
				@Override
				public void onSuccess(Method method, Void response) {
					//VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
			    }
			 });
		   }
		); 
		
		montoDoubleBox.addValueChangeHandler(e -> {
			guardarMonto();
		});
		
		glosaTextArea.addValueChangeHandler(e -> {
			if(movimientoSeleccionado == null) mensajeAviso.mostrar("");
			
			String glosa = glosaTextArea.getValue();
			//cargador.fijarEstadoGuiaEspera();
			servicioMovimiento.guardarGlosa(movimientoSeleccionado.getId(), glosa, movimientoSeleccionado.getTipoCuenta(), new LlamadaRemota<Void>("No se pude guardar glosa", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					//cargador.fijarEstadoGuiaCargado();
				}
				
			});
		});
		
		guardarBtn.addClickHandler(e -> {
			
			if(validarCampos()) {
				servicioMovimiento.cambiarEstado(movimientoSeleccionado.getId(), "V", new LlamadaRemota<Void>("No se pudo Guardar", false) {
					@Override
					public void onSuccess(Method method, Void response) {
						if(tipoMovimientoListBox.getSelectedValue().equals(TipoCuenta.INGRESO.name()))
							servicioMovimiento.generarNroComprobanteIngreso(movimientoSeleccionado.getId(), new LlamadaRemota<Integer>("No se puede generar numero de comprobandte de ingreso", true) {
								@Override
								public void onSuccess(Method method, Integer response) {
									movimientoSeleccionado.setNroComprobante(response);
									nroComprobanteValue.setText("" + response);
									mensajeAviso.mostrar("Guardado Exitosamente");
								}}
							);
						else  
							servicioMovimiento.generarNroComprobanteEgreso(movimientoSeleccionado.getId(), new LlamadaRemota<Integer>("No se puede generar numero de comprobandte de ingreso", true) {
								@Override
								public void onSuccess(Method method, Integer response) {
									movimientoSeleccionado.setNroComprobante(response);
									nroComprobanteValue.setText("" + response);
									mensajeAviso.mostrar("Guardado Exitosamente");
								}}
							);						    
					}
				});
			}
		});
		
		imprimirBtn.addClickHandler(e -> {
			String ciudad    = utilDCargo.getCiudad();
			String direccion = utilDCargo.getDireccion();
			String telefono  = utilDCargo.getTelefono();
			
			String titulo                = "COMPROBANTE DE " + movimientoSeleccionado.getTipoCuenta();
			String numeroComprobante     = utilDCargo.validarNullParaMostrar(movimientoSeleccionado.getNroComprobante());
			String fecha                 = adminParametros.getDateParam().getFormattedValue();
			String nroGuiaOrConocimiento = movimientoSeleccionado.getNroGuiOrConocimiento();
			String origen                = "";
			String items[][] = new String[1][2];
//			if(movimientoSeleccionado.getCuenta() != null) {
//				items[0][0] = "" + movimientoSeleccionado.getCuenta();
//				if(movimientoSeleccionado.getCuenta().get)
//			}
			
			items[0][1] = "";
			
			String glosa = movimientoSeleccionado.getGlosa();
			String entregueConforme = "";
			String recibiConforme = "";
			
			
			imprimirPDF.reporteComprobante(
					ciudad, direccion, telefono,
					titulo, numeroComprobante, fecha, 
					nroGuiaOrConocimiento, origen, 
					items, glosa, 
					entregueConforme, recibiConforme);
		});
		
		salirBtn.addClickHandler(e -> VistaMovimientoAccion.this.hide());
		
		seleccionarGuiaOrConocimientoBtn.addClickHandler(e -> {
			if(movimientoSeleccionado == null) {
				mensajeAviso.mostrar("Tiene que elegir un Movimiento");
				return ;
			}
			if(movimientoSeleccionado.getTipoCuenta() == null) {
				mensajeAviso.mostrar("Tiene que elegir un Tipo Movimiento");
				return ;
			}
			
			if(movimientoSeleccionado.getTipoCuenta() == TipoCuenta.INGRESO) {
				vistaElegirGuiaDialogBox.mostrar();
			} else {
				vistaElegirConocimientoDialogBox.mostrar();
			}
			
			
		});
		
	}
	
	public void mostrar(MovimientoAccion movimientoAccion, final Movimiento transportista){
		this.movimientoAccion = movimientoAccion;
		this.movimientoSeleccionado = transportista;
		GWT.log("TransportistaAccion:" + movimientoAccion);
		vistaElegirGuiaDialogBox.setVistaMovimientoAccion(this);
		vistaElegirConocimientoDialogBox.setVistaMovimientoAccion(this);
		
		construirGUI();
	}
	
	public void mostrarGuiaSeleccionada(Guia guiaSeleccionada) {
		log.info("movimientoSeleccionado.getTipoCuenta(): " + movimientoSeleccionado.getTipoCuenta());
    	//((MovimientoIngreso)movimientoSeleccionado).setGuia(guiaSeleccionada);
		nroGuiaOrConocimientoValue.setText(guiaSeleccionada.getNroGuia()+"");
		origenValue.setText(guiaSeleccionada.getOficinaOrigen().getNombre());
		destinoValue.setText(guiaSeleccionada.getOficinaDestino().getNombre());
		
		String montoOrigen = NumberFormat.getFormat("0.00").format(guiaSeleccionada.getPagoOrigen());
		montoOrigenValue.setText(montoOrigen);
			
		String montoDestino = NumberFormat.getFormat("0.00").format(guiaSeleccionada.getSaldoDestino());
		montoDestinoValue.setText(montoDestino);
			
		servicioMovimiento.guardarGuia(movimientoSeleccionado.getId(), guiaSeleccionada.getId(), new LlamadaRemota<Void>("", true) {
			@Override
			public void onSuccess(Method method, Void response) {
				Double pagoOrigen = guiaSeleccionada.getPagoOrigen() == null ? 0.0 : guiaSeleccionada.getPagoOrigen();
				Double saldoDestino = guiaSeleccionada.getSaldoDestino() == null ? 0.0 : guiaSeleccionada.getSaldoDestino();
				montoDoubleBox.setValue(pagoOrigen + saldoDestino);
				
				guardarMonto();
			}
		});
		 
	}
	
	public void mostrarConocimientoSeleccionado(Conocimiento conocimientoSeleccionada) {
		log.info("movimientoSeleccionado.getTipoCuenta(): " + movimientoSeleccionado.getTipoCuenta());
		((MovimientoEgreso)movimientoSeleccionado).setConocimiento(conocimientoSeleccionada);
		nroGuiaOrConocimientoValue.setText(conocimientoSeleccionada.getNroConocimiento()+"");
		origenValue.setText(conocimientoSeleccionada.getOficinaOrigen().getNombre());
		destinoValue.setText(conocimientoSeleccionada.getOficinaDestino().getNombre());
		
		String montoOrigen = NumberFormat.getFormat("0.00").format(conocimientoSeleccionada.getPagoOrigen());
		montoOrigenValue.setText(montoOrigen);
		
		String montoDestino = NumberFormat.getFormat("0.00").format(conocimientoSeleccionada.getPagoDestino());
		montoDestinoValue.setText(montoDestino);
		
		servicioMovimiento.guardarConocimiento(movimientoSeleccionado.getId(), conocimientoSeleccionada.getId(), new LlamadaRemota<Void>("", true) {
			@Override
			public void onSuccess(Method method, Void response) {
				Double pagoOrigen = conocimientoSeleccionada.getPagoOrigen() == null ? 0.0 : conocimientoSeleccionada.getPagoOrigen();
				Double saldoDestino = conocimientoSeleccionada.getPagoDestino() == null ? 0.0 : conocimientoSeleccionada.getPagoDestino();
				montoDoubleBox.setValue(pagoOrigen + saldoDestino);
				
				guardarMonto();
			}
		});
		
	}
	
	
	private void limpiarCampos() {
		nroComprobanteValue.setText(""); 
		fechaLabelValue.setText("");     fechaTextBox.setValue(null);
		cuentaLabelValue.setText("");    cuentaListBox.clear();
		subCuentaLabelValue.setText(""); subCuentaListBox.clear();
		montoLabelValue.setText("");     //montoDoubleBox.setValue(null);
		seleccionarGuiaOrConocimientoBtn.setTitle("");
		seleccionarGuiaOrConocimientoBtn.setText("");
		nroGuiaOrConocimientoValue.setText("");
		origenValue.setText(""); 
		destinoValue.setText("");
		montoOrigenValue.setText("");
		montoDestinoValue.setText("");
		montoDoubleBox.setText(""); montoLabelValue.setText("");
		glosaTextArea.setText("");
		setVisibleFormularioGuiaOrConocimiento(false);
	}
	
	private boolean validarCampos() {
		
		boolean isTipoMovimiento = tipoMovimientoListBox.getSelectedValue().equals("0") ? false : true;
		log.info("isTipoMovimiento: " + isTipoMovimiento);
		//String tipoMovimientoListBox.getSelectedValue().e
		if(!isTipoMovimiento) { 
			mensajeAviso.mostrar("Debe elegir un Tipo Movimiento"); return false;
		}
		
		boolean isCuenta = ( cuentaListBox.getSelectedValue() == null | cuentaListBox.getSelectedValue().equals("0")) ? false : true;
		//cuentaListBox.getSelectedItemText().contains("1000")
		log.info("  isCuenta: " + isCuenta);
		if(!isCuenta) { 
			mensajeAviso.mostrar("Debe elegir una  Cuenta"); return false; 
		}
		
		boolean isSubCuenta = subCuentaListBox.getSelectedValue().equals("0") ? false : true;
		log.info("  isSubCuenta: " + isSubCuenta);
		if(!isSubCuenta) {
			mensajeAviso.mostrar("Debe elegir una  Sub Cuenta"); return false;
		}
		
		boolean isFecha = fechaTextBox.getValue() == null ? false : true;
		log.info("  isFecha: " + isFecha);
		if(!isFecha) {
			mensajeAviso.mostrar("Debe elegir una fecha"); return false;
		}
		
//		boolean isNro = nroGuiaOrConocimientoValue.getText().equals("") ? false : true;
//		log.info("  isNro: " + isNro);
//		if(!isNro) {
//			mensajeAviso.mostrar("Debe elegir un Guia o Conocimiento"); return false;
//		}
		
		boolean isMonto = montoDoubleBox.getValue() == null ? false : true;
		log.info("  isMonto: " + isMonto);
		if(!isMonto) {
			mensajeAviso.mostrar("Debe elegir un Monto"); return false;
		}
		
		return true;	
	}
	
	private void llegarSubCuentas(Integer subCuentaSeleccion) {
		String tipoMovimiento = tipoMovimientoListBox.getSelectedValue();
		
		Long id = Long.parseLong(cuentaListBox.getSelectedValue());
		
		if(tipoMovimiento == TipoCuenta.INGRESO.name() && id != 0) {
			servicioCuenta.getSubCuentasIngreso(id, new LlamadaRemota<List<CuentaIngreso>>("", false) {
				@Override
				public void onSuccess(Method method, List<CuentaIngreso> response) {
					subCuentaListBox.clear();
					subCuentaListBox.addItem("","0");
					for (CuentaIngreso cuentaIngreso : response) 
						subCuentaListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getId() + "");
					
					// Seleccionar SubCuenta
					int subCuentaSeleccionado = 0;
					int i = 1;
					if(subCuentaSeleccion != null) {
						for (Cuenta cuentaIngreso : response) 
							if(cuentaIngreso.getNroCuenta().equals(subCuentaSeleccion)) subCuentaSeleccionado = i;
						subCuentaListBox.setItemSelected(subCuentaSeleccionado, true);
					}
				}
			});
			
//			if(cuentaListBox.getSelectedItemText().contains("1000")) whurtado. se modificó no existen guias por concepto de ingreso
//				setVisibleFormularioGuiaOrConocimiento(true);
//			else
//				setVisibleFormularioGuiaOrConocimiento(false);
		} else if (tipoMovimiento == TipoCuenta.EGRESO.name() && id != 0) {
			servicioCuenta.getSubCuentasEgreso(id, new LlamadaRemota<List<CuentaEgreso>>("", false) {
				@Override
				public void onSuccess(Method method, List<CuentaEgreso> response) {
					subCuentaListBox.clear();
					subCuentaListBox.addItem("","0");
					for (CuentaEgreso cuentaIngreso : response) 
						subCuentaListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getId() + "");
					
					// Seleccionar SubCuenta
					int subCuentaSeleccionado = 0;
					int i = 1;
					if(subCuentaSeleccion != null) {
						for (Cuenta cuentaIngreso : response) 
							if(cuentaIngreso.getNroCuenta().equals(subCuentaSeleccion)) subCuentaSeleccionado = i;
						subCuentaListBox.setItemSelected(subCuentaSeleccionado, true);
					}
					
					
				}
			});
			if(cuentaListBox.getSelectedItemText().contains("2000")) 
				setVisibleFormularioGuiaOrConocimiento(true);
			else
				setVisibleFormularioGuiaOrConocimiento(false);
		} else if(id == 0) {
			subCuentaListBox.clear();
		}
	}
	
	private void guardarMonto() {
		if(movimientoSeleccionado == null) mensajeAviso.mostrar("");
		
		Double monto = montoDoubleBox.getValue();
		//cargador.fijarEstadoGuiaEspera();
		servicioMovimiento.guardarMonto(movimientoSeleccionado.getId(), monto, movimientoSeleccionado.getTipoCuenta(), new LlamadaRemota<Void>("No se puede guardar Monto", false) {
			@Override
			public void onSuccess(Method method, Void response) {
				//cargador.fijarEstadoGuiaCargado();
			}
			
		});
	}
	
	private void setVisibleFormularioGuiaOrConocimiento(boolean visible) {
		nroGuiaOrConocimientoLabel.setVisible(visible);
		nroGuiaOrConocimientoValue.setVisible(visible);
		seleccionarGuiaOrConocimientoBtn.setVisible(visible);
		origenLabel.setVisible(visible);
		origenValue.setVisible(visible);
		destinoLabel.setVisible(visible);
		destinoValue.setVisible(visible);
	    montoOrigenLabel.setVisible(visible);
		montoOrigenValue.setVisible(visible);
		saldoOrigenLabel.setVisible(visible);
		montoDestinoValue.setVisible(visible);
		
	}
}
