package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.dominio.Movimiento;
import com.sinergia.dcargo.client.shared.dominio.MovimientoEgreso;
import com.sinergia.dcargo.client.shared.dominio.MovimientoIngreso;
import com.sinergia.dcargo.client.shared.dominio.TipoCuenta;
import com.sinergia.dcargo.client.shared.dto.DeudasPorCobrarReporte;
import com.sinergia.dcargo.client.shared.dto.DeudasReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionCargaReporte;
import com.sinergia.dcargo.client.shared.dto.LiquidacionReporte;

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
	 @Path("/nuevoMovimientoIngreso")
	 void nuevoMovimientoIngreso(MethodCallback<MovimientoIngreso> call);
	 
	 @PUT
	 @Produces("application/json")
	 @Consumes("application/json")
	 @Path("/nuevoMovimientoEgreso")
	 void nuevoMovimientoEgreso(MethodCallback<MovimientoEgreso> call);
	  
	 @PUT
	 @Consumes("application/json")
	 @Path("/guardarFechaRegistro/{idMovimiento}/{fechaRegistro}/{tipoCuenta}")
	 void guardarFechaRegistro(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("fechaRegistro") Long fechaRegistro, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta, MethodCallback<Void> call);
	 
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
	  
	  @PUT
	  @Consumes("application/json")
	  @Produces("application/json")
	  @Path("/reporteLiquidacionCarga/")
	  void reporteLiquidacionCarga(LiquidacionReporte liquidacionReporte, MethodCallback<LiquidacionCargaReporte> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Produces("application/json")
	  @Path("/reporteDeudasPorCobrar/")
	  void reporteDeudasPorCobrar(DeudasReporte deudasReporte, MethodCallback<DeudasPorCobrarReporte> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Produces("application/json")
	  @Path("/generarNroComprobanteIngreso/{idMovimiento}/")
	  void generarNroComprobanteIngreso(@QueryParam("idMovimiento") Long idMovimiento, MethodCallback<Integer> call);
	  
	  @PUT
	  @Consumes("application/json")
	  @Produces("application/json")
	  @Path("/generarNroComprobanteEgreso/{idMovimiento}/")
	  void generarNroComprobanteEgreso(@QueryParam("idMovimiento") Long idMovimiento, MethodCallback<Integer> call);
	  
	  
}
