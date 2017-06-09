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

import com.sinergia.dcargo.client.shared.Guia;

@Path("/rest/guia")
public interface ServicioGuiaCliente extends RestService {
	
	  @PUT
	  @Path("/buscarGuias")
	  @Produces("application/json")
	  void buscarGuias(Guia cliente, MethodCallback<List<Guia>> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/nuevaGuia")
	  void nuevaGuia(MethodCallback<Guia> call);
	  
	  @PUT
	  @Consumes("application/json")
	  void guardar(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarNroFactura")
	  void guardarNroFactura(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarRemitente")
	  void guardarRemitente(Guia guia,  MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarConsignatario")
	  void guardarConsignatario(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarOrigen")
	  void guardarOrigen(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarDestino")
	  void guardarDestino(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarAdjunto")
	  void guardarAdjunto(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarResumen")
	  void guardarResumen(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarNroEntrega")
	  void guardarNroEntrega(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarPagoOrigen")
	  void guardarPagoOrigen(Guia guia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarPagoDestino")
	  void guardarPagoDestino(Guia guia, MethodCallback<Void> call);
	  
	  @GET
	  @Path("/consultarGuia/{idGuia}")
	  @Produces("application/json")
	  void consultarGuia(@QueryParam("idGuia") Long idGuia, MethodCallback<Guia> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/cambiarEstado/{idGuia}/{estado}")
	  void cambiarEstado(@QueryParam("idGuia") Long idGuia, @QueryParam("estado")Boolean estado, MethodCallback<Void> call);
	  
	  @DELETE
	  @Path("/{id:[0-9]+}")
	  void borrar(@PathParam("id") Long id,  MethodCallback<Boolean> call);
	  
}
