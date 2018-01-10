package com.sinergia.dcargo.client.local.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SafeHtmlHeader;
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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.api.ServicioMovimientoCliente;
import com.sinergia.dcargo.client.local.message.MensajeConfirmacion;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.local.presenter.PresentadorMovimiento;
import com.sinergia.dcargo.client.shared.dominio.Cuenta;
import com.sinergia.dcargo.client.shared.dominio.CuentaEgreso;
import com.sinergia.dcargo.client.shared.dominio.CuentaIngreso;
import com.sinergia.dcargo.client.shared.dominio.Movimiento;
import com.sinergia.dcargo.client.shared.dominio.TipoCuenta;
import com.sinergia.dcargo.client.shared.dominio.Usuario;

@Singleton
public class VistaMovimiento extends View<Movimiento> implements PresentadorMovimiento.Display {
	
	@Inject VistaMovimientoAccion         vistaMovimientoAccion; 
	@Inject MensajeConfirmacion           mensageConfirmacion;
	@Inject MensajeExito                  mensajeExito;
	@Inject MensajeAviso                  mensajeAviso;
	@Inject ServicioCuentaCliente         servicioCuenta;
	@Inject ServicioMovimientoCliente     servicioMovimiento;
	@Inject AdminParametros               adminParametros;
	
	public VistaMovimiento() { super(10); }
	public VistaMovimiento(int paging) { super(paging); }
	
	private ListBox tipoCuentaListBox = new ListBox();
	
	private ListBox cuentaListBox    = new ListBox();
	private ListBox subCuentaListBox = new ListBox();
	
	private DateBox fechaIniDateBox = new DateBox();
	private DateBox fechaFinDateBox = new DateBox();
	
	private HTML       cuentaLabel               = new HTML("<b>Nro Comprobante: </b>");
	private IntegerBox nroComprobanteIntegerBox         = new IntegerBox();
	private IntegerBox nroGuiaIntegerBox         = new IntegerBox();
	//private IntegerBox nroConocimientoIntegerBox = new IntegerBox();
	private Widget     nroIntegerBox             = nroComprobanteIntegerBox;
	
	private MovimientoAccion movimientoAccion;
	
	private TextBox glosaTextBox = new TextBox();
	private ListBox estadoListBox = new ListBox();
	
	private ListBox usuarioListBox = new ListBox();
	
	private Button buscarBtn = new Button("Buscar");
	
	private Button consultarBtn = new Button("Consultar");
	private Button nuevoBtn = new Button("Nuevo");
	private Button modificarBtn = new Button("Modificar");
	private Button anularBtn = new Button("Anular");
	private Button salirBtn = new Button("Salir");
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void viewIU() {
		
		// Config
		defaultUI();
		//grid.setWidth("1000px");
		
		
		fechaIniDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		fechaFinDateBox.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getShortDateFormat()));
		
		// Título
		HorizontalPanel hpTitulo = new HorizontalPanel();
		hpTitulo.add(new HTML("<center class='tituloModulo'> Buscar Movimiento </center>"));
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
	    layout.setWidget(1, 0, new HTML("<b>Fecha Inicial: </b>"));
	    layout.setWidget(1, 1, fechaIniDateBox);
	    layout.setWidget(2, 0, new HTML("<b>Fecha Final: </b>"));
	    layout.setWidget(2, 1, fechaFinDateBox);
	    
	    layout.setWidget(0, 2, cuentaLabel);
	    layout.setWidget(0, 3, nroIntegerBox);
	    layout.setWidget(1, 2, new HTML("<b>Nro Guía: </b>"));
	    layout.setWidget(1, 3, nroGuiaIntegerBox);
	    layout.setWidget(2, 2, new HTML("<b>Glosa: </b>"));
	    layout.setWidget(2, 3, glosaTextBox);
	    
	    
	    
	    layout.setWidget(0, 4, new HTML("<b>Cuenta: </b>"));
	    layout.setWidget(0, 5, cuentaListBox);
	    layout.setWidget(1, 4, new HTML("<b>Sub Cuenta: </b>"));
	    layout.setWidget(1, 5, subCuentaListBox);
	    layout.setWidget(2, 4, new HTML("<b>Estado: </b>"));
	    layout.setWidget(2, 5, estadoListBox);
	    
	    
	    if(adminParametros.getUsuario().getAdministrador()) {
	    	layout.setWidget(0, 6, new HTML("<b>Usuario: </b>"));
		    layout.setWidget(0, 7, usuarioListBox);
		    layout.setWidget(1, 6, buscarBtn);
	    } else {
	    	layout.setWidget(1, 6, buscarBtn);
	    }
	    
	    vpNorte.add(layout);
		
		// Tipo 
		TextColumn<Movimiento> tipoColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				return entity.getTipoCuenta().name();
			}
		};
		grid.setColumnWidth(tipoColmun, 30, Unit.PX);
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
		
		// Suc cuenta
		TextColumn<Movimiento> direccionColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				if(entity.getCuenta() == null) {
					return "";
				}
				return entity.getCuenta().getNroCuenta() + " - " + entity.getCuenta().getDescripcion();
			}
		};
		grid.setColumnWidth(direccionColmun, 80, Unit.PX);
		grid.addColumn(direccionColmun, "Sub Cuenta");
		
		// Monto
		TextColumn<Movimiento> telefonoColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				if(entity.getMonto() == null) {
					return "";
				}
				String formatted = NumberFormat.getFormat("0.00").format(entity.getMonto());				
				return formatted;
			}
		};
		Header<String> ageFooter = new Header<String>(new TextCell()) {
		      @Override
		      public String getValue() {
		        List<Movimiento> items = grid.getVisibleItems();
		        if (items.size() == 0) {
		          return "";
		        } else {
		          Double total = 0.0;
		          for (Movimiento item : items) {
		        	  if(item.getTipoCuenta() == TipoCuenta.INGRESO)
		        		  total += item.getMonto() == null ? 0 : item.getMonto();
		              if(item.getTipoCuenta() == TipoCuenta.EGRESO)
		            	  total -= item.getMonto();
		          }
		          String totalFormatted = NumberFormat.getFormat("0.00").format(total);
		          return "" + totalFormatted + " Bs";
		        }
		      }
		};
		grid.setColumnWidth(telefonoColmun, 40, Unit.PX);
		grid.addColumn(telefonoColmun, new SafeHtmlHeader(SafeHtmlUtils.fromSafeConstant("Monto")), ageFooter);
		
		// Guia/Conocimiento
		TextColumn<Movimiento> guiaConocimientoColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				return entity.getNroGuiOrConocimiento();
			}
		};
		grid.setColumnWidth(guiaConocimientoColmun, 50, Unit.PX);
		grid.addColumn(guiaConocimientoColmun, "Guía/Conocimiento");
		
		// Origen
		TextColumn<Movimiento> placaColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				return entity.getOrigen();
			}
		};
		grid.setColumnWidth(placaColmun, 40, Unit.PX);
		grid.addColumn(placaColmun, "Origen");
		
		// Destino
		TextColumn<Movimiento> marcaColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				return entity.getDestino();
			}
		};
		grid.setColumnWidth(marcaColmun, 40, Unit.PX);
		grid.addColumn(marcaColmun, "Destino");
		
		// Usuario
		TextColumn<Movimiento> usuarioColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				return entity.getNombreUsuarioRev();
			}
			
		};
		if(adminParametros.getUsuario().getAdministrador())
			grid.addColumn(usuarioColmun, "Usuario");
		grid.setColumnWidth(usuarioColmun, 40, Unit.PX);
		
		
		// Estado
		TextColumn<Movimiento> estadoColmun = new TextColumn<Movimiento>() {
			@Override
			public String getValue(Movimiento entity) {
				return entity.getEstadoDescripcion();
			}
			
		};
		grid.setColumnWidth(estadoColmun, 40, Unit.PX);
		grid.addColumn(estadoColmun, "Estado");
		
		
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
		if(adminParametros.getUsuario().getAdministrador())
			horizontalPanelButton.add(modificarBtn);
		horizontalPanelButton.add(anularBtn);
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
		
		fechaIniDateBox.setValue(adminParametros.getDateParam().getDate());
		fechaFinDateBox.setValue(adminParametros.getDateParam().getDate());
		
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
		log.info("getParametrosBusqueda()");
		
		TipoCuenta tipoCuenta = null;
		if(!tipoCuentaListBox.getSelectedValue().equals("Todos")) 
			tipoCuenta = tipoCuentaListBox.getSelectedValue().equals(TipoCuenta.INGRESO.name())?TipoCuenta.INGRESO:TipoCuenta.EGRESO;
		
		Long idCuenta = Long.parseLong(subCuentaListBox.getSelectedValue());
		Cuenta cuenta = null;
		if(idCuenta != 0L) {
			cuenta = new Cuenta();
			cuenta.setId(idCuenta);
		}
		Integer nroComprobante = null;
		if(nroComprobanteIntegerBox.getValue() != null)
			nroComprobante = nroComprobanteIntegerBox.getValue();
		
		Movimiento t = new Movimiento();
		t.setTipoCuenta(tipoCuenta);
		t.setCuenta(cuenta);
		t.setNroComprobante(nroComprobante);
		t.setEstado(estadoListBox.getSelectedValue().equals("Todos")?null:estadoListBox.getSelectedValue().charAt(0));
		t.setFechaRegistroIni(fechaIniDateBox.getValue());
		t.setFechaRegistroFin(fechaFinDateBox.getValue());
		
		t.setNombreUsuarioRev(usuarioListBox.getSelectedItemText());
		
		return t;
	}
	
	@SuppressWarnings("unchecked")
	private void implementarEscuchadores(){
		
		tipoCuentaListBox.addChangeHandler(e -> {
			log.info("Tipo Cuenta  : " + tipoCuentaListBox.getSelectedValue());
			if(tipoCuentaListBox.getSelectedValue() == null) return ;
			
			if(tipoCuentaListBox.getSelectedValue().equals("Todos")){
				tipoCuentaListBox.clear();
				tipoCuentaListBox.addItem("Todos", "Todos");
				tipoCuentaListBox.addItem(TipoCuenta.INGRESO.name(), TipoCuenta.INGRESO.name());
				tipoCuentaListBox.addItem(TipoCuenta.EGRESO.name(), TipoCuenta.EGRESO.name());
				
				cuentaListBox.clear();
				cuentaListBox.addItem("Todos", "0");
				subCuentaListBox.clear();
				subCuentaListBox.addItem("Todos", "0");
				return ;
			}
				
			if(tipoCuentaListBox.getSelectedValue().equals(TipoCuenta.INGRESO.name())) {
				cargarDatosCuentaIngresoListBox();
			} else
				cargarDatosCuentaEgresoListBox();
			
			subCuentaListBox.clear();
			subCuentaListBox.addItem("Todos", "0");
		});
		cuentaListBox.addChangeHandler(e -> {
			log.info("Cuenta: " + cuentaListBox.getSelectedValue());
			if(cuentaListBox.getSelectedValue() == null) return ;
			
			cargarDatosSubCuentaListBox();
		});
		
		buscarBtn.addClickHandler( e -> ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).clear());
		
		consultarBtn.addClickHandler(e -> {
			Movimiento movimiento = ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + movimiento);
			if(movimiento == null){
				mensajeAviso.mostrar("Seleccione un movimiento");
			} else {
				//vistaMovimientoAccion.mostrar(MovimientoAccion.CONSULTAR, movimiento);
			}
		});
		
//		nuevoBtn.addClickHandler(e->vistaMovimientoAccion.mostrar(MovimientoAccion.NUEVO_INGRESO, null));
//		nuevoBtn.addClickHandler(e->vistaMovimientoAccion.mostrar(MovimientoAccion.NUEVO_EGRESO, null));
		
		modificarBtn.addClickHandler(e->{
			Movimiento movimiento = ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).getSelectedObject();
			log.info("Movimiento: " + movimiento);
			if(movimiento == null){
				new MensajeAviso("Seleccione un Movimiento").show();
			} else {
				//vistaMovimientoAccion.mostrar(MovimientoAccion.MODIFICAR, movimiento);
			}
		});
		anularBtn.addClickHandler(e->{
			final Movimiento movimiento = ((SingleSelectionModel<Movimiento>)grid.getSelectionModel()).getSelectedObject();
			log.info("Transportista: " + movimiento);
			if(movimiento == null){
				new MensajeAviso("Seleccione un Movimiento").show();
			} else {
				mensageConfirmacion.mostrar("Realmente decea anular este movimiento: " + movimiento.getTipoCuenta().name(), new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) { 
						servicioMovimiento.cambiarEstado(movimiento.getId(), "A", new LlamadaRemota<Void>("No se puede anular Conocimiento", true) {
							@Override
							public void onSuccess(Method method, Void response) {
								VistaMovimiento.this.mensageConfirmacion.hide();
								buscarBtn.click();
								//mensajeExito.mostrar("Eliminado exitosamente");
							}
						});
					}
				});				
			}
		});
	}
	
	private void cargarDatosIniciales() {
		tipoCuentaListBox.clear();
		tipoCuentaListBox.addItem("Todos", "Todos");
		tipoCuentaListBox.addItem(TipoCuenta.INGRESO.name(), TipoCuenta.INGRESO.name());
		tipoCuentaListBox.addItem(TipoCuenta.EGRESO.name(), TipoCuenta.EGRESO.name());
		
		cuentaListBox.addItem("Todos", "0");
		subCuentaListBox.clear();
		subCuentaListBox.addItem("Todos", "0");

		servicioMovimiento.getEstados(new LlamadaRemota<List<String>>("No se puede obtener estados", true) {
			@Override
			public void onSuccess(Method method, List<String> response) {
				estadoListBox.clear();
				estadoListBox.addItem("Todos");
				response.forEach(e -> estadoListBox.addItem(e));
				estadoListBox.setSelectedIndex(2);
			}
		});
		
	}
	
	private void cargarDatosCuentaIngresoListBox() {
		cuentaListBox.clear();
		cuentaListBox.addItem("Todos","0");
		List<CuentaIngreso> cuentasIngreso = adminParametros.getCuentasIngreso();
		log.info("cuentasIngreso.size(): " + cuentasIngreso.size());
		for (CuentaIngreso c: cuentasIngreso) {
			cuentaListBox.addItem(c.getNroCuenta() + " - " + c.getDescripcion(), c.getId()+"");
		}
	}
	
	private void cargarDatosCuentaEgresoListBox() {
		cuentaListBox.clear();
		cuentaListBox.addItem("Todos","0");
		List<CuentaEgreso> cuentasEgreso = adminParametros.getCuentasEgreso();
		for (CuentaEgreso c: cuentasEgreso) {
			cuentaListBox.addItem(c.getNroCuenta() + " - " + c.getDescripcion(), c.getId()+"");
		}
		cuentaListBox.setItemSelected(0, true);
	}
	
	private void cargarDatosSubCuentaListBox(){
		Long id = Long.parseLong(cuentaListBox.getSelectedValue());
		if(id == 0) {
			subCuentaListBox.clear();
			subCuentaListBox.addItem("Todos", "0");
			return ;
		} 
		servicioCuenta.getSubCuentasIngreso(id, new LlamadaRemota<List<CuentaIngreso>>("No se puede obtener subcuentas", false) {
			@Override
			public void onSuccess(Method method, List<CuentaIngreso> response) {
				subCuentaListBox.clear();
				subCuentaListBox.addItem("Todos", "0");
				for (CuentaIngreso cuentaIngreso: response) {
					subCuentaListBox.addItem(cuentaIngreso.getNroCuenta() + " - " + cuentaIngreso.getDescripcion(), cuentaIngreso.getId() + "");
				}
			}
		});
	}
	@Override
	public void llenarUsuarios(List<Usuario> usuarios) {
		usuarioListBox.clear();
		usuarioListBox.addItem("Todos", "0");
		for (Usuario usuario : usuarios) {
//			String nombre = usuario.getNombres() == null ? "" : usuario.getNombres();
//			String apellidos = usuario.getApellidos() == null ? "" : usuario.getApellidos();
			usuarioListBox.addItem(usuario.getNombreUsuario(), usuario.getId() + "");
		}
		for(int i = 0; i< usuarioListBox.getItemCount(); i++) {
			if(usuarioListBox.getItemText(i).equals(adminParametros.getUsuario().getNombreUsuario())) {
				usuarioListBox.setSelectedIndex(i);
				break;
			}
		}
	}
	
	@Override
	public void setMovimientoAccion(MovimientoAccion movimientoAccion) {
		this.movimientoAccion = movimientoAccion;
	}
	
	
}
