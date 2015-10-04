package es.giralsoft.persistencia;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Participacion;
import es.giralsoft.dominio.Partido;

@Repository
public interface ParticipacionRepository extends CrudRepository<Participacion, Long> {

	@Query("SELECT SUM(a.goles) as goles, SUM(a.asistencias) as asistencias, AVG(a.nota), a.jugador "
			+ "FROM Competicion c JOIN c.partidos p JOIN p.participaciones a "
			+ "WHERE c = ?1 AND p.fecha >= ?2 AND p.fecha <= ?3 "
			+ "GROUP BY a.jugador")
	List<Object[]> buscarParticipaciones(Competicion competicion, Date fechaDesde, Date fechaHasta);

	@Query("SELECT SUM(a.goles) as goles, SUM(a.asistencias) as asistencias, AVG(a.nota), a.jugador "
			+ "FROM Competicion c JOIN c.partidos p JOIN p.participaciones a "
			+ "WHERE c.activo = ?1 AND p.fecha >= ?2 AND p.fecha <= ?3 "
			+ "GROUP BY a.jugador")
	List<Object[]> buscarParticipaciones(boolean activo, Date fechaDesde, Date fechaHasta);
	
	@Query("SELECT p FROM Participacion p WHERE p.partido = ?1")
	List<Participacion> buscarParticipaciones(Partido partido);
	
	@Query("SELECT COUNT(p), SUM(p.goles), SUM(p.asistencias), AVG(CASE WHEN p.nota >= 0 THEN p.nota ELSE null END), p.jugador FROM Competicion c JOIN c.partidos pa JOIN pa.participaciones p WHERE c = ?1 GROUP BY p.jugador")
	List<Object[]> buscarParticipaciones(Competicion competicion);
	
	@Query("SELECT COUNT(p), SUM(p.goles), SUM(p.asistencias), AVG(CASE WHEN p.nota >= 0 THEN p.nota ELSE null END), p.jugador FROM Competicion c JOIN c.partidos pa JOIN pa.participaciones p WHERE pa IN ?1 GROUP BY p.jugador")
	List<Object[]> buscarParticipaciones(List<Partido> partidos);

}
