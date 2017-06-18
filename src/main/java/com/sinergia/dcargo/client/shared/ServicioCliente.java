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
@Path("/cliente")
public interface ServicioCliente {
	
  @GET
  @Produces("application/json")
  List<Cliente> getTodos();
  
  @PUT
  @Path("/buscarClientes")
  @Produces("application/json")
  List<Cliente> buscarClientes(Cliente cliente);
  
  @PUT
  @Path("/nuevoCliente")
  @Produces("application/json")
  Cliente nuevoCliente();
  
  @PUT
  @Produces("application/json")
  @Path("/guardarNombres/{idCliente}/{nombres}/{nit}")
  void guardarNombres(@QueryParam("idCliente") Long idCliente, @QueryParam("nombres") String nombres) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarDireccion/{idCliente}/{direccion}")
  void guardarDireccion(@QueryParam("idCliente") Long idCliente, @QueryParam("direccion") String direccion) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarTelefono/{idCliente}/{telefono}")
  void guardarTelefono(@QueryParam("idCliente") Long idCliente, @QueryParam("telefono") String telefono) throws Exception;
 
  @PUT
  @Produces("application/json")
  @Path("/guardarNit/{idCliente}/{nit}/{nombres}")
  void guardarNit(@QueryParam("idCliente") Long idCliente, @QueryParam("nit") String nit) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/guardarNit/{idCliente}/{ci}")
  void guardarCi(@QueryParam("idCliente") Long idCliente, @QueryParam("ci") String ci) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/cambiarEstado/{idCliente}/{estado}")
  void cambiarEstado(@QueryParam("idCliente") Long idCliente, @QueryParam("estado") String estado) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/esUnicoNombreCon/{nombre}")
  public Resultado esUnicoNombreCon(@QueryParam("nombre") String nombre) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/esUnicoNitCon/{nit}")
  public Resultado esUnicoNitCon(@QueryParam("nit") String nit) throws Exception;
  
  @PUT
  @Consumes("application/json")
  void guardar(Cliente cliente) throws Exception;
  
  @DELETE
  @Path("/{id:[0-9]+}")
  Boolean borrar(@PathParam("id") Long id);
  
  @PUT
  @Produces("application/json")
  List<Cliente> buscarClientesPorNombre(String nombre);

}
