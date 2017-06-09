//
//package com.sinergia.dcargo.client.shared;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//
//import com.gargoylesoftware.htmlunit.javascript.host.dom.Text;
//import com.google.gwt.cell.client.DateCell;
//import com.google.gwt.core.client.GWT;
//import com.google.gwt.editor.client.Editor.Path;
//import com.google.gwt.i18n.client.DateTimeFormat;
//import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
//import com.google.gwt.i18n.client.NumberFormat;
//import com.google.gwt.safehtml.shared.SafeHtml;
//import com.google.gwt.safehtml.shared.SafeHtmlUtils;
//import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
//import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.IsWidget;
//import com.google.gwt.user.client.ui.Widget;
//import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
//import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
//import com.sencha.gxt.core.client.ValueProvider;
//import com.sencha.gxt.core.client.util.DateWrapper;
//import com.sencha.gxt.data.shared.Converter;
//import com.sencha.gxt.data.shared.ListStore;
//import com.sencha.gxt.data.shared.ModelKeyProvider;
//import com.sencha.gxt.data.shared.PropertyAccess;
//import com.sencha.gxt.data.shared.StringLabelProvider;
//import com.sencha.gxt.widget.core.client.ContentPanel;
//import com.sencha.gxt.widget.core.client.button.TextButton;
//import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
//import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
//import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
//import com.sencha.gxt.widget.core.client.event.SelectEvent;
//import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
//import com.sencha.gxt.widget.core.client.form.CheckBox;
//import com.sencha.gxt.widget.core.client.form.DateField;
//import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
//import com.sencha.gxt.widget.core.client.form.DoubleField;
//import com.sencha.gxt.widget.core.client.form.FloatField;
//import com.sencha.gxt.widget.core.client.form.IntegerField;
//import com.sencha.gxt.widget.core.client.form.PropertyEditor;
//import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
//import com.sencha.gxt.widget.core.client.form.TextField;
//import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
//import com.sencha.gxt.widget.core.client.grid.ColumnModel;
//import com.sencha.gxt.widget.core.client.grid.Grid;
//import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
//import com.sencha.gxt.widget.core.client.grid.RowNumberer;
//import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
//import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
//import com.sinergia.dcargo.client.local.AdminParametros;
//
//@Singleton
//public abstract class AbstractGridEditingExample implements IsWidget {
//
//	@Inject
//	private AdminParametros adminParametros;
//	
//	private List<Unidad> unidades;
//	// just to show the converter feature
//	enum Light {
//		MOSTLYSHADY("Mostly Shady"), MOSTLYSUNNY("Mostly Sunny"), SHADE("Shade"), SUNNY("Sunny"), SUNORSHADE(
//				"Sun or Shade");
//		static Light parseString(String object) throws ParseException {
//			if (Light.MOSTLYSUNNY.toString().equals(object)) {
//				return Light.MOSTLYSUNNY;
//			} else if (Light.SUNORSHADE.toString().equals(object)) {
//				return Light.SUNORSHADE;
//			} else if (Light.MOSTLYSHADY.toString().equals(object)) {
//				return Light.MOSTLYSHADY;
//			} else if (Light.SHADE.toString().equals(object)) {
//				return Light.SHADE;
//			} else if (Light.SUNNY.toString().equals(object)) {
//				return Light.SUNNY;
//			} else {
//				throw new ParseException(object.toString() + " could not be parsed", 0);
//			}
//		}
//
//		private String text;
//
//		Light(String text) {
//			this.text = text;
//		}
//
//		@Override
//		public String toString() {
//			return text;
//		}
//	}
//
//	interface PlaceProperties extends PropertyAccess<Item> {
//
//		@Path("id")
//		ModelKeyProvider<Item> id();
//		
//		ValueProvider<Item, Integer> cantidad();
//
//		ValueProvider<Item, String> contenido();
//
//		ValueProvider<Item, Double> peso();
//
//		//ValueProvider<Item, String> unidad();
//
//		//ValueProvider<Item, Double> precio();
//		
//		ValueProvider<Item, Double> total();
//	}
//
//	private static final PlaceProperties properties = GWT.create(PlaceProperties.class);
//
//	protected Grid<Item> grid;
//
//	protected ContentPanel panel;
//
//	@Override
//	public Widget asWidget() {
//		
//		GWT.log("AbstractGridEditingExample.adminParametros.getUnidades(): " + adminParametros.getUnidades());
//		unidades = adminParametros.getUnidades();
//		
//		if (panel == null) {
//			RowNumberer<Item> numberedColumn = new RowNumberer<>();
//			ColumnConfig<Item, Integer> bultosColumn = new ColumnConfig<>(properties.cantidad(), 50, "Bultos");
//			ColumnConfig<Item, String> contenidoColumn = new ColumnConfig<>(properties.contenido(), 150, "Contenido");
//			ColumnConfig<Item, Double> pesoColumn = new ColumnConfig<>(properties.peso(), 50, "Peso");
//			//ColumnConfig<Item, String> unidadColumn = new ColumnConfig<>(properties.unidad(), 65, "Unidad");
//			//ColumnConfig<Item, Double> priceColumn = new ColumnConfig<>(properties.precio(), 75, "Precio");
//			ColumnConfig<Item, Double> totalColumn = new ColumnConfig<>(properties.total(), 75, "Total");
//
////			priceColumn.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
////			priceColumn.setCell(new SimpleSafeHtmlCell<Double>(new AbstractSafeHtmlRenderer<Double>() {
////				@Override
////				public SafeHtml render(Double object) {
////					return SafeHtmlUtils.fromString(NumberFormat.getCurrencyFormat().format(object));
////				}
////			}));
//
//			List<ColumnConfig<Item, ?>> l = new ArrayList<>();
//			l.add(numberedColumn);
//			l.add(bultosColumn);
//			l.add(contenidoColumn);
//			//l.add(priceColumn);
//			l.add(pesoColumn);
//			//l.add(unidadColumn);
//			l.add(totalColumn);
//
//			ColumnModel<Item> columns = new ColumnModel<>(l);
//
//			final ListStore<Item> store = new ListStore<>(properties.id());
//			// store.addAll(TestData.getPlants());
//
//			grid = new Grid<>(store, columns);
//			grid.getView().setAutoExpandColumn(bultosColumn);
//
//			// EDITING //
//			SimpleComboBox<Unidad> lightCombo = new SimpleComboBox<>(new StringLabelProvider<Unidad>());
//			lightCombo.setClearValueOnParseError(false);
//			lightCombo.setPropertyEditor(new PropertyEditor<Unidad>() {
//				@Override
//				public Unidad parse(CharSequence text) throws ParseException {
//					for (Unidad unidad: unidades) {
//						if(text.equals(unidad.getAbreviatura())) return unidad; 
//					}
//					return null;
//					//return Unidad.parseString(text.toString());
//				}
//
//				@Override
//				public String render(Unidad unidad) {
//					return unidad.getAbreviatura();
//					//return object == null ? Light.SUNNY.toString() : object.toString();
//				}
//			});
//			lightCombo.setTriggerAction(TriggerAction.ALL);
//			lightCombo.add(unidades);
//
//			Converter<String, Unidad> lightConverter = new Converter<String, Unidad>() {
//				@Override
//				public String convertFieldValue(Unidad unidad) {
//					return unidad.getAbreviatura();
//					//return object == null ? "" : object.toString();
//				}
//
//				@Override
//				public Unidad convertModelValue(String text) {
//					for (Unidad unidad: unidades) {
//						if(text.equals(unidad.getAbreviatura())) return unidad; 
//					}
//					return null;
////					try {
////						return Light.parseString(object);
////					} catch (ParseException e) {
////						return null;
////					}
//				}
//			};
//
//			DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
//			DateField dateField = new DateField(new DateTimePropertyEditor(dateFormat));
//			dateField.setClearValueOnParseError(false);
//
//			final GridEditing<Item> editing = createGridEditing(grid);
//			editing.addEditor(bultosColumn, new IntegerField());
//			editing.addEditor(contenidoColumn, new TextField());
//			//editing.addEditor(pesoColumn, lightConverter, lightCombo);
//			//editing.addEditor(unidadColumn, lightConverter, lightCombo);
//			//editing.addEditor(priceColumn, new DoubleField());
//			// column 5 is not editable
//
//			// EDITING //
//			customizeGrid(grid);
//
//			TextButton addButton = new TextButton("Add Item");
//			addButton.addSelectHandler(new SelectHandler() {
//				@Override
//				public void onSelect(SelectEvent event) {
//					Item plant = new Item();
//
//					editing.cancelEditing();
//					store.add(0, plant);
//
//					int row = store.indexOf(plant);
//					editing.startEditing(new GridCell(row, 0));
//				}
//			});
//
//			ToolBar toolBar = new ToolBar();
//			toolBar.add(addButton);
//
//			VerticalLayoutContainer verticalLayoutContainer = new VerticalLayoutContainer();
//			verticalLayoutContainer.add(toolBar, new VerticalLayoutData(1, -1));
//			verticalLayoutContainer.add(grid, new VerticalLayoutData(1, 1));
//
//			panel = new ContentPanel();
//			panel.setHeading("Abstract Grid Editing");
//			panel.add(verticalLayoutContainer);
//
//			panel.setButtonAlign(BoxLayoutPack.CENTER);
//			panel.addButton(new TextButton("Reset", new SelectHandler() {
//				@Override
//				public void onSelect(SelectEvent event) {
//					store.rejectChanges();
//				}
//			}));
//
//			panel.addButton(new TextButton("Save", new SelectHandler() {
//				@Override
//				public void onSelect(SelectEvent event) {
//					store.commitChanges();
//				}
//			}));
//		}
//
//		return panel;
//	}
//
//	/**
//	 * Abstract method to allow example subclass to build the specific editing
//	 * details
//	 */
//	protected abstract GridEditing<Item> createGridEditing(Grid<Item> grid);
//
//	/**
//	 * Additional modifications can be made to the grid in the subclass with
//	 * this method
//	 */
//	protected void customizeGrid(Grid<Item> grid) {
//	}
//
//}
