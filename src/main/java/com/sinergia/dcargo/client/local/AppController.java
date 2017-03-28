package com.sinergia.dcargo.client.local;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.sinergia.dcargo.client.local.event.LoginEvent;
import com.sinergia.dcargo.client.local.event.LoginEventHandler;
import com.sinergia.dcargo.client.local.event.UserMainEvent;
import com.sinergia.dcargo.client.local.event.UserMainEventHandler;
import com.sinergia.dcargo.client.local.presenter.LoginPresenter;
import com.sinergia.dcargo.client.local.presenter.MainContentPresenter;
import com.sinergia.dcargo.client.local.presenter.Presenter;
import com.sinergia.dcargo.client.local.presenter.UserMainPresenter;

@ApplicationScoped
public class AppController implements com.sinergia.dcargo.client.local.presenter.Presenter, ValueChangeHandler<String> {
	
//  @Inject
//  private IOCBeanManager manager;

  @Inject
  private HandlerManager eventBus;

  private HasWidgets container;
  
  @Inject
  private LoginPresenter loginPresenter;
  
  @Inject
  private MainContentPresenter mainContentPresenter;
  
  @Inject
  private UserMainPresenter userMainPresenter;
  
  public void bind() {
	  
    History.addValueChangeHandler(this);

    eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {
		@Override
		public void onLogin(LoginEvent event) {
		    doLogined();	
		}
	});
    
    eventBus.addHandler(UserMainEvent.TYPE, new UserMainEventHandler() {
		@Override
		public void onLogin(UserMainEvent event) {
			History.newItem("users");
		}
	});   
    
//
//    eventBus.addHandler(EditContactEvent.TYPE, new EditContactEventHandler() {
//      public void onEditContact(EditContactEvent event) {
//        doEditContact(event.getId());
//      }
//    });
//
//    eventBus.addHandler(EditContactCancelledEvent.TYPE,
//        new EditContactCancelledEventHandler() {
//          public void onEditContactCancelled(EditContactCancelledEvent event) {
//            doEditContactCancelled();
//          }
//        });
//
//    eventBus.addHandler(ContactUpdatedEvent.TYPE,
//        new ContactUpdatedEventHandler() {
//          public void onContactUpdated(ContactUpdatedEvent event) {
//            doContactUpdated();
//          }
//        });
  }

//  private void doLogin() {
//	 History.newItem("login");
//  }
  
  private void doLogined(){
	  History.newItem("logined");  
  }
  
  private void doAddNewContact() {
	  History.newItem("add");
  }

  private void doEditContact(String id) {
	  History.newItem("edit", false);
    
//    IOCBeanDef<EditContactPresenter> bean = manager.lookupBean(EditContactPresenter.class);
//    
//    EditContactPresenter presenter = null;
//    if (bean != null) {
//      presenter = bean.getInstance();
//    }
//    
//    if (presenter != null) {
//      presenter.go(container, id);
//    }
  }

  private void doEditContactCancelled() {
    History.newItem("list");
  }

  private void doContactUpdated() {
    History.newItem("list");
  }

  public void go(final HasWidgets container) {
    this.container = container;
    bind();
   
    if ("".equals(History.getToken())) {
      History.newItem("login");
    } else {
      History.fireCurrentHistoryState();
    }
  }

  public void onValueChange(ValueChangeEvent<String> event) {
    String token = event.getValue();
    if (token != null) {
      Presenter presenter = null;

      if (token.equals("login")) {
        //IOCBeanDef<ContactsPresenter> bean = manager.lookupBean(ContactsPresenter.class);
        //if (bean != null) {
          presenter = loginPresenter;
        //}
      } else if (token.equals("logined")) {
    	  presenter =  mainContentPresenter;
      } else if (token.equals("users")) {
    	  presenter =  userMainPresenter;
      }         
      
      
//    } else if (token.equals("add") || token.equals("edit")) {
//        IOCBeanDef<EditContactPresenter> bean = manager.lookupBean(EditContactPresenter.class);
//        if (bean != null) {
//          presenter = bean.getInstance();
//        }
//    }
     
      
      if (presenter != null) {
        presenter.go(container);
      }
    }
  }
}