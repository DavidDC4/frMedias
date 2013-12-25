package utilidades;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class Calculos {

	public static Map<String, Double> calcularMedias(Multimap<String, Double> puntuaciones) {
		Map<String, Double> res = Maps.newHashMap();
		
		for(String jugador : puntuaciones.keySet()) {
			Double acum = 0.0;
			List<Double> puntuacionesJugador = (List<Double>) puntuaciones.get(jugador);
			for(Double puntuacion : puntuacionesJugador) {
				acum = acum + puntuacion;
			}
			Double media = acum / puntuacionesJugador.size();
			res.put(jugador, media);
		}
		
		return res;
	}
	
}
