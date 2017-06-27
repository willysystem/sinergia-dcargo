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
@Path("/transportistas")
public interface ServicioTransportista {
	
  @GET
  @Produces("application/json")
  public List<Transportista> getTodos();
  
  @PUT
  @Path("/buscarTransportista")
  @Produces("application/json")
  public List<Transportista> buscarTransportista(Transportista transportista);
  
  @PUT
  @Path("/nuevoTransportista")
  @Produces("application/json")
  public Transportista nuevoTransportista();
  
  @PUT
  @Produces("application/json")
  @Path("/guardarBrevet/{idTransportista}/{brevet}")
  public void guardarBrevet(@QueryParam("idTransportista") Long idTransportista, @QueryParam("brevet") String brevet) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarNombre/{idTransportista}/{nombre}")
  public void guardarNombre(@QueryParam("idTransportista") Long idTransportista, @QueryParam("nombre") String nombre) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarDireccion/{idTransportista}/{direccion}")
  public void guardarDireccion(@QueryParam("idTransportista") Long idTransportista, @QueryParam("direccion") String direccion) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarTelefono/{idTransportista}/{telefono}")
  public void guardarTelefono(@QueryParam("idTransportista") Long idTransportista, @QueryParam("telefono") String telefono) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarPlaca/{idTransportista}/{placa}")
  public void guardarPlaca(@QueryParam("idTransportista") Long idTransportista, @QueryParam("placa") String placa) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarMarca/{idTransportista}/{marca}")
  void guardarMarca(@QueryParam("idTransportista") Long idTransportista, @QueryParam("marca") String marca) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarColor/{idTransportista}/{color}")
  public void guardarColor(@QueryParam("idTransportista") Long idTransportista, @QueryParam("color") String color) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarCapacidad/{idTransportista}/{capacidad}")
  public void guardarCapacidad(@QueryParam("idTransportista") Long idTransportista, @QueryParam("capacidad") String capacidad) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarVecinoDe/{idTransportista}/{vecinoDe}")
  public void guardarVecinoDe(@QueryParam("idTransportista") Long idTransportista, @QueryParam("vecinoDe") String vecinoDe) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarNit/{idTransportista}/{ci}")
  public void guardarCi(@QueryParam("idTransportista") Long idTransportista, @QueryParam("ci") String ci) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarNit/{idTransportista}/{nit}/{nombres}")
  public void guardarNit(@QueryParam("idTransportista") Long idTransportista, @QueryParam("nit") String nit) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/cambiarEstado/{idTransportista}/{estado}")
  public void cambiarEstado(@QueryParam("idTransportista") Long idTransportista, @QueryParam("estado") String estado) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/esUnicoNombreCon/{nombre}")
  public Resultado esUnicoBrevetCon(@QueryParam("nombre") String nombre) throws Exception;
  
  @DELETE
  @Path("/{id:[0-9]+}")
  public void borrar(@PathParam("id") Long id);
  
}
