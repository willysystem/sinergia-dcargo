package com.sinergia.dcargo.client.local.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.AdminParametros;
import com.sinergia.dcargo.client.local.api.ServicioClienteCliente;
import com.sinergia.dcargo.client.local.api.ServicioGuiaCliente;
import com.sinergia.dcargo.client.local.api.ServicioOficinaCliente;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.view.Cargador;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Guia;
import com.sinergia.dcargo.client.shared.Oficina;

@Singleton
public class PresentadorGuia implements Presenter {

	@Inject
	public Display display;

	@Inject
	private Logger log;
	
	@Inject
	private AdminParametros adminParametros;
	
	@Inject
	private Cargador cargador;
	
	private List<Cliente> clientes;
	private List<Oficina> oficinas;
	
	private ServicioClienteCliente servicioCliente = GWT.create(ServicioClienteCliente.class);
	private ServicioOficinaCliente servicioOficina = GWT.create(ServicioOficinaCliente.class);
	private ServicioGuiaCliente servicioGuia = GWT.create(ServicioGuiaCliente.class);

	public interface Display {

		//void showUsers(List<Usuario> response);
//		HasClickHandlers getSaveButton();
//		HasClickHandlers getNewButton();
//		List<Usuario> commitChangesLocal();
//		void addNewUser(Usuario user);
//		HasClickHandlers getFijarContrasenaButton();
//		HasClickHandlers getReCargarButton();
//		void setOffices(List<Oficina> offices);
//		Usuario getUsuarioSeleccionado();
		//void reCargarDatos(List<Usuario> user);
		
		void viewIU();
		HasClickHandlers getBuscarButton();
		void cargarDataUI(List<Guia> clientes);
		Guia getParametrosBusqueda();
		void fijarOracleParaClientes(List<String> palabras);
		void fijarOracleParaOficina(List<String> palabras);
		
	}
	
	
	public PresentadorGuia() {
		GWT.log(this.getClass().getSimpleName() + "()");
	}
	
	@PostConstruct
	public void postConstruct(){
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
	}
	
	@AfterInitialization
	public void after() {
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
	}

	@Override
	public void go(HasWidgets container) {
		log.info(this.getClass().getSimpleName() + ".go()" );
		display.viewIU();
		
		//Cliente cliente = new Cliente();
		clientes = adminParametros.getClientes();
		log.info("clientes.size: " + clientes.size());
		List<String> palabras = new ArrayList<>();
		for (Cliente cli : clientes) {
			palabras.add(cli.getNombre());
		}
		display.fijarOracleParaClientes(palabras);
		
		oficinas = adminParametros.getOficinas();
		log.info("oficinas.size: " + oficinas.size());
		List<String> palabras1 = new ArrayList<>();
		for (Oficina oficina : oficinas) {
			palabras1.add(oficina.getNombre());
		}
		display.fijarOracleParaOficina(palabras1);
		
		
//		servicioCliente.buscarClientes(cliente, new MethodCallback<List<Cliente>>() {
//			@Override
//			public void onFailure(Method method, Throwable exception) {
//				log.info("Error traer clientes: " + exception.getMessage());
//				new MensajeError("Error traer clientes:", exception).show();
//			}
//			@Override
//			public void onSuccess(Method method, List<Cliente> response) {
//				
//			}
//		});
		
//		servicioOficina.getOffices(new MethodCallback<List<Oficina>>() {
//			@Override
//			public void onFailure(Method method, Throwable exception) {
//				log.info("Error traer clientes: " + exception.getMessage());
//				new MensajeError("Error traer clientes:", exception).show();
//			}
//			@Override
//			public void onSuccess(Method method, List<Oficina> response) {
//			}
//		});
		
//		servicioCliente.getTodos(new MethodCallback<List<Guia>>() {
//			@Override
//			public void onSuccess(Method method, List<Guia> response) {
//				showClientesData(response);
//			}
//			
//			@Override
//			public void onFailure(Method method, Throwable exception) {
//				log.info("Error al guardar: " + exception.getMessage());
//				new MensajeError("Error al guardar", exception).show();
//			}
//		});
		
		
		bind();

	}

	public void bind() {
		
		this.display.getBuscarButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Guia guia = display.getParametrosBusqueda();
				log.info("guia: "+ guia);
				cargador.center();
				servicioGuia.buscarGuias(guia, new MethodCallback<List<Guia>>() {
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al traer Guias: " + exception.getMessage());
						cargador.hide();
						new MensajeError("Error al traer Guias: ", exception).show();
					}
					@Override
					public void onSuccess(Method method, List<Guia> response) {
						showGuiaData(response);
						cargador.hide();
					}
				});
				
				//				servicioCliente.buscarClientes(cliente, new MethodCallback<List<Guia>>() {
//				@Override
//				public void onSuccess(Method method, List<Guia> response) {
//					showClientesData(response);
//				}
//				
//				@Override
//				public void onFailure(Method method, Throwable exception) {
//					log.info("Error al guardar: " + exception.getMessage());
//					new MensajeError("Error al guardar", exception).show();
//				}
//			});
			}
		});
		
//		this.display.getSaveButton().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				GWT.log("event: " + event);
//				List<Usuario> users = display.commitChangesLocal();
//				if(users.size() == 0) {
//					new MensajeAviso("No se modificaron usuarios").show();
//					return ;
//				} 
//				userServiceClient.saveAll(users, new MethodCallback<Void>() {
//					@Override
//					public void onFailure(Method method, Throwable exception) {
//						log.info("Error al guardar: " + exception.getMessage());
//						new MensajeError("Error al guardar", exception).show();
//					}
//					@Override
//					public void onSuccess(Method method, Void response) {
//						new MensajeExito("Guardó exitosamente ...").show();
//					}
//				});
//			}
//		});
//
//		this.display.getNewButton().addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//			   userServiceClient.newUser(new MethodCallback<Usuario>() {
//			      @Override
//				  public void onFailure(Method method, Throwable exception) {
//					 
//				  }
//				  @Override
//				  public void onSuccess(Method method, Usuario response) {
//					 display.addNewUser(response);
//				  }
//			   });	
//			}
//		});
//		
//		this.display.getFijarContrasenaButton().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				final Usuario user = display.getUsuarioSeleccionado();
//				if(user == null) {
//					new MensajeAviso("Selecione un usuario").show();
//				} else {
//					userServiceClient.fijarContraseniaPorDefecto(user.getId(), new MethodCallback<Usuario>() {
//						@Override
//						public void onFailure(Method method, Throwable exception) {
//							new MensajeError("Error al fijar contraseña por defecto", exception).show();
//						}
//						@Override
//						public void onSuccess(Method method, Usuario response) {
//							new MensajeAviso("Se cambio exitosamente la contraseña por defecto para " + user.getNombres()).show();
//						}
//					});
//				}
//			}
//		});
//		
//		this.display.getReCargarButton().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				userServiceClient.getUsers(new MethodCallback<List<Usuario>>() {
//					@Override
//					public void onFailure(Method method, Throwable exception) {
//						new MensajeError("Error al cargar los datos", exception).show();
//					}
//
//					@Override
//					public void onSuccess(Method method, List<Usuario> response) {
//						showUsersData(response);
//					}
//				});
//			}
//		});

	}
	
	int i = 1;
	private void showGuiaData(List<Guia> guias) {
		for (Guia guia: guias) {
			guia.setNro(i++);
		}
		i = 1;
		display.cargarDataUI(guias);
	}

//	@Produces
//	public ServicioUsuarioCliente createUserServiceClient(){
//		return userServiceClient;
//	}
	
}
