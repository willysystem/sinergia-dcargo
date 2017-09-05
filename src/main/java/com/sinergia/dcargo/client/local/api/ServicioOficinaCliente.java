package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.dominio.Oficina;

@Path("/rest/office")
public interface ServicioOficinaCliente extends RestService {
	
	@GET
	public void getOffices(MethodCallback<List<Oficina>> callback);
		
}
