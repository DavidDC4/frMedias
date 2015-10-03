package es.giralsoft.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Sesion {
	
	private Map<String, Object> datos;
	
	public void putDato(String clave, Object valor) {
		if(datos == null) {
			datos = new HashMap<>();
		}
		datos.put(clave, valor);
	}
	
	public Object getDato(String clave) {
		if(datos == null) {
			datos = new HashMap<>();
		}
		Object object = datos.get(clave);
		datos.remove(clave);
		return object;
	}
	
	public Object getDatoSinBorrar(String clave) {
		if(datos == null) {
			datos = new HashMap<>();
		}
		return datos.get(clave);
	}

}
