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
//import java.util.List;
//
//import javax.ejb.Stateless;
//import javax.enterprise.event.Event;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.ws.rs.core.Response;
//
//import com.sinergia.dcargo.client.shared.ContactStorageService;
//import com.sinergia.dcargo.client.shared.User;
//import com.sinergia.dcargo.client.shared.UserService;
//
///**
// * Server-side implementation for the RPC service,
// * {@link ContactStorageService}. Performs database CRUD operations using the
// * {@link ContactEntityService} and fires Errai CDI {@link Event Events} that
// * are observed by clients over the wire to publish creation, update, and
// * deletion of {@link Contact Contacts}.
// */
//@Stateless
//public class ContactStorageServiceImpl implements UserService {
//
//	@PersistenceContext(unitName = "dCargoUnit")
//	private EntityManager em;
//	
//	@Override
//	public List<User> getAllUsers() {
//		Query query = em.createQuery("SELECT u FROM User u");
//		return query.getResultList();
//	}
//	
//	@Override
//	public Response delete(Long id) {
//		
//		return null;
//	}
//
//	
//
//}
