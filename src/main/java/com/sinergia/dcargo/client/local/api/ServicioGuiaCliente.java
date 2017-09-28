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

import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Guia;

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
	  
	  @GET 
	  @Path("/getEstados")
	  @Produces("application/json")
	  @Consumes("application/json")
	  public void getEstados(MethodCallback<List<EstadoGuia>> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/cambiarEstado/{idGuia}/{estadoDescripcion}")
	  void cambiarEstado(@QueryParam("idGuia") Long idGuia, @QueryParam("estadoDescripcion")String estadoDescripcion, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarTotal/{idGuia}/{total}")
	  void guardarTotal(@QueryParam("idGuia") Long idGuia, @QueryParam("total")Double total, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarPesoTotal/{idGuia}/{pesoTotal}")
	  void guardarPesoTotal(@QueryParam("idGuia") Long idGuia, @QueryParam("pesoTotal") Double pesoTotal, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarBultosTotal/{idGuia}/{bultosTotal}")
	  void guardarBultosTotal(@QueryParam("idGuia") Long idGuia, @QueryParam("bultosTotal") Integer bultosTotal, MethodCallback<Void> call);
	  
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarNombreClienteEntrega/{idGuia}/{nombreClienteEntrega}")
	  void guardarNombreClienteEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("nombreClienteEntrega") String nombreClienteEntrega, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarCiEntrega/{idGuia}/{ciEntrega}")
	  void guardarCiEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("ciEntrega") String ciEntrega, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarNroFacturaEntrega/{idGuia}/{nroFacturaEntrega}")
	  void guardarNroFacturaEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("nroFacturaEntrega") String nroFacturaEntrega, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarNotaEntrega/{idGuia}/{notaEntrega}")
	  void guardarNotaEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("notaEntrega") String notaEntrega, MethodCallback<Void> call);
	  
//	  @PUT
//	  @Consumes("application/json")
//	  @Path("/guardarFechaEntrega/{idGuia}/{fechaEntrega}")
//	  void guardarFechaEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("fechaEntrega") String fechaEntrega, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarEntregaConsignatario/{idGuia}/{entregaConsignatario}")
	  void guardarEntregaConsignatario(@QueryParam("idGuia") Long idGuia, @QueryParam("entregaConsignatario") Boolean entregaConsignatario, MethodCallback<Void> call);
	  
	  @DELETE
	  @Path("/{id:[0-9]+}")
	  void borrar(@PathParam("id") Long id,  MethodCallback<Boolean> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/pagarOrigen/{idGuia}/{monto}/{glosa}")
	  void pagarOrigen(@QueryParam("idGuia") Long idGuia, @QueryParam("monto") Double monto, @QueryParam("glosa") String glosa, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/quitarPagoOrigen/{idGuia}")
	  void quitarPagoOrigen(Long idGuia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/pagarDestino/{idGuia}/{monto}/{glosa}")
	  void pagarDestino(@QueryParam("idGuia") Long idGuia, @QueryParam("monto") Double monto, @QueryParam("glosa") String glosa, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/quitarPagoDestino/{idGuia}")
	  void quitarPagoDestino(Long idGuia, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Path("/generarNroGuia/{idGuia}/")
	  void generarNroGuia(@QueryParam("idGuia") Long idGuia, MethodCallback<Integer> call);
	  
}
