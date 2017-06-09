package com.sinergia.dcargo.client.shared;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author willy
 */
@Path("/office")
public interface OficinaServicio {

	@GET
	@Produces("application/json")
	List<Oficina> getAllOffices();

	public List<Oficina> buscarOficinaPorNombre(String nombre);
	
}
