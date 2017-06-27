package com.sinergia.dcargo.client.local.view;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;

public abstract class View<E> {
	
	@Inject
	protected Logger log;
	
	@Inject
	protected MainContentPresenter.Display mainContentView;
	
	protected SimplePager simplePager;
	
	protected ListDataProvider<E> dataProvider = new ListDataProvider<E>();
	
	protected Button reCargarButton = new Button("Recargar");
	
	protected ProvidesKey<E> KEY_PROVIDER = new ProvidesKey<E>() {
		@Override
		public Object getKey(E item) {
			return item == null ? null : getKeyItem(item);
		}
	};
	
	protected DataGrid<E> grid;
	
	protected int paging;
	
	protected View(int paging){
		GWT.log(this.getClass().getSimpleName() + "()");
		this.paging = paging;
	}
	
	@PostConstruct
	protected void init() {
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}
	
	@AfterInitialization
	public void cargarDataUI() {
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
	}
	
	protected void defaultUI(){
		
		// Grid
		grid = new DataGrid<E>(10, KEY_PROVIDER);
		grid.setEmptyTableWidget(new Label("Sin datos"));
		
		// Selection Model
		SingleSelectionModel<E> selectionModel = new SingleSelectionModel<E>();
		grid.setSelectionModel(selectionModel);
		
		// DataProvider
		dataProvider.addDataDisplay(grid);
		
		// Pager
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		simplePager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		simplePager.setDisplay(grid);
		
		// Nro
		TextColumn<E> nro = new TextColumn<E>() {
			@Override
			public String getValue(E entity) {
				return getNro(entity);
			}
		};
		grid.setColumnWidth(nro, 15, Unit.PX);
		grid.addColumn(nro, "Nº");

	}
	
	protected abstract Object getKeyItem(E item);

	protected abstract String getNro(E entity);
	
}
