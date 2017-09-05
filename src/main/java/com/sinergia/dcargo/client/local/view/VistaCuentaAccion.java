package com.sinergia.dcargo.client.local.view;

import java.util.List;

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
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTML;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.LlamadaRemotaVacia;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.client.shared.dominio.Cuenta;
import com.sinergia.dcargo.client.shared.dominio.CuentaEgreso;
import com.sinergia.dcargo.client.shared.dominio.CuentaIngreso;
import com.sinergia.dcargo.client.shared.dominio.TipoCuenta;
import com.sinergia.dcargo.client.shared.dto.CuentaEgresoTO;
import com.sinergia.dcargo.client.shared.dto.CuentaIngresoTO;
import com.sinergia.dcargo.client.shared.dto.CuentaTO;

/**
 * @author Willy
 */
@Singleton
public class VistaCuentaAccion extends DialogBox {
	
	@Inject private Logger log;
	@Inject private Cargador cargador;
	@Inject private MensajeAviso mensajeAviso;
	@Inject private ServicioCuentaCliente servicioCuenta;
	@Inject private AdminParametros adminParametros;
	
	private IVistaCuentas vistaCuentas;
	
	private CuentaAccion cuentaAccion;
	private CuentaTO cuentaSeleccionada;
	private TipoCuenta tipoCuenta;
	
	private HTML nroCuentaLabel         = new HTML("<b>Cuenta* :</b>");
	private IntegerBox nroCuentaTextBox = new IntegerBox();
	
	private HTML descripcionLabel       = new HTML("<b>Descipción* :</b>");
	private TextBox descripcionTextBox  = new TextBox();
	
	private HTML cuentaPadreLabel       = new HTML("<b>Cuenta Padre* :</b>");
	private ListBox cuentaPadreListBox  = new ListBox();
	
	private Button guardarBtn = new Button("Guardar");
	private Button cancelarBtn = new Button("Cancelar");
	private Button salirBtn = new Button("Salir");
	
	
	public VistaCuentaAccion() {
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
		setText(cuentaAccion.getTitulo());
		
		// Datos Iniciales
		datosIniciales();
		
		FlexTable layout = new FlexTable();
		layout.setCellSpacing(6);
		FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
		cellFormatter.setColSpan(0, 0, 2);
		cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Campos
		layout.setWidget(1,0, nroCuentaLabel);
		layout.setWidget(2,0, descripcionLabel);
		if(cuentaAccion == CuentaAccion.NUEVO_SUB_CUENTA || cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA) 
			layout.setWidget(3,0, cuentaPadreLabel); 
		if(cuentaAccion == CuentaAccion.NUEVO_CUENTA || cuentaAccion == CuentaAccion.NUEVO_SUB_CUENTA || cuentaAccion == CuentaAccion.MODIFICAR_CUENTA || cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA){
			nroCuentaTextBox.setText("");
			descripcionTextBox.setValue("");
			layout.setWidget(1,1, nroCuentaTextBox);
			layout.setWidget(2,1, descripcionTextBox);
			if(cuentaAccion == CuentaAccion.NUEVO_SUB_CUENTA || cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA)     
				layout.setWidget(3,1, cuentaPadreListBox);
		}
		
		// Valores a campos 
		if(cuentaAccion == CuentaAccion.MODIFICAR_CUENTA || cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA) {
			if(cuentaSeleccionada.getNroCuenta() == null) nroCuentaTextBox.setText(""); 
			else nroCuentaTextBox.setValue(cuentaSeleccionada.getNroCuenta());
			
			if(cuentaSeleccionada.getDescripcion() == null) descripcionTextBox.setValue(""); 
			else descripcionTextBox.setValue(cuentaSeleccionada.getDescripcion());
			
			if(cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA) {
				String nroCuenta = cuentaSeleccionada.getCuenta().getNroCuenta() + "";
				log.info("nroCuenta: " + nroCuenta);
				int indexSeleccionado = 0; 
				for (int i = 0; i < cuentaPadreListBox.getItemCount(); i++) {
					String nroCuentaIndex = cuentaPadreListBox.getValue(i);
					log.info("nroCuentaIndex: " + nroCuentaIndex);
					if(nroCuenta.equals(nroCuentaIndex)){
						indexSeleccionado = i;
					}
					
				}
				log.info("indexSeleccionado: " + indexSeleccionado);
				cuentaPadreListBox.setItemSelected(indexSeleccionado, true);
			}
		}
		
		// Acciones
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		
		if(cuentaAccion == CuentaAccion.NUEVO_CUENTA || cuentaAccion == CuentaAccion.NUEVO_SUB_CUENTA || 
		   cuentaAccion == CuentaAccion.MODIFICAR_CUENTA || cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA) {
			horizontalPanelButton.add(guardarBtn);
			horizontalPanelButton.add(cancelarBtn);
		}
		
		if(cuentaAccion == CuentaAccion.CONSULTAR){
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
		
		nroCuentaTextBox.addValueChangeHandler(e -> {
			final Integer nroCuenta = nroCuentaTextBox.getValue();
			log.info("-> nroCuenta: " + nroCuenta);
			servicioCuenta.esUnicoNroCuentaCon(tipoCuenta, nroCuenta, new LlamadaRemota<Resultado>("Error al revisar la cuenta única", true) {
				@Override
				public void onSuccess(Method method, Resultado response) {
					log.info("nroCuenta: " + nroCuenta);
					log.info("servicioCuenta: " + servicioCuenta);
					log.info("cuentaSeleccionada: " + cuentaSeleccionada);
					log.info("response: " + response);
					if(response.isVariableBoolean()) servicioCuenta.guardarNroCuenta(cuentaSeleccionada.getId(), nroCuenta, tipoCuenta, new LlamadaRemotaVacia<>("Error al guardar nombres", false));
					else {
						nroCuentaTextBox.setText("");
						mensajeAviso.mostrar("Nro cuenta ya existe");
					}
				}
			});
		});
		descripcionTextBox.addValueChangeHandler(e -> {
			servicioCuenta.guardarDescripcion(cuentaSeleccionada.getId(), descripcionTextBox.getValue(), tipoCuenta, new LlamadaRemotaVacia<Void>("Error al guardar dirección", false));
		});
		cuentaPadreListBox.addChangeHandler( e -> {
			Integer cuentaPadre = Integer.valueOf(cuentaPadreListBox.getSelectedValue());
			log.info("cuentaPadre: " + cuentaPadre);
			Cuenta cuenta = null;
			if(tipoCuenta == TipoCuenta.INGRESO)
				cuenta = adminParametros.buscarCuentaIngresoPorNroCuenta(cuentaPadre);
			if(tipoCuenta == TipoCuenta.EGRESO)
				cuenta = adminParametros.buscarCuentaEgresoPorNroCuenta(cuentaPadre);
			log.info("cuentaIngresoPadre: " + cuenta);
			
			Long idPadre = null;
			if(cuenta != null) {
				idPadre = cuenta.getId();
			}
			servicioCuenta.guardarCuentaPadre(cuentaSeleccionada.getId(), idPadre, tipoCuenta , new LlamadaRemotaVacia<Void>("Error al guardar cuenta padre", false));
		});
		
		guardarBtn.addClickHandler(e -> {
			boolean validos = validarCampos();
			if(!validos)
				mensajeAviso.mostrar("Llenar los campos obligatorios (*)");
			else {
				this.hide();
				vistaCuentas.cargarData();
				if(tipoCuenta == TipoCuenta.INGRESO) adminParametros.recargarCuentasIngreso();
				else adminParametros.recargarCuentasEgreso();
			}	
			
		});
		
		salirBtn.addClickHandler(e -> this.hide());
		cancelarBtn.addClickHandler(e -> {
			servicioCuenta.borrarCuenta(cuentaSeleccionada.getId(), new LlamadaRemota<Void>("No se puede borrar cuenta", false){
				@Override
				public void onSuccess(Method method, Void response) {
					VistaCuentaAccion.this.hide();
				}});
		});	
		
	}
	
	private void datosIniciales(){
		nroCuentaTextBox.setText("");
		descripcionTextBox.setText("");
		cuentaPadreListBox.clear(); 
		cuentaPadreListBox.addItem("Ninguno", "0");
		if(tipoCuenta == TipoCuenta.INGRESO){
			List<CuentaIngreso> cuentas = adminParametros.getCuentasIngreso();
			for (CuentaIngreso cuentaIngreso : cuentas) {
				cuentaPadreListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getNroCuenta() + "");
			}
		}
		if(tipoCuenta == TipoCuenta.EGRESO){
			List<CuentaEgreso> cuentas = adminParametros.getCuentasEgreso();
			for (CuentaEgreso cuentaIngreso : cuentas) {
				cuentaPadreListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getNroCuenta() + "");
			}
		}
		
	}
	
	public void mostrar(CuentaAccion cuentaAccion, final CuentaTO cliente, TipoCuenta tipoCuenta){
		this.cuentaAccion = cuentaAccion;
		this.cuentaSeleccionada = cliente;
		this.tipoCuenta = tipoCuenta;
		if(cuentaAccion == CuentaAccion.NUEVO_CUENTA || cuentaAccion == CuentaAccion.NUEVO_SUB_CUENTA){
			cargador.show();
			if(tipoCuenta == TipoCuenta.INGRESO)
			  servicioCuenta.nuevoCuentaIngreso(new LlamadaRemota<CuentaIngreso>("No se puede crear cuenta ingreso",true) {
				@Override
				public void onSuccess(Method method, CuentaIngreso response) {
					CuentaIngresoTO cuentaTO = new CuentaIngresoTO();
					cuentaTO.setId(response.getId());
					cuentaSeleccionada = cuentaTO;
					construirGUI();
					VistaCuentaAccion.this.cargador.hide();
				}
			  });
			if(tipoCuenta == TipoCuenta.EGRESO)
				  servicioCuenta.nuevaCuentaEgreso(new LlamadaRemota<CuentaEgreso>("No se puede crear cuenta ingreso",true) {
					@Override
					public void onSuccess(Method method, CuentaEgreso response) {
						CuentaEgresoTO cuentaTO = new CuentaEgresoTO();
						cuentaTO.setId(response.getId());
						cuentaSeleccionada = cuentaTO;
						construirGUI();
						VistaCuentaAccion.this.cargador.hide();
					}
			 });
			
		}
		if(cuentaAccion == CuentaAccion.MODIFICAR_CUENTA || cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA)
			construirGUI();
		
		
	}
	
	private boolean validarCampos(){
		Integer nroCuenta = nroCuentaTextBox.getValue();
		String descripcion = descripcionTextBox.getValue();
		if(nroCuenta == null) return false;
		if(descripcion.isEmpty()) return false;
		if(cuentaAccion == CuentaAccion.NUEVO_SUB_CUENTA ||cuentaAccion == CuentaAccion.MODIFICAR_SUB_CUENTA){
			Integer cuentaPadre = Integer.valueOf(cuentaPadreListBox.getSelectedValue());
			Cuenta cuenta = null;
			if(tipoCuenta == TipoCuenta.INGRESO)
				cuenta = adminParametros.buscarCuentaIngresoPorNroCuenta(cuentaPadre);
			else 
				cuenta = adminParametros.buscarCuentaEgresoPorNroCuenta(cuentaPadre);
			if(cuenta == null) return false;
			else return true;
		}
		return true;
	}

	public void setVistaCuentas(IVistaCuentas vistaCuentas) {
		this.vistaCuentas = vistaCuentas;
	}
	
}
