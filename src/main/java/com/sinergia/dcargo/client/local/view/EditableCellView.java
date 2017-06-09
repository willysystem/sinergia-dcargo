package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

public abstract class EditableCellView<E>  extends View<E> {
	
	protected EditableCellView(int paging) {
		super(paging);
	}

	protected List<AbstractEditableCell<?, ?>> editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
	
	protected List<E> pendingChanges = new ArrayList<>();
	
	/**
	 * Add a column with a header.
	 * 
	 * @param <C>
	 *            the cell type
	 * @param cell
	 *            the cell used to render the column
	 * @param headerText
	 *            the header string
	 * @param getter
	 *            the value getter for the cell
	 */
	protected <C> Column<E, C> addColumn(Cell<C> cell, String headerText, final GetValue<E,C> getter, FieldUpdater<E, C> fieldUpdater) {
		Column<E, C> column = new Column<E, C>(cell) {
			@Override
			public C getValue(E object) {
				return getter.getValue(object);
			}
		};
		column.setFieldUpdater(fieldUpdater);
		if (cell instanceof AbstractEditableCell<?, ?>) {
			editableCells.add((AbstractEditableCell<?, ?>) cell);
		}
		grid.addColumn(column, headerText);
		
		return column;
	}
	
//	protected <C> Column<E, C>  addColumnEditable1111(Cell<C> cell, String tittleHeader, final String methodGet, String methodSet){
//		log.info("addColumnEditable:");
//		Column<E, C> column = addColumn(cell, tittleHeader, 
//				 new GetValue<E, C>() {
//					@Override
//					public C getValue(E entity) {
//						C value = null;
//						try {
//							
////							Function function = GWT.create(Function.class);
////							value = (C)function.executeGet(entity, methodGet);
//							//value = (C)GwtReflect.invoke(entity.getClass(), methodGet, null, entity);
//							
////							log.info("valueIni: " + entity.getClass().getName());
////							RttiClass clazz = Rtti.forName(entity.getClass().getName());
////							log.info("clazz:" + clazz);
//							
////							RttiMethod method = clazz.getMethod(methodGet);
////							log.info("method:" + method);
////							value = (C)method.invoke(entity);
////							log.info("value:" + value);
//							
////							Class c = Class.forName(Usuario.class.getName());
//							
//						} catch (Throwable e) {
//							e.printStackTrace();
//						}
//						return value;
//					}
//			  }, new FieldUpdater<E, C>() {
//				  	@Override
//				  	public void update(int index, E entity, C value) {
//				  		try{
//				  			// Class arg[] = { value.getClass() };
//				  			// Function function = GWT.create(Function.class);
//                            // GwtReflect.invoke(entity.getClass(), methodSet, arg, entity, value);
//				  		} catch (Throwable e) {
//				  			e.printStackTrace();
//				  		}
//				  		pendingChanges.add(entity);
//				  	}
//			 });
//		return column;
//	} 
	
	
}
