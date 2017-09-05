package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.dominio.Unidad;

@Path("/rest/unidad")
public interface ServicioUnidadCliente extends RestService {
	
	  @PUT
	  @Path("/getTodasUnidades")
	  @Produces("application/json")
	  void getTodasUnidades(MethodCallback<List<Unidad>> call);
	  
	  @PUT
	  @Consumes("application/json")
	  void guardar(Unidad unidad, MethodCallback<Void> call) throws Exception;
	
}
