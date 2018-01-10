package com.sinergia.dcargo.client.shared.dominio;

import java.util.Date;

import org.hibernate.envers.RevisionListener;
import org.jboss.security.SecurityContext;
import org.jboss.security.SecurityContextAssociation;

public class UserRevisionListener implements RevisionListener {
	
    @Override
    public void newRevision(Object revisionEntity) {
    	
    	SecurityContext sc = SecurityContextAssociation.getSecurityContext();
    	String userName = sc.getUtil().getUserName(); 
    	
        RevEntity exampleRevEntity = (RevEntity) revisionEntity;
        exampleRevEntity.setNombreUsuarioRev(userName);
        exampleRevEntity.setFechaHoraRev(new Date().getTime());
        
    }
}