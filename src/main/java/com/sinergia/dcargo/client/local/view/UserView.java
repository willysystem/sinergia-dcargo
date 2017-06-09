package com.sinergia.dcargo.client.local.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Singleton;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sinergia.dcargo.client.local.presenter.UserPresenter;
import com.sinergia.dcargo.client.shared.Oficina;
import com.sinergia.dcargo.client.shared.Usuario;

/**
 * @author willy
 */
@Singleton
public class UserView extends EditableCellView<Usuario> implements UserPresenter.Display {

	private Button saveButton = new Button("Guardar");
	private Button newButton = new Button("Nuevo");
	private Button fijarContrasenaButton = new Button("Fijar Contraseña");
	
	public UserView() {
		super(10);
	}
	
	@Override
	public void viewIU() {
		
		log.info("pre0 addColumnEditable:");
		defaultUI();
		
		// Names
		Column<Usuario, String> namesTextColumn = addColumn(new EditTextCell(), "Nombres", 
			 new GetValue<Usuario, String>() {
				@Override
				public String getValue(Usuario contact) {
					return contact.getNombres();
				}
		 }, new FieldUpdater<Usuario, String>() {
			  	@Override
			  	public void update(int index, Usuario user, String names) {
			  		user.setNombres(names);
			  		pendingChanges.add(user);
			  	}
		});
		grid.setColumnWidth(namesTextColumn, 50, Unit.PX);
		
		// Surnames
		Column<Usuario, String> surnamesTextColumn = addColumn(new EditTextCell(), "Apellidos", new GetValue<Usuario, String>() {
				@Override
				public String getValue(Usuario contact) {
					return contact.getApellidos();
				}
			}, new FieldUpdater<Usuario, String>() {
				@Override
				public void update(int index, Usuario user, String surnames) {
					user.setApellidos(surnames);
					pendingChanges.add(user);	
				}
		});
		grid.setColumnWidth(surnamesTextColumn, 50, Unit.PX);
		
		// Usuario Name
		Column<Usuario, String> userName = addColumn(new EditTextCell(), "Usuario", new GetValue<Usuario, String>() {
				@Override
				public String getValue(Usuario contact) {
					return contact.getNombreUsuario();
				}
			}, new FieldUpdater<Usuario, String>() {
				@Override
				public void update(int index, Usuario user, String userName) {
					user.setNombreUsuario(userName);
					pendingChanges.add(user);
				}
			});
			grid.setColumnWidth(userName, 30, Unit.PX);

			// Expiration Date
			DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
			Column<Usuario, Date> expirationDate = addColumn(new DateCell(dateFormat), "Expiración", new GetValue<Usuario, Date>() {
				@Override
				public Date getValue(Usuario contact) {
					return contact.getFechaExpiracion();
				}
			}, new FieldUpdater<Usuario, Date>() {
				@Override
				public void update(int index, Usuario user, Date expirationDate) {
					user.setFechaExpiracion(expirationDate);
					pendingChanges.add(user);
				}
			});
			grid.setColumnWidth(expirationDate, 30, Unit.PX);

			// Usuario Type
			Column<Usuario, Boolean> adm = addColumn(new CheckboxCell(), "¿Administrador?", new GetValue<Usuario, Boolean>() {
				@Override
				public Boolean getValue(Usuario user) {
					return user.getAdministrador();
				}
			}, new FieldUpdater<Usuario, Boolean>() {
				@Override
				public void update(int index, Usuario user, Boolean adminitrator) {
					user.setAdministrador(adminitrator);
					pendingChanges.add(user);
				}
			});
			adm.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setColumnWidth(adm, 36, Unit.PX);
			
			// Status
			Column<Usuario, Boolean> activo = addColumn(new CheckboxCell(), "¿Activo?", new GetValue<Usuario, Boolean>() {
				@Override
				public Boolean getValue(Usuario user) {
					return user.getActivo();
				}
			}, new FieldUpdater<Usuario, Boolean>() {
				@Override
				public void update(int index, Usuario user, Boolean active) {
					user.setActivo(active);
					pendingChanges.add(user);
				}
			});
			grid.setColumnWidth(activo, 22, Unit.PX);
			activo.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			grid.setWidth("1000px");
			grid.setHeight("350px");
			DockPanel dock = new DockPanel();
			dock.add(grid, DockPanel.NORTH);
			
			HorizontalPanel horizontalPanelPager = new HorizontalPanel();
			horizontalPanelPager.setWidth("100%");
			horizontalPanelPager.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			horizontalPanelPager.add(simplePager);
			dock.add(horizontalPanelPager, DockPanel.CENTER);
			
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setWidth("100%");
			horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
			
			HorizontalPanel horizontalPanelButton = new HorizontalPanel();
			horizontalPanelButton.setSpacing(5);
			horizontalPanelButton.add(saveButton);
			horizontalPanelButton.add(newButton);
			horizontalPanelButton.add(fijarContrasenaButton);
			horizontalPanelButton.add(reCargarButton);
			horizontalPanel.add(horizontalPanelButton);
			dock.add(horizontalPanel, DockPanel.SOUTH);

			log.info("-->mainContentView: " + mainContentView);
			log.info("-->mainContentView.getCentralPanel(): " + mainContentView.getCentralPanel());
			mainContentView.getCentralPanel().add(dock);		
	}


	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	@Override
	public HasClickHandlers getNewButton() {
		return newButton;
	}
	
	@Override
	public HasClickHandlers getFijarContrasenaButton() {
		return fijarContrasenaButton;
	}
	
	@Override
	public HasClickHandlers getReCargarButton() {
		return reCargarButton;
	}
	

	@Override
	public void addNewUser(Usuario user) {
		int nro = dataProvider.getList().size() + 1;
		user.setNro(nro);
		dataProvider.getList().add(user);
		grid.flush();
		GWT.log("addNewUser ok: " + user);
	}

	@Override
	public void setOffices(final List<Oficina> offices) {
		log.info("offices.size: " + offices.size());
		Oficina officeAux = new Oficina();
		officeAux.setNombre("");
		offices.add(officeAux);

		// Oficina
		List<String> officesUI = new ArrayList<String>();
		officesUI.add("");
		for (Oficina office : offices) {
			officesUI.add(office.getNombre());
		}
		SelectionCell selectionCell = new SelectionCell(officesUI);
		Column<Usuario, String> sucursal = addColumn(selectionCell, "Sucursal", new GetValue<Usuario, String>() {
			@Override
			public String getValue(Usuario user) {
				if (user.getOffice() != null) {
					String name = user.getOffice().getNombre();
					// log.info("user.getOffice().getName(): " + name);
					return name;
				} else {
					// log.info("na");
					return "";
				}
			}
		}, new FieldUpdater<Usuario, String>() {
			@Override
			public void update(int index, Usuario user, String value) {
				for (Oficina office : offices) {
					//if(office.getName().subSequence(beginIndex, endIndex))
					log.info("user: " + user + ", value: " + value);
					log.info("office: " + office + ", office.getName(): " + office.getNombre());
					if (office.getNombre().equals(value)) {
						if(value.equals("")){
							user.setOffice(null);
							log.info("null");
						} else {
							user.setOffice(office);
						}
						pendingChanges.add(user);
						break;
					}
				}
			}
		});
		grid.setColumnWidth(sucursal, 60, Unit.PX);
	}

	@Override
	public List<Usuario> commitChangesLocal() {
		return pendingChanges;
	}

	@Override
	public Usuario getUsuarioSeleccionado() {
		for(Usuario user: dataProvider.getList()){
			if(grid.getSelectionModel().isSelected(user)){
				return user;
			}
		}
		return null;
	}

	@Override
	public void cargarDataUI(List<Usuario> users) {
		dataProvider.getList().clear();
		dataProvider.setList(users);
	}

	@Override
	protected Object getKeyItem(Usuario item) {
		return item.getId();
	}

	@Override
	protected String getNro(Usuario entity) {
		return entity.getNro()+"";
	}

}