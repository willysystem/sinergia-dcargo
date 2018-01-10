package com.sinergia.dcargo.client.local.api;

import java.util.Date;
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
import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Guia;

@Path("/rest/conocimiento")
public interface ServicioConocimientoCliente extends RestService {
	
	  @PUT 
	  @Path("/buscarConocimiento")
	  @Produces("application/json")
	  public void buscarConocimiento(Conocimiento conocimiento, MethodCallback<List<Conocimiento>> call);	
		
	  @GET 
	  @Path("/consultarConocimiento/{idConocimiento}")
	  @Produces("application/json")
	  public void consultarConocimiento(@QueryParam("idConocimiento") Long idConocimiento, MethodCallback<Conocimiento> call);
	  
	  @GET 
	  @Path("/getEstados")
	  @Produces("application/json")
	  @Consumes("application/json")
	  public void getEstados(MethodCallback<List<EstadoGuia>> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/nuevaConocimiento")
	  public void nuevoConocimiento(MethodCallback<Conocimiento> call);
	  
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarFechaRegistro/{idConocimiento}/{fechaRegistro}")
	  public void guardarFechaRegistro(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("fechaRegistro") Date fechaRegistro, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarPropietario/{idConocimiento}/{idPropietario}")
	  public void guardarPropietario(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idPropietario") Long idPropietario, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarConductor/{idConocimiento}/{idConductor}")
	  public void guardarConductor(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idConductor") Long idConductor, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarOficinaOrigen/{idConocimiento}/{idOficinaOrigen}")
	  public void guardarOficinaOrigen(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idOficinaOrigen") Long idOficinaOrigen, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarOficinaDestino/{idConocimiento}/{idOficinaDestino}")
	  public void guardarOficinaDestino(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idOficinaDestino") Long idOficinaDestino, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarMulta/{idConocimiento}/{multa}")
	  public void guardarMulta(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("multa") Double multa, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarDias/{idConocimiento}/{dias}")
	  public void guardarDias(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("dias") Integer dias, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarObservacion/{idConocimiento}/{observacion}")
	  public void guardarObservacion(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("observacion") String observacion, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarAdjunto/{idConocimiento}/{adjunto}")
	  public void guardarAdjunto(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("adjunto") String adjunto, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarAdjunto2/{idConocimiento}/{adjunto2}")
	  public void guardarAdjunto2(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("adjunto2") String adjunto2, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarAclaracion/{idConocimiento}/{aclaracion}")
	  public void guardarAclaracion(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("aclaracion") String aclaracion, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarAclaracion2/{idConocimiento}/{aclaracion2}")
	  public void guardarAclaracion2(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("aclaracion2") String aclaracion2, MethodCallback<Void> call);
	 
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarFlete/{idConocimiento}/{flete}")
	  public void guardarFlete(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("flete") Double flete, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Produces("application/json")
	  @Path("/guardarAcuenta/{idConocimiento}/{acuenta}")
	  public void guardarAcuenta(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("acuenta") Double acuenta, MethodCallback<Void> call);
	  
//	  @PUT
//	  @Consumes("application/json")
//	  @Path("/guardarAcuenta/{idConocimiento}/{acuenta}")
//	  public void guardarAcuenta(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("acuenta") Double acuenta, MethodCallback<Void> call);
	  
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarPagoOrigen/{idConocimiento}/{pagoOrigen}")
	  public void guardarPagoOrigen(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("pagoOrigen") Double pagoOrigen, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")  
	  @Path("/guardarPagoDestino/{idConocimiento}/{pagoDestino}")
	  public void guardarPagoDestino(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("pagoDestino") Double pagoDestino, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/cambiarEstado/{idConocimiento}/{estado}")
	  public void cambiarEstado(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("estado") String estado, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/adicionarGuia/{idConocimiento}/{idGuia}")
	  public void adicionarGuia(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idGuia") Long idGuia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/quitarGuia/{idConocimiento}/{idGuia}")
	  public void quitarGuia(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idGuia") Long idGuia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Produces("application/json")
	  @Path("/getGuias/{idConocimiento}")
	  public void getGuias(@QueryParam("idConocimiento") Long idConocimiento, MethodCallback<List<Guia>> call);
	  
	  @DELETE
	  @Path("/{id:[0-9]+}")
	  public void borrar(@PathParam("id") Long id, MethodCallback<Resultado> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/generarNroConocimiento/{idConocimiento}")
	  public void generarNroConocimiento(@QueryParam("idConocimiento") Long idConocimiento, MethodCallback<Integer> call);
	  
}
