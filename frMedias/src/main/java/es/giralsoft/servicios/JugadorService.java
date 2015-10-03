package es.giralsoft.servicios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.giralsoft.dominio.Jugador;
import es.giralsoft.persistencia.JugadorRepository;

@Service
@Transactional
public class JugadorService {

	@Autowired
	private JugadorRepository jugadorRepository;

	public List<Jugador> findAll() {
		return (List<Jugador>) jugadorRepository.findAll();
	}

	public void save(Jugador jugador) {
		jugadorRepository.save(jugador);
	}

	public void delete(Jugador jugador) {
		jugadorRepository.delete(jugador);
	}

	public Jugador buscarPorNombre(String nombre) {
		List<Jugador> jugadores = jugadorRepository.buscarJugadorPorNombre(nombre);
		if (jugadores != null && !jugadores.isEmpty()) {
			return jugadores.get(0);
		} else {
			return null;
		}
	}

	public List<Jugador> buscarJugadores(boolean activos) {
		return (List<Jugador>) jugadorRepository.buscarTodos(activos);
	}

}
