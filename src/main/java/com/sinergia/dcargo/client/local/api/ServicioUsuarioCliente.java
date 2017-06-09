package com.sinergia.dcargo.client.local.api;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import com.sinergia.dcargo.client.shared.DateParam;
import com.sinergia.dcargo.client.shared.Usuario;

@Path("/rest/user")
public interface ServicioUsuarioCliente extends RestService {
	
	@GET
	public void getUsers(MethodCallback<List<Usuario>> callback);
	
	@GET
	@Path("/currentUser")
	public void getCurrentUser(MethodCallback<Usuario> callback);
	
	@GET
	@Produces("application/json")
	@Path("/serverDate")
	public void getSeverDate(MethodCallback<DateParam> callback);
	
	@PUT
	@Consumes("application/json")
	void saveAll(List<Usuario> users, MethodCallback<Void> callback);
	
	@PUT
	@Produces("application/json")
	@Path("/newUser")
	void newUser(MethodCallback<Usuario> callback);
	
	@PUT
	@Consumes("application/json")
	@Path("/fijarContraseniaPorDefecto/{idUser}")
	void fijarContraseniaPorDefecto(@PathParam("idUser") Long idUser, MethodCallback<Usuario> callback);
	
	@PUT
	@Consumes("application/json")
	@Path("/cambiarContrasenia/{idUser}/{ultimaContrasenia}/{nuevaContrasenia}")
	void cambiarContrasenia(@PathParam("idUser") Long idUser, @PathParam("ultimaContrasenia") String ultimaContrasenia, @PathParam("nuevaContrasenia") String newContrasenia, MethodCallback<Usuario> callback);
	
}
