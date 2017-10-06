package com.sinergia.dcargo.client.shared.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

//@MappedSuperclass
@Entity
@RevisionEntity(UserRevisionListener.class)
@Table(name="rev_entity")
public class RevEntity {
	
	
	@Id
    @GeneratedValue
    @RevisionNumber
    private int id;
	
	private String nombreUsuarioRev;
	
	@RevisionTimestamp
	private long fechaHoraRev;

	public String getNombreUsuarioRev() {
		return nombreUsuarioRev;
	}

	public void setNombreUsuarioRev(String nombreUsuarioRev) {
		this.nombreUsuarioRev = nombreUsuarioRev;
	}

	public long getFechaHoraRev() {
		return fechaHoraRev;
	}

	public void setFechaHoraRev(long fechaHoraRev) {
		this.fechaHoraRev = fechaHoraRev;
	}
   
    
}