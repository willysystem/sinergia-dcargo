//package com.sinergia.dcargo.client.local.view;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//import org.jboss.errai.ioc.client.api.AfterInitialization;
//
//import com.google.gwt.cell.client.FieldUpdater;
//import com.google.gwt.cell.client.SelectionCell;
//import com.google.gwt.cell.client.TextInputCell;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.dom.client.Style.Unit;
//import com.google.gwt.user.cellview.client.Column;
//import com.google.gwt.user.client.ui.IsWidget;
//import com.google.gwt.user.client.ui.Widget;
//import com.sinergia.dcargo.client.local.AdminParametros;
//import com.sinergia.dcargo.client.shared.Item;
//import com.sinergia.dcargo.client.shared.Precio;
//import com.sinergia.dcargo.client.shared.Unidad;
//
//@Singleton
//public class GridItem1 extends EditableCellView<Item> implements IsWidget {
//	
//	@Inject
//	private AdminParametros adminParametros;
//	
//	public GridItem1(){
//		super(10);
//		GWT.log(this.getClass().getSimpleName() + "()");
//		
//	}
//	
//	@PostConstruct
//	public void init() {
//		log.info("@PostConstruct: " + this.getClass().getSimpleName());
//	}
//	
//	@AfterInitialization
//	public void cargarDataUI() {
//		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
//		
//	}
//	
//	private void createGrid(){
//		
//		GWT.log("GridItem1.adminParametros.getUnidades(): " + adminParametros.getUnidades());
//		
//		defaultUI();
//		
//		// Bultos
//		Column<Item, String> bultosTextColumn = addColumn(new TextInputCell(), "Bultos", 
//				 new GetValue<Item, String>() {
//					@Override
//					public String getValue(Item item) {
//						if(item.getCantidad() == 0){
//							return "";
//						}
//						return item.getCantidad()+"";
//						
//					}
//			 }, new FieldUpdater<Item, String>() {
//				  	@Override
//				  	public void update(int index, Item item, String names) {
//				  		item.setCantidad(Integer.parseInt(names));
//				  		pendingChanges.add(item);
//				  	}
//		});
//		grid.setColumnWidth(bultosTextColumn, 10, Unit.PX);
//		
//		// Contenido
//		//new EditTextCell()
//		//TextInputCell text = new TextInputCell();
//		
//		Column<Item, String> contenidoTextColumn = addColumn(new TextInputCell(), "Contenido", 
//				 new GetValue<Item, String>() {
//					@Override
//					public String getValue(Item contact) {
//						return contact.getContenido();
//					}
//			 }, new FieldUpdater<Item, String>() {
//				  	@Override
//				  	public void update(int index, Item item, String atributo) {
//				  		item.setContenido(atributo);
//				  		pendingChanges.add(item);
//				  	}
//		});
//		grid.setColumnWidth(contenidoTextColumn, 40, Unit.PX);
//		
//		// Peso
//		Column<Item, String> pesoTextColumn = addColumn(new TextInputCell(), "Peso", 
//				 new GetValue<Item, String>() {
//					@Override
//					public String getValue(Item item) {
//						if(item.getPeso() == 0.0D){
//							return "";
//						}
//						return item.getPeso()+"";
//					}
//			 }, new FieldUpdater<Item, String>() {
//				  	@Override
//				  	public void update(int index, Item item, String value) {
//				  		GWT.log("   update: value: " + value );
//				  		item.setPeso(Double.parseDouble(value));
//				  		pendingChanges.add(item);
//				  	}
//		});
//		grid.setColumnWidth(pesoTextColumn, 10, Unit.PX);
//		
//		// Unidad
//		List<String> unidadesCadena = new ArrayList<String>();
//		unidadesCadena.add("");
//		final List<Unidad> unidades = adminParametros.getUnidades();  
//		for (Unidad unidad : unidades) {
//			unidadesCadena.add(unidad.getAbreviatura());
//		}
//		SelectionCell selectionCell = new SelectionCell(unidadesCadena);
//		Column<Item, String> sucursal = addColumn(selectionCell, "Unidad", new GetValue<Item, String>() {
//			@Override
//			public String getValue(Item item) {
//				if (item.getUnidad() != null) {
//					String name = item.getUnidad().getAbreviatura();
//					return name;
//				} else {
//					return "";
//				}
//			}
//		}, new FieldUpdater<Item, String>() {
//			@Override
//			public void update(int index, Item item, String value) {
//				for (Unidad unidad : unidades) {
//					if (unidad.getAbreviatura().equals(value)) {
//						if(value.equals("")){
//							item.setCantidad(null);
//							log.info("null");
//						} else {
//							item.setUnidad(unidad);
//						}
//						pendingChanges.add(item);
//						break;
//					}
//				}
//			}
//		});
//		grid.setColumnWidth(sucursal, 12, Unit.PX);
//
//		// Precio
//		List<String> preciosCadena = new ArrayList<String>();
//		preciosCadena.add("");
//		final List<Precio> precios = adminParametros.getPrecios();  
//		for (Precio precio : precios) {
//			preciosCadena.add(precio.getDescripcion());
//		}
//		SelectionCell selectionCellPrecio = new SelectionCell(preciosCadena);
//		Column<Item, String> precioColumn = addColumn(selectionCellPrecio, "Precio", new GetValue<Item, String>() {
//			@Override
//			public String getValue(Item item) {
//				if (item.getUnidad() != null) {
//					String name = item.getUnidad().getAbreviatura();
//					return name;
//				} else {
//					return "";
//				}
//			}
//		}, new FieldUpdater<Item, String>() {
//			@Override
//			public void update(int index, Item item, String value) {
//				for (Precio unidad : precios) {
//					if (unidad.getDescripcion().equals(value)) {
//						if(value.equals("")){
//							item.setCantidad(null);
//							log.info("null");
//						} else {
//							item.setPrecio(unidad);
//						}
//						pendingChanges.add(item);
//						break;
//					}
//				}
//			}
//		});
//		grid.setColumnWidth(precioColumn, 12, Unit.PX);
//		
//		// Total
//		//TextInputCell totalTextInputCell = new TextInputCell();
//		//totalTextInputCell.setValue(context, parent, value);
//		Column<Item, String> totalTextColumn = addColumn(new ValidatableTotalInputCell("Mensaje de error"), "Total", 
//				 new GetValue<Item, String>() {
//					@Override
//					public String getValue(Item item) {
//						if(item.getTotal() == 0.0D){
//							return "";
//						}
//						return item.getTotal()+"";
//					}
//			 }, new FieldUpdater<Item, String>() {
//				  	@Override
//				  	public void update(int index, Item item, String atributo) {
//				  		if(atributo != null) {
//				  			item.setTotal(Double.parseDouble(atributo.equals("")?"0.0":atributo));
//					  		pendingChanges.add(item);
//				  		}
//				  		
//				  	}
//		});
//		grid.setColumnWidth(totalTextColumn, 15, Unit.PX);
//		grid.setHeight("300px");
//		//grid.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
//	
//	}
//	
//	public void cargarDataUI(List<Item> item) {
//		dataProvider.getList().clear();
//		dataProvider.setList(item);
//	}
//	
//	public void addItem(Item item){
//		int nro = dataProvider.getList().size() + 1;
//		item.setId(2L);
//		item.setNro(nro);
//		item.setCantidad(0);
//		item.setContenido("");
//		item.setPeso(0D);
//		item.setTotal(0D);
//		dataProvider.getList().add(item);
//		grid.flush();
//	}
//	
//	public List<Item> getItems(){
//		return dataProvider.getList();
//	}
//	
//	@Override
//	protected Object getKeyItem(Item item) {
//		return item.getId();
//	}
//
//	@Override
//	protected String getNro(Item entity) {
//		return entity.getNro()+"";
//	}
//
//	@Override
//	public Widget asWidget() {
//		createGrid();
//		return grid;
//	}
//	
//}
