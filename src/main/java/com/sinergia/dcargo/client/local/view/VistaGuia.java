package com.sinergia.dcargo.client.local.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Oficina;

@Singleton
public class VistaGuia extends View<Guia> implements PresentadorGuia.Display {
	
	@Inject
	private VistaGuiaAccion vistaNuevaGuia;
	
	private MultiWordSuggestOracle clienteOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();
	
	private IntegerBox nroGuia = new IntegerBox();
	
	private SuggestBox remiteSuggestBox        = new SuggestBox(clienteOracle);
	private SuggestBox consignatarioSuggestBox = new SuggestBox(clienteOracle);
	
	private SuggestBox origenSuggestBox  = new SuggestBox(oficinaOracle);
	private SuggestBox destinoSuggestBox = new SuggestBox(oficinaOracle);
	
	private DateBox fechaReceptionDateBox = new DateBox();
	private DateBox fechaEntregaDateBox = new DateBox();
	
	private TextBox nroFacturaOrigenTextBox = new TextBox();
	private TextBox nroFacturaDestinoTextBox = new TextBox();
	
	private CheckBox activoCheckBox = new CheckBox();
	
	private Button buscarBtn = new Button("Buscar");
	
	private Button nuevoBtn = new Button("Nuevo");
	private Button consultarBtn = new Button("Consultar");	
	private Button modificarBtn = new Button("Modificar");
	private Button anularBtn = new Button("Anular");
	private Button reporteBtn = new Button("Reporte");
	private Button entregaBtn = new Button("Entrega");
//	private Button salirBtn = new Button("Salir");
	
	
	public VistaGuia() {
		super(10);
	}
	
	public VistaGuia(int paging) {
		super(paging);
	}
	
	@Override
	public void viewIU() {
		
		//SingleSelectionModel<Guia> selectionModel = new SingleSelectionModel<>();
		//grid.setSelectionModel(selectionModel);
		
		nroFacturaOrigenTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				GWT.log("nroFacturaOrigenTextBox.value: " + event.getValue());
			}
		});
		
		activoCheckBox.setValue(true);
		fechaReceptionDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		fechaEntregaDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.setWidth("100%");
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Guias</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.add(hpTitulo);
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		vpNorte.setWidth("100%");
		
		FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    // Campos
	    layout.setHTML(1, 0, "Remite:");
	    layout.setWidget(1, 1, remiteSuggestBox);
	    layout.setHTML(2, 0, "Consignatario:");
	    layout.setWidget(2, 1, consignatarioSuggestBox);
	    
	    layout.setHTML(1, 2, "Origen:");
	    layout.setWidget(1, 3, origenSuggestBox);
	    layout.setHTML(2, 2, "Destino:");
	    layout.setWidget(2, 3, destinoSuggestBox);
	  
	    layout.setHTML(1, 4, "Fecha Recepción:");
	    layout.setWidget(1, 5, fechaReceptionDateBox);
	    layout.setHTML(2, 4, "Fecha Entrega:");
	    layout.setWidget(2, 5, fechaEntregaDateBox);
	    
	    layout.setHTML(1, 6, "Factura origen:");
	    layout.setWidget(1, 7, nroFacturaOrigenTextBox);
	    layout.setHTML(2, 6, "Fecha destino:");
	    layout.setWidget(2, 7, nroFacturaDestinoTextBox);
	    
	    layout.setHTML(3, 0, "Nro Guia:");
	    layout.setWidget(3, 1, nroGuia);
	    
	    layout.setHTML(3, 2, "Activo:");
	    layout.setWidget(3, 3, activoCheckBox);
	    
	    layout.setWidget(3, 4, buscarBtn);
	    
	    vpNorte.add(layout);
	    
		defaultUI();
		
		// NroGuia
		TextColumn<Guia> nroGuiaColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getNroGuia() + "";
			}
		};
		grid.setColumnWidth(nroGuiaColmun, 10, Unit.PX);
		grid.addColumn(nroGuiaColmun, "Guia");
		
		// Remite
		TextColumn<Guia> remiteColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getRemitente() == null? "":entity.getRemitente().getNombre();
			}
		};
		grid.setColumnWidth(remiteColmun, 30, Unit.PX);
		grid.addColumn(remiteColmun, "Remite");
		
		// Consignatario
		TextColumn<Guia> consignatarioColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getConsignatario() == null? "":entity.getConsignatario().getNombre();
			}
		};
		grid.setColumnWidth(consignatarioColmun, 30, Unit.PX);
		grid.addColumn(consignatarioColmun, "Consignatario");
		
		// Origen
		TextColumn<Guia> origenColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getOficinaOrigen() == null?"":entity.getOficinaOrigen().getNombre();
			}
		};
		grid.setColumnWidth(origenColmun, 20, Unit.PX);
		grid.addColumn(origenColmun, "Origen");
		
		// Destino
		TextColumn<Guia> destinoColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getOficinaDestino() == null?"":entity.getOficinaDestino().getNombre();
			}
		};
		grid.setColumnWidth(destinoColmun, 20, Unit.PX);
		grid.addColumn(destinoColmun, "Destino");
		
		// Fecha Recepcion
		TextColumn<Guia> fechaRegistroColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				if(entity.getFechaRegistro() != null){
					return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(entity.getFechaRegistro());
				}
				return "";	
 			}
		};
		grid.setColumnWidth(fechaRegistroColmun, 15, Unit.PX);
		grid.addColumn(fechaRegistroColmun, "Registro");
		
		// Fecha Entrega
		TextColumn<Guia> fechaEntregaColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				if(entity.getFechaEntrega() != null){
					return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(entity.getFechaEntrega());
				}
				return "";
			}
		};
		grid.setColumnWidth(fechaEntregaColmun, 15, Unit.PX);
		grid.addColumn(fechaEntregaColmun, "Entrega");
		
		// Factura Origen
		TextColumn<Guia> facturaOrigenColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return entity.getNroFactura();
			}
		};
		grid.setColumnWidth(facturaOrigenColmun, 15, Unit.PX);
		grid.addColumn(facturaOrigenColmun, "Fact. Origen");		
		
		// Factura Destino
		TextColumn<Guia> facturaDestinoColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				return ""; //entity.getNroFactura();
			}
		};
		grid.setColumnWidth(facturaDestinoColmun, 15, Unit.PX);
		grid.addColumn(facturaDestinoColmun, "Fact. Destino");			
		
		// Activo
		TextColumn<Guia> activoColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				if(entity.getActivo() != null){
					return entity.getActivo()?"Si":"No";
				}
				return "";
			}
		};
		grid.setColumnWidth(activoColmun, 10, Unit.PX);
		grid.addColumn(activoColmun, "Activo");
		
		//grid.setWidth("1000px");
		grid.setHeight("300px");
		
		VerticalPanel vpGrid = new VerticalPanel();
		vpGrid.setWidth("100%");
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
		horizontalPanelButton.add(nuevoBtn);
		horizontalPanelButton.add(consultarBtn);
		horizontalPanelButton.add(modificarBtn);
		horizontalPanelButton.add(anularBtn);
		horizontalPanelButton.add(reporteBtn);
		horizontalPanelButton.add(entregaBtn);
//		horizontalPanelButton.add(salirBtn);
		horizontalPanel.add(horizontalPanelButton);
		
		DockPanel dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		dock.add(horizontalPanel, DockPanel.SOUTH);
		
		mainContentView.getCentralPanel().add(dock);
		
		implementarAcciones();
	}
	
	@Override
	protected Object getKeyItem(Guia item) {
		return item.getId();
	}
	@Override
	protected String getNro(Guia entity) {
		return entity.getNro()+"";
	}

	@Override
	public void cargarDataUI(List<Guia> clientes) {
		dataProvider.getList().clear();
		dataProvider.setList(clientes);
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public Guia getParametrosBusqueda() {
		Cliente remitente = new Cliente();
		remitente.setNombre(remiteSuggestBox.getValue());
		Cliente consignatario = new Cliente();
		consignatario.setNombre(consignatarioSuggestBox.getValue());
		Oficina origen = new Oficina();
		origen.setNombre(origenSuggestBox.getValue());
		Oficina destino = new Oficina();
		destino.setNombre(destinoSuggestBox.getValue());
		Guia guia = new Guia();
		guia.setNroGuia(nroGuia.getValue());
		guia.setRemitente(remitente);
		guia.setConsignatario(consignatario);
		guia.setOficinaOrigen(origen);
		guia.setOficinaDestino(destino);
		guia.setFechaRegistro(fechaReceptionDateBox.getValue());
		guia.setFechaEntrega(fechaEntregaDateBox.getValue());
		guia.setNroFactura(nroFacturaOrigenTextBox.getValue());
		guia.setActivo(activoCheckBox.getValue());
		return guia;
	}

	@Override
	public void fijarOracleParaClientes(List<String> palabras) {
		clienteOracle.addAll(palabras);
	}

	@Override
	public void fijarOracleParaOficina(List<String> palabras) {
		oficinaOracle.addAll(palabras);
	}
	
	private void implementarAcciones() {
		nuevoBtn.addClickHandler(e -> vistaNuevaGuia.mostrar(GuiaAccion.NUEVO, null));
		consultarBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null){
				new MensajeAviso("Seleccione la Guia que decea consultar").show();
			} else {
				vistaNuevaGuia.mostrar(GuiaAccion.CONSULTAR, guia);
			}
		});
		modificarBtn.addClickHandler(e -> {
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null){
				new MensajeAviso("Seleccione la Guia que decea consultar").show();
			} else {
				vistaNuevaGuia.mostrar(GuiaAccion.MODIFICAR, guia);
			}
		});
		anularBtn.setEnabled(false);
		reporteBtn.setEnabled(false);
		entregaBtn.setEnabled(false);
//		salirBtn.addClickHandler(e->{});
		//modificarBtn.addClickHandler(e -> vistaNuevaGuia.mostrar(GuiaAccion.MODIFICAR, guia));
	}
}
