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
import com.sinergia.dcargo.client.local.api.LlamadaRemota;
import com.sinergia.dcargo.client.local.api.ServicioClienteCliente;
import com.sinergia.dcargo.client.local.api.ServicioCuentaCliente;
import com.sinergia.dcargo.client.local.api.ServicioOficinaCliente;
import com.sinergia.dcargo.client.local.api.ServicioPrecioCliente;
import com.sinergia.dcargo.client.local.api.ServicioTransportistasCliente;
import com.sinergia.dcargo.client.local.api.ServicioUnidadCliente;
import com.sinergia.dcargo.client.local.message.MensajeError;
import com.sinergia.dcargo.client.local.view.Carga;
import com.sinergia.dcargo.client.local.view.Cargador;
import com.sinergia.dcargo.client.shared.dominio.Cliente;
import com.sinergia.dcargo.client.shared.dominio.CuentaEgreso;
import com.sinergia.dcargo.client.shared.dominio.CuentaIngreso;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dominio.Precio;
import com.sinergia.dcargo.client.shared.dominio.Transportista;
import com.sinergia.dcargo.client.shared.dominio.Unidad;
import com.sinergia.dcargo.client.shared.dominio.Usuario;
import com.sinergia.dcargo.client.shared.dto.DateParam;

@Singleton
public class AdminParametros {

	@Inject private Cargador cargador;
	
	@Inject protected Logger log;
	
	@Inject private ServicioClienteCliente servicioCliente; 
	@Inject private ServicioOficinaCliente servicioOficina;
	@Inject private ServicioUnidadCliente servicioUnidad;
	@Inject private ServicioPrecioCliente servicioPrecio;
	@Inject private ServicioTransportistasCliente servicioTransportista;
	@Inject private ServicioCuentaCliente servicioCuenta;
	
	
	private List<Cliente> clientes;
	private List<Oficina> oficinas;
	private List<Unidad> unidades;
	private List<Precio> precios;
	private List<Transportista> transportistas;
	private List<CuentaIngreso> cuentasIngreso;
	private List<CuentaEgreso> cuentasEgreso;
	
	private List<Usuario> usuarios;
	
	private Usuario usuario;
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	private DateParam dateParam;
	
	public AdminParametros(){
		GWT.log(this.getClass().getSimpleName() + "()");
	}
	
	@PostConstruct
	public void init(){
		log.info("@PostConstruct: " + this.getClass().getSimpleName());
		poblarParametros(null);
	}
	
	@AfterInitialization
	public void initTwo(){
		log.info("@AfterInitialization: " + this.getClass().getSimpleName());
	}
	
	public void poblarTransportistas(final Carga carga) {
		cargador.center();
		servicioTransportista.getTodos(new LlamadaRemota<List<Transportista>>("Error al obtener los transportista",true) {
			@Override
			public void onSuccess(Method method, List<Transportista> response) {
				transportistas = response;
				log.info("transportistas.size" + transportistas.size() );
				AdminParametros.this.cargador.hide();
				carga.cargarOracles();
			}
		});
	}
	
	public void poblarParametros(final Carga carga) {
		
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
										if(carga != null){
											carga.cargarOracles();
										}
										servicioTransportista.getTodos(new LlamadaRemota<List<Transportista>>("Error al obtener los transportista",true) {
											@Override
											public void onSuccess(Method method, List<Transportista> response) {
												transportistas = response;
												log.info("transportistas.size" + transportistas.size() );
												servicioCuenta.getTodasCuentasIngreso(new LlamadaRemota<List<CuentaIngreso>>("Error al obtener los transportista", true) {
													@Override
													public void onSuccess(Method method, List<CuentaIngreso> response) {
														log.info("CuentasIngreso.size" + response.size() );
														cuentasIngreso = (List<CuentaIngreso>)response;
														servicioCuenta.getTodasCuentasEgreso(new LlamadaRemota<List<CuentaEgreso>>("Error al obtener los transportista", true) {
															@Override
															public void onSuccess(Method method, List<CuentaEgreso> response) {
																log.info("CuentasEgreso.size" + response.size() );
																cuentasEgreso = (List<CuentaEgreso>)response;
																//log.info("cuentasIngreso.size" + cuentasIngreso.size() );
																AdminParametros.this.cargador.hide();
															}
														});
													}
												});
											}
										});
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
	
	public List<Transportista> getTransportistas() {
		return transportistas;
	}

	public void setTransportistas(List<Transportista> transportistas) {
		this.transportistas = transportistas;
	}

	public DateParam getDateParam() {
		return dateParam;
	}

	public void setDateParam(DateParam dateParam) {
		this.dateParam = dateParam;
	}
	
	public List<CuentaEgreso> getCuentasEgreso() {
		return cuentasEgreso;
	}
	
	public List<CuentaIngreso> getCuentasIngreso() {
		return cuentasIngreso;
	}

	public Cliente buscarClientePorNombre(String nombre){
		for (Cliente cliente : clientes) {
			if(cliente.getNombre().equals(nombre))
				return cliente;
		}
		return null;
	}
	
	public Cliente buscarClientePorId(Long id){
		for (Cliente cliente : clientes) {
			if(cliente.getId() == id )
				return cliente;
		}
		return null;
	}
	
	public Transportista buscarTransportistaPorNombre(String nombre){
		for (Transportista transportista : transportistas) {
			if(transportista.getNombre() == null ) continue;
			if(transportista.getNombre().equals(nombre))
				return transportista;
		}
		return null;
	}
	
	public Transportista buscarTransportistaPorId(String nombre){
		for (Transportista transportista : transportistas) {
			if(transportista.getNombre().equals(nombre))
				return transportista;
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
	
	public CuentaIngreso buscarCuentaIngresoPorNroCuenta(Integer nroCuenta){
		for (CuentaIngreso cuenta: cuentasIngreso) {
			log.info("       cuentasIngreso: " + cuenta.getNroCuenta());
			if(cuenta.getNroCuenta() != null)
				if(cuenta.getNroCuenta().equals(nroCuenta))
					return cuenta;
		}
		return null;
	}
	
	public CuentaEgreso buscarCuentaEgresoPorNroCuenta(Integer nroCuenta){
		for (CuentaEgreso cuenta: cuentasEgreso) {
			log.info("       cuentasIngreso: " + cuenta.getNroCuenta());
			if(cuenta.getNroCuenta() != null)
				if(cuenta.getNroCuenta().equals(nroCuenta))
					return cuenta;
		}
		return null;
	}
	
	public void recargarCuentasIngreso(){
		log.info("transportistas.size" + transportistas.size() );
		cargador.show();
		servicioCuenta.getTodasCuentasIngreso(new LlamadaRemota<List<CuentaIngreso>>("Error al obtener los transportista", true) {
			@Override
			public void onSuccess(Method method, List<CuentaIngreso> response) {
				log.info("response.size" + response.size() );
				cuentasIngreso = (List<CuentaIngreso>)response;
				//log.info("cuentasIngreso.size" + cuentasIngreso.size() );
				AdminParametros.this.cargador.hide();
			}
		});
	}
	
	public void recargarCuentasEgreso(){
		log.info("transportistas.size" + transportistas.size() );
		cargador.show();
		servicioCuenta.getTodasCuentasEgreso(new LlamadaRemota<List<CuentaEgreso>>("Error al obtener los transportista", true) {
			@Override
			public void onSuccess(Method method, List<CuentaEgreso> response) {
				log.info("response.size" + response.size() );
				cuentasEgreso = (List<CuentaEgreso>)response;
				//log.info("cuentasIngreso.size" + cuentasIngreso.size() );
				AdminParametros.this.cargador.hide();
			}
		});
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
}
