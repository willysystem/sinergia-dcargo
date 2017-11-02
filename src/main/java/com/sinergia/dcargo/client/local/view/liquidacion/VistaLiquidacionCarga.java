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
import com.google.gwt.user.client.ui.IntegerBox;
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
import com.sinergia.dcargo.client.local.presenter.PresentadorLiquidacionCarga;
import com.sinergia.dcargo.client.local.view.View;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dto.LiquidacionCargaReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionReporte;


@Singleton
public class VistaLiquidacionCarga extends View<LiquidacionReporte> implements PresentadorLiquidacionCarga.Display {
	
	@Inject MensajeConfirmacion           mensageConfirmacion;
	@Inject MensajeExito                  mensajeExito;
	@Inject MensajeAviso                  mensajeAviso;
	
	@Inject ServicioCuentaCliente         servicioCuenta;
	@Inject ServicioMovimientoCliente     servicioMovimiento;
	@Inject AdminParametros               adminParametros;
	
	@Inject ImprimirPDF                   imprimirPDF; 
	@Inject UtilDCargo          utilDCargo;
	
	private LiquidacionCargaReporte liquidacionCargaReporte;
	
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();

	private HTML fechaInicioLabel = new HTML("<b>Fecha Inicio: </b>");
	private DateBox fechaInicio = new DateBox();
	
	private HTML fechaFinLabel = new HTML("<b>Fecha Fin: </b>");
	private DateBox   fechaFin = new DateBox();
	
	private HTML             origenLabel = new HTML("<b>Origen: </b>");
	private SuggestBox origenSuggestBox  = new SuggestBox(oficinaOracle);
	
	private HTML             destinoLabel = new HTML("<b>Destino: </b>");
	private SuggestBox destinoSuggestBox  = new SuggestBox(oficinaOracle);
			
	private HTML porcentajeDeduccionesLabel          = new HTML("<b>Porcentaje para el destino: </b>");		
	private IntegerBox    porcentajeDeduccionesValue = new IntegerBox();
	
	private Button imprimirBtn = new Button("Imprimir");
	private Button salirBtn = new Button("Salir");
	
	private Button buscarBtn = new Button("Buscar");
	
	private LiquidacionReporte liquidacionReporteBusqueda;
	
	public VistaLiquidacionCarga() { super(10); }
	public VistaLiquidacionCarga(int paging) { super(paging); }
	
	@Override
	public void viewIU() {
		
		// Config
		defaultUI();
		fechaInicio.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd"))); 
		fechaFin.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat("yyyy-MM-dd")));
		porcentajeDeduccionesValue.setValue(10);
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Reporte de Liquidacion de Carga</center>"));
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
	    layout.setWidget(2, 0, porcentajeDeduccionesLabel);
	    layout.setWidget(2, 1, porcentajeDeduccionesValue);
	    layout.setWidget(2, 3, buscarBtn);
	    
	    vpNorte.add(layout);
	    
		// Fecha 
		TextColumn<LiquidacionReporte> tipoColmun = new TextColumn<LiquidacionReporte>() {
			@Override
			public String getValue(LiquidacionReporte entity) {
				return entity.getFecha();
			}
		};
		grid.setColumnWidth(tipoColmun, 30, Unit.PX);
		grid.addColumn(tipoColmun, "Fecha");
		
		// Nro Conocimiento
		TextColumn<LiquidacionReporte> guiaConocimientoColmun = new TextColumn<LiquidacionReporte>() {
			@Override
			public String getValue(LiquidacionReporte entity) {
				return entity.getNroConocimiento();
			}
		};
		grid.setColumnWidth(guiaConocimientoColmun, 50, Unit.PX);
		grid.addColumn(guiaConocimientoColmun, "Nro Conocimiento");
		
		// Nro Guia
		TextColumn<LiquidacionReporte> direccionColmun = new TextColumn<LiquidacionReporte>() {
			@Override
			public String getValue(LiquidacionReporte entity) {
				return entity.getNroGuia();
			}
		};
		grid.setColumnWidth(direccionColmun, 80, Unit.PX);
		grid.addColumn(direccionColmun, "Nro Guia");
		
		// Cobro Origen
		TextColumn<LiquidacionReporte> telefonoColmun = new TextColumn<LiquidacionReporte>() {
			@Override
			public String getValue(LiquidacionReporte entity) {
				return entity.getCobroOrigen();
			}
		};
		grid.setColumnWidth(telefonoColmun, 40, Unit.PX);
		grid.addColumn(telefonoColmun, "Cobro Origen");
		
		// Cobro Destino
		TextColumn<LiquidacionReporte> placaColmun = new TextColumn<LiquidacionReporte>() {
			@Override
			public String getValue(LiquidacionReporte entity) {
				return entity.getCobroDestino();
			}
		};
		grid.setColumnWidth(placaColmun, 40, Unit.PX);
		grid.addColumn(placaColmun, "Cobro Destino");
		
		// Flete Destino
		TextColumn<LiquidacionReporte> vecinoColmun = new TextColumn<LiquidacionReporte>() {
			@Override
			public String getValue(LiquidacionReporte entity) {
				return entity.getFleteDestino();
			}
			
		};
		grid.setColumnWidth(vecinoColmun, 40, Unit.PX);
		grid.addColumn(vecinoColmun, "Flete Destino");
		
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
		DockPanel dock = new DockPanel();
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		dock.add(horizontalPanel, DockPanel.SOUTH);
		mainContentView.getCentralPanel().add(dock);
		
		
		salirBtn.addClickHandler(e -> Window.Location.assign(GWT.getHostPageBaseURL()));
		
		implementarEscuchadores();
		
		cargarDatosIniciales();
	}

	@Override
	protected Object getKeyItem(LiquidacionReporte item) {
		return item.getId();
	}
	@Override
	protected String getNro(LiquidacionReporte entity) {
		return entity.getNro()+"";
	}

	@Override
	public void cargarDataUI(LiquidacionCargaReporte l) {
		this.liquidacionCargaReporte = l;
		dataProvider.getList().clear();
		dataProvider.setList(l.getLiquidacionesReporte());
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public LiquidacionReporte getParametrosBusqueda() {
		log.info("getParametrosBusqueda(): liquidacionReporteBusqueda: " + liquidacionReporteBusqueda);
		return liquidacionReporteBusqueda;
	}
	
	private void implementarEscuchadores(){
		
		fechaInicio.addValueChangeHandler(e -> liquidacionReporteBusqueda.setFechaInicioBusqueda(fechaInicio.getValue()));
		fechaFin.addValueChangeHandler(e -> liquidacionReporteBusqueda.setFechaDestinoBusqueda(fechaFin.getValue()));
		origenSuggestBox.addSelectionHandler(e-> {
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
			liquidacionReporteBusqueda.setIdOrigenBusqueda(oficina.getId());
		});
		destinoSuggestBox.addSelectionHandler(e->{
			String origenNombre = e.getSelectedItem().getReplacementString();
			Oficina oficina = adminParametros.buscarOficinaPorNombre(origenNombre);
			liquidacionReporteBusqueda.setIddDestinoBusqueda(oficina.getId());
		});
		
		imprimirBtn.addClickHandler(e -> {
			String ciudad    = utilDCargo.getCiudad();
			String direccion = utilDCargo.getDireccion();
			String telefono  = utilDCargo.getTelefono();
			String origen    = origenSuggestBox.getValue();
			String destino   = destinoSuggestBox.getValue();
			String fechaIni  = DateTimeFormat.getFormat("yyyy-MM-dd").format(fechaInicio.getValue());
			String fechaFi   = DateTimeFormat.getFormat("yyyy-MM-dd").format(fechaFin.getValue());   		
			
			String items[][] = new String[7][liquidacionCargaReporte.getLiquidacionesReporte().size()];
			int k = 0;
			for (LiquidacionReporte l: liquidacionCargaReporte.getLiquidacionesReporte()) {
				items[k][0] = l.getNro()+"";
				items[k][1] = l.getFecha();
				items[k][2] = l.getNroConocimiento();
				items[k][3] = l.getNroGuia();
				items[k][4] = l.getCobroOrigen();
				items[k][5] = l.getCobroDestino();
				items[k++][6] = l.getFleteDestino();
			}
			
			items[k][0] = "";
			items[k][1] = "Total";
			items[k][2] = liquidacionCargaReporte.getNroTotalConocimientos();
			items[k][3] = liquidacionCargaReporte.getNroTotalGuia();
			items[k][4] = liquidacionCargaReporte.getTotalCobroOrigen();
			items[k][5] = liquidacionCargaReporte.getTotalCobroDestino();
			items[k][6] = liquidacionCargaReporte.getTotalFleteDestino();
			
			Double totalCobroOrigen  = Double.valueOf(liquidacionCargaReporte.getTotalCobroOrigen());
			Double totalCobroDestino = Double.valueOf(liquidacionCargaReporte.getTotalCobroDestino());
			
			String titulo1 = "Liquidación de Carga enviada de: "  + origen + " a: " + destino;
			String titulo2 = "Movimientos comprendidos entre el :" + fechaIni + " y el: " + fechaFi;
			
			Double deduccion = porcentajeDeduccionesValue.getValue() == null ? 0.0 : porcentajeDeduccionesValue.getValue();
			Double sumaDed = (totalCobroOrigen + totalCobroDestino)/100.0*Double.valueOf(deduccion); 
			
			String porcentaje      = utilDCargo.validarNullParaMostrar(deduccion);
			String cuidadDestino   = utilDCargo.validarNullParaMostrar(origenSuggestBox.getValue());
			String pagoFletes      = utilDCargo.validarNullParaMostrar(liquidacionCargaReporte.getTotalFleteDestino());
			String deduccionP       = utilDCargo.validarNullParaMostrar(sumaDed);
			String sumaDeducciones = utilDCargo.validarNullParaMostrar(liquidacionCargaReporte.getTotalFleteDestinoDouble() + sumaDed);
			
			String totalDestino = utilDCargo.validarNullParaMostrar(totalCobroDestino);
			String saldoOrigen  =  utilDCargo.validarNullParaMostrar(totalCobroDestino + sumaDed);
			
			
			imprimirPDF.reporteLiquidacionCarga(ciudad, direccion, telefono, titulo1 , titulo2, items, 
					porcentaje, cuidadDestino, pagoFletes, deduccionP, sumaDeducciones, 
					totalDestino, saldoOrigen, origen);
		});
	}
	
	private void cargarDatosIniciales() {
		liquidacionReporteBusqueda = new LiquidacionReporte();
		
		List<Oficina> oficinas = adminParametros.getOficinas();
		log.info("oficinas.size: " + oficinas.size());
		List<String> palabras1 = new ArrayList<>();
		for (Oficina oficina : oficinas) {
			palabras1.add(oficina.getNombre());
		}
		oficinaOracle.addAll(palabras1);
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
		if(porcentajeDeduccionesValue.getValue() == null) {
			mensajeAviso.mostrar("Elegir porcetaje de deducciones");
			return false;
		}
		return true;
	}
	
}
