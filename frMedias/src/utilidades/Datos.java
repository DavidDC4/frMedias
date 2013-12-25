package utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Datos {
	
	public static Multimap<String, Double> leerJornada() throws IOException {
		return leerFichero("puntuacionesJornada.txt");
	}
	
	public static Multimap<String, Double> leerTemporada() throws IOException {
		return leerFichero("puntuacionesGlobal.txt");
	}

	private static Multimap<String, Double> leerFichero(String fichero) throws IOException {
		Multimap<String, Double> res = ArrayListMultimap.create();
		
		Multimap<String, String> nombres = leerFicheroDeNombres();
		
		File archivo = new File (fichero);
		FileReader fr = new FileReader(archivo);
		BufferedReader br = new BufferedReader(fr);
		
		String linea = br.readLine();
		while(linea != null) {
			String[] campos = linea.split("-");
			
			String jugador = convertirNombre(nombres, campos[0]);
			Double puntuacion = Double.parseDouble(campos[1]);
			
			res.put(jugador, puntuacion);
		}
		
		br.close();
		return res;
	}
	
	private static Multimap<String, String> leerFicheroDeNombres() throws IOException {
		Multimap<String, String> res = ArrayListMultimap.create();
		
		File archivo = new File ("plantilla.dat");
		FileReader fr = new FileReader(archivo);
		BufferedReader br = new BufferedReader(fr);
		
		String linea = br.readLine();
		while(linea != null) {
			String[] campos = linea.split("-");
			String jugador = campos[0];
			for(int i = 1 ; i < campos.length ; i++) {
				res.put(jugador, campos[i]);
			}
		}
		
		br.close();
		return res;
	}

	private static String convertirNombre(Multimap<String, String> nombres, String nombre) {
		String res = null;
		if(nombres.containsKey(nombre)) {
			res = nombre;
		} else if(nombres.containsValue(nombre)) {
			for(String j : nombres.keySet()) {
				if(nombres.get(j).contains(nombre)) {
					res = j;
				}
			}
		} else {
			throw new Error("No se ha localizado a " + nombre + " en el registro de la plantilla");
		}
		return res;
	}
}
