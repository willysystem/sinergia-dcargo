package com.sinergia.dcargo.client.shared.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * <div class="es">Objeto de transporte para guarda un valor fecha en formato 'EEEE dd/MM/yyyy'.</div>
 * 
 * <div class="en">Transport object to save value in 'EEEE dd/MM/yyyy' format.</div>    
 *   
 * @author willy
 *
 */
public class DateParam implements Serializable {
	
	private static final long serialVersionUID = -6517472773296676998L;

	private String formattedValue;
	
	private Date date;

	public String getFormattedValue() {
		return formattedValue;
	}

	public void setFormattedValue(String formattedValue) {
		this.formattedValue = formattedValue;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
}
