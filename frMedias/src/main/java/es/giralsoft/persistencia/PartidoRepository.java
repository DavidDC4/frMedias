package es.giralsoft.persistencia;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Partido;

@Repository
public interface PartidoRepository extends CrudRepository<Partido, Long> {
	
	@Query("SELECT p FROM Competicion c JOIN c.partidos p WHERE c.activo = ?1 AND p.fecha >= ?2 AND p.fecha <= ?3 ORDER BY p.fecha")
	List<Partido> buscarPartidos(boolean activo, Date fechaDesde, Date fechaHasta);

	@Query("SELECT p FROM Competicion c JOIN c.partidos p WHERE c = ?1 ORDER BY p.fecha")
	List<Partido> buscarPartidos(Competicion competicion);

}
