package com.sinergia.dcargo.client.local;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.slf4j.Logger;

import com.google.gwt.core.shared.GWT;
import com.sinergia.dcargo.client.local.api.ServicioClienteCliente;
import com.sinergia.dcargo.client.local.api.ServicioOficinaCliente;
import com.sinergia.dcargo.client.local.api.ServicioPrecioCliente;
import com.sinergia.dcargo.client.local.api.ServicioUnidadCliente;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.view.Cargador;
import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.DateParam;
import com.sinergia.dcargo.client.shared.Oficina;
import com.sinergia.dcargo.client.shared.Precio;
import com.sinergia.dcargo.client.shared.Unidad;

@Singleton
public class AdminParametros {

	@Inject
	private Cargador cargador;
	
	@Inject
	protected Logger log;
	
	private ServicioClienteCliente servicioCliente = GWT.create(ServicioClienteCliente.class);
	private ServicioOficinaCliente servicioOficina = GWT.create(ServicioOficinaCliente.class);;
	private ServicioUnidadCliente servicioUnidad   = GWT.create(ServicioUnidadCliente.class);;
	private ServicioPrecioCliente servicioPrecio   = GWT.create(ServicioPrecioCliente.class);;
	
	private List<Cliente> clientes;
	private List<Oficina> oficinas;
	private List<Unidad> unidades;
	private List<Precio> precios;
	
	private DateParam dateParam;
	
	public AdminParametros(){
		GWT.log(this.getClass().getSimpleName() + "()");
	}
	
	@PostConstruct
	public void init(){
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
		poblarParametros();
	}
	
	@AfterInitialization
	public void initTwo(){
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
	}
	
	private void poblarParametros() {
		
		cargador.center();
		servicioCliente.buscarClientes(new Cliente(), new MethodCallback<List<Cliente>>() {
			@Override
			public void onSuccess(Method method, List<Cliente> response) {
				clientes = response;
				log.info("clientes.size" + clientes.size() );
				servicioOficina.getOffices(new MethodCallback<List<Oficina>>() {
					@Override
					public void onSuccess(Method method, List<Oficina> response) {
						oficinas = response;
						log.info("oficinas.size" + oficinas.size() );
						servicioUnidad.getTodasUnidades(new MethodCallback<List<Unidad>>() {
							@Override
							public void onSuccess(Method method, List<Unidad> response) {
								unidades = response;
								log.info("unidades.size" + unidades.size() );
								servicioPrecio.getTodosLosPrecios(new MethodCallback<List<Precio>>() {
									@Override
									public void onSuccess(Method method, List<Precio> response) {
										precios = response;
										log.info("precios.size" + precios.size() );
										cargador.hide();
									}
									@Override
									public void onFailure(Method method, Throwable exception) {
										log.info("Error al traer Precios: " + exception.getMessage());
										cargador.hide();
										new MensajeError("Error al traer Precios: ", exception).show();
									}
								});
							}
							@Override
							public void onFailure(Method method, Throwable exception) {
								log.info("Error al traer Unidades: " + exception.getMessage());
								cargador.hide();
								new MensajeError("Error al traer Unidades: ", exception).show();
							}
						});
					}
					@Override
					public void onFailure(Method method, Throwable exception) {
						log.info("Error al traer Oficinas: " + exception.getMessage());
						cargador.hide();
						new MensajeError("Error al traer Oficinas: ", exception).show();
					}
				});
			}

			@Override
			public void onFailure(Method method, Throwable exception) {
				log.info("Error al traer Clientes: " + exception.getMessage());
				cargador.hide();
				new MensajeError("Error al traer Clientes: ", exception).show();
			}
		});

	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public List<Oficina> getOficinas() {
		return oficinas;
	}

	public void setOficinas(List<Oficina> oficinas) {
		this.oficinas = oficinas;
	}

	public List<Unidad> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<Unidad> unidades) {
		this.unidades = unidades;
	}

	public List<Precio> getPrecios() {
		return precios;
	}

	public void setPrecios(List<Precio> precios) {
		this.precios = precios;
	}

	public DateParam getDateParam() {
		return dateParam;
	}

	public void setDateParam(DateParam dateParam) {
		this.dateParam = dateParam;
	}
	
	public Cliente buscarClientePorNombre(String nombre){
		for (Cliente cliente : clientes) {
			if(cliente.getNombre().equals(nombre))
				return cliente;
		}
		return null;
	}
	
	public Oficina buscarOficinaPorNombre(String nombre){
		for (Oficina ofi : oficinas) {
			if(ofi.getNombre().equals(nombre))
				return ofi;
		}
		return null;
	} 
	
}
