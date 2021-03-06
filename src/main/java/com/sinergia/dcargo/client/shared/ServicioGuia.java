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

import org.fusesource.restygwt.client.MethodCallback;

import com.sinergia.dcargo.client.shared.dominio.EstadoGuia;
import com.sinergia.dcargo.client.shared.dominio.Guia;

/**
 * @author willy
 */
@Path("/guia")
public interface ServicioGuia {

  @GET 
  @Path("/consultarGuia/{idGuia}")
  @Produces("application/json")
  Guia consultarGuia(@QueryParam("idGuia") Long idGuia);
  
  @GET 
  @Path("/getEstados")
  @Produces("application/json")
  @Consumes("application/json")
  public List<EstadoGuia> getEstados() throws Exception;
  
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
  @Path("/cambiarEstado/{idGuia}/{estadoDescripcion}")
  void cambiarEstado(@QueryParam("idGuia") Long idGuia, @QueryParam("estadoDescripcion")String estadoDescripcion) throws Exception;
 
  
  @PUT
  @Consumes("application/json")
  @Produces("application/json")
  @Path("/guardartotal/{idGuia}/{total}")
  void guardartotal(@QueryParam("idGuia") Long idGuia, @QueryParam("total") Double total) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarPesoTotal/{idGuia}/{pesoTotal}")
  void guardarPesoTotal(@QueryParam("idGuia") Long idGuia, @QueryParam("pesoTotal") Double pesoTotal) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarBultosTotal/{idGuia}/{bultosTotal}")
  void guardarBultosTotal(@QueryParam("idGuia") Long idGuia, @QueryParam("bultosTotal") Integer bultosTotal) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarNombreClienteEntrega/{idGuia}/{nombreClienteEntrega}")
  void guardarNombreClienteEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("nombreClienteEntrega") String nombreClienteEntrega) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarCiEntrega/{idGuia}/{ciEntrega}")
  void guardarCiEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("ciEntrega") String ciEntrega) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarNroFacturaEntrega/{idGuia}/{nroFacturaEntrega}")
  void guardarNroFacturaEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("nroFacturaEntrega") String nroFacturaEntrega) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarNotaEntrega/{idGuia}/{notaEntrega}")
  void guardarNotaEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("notaEntrega") String notaEntrega) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarNotaEntrega/{idGuia}/{nroNotaEntrega}")
  void guardarNroNotaEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("nroNotaEntrega") String nroNotaEntrega) throws Exception;
  
//  @PUT
//  @Consumes("application/json")
//  @Path("/guardarFechaEntrega/{idGuia}/{fechaEntrega}")
//  void guardarFechaEntrega(@QueryParam("idGuia") Long idGuia, @QueryParam("fechaEntrega") DateParam fechaEntrega) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarEntregaConsignatario/{idGuia}/{entregaConsignatario}")
  void guardarEntregaConsignatario(@QueryParam("idGuia") Long idGuia, @QueryParam("entregaConsignatario") Boolean entregaConsignatario) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/guardarObservaciones/{idGuia}/{observaciones}")
  void guardarObservaciones(@QueryParam("idGuia") Long idGuia, @QueryParam("observaciones") String observaciones) throws Exception;
  
  @DELETE
  @Path("/{id:[0-9]+}")
  Boolean borrar(@PathParam("id") Long id);

  @PUT
  @Consumes("application/json")
  @Produces("application/json")
  @Path("/pagarOrigen/{idGuia}/{monto}/{glosa}")
  public Integer pagarOrigen(@QueryParam("idGuia") Long idGuia, @QueryParam("monto") Double monto, @QueryParam("glosa") String glosa) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/quitarPagoOrigen/{idGuia}")
  public void quitarPagoOrigen(Long idGuia) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/pagarDestino/{idGuia}/{monto}/{glosa}")
  public Integer pagarDestino(@QueryParam("idGuia") Long idGuia, @QueryParam("monto") Double monto, @QueryParam("glosa") String glosa) throws Exception;
  
  @PUT
  @Consumes("application/json")
  @Path("/quitarPagoDestino/{idGuia}")
  public void quitarPagoDestino(Long idGuia) throws Exception;

  @PUT
  @Produces("application/json")
  @Path("/generarNroGuia/{idGuia}")
  public Integer generarNroGuia(@QueryParam("idGuia") Long idGuia) throws Exception;
  
  public Guia serializarParaBusqueda(Guia guiaP);
 
  
}
