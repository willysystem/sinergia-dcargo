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

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 * @author willy
 */
@Path("/guia")
public interface ServicioGuia {

  @GET 
  @Path("/consultarGuia/{idGuia}")
  @Produces("application/json")
  Guia consultarGuia(@QueryParam("idGuia") Long idGuia);
  
  @PUT
  @Path("/buscarGuias")
  @Produces("application/json")
  List<Guia> buscarGuias(Guia cliente);
  
  @PUT
  @Consumes("application/json")
  void guardar(Guia guia) throws Exception;

  @PUT
  @Consumes("application/json")
  @Path("/nuevaGuia")
  Guia nuevaGuia() throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarNroFactura")
  void guardarNroFactura(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarRemitente")
  void guardarRemitente(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarConsignatario")
  void guardarConsignatario(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarOrigen")
  void guardarOrigen(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarDestino")
  void guardarDestino(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarAdjunto")
  void guardarAdjunto(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarResumen")
  void guardarResumen(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarNroEntrega")
  void guardarNroEntrega(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarPagoOrigen")
  void guardarPagoOrigen(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarPagoDestino")
  void guardarPagoDestino(Guia guia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/cambiarEstado/{idGuia}/{estado}")
  void cambiarEstado(@QueryParam("idGuia") Long idGuia, @QueryParam("estado")Boolean estado) throws Exception;
  
  @DELETE
  @Path("/{id:[0-9]+}")
  Boolean borrar(@PathParam("id") Long id);

}
