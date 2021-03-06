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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.sinergia.dcargo.client.shared.dominio.Unidad;

/**
 * @author willy
 */
@Path("/unidad")
public interface ServicioUnidad {

  @PUT
  @Path("/getTodasUnidades")
  @Produces("application/json")
  List<Unidad> getTodasUnidades();
  
  @PUT
  @Consumes("application/json")
  void guardar(Unidad unidad) throws Exception;
  
}
