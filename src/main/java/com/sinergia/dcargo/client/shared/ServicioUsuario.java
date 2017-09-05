package com.sinergia.dcargo.client.shared;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.sinergia.dcargo.client.shared.dominio.Usuario;

/**
 * @author willy
 */
@Path("/user")
public interface ServicioUsuario {

  @GET
  @Produces("application/json")
  List<Usuario> getAllUsers();
  
  @GET
  @Produces("application/json")
  @Path("/currentUser")
  Usuario getCurrentUser();
  
  @PUT
  @Consumes("application/json")
  void saveAll(List<Usuario> users) throws Exception;
  
  @PUT
  @Produces("application/json")
  @Path("/newUser")
  Usuario newUser();
  
  @GET
  @Produces("application/json")
  @Path("/serverDate")
  DateParam getSeverDate();
  
  @PUT
  @Consumes("application/json")
  @Path("/fijarContraseniaPorDefecto/{idUser}")
  void fijarContraseniaPorDefecto(@PathParam("idUser") Long idUser);
  
  @PUT
  @Consumes("application/json")
  @Path("/cambiarContrasenia/{idUser}/{ultimaContrasenia}/{nuevaContrasenia}")
  void cambiarContrasenia(@PathParam("idUser") Long idUser, @PathParam("ultimaContrasenia") String ultimaContrasenia, @PathParam("nuevaContrasenia") String newContrasenia)  throws Exception;
  
  /**
   * @param id
   *          The id number of an existing {@link Contact} to be deleted.
   * @return A {@link Response} with status 204 if successful. Otherwise a {@link Response} with an appropriate error
   *         status.
   */
  @DELETE
  @Path("/{id:[0-9]+}")
  void delete(@PathParam("id") Long id);

}
