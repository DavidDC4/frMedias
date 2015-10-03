package es.giralsoft.util;

import java.util.Comparator;

import es.giralsoft.dominio.Participacion;

public class ComparadorPosiciones implements Comparator<Participacion> {

	@Override
	public int compare(Participacion o1, Participacion o2) {
		int value = o1.getJugador().getPosicion().compareTo(o2.getJugador().getPosicion());
		if(value == 0) {
			value = Integer.compare(o1.getJugador().getDorsal(), o2.getJugador().getDorsal());
		}
		return value;
	}

}
