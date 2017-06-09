package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.Precio;

@Path("/rest/precio")
public interface ServicioPrecioCliente extends RestService {
	
	  @PUT
	  @Path("/getTodosLosPrecios")
	  @Produces("application/json")
	  void getTodosLosPrecios(MethodCallback<List<Precio>> call);
	  
	  @PUT
	  @Consumes("application/json")
	  void guardar(Precio unidad, MethodCallback<Void> call) throws Exception;
	
}
