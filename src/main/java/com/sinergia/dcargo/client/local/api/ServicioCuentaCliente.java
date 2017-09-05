package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.Resultado;
import com.sinergia.dcargo.client.shared.dominio.CuentaEgreso;
import com.sinergia.dcargo.client.shared.dominio.CuentaIngreso;
import com.sinergia.dcargo.client.shared.dominio.TipoCuenta;

@Path("/rest/cuentas")
public interface ServicioCuentaCliente extends RestService {
	
	  // EGRESO
	  @PUT
	  @Path("/nuevoCuentaEgreso")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void nuevaCuentaEgreso(MethodCallback<CuentaEgreso> call);
	  
	  // INGRESO
		
	  @PUT
	  @Path("/nuevoCuentaIngreso")
	  //@Consumes("application/json")
	  @Produces("application/json")
	  public void nuevoCuentaIngreso(MethodCallback<CuentaIngreso> call);

	  @GET
	  @Path("/getTodasCuentasIngreso")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void getTodasCuentasIngreso(MethodCallback<List<CuentaIngreso>> call);
	  
	  @GET
	  @Path("/getTodasCuentasEgreso")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void getTodasCuentasEgreso(MethodCallback<List<CuentaEgreso>> call);
	  
	  @GET
	  @Path("/getSubCuentasIngreso/{cuentaIngresoId}")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void getSubCuentasIngreso(@QueryParam("cuentaIngresoId") Long cuentaIngresoId, MethodCallback<List<CuentaIngreso>> call);
	  
	  @GET
	  @Path("/getSubCuentasEgreso/{cuentaEgresoId}")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void getSubCuentasEgreso(@QueryParam("cuentaEgresoId") Long cuentaEgresoId, MethodCallback<List<CuentaEgreso>> call);
	  
	  // EGRESO E INGRESO
	  
	  @PUT
	  @Path("/guardarNroCuenta/{cuentaId}/{nroCuenta}/{tipoCuenta}")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void guardarNroCuenta(@QueryParam("cuentaId") Long cuentaId, @QueryParam("nroCuenta") Integer nroCuenta, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta, MethodCallback<Void> call);
	  
	  @PUT
	  @Path("/guardarDescripcion/{cuentaId}/{descripcion}/{tipoCuenta}")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void guardarDescripcion(@QueryParam("cuentaId") Long cuentaId, @QueryParam("descripcion") String descripcion, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta, MethodCallback<Void> call);
	  
	  @PUT
	  @Path("/guardarCuentaPadre/{cuentaId}/{idPadre}/{tipoCuenta}")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void guardarCuentaPadre(@QueryParam("cuentaId") Long cuentaId, @QueryParam("idPadre") Long idPadre, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta,  MethodCallback<Void> call);
	  
	  @DELETE
	  @Path("/borrarCuenta/{cuentaId}")
	  @Consumes("application/json")
	  @Produces("application/json")
	  public void borrarCuenta(@QueryParam("cuentaId") Long cuentaId, MethodCallback<Void> call);
	  
	  @PUT
	  @Produces("application/json")
	  @Consumes("application/json")
	  @Path("/esUnicoNroCuentaCon/{type}/{nroCuenta}")
	  public void esUnicoNroCuentaCon(@QueryParam("type") TipoCuenta type, @QueryParam("nroCuenta") Integer nroCuenta, MethodCallback<Resultado> call);
	  
}
