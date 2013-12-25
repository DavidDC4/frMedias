package Utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class Datos {

	public Multimap<String, Double> leerJornada() throws IOException {
		Multimap<String, Double> res = ArrayListMultimap.create();
		
		File archivo = new File ("puntuacionesJornada.txt");
		FileReader fr = new FileReader (archivo);
		BufferedReader br = new BufferedReader(fr);
		
		String linea = br.readLine();
		while(linea != null) {
			String[] campos = linea.split("-");
			
			String jugador = campos[0];
			Double puntuacion = Double.parseDouble(campos[1]);
			
			res.put(jugador, puntuacion);
		}
		
		br.close();
		return res;
	}
	
}
