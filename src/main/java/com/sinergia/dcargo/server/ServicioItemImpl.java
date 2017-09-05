package com.sinergia.dcargo.server;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sinergia.dcargo.client.shared.ServicioItem;
import com.sinergia.dcargo.client.shared.dominio.Guia;
import com.sinergia.dcargo.client.shared.dominio.Item;
import com.sinergia.dcargo.server.dao.Dao;
import com.sinergia.dcargo.server.util.Utilitario;

/**
 * 
 * @author willy
 */
@Stateless
public class ServicioItemImpl extends Dao<Item> implements ServicioItem {

	public ServicioItemImpl() {
		super(Item.class);
	}
	
	@PersistenceContext(unitName = "dCargoUnit")
	private EntityManager em;
	
	@Resource
	private SessionContext sctx;

	@Override
	public List<Item> buscarItemByGuia(Long idGuia) {
		String sql = "SELECT i FROM Item i WHERE i.guia.id = :idGuia";
		Query query = em.createQuery(sql);
		query.setParameter("idGuia", idGuia);
		@SuppressWarnings("unchecked")
		List<Item> items = query.getResultList();
		List<Item> itemsDTO = Utilitario.mapper(items, Item.class); 
		return itemsDTO;
	}

	@Override
	public void guardarTodos(Guia guia) throws Exception {
		
		if(guia.getId() == 0L) {
			guia.setId(null);
			// Get nro secuencial
			Query query = em.createQuery("SELECT MAX(g.nroGuia) FROM Guia g");
			Integer nroGuia = query.getFirstResult() + 1;
			guia.setNroGuia(nroGuia);
		}
		
		for (Item item : guia.getItems()) {
			merge(item);
		}
		
	}

	@Override
	public Item nuevoItem(Long idGuia) throws Exception {
		Guia guiaP = em.find(Guia.class, idGuia);
		Item item = new Item();
		Guia guia = new Guia();
		guia.setId(guiaP.getId());
		item.setGuia(guia);
		Item itemP = merge(item);
		item.setId(itemP.getId());
		return item;
	}

	@Override
	public void guardar(Item item) throws Exception {
		Item itemP = buscarPorId(item.getId());
		itemP.setCantidad(item.getCantidad());
		itemP.setContenido(item.getContenido());
		itemP.setPeso(item.getPeso());
		itemP.setUnidad(item.getUnidad());
		itemP.setPrecio(item.getPrecio());
		itemP.setTotal(item.getTotal());
		merge(itemP);
	}

	@Override
	public void borrar(Long idItem) {
		Item itemP = buscarPorId(idItem);
		em.remove(itemP);
	}

}
