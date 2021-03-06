///**
// * Copyright (C) 2016 Red Hat, Inc. and/or its affiliates.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.sinergia.dcargo.client.shared;
//
//import java.util.List;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//
///**
// * @author willy
// */
//@Path("/precio")
//public interface ServicioCuentaIngreso {
//
//  // INGRESO
//	
//  @GET
//  @Path("/getTodosCuentasIngreso")
//  @Produces("application/json")
//  public List<CuentaIngreso> getTodosCuentasIngreso() throws Exception;
//  
//  @PUT
//  @Path("/nuevoCuentaIngreso")
//  @Consumes("application/json")
//  @Produces("application/json")
//  public CuentaIngreso nuevoCuentaIngreso() throws Exception;
//
//  // EGRESO E INGRESO
// 
//  @PUT
//  @Path("/guardarNroCuenta/{cuentaId}/{nroCuenta}")
//  @Consumes("application/json")
//  @Produces("application/json")
//  public void guardarNroCuenta(@QueryParam("cuentaId") Long cuentaId, @QueryParam("nroCuenta") Integer nroCuenta) throws Exception;
//  
//  @PUT
//  @Path("/guardarDescripcion/{cuentaId}/{descripcion}")
//  @Consumes("application/json")
//  @Produces("application/json")
//  public void guardarDescripcion(@QueryParam("cuentaId") Long cuentaId, @QueryParam("descripcion") String descripcion) throws Exception;
//  
//  @DELETE
//  @Path("/borrarCuenta/{cuentaId}")
//  @Consumes("application/json")
//  @Produces("application/json")
//  public void borrarCuenta(@QueryParam("cuentaId") Long cuentaId) throws Exception;
//  
//}
