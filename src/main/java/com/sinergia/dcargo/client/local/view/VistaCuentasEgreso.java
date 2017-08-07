
package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.Method;
import org.slf4j.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.treegrid.TreeGrid;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.CuentaEgresoTO;
import com.sinergia.dcargo.client.local.CuentaTO;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeConfirmacion;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.CuentaEgreso;
import com.sinergia.dcargo.client.shared.TipoCuenta;
import com.sinergia.dcargo.client.shared.UtilCompartido;

/**
 * 
 * @author Willy
 */
@Singleton
public class VistaCuentasEgreso implements IVistaCuentas {
	
	@Inject VistaCuentaAccion vistaCuentaAccion; 
	@Inject ServicioCuentaCliente servicioCuenta;
	@Inject Logger log;
	@Inject AdminParametros adminParametros;
	@Inject MensajeConfirmacion mensageConfirmacion;
	@Inject MensajeExito mensajeExito;
	@Inject MensajeAviso mensajeAviso;
	@Inject Cargador cargador;
	
	TreeGrid<CuentaEgresoTO> gridCuentaEgreso;
	
	TreeStore<CuentaEgresoTO> storeCuentaEgreso;
	
	GridSelectionModel<CuentaEgresoTO> selectionModel;

	TextButton addButton       = new TextButton("Nueva Cuenta");
	TextButton addSubButton    = new TextButton("Nueva Sub Cuenta");
	TextButton editButton      = new TextButton("Modificar Cuenta");
	TextButton editSubButton   = new TextButton("Modificar Sub Cuenta");
	TextButton borrarButton    = new TextButton("Borrar Cuenta");
	TextButton borrarSubButton = new TextButton("Borrar Sub Cuenta");
	TextButton recargarButton  = new TextButton("Recargar");
	
	
	interface CuentaPropierties extends PropertyAccess<CuentaEgresoTO> {
		@Path("id")                     ModelKeyProvider<CuentaEgresoTO>       id();
		@Path("nroCuenta")              ValueProvider<CuentaEgresoTO, Integer> nroCuenta();
		@Path("descripcion")            ValueProvider<CuentaEgresoTO, String>  descripcion();
	}
	
	CuentaPropierties cuentaPropierties = GWT.create(CuentaPropierties.class);
	
	public VistaCuentasEgreso() { super(); }
	
	public Widget viewIU() {
		log.info("VistaCuentasIngreso.viewIU()");
		
		//Config
		config();
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Cuentas de Egreso</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpTitulo.setWidth("100%");
		
		// Cuerpo
		ToolBar toolBar = new ToolBar();
		toolBar.add(addButton);
		toolBar.add(addSubButton);
		toolBar.add(editButton);
		toolBar.add(editSubButton);
		toolBar.add(borrarButton);
		toolBar.add(borrarSubButton);
		toolBar.add(recargarButton);

		VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
		verticalLayoutContainer.add(toolBar, new VerticalLayoutData(1, -1));
		verticalLayoutContainer.add(getGridCuentaIngreso(), new VerticalLayoutData(1, 1));
		
		
		DockPanel dock = new DockPanel();
		dock.add(hpTitulo, DockPanel.NORTH);
		dock.add(verticalLayoutContainer, DockPanel.CENTER);
		
		addButton.addSelectHandler(e -> {
			vistaCuentaAccion.setVistaCuentas(this);
			vistaCuentaAccion.mostrar(CuentaAccion.NUEVO_CUENTA, null, TipoCuenta.EGRESO );
		});
		addSubButton.addSelectHandler(e -> {
			vistaCuentaAccion.setVistaCuentas(this);
			vistaCuentaAccion.mostrar(CuentaAccion.NUEVO_SUB_CUENTA, null, TipoCuenta.EGRESO);
			});
		editButton.addSelectHandler(e -> {
			CuentaEgresoTO cuenta = gridCuentaEgreso.getSelectionModel().getSelectedItem();
			if(cuenta == null) mensajeAviso.mostrar("Seleccione una cuenta");
			vistaCuentaAccion.setVistaCuentas(this); 
			vistaCuentaAccion.mostrar(CuentaAccion.MODIFICAR_CUENTA, cuenta, TipoCuenta.EGRESO);
		});
		editSubButton.addSelectHandler(e -> {
			CuentaEgresoTO cuenta = gridCuentaEgreso.getSelectionModel().getSelectedItem();
			if(cuenta == null) mensajeAviso.mostrar("Seleccione una cuenta");
			vistaCuentaAccion.setVistaCuentas(this); 
			vistaCuentaAccion.mostrar(CuentaAccion.MODIFICAR_SUB_CUENTA, cuenta, TipoCuenta.EGRESO);
		});
		borrarButton.addSelectHandler(e -> {
			CuentaEgresoTO cuenta = gridCuentaEgreso.getSelectionModel().getSelectedItem();
			if(cuenta == null) mensajeAviso.mostrar("Seleccione una cuenta");
			else {
				mensageConfirmacion.mostrar("¿Esta Seguro de borrar esta cuenta y todas sus sub cuentas?", new ClickHandler(){
					@Override
					public void onClick(ClickEvent event) {
						servicioCuenta.borrarCuenta(cuenta.getId(), new LlamadaRemota<Void>("No se pudo borrar cuenta", true) {
							@Override
							public void onSuccess(Method method, Void response) {
								VistaCuentasEgreso.this.cargador.hide();
								cargarData();
								adminParametros.recargarCuentasEgreso();
								mensageConfirmacion.hide();
								
							}
						});
					}
				});
			}
		});
		
		borrarSubButton.addSelectHandler(e -> {
 			CuentaEgresoTO cuenta = gridCuentaEgreso.getSelectionModel().getSelectedItem();
 			if(cuenta == null) mensajeAviso.mostrar("Seleccione una cuenta");
 			else {
 				cargador.show();
 				servicioCuenta.borrarCuenta(cuenta.getId(), new LlamadaRemota<Void>("No se pudo borrar sub cuenta", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						VistaCuentasEgreso.this.cargador.hide();
						cargarData();
					}
				});
 			}
		});
		recargarButton.addSelectHandler(e -> cargarData());
		
		cargarData();
		
		return dock;
	}

	@Override
	public void cargarData(){
		cargador.show();
		servicioCuenta.getTodasCuentasEgreso(new LlamadaRemota<List<CuentaEgreso>>("No se puede cargar cuentas de ingreso", true) {
			@Override
			public void onSuccess(Method method, List<CuentaEgreso> response) {
				config();
				List<CuentaEgresoTO> cuentasTO = UtilCompartido.toDTOEgreso(response);
				showClientesData(cuentasTO);
				VistaCuentasEgreso.this.cargador.hide();
			}
		});
	}
	
	
	public void cargarDataUI(List<CuentaEgresoTO> cuentas) {
		storeCuentaEgreso.clear();
		
		for (CuentaEgresoTO cTO1 : cuentas) {
			storeCuentaEgreso.add(cTO1);
			for (CuentaTO cTO2: cTO1.getSubCuentas()) {
				storeCuentaEgreso.add(cTO1, (CuentaEgresoTO)cTO2);
			}
		}
		log.info("gridCuentaEgreso: " + gridCuentaEgreso.getId());
		
		Timer timer = new Timer(){
            @Override
            public void run() {
        		gridCuentaEgreso.expandAll();
            }
        };
        timer.schedule(250);
		
	}
	
	private Grid<CuentaEgresoTO> getGridCuentaIngreso(){
		
		storeCuentaEgreso = new TreeStore<>(cuentaPropierties.id());
		
		ColumnConfig<CuentaEgresoTO, Integer> nroCuentaColumn  = new ColumnConfig<>(cuentaPropierties.nroCuenta(), 150, "Nro Cuenta");
		ColumnConfig<CuentaEgresoTO, String> descripcionColumn = new ColumnConfig<>(cuentaPropierties.descripcion(), 150, "Descripción");
		
		List<ColumnConfig<CuentaEgresoTO, ?>> columns = new ArrayList<>();
		columns.add(nroCuentaColumn);
		columns.add(descripcionColumn);
		
		ColumnModel<CuentaEgresoTO> cm = new ColumnModel<>(columns);
		
		gridCuentaEgreso = new TreeGrid<>(storeCuentaEgreso, cm, nroCuentaColumn);
		gridCuentaEgreso.getView().setAutoExpandColumn(descripcionColumn);
		gridCuentaEgreso.setWidth(480);
		gridCuentaEgreso.setHeight(400);
		gridCuentaEgreso.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<CuentaEgresoTO>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<CuentaEgresoTO> event) {
				CuentaEgresoTO cuenta = selectionModel.getSelectedItem();
				if(cuenta != null){
					log.info("selectionModel: " + cuenta);
					if(cuenta.getCuenta() != null){
						borrarButton.setVisible(false);
						editButton.setVisible(false);
						editSubButton.setVisible(true);
						borrarSubButton.setVisible(true);
					} else {
						borrarButton.setVisible(true);
						editButton.setVisible(true);
						editSubButton.setVisible(false);
						borrarSubButton.setVisible(false);
					}
				}
			}
		});
		selectionModel = gridCuentaEgreso.getSelectionModel();
		return gridCuentaEgreso;
	}
	
	private void config(){
		addButton.setVisible(true);
		addSubButton.setVisible(true);
		editButton.setVisible(false);
		editSubButton.setVisible(false);
		borrarButton.setVisible(false);
		borrarSubButton.setVisible(false);
	}
	
	int i = 1;
	private void showClientesData(List<CuentaEgresoTO> cuentas) {
		for (CuentaTO cliente: cuentas) {
			cliente.setNro(i++);
		}
		i = 1;
		cargarDataUI(cuentas);
	}
}
