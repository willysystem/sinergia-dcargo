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
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.sinergia.dcargo.client.shared.dominio.Conocimiento;
import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Guia;

/**
 * @author willy
 */
@Path("/conocimiento")
public interface ServicioConocimiento {

  @PUT 
  @Path("/buscarConocimiento")
  @Consumes("application/json")
  public List<Conocimiento> buscarConocimiento(Conocimiento conocimiento);	
	
  @GET 
  @Path("/consultarConocimiento/{idConocimiento}")
  @Produces("application/json")
  public Conocimiento consultarConocimiento(@QueryParam("idConocimiento") Long idConocimiento);
  
  @GET 
  @Path("/getEstados")
  @Produces("application/json")
  public List<EstadoGuia> getEstados() throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/nuevaConocimiento")
  public Conocimiento nuevoConocimiento() throws Exception;
  
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarFechaRegistro/{idConocimiento}/{fechaRegistro}")
  public void guardarFechaRegistro(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("fechaRegistro") Date fechaRegistro) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarPropietario/{idConocimiento}/{idPropietario}")
  public void guardarPropietario(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idPropietario") Long idPropietario) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarConductor/{idConocimiento}/{idConductor}")
  public void guardarConductor(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idConductor") Long idConductor) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarOficinaOrigen/{idConocimiento}/{idOficinaOrigen}")
  public void guardarOficinaOrigen(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idOficinaOrigen") Long idOficinaOrigen) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarOficinaDestino/{idConocimiento}/{idOficinaDestino}")
  public void guardarOficinaDestino(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idOficinaDestino") Long idOficinaDestino) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarMulta/{idConocimiento}/{multa}")
  public void guardarMulta(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("multa") Double multa) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarDias/{idConocimiento}/{dias}")
  public void guardarDias(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("dias") Integer dias) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarObservacion/{idConocimiento}/{observacion}")
  public void guardarObservacion(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("observacion") String observacion) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarAdjunto/{idConocimiento}/{adjunto}")
  public void guardarAdjunto(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("adjunto") String adjunto) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarAdjunto2/{idConocimiento}/{adjunto2}")
  public void guardarAdjunto2(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("adjunto2") String adjunto2) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarAclaracion/{idConocimiento}/{aclaracion}")
  public void guardarAclaracion(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("aclaracion") String aclaracion) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarAclaracion2/{idConocimiento}/{aclaracion2}")
  public void guardarAclaracion2(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("aclaracion2") String aclaracion2) throws Exception;
 
  @PUT
  @Consumes("application/json")
  @Path("/guardarFlete/{idConocimiento}/{flete}")
  public void guardarFlete(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("flete") Double flete) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Produces("application/json")
  @Path("/guardarAcuenta/{idConocimiento}/{acuenta}/{guardarAcuenta}")
  public void guardarAcuenta(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("acuenta") Double acuenta, @QueryParam("guardarAcuenta") Boolean guardarAcuenta) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarPagoOrigen/{idConocimiento}/{pagoOrigen}")
  public void guardarPagoOrigen(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("pagoOrigen") Double pagoOrigen) throws Exception;
  
  @PUT
  @Consumes("application/json")  
  @Path("/guardarPagoDestino/{idConocimiento}/{pagoDestino}")
  public void guardarPagoDestino(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("pagoDestino") Double pagoDestino) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/cambiarEstado/{idConocimiento}/{estado}")
  public void cambiarEstado(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("estado")String estado) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/adicionarGuia/{idConocimiento}/{idGuia}")
  public void adicionarGuia(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idGuia") Long idGuia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/quitarGuia/{idConocimiento}/{idGuia}")
  public void quitarGuia(@QueryParam("idConocimiento") Long idConocimiento, @QueryParam("idGuia") Long idGuia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Produces("application/json")
  @Path("/getGuias/{idConocimiento}")
  public List<Guia> getGuias(@QueryParam("idConocimiento") Long idConocimiento) throws Exception;
  
  @DELETE
  @Path("/{id:[0-9]+}")
  public Resultado borrar(@PathParam("id") Long id) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/generarNroConocimiento/{idConocimiento}")
  public Integer generarNroConocimiento(@QueryParam("idConocimiento") Long idConocimiento);

}
