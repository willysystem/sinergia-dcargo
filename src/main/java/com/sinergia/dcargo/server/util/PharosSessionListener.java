package com.sinergia.dcargo.server.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class PharosSessionListener implements HttpSessionListener,
		HttpSessionAttributeListener {

	private  final String LoginInfoSID = "LoginInfoSID";
	
	
	
	public void sessionCreated(HttpSessionEvent arg0) {
		String sessionId = arg0.getSession().getId();
		//System.err.println("###### Listener : session create "+arg0.getSession().getId());
	    //sessionStatus.put(sessionId, new SessionInfo(arg0.getSession(),null,true));
		//PharosSessionManager.putSessionInfo(arg0.getSession(), null, true);
//	    System.err.println("###### Listener create : sessionStatus="+sessionStatus.size());
		
		System.out.println("--- sessionCreated: " + sessionId);
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {		
		
		String sessionId = sessionEvent.getSession().getId();
		//System.err.println("###### Listener : session destroyed"+arg0.getSession().getId());
		
//		CommonInfo commonInfo = (CommonInfo)arg0.getSession().getAttribute(LoginInfoSID);
//		SessionInfo info = PharosSessionManager.getSessionInfo(sessionId);
//		if(commonInfo == null){
//			commonInfo = new CommonInfo();
//			commonInfo.setUserCode(info.getUserCode());
//		}
//		PharosSessionManager.removeSessionInfo(sessionId);
//		if(commonInfo.getUserCode()==null){
//			return; 
//		}
//		
//		PharosSessionManager.sessionDestroyed(sessionId,commonInfo);
//		System.err.println("###### Listener destroyed : sessionStatus="+sessionStatus.size());
		
		//sessionEvent.getSession().getServletContext().get
		
		HttpSession session = sessionEvent.getSession();
		session.invalidate();
		
//		HttpSession session = sessionEvent.getSession(); 
//		System.out.println("--- sessionDestroyed: " + sessionId);
//		if (sessionEvent.getParameter("logout") != null) {  
//		    session.invalidate();
//		    response.sendRedirect("restanes.jsp");
//		    return; // <--- Here.
//		}
	}

	public void attributeAdded(HttpSessionBindingEvent arg0) {
//		System.err.println("###### Listener : session attributeAdded");
//		CommonInfo commonInfo = (CommonInfo)arg0.getSession().getAttribute(LoginInfoSID);
//		String name = arg0.getName();
//		Object value = arg0.getValue();
//		String sessionId = arg0.getSession().getId();
		
	//	System.out.println("Listener arg-name=" + name + ", arg-value=" + value);
	//	System.out.println("Listener commonInfo=" + commonInfo);
		
//		if(name.equals(LoginInfoSID)){
//			SessionInfo info = PharosSessionManager.getSessionInfo(sessionId);
//			if (info!=null) {
//				//System.out.println("Listener session-info=" + info);
//				if (commonInfo!=null) {
//					info.setUserCode(commonInfo.getUserCode());	
//				}
//			}
//		}
//		PharosSessionManager.attributeAdded(sessionId,commonInfo, name, value);
	}

	public void attributeRemoved(HttpSessionBindingEvent arg0) {
		String sessionId = arg0.getSession().getId();
		//System.err.println("###### Listener : session removed "+sessionId);
//		SessionInfo info = PharosSessionManager.getSessionInfo(sessionId);
//		boolean isValid = info==null?false:info.isValid();
//			
//		if(!isValid){
//			return ;
//		}
//		CommonInfo commonInfo = (CommonInfo)arg0.getSession().getAttribute(LoginInfoSID);
//		if(commonInfo == null){
//			//System.err.println(" info es null adicionando "+info.getUserCode());
//			commonInfo = new CommonInfo();
//			commonInfo.setUserCode(info.getUserCode());
//			
//		}
//		if(commonInfo.getUserCode()==null){
//			return;
//		}
//		
//		
//		String name = arg0.getName();
//		PharosSessionManager.attributeRemoved(commonInfo, name);
	}

	public void attributeReplaced(HttpSessionBindingEvent arg0) {
	//	System.err.println("###### Listener : session replaced");
//		CommonInfo commonInfo = (CommonInfo)arg0.getSession().getAttribute(LoginInfoSID);
//		String name = arg0.getName();
//		Object value = arg0.getValue();
//		String sessionId = arg0.getSession().getId();
//		PharosSessionManager.attributeReplaced(sessionId,commonInfo, name, value);
	}
	

	

}
