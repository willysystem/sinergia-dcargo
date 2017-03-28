package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.local.presenter.UserMainPresenter;
import com.sinergia.dcargo.client.shared.User;

public class UserMainView implements UserMainPresenter.Display {

	@Inject
	private MainContentPresenter.Display mainContentView;
	
	private List<AbstractEditableCell<?, ?>> editableCells;
	
	DataGrid<User> grid;
	
	private ListDataProvider<User> dataProvider = new ListDataProvider<User>();
	
	private List<PendingChange<?>> pendingChanges = new ArrayList<PendingChange<?>>();
	
	/**
     * The key provider that provides the unique ID of a contact.
     */
    public static final ProvidesKey<User> KEY_PROVIDER = new ProvidesKey<User>() {
      @Override
      public Object getKey(User item) {
        return item == null ? null : item.getId();
      }
    };
	
	private Button saveButton = new Button("Guardar");
	private Button newButton = new Button("Nuevo");
    
	public UserMainView(){
		
	}
	
	@PostConstruct
	public void init() {
		
	}

	@Override
	public void showUsers(List<User> response) {
		
	   editableCells = new ArrayList<AbstractEditableCell<?, ?>>();
	   
	   grid = new DataGrid<User>(25, KEY_PROVIDER);
//	   //contactList.set
	   //contactList.setMinimumTableWidth(140, Unit.EM);
	   grid.setEmptyTableWidget(new Label("Sin datos"));
	   dataProvider.addDataDisplay(grid);
	   dataProvider.setList(response);
	   
//	   //contactList.se
//	   //ContactDatabase.get().addDataDisplay(contactList);
//		
//       
	   // Nro
	   TextColumn<User> nro = new TextColumn<User>() {
		   @Override
		   public String getValue(User object) {
			    return "" + object.getNro();
		   }};
	   grid.setColumnWidth(nro, 3, Unit.EM);
	   grid.addColumn(nro, "Nº");
	   
//	   // Names
	   Column<User, String> namesTextColumn = addColumn(new EditTextCell(), "Nombres", new GetValue<String>() {
		   										@Override
		   										public String getValue(User contact) {
		   											return contact.getNames();
		   										}
	            								}, new FieldUpdater<User, String>() {
	            										@Override
	            										public void update(int index, User object, String value) {
	            											GWT.log("index: " + index + ", object: " + object + ", value: " + value);
	            											pendingChanges.add(new NamesChange(object, value));
	            										}
	            								});
	   grid.setColumnWidth(namesTextColumn, 12, Unit.EM);
	   
      // Surnames
	   Column<User, String> surnamesTextColumn = addColumn(new EditTextCell(), "Apellidos", new GetValue<String>() {
				@Override
				public String getValue(User contact) {
					return contact.getSurnames();
				}
			}, new FieldUpdater<User, String>() {
					@Override
					public void update(int index, User object, String value) {
						//pendingChanges.add(new FirstNameChange(object, value));
					}
			});
	   grid.setColumnWidth(surnamesTextColumn, 12, Unit.EM);
	   
	   // User Name
	   Column<User, String> userName = addColumn(new EditTextCell(), "Usuario", new GetValue<String>() {
			@Override
			public String getValue(User contact) {
				return contact.getUser();
			}
		}, new FieldUpdater<User, String>() {
				@Override
				public void update(int index, User object, String value) {
					//pendingChanges.add(new FirstNameChange(object, value));
				}
		});
      grid.setColumnWidth(userName, 8, Unit.EM);
       
      // Expiration Date
      DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
      Column<User, Date> expirationDate = addColumn(new DateCell(dateFormat), "Expiración", new GetValue<Date>() {
			@Override
			public Date getValue(User contact) {
				return contact.getExpirationDate();
			}
		}, new FieldUpdater<User, Date>() {
			@Override
			public void update(int index, User object, Date value) {
				//pendingChanges.add(new FirstNameChange(object, value));
			}
		});
      grid.setColumnWidth(expirationDate, 10, Unit.EM); 
      
      // Status
      addColumn(new CheckboxCell(), "¿Administrador?", new GetValue<Boolean>() {
        @Override
        public Boolean getValue(User user) {
        	return user.getAdministrator();
        }
      }, new FieldUpdater<User, Boolean>() {
        @Override
        public void update(int index, User object, Boolean value) {
//          for (Category category : categories) {
//            if (category.getDisplayName().equals(value)) {
//              pendingChanges.add(new CategoryChange(object, category));
//              break;
//            }
//          }
        }
      });
      
      // User Type
      addColumn(new CheckboxCell(), "¿Activo?", new GetValue<Boolean>() {
          @Override
          public Boolean getValue(User user) {
          	return user.getActive();
          }
        }, new FieldUpdater<User, Boolean>() {
          @Override
          public void update(int index, User object, Boolean value) {
//            for (Category category : categories) {
//              if (category.getDisplayName().equals(value)) {
//                pendingChanges.add(new CategoryChange(object, category));
//                break;
//              }
//            }
          }
        });
	   
		SimpleLayoutPanel slp = new SimpleLayoutPanel();
		slp.setSize("900px", "400px");
		slp.add(grid);
        
		DockPanel dock = new DockPanel();
		dock.add(slp, DockPanel.CENTER);
		
        //VerticalPanel verticalPanel = new VerticalPanel();
        //verticalPanel.setSize("900px", "400px");
        //verticalPanel.add(slp);
        
		//verticalPanel.add(saveButton);
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setWidth("100%");
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalPanel horizontalPanelButton = new HorizontalPanel();
		horizontalPanelButton.setSpacing(5);
		horizontalPanelButton.add(saveButton);
		horizontalPanelButton.add(newButton);
		horizontalPanel.add(horizontalPanelButton);
		
		dock.add(horizontalPanel, DockPanel.SOUTH);
		
		mainContentView.getCentralPanel().add(dock);
	}
	
	@Override
	public List<User> commitChangesLocal(){
		// Commit the changes.
        for (PendingChange<?> pendingChange : pendingChanges) {
          pendingChange.commit();
        }
        
        //pendingChanges.clear();
        List<User> users = new ArrayList<User>();
        int n = pendingChanges.size();
        GWT.log("n:" + n);
        for(int i = 0; i < n; i++) {
        	users.add(pendingChanges.get(i).getUser());
		}
        return users;

        // Push the changes to the views.
        //ContactDatabase.get().refreshDisplays();

	}
	
	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}
	
	/**
	   * Add a column with a header.
	   * 
	   * @param <C> the cell type
	   * @param cell the cell used to render the column
	   * @param headerText the header string
	   * @param getter the value getter for the cell
	   */
	  private <C> Column<User, C> addColumn(Cell<C> cell, String headerText,
	      final GetValue<C> getter, FieldUpdater<User, C> fieldUpdater) {
	    Column<User, C> column = new Column<User, C>(cell) {
	      @Override
	      public C getValue(User object) {
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

	  private static interface GetValue<C> {
		 C getValue(User contact);
	  }
	  
	  interface Binder extends UiBinder<Widget, UserMainView> {
	  }
	  
	  /**
	   * A pending change to a {@link ContactInfo}. Changes aren't committed
	   * immediately to illustrate that cells can remember their pending changes.
	   * 
	   * @param <T> the data type being changed
	   */
	  private abstract static class PendingChange<T> {
	    private final User user;
	    private final T value;

	    public PendingChange(User user, T value) {
	      this.user = user;
	      this.value = value;
	    }

	    /**
	     * Commit the change to the contact.
	     */
	    public void commit() {
	      doCommit(user, value);
	    }

	    /**
	     * Update the appropriate field in the {@link ContactInfo}.
	     * 
	     * @param contact the contact to update
	     * @param value the new value
	     */
	    protected abstract void doCommit(User user, T value);
	    
	    public User getUser() {
	    	return user;
	    }
	    
	  }
	  
	  /**
	   * Updates the first name.
	   */
	  private static class NamesChange extends PendingChange<String> {

	    public NamesChange(User contact, String value) {
	      super(contact, value);
	    }

	    @Override
	    protected void doCommit(User user, String value) {
	      user.setNames(value);
	    }
	  }

	@Override
	public HasClickHandlers getNewButton() {
		return newButton;
	}

	@Override
	public void addNewUser(User user) {
		GWT.log("user: " + user);
		dataProvider.getList().add(user);
		GWT.log("addNewUser ok: " + user);
	}


}
