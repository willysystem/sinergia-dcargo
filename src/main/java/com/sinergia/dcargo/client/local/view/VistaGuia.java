package com.sinergia.dcargo.client.local.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.local.presenter.PresentadorGuia;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.EstadoGuia;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Oficina;

@Singleton
public class VistaGuia extends View<Guia> implements PresentadorGuia.Display {
	
	@Inject
	private VistaGuiaAccion vistaNuevaGuia;
	
	@Inject
	private ServicioGuiaCliente servicioGuiaCliente;
	
	@Inject
	private Cargador cargador;
	
	@Inject
	private MensajeExito mensajeExito;
	
	@Inject
	private ServicioGuiaCliente servicioGuia;
	
	@Inject
	private ImprimirPDF imprimirPDF;
	
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
	
	private ListBox estadoListBox = new ListBox();
	//private CheckBox activoCheckBox = new CheckBox();
	
	private Button buscarBtn = new Button("Buscar");
	
	private Button nuevoBtn = new Button("Nuevo");
	private Button consultarBtn = new Button("Consultar");	
	private Button modificarBtn = new Button("Modificar");
	private Button anularBtn = new Button("Anular");
	private Button imprimirBtn = new Button("Imprimir Búsqueda");
	private Button entregaBtn = new Button("Entrega");
//	private Button salirBtn = new Button("Salir");
	
	
	public VistaGuia() {
		super(10);
	}
	
	public VistaGuia(int paging) {
		super(paging);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void viewIU() {
		
		nroFacturaOrigenTextBox.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				GWT.log("nroFacturaOrigenTextBox.value: " + event.getValue());
			}
		});
		
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
	    
	    layout.setHTML(3, 2, "Estado:");
	    layout.setWidget(3, 3, estadoListBox);
	    
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
		
		// Estado
		TextColumn<Guia> activoColmun = new TextColumn<Guia>() {
			@Override
			public String getValue(Guia entity) {
				if(entity.getEstadoDescripcion() != null){
					return entity.getEstadoDescripcion();
				}
				return "";
			}
		};
		grid.setColumnWidth(activoColmun, 10, Unit.PX);
		grid.addColumn(activoColmun, "Estado");
		
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
		horizontalPanelButton.add(imprimirBtn);
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
		
		cargarEstadosListBox();
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
		guia.setEstadoDescripcion(estadoListBox.getSelectedValue());
		return guia;
	}

	@Override
	public void fijarOracleParaClientes(List<String> palabras) {
		for (String s : palabras) {
			if(s != null)
				clienteOracle.add(s);
		}
		
	}

	@Override
	public void fijarOracleParaOficina(List<String> palabras) {
		oficinaOracle.addAll(palabras);
	}
	
	private void implementarAcciones() {
		nuevoBtn.addClickHandler(e -> vistaNuevaGuia.mostrar(GuiaAccion.NUEVO, null));
		consultarBtn.addClickHandler(e -> {
			@SuppressWarnings("unchecked")
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null){
				new MensajeAviso("Seleccione la Guia que decea consultar").show();
			} else {
				vistaNuevaGuia.mostrar(GuiaAccion.CONSULTAR, guia);
			}
		});
		modificarBtn.addClickHandler(e -> {
			@SuppressWarnings("unchecked")
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null){
				new MensajeAviso("Seleccione la Guia que decea consultar").show();
			} else {
				vistaNuevaGuia.mostrar(GuiaAccion.MODIFICAR, guia);
			}
		});
		anularBtn.addClickHandler(e -> {
			@SuppressWarnings("unchecked")
			Guia guia = ((SingleSelectionModel<Guia>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null){
				new MensajeAviso("Seleccione la Guia que decea anular").show();
			} else {
				VistaGuia.this.cargador.center();
				servicioGuia.cambiarEstado(guia.getId(), "Anulado", new LlamadaRemota<Void>("No se pudo anular la Guia", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						mensajeExito.mostrar("Guia anulada existosamente, con nro: " + guia.getNroGuia());
						mensajeExito.center();
						VistaGuia.this.cargador.hide();
					}
				});
			}
		});
		imprimirBtn.addClickHandler(e -> {
			List<Guia> guias = dataProvider.getList();
			String[][] guiasImprimir = new String[guias.size()][11];
			int k = 0;
			for (Guia guia : guias) {
				String remite = "";
				if(guia.getRemitente()!=null) remite = guia.getRemitente().getNombre();
				String consignatario = "";
				if(guia.getConsignatario()!=null) consignatario = guia.getConsignatario().getNombre(); 
				GWT.log("  k:" + k);
				guiasImprimir[k][0]  = (k+1)+"";
				guiasImprimir[k][1]  = guia.getNroGuia()+"";
				guiasImprimir[k][2]  = remite;
				guiasImprimir[k][3]  = consignatario;
				guiasImprimir[k][4]  = guia.getOficinaOrigen()==null?"":guia.getOficinaOrigen().getNombre();
				guiasImprimir[k][5]  = guia.getOficinaDestino()==null?"":guia.getOficinaDestino().getNombre();
				guiasImprimir[k][6]  = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(guia.getFechaRegistro());
				guiasImprimir[k][7]  = "";//guia.getFechaEntrega()+"";
				guiasImprimir[k][8]  = guia.getNroFactura();
				guiasImprimir[k][9]  = "";
				guiasImprimir[k][10] = guia.getEstadoDescripcion();
				k++;
			}
			imprimirPDF.generarImpresionBusqueda(guiasImprimir);
		});
		
		anularBtn.setEnabled(true);
		imprimirBtn.setEnabled(true);
		entregaBtn.setEnabled(false);
		
//		salirBtn.addClickHandler(e->{});
		//modificarBtn.addClickHandler(e -> vistaNuevaGuia.mostrar(GuiaAccion.MODIFICAR, guia));
	}
	
	private void cargarEstadosListBox() {
		servicioGuiaCliente.getEstados(new LlamadaRemota<List<EstadoGuia>>("No se puede obtener estados", false){
			@Override
			public void onSuccess(Method method, List<EstadoGuia> response) {
				GWT.log("onSuccess:" + response.size());
				for (EstadoGuia e : response) {
					estadoListBox.addItem(e.getEstadoDescripcion());
				}
			}
		});
	}
	
}
