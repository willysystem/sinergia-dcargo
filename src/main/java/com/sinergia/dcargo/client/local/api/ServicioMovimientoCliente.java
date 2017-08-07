package com.sinergia.dcargo.client.local.api;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.Movimiento;
import com.sinergia.dcargo.client.shared.TipoCuenta;

@Path("/rest/movimiento")
public interface ServicioMovimientoCliente extends RestService {
	 @GET 
	 @Path("/getEstados")
	 @Produces("application/json")
	 public void getEstados(MethodCallback<List<String>> call);
	  
	 @PUT
	 @Path("/buscarMovimientos")
	 @Produces("application/json")
	 @Consumes("application/json")
	 void buscarMovimientos(Movimiento cliente, MethodCallback<List<Movimiento>> call);
	  
	 @PUT
	 @Produces("application/json")
	 @Consumes("application/json")
	 @Path("/nuevGuia")
	 void nuevoMovimiento(TipoCuenta tipoCuenta, MethodCallback<Movimiento> call);
	  
	 @PUT
	 @Consumes("application/json")
	 @Path("/guardarFechaRegistro/{idMovimiento}/{fechaRegistro}/{tipoCuenta}")
	 void guardarFechaRegistro(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("fechaRegistro") Date fechaRegistro, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta, MethodCallback<Void> call);
	 
	 @PUT
	 @Consumes("application/json")
	 @Path("/guardarSubCuenta/{idMovimiento}/{idSubCuenta}/{tipoCuenta}")
	 void guardarSubCuenta(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("idSubCuenta") Long idSubCuenta, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarGuia/{idMovimiento}/{idGuia}")
	  void guardarGuia(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("idGuia") Long idGuia, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarConocimiento/{idMovimiento}/{idConocimiento}")
	  void guardarConocimiento(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("idGuia") Long idConocimiento, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarMonto/{idMovimiento}/{monto}/{tipoCuenta}")
	  void guardarMonto(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("monto") Double monto, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/guardarGlosa/{idMovimiento}/{glosa}/{tipoCuenta}")
	  void guardarGlosa(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("glosa") String glosa, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta, MethodCallback<Void> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Path("/cambiarEstado/{idMovimiento}/{estado}")
	  void cambiarEstado(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("estado")String estado, MethodCallback<Void> call);
	  
}
