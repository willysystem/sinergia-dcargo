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

import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.client.shared.Transportista;

@Path("/rest/transportistas")
public interface ServicioTransportistasCliente extends RestService {
	
	  @GET
	  @Produces("application/json")
	  public void getTodos(MethodCallback<List<Transportista>> call);
	  
	  @PUT
	  @Path("/buscarTransportista")
	  @Produces("application/json")
	  public void buscarTransportista(Transportista transportista, MethodCallback<List<Transportista>> call);
	  
	  @PUT
	  @Path("/nuevoTransportista")
	  @Produces("application/json")
	  public void nuevoTransportista(MethodCallback<Transportista> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarBrevet/{idTransportista}/{brevet}")
	  public void guardarBrevet(@QueryParam("idTransportista") Long idTransportista, @QueryParam("brevet") String brevet, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarNombre/{idTransportista}/{nombre}")
	  public void guardarNombre(@QueryParam("idTransportista") Long idTransportista, @QueryParam("nombre") String nombre, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarDireccion/{idTransportista}/{direccion}")
	  public void guardarDireccion(@QueryParam("idTransportista") Long idTransportista, @QueryParam("direccion") String direccion, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarTelefono/{idTransportista}/{telefono}")
	  public void guardarTelefono(@QueryParam("idTransportista") Long idTransportista, @QueryParam("telefono") String telefono, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarPlaca/{idTransportista}/{placa}")
	  public void guardarPlaca(@QueryParam("idTransportista") Long idTransportista, @QueryParam("placa") String placa, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarMarca/{idTransportista}/{marca}")
	  void guardarMarca(@QueryParam("idTransportista") Long idTransportista, @QueryParam("marca") String marca, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarColor/{idTransportista}/{color}")
	  public void guardarColor(@QueryParam("idTransportista") Long idTransportista, @QueryParam("color") String color, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarCapacidad/{idTransportista}/{capacidad}")
	  public void guardarCapacidad(@QueryParam("idTransportista") Long idTransportista, @QueryParam("capacidad") String capacidad, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarVecinoDe/{idTransportista}/{vecinoDe}")
	  public void guardarVecinoDe(@QueryParam("idTransportista") Long idTransportista, @QueryParam("vecinoDe") String vecinoDe, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarNit/{idTransportista}/{ci}")
	  public void guardarCi(@QueryParam("idTransportista") Long idTransportista, @QueryParam("ci") String ci, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/guardarNit/{idTransportista}/{nit}/{nombres}")
	  public void guardarNit(@QueryParam("idTransportista") Long idTransportista, @QueryParam("nit") String nit, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/cambiarEstado/{idTransportista}/{estado}")
	  public void cambiarEstado(@QueryParam("idTransportista") Long idTransportista, @QueryParam("estado") String estado, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/esUnicoNombreCon/{nombre}")
	  public void esUnicoBrevetCon(@QueryParam("nombre") String nombre, MethodCallback<Resultado> call);
	  
	  @DELETE
	  @Path("/{id:[0-9]+}")
	  public void borrar(@PathParam("id") Long id, MethodCallback<Resultado> call);
	  
}
