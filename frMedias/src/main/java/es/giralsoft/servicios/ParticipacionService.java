package es.giralsoft.servicios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Jugador;
import es.giralsoft.dominio.Participacion;
import es.giralsoft.dominio.Partido;
import es.giralsoft.persistencia.ParticipacionRepository;

@Service
@Transactional
public class ParticipacionService {
	
	@Autowired
	private ParticipacionRepository participacionRepository;
	
	public List<Participacion> buscarParticipaciones(Competicion competicion, boolean activo, Date fechaDesde, Date fechaHasta) {
		if(fechaDesde == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(0, 1, 1);
			fechaDesde = calendar.getTime();
		}
		
		if(fechaHasta == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(3000, 12, 31);
			fechaHasta = calendar.getTime();
		}
		
		List<Object[]> resultados = null;
		if(competicion != null) {
			resultados = participacionRepository.buscarParticipaciones(competicion, fechaDesde, fechaHasta);
		}
		else {
			resultados = participacionRepository.buscarParticipaciones(activo, fechaDesde, fechaHasta);
		}
		
		List<Participacion> participaciones = new ArrayList<>();
		for(Object[] resultado : resultados) {
			Participacion participacion = new Participacion();
			participacion.setGoles(((Long) resultado[0]).intValue());
			participacion.setAsistencias(((Long) resultado[1]).intValue());
			long factor = (long) Math.pow(10, 2);
			double media = (double) resultado[2] * factor;
			long tmp = Math.round(media);
			media = (double) tmp / factor;
			participacion.setNota(media);
			participacion.setJugador((Jugador) resultado[3]);
			participaciones.add(participacion);
		}
		
		return participaciones;
	}

	public List<Participacion> buscarParticipaciones(Competicion competicion) {
		List<Object[]> resultado = participacionRepository.buscarParticipaciones(competicion);
		List<Participacion> participaciones = new ArrayList<>();
		for(Object[] o : resultado) {
			Participacion participacion = new Participacion();
			participacion.setPartidos(((Long) o[0]).intValue());
			participacion.setGoles(((Long) o[1]).intValue());
			participacion.setAsistencias(((Long) o[2]).intValue());
			long factor = (long) Math.pow(10, 2);
			double media = (double) o[3] * factor;
			long tmp = Math.round(media);
			media = (double) tmp / factor;
			participacion.setNota(media);
			participacion.setJugador((Jugador) o[4]);
			participaciones.add(participacion);
		}
		return participaciones;
	}
	
	public List<Participacion> buscarParticipaciones(List<Partido> partidos) {
		List<Object[]> resultado = participacionRepository.buscarParticipaciones(partidos);
		List<Participacion> participaciones = new ArrayList<>();
		for(Object[] o : resultado) {
			Participacion participacion = new Participacion();
			participacion.setPartidos(((Long) o[0]).intValue());
			participacion.setGoles(((Long) o[1]).intValue());
			participacion.setAsistencias(((Long) o[2]).intValue());
			long factor = (long) Math.pow(10, 2);
			double media = (double) o[3] * factor;
			long tmp = Math.round(media);
			media = (double) tmp / factor;
			participacion.setNota(media);
			participacion.setJugador((Jugador) o[4]);
			participaciones.add(participacion);
		}
		return participaciones;
	}

}
