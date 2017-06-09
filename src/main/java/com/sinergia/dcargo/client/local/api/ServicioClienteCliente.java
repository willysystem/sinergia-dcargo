package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.Cliente;

@Path("/rest/cliente")
public interface ServicioClienteCliente extends RestService {
	
	  @GET
	  @Produces("application/json")
	  void getTodos(MethodCallback<List<Cliente>> callback);
	  
	  @PUT
	  @Consumes("application/json")
	  void guardar(MethodCallback<Void> callback) throws Exception;
	  
	  @PUT
	  @Path("/buscarClientes")
	  @Produces("application/json")
	  void buscarClientes(Cliente cliente, MethodCallback<List<Cliente>> callback);
	  
	  @DELETE
	  @Path("/{id:[0-9]+}")
	  void borrar(@PathParam("id") Long id, MethodCallback<Boolean> callback);
	  
}
