package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CssFloatLayoutContainer.CssFloatData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.grid.AggregationNumberSummaryRenderer;
import com.sencha.gxt.widget.core.client.grid.AggregationRowConfig;
import com.sencha.gxt.widget.core.client.grid.AggregationSafeHtmlRenderer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.api.ServicioItemCliente;
import com.sinergia.dcargo.client.local.message.MensajeExito;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Item;
import com.sinergia.dcargo.client.shared.dominio.Unidad;
import com.sencha.gxt.widget.core.client.grid.SummaryType.SumSummaryType;
@Singleton
public class GridItem3 /*extends AbstractGridEditingExample*/ implements IsWidget {

	@Inject private AdminParametros adminParametros;
	@Inject protected Logger log;
	@Inject private ServicioItemCliente servicioItem;
	@Inject private ServicioGuiaCliente servicioGuia;
	//@Inject private MensajeAviso mensajeAviso;
	//@Inject private StockEditor stockEditor;
	
	private VistaGuiaAccion vistaGuiaAccion;
	
	// Panel Editor
	private IntegerBox bultosBox = new IntegerBox();
	private TextBox    contenidoBox = new TextBox();
	private DoubleBox  pesoBox = new DoubleBox();
	private ListBox    unidadCombo  = new ListBox();
	private DoubleBox  precioBox = new DoubleBox();
	private DoubleBox  totalBox = new DoubleBox();
	
	private Button guardarButton = new Button("Guardar");
	TextButton borrarButton = new TextButton("Borrar Item");
	
	private CssFloatLayoutContainer floatContaniner;
	//private FramedPanel panelEditor;
	
	
	// Grid

	Item itemSeleccionado;
	Guia guiaSeleccionada;
	//GridEditing<Item> editing;
	DoubleField totalField = new DoubleField();
	DoubleField pesoField = new DoubleField();
	ListStore<Item> store = new ListStore<>(properties.id());
	
	private Grid<Item> grid;
	private FramedPanel panel;
	
	//private DoubleField precioDoubleField = new DoubleField();
	
	private boolean bultosValido    = false;
	private boolean contenidoValido = false;
	private boolean totalValido     = false;
	
	//private ColumnConfig<Item, String> eliminarColumn;

	private boolean isEditable = false;
	
	public interface PriceTemplate extends XTemplates {
		@XTemplate("<div style='text-align:right;padding:3px'>{value:currency}</div>")
		SafeHtml render(double value);
	}

	public GridItem3() {
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

		ValueProvider<Item, Double> precio();
		
		ValueProvider<Item, Double> total();
		
		ValueProvider<Item, String> eliminar();
		
	}
	
	@Override
	public Widget asWidget() {	
		
		guardarButton.setEnabled(false);
		
		if (panel != null) {
			floatContaniner.setVisible(isEditable);
			guardarButton.setVisible(isEditable);
			return panel;
		}
		
		// Editor
		addEscuchadores();
		
		List<Unidad> unidades = adminParametros.getUnidades();
		unidadCombo.addItem("", "0");
		for (Unidad unidad : unidades) {
			unidadCombo.addItem(unidad.getAbreviatura(), "" + unidad.getId());
		}
		guardarButton.setHeight("30px");
			
		floatContaniner = new CssFloatLayoutContainer();
		floatContaniner.add(new FieldLabel(bultosBox, "Bultos*"), new CssFloatData(0.1, new Margins(0, 7, 0, 0)));
		floatContaniner.add(new FieldLabel(contenidoBox, "Contenido*"), new CssFloatData(0.35, new Margins(0, 7, 0, 0)));
		floatContaniner.add(new FieldLabel(pesoBox, "Peso"), new CssFloatData(0.1, new Margins(0, 7, 0, 0)));
		floatContaniner.add(new FieldLabel(unidadCombo, "Unidad"), new CssFloatData(0.1, new Margins(0, 7, 0, 0)));
		floatContaniner.add(new FieldLabel(precioBox, "Precio"), new CssFloatData(0.1, new Margins(0, 7, 0, 0)));
		floatContaniner.add(new FieldLabel(totalBox, "Total*"), new CssFloatData(0.1, new Margins(0, 7, 0, 0)));
		floatContaniner.add(guardarButton, new CssFloatData(0.1, new Margins(0, 7, 0, 0)));
		
			
		RowNumberer<Item> numberedColumn = new RowNumberer<>();
		numberedColumn.setWidth(15);
		ColumnConfig<Item, Integer> bultosColumn = new ColumnConfig<>(properties.cantidad(), 50, "Bultos");
		ColumnConfig<Item, String> contenidoColumn = new ColumnConfig<>(properties.contenido(), 350, "Contenido");
		ColumnConfig<Item, Double> pesoColumn = new ColumnConfig<>(properties.peso(), 50, "Peso");
		ColumnConfig<Item, String> unidadColumn = new ColumnConfig<>(properties.unidadTitulo(), 65, "Unidad");
		ColumnConfig<Item, Double> precioColumn = new ColumnConfig<>(properties.precio(), 75, "Precio");
		ColumnConfig<Item, Double> totalColumn = new ColumnConfig<>(properties.total(), 75, "Total");
		ColumnConfig<Item, String> eliminarColumn = new ColumnConfig<>(properties.eliminar(), 180, "");
		
		TextButtonCell buttonCellEliminar = new TextButtonCell();
		buttonCellEliminar.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Context c = event.getContext();
				int row = c.getIndex();
				Item item = store.get(row);
				log.info("  item" + item);
				itemSeleccionado = item;
				eliminarItemSeleccionado();
				//log.info(""+event.getSource());
			}
		});
		eliminarColumn.setCell(buttonCellEliminar);
		

		List<ColumnConfig<Item, ?>> colmuns = new ArrayList<>();
		colmuns.add(numberedColumn);
		colmuns.add(bultosColumn);
		colmuns.add(contenidoColumn);
		colmuns.add(pesoColumn);
		colmuns.add(unidadColumn);
		colmuns.add(precioColumn);
		colmuns.add(totalColumn);
		if(isEditable)
			colmuns.add(eliminarColumn);

		ColumnModel<Item> columns = new ColumnModel<>(colmuns);
		
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
		
		GWT.log("guiaSeleccionada.getItems(): " + guiaSeleccionada.getItems());
		
		
		
		
		GridSelectionModel<Item> selecctionModel = new GridSelectionModel<>();
		
		grid = new Grid<>(store, columns);
		grid.setSelectionModel(selecctionModel);
		grid.setHeight(140);
		grid.getView().setAutoExpandColumn(contenidoColumn);
		grid.addRowClickHandler(e -> selecionarItem());		
		numberedColumn.initPlugin(grid);
			
		VerticalPanel container = new VerticalPanel();
		container.add(floatContaniner); //, new VerticalLayoutData(1, -1, new Margins(15, 15, 0, 15)));
		container.add(grid); //, new VerticalLayoutData(1, -1, new Margins(0, 15, 0, 15)));
	    panel = new FramedPanel();
	    panel.setHeaderVisible(false);
	    panel.add(container);
		    
	    List<FieldLabel> fieldLabels = FormPanelHelper.getFieldLabels(panel);
	    for (FieldLabel fieldLabel : fieldLabels) {
	      fieldLabel.setLabelAlign(LabelAlign.TOP);
	    }
	    panel.setWidth(1000);
	    panel.setBorders(false);
	    panel.setStyleName("panelItems");
	    
		return panel;
	}
	

	private void addEscuchadores() {
		bultosBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(bultosBox.getValue() != null) bultosValido = true;
				else {
					bultosValido = false;
				}
				validarFila();
			}
		});
		contenidoBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(contenidoBox.getValue() != null && !contenidoBox.getValue().equals("")) contenidoValido = true;
				else {
					contenidoValido = false;
				}
				validarFila();
			}
		});
		totalBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if(totalBox.getValue() != null) totalValido = true;
				else {
					totalValido = false;
				}
				validarFila();
			}
		});
		pesoBox.addChangeHandler(e -> calcularTotal());
		precioBox.addChangeHandler(e -> calcularTotal());
		guardarButton.addClickHandler(e -> {
			if(itemSeleccionado == null) {
				vistaGuiaAccion.fijarEstadoGuiaEspera();
				servicioItem.nuevoItem(guiaSeleccionada.getId(), new LlamadaRemota<Item>("Error la crear Item", true) {
					@Override
					public void onSuccess(Method method, Item response) {						
						actualizarItem(response);
					}
				});
			} else {
				actualizarItem(itemSeleccionado);
			}
		});
		
		borrarButton.addSelectHandler(e-> {
			 vistaGuiaAccion.fijarEstadoGuiaEspera(); 	
			 servicioItem.borrar(itemSeleccionado.getId(), new LlamadaRemota<Void>("No se puede borrar item", true) {
				@Override
				public void onSuccess(Method method, Void response) {
					store.remove(itemSeleccionado);
					new MensajeExito("Item borrado exitosamente").show();
					//cargador.hide();
					vistaGuiaAccion.fijarEstadoGuiaCargado();
					
					
					// Validar si existe almenos un registro
					if(store.size() > 0 ) vistaGuiaAccion.setAlmenosUnRegistro(true);
					else vistaGuiaAccion.setAlmenosUnRegistro(false);
					vistaGuiaAccion.validarParaRemitir();
					
					guiaSeleccionada.getItems().remove(itemSeleccionado);
				}
			  });
			}
		);
	}
	
	protected GridEditing<Item> createGridEditing(Grid<Item> editableGrid) {
		GridRowEditing<Item> rowEditing = new GridRowEditing<>(editableGrid);
		
		rowEditing.getSaveButton().addSelectHandler(e -> {
			log.info("->");
		});

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
	
	Item itemTemp;
	private void actualizarItem(Item item) {
		
		log.info("actualizando store.findModel(item): " + store.findModel(item));
		itemTemp = null;
		if(store.findModel(item) == null) {
			itemTemp = new Item();
			itemTemp.setId(item.getId());
		} else {
			itemTemp = store.findModel(item);
		}
			
		itemTemp.setCantidad(bultosBox.getValue());
		itemTemp.setContenido(contenidoBox.getValue());
		itemTemp.setPeso(pesoBox.getValue());
		
		log.info("unidadCombo.getSelectedValue(): " + unidadCombo.getSelectedValue());
		String idString = unidadCombo.getSelectedValue();
		Long idUnidad = null;
		if(!idString.equals("0")) {
			idUnidad = Long.parseLong(idString);
			Unidad unidadTemp = new Unidad();
			unidadTemp.setId(idUnidad);
			itemTemp.setUnidad(unidadTemp);
			
		}
			
		itemTemp.setPrecio(precioBox.getValue());
		itemTemp.setTotal(totalBox.getValue());
		
		if(store.findModel(item) == null) {
			itemTemp.setEliminar("Eliminar");
			store.add(store.size(), itemTemp);
		} 
		
		// Resumen y total
		String resumen = "";
		Double total = 0.0D;
		Double pesoTotal = 0.0D;
		Integer bultosTotal = 0;

		
		for (Item i: store.getAll()) {
			resumen = resumen + i.getCantidad() + " " + i.getContenido() + ",";
			total = total + (i.getTotal() == null ? 0.0 : i.getTotal());
			pesoTotal = pesoTotal + (i.getPeso()==null?0:i.getPeso());
			bultosTotal = bultosTotal + (i.getCantidad() == null ? 0 : i.getCantidad());
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
				servicioGuia.guardartotal(guiaSeleccionada.getId(), totalTemp, new LlamadaRemota<Void>("No se pudo guardar Total", false){
					@Override
					public void onSuccess(Method method, Void response) {
						servicioGuia.guardarPesoTotal(guiaSeleccionada.getId(), pesoTotalTemp, new LlamadaRemota<Void>("No se pudo guardar peso Total", false){
							@Override
							public void onSuccess(Method method, Void response) {
								servicioGuia.guardarBultosTotal(guiaSeleccionada.getId(), bultosTotalTemp, new LlamadaRemota<Void>("No se pudo guardar bultos Total", false){
									@Override
									public void onSuccess(Method method, Void response) {
										log.info("");
										
										if(itemTemp.getUnidad() != null)
											for (Unidad u: adminParametros.getUnidades()) {
												if(u.getId() == itemTemp.getUnidad().getId()) {
													itemTemp.setUnidadTitulo(u.getAbreviatura());
													break;
												}
											}
										
										guiaSeleccionada.setResumenContenido(resumenTemp);
										guiaSeleccionada.setTotalGuia(totalTemp);
										guiaSeleccionada.setTotalPeso(pesoTotalTemp);
										guiaSeleccionada.setTotalCantidad(bultosTotalTemp);
										guiaSeleccionada.getItems().add(itemTemp);
										
										vistaGuiaAccion.setTotalGuia(totalTemp);
										log.info("   totalTemp: " + totalTemp);
										
//										if(itemSeleccionado != null) {
//											store.remove(itemSeleccionado);
//											itemSeleccionado = null;
//										}
										
										store.commitChanges();
										store.update(itemTemp);
										
										limpiarCampos();
										
										grid.getSelectionModel().deselectAll();
										
										// Validar si existe almenos un registro
										if(store.size() > 0 ) vistaGuiaAccion.setAlmenosUnRegistro(true);
										else vistaGuiaAccion.setAlmenosUnRegistro(false);
										vistaGuiaAccion.validarParaRemitir();
										
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
	
	private boolean validarFila(){
		log.info("-- Validaciones");
		log.info("bultosValido: " + bultosValido);
		log.info("contenidoValido: " + contenidoValido);
		log.info("totalValido: " + totalValido);
		
		if(bultosValido && contenidoValido && totalValido){
			guardarButton.setEnabled(true);
			return true;
		} else {
			guardarButton.setEnabled(false);
			return false;
		}
			 
	}
	
	private void calcularTotal() {
		
		Double precio = precioBox.getValue();
		if(precio == null) precio = 0.0;
		Double peso = pesoBox.getValue();
		if(peso == null) peso = 0.0;
		Double total = precio * peso;
		totalBox.setValue(total);
		
		totalValido = true;
		validarFila();
		
	}
	
	public void setEnable(boolean enabled) {
		grid.setEnabled(enabled);
	}
	
	public void limpiarCampos() {
		if(itemSeleccionado != null) itemSeleccionado.setId(null);
		bultosBox.setValue(null);
		contenidoBox.setValue(null);
		pesoBox.setValue(null);
		unidadCombo.setSelectedIndex(0);
		precioBox.setValue(null);
		totalBox.setValue(null);
		
	}
	
	private void selecionarItem() {
		itemSeleccionado = grid.getSelectionModel().getSelectedItem();
		bultosBox.setValue(itemSeleccionado.getCantidad());
		contenidoBox.setValue(itemSeleccionado.getContenido());
		pesoBox.setValue(itemSeleccionado.getPeso());
		
		if(itemSeleccionado.getUnidad() != null) {
			boolean noEncontrado = false;
			int i = 0;
			while (!noEncontrado) {
				String unidad = unidadCombo.getItemText(i);
				for (Unidad u: adminParametros.getUnidades()) {
					if(u.getAbreviatura().equals(unidad)) {
						unidadCombo.setSelectedIndex(i);
						noEncontrado = true;
						break;
					}
				}
				i++;
			}
		}
		precioBox.setValue(itemSeleccionado.getPrecio());
		totalBox.setValue(itemSeleccionado.getTotal());
		
	}
	
	private void eliminarItemSeleccionado() {
		 vistaGuiaAccion.fijarEstadoGuiaEspera(); 	
		 servicioItem.borrar(itemSeleccionado.getId(), new LlamadaRemota<Void>("No se puede borrar item", true) {
			@Override
			public void onSuccess(Method method, Void response) {
				store.remove(itemSeleccionado);
				new MensajeExito("Item borrado exitosamente").show();
				//cargador.hide();
				
				guiaSeleccionada.getItems().remove(itemSeleccionado);
				
				grid.getSelectionModel().deselectAll();
				itemSeleccionado = null;
				limpiarCampos();
				
				
				
				vistaGuiaAccion.fijarEstadoGuiaCargado();
				
			}
		  });
	}
	
	public void setVisibleEditorGrid(boolean visible) {
		isEditable = visible;
		//floatContaniner.setVisible(visible);
//		if(visible) {
//			grid.getColumnModel().getColumns().add(eliminarColumn);
//		} else {
//			grid.getColumnModel().getColumns().remove(eliminarColumn);
//		}
	}
	
}
