package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioConocimientoCliente;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.pdf.ImprimirPDF;
import com.sinergia.dcargo.client.local.presenter.PresentadorConocimiento;
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dominio.Transportista;

@Singleton
public class VistaConocimiento extends View<Conocimiento> implements PresentadorConocimiento.Display {

	@Inject
	private VistaConocimientoAccion vistaConocimientoAccion;
	
	@Inject
	private ServicioConocimientoCliente servicioConocimiento;
	
	@Inject
	private Cargador cargador;
	
	@Inject
	private MensajeExito mensajeExito;
	
	@Inject
	private MensajeAviso mensajeAviso;  
	
	@Inject
	private ServicioGuiaCliente servicioGuia;
	
	@Inject
	private ImprimirPDF imprimirPDF;
	
	@Inject
	private AdminParametros adminParametros;
	
	private VistaElegirConocimientoDialogBox vistaElegirConocimientoDialogBox;
	
	private MultiWordSuggestOracle oficinaOracle = new MultiWordSuggestOracle();
	private MultiWordSuggestOracle transportistaOracle = new MultiWordSuggestOracle();
	
	private HTML nroConocimientoLabel = new HTML("<b>Nro. Conocimiento: </b>");
	private IntegerBox nroConocimiento = new IntegerBox();
	
	private HTML fechaInicioLabel = new HTML("<b>Fecha Inicio: </b>");
	private DateBox fechaInicio = new DateBox();
	
	private HTML fechaFinLabel = new HTML("<b>Fecha Fin: </b>");
	private DateBox fechaFin = new DateBox();
	
	private HTML propietarioLabel = new HTML("<b>Propietario: </b>");
	private SuggestBox propietarioSuggestBox  = new SuggestBox(transportistaOracle);
	
	private HTML conductorLabel = new HTML("<b>Conductor: </b>");
	private SuggestBox conductorSuggestBox  = new SuggestBox(transportistaOracle);
	
	private HTML origenLabel = new HTML("<b>Origen: </b>");
	private SuggestBox origenSuggestBox  = new SuggestBox(oficinaOracle);
	
	private HTML destinoLabel = new HTML("<b>Destino: </b>");
	private SuggestBox destinoSuggestBox  = new SuggestBox(oficinaOracle);
	
	private Button buscarBtn = new Button("Buscar");
	
	private ListBox estadoListBox = new ListBox();
	
	private Button nuevoBtn = new Button("Nuevo");
	private Button consultarBtn = new Button("Consultar");	
	private Button modificarBtn = new Button("Modificar");
	private Button anularBtn = new Button("Anular");
	private Button imprimirBtn = new Button("Imprimir Búsqueda");
	private Button salirBtn = new Button("Salir");
	
	private Button seleccionBtn = new Button("Seleccion");
	private Button salirSelecionBtn = new Button("Salir");
	
	public VistaConocimiento() {
		super(10);
	}
	
	protected VistaConocimiento(int paging) {
		super(paging);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IsWidget viewIU(boolean esDialogBox) {
		
		// formatos
		fechaInicio.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		fechaFin.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.setWidth("100%");
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Conocimiento</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.add(hpTitulo);
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		vpNorte.setWidth("100%");
		
		// Campos
		FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    layout.setWidget(1, 0, propietarioLabel);
	    layout.setWidget(1, 1, propietarioSuggestBox);
	    layout.setWidget(2, 0, conductorLabel);
	    layout.setWidget(2, 1, conductorSuggestBox);
	    
	    layout.setWidget(1, 2, origenLabel);
	    layout.setWidget(1, 3, origenSuggestBox);
	    layout.setWidget(2, 2, destinoLabel);
	    layout.setWidget(2, 3, destinoSuggestBox);
	    
	    layout.setWidget(1, 4, fechaInicioLabel);
	    layout.setWidget(1, 5, fechaInicio);
	    layout.setWidget(2, 4, fechaFinLabel);
	    layout.setWidget(2, 5, fechaFin);
	    
	    layout.setWidget(3, 0, nroConocimientoLabel);
	    layout.setWidget(3, 1, nroConocimiento);
	    
	    layout.setHTML(3, 2, "<b>Estado: </b>");
	    layout.setWidget(3, 3, estadoListBox);
	    
	    layout.setWidget(3, 4, buscarBtn);
	    
	    vpNorte.add(layout);
	    
	    // Default
	    defaultUI();
	    
		// NroGuia
		TextColumn<Conocimiento> nroGuiaColmun = new TextColumn<Conocimiento>() {
			@Override
			public String getValue(Conocimiento entity) {
				return entity.getNroConocimiento() + "";
			}
		};
		grid.setColumnWidth(nroGuiaColmun, 10, Unit.PX);
		grid.addColumn(nroGuiaColmun, "Conoc.");
		
		// Origen
		TextColumn<Conocimiento> origenColmun = new TextColumn<Conocimiento>() {
			@Override
			public String getValue(Conocimiento entity) {
				return entity.getOficinaOrigen() == null?"":entity.getOficinaOrigen().getNombre();
			}
		};
		grid.setColumnWidth(origenColmun, 20, Unit.PX);
		grid.addColumn(origenColmun, "Origen");
		
		// Destino
		TextColumn<Conocimiento> destinoColmun = new TextColumn<Conocimiento>() {
			@Override
			public String getValue(Conocimiento entity) {
				return entity.getOficinaDestino() == null?"":entity.getOficinaDestino().getNombre();
			}
		};
		grid.setColumnWidth(destinoColmun, 20, Unit.PX);
		grid.addColumn(destinoColmun, "Destino");
		
		// Propietario
		TextColumn<Conocimiento> propietarioColmun = new TextColumn<Conocimiento>() {
			@Override
			public String getValue(Conocimiento entity) {
				return entity.getTransportistaPropietario() == null? "":entity.getTransportistaPropietario().getNombre();
			}
		};
		grid.setColumnWidth(propietarioColmun, 30, Unit.PX);
		grid.addColumn(propietarioColmun, "Propietario");
	    
		// Conductor
		TextColumn<Conocimiento> conductorColmun = new TextColumn<Conocimiento>() {
			@Override
			public String getValue(Conocimiento entity) {
				return entity.getTransportistaConductor() == null? "":entity.getTransportistaConductor().getNombre();
			}
		};
		grid.setColumnWidth(conductorColmun, 30, Unit.PX);
		grid.addColumn(conductorColmun, "Conductor");
		
		// Fecha
		TextColumn<Conocimiento> fechaColmun = new TextColumn<Conocimiento>() {
			@Override
			public String getValue(Conocimiento entity) {
				if(entity.getFechaRegistro() != null){
					return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(entity.getFechaRegistro());
				}
				return "";	
				}
		};
		grid.setColumnWidth(fechaColmun, 15, Unit.PX);
		grid.addColumn(fechaColmun, "Registro");
		
		// Estado
		TextColumn<Conocimiento> estadoColmun = new TextColumn<Conocimiento>() {
			@Override
			public String getValue(Conocimiento entity) {
				if(entity.getEstadoDescripcion() != null){
					return entity.getEstadoDescripcion();
				}
				return "";
			}
		};
		grid.setColumnWidth(estadoColmun, 10, Unit.PX);
		grid.addColumn(estadoColmun, "Estado");
		
		
		//
		grid.setHeight("300px");
		
		VerticalPanel vpGrid = new VerticalPanel();
		vpGrid.setWidth("100%");
		vpGrid.add(grid);
		
		/// ACCION para seleccionar
		HorizontalPanel horizontalPanelSelect = new HorizontalPanel();
		horizontalPanelSelect.setWidth("100%");
		horizontalPanelSelect.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
				
		HorizontalPanel horizontalPanelButtonSelect = new HorizontalPanel();
		horizontalPanelButtonSelect.setSpacing(5);
		horizontalPanelButtonSelect.add(seleccionBtn);
		horizontalPanelButtonSelect.add(salirSelecionBtn);
		horizontalPanelSelect.add(horizontalPanelButtonSelect);
		
		HorizontalPanel horizontalPanelPager = new HorizontalPanel();
		horizontalPanelPager.setWidth("100%");
		horizontalPanelPager.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanelPager.add(simplePager);
		vpGrid.add(horizontalPanelPager);
		
		// Acciones
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
		horizontalPanelButton.add(salirBtn);
		horizontalPanel.add(horizontalPanelButton);
		
		// Layout general
		DockPanel dock = new DockPanel();
		dock.setWidth("100%");
		dock.setHeight("100%");
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		
		if(esDialogBox) dock.add(horizontalPanelSelect, DockPanel.SOUTH);
		else            dock.add(horizontalPanel, DockPanel.SOUTH);
		
		//mainContentView.getCentralPanel().add(dock);
		
		cargarDatosIniciles();
		implementarAcciones();
		
		if(esDialogBox) {
			accionPresentador();
			return dock;
		} else mainContentView.getCentralPanel().add(dock);
		
		return null;
	}
	
	private void accionPresentador() {
		log.info(this.getClass().getSimpleName() + ".go()" );
		//display.viewIU();
		log.info(this.getClass().getSimpleName() + ".go()2" );
		
		List<Transportista> transportistas = adminParametros.getTransportistas();
		
		List<String> palabras = new ArrayList<>();
		for (Transportista cli :transportistas) {
			if(cli.getNombre() != null){
				palabras.add(cli.getNombre());
			}
		}
		log.info(this.getClass().getSimpleName() + ".go()3: ");
		
		fijarOracleParaTransportistas(palabras);
		log.info(this.getClass().getSimpleName() + ".go()4" );
		
		buscarBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Conocimiento conocimiento = getParametrosBusqueda();
				log.info("conocimiento: "+ conocimiento);
				cargador.center();
				servicioConocimiento.buscarConocimiento(conocimiento, new MethodCallback<List<Conocimiento>>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al traer Guias: " + exception.getMessage());
						cargador.hide();
						new MensajeError("Error al traer Guias: ", exception).show();
					}
					@Override
					public void onSuccess(Method method, List<Conocimiento> response) {
						showGuiaData(response);
						cargador.hide();
					}
				});
			}
		});
		
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public void cargarDataUI(List<Conocimiento> conocimientos) {
		dataProvider.getList().clear();
		dataProvider.setList(conocimientos);
	}

	@Override
	public Conocimiento getParametrosBusqueda() {
		
		Conocimiento conocimiento = new Conocimiento();
		
		Oficina origen = new Oficina();
		origen.setId(getIdFromNombreOficina(origenSuggestBox.getValue()));
		conocimiento.setOficinaOrigen(origen);
		
		Oficina destino = new Oficina();
		destino.setId(getIdFromNombreOficina(destinoSuggestBox.getValue()));
		conocimiento.setOficinaDestino(destino);
		
		Transportista propiedad = new Transportista();
		propiedad.setId(getIdFromNombreTransportista(propietarioSuggestBox.getValue()));
		conocimiento.setTransportistaPropietario(propiedad);
		
		Transportista conductor = new Transportista();
		conductor.setId(getIdFromNombreTransportista(conductorSuggestBox.getValue()));
		conocimiento.setTransportistaConductor(conductor);
		
		conocimiento.setNroConocimiento(nroConocimiento.getValue());
		conocimiento.setEstadoDescripcion(estadoListBox.getSelectedValue());
		
		return conocimiento;
	}

	@Override
	public void fijarOracleParaTransportistas(List<String> palabras) {
		transportistaOracle.addAll(palabras);
	}
	
	@Override
	protected Object getKeyItem(Conocimiento item) {
		return item.getId();
	}

	@Override
	protected String getNro(Conocimiento entity) {
		return entity.getNro()+"";
	}
	
	
	private void cargarDatosIniciles() {
		servicioConocimiento.getEstados(new LlamadaRemota<List<EstadoGuia>>("No se puede obtener estados", false){
			@Override
			public void onSuccess(Method method, List<EstadoGuia> response) {
				estadoListBox.clear();
				estadoListBox.addItem("Todos");
				for (EstadoGuia e : response) {
					estadoListBox.addItem(e.getEstadoDescripcion());
				}
				estadoListBox.setSelectedIndex(2);
			}
		});
		
	}
	
	private void implementarAcciones() {
		nuevoBtn.addClickHandler(e -> vistaConocimientoAccion.mostrar(ConocimientoAccion.NUEVO, null));
		consultarBtn.addClickHandler(e -> {
			@SuppressWarnings("unchecked")
			Conocimiento conocimiento = ((SingleSelectionModel<Conocimiento>)grid.getSelectionModel()).getSelectedObject();
			if(conocimiento == null)
				mensajeAviso.mostrar("Seleccione la Conocimiento que decea consultar");
			else 
				vistaConocimientoAccion.mostrar(ConocimientoAccion.CONSULTAR, conocimiento);
		});
		modificarBtn.addClickHandler(e -> {
			@SuppressWarnings("unchecked")
			Conocimiento conocimiento = ((SingleSelectionModel<Conocimiento>)grid.getSelectionModel()).getSelectedObject();
			if(conocimiento == null)
				mensajeAviso.mostrar("Seleccione la Conocimiento que decea consultar");
			else
				vistaConocimientoAccion.mostrar(ConocimientoAccion.MODIFICAR, conocimiento);
		});
		anularBtn.addClickHandler(e -> {
			@SuppressWarnings("unchecked")
			Conocimiento conocimiento = ((SingleSelectionModel<Conocimiento>)grid.getSelectionModel()).getSelectedObject();
			if(conocimiento == null){
				new MensajeAviso("Seleccione la Conocimiento que decea anular").show();
			} else {
				VistaConocimiento.this.cargador.center();
				servicioGuia.cambiarEstado(conocimiento.getId(), "Anulado", new LlamadaRemota<Void>("No se pudo anular la Conocimiento", true) {
					@Override
					public void onSuccess(Method method, Void response) {
						mensajeExito.mostrar("Conocimiento anulada existosamente, con nro: " + conocimiento.getNroConocimiento());
						mensajeExito.center();
						VistaConocimiento.this.cargador.hide();
					}
				});
			}
		});
		imprimirBtn.addClickHandler(e -> {
			List<Conocimiento> cs = dataProvider.getList();
			String[][] guiasImprimir = new String[cs.size()][8];
			int k = 0;
			for (Conocimiento conocimiento : cs) {
				String propietario = "";
				if(conocimiento.getTransportistaPropietario()!=null) propietario = conocimiento.getTransportistaPropietario().getNombre();
				String conductor = "";
				if(conocimiento.getTransportistaConductor()!=null) conductor = conocimiento.getTransportistaConductor().getNombre(); 
				guiasImprimir[k][0]  = (k+1)+"";
				guiasImprimir[k][1]  = conocimiento.getNroConocimiento()+"";
				guiasImprimir[k][2]  = conocimiento.getOficinaOrigen()==null?"":conocimiento.getOficinaOrigen().getNombre();
				guiasImprimir[k][3]  = conocimiento.getOficinaDestino()==null?"":conocimiento.getOficinaDestino().getNombre();
				guiasImprimir[k][4]  = propietario;
				guiasImprimir[k][5]  = conductor;
				guiasImprimir[k][6]  = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(conocimiento.getFechaRegistro());
				guiasImprimir[k][7]  = conocimiento.getEstadoDescripcion(); 
				k++;
			}
			imprimirPDF.generarImpresionBusquedaConocimientos(guiasImprimir);
		});
		salirBtn.addClickHandler(e -> {
			Window.Location.assign(com.google.gwt.core.client.GWT.getHostPageBaseURL());
		});
		
		seleccionBtn.addClickHandler(e -> {
			@SuppressWarnings("unchecked")
			Conocimiento guia = ((SingleSelectionModel<Conocimiento>)grid.getSelectionModel()).getSelectedObject();
			if(guia == null) {
				mensajeAviso.mostrar("Seleccione un Conocimiento");
				return ;
			}
			vistaElegirConocimientoDialogBox.setConocimientoSeleccionada(guia);
		});
		
		salirSelecionBtn.addClickHandler(e -> {
			vistaElegirConocimientoDialogBox.hide();
		});
		
		anularBtn.setEnabled(true);
		imprimirBtn.setEnabled(true);
		salirBtn.setEnabled(true);
		
	}
	
	private Long getIdFromNombreTransportista(String nombre){
		List<Transportista> transportistas = adminParametros.getTransportistas();
		for (Transportista t: transportistas) {
			if(t != null && t.getNombre() != null)
				if(t.getNombre().equals(nombre)) return t.getId();
		}
		return null;
	}
	
	private Long getIdFromNombreOficina(String nombre){
		List<Oficina> oficinas = adminParametros.getOficinas();
		for (Oficina o: oficinas) {
			if(o != null)
				if(o.getNombre().equals(nombre)) return o.getId();
		}
		return null;
	}
	
	public void setVistaElegirConocimientoDialogBox(VistaElegirConocimientoDialogBox vistaElegirConocimientoDialogBox) {
		this.vistaElegirConocimientoDialogBox = vistaElegirConocimientoDialogBox;
	}
	
	int i = 1;
	private void showGuiaData(List<Conocimiento> conocimientos) {
		for (Conocimiento guia: conocimientos) {
			guia.setNro(i++);
		}
		i = 1;
		cargarDataUI(conocimientos);
	}
	
}
