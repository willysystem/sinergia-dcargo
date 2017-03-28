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
//package com.sinergia.dcargo.server;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.ejb.Stateless;
//import javax.ejb.TransactionAttribute;
//import javax.ejb.TransactionAttributeType;
//
//import com.sinergia.dcargo.client.shared.Usuario;
//
///**
// * A service that provides transaction boundaries around CRUD operations on {@link Contact Contacts}.
// */
//@Stateless
//@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
//public class ContactEntityService {
//
////  @PersistenceContext(unitName = "forge-default")
////  private EntityManager em;
//
//  public List<Usuario> getAllContacts() {
//    Usuario usuario = new Usuario();
//    usuario.setUsuario("whurtado");
//    
//    List<Usuario> list = new ArrayList<Usuario>();
//    list.add(usuario);
//    
//    return list;
//	  
//  }
//  
//}
