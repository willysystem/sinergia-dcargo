package com.sinergia.dcargo.client.local.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.ServicioTransportistasCliente;
import com.sinergia.dcargo.client.local.message.MensajeConfirmacion;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.presenter.PresentadorMovimiento;
import com.sinergia.dcargo.client.shared.CuentaIngreso;
import com.sinergia.dcargo.client.shared.Movimiento;
import com.sinergia.dcargo.client.shared.MovimientoEgreso;
import com.sinergia.dcargo.client.shared.MovimientoIngreso;
import com.sinergia.dcargo.client.shared.TipoCuenta;

@Singleton
public class VistaMovimiento extends View<Movimiento> implements PresentadorMovimiento.Display {
	
	@Inject VistaMovimientoAccion vistaMovimientoAccion; 
	@Inject MensajeConfirmacion mensageConfirmacion;
	@Inject MensajeExito mensajeExito;
	@Inject ServicioTransportistasCliente servicioTransportista;
	@Inject AdminParametros adminParametros;
	
	public VistaMovimiento() { super(10); }
	public VistaMovimiento(int paging) { super(paging); }
	
	private ListBox tipoCuentaListBox = new ListBox();
	
	private ListBox cuentaListBox = new ListBox();
	private ListBox subCuentaListBox = new ListBox();
	
	private DateBox fechaIniDateBox = new DateBox();
	private DateBox fechaFinDateBox = new DateBox();
	
	private HTML       nroGuiaLabel      = new HTML("<b>Nro Guia: </b>");
	private IntegerBox nroGuiaIntegerBox = new IntegerBox();
	private IntegerBox nroConocimientoIntegerBox = new IntegerBox();
	private Widget     nroIntegerBox     = nroGuiaIntegerBox;
	
	
	private ListBox estadoListBox = new ListBox();
	
	private Button buscarBtn = new Button("Buscar");
	
	private Button consultarBtn = new Button("Consultar");
	private Button nuevoBtn = new Button("Nuevo");
	private Button modificarBtn = new Button("Modificar");
	private Button eliminarBtn = new Button("Eliminar");
	private Button salirBtn = new Button("Salir");
	
	@SuppressWarnings("unchecked")
	@Override
	public void viewIU() {
		
		// Config
		defaultUI();
		//grid.setWidth("1000px");
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Movimientos de Ingreso y Egreso</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpTitulo.setWidth("100%");
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.add(hpTitulo);
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		
		FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    //FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
	    //cellFormatter.setColSpan(0, 0, 2);
	    //cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    // Campos
	    layout.setWidget(0, 0, new HTML("<b>Tipo Movimiento: </b>"));
	    layout.setWidget(0, 1, tipoCuentaListBox);
	    layout.setWidget(0, 2, new HTML("<b>Cuenta: </b>"));
	    layout.setWidget(0, 3, cuentaListBox);
	    layout.setWidget(1, 2, new HTML("<b>Sub Cuenta: </b>"));
	    layout.setWidget(1, 3, subCuentaListBox);
	    layout.setWidget(0, 4, new HTML("<b>Fecha Inicial: </b>"));
	    layout.setWidget(0, 5, fechaIniDateBox);
	    layout.setWidget(1, 4, new HTML("<b>Fecha Final: </b>"));
	    layout.setWidget(1, 5, fechaFinDateBox);
	    layout.setWidget(2, 2, nroGuiaLabel);
	    layout.setWidget(2, 3, nroIntegerBox);
	    layout.setWidget(2, 4, new HTML("<b>Estado: </b>"));
	    layout.setWidget(2, 5, estadoListBox);
	    layout.setWidget(2, 6, buscarBtn);
	    
	    vpNorte.add(layout);
		
		// Tipo 
		TextColumn<Movimiento> tipoColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				return entity.getTipoCuenta().name();
			}
		};
		grid.setColumnWidth(tipoColmun, 80, Unit.PX);
		grid.addColumn(tipoColmun, "Tipo");
		
		// Fecha
		TextColumn<Movimiento> fechaColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				if(entity.getFechaRegistro() != null)
					return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(entity.getFechaRegistro());
				return "";
			}
		};
		grid.setColumnWidth(fechaColmun, 40, Unit.PX);
		grid.addColumn(fechaColmun, "Fecha");
		
		// Guia/Conocimiento
		TextColumn<Movimiento> ciColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				if(entity instanceof MovimientoIngreso)
				   if(((MovimientoIngreso)entity).getGuia() != null){
					   return ((MovimientoIngreso)entity).getGuia().getNroGuia() + "";
				   }
				else {
					if(((MovimientoEgreso)entity).getConocimiento() != null){
					   return ((MovimientoEgreso)entity).getConocimiento().getNroConocimiento() + "";
					}
				}
				return "";
			}
		};
		grid.setColumnWidth(ciColmun, 40, Unit.PX);
		grid.addColumn(ciColmun, "");
		
		// Direccion
		TextColumn<Movimiento> direccionColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
//				return entity.getDireccion();
				return null;
			}
		};
		grid.setColumnWidth(direccionColmun, 80, Unit.PX);
		grid.addColumn(direccionColmun, "Dirección");
		
		// Telefono
		TextColumn<Movimiento> telefonoColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
//				return entity.getTelefono();
				return null;
			}
		};
		grid.setColumnWidth(telefonoColmun, 40, Unit.PX);
		grid.addColumn(telefonoColmun, "Teléfono");
		
		// Placa
		TextColumn<Movimiento> placaColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
//				return entity.getPlaca();
				return null;
			}
		};
		grid.setColumnWidth(placaColmun, 40, Unit.PX);
		grid.addColumn(placaColmun, "Placa");
		
		// Marca
		TextColumn<Movimiento> marcaColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
//				return entity.getMarca();
				return null;
			}
		};
		grid.setColumnWidth(marcaColmun, 40, Unit.PX);
		grid.addColumn(marcaColmun, "Marca");
		
		// Color
		TextColumn<Movimiento> colorColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
//				return entity.getColor();
				return null;
			}
		};
		grid.setColumnWidth(colorColmun, 40, Unit.PX);
		grid.addColumn(colorColmun, "Color");
		
		// Vecino de
		TextColumn<Movimiento> vecinoColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
//				return entity.getVecino_de();
				return null;
			}
			
		};
		grid.setColumnWidth(vecinoColmun, 40, Unit.PX);
		grid.addColumn(vecinoColmun, "Vecino de");
		
		grid.setWidth("1000px");
		grid.setHeight("350px");
		
		VerticalPanel vpGrid = new VerticalPanel();
		vpGrid.add(grid);
		
		HorizontalPanel horizontalPanelPager = new HorizontalPanel();
		horizontalPanelPager.setWidth("100%");
		horizontalPanelPager.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanelPager.add(simplePager);
		vpGrid.add(horizontalPanelPager);
		
		
		/// ACCIONES
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		horizontalPanelButton.add(consultarBtn);
		horizontalPanelButton.add(nuevoBtn);
		horizontalPanelButton.add(modificarBtn);
		horizontalPanelButton.add(eliminarBtn);
		horizontalPanelButton.add(salirBtn);
		
		horizontalPanel.add(horizontalPanelButton);
		DockPanel dock = new DockPanel();
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		dock.add(horizontalPanel, DockPanel.SOUTH);
		mainContentView.getCentralPanel().add(dock);
		
		buscarBtn.addClickHandler( e -> ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).clear());
		
		consultarBtn.addClickHandler(e -> {
			Movimiento transportista = ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + transportista);
			if(transportista == null){
				new MensajeAviso("Seleccione un Transportista").show();
			} else {
				//vistaTransportistaAccion.mostrar(TransportistaAccion.CONSULTAR, transportista);
			}
		});
		nuevoBtn.addClickHandler(e->vistaMovimientoAccion.mostrar(TransportistaAccion.NUEVO, null));
		modificarBtn.addClickHandler(e->{
			Movimiento Transportista = ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + Transportista);
			if(Transportista == null){
				new MensajeAviso("Seleccione un Transportista").show();
			} else {
				//vistaTransportistaAccion.mostrar(TransportistaAccion.MODIFICAR, Transportista);
			}
		});
		eliminarBtn.addClickHandler(e->{
			final Movimiento Transportista = ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + Transportista);
			if(Transportista == null){
				new MensajeAviso("Seleccione un Transportista").show();
			} else {
//				mensageConfirmacion.mostrar("Decea eliminar al Transportista: " + Transportista.getNombre(), new ClickHandler() {
//					@Override
//					public void onClick(ClickEvent event) {
//						servicioTransportista.borrar(Transportista.getId(), new LlamadaRemota<Void>("No se puede eliminar Transportista", true) {
//							@Override
//							public void onSuccess(Method method, Void response) {
//								VistaMovimiento.this.mensageConfirmacion.hide();
//								buscarBtn.click();
//								//mensajeExito.mostrar("Eliminado exitosamente");
//							}
//						});
//					}
//				});
			}
		});
		salirBtn.addClickHandler(e -> Window.Location.assign(GWT.getHostPageBaseURL()));
		
		cargarDatosIniciales();
	}

	@Override
	protected Object getKeyItem(Movimiento item) {
		return item.getId();
	}
	@Override
	protected String getNro(Movimiento entity) {
		return entity.getNro()+"";
	}

	@Override
	public void cargarDataUI(List<Movimiento> transportistas) {
		dataProvider.getList().clear();
		dataProvider.setList(transportistas);
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public Movimiento getParametrosBusqueda() {
		Movimiento t = new Movimiento();
//		t.setNombre(nombresTextField.getValue());
//		t.setDireccion(direccionTextField.getValue());
//		t.setTelefono(telefonoTextField.getValue());
//		t.setMarca(marcaTextField.getValue());
//		t.setPlaca(placaTextField.getValue());
//		t.setColor(colorTextField.getValue());
//		t.setVecino_de(vecinoTextField.getValue());
//		t.setCi(ciTextField.getValue());
		return t;
	}
	
	private void cargarDatosIniciales() {
		tipoCuentaListBox.clear();
		tipoCuentaListBox.addItem(TipoCuenta.INGRESO.name(), TipoCuenta.INGRESO.name());
		tipoCuentaListBox.addItem(TipoCuenta.EGRESO.name(), TipoCuenta.EGRESO.name());
		tipoCuentaListBox.setItemSelected(0, true);
		
		cuentaListBox.clear();
		List<CuentaIngreso> cuentasIngreso = adminParametros.getCuentasIngreso();
		for (CuentaIngreso c: cuentasIngreso) {
			cuentaListBox.addItem(c.getNroCuenta() + " - " + c.getDescripcion(), c.getId()+"");
		}
		cuentaListBox.setItemSelected(0, true);
		
	}
	
}
