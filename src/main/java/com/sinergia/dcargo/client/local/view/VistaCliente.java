package com.sinergia.dcargo.client.local.view;

import java.util.List;

import javax.inject.Singleton;
import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sinergia.dcargo.client.local.presenter.PresentadorClientes;
import com.sinergia.dcargo.client.shared.Cliente;

@Singleton
public class VistaCliente extends View<Cliente> implements PresentadorClientes.Display {
	
	public VistaCliente() {
		super(10);
	}
	
	public VistaCliente(int paging) {
		super(paging);
	}
	
	private TextBox nombresTextField = new TextBox();
	private TextBox direccionTextField = new TextBox();
	private TextBox telefonoTextField = new TextBox();
	private TextBox nitTextField = new TextBox();
	private TextBox ciTextField = new TextBox();
	private IntegerBox codigoIntegerBox = new IntegerBox();
	
	private Button buscarBtn = new Button("Buscar");
	
	@Override
	public void viewIU() {
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.add(new HTML("<center style='font-weight:bold;font-size:16px'>Clientes</center>"));
		hpTitulo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hpTitulo.setWidth("100%");
		//hpTitulo.
		
		VerticalPanel vpNorte = new VerticalPanel();
		vpNorte.add(hpTitulo);
		vpNorte.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vpNorte.setHeight("20px");
		
		FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();
	    //layout.setWidget(0, 2, new HTML("<h2>Clientes</h2>"));
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    // Campos
	    layout.setHTML(1, 0, "Nombres:");
	    layout.setWidget(1, 1, nombresTextField);
	    layout.setHTML(1, 2, "Dirección:");
	    layout.setWidget(1, 3, direccionTextField);
	    layout.setHTML(1, 4, "Telefono:");
	    layout.setWidget(1, 5, telefonoTextField);
	    layout.setHTML(2, 0, "NIT");
	    layout.setWidget(2, 1, nitTextField);
	    layout.setHTML(2, 2, "CI:");
	    layout.setWidget(2, 3, ciTextField);
	    layout.setHTML(2, 4, "Código:");
	    layout.setWidget(2, 5, codigoIntegerBox);
	    layout.setWidget(2, 6, buscarBtn);
	    vpNorte.add(layout);
	    
	    // Wrap the content in a DecoratorPanel
//	    DecoratorPanel decPanel = new DecoratorPanel();
//	    decPanel.setWidget(layout);

		
		defaultUI();
		
		// Nombres 
		TextColumn<Cliente> nombresColmun = new TextColumn<Cliente>() {
			@Override
			public String getValue(Cliente entity) {
				return entity.getNombre();
			}
		};
		grid.setColumnWidth(nombresColmun, 80, Unit.PX);
		grid.addColumn(nombresColmun, "Nombres");
		
		// Direccion
		TextColumn<Cliente> direccionColmun = new TextColumn<Cliente>() {
			@Override
			public String getValue(Cliente entity) {
				return entity.getDireccion();
			}
		};
		grid.setColumnWidth(direccionColmun, 40, Unit.PX);
		grid.addColumn(direccionColmun, "Dirección");
		
		// Telefono
		TextColumn<Cliente> telefonoColmun = new TextColumn<Cliente>() {
			@Override
			public String getValue(Cliente entity) {
				return entity.getTelefono();
			}
		};
		grid.setColumnWidth(telefonoColmun, 40, Unit.PX);
		grid.addColumn(telefonoColmun, "Telefono");
		
		// NIT
		TextColumn<Cliente> nitColmun = new TextColumn<Cliente>() {
			@Override
			public String getValue(Cliente entity) {
				return entity.getNit();
			}
		};
		grid.setColumnWidth(nitColmun, 40, Unit.PX);
		grid.addColumn(nitColmun, "NIT");
		
		// CI 
		TextColumn<Cliente> ciColmun = new TextColumn<Cliente>() {
			@Override
			public String getValue(Cliente entity) {
				return entity.getCi();
			}
		};
		grid.setColumnWidth(ciColmun, 40, Unit.PX);
		grid.addColumn(ciColmun, "CI");
		
		// Código
		TextColumn<Cliente> codigoColmun = new TextColumn<Cliente>() {
			@Override
			public String getValue(Cliente entity) {
				return entity.getCodigo() + "";
			}
			
		};
		grid.setColumnWidth(codigoColmun, 40, Unit.PX);
		grid.addColumn(codigoColmun, "Código");
		
		
		grid.setWidth("1000px");
		grid.setHeight("350px");
		
		VerticalPanel vpGrid = new VerticalPanel();
		vpGrid.add(grid);
		
		HorizontalPanel horizontalPanelPager = new HorizontalPanel();
		horizontalPanelPager.setWidth("100%");
		horizontalPanelPager.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanelPager.add(simplePager);
		vpGrid.add(horizontalPanelPager);
		
		DockPanel dock = new DockPanel();
		dock.add(vpNorte, DockPanel.NORTH);
		dock.add(vpGrid, DockPanel.CENTER);
		
		mainContentView.getCentralPanel().add(dock);
	}
	
	@Override
	protected Object getKeyItem(Cliente item) {
		return item.getId();
	}
	@Override
	protected String getNro(Cliente entity) {
		return entity.getNro()+"";
	}

	@Override
	public void cargarDataUI(List<Cliente> clientes) {
		dataProvider.getList().clear();
		dataProvider.setList(clientes);
	}

	@Override
	public HasClickHandlers getBuscarButton() {
		return buscarBtn;
	}

	@Override
	public Cliente getParametrosBusqueda() {
		Cliente cliente = new Cliente();
		cliente.setCodigo(codigoIntegerBox.getValue());
		cliente.setDireccion(direccionTextField.getValue());
		cliente.setNit(nitTextField.getValue());
		cliente.setNombre(nombresTextField.getValue());
		cliente.setTelefono(telefonoTextField.getValue());
		cliente.setCi(ciTextField.getValue());
		return cliente;
	}
	
}
