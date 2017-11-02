package com.sinergia.dcargo.client.local.view.liquidacion;

import java.util.ArrayList;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.UtilDCargo;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.message.MensajeConfirmacion;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.local.presenter.PresentadorDeudasPorCobrar;
import com.sinergia.dcargo.client.local.view.View;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dto.DeudasPorCobrarReporte;
import com.sinergia.dcargo.client.shared.dto.DeudasReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionCargaReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionReporte;


@Singleton
public class VistaDeudasPorCobrar extends View<DeudasReporte> implements PresentadorDeudasPorCobrar.Display {
	
	@Inject MensajeConfirmacion           mensageConfirmacion;
	@Inject MensajeExito                  mensajeExito;
	@Inject MensajeAviso                  mensajeAviso;
	
	@Inject ServicioCuentaCliente         servicioCuenta;
	@Inject ServicioMovimientoCliente     servicioMovimiento;
	@Inject AdminParametros               adminParametros;
	
	@Inject ImprimirPDF                   imprimirPDF; 
	@Inject UtilDCargo          utilDCargo;
	
	private DeudasPorCobrarReporte deudasPorCobrarReporte;
	
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle clienteOracle = new MultiWordSuggestOracle();

	private HTML fechaInicioLabel = new HTML("<b>Fecha Inicio: </b>");
	private DateBox fechaInicio = new DateBox();
	
	private HTML fechaFinLabel = new HTML("<b>Fecha Fin: </b>");
	private DateBox   fechaFin = new DateBox();
	
	private HTML             origenLabel = new HTML("<b>Origen: </b>");
	private SuggestBox origenSuggestBox  = new SuggestBox(oficinaOracle);
	
	private HTML             destinoLabel = new HTML("<b>Destino: </b>");
	private SuggestBox destinoSuggestBox  = new SuggestBox(oficinaOracle);
			
	private HTML            clienteLabel = new HTML("<b>Remitente: </b>");		
	private SuggestBox    clienteListBox = new SuggestBox(clienteOracle);
	
	private Button imprimirBtn = new Button("Imprimir");
	private Button salirBtn = new Button("Salir");
	
	private Button buscarBtn = new Button("Buscar");
	
	private DeudasReporte liquidacionReporteBusqueda;
	
	private DockPanel dock;
	
	public VistaDeudasPorCobrar() { super(10); }
	public VistaDeudasPorCobrar(int paging) { super(paging); }
	
	@Override
	public void viewIU() {
		
		if(dock == null) implementarEscuchadores();
		
		// Config
		defaultUI();
		
		fechaInicio.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd"))); 
		fechaFin.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Reporte Deudas por Cobrar</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpTitulo.setWidth("100%");
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.add(hpTitulo);
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		
		FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    
	    // Campos
	    layout.setWidget(0, 0, fechaInicioLabel);
	    layout.setWidget(0, 1, fechaInicio);
	    layout.setWidget(0, 2, fechaFinLabel);
	    layout.setWidget(0, 3, fechaFin);
	    layout.setWidget(1, 0, origenLabel);
	    layout.setWidget(1, 1, origenSuggestBox);
	    layout.setWidget(1, 2, destinoLabel);
	    layout.setWidget(1, 3, destinoSuggestBox);
	    layout.setWidget(2, 0, clienteLabel);
	    layout.setWidget(2, 1, clienteListBox);
	    layout.setWidget(2, 3, buscarBtn);
	    
	    vpNorte.add(layout);
	    
		// Fecha 
		TextColumn<DeudasReporte> tipoColmun = new TextColumn<DeudasReporte>() {
			@Override
			public String getValue(DeudasReporte entity) {
				return entity.getFecha();
			}
		};
		grid.setColumnWidth(tipoColmun, 30, Unit.PX);
		grid.addColumn(tipoColmun, "Fecha");
		
		// Nro Guia
		TextColumn<DeudasReporte> direccionColmun = new TextColumn<DeudasReporte>() {
			@Override
			public String getValue(DeudasReporte entity) {
				return entity.getNroGuia();
			}
		};
		grid.setColumnWidth(direccionColmun, 30, Unit.PX);
		grid.addColumn(direccionColmun, "Nro Guia");
		
		// Origen
		TextColumn<DeudasReporte> guiaConocimientoColmun = new TextColumn<DeudasReporte>() {
			@Override
			public String getValue(DeudasReporte entity) {
				return entity.getOrigen();
			}
		};
		grid.setColumnWidth(guiaConocimientoColmun, 50, Unit.PX);
		grid.addColumn(guiaConocimientoColmun, "Origen");
		
		// Destino
		TextColumn<DeudasReporte> telefonoColmun = new TextColumn<DeudasReporte>() {
			@Override
			public String getValue(DeudasReporte entity) {
				return entity.getDestino();
			}
		};
		grid.setColumnWidth(telefonoColmun, 40, Unit.PX);
		grid.addColumn(telefonoColmun, "Destino");
		
		// Deudas - cliente
		TextColumn<DeudasReporte> placaColmun = new TextColumn<DeudasReporte>() {
			@Override
			public String getValue(DeudasReporte entity) {
				return entity.getDeudasClientes();
			}
		};
		grid.setColumnWidth(placaColmun, 80, Unit.PX);
		grid.addColumn(placaColmun, "Deudas Cliente");
		
		// Deudas - Monto
		TextColumn<DeudasReporte> vecinoColmun = new TextColumn<DeudasReporte>() {
			@Override
			public String getValue(DeudasReporte entity) {
				return entity.getDeudasMonto();
			}
			
		};
		grid.setColumnWidth(vecinoColmun, 40, Unit.PX);
		grid.addColumn(vecinoColmun, "Deudas Monto");
		
//		// Ingresos - Fecha
//		TextColumn<DeudasReporte> ingresoFechaColmun = new TextColumn<DeudasReporte>() {
//			@Override
//			public String getValue(DeudasReporte entity) {
//				return entity.getIngresosFecha();
//			}
//		};
//		grid.setColumnWidth(ingresoFechaColmun, 40, Unit.PX);
//		grid.addColumn(ingresoFechaColmun, "Ingresos Fecha");
//		
//		// Ingresos - NroComprobante
//		TextColumn<DeudasReporte> ingresonroCbteColmun = new TextColumn<DeudasReporte>() {
//			@Override
//			public String getValue(DeudasReporte entity) {
//				return entity.getIngresosNroComprobante();
//			}
//		};
//		grid.setColumnWidth(ingresonroCbteColmun, 40, Unit.PX);
//		grid.addColumn(ingresonroCbteColmun, "Ingresos Cbte.");
//		
//		// Ingresos - Acuenta
//		TextColumn<DeudasReporte> ingresoACuentaColmun = new TextColumn<DeudasReporte>() {
//			@Override
//			public String getValue(DeudasReporte entity) {
//				return entity.getIngresosAcuenta();
//			}
//		};
//		grid.setColumnWidth(ingresoACuentaColmun, 40, Unit.PX);
//		grid.addColumn(ingresoACuentaColmun, "Ingresos Acuenta");
//		
//		// Ingresos - Saldo
//		TextColumn<DeudasReporte> ingresoSaldoColmun = new TextColumn<DeudasReporte>() {
//			@Override
//			public String getValue(DeudasReporte entity) {
//				return entity.getIngresosSaldo();
//			}
//		};
//		grid.setColumnWidth(ingresoSaldoColmun, 40, Unit.PX);
//		grid.addColumn(ingresoSaldoColmun, "Ingresos Saldo");
		
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
		horizontalPanelButton.add(imprimirBtn);
		horizontalPanelButton.add(salirBtn);
		
		horizontalPanel.add(horizontalPanelButton);
		
		dock = new DockPanel();
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		dock.add(horizontalPanel, DockPanel.SOUTH);
		mainContentView.getCentralPanel().add(dock);
		
		
		salirBtn.addClickHandler(e -> Window.Location.assign(GWT.getHostPageBaseURL()));
		
		cargarDatosIniciales();
	}

	@Override
	protected Object getKeyItem(DeudasReporte item) {
		return item.getId();
	}
	
	@Override
	protected String getNro(DeudasReporte entity) {
		return entity.getNro()+"";
	}

	@Override
	public void cargarDataUI(DeudasPorCobrarReporte l) {
		this.deudasPorCobrarReporte = l;
		dataProvider.getList().clear();
		dataProvider.setList(l.getDeudasReporte());
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public DeudasReporte getParametrosBusqueda() {
		log.info("getParametrosBusqueda(): liquidacionReporteBusqueda: " + liquidacionReporteBusqueda);
		return liquidacionReporteBusqueda;
	}
	
	private void implementarEscuchadores(){
		
		fechaInicio.addValueChangeHandler(e -> {
			log.info(" --fechaInicio.getValue(): " + fechaInicio.getValue());
			liquidacionReporteBusqueda.setFechaInicioBusqueda(fechaInicio.getValue());
		});
		fechaFin.addValueChangeHandler(e -> liquidacionReporteBusqueda.setFechaDestinoBusqueda(fechaFin.getValue()));
		origenSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
			liquidacionReporteBusqueda.setIdOrigenBusqueda(oficina.getId());
		});
		destinoSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
			liquidacionReporteBusqueda.setIddDestinoBusqueda(oficina.getId());
		});
		clienteListBox.addSelectionHandler(e->{
			String nombreCliente = e.getSelectedItem().getReplacementString();
			Cliente cliente = adminParametros.buscarClientePorNombre(nombreCliente);
			liquidacionReporteBusqueda.setIdCliente(cliente.getId());
		});
		
		imprimirBtn.addClickHandler(e -> {
			String ciudad    = utilDCargo.getCiudad();
			String direccion = utilDCargo.getDireccion();
			String telefono  = utilDCargo.getTelefono();
			String origen    = origenSuggestBox.getValue();
			String destino   = destinoSuggestBox.getValue();
			String fechaIni  = DateTimeFormat.getFormat("yyyy-MM-dd").format(fechaInicio.getValue());
			String fechaFi   = DateTimeFormat.getFormat("yyyy-MM-dd").format(fechaFin.getValue());   		
			
			String items[][] = new String[11][deudasPorCobrarReporte.getDeudasReporte().size()];
			int k = 0;
			for (DeudasReporte l: deudasPorCobrarReporte.getDeudasReporte()) {
				items[k][0]  = l.getNro()+"";
				items[k][1]  = l.getFecha();
				items[k][2]  = l.getNroGuia();
				items[k][3]  = l.getOrigen();
				items[k][4]  = l.getDestino();
				items[k][5]  = l.getDeudasClientes();
				items[k][6]  = l.getDeudasMonto();
//				items[k][7]  = l.getIngresosFecha();
//				items[k][8]  = l.getIngresosNroComprobante();
//				items[k][9]  = l.getIngresosAcuenta();
//				items[k][10] = l.getIngresosSaldo();
				k++;
			}
			
			items[k][0] = "";
			items[k][6] = deudasPorCobrarReporte.getMontoTotalDeudas();
			items[k][9] = deudasPorCobrarReporte.getMontoTotalAcuenta();
			
			String titulo1 = "Deudas por guias de carga al enviada de: "  + origen + " a: " + destino;
			String titulo2 = "Comprendidos entre el :" + fechaIni + " y el: " + fechaFi;
			
			imprimirPDF.reporteDeudasPorCobrar(ciudad, direccion, telefono, titulo1 , titulo2, items);
		});
	}
	
	private void cargarDatosIniciales() {
		liquidacionReporteBusqueda = new DeudasReporte();
		
		// Oficinas
		List<Oficina> oficinas = adminParametros.getOficinas();
		log.info("oficinas.size: " + oficinas.size());
		List<String> palabras1 = new ArrayList<>();
		for (Oficina oficina : oficinas) {
			palabras1.add(oficina.getNombre());
		}
		oficinaOracle.addAll(palabras1);
		
		// Clientes
		List<Cliente> clientes = adminParametros.getClientes();
		List<String> palabras2 = new ArrayList<>();
		for (Cliente cliente : clientes) {
			palabras2.add(cliente.getNombre());
		}
		clienteOracle.addAll(palabras2);
		
	}
	
	@Override
	public boolean validar() {
		if(fechaInicio.getValue() == null) {
			mensajeAviso.mostrar("Elegir fecha de inicio");
			return false;
		}
		if(fechaFin.getValue() == null) {
			mensajeAviso.mostrar("Elegir fecha de fin");
			return false;
		}
		if(origenSuggestBox.getValue() == null || origenSuggestBox.getValue().equals("")) {
			mensajeAviso.mostrar("Elegir origen");
			return false;
		}
		if(destinoSuggestBox.getValue() == null || destinoSuggestBox.getValue().equals("")) {
			mensajeAviso.mostrar("Elegir destino");
			return false;
		}
		if(clienteListBox.getValue() == null) {
			mensajeAviso.mostrar("Elegir porcetaje de deducciones");
			return false;
		}
		return true;
	}
	
}
