package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Resultado;

@Path("/rest/cliente")
public interface ServicioClienteCliente extends RestService {
	
	  @GET
	  @Produces("application/json")
	  void getTodos(MethodCallback<List<Cliente>> callback);
	  
	  @PUT
	  @Path("/nuevoCliente")
	  @Produces("application/json")
	  void nuevoCliente(MethodCallback<Cliente> callback);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarNombres/{idCliente}/{nombres}/{nit}")
	  void guardarNombres(@QueryParam("idCliente") Long idCliente, @QueryParam("nombres") String nombres, MethodCallback<Void> callback);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarDireccion/{idCliente}/{direccion}")
	  void guardarDireccion(@QueryParam("idCliente") Long idCliente, @QueryParam("direccion") String direccion, MethodCallback<Void> callback);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarTelefono/{idCliente}/{telefono}")
	  void guardarTelefono(@QueryParam("idCliente") Long idCliente, @QueryParam("telefono") String telefonom, MethodCallback<Void> callback);
	 
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarNit/{idCliente}/{nit}/{nombres}")
	  void guardarNit(@QueryParam("idCliente") Long idCliente, @QueryParam("nit") String nit, MethodCallback<Void> callback);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarNit/{idCliente}/{ci}")
	  void guardarCi(@QueryParam("idCliente") Long idCliente, @QueryParam("ci") String ci, MethodCallback<Void> callback);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/cambiarEstado/{idCliente}/{estado}")
	  void cambiarEstado(@QueryParam("idCliente") Long idCliente, @QueryParam("estado") String estado, MethodCallback<Void> callback);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/esUnicoNombreCon/{nombre}")
	  void esUnicoNombreCon(@QueryParam("nombre") String nombre, MethodCallback<Resultado> callback);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/esUnicoNitCon/{nit}")
	  void esUnicoNitCon(@QueryParam("nit") String nit, MethodCallback<Resultado> callback);
	  
	  @PUT
	  @Consumes("application/json")
	  void guardar(MethodCallback<Void> callback);
	  
	  @PUT
	  @Path("/buscarClientes")
	  @Produces("application/json")
	  void buscarClientes(Cliente cliente, MethodCallback<List<Cliente>> callback);
	  
	  @DELETE
	  @Path("/{id:[0-9]+}")
	  void borrar(@PathParam("id") Long id, MethodCallback<Boolean> callback);
	  
}
