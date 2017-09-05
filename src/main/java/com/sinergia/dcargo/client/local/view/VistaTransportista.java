package com.sinergia.dcargo.client.local.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioTransportistasCliente;
import com.sinergia.dcargo.client.local.message.MensajeConfirmacion;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.presenter.PresentadorTransportistas;
import com.sinergia.dcargo.client.shared.dominio.Transportista;

@Singleton
public class VistaTransportista extends View<Transportista> implements PresentadorTransportistas.Display {
	
	@Inject
	VistaTransportistaAccion vistaTransportistaAccion; 
	
	@Inject
	MensajeConfirmacion mensageConfirmacion;
	
	@Inject 
	ServicioTransportistasCliente servicioTransportista;
	
	@Inject 
	MensajeExito mensajeExito;
	
	public VistaTransportista() {
		super(10);
	}
	
	public VistaTransportista(int paging) {
		super(paging);
	}
	
	private TextBox nombresTextField = new TextBox();
	private TextBox direccionTextField = new TextBox();
	private TextBox telefonoTextField = new TextBox();
	private TextBox placaTextField = new TextBox();
	private TextBox marcaTextField = new TextBox();
	private TextBox colorTextField = new TextBox();
	private TextBox vecinoTextField = new TextBox();
	private TextBox ciTextField = new TextBox();
	
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
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Transportistas</center>"));
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
	    layout.setWidget(0, 0, new HTML("<b>Nombres: </b>"));
	    layout.setWidget(0, 1, nombresTextField);
	    layout.setWidget(0, 2, new HTML("<b>CI o Brevet: </b>"));
	    layout.setWidget(0, 3, ciTextField);
	    layout.setWidget(0, 4, new HTML("<b>Dirección: </b>"));
	    layout.setWidget(0, 5, direccionTextField);
	    layout.setWidget(0, 6, new HTML("<b>Telefono: </b>"));
	    layout.setWidget(0, 7, telefonoTextField);
	    layout.setWidget(1, 0, new HTML("<b>Placa: </b>"));
	    layout.setWidget(1, 1, placaTextField);
	    layout.setWidget(1, 2, new HTML("<b>Marca: </b>"));
	    layout.setWidget(1, 3, marcaTextField);
	    layout.setWidget(1, 4, new HTML("<b>Color: </b>"));
	    layout.setWidget(1, 5, colorTextField);
	    layout.setWidget(1, 6, new HTML("<b>Vecino </b>"));
	    layout.setWidget(1, 7, vecinoTextField);
	    layout.setWidget(1, 8, buscarBtn);
	    
	    vpNorte.add(layout);
		
		// Nombres 
		TextColumn<Transportista> nombresColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getNombre();
			}
		};
		grid.setColumnWidth(nombresColmun, 80, Unit.PX);
		grid.addColumn(nombresColmun, "Nombres");
		
		// Brevet
		TextColumn<Transportista> brevetColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getBrevetCi();
			}
		};
		grid.setColumnWidth(brevetColmun, 40, Unit.PX);
		grid.addColumn(brevetColmun, "Brevet");
		
		// CI
		TextColumn<Transportista> ciColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getCi();
			}
		};
		grid.setColumnWidth(ciColmun, 40, Unit.PX);
		grid.addColumn(ciColmun, "CI");
		
		// Direccion
		TextColumn<Transportista> direccionColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getDireccion();
			}
		};
		grid.setColumnWidth(direccionColmun, 80, Unit.PX);
		grid.addColumn(direccionColmun, "Dirección");
		
		// Telefono
		TextColumn<Transportista> telefonoColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getTelefono();
			}
		};
		grid.setColumnWidth(telefonoColmun, 40, Unit.PX);
		grid.addColumn(telefonoColmun, "Teléfono");
		
		// Placa
		TextColumn<Transportista> placaColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getPlaca();
			}
		};
		grid.setColumnWidth(placaColmun, 40, Unit.PX);
		grid.addColumn(placaColmun, "Placa");
		
		// Marca
		TextColumn<Transportista> marcaColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getMarca();
			}
		};
		grid.setColumnWidth(marcaColmun, 40, Unit.PX);
		grid.addColumn(marcaColmun, "Marca");
		
		// Color
		TextColumn<Transportista> colorColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getColor();
			}
		};
		grid.setColumnWidth(colorColmun, 40, Unit.PX);
		grid.addColumn(colorColmun, "Color");
		
		// Vecino de
		TextColumn<Transportista> vecinoColmun = new TextColumn<Transportista>() {
			@Override
			public String getValue(Transportista entity) {
				return entity.getVecino_de();
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
		
		buscarBtn.addClickHandler( e -> ((SingleSelectionModel<Transportista>)grid.getSelectionModel()).clear());
		
		consultarBtn.addClickHandler(e -> {
			Transportista transportista = ((SingleSelectionModel<Transportista>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + transportista);
			if(transportista == null){
				new MensajeAviso("Seleccione un Transportista").show();
			} else {
				vistaTransportistaAccion.mostrar(TransportistaAccion.CONSULTAR, transportista);
			}
		});
		nuevoBtn.addClickHandler(e->vistaTransportistaAccion.mostrar(TransportistaAccion.NUEVO, null));
		modificarBtn.addClickHandler(e->{
			Transportista Transportista = ((SingleSelectionModel<Transportista>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + Transportista);
			if(Transportista == null){
				new MensajeAviso("Seleccione un Transportista").show();
			} else {
				vistaTransportistaAccion.mostrar(TransportistaAccion.MODIFICAR, Transportista);
			}
		});
		eliminarBtn.addClickHandler(e->{
			final Transportista Transportista = ((SingleSelectionModel<Transportista>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + Transportista);
			if(Transportista == null){
				new MensajeAviso("Seleccione un Transportista").show();
			} else {
				mensageConfirmacion.mostrar("Decea eliminar al Transportista: " + Transportista.getNombre(), new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						servicioTransportista.borrar(Transportista.getId(), new LlamadaRemota<Void>("No se puede eliminar Transportista", true) {
							@Override
							public void onSuccess(Method method, Void response) {
								VistaTransportista.this.mensageConfirmacion.hide();
								buscarBtn.click();
								//mensajeExito.mostrar("Eliminado exitosamente");
							}
						});
					}
				});
			}
		});
		salirBtn.addClickHandler(e -> Window.Location.assign(GWT.getHostPageBaseURL()));
	}

	@Override
	protected Object getKeyItem(Transportista item) {
		return item.getId();
	}
	@Override
	protected String getNro(Transportista entity) {
		return entity.getNro()+"";
	}

	@Override
	public void cargarDataUI(List<Transportista> Transportistas) {
		dataProvider.getList().clear();
		dataProvider.setList(Transportistas);
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public Transportista getParametrosBusqueda() {
		Transportista t = new Transportista();
		t.setNombre(nombresTextField.getValue());
		t.setDireccion(direccionTextField.getValue());
		t.setTelefono(telefonoTextField.getValue());
		t.setMarca(marcaTextField.getValue());
		t.setPlaca(placaTextField.getValue());
		t.setColor(colorTextField.getValue());
		t.setVecino_de(vecinoTextField.getValue());
		t.setCi(ciTextField.getValue());
		return t;
	}
	
}
