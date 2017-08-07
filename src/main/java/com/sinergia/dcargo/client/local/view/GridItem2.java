package com.sinergia.dcargo.client.local.view;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.logging.client.DefaultLevel.Info;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.Converter;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.AggregationNumberSummaryRenderer;
import com.sencha.gxt.widget.core.client.grid.AggregationRowConfig;
import com.sencha.gxt.widget.core.client.grid.AggregationSafeHtmlRenderer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.LlamadaRemotaVacia;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.api.ServicioItemCliente;
import com.sinergia.dcargo.client.local.message.MensajeAviso;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Item;
import com.sinergia.dcargo.client.shared.Precio;
import com.sinergia.dcargo.client.shared.ServicioGuia;
import com.sinergia.dcargo.client.shared.Unidad;

import com.sencha.gxt.widget.core.client.grid.SummaryType.SumSummaryType;
@Singleton
public class GridItem2 /*extends AbstractGridEditingExample*/ implements IsWidget {

	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	protected Logger log;
	
	@Inject
	private ServicioItemCliente servicioItem;
	
	@Inject
	private ServicioGuiaCliente servicioGuia;
	
	@Inject
	private MensajeAviso mensajeAviso;
	
//	@Inject
//	private Cargador cargador;

	private VistaGuiaAccion vistaGuiaAccion;

	Item itemSeleccionado;
	Guia guiaSeleccionada;
	GridEditing<Item> editing;
	DoubleField totalField = new DoubleField();
	DoubleField pesoField = new DoubleField();
	ListStore<Item> store = new ListStore<>(properties.id());;
	
	private List<Unidad> unidades;
	private List<Precio> precios;
	
	private Grid<Item> grid;
	private ContentPanel panel;
	
	//private Widget widget;

	public interface PriceTemplate extends XTemplates {
		@XTemplate("<div style='text-align:right;padding:3px'>{value:currency}</div>")
		SafeHtml render(double value);
	}

	public GridItem2() {
		GWT.log(this.getClass().getSimpleName() + "()");
	}

	@PostConstruct
	public void init() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}

	@AfterInitialization
	public void cargarDataUI() {
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());

	}
	
	private static final PlaceProperties properties = GWT.create(PlaceProperties.class);
	interface PlaceProperties extends PropertyAccess<Item> {

		@Path("id")
		ModelKeyProvider<Item> id();
		
		ValueProvider<Item, Integer> cantidad();

		ValueProvider<Item, String> contenido();

		ValueProvider<Item, Double> peso();

		ValueProvider<Item, String> unidadTitulo();

		ValueProvider<Item, String> precioMonto();
		
		ValueProvider<Item, Double> total();
		
	}
	
	@Override
	public Widget asWidget() {
		GWT.log("GridItem2.adminParametros.getUnidades(): " + adminParametros.getUnidades());
//		if (widget == null) {
//			widget = super.asWidget();
//			customize();
//		}
//		return widget;
		
		unidades = adminParametros.getUnidades();
		precios = adminParametros.getPrecios();
		
		if (panel == null) {
			RowNumberer<Item> numberedColumn = new RowNumberer<>();
			ColumnConfig<Item, Integer> bultosColumn = new ColumnConfig<>(properties.cantidad(), 50, "Bultos");
			ColumnConfig<Item, String> contenidoColumn = new ColumnConfig<>(properties.contenido(), 350, "Contenido");
			ColumnConfig<Item, Double> pesoColumn = new ColumnConfig<>(properties.peso(), 50, "Peso");
			ColumnConfig<Item, String> unidadColumn = new ColumnConfig<>(properties.unidadTitulo(), 65, "Unidad");
			ColumnConfig<Item, String> precioColumn = new ColumnConfig<>(properties.precioMonto(), 75, "Precio");
			ColumnConfig<Item, Double> totalColumn = new ColumnConfig<>(properties.total(), 75, "Total");

//			priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
//			priceColumn.setCell(new SimpleSafeHtmlCell<Double>(new AbstractSafeHtmlRenderer<Double>() {
//				@Override
//				public SafeHtml render(Double object) {
//					return SafeHtmlUtils.fromString(NumberFormat.getCurrencyFormat().format(object));
//				}
//			}));

			List<ColumnConfig<Item, ?>> l = new ArrayList<>();
			l.add(numberedColumn);
			l.add(bultosColumn);
			l.add(contenidoColumn);
			l.add(pesoColumn);
			l.add(unidadColumn);
			l.add(precioColumn);
			l.add(totalColumn);

			ColumnModel<Item> columns = new ColumnModel<>(l);
			

			
			GWT.log("guiaSeleccionada.getItems(): " + guiaSeleccionada.getItems());
			grid = new Grid<>(store, columns);
			//grid.getView().setAutoExpandColumn(contenidoColumn);
			numberedColumn.initPlugin(grid);
			
			// Sumatoria
			final NumberFormat bultosFormat = NumberFormat.getFormat("0");
			final NumberFormat pesoTotalFormat = NumberFormat.getFormat("0.00");
			
			AggregationRowConfig<Item> totales = new AggregationRowConfig<>();
			totales.setRenderer(bultosColumn, new AggregationNumberSummaryRenderer<Item, Number>(bultosFormat, new SumSummaryType<Number>()));
			totales.setRenderer(contenidoColumn, new AggregationSafeHtmlRenderer<>(new SafeHtml() {
				private static final long serialVersionUID = 111111111L;
				@Override
				public String asString() {
					return "<-- Total Bultos&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Total Peso -->";
				}
			}));
			totales.setRenderer(pesoColumn, new AggregationNumberSummaryRenderer<Item, Number>(pesoTotalFormat, new SumSummaryType<Number>()));
			totales.setRenderer(precioColumn, new AggregationSafeHtmlRenderer<>("Total"));
			totales.setRenderer(totalColumn, new AggregationNumberSummaryRenderer<Item, Number>(pesoTotalFormat, new SumSummaryType<Number>()));
			columns.addAggregationRow(totales);
			
			//totales.setRenderer(bultosColumn, new AggregationNum);
			
			// Unidad
			SimpleComboBox<Unidad> unidadCombo = new SimpleComboBox<>(new StringLabelProvider<Unidad>(){
				@Override
				  public String getLabel(Unidad item) {
				    return item.getAbreviatura();
				  }
			});
			unidadCombo.setClearValueOnParseError(false);
			unidadCombo.addSelectionHandler(new SelectionHandler<Unidad>() {
				@Override
				public void onSelection(SelectionEvent<Unidad> event) {
					Unidad unidad = event.getSelectedItem();
					GWT.log("event.getSelectedItem(): unidad: " + unidad);
					itemSeleccionado.setUnidad(unidad);
				}
			});
			unidadCombo.setPropertyEditor(new PropertyEditor<Unidad>() {
				@Override
				public Unidad parse(CharSequence text) throws ParseException {
					for (Unidad unidad: unidades) {
						if(text.equals(unidad.getAbreviatura())) return unidad; 
					}
					return null;
				}

				@Override
				public String render(Unidad unidad) {
					return unidad.getAbreviatura();
				}
			});
			unidadCombo.setTriggerAction(TriggerAction.ALL);
			unidadCombo.add(unidades);
			
			Converter<String, Unidad> unidadConverter = new Converter<String, Unidad>() {
				@Override
				public String convertFieldValue(Unidad object) {
					if(object == null) {
						return "";
					} else {
						if(object.getAbreviatura() == null) return "";
						else return object.getAbreviatura();
					}
					
				}
				@Override
				public Unidad convertModelValue(String object) {
					for (Unidad unidad: unidades) {
						if(object.equals(unidad.getAbreviatura())) return unidad; 
					}
					return null;
				}
			};
			
			// Precio
			SimpleComboBox<Precio> precioCombo = new SimpleComboBox<>(new StringLabelProvider<Precio>(){
				@Override
				  public String getLabel(Precio item) {
				    return item.getDescripcion();
				  }
			});
			precioCombo.setClearValueOnParseError(false);
			precioCombo.addSelectionHandler(e->{
				Precio precio = e.getSelectedItem();
				GWT.log("event.getSelectedItem(): precio: " + precio);
				Double precioD = precio.getPrecio();
				Double pesoD = pesoField.getValue();
				Double totalD = precioD*pesoD;
				totalField.setValue(totalD);
				
				GWT.log("event.getSelectedItem(): precio: " + precio);
				itemSeleccionado.setPrecio(precio);
			});
			precioCombo.setPropertyEditor(new PropertyEditor<Precio>() {
				@Override
				public Precio parse(CharSequence text) throws ParseException {
					for (Precio unidad: precios) {
						if(text.equals(unidad.getDescripcion())) return unidad; 
					}
					return null;
				}

				@Override
				public String render(Precio unidad) {
					return unidad.getDescripcion();
				}
			});
			precioCombo.setTriggerAction(TriggerAction.ALL);
			precioCombo.add(precios);
			
			Converter<String, Precio> precioConverter = new Converter<String, Precio>() {
				@Override
				public String convertFieldValue(Precio object) {
					if(object == null){
						return "";
					} else {
						if(object.getDescripcion() == null) return "";
						else return object.getDescripcion();
					}
				}
				@Override
				public Precio convertModelValue(String object) {
					for (Precio unidad: precios) {
						if(object.equals(unidad.getDescripcion())) return unidad; 
					}
					return null;
				}
			};

			IntegerField bultosIntegerField = new IntegerField();
//			bultosIntegerField.addKeyDownHandler(e->{
//				log.info("addKeyDownHandler: " + bultosIntegerField.getValue() + "-->" + e.getNativeEvent().getKeyCode());
//				if(e.getNativeEvent().getKeyCode() == 13){
//					store.commitChanges();
//					editing.completeEditing();
//				}
//			});
			TextField contenidoTextField = new TextField();
//			contenidoTextField.addKeyDownHandler(e->{
//				log.info("contenidoTextField.addKeyDownHandler: " + contenidoTextField.getValue() + "-->" + e.getNativeEvent().getKeyCode());
//				if(e.getNativeEvent().getKeyCode() == 13){
//					store.commitChanges();
//					editing.completeEditing();
//				}
//			});
//			totalField.addKeyDownHandler(e->{
//				log.info("totalField.addKeyDownHandler: " + totalField.getValue() + "-->" + e.getNativeEvent().getKeyCode());
//				if(e.getNativeEvent().getKeyCode() == 13){
//					store.commitChanges();
//					editing.completeEditing();
//				}
//			});
//			totalField.addValueChangeHandler(e ->{
//				log.info("totalField.addValueChangeHandler: " + totalField.getValue());
//				store.commitChanges();
//				editing.completeEditing();
//			});
			
			editing = createGridEditing(grid);
			editing.addEditor(bultosColumn, bultosIntegerField);
			editing.addEditor(contenidoColumn, contenidoTextField);
			editing.addEditor(pesoColumn, pesoField);
			editing.addEditor(unidadColumn, unidadConverter, unidadCombo);
			editing.addEditor(precioColumn, precioConverter, precioCombo);
			
			editing.addEditor(totalColumn, totalField);
		
			editing.addCancelEditHandler(e -> store.rejectChanges());
			editing.addCompleteEditHandler( e -> {
				guardarItem();
			});
			
			// column 5 is not editable

			// EDITING //
			//customizeGrid(grid);

			TextButton addButton = new TextButton("Nuevo Item");
			addButton.addSelectHandler(e -> {
				vistaGuiaAccion.fijarEstadoGuiaEspera();
				servicioItem.nuevoItem(guiaSeleccionada.getId(), new LlamadaRemota<Item>("Error la crear Item", true) {
					@Override
					public void onSuccess(Method method, Item response) {
						//cargador.hide();
						itemSeleccionado = response;
						itemSeleccionado.setUnidadTitulo("");
						itemSeleccionado.setPrecioMonto("");
						
						editing.cancelEditing();
						store.add(store.size(), itemSeleccionado);

						int row = store.indexOf(itemSeleccionado);
						editing.startEditing(new GridCell(row, 0));
						
						vistaGuiaAccion.fijarEstadoGuiaCargado();
						
						guiaSeleccionada.getItems().add(itemSeleccionado);
					}
				});
			 }
			);
			
			TextButton borrarButton = new TextButton("Borrar Item");
			borrarButton.addSelectHandler(e-> {
			 vistaGuiaAccion.fijarEstadoGuiaEspera(); 	
			 servicioItem.borrar(itemSeleccionado.getId(), new LlamadaRemota<Void>("No se puede borrar item", true) {
				@Override
				public void onSuccess(Method method, Void response) {
					store.remove(itemSeleccionado);
					new MensajeExito("Item borrado exitosamente").show();
					//cargador.hide();
					vistaGuiaAccion.fijarEstadoGuiaCargado();
					
					guiaSeleccionada.getItems().remove(itemSeleccionado);
				}
			  });
			}
			);


			ToolBar toolBar = new ToolBar();
			toolBar.add(addButton);
			toolBar.add(borrarButton);

			VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
			verticalLayoutContainer.add(toolBar, new VerticalLayoutData(1, -1));
			verticalLayoutContainer.add(grid, new VerticalLayoutData(1, 1));

			panel = new ContentPanel();
			panel.setHeading("Abstract Grid Editing");
			panel.add(verticalLayoutContainer);

			panel.setButtonAlign(BoxLayoutPack.CENTER);

		}
		customize();
		return panel;
	}

	protected void customize() {
		panel.setHeading("Row Editable Grid");
		panel.setHeaderVisible(false);
		grid.setHeight(150);
		// grid.setWidth("100%");
	}
	
	protected GridEditing<Item> createGridEditing(Grid<Item> editableGrid) {
		GridRowEditing<Item> rowEditing = new GridRowEditing<>(editableGrid);
		
		rowEditing.getSaveButton().addSelectHandler(e -> {
			log.info("->");
		});
//		ColumnConfig<Item, Double> price = editableGrid.getColumnModel().getColumn(2);
//		rowEditing.addRenderer(price, new AbstractSafeHtmlRenderer<Double>() {
//			PriceTemplate template = GWT.create(PriceTemplate.class);
//
//			@Override
//			public SafeHtml render(Double object) {
//				return template.render(object);
//			}
//		});

		return rowEditing;
	}

	public List<Item> getItems(){
		return store.getAll();
	}

	public void setGuiaSeleccionada(Guia guiaSeleccionada) {
		this.guiaSeleccionada = guiaSeleccionada;
		this.store.clear();
		this.store.addAll(this.guiaSeleccionada.getItems());
	}

	public void setVistaGuiaAccion(VistaGuiaAccion vistaGuiaAccion) {
		this.vistaGuiaAccion = vistaGuiaAccion;
	}
	
	private void guardarItem() {
		
		store.commitChanges();
		Item itemSelected = grid.getSelectionModel().getSelectedItem();
		GWT.log("item: " + itemSelected);
		if(itemSelected == null){
			itemSelected = itemSeleccionado;
		}
		
		GWT.log("itemSeleccionado: " + itemSeleccionado);
		Item itemTemp = new Item();
		itemTemp.setId(itemSelected.getId());
		itemTemp.setCantidad(itemSelected.getCantidad());
		itemTemp.setContenido(itemSelected.getContenido());
		itemTemp.setPeso(itemSelected.getPeso());
		if(itemSelected.getUnidad() != null){
			Unidad unidadTemp = new Unidad();
			unidadTemp.setId(itemSelected.getUnidad().getId());
			itemTemp.setUnidad(unidadTemp);
		}
		if(itemSelected.getPrecio() != null) {
			Precio precioTemp = new Precio();
			precioTemp.setId(itemSelected.getPrecio().getId());
			itemTemp.setPrecio(precioTemp);
		}
		itemTemp.setTotal(itemSelected.getTotal());
		
		boolean valido = validarFila(itemTemp);
		if(!valido){
			mensajeAviso.mostrar("Los campos Bultos, Contenido y Total son obligatorios");
			return ;
		}
		
		// Resumen y total
		String resumen = "";
		Double total = 0.0D;
		Double pesoTotal = 0.0D;
		Integer bultosTotal = 0;
		for (Item i: store.getAll()) {
			resumen = resumen + i.getContenido() + ",";
			total = total + i.getTotal();
			pesoTotal = pesoTotal + (i.getPeso()==null?0:i.getPeso());
			bultosTotal = bultosTotal + i.getCantidad();
		}
		resumen = resumen.substring(0, resumen.length()-1);
		vistaGuiaAccion.setResumen(resumen);
		
		final Double totalTemp = total;
		final String resumenTemp = resumen;
		final Double pesoTotalTemp = pesoTotal;
		final Integer bultosTotalTemp = bultosTotal;
		
		vistaGuiaAccion.fijarEstadoGuiaEspera();
		servicioItem.guardar(itemTemp, new LlamadaRemota<Void>("No se guardo Item", false){
			@Override
			public void onSuccess(Method method, Void response) {
				servicioGuia.guardarTotal(guiaSeleccionada.getId(), totalTemp, new LlamadaRemota<Void>("No se pudo guardar Total", false){
					@Override
					public void onSuccess(Method method, Void response) {
						servicioGuia.guardarPesoTotal(guiaSeleccionada.getId(), pesoTotalTemp, new LlamadaRemota<Void>("No se pudo guardar peso Total", false){
							@Override
							public void onSuccess(Method method, Void response) {
								servicioGuia.guardarBultosTotal(guiaSeleccionada.getId(), bultosTotalTemp, new LlamadaRemota<Void>("No se pudo guardar bultos Total", false){
									@Override
									public void onSuccess(Method method, Void response) {
										guiaSeleccionada.setResumenContenido(resumenTemp);
										guiaSeleccionada.setTotalGuia(totalTemp);
										guiaSeleccionada.setTotalPeso(pesoTotalTemp);
										guiaSeleccionada.setTotalCantidad(bultosTotalTemp);
										vistaGuiaAccion.fijarEstadoGuiaCargado();
									}
								});											
							}
						});
					}
				});
			}
		});
	}
	
	private boolean validarFila(Item item){
		if(item.getCantidad() == null) return false;
		if(item.getContenido() == null) return false;
		if(item.getTotal() == null) return false;
		return true;
	}
	
}
