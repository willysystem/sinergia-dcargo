package com.sinergia.dcargo.server.util;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.sinergia.dcargo.client.shared.Cliente;
import com.sinergia.dcargo.client.shared.Item;
import com.sinergia.dcargo.client.shared.Precio;
import com.sinergia.dcargo.client.shared.Transportista;
import com.sinergia.dcargo.client.shared.Unidad;

public class Utilitario {
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	
	public static  <E> List<E>  mapper(List<E> lista, Class<E> clazz){
		List<E> listaDTO = new ArrayList<E>();
		for (E e : lista) {
			
			if(clazz.equals(Cliente.class)){
				((Cliente)e).setGuiasConsignatario(null);
				((Cliente)e).setGuiasRemitente(null);
			} else if(clazz.equals(Transportista.class)){
				((Transportista)e).setConocimientosPropietario(null);
			} else if(clazz.equals(Item.class)){
				((Item)e).setGuia(null);
			} else if(clazz.equals(Unidad.class)){
				((Unidad)e).setItems(null);
			} else if(clazz.equals(Precio.class)){
				((Precio)e).setItems(null);
			}
			
			listaDTO.add(modelMapper.map(e, clazz));
		}
		return listaDTO;
	}
}
