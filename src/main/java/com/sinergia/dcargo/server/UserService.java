//package com.sinergia.dcargo.server;
//
//import java.util.Date;
//import java.util.List;
//
//import javax.inject.Inject;
//
//import org.jboss.errai.bus.client.ErraiBus;
//import org.jboss.errai.bus.client.api.SubscribeListener;
//import org.jboss.errai.bus.client.api.base.MessageBuilder;
//import org.jboss.errai.bus.client.api.messaging.MessageBus;
//import org.jboss.errai.bus.client.api.messaging.RequestDispatcher;
//import org.jboss.errai.bus.client.framework.SubscriptionEvent;
//import org.jboss.errai.bus.server.annotations.Command;
//import org.jboss.errai.bus.server.annotations.Service;
//import org.jboss.errai.common.client.api.ResourceProvider;
//import org.jboss.errai.common.client.api.tasks.AsyncTask;
//import org.jboss.errai.common.client.util.TimeUnit;
//
//import com.sinergia.dcargo.client.shared.User;
//
//@Service
//public class UserService {
//	
//	private volatile AsyncTask task;
//	
//	@Inject
//	public UserService(final RequestDispatcher dispatcher, final MessageBus bus) {
//		bus.addSubscribeListener(new SubscribeListener() {
//			@Override
//			public void onSubscribe(SubscriptionEvent event) {
//				if(event.getSubject().equals("UserService")) {
//					if(task == null || task.isCancelled()) {
//						task = MessageBuilder.createMessage()
//							    .toSubject("UserService")
//							    .command("listUsers")
//							    .withProvided("Data", new ResourceProvider<List<User>>() {
//									@Override
//									public List<User> get() {
//										// TODO Auto-generated method stub
//										return null;
//									}
//								}).noErrorHandling()
//							      .sendRepeatingWith(dispatcher, TimeUnit.MILLISECONDS, 50);
//							     
//					}		
//				}
//			}
//		});
//	}
//	
//	@Command("GetUsers")
//    private void getUsers() {
//		System.out.println("getUsers: " + new Date(System.currentTimeMillis()));
//	}
//	
//	
//}
