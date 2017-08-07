/**
 * Copyright (C) 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sinergia.dcargo.client.shared;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * @author willy
 */
@Path("/movimiento")
public interface ServicioMovimiento {

  @GET 
  @Path("/getEstados")
  @Produces("application/json")
  public List<String> getEstados() throws Exception;
  
  @PUT
  @Path("/buscarMovimientos")
  @Produces("application/json")
  @Consumes("application/json")
  List<Movimiento> buscarMovimientos(Movimiento cliente);
  
  @PUT
  @Produces("application/json")
  @Consumes("application/json")
  @Path("/nuevGuia")
  Movimiento nuevoMovimiento(TipoCuenta tipoCuenta) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarFechaRegistro/{idMovimiento}/{fechaRegistro}/{tipoCuenta}")
  void guardarFechaRegistro(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("fechaRegistro") Date fechaRegistro, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta) throws Exception;
 
  @PUT
  @Consumes("application/json")
  @Path("/guardarSubCuenta/{idMovimiento}/{idSubCuenta}/{tipoCuenta}")
  void guardarSubCuenta(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("idSubCuenta") Long idSubCuenta, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarGuia/{idMovimiento}/{idGuia}")
  void guardarGuia(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("idGuia") Long idGuia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarConocimiento/{idMovimiento}/{idConocimiento}")
  void guardarConocimiento(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("idGuia") Long idConocimiento) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarMonto/{idMovimiento}/{monto}/{tipoCuenta}")
  void guardarMonto(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("monto") Double monto, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarGlosa/{idMovimiento}/{glosa}/{tipoCuenta}")
  void guardarGlosa(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("glosa") String glosa, @QueryParam("tipoCuenta") TipoCuenta tipoCuenta) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/cambiarEstado/{idMovimiento}/{estado}")
  void cambiarEstado(@QueryParam("idMovimiento") Long idMovimiento, @QueryParam("estado")String estado) throws Exception;
  
}
