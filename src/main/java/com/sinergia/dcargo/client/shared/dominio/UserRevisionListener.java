package com.sinergia.dcargo.client.shared.dominio;

import java.util.Date;

import org.hibernate.envers.RevisionListener;

public class UserRevisionListener implements RevisionListener {
	
    
    @Override
    public void newRevision(Object revisionEntity) {
    	
        RevEntity exampleRevEntity = (RevEntity) revisionEntity;
        exampleRevEntity.setNombreUsuarioRev("user");
        exampleRevEntity.setFechaHoraRev(new Date().getTime());
    }
}