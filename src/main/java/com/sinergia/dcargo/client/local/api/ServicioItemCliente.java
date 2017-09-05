package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Item;

@Path("/rest/item")
public interface ServicioItemCliente extends RestService {
	
	  @PUT
	  @Path("/buscarItemByGuia")
	  @Produces("application/json")
	  void buscarItemByGuia(Long idGuia, MethodCallback<List<Item>> call);
	  
	  @PUT
	  @Consumes("application/json")
	  void guardarTodos(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardar")
	  void guardar(Item item, MethodCallback<Void> call);
	
	  @PUT
	  @Consumes("application/json")
	  @Path("/nuevoItem")
	  void nuevoItem(Long idGuia, MethodCallback<Item> call);
	  
	  @DELETE 
	  @Consumes("application/json")
	  @Path("/borrarItem")
	  void borrar(Long idGuia, MethodCallback<Void> call);
	  
}
