package utilidades;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Datos {
	
	public static Multimap<String, Double> leerJornada() throws IOException {
		return leerFichero("puntuacionesJornada.txt");
	}
	
	public static Multimap<String, Double> leerTemporada() throws IOException {
		return leerFichero("puntuacionesGlobal.dat");
	}

	private static Multimap<String, Double> leerFichero(String fichero) throws IOException {
		Multimap<String, Double> res = ArrayListMultimap.create();
		
		Multimap<String, String> nombres = leerFicheroDeNombres();
		
		File archivo = new File (fichero);
		FileReader fr = new FileReader(archivo);
		BufferedReader br = new BufferedReader(fr);
		
		String linea = br.readLine();
		while(linea != null) {
			if(!linea.equals("")) {
				String[] campos = linea.split("-");
				String jugador = convertirNombre(nombres, campos[0].trim());
				String[] puntuaciones = campos[1].split(",");
				for(int i = 0 ; i < puntuaciones.length ; i++) {
					Double puntuacion = Double.parseDouble(puntuaciones[i].trim());
					res.put(jugador, puntuacion);
				} 
			}
			
			linea = br.readLine();
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
			String jugador = campos[0].trim();
			for(int i = 1 ; i < campos.length ; i++) {
				res.put(jugador, campos[i].trim());
			}
			linea = br.readLine();
		}
		
		br.close();
		return res;
	}

	private static String convertirNombre(Multimap<String, String> nombres, String nombre) {
		String res = null;
		if(nombres.containsKey(nombre)) {
			res = nombre;
		} else {
			for(String j : nombres.keySet()) {
				if(nombres.get(j).contains(nombre)) {
					res = j;
				}
			}
			
			if(res == null) {
				throw new Error("No se ha localizado a " + nombre + " en el registro de la plantilla");
			}
		}
		return res;
	}

	public static void guardarPuntuaciones(Multimap<String, Double> puntuacionesGlobal) throws IOException {
		File archivo = new File ("puntuacionesGlobal.dat");
		FileWriter fw = new FileWriter(archivo);
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(String jugador : puntuacionesGlobal.keySet()) {
			String linea = jugador + " - ";
			for(Double media : puntuacionesGlobal.get(jugador)) {
				linea = linea + media + ",";
			}
			linea = linea.substring(0, linea.length()-1) + "\n";
			bw.write(linea);
		}
		
		bw.close();
	}
}
