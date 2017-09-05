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
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
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
	 
	@Inject private MensajeExito mensajeExito;
	@Inject private MensajeAviso mensajeAviso;
	@Inject private MensajeError mensajeError;
	
	@Inject private VistaElegirGuiaDialogBox vistaElegirGuiaDialogBox;
	@Inject private VistaElegirConocimientoDialogBox vistaElegirConocimientoDialogBox;
	
	
	@Inject private ServicioMovimientoCliente servicioMovimiento;
	@Inject private ServicioCuentaCliente     servicioCuenta;
	
	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	private Cargador cargador;

	private MovimientoAccion movimientoAccion;
	private Movimiento movimientoSeleccionado;
	
	//private TipoCuenta tipoMovimiento;
	
	private HTML    tipoMovimientoLabel   = new HTML("<b>Tipo Movimiento: </b>");
	private ListBox tipoMovimientoListBox = new ListBox();
	
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
	private Label saldoOrigenValue = new Label();
	
	private HTML     glosaLabel      = new HTML("<b>Glosa:</b>");
	private Label    glosaLabelValue = new Label();
	private TextArea glosaTextArea   = new TextArea();
	
	private Button guardarBtn  = new Button("Guardar");
	private Button cancelarBtn = new Button("Cancelar");
	private Button salirBtn    = new Button("Salir");
	
	private Widget fechaValue;
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
	
	private void construirGUI() {
		
		setGlassEnabled(true);
		setAnimationEnabled(false);
		setText(movimientoAccion.getTitulo());
		
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		
		limpiarCampos();
		
		if(movimientoAccion == MovimientoAccion.NUEVO || movimientoAccion == MovimientoAccion.MODIFICAR){
			fechaValue     = fechaTextBox;	
			cuentaValue    = cuentaListBox;
			subCuentaValue = subCuentaListBox;
			montoValue     = montoDoubleBox;
			glosaValue     = glosaTextArea;
			
			if(movimientoAccion == MovimientoAccion.MODIFICAR) {
				fechaTextBox.setValue(movimientoSeleccionado.getFechaRegistro());
//				if(tipoMovimiento == TipoCuenta.INGRESO){
//					for(int i = 0; i<cuentaListBox.getItemCount(); i++){
//						 Long id = Long.valueOf(cuentaListBox.getValue(0));
//						 if(id == movimientoSeleccionado.getCuenta().getId()){
//							 cuentaListBox.setItemSelected(i, true);
//							 break;
//						 }
//					}
//				}
			}
		} else if(movimientoAccion == MovimientoAccion.CONSULTAR ) {
			fechaValue     = fechaLabel;	
			cuentaValue    = cuentaLabel;
			subCuentaValue = subCuentaLabel;
			montoValue     = montoLabel;
			glosaValue     = glosaLabelValue;
		} else mensajeError.mostrar("Error grave", null); 
		
		// Campos
		layout.setWidget(0,0, tipoMovimientoLabel);
		layout.setWidget(0,1, tipoMovimientoListBox);
		
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
		layout.setWidget(5,5, saldoOrigenValue);
		
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
			horizontalPanelButton.add(salirBtn);
		}
		if(movimientoAccion == MovimientoAccion.MODIFICAR){
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(salirBtn);
		}
		
		if(movimientoAccion == MovimientoAccion.CONSULTAR){
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
		
		cargarDatosIniciales();
		
		agregarEscuchadores();
		
		center();
		
		
	}
	
	private void cargarDatosIniciales() {
		
		tipoMovimientoListBox.clear();
		tipoMovimientoListBox.addItem("","");
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
				log.info("INGRESO");
				seleccionarGuiaOrConocimientoBtn.setTitle("Seleccionar Guia");
				seleccionarGuiaOrConocimientoBtn.setText("Seleccionar Guia*");
				servicioMovimiento.nuevoMovimientoIngreso( new LlamadaRemota<MovimientoIngreso>("", true) {
					@Override
					public void onSuccess(Method method, MovimientoIngreso response) {
						movimientoSeleccionado = response;
						movimientoSeleccionado.setTipoCuenta(TipoCuenta.INGRESO);
						nroComprobanteValue.setText(movimientoSeleccionado.getNroComprobante() + "");
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
						nroComprobanteValue.setText(movimientoSeleccionado.getNroComprobante() + "");
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
			String tipoMovimiento = tipoMovimientoListBox.getSelectedValue();
			
			Long id = Long.parseLong(cuentaListBox.getSelectedValue());
			
			if(tipoMovimiento == TipoCuenta.INGRESO.name() && id != 0) {
				servicioCuenta.getSubCuentasIngreso(id, new LlamadaRemota<List<CuentaIngreso>>("", false) {
					@Override
					public void onSuccess(Method method, List<CuentaIngreso> response) {
						subCuentaListBox.clear();
						subCuentaListBox.addItem("","0");
						for (CuentaIngreso cuentaIngreso : response) {
							subCuentaListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getId() + "");
						}
					}
				});
			} else if (tipoMovimiento == TipoCuenta.EGRESO.name() && id != 0) {
				servicioCuenta.getSubCuentasEgreso(id, new LlamadaRemota<List<CuentaEgreso>>("", false) {
					@Override
					public void onSuccess(Method method, List<CuentaEgreso> response) {
						subCuentaListBox.clear();
						subCuentaListBox.addItem("","0");
						for (CuentaEgreso cuentaIngreso : response) {
							subCuentaListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getId() + "");
						}
					}
				});
			} else if(id == 0) {
				subCuentaListBox.clear();
			}
		});
		
		subCuentaListBox.addChangeHandler(e -> { 
			Long subId = Long.parseLong(subCuentaListBox.getSelectedValue());
			servicioMovimiento.guardarSubCuenta(movimientoSeleccionado.getId(), subId, movimientoSeleccionado.getTipoCuenta(), new LlamadaRemota<Void>("Error al guardar sub cuenta", true) {
				@Override
				public void onSuccess(Method method, Void response) {
					
				}
			});
		});
		
		fechaTextBox.addValueChangeHandler(e -> {
			//cargador.fijarEstadoGuiaEspera();
			Date date = fechaTextBox.getValue();
			movimientoSeleccionado.setFechaRegistro(date);
		    servicioMovimiento.guardarFechaRegistro(movimientoSeleccionado.getId(),date, movimientoSeleccionado.getTipoCuenta(),  new LlamadaRemota<Void>("Error al guardar Nombre", false){
				@Override
				public void onSuccess(Method method, Void response) {
					//VistaMovimientoAccion.this.cargador.fijarEstadoGuiaCargado();
			    }
			 });
		   }
		); 
		
		montoDoubleBox.addValueChangeHandler(e -> {
					
			if(movimientoSeleccionado == null) mensajeAviso.mostrar("");
				
			Double monto = montoDoubleBox.getValue();
			//cargador.fijarEstadoGuiaEspera();
			servicioMovimiento.guardarMonto(movimientoSeleccionado.getId(), monto, movimientoSeleccionado.getTipoCuenta(), new LlamadaRemota<Void>("No se puede guardar Monto", false) {
				@Override
				public void onSuccess(Method method, Void response) {
					//cargador.fijarEstadoGuiaCargado();
				}
				
			});
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
			if(!validarCampos()) mensajeAviso.mostrar("Llenar los campos obligatorios (*)");
			else {
				servicioMovimiento.cambiarEstado(movimientoSeleccionado.getId(), "V", new LlamadaRemota<Void>("No se pudo Guardar", false) {
					@Override
					public void onSuccess(Method method, Void response) {
						mensajeAviso.mostrar("Guardado Exitosamente");
					}
				});
				hide(); 
			}
		});
		
		cancelarBtn.addClickHandler(e -> { 
				
		});
		
		salirBtn.addClickHandler(e -> this.hide());
		
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
	
	public void mostrar(MovimientoAccion transportistaAccion, final Movimiento transportista){
		this.movimientoAccion = transportistaAccion;
		this.movimientoSeleccionado = transportista;
		GWT.log("TransportistaAccion:" + transportistaAccion);
		vistaElegirGuiaDialogBox.setVistaMovimientoAccion(this);
		vistaElegirConocimientoDialogBox.setVistaMovimientoAccion(this);
		
		if(transportistaAccion == MovimientoAccion.NUEVO) {
//		servicioTransportista.nuevoTransportista(new LlamadaRemota<Transportista>("No se pude crear nueva guia",true) {
//			@Override
//			public void onSuccess(Method method, Transportista response) {
//				log.info("Transportista creado: " + response.getId());
//				VistaMovimientoAccion.this.transportistaSeleccionado = response;
//			}
//		});
		}
		construirGUI();
	}
	
	public void mostrarGuiaSeleccionada(Guia guiaSeleccionada) {
		log.info("movimientoSeleccionado.getTipoCuenta(): " + movimientoSeleccionado.getTipoCuenta());
    	((MovimientoIngreso)movimientoSeleccionado).setGuia(guiaSeleccionada);
		nroGuiaOrConocimientoValue.setText(guiaSeleccionada.getNroGuia()+"");
		origenValue.setText(guiaSeleccionada.getOficinaOrigen().getNombre());
		destinoValue.setText(guiaSeleccionada.getOficinaDestino().getNombre());
		
		String montoOrigen = NumberFormat.getFormat("0.00").format(guiaSeleccionada.getPagoOrigen());
		montoOrigenValue.setText(montoOrigen);
			
		String montoDestino = NumberFormat.getFormat("0.00").format(guiaSeleccionada.getSaldoDestino());
		saldoOrigenValue.setText(montoDestino);
			
		servicioMovimiento.guardarGuia(movimientoSeleccionado.getId(), guiaSeleccionada.getId(), new LlamadaRemota<Void>("", true) {
			@Override
			public void onSuccess(Method method, Void response) {
				Double pagoOrigen = guiaSeleccionada.getPagoOrigen() == null ? 0.0 : guiaSeleccionada.getPagoOrigen();
				Double saldoDestino = guiaSeleccionada.getSaldoDestino() == null ? 0.0 : guiaSeleccionada.getSaldoDestino();
				montoDoubleBox.setValue(pagoOrigen + saldoDestino);
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
		saldoOrigenValue.setText(montoDestino);
		
		servicioMovimiento.guardarConocimiento(movimientoSeleccionado.getId(), conocimientoSeleccionada.getId(), new LlamadaRemota<Void>("", true) {
			@Override
			public void onSuccess(Method method, Void response) {
				
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
		nroGuiaOrConocimientoValue.setText("");
		origenValue.setText(""); 
		destinoValue.setText("");
		montoOrigenValue.setText("");
		saldoOrigenValue.setText("");
		montoDoubleBox.setText(""); montoLabelValue.setText("");
		glosaTextArea.setText("");
	}
	
	private boolean validarCampos() {
//		String nombres  = fechaTextBox.getValue();
//		if(nombres.isEmpty()) return false;
		return true;	
	}
	
}
