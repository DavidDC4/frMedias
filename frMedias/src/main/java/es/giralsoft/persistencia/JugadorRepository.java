package es.giralsoft.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.giralsoft.dominio.Jugador;

@Repository
public interface JugadorRepository extends CrudRepository<Jugador, Long> {
	
	@Query("SELECT j FROM Jugador j WHERE j.activo = ?1 ORDER BY j.posicion, j.dorsal")
	List<Jugador> buscarTodos(boolean activo);
	
	@Query("SELECT j FROM Jugador j WHERE j.nombre = ?1 OR ?1 IN elements(j.sinonimos)")
	List<Jugador> buscarJugadorPorNombre(String nombre);

}
