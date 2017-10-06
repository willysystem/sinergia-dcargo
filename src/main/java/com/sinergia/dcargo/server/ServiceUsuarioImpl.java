package com.sinergia.dcargo.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.sinergia.dcargo.client.shared.ServicioUsuario;
import com.sinergia.dcargo.client.shared.dominio.Aplicacion;
import com.sinergia.dcargo.client.shared.dominio.Oficina;
import com.sinergia.dcargo.client.shared.dominio.Rol;
import com.sinergia.dcargo.client.shared.dominio.Usuario;
import com.sinergia.dcargo.client.shared.dto.DateParam;
import com.sinergia.dcargo.server.dao.Dao;
import com.sinergia.dcargo.server.util.MD5;

/**
 * 
 * @author willy
 */
@Stateless
public class ServiceUsuarioImpl extends Dao<Usuario> implements ServicioUsuario {

	public ServiceUsuarioImpl() {
		super(Usuario.class);
	}
	
	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Usuario> getAllUsers() {
		Query query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
		List<Usuario> results = query.getResultList();
		List<Usuario> users = new ArrayList<>();
		for (Usuario user : results) {
			if(user.getEstado() == null) continue; 
			if(user.getEstado()=='E') continue;
			
			Usuario u = new Usuario();
			u.setId(user.getId());
			u.setAdministrador(user.getAdministrador());
			u.setActivo(user.getEstado()=='A'?true:false);
			u.setFechaExpiracion(user.getFechaExpiracion());
			u.setNombres(user.getNombres());
			u.setNro(user.getNro());
			u.setApellidos(user.getApellidos());
			u.setNombreUsuario(user.getNombreUsuario());
		
			if(user.getOffice() != null){
				Oficina office =  new Oficina();
				office.setId(user.getOffice().getId());
				office.setNombre(user.getOffice().getNombre());
				u.setOffice(office);
			} 
			
			users.add(u);
		}
		return users;
	}
	
	@Override
	public void delete(Long id) {
		Usuario usuarioP = buscarPorId(id);
		usuarioP.setEstado('E');
		em.merge(usuarioP);
	}

	@Override
	public void saveAll(List<Usuario> users) throws Exception {
		for (Usuario user : users) {
			Usuario userP = em.find(Usuario.class, user.getId());
			user.setContrasenia(userP.getContrasenia());
			user.setEstado(user.getActivo()?'A':'I');
			em.merge(user);
		}
	}

	@Override
	public Usuario newUser() {
		Usuario user =  new Usuario();
		user.setContrasenia(MD5.md5("123456"));
		Calendar calendar = new GregorianCalendar(2020,2,2);
		user.setFechaExpiracion(calendar.getTime());
		user.setActivo(false);
		user.setAdministrador(false);
		em.persist(user);
		return user;
	}

	@Override
	public Usuario getCurrentUser() {
		String userName = sctx.getCallerPrincipal().getName();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Usuario> q = cb.createQuery(Usuario.class);
		Root<Usuario> c = q.from(Usuario.class);
		q.select(c).where(cb.equal(c.get("nombreUsuario"), userName));
		TypedQuery<Usuario> tq = em.createQuery(q);
		Usuario userP = tq.getSingleResult();
		  
//		Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.user = " + userName, Usuario.class);
//		Usuario user = (Usuario)query.getSingleResult();
		
		Usuario user = new Usuario();
		Oficina office =  new Oficina();
		office.setId(userP.getOffice().getId());
		office.setNombre(userP.getOffice().getNombre());
		office.setUsers(null);
		user.setNombreUsuario(userP.getNombreUsuario());
		user.setNombres(userP.getNombres());
		user.setApellidos(userP.getApellidos());
		user.setOffice(office);
		user.setConocimientos(null);
		user.setGuiasEntrega(null);
		user.setGuiasRegistro(null);
		user.setAdministrador(userP.getAdministrador());
		//user.setRol(null);
		
		// Permisos
		Rol rolP = null;
		if(userP.getAdministrador()) rolP = getRolPorNombre("Administrador");  
		else rolP = getRolPorNombre("Operador");
		
		Rol rol = new Rol();
		rol.setNombre(rolP.getNombre());
		
		for (Aplicacion app: rolP.getAplicaciones()) {
			Aplicacion appDTO = new Aplicacion();
			appDTO.setNombre(app.getNombre());
			appDTO.setTitulo(app.getTitulo());
			appDTO.setSubAplicaciones(null);
			rol.getAplicaciones().add(appDTO);
			if(app.getAplicacion1() != null) {
				Aplicacion appDTOPadre = new Aplicacion();
				appDTOPadre.setNombre(app.getAplicacion1().getNombre());
				appDTOPadre.setTitulo(app.getAplicacion1().getTitulo());
				appDTOPadre.setSubAplicaciones(null);
				appDTO.setAplicacion1(appDTOPadre);
				
			}
		}
		user.setRol(rol);
		
		return user;
	}

	@Override
	public DateParam getSeverDate() {
		
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd/MM/yyyy");
		String formated = sdf.format(new Date());
		DateParam dateParam = new DateParam();
		dateParam.setFormattedValue(formated);
		dateParam.setDate(new Date());
		
		return dateParam;
	}

	@Override
	public void fijarContraseniaPorDefecto(Long idUser) {
		Usuario user = buscarPorId(idUser);
		user.setContrasenia(MD5.md5("123456"));
		merge(user);
	}

	@Override
	public void cambiarContrasenia(Long idUser, String ultimaContrasenia, String newContrasenia) throws Exception {
		Usuario usuario = buscarPorId(idUser);
		String contraseniaAnteriorMD5 = usuario.getContrasenia();
		String ultimaContraseniaMD5 = MD5.md5(ultimaContrasenia);
		if(contraseniaAnteriorMD5.equals(ultimaContraseniaMD5)){
			// Fijar nueva contraseña
			usuario.setContrasenia(MD5.md5(newContrasenia));
			merge(usuario);
		} else {
			throw new Exception("La última contraseña guardada no coincide");
		} 
	}
	
	private Rol getRolPorNombre(String nombre) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Rol> q = cb.createQuery(Rol.class);
		Root<Rol> c = q.from(Rol.class);
		q.select(c).where(cb.equal(c.get("nombre"), nombre));
		TypedQuery<Rol> tq = em.createQuery(q);
		Rol rolP = tq.getSingleResult();
		return rolP;
	}
	
//	public List<Aplicacion> getAplicacionesPermitidas(){
//		String userName = sctx.getCallerPrincipal().getName();
//		Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario ", Usuario.class);
//		Usuario usuario = (Usuario)query.getSingleResult();
//		
//		
//	}
	
//	private String getDescripcionEstado(Character estado) {
//		if(estado == 'A') return "Activo";
//		if(estado == 'I') return "Inactivo";
//		if(estado == 'E') return "Eliminado";
//		
//		return "";
//	}	
	
}
