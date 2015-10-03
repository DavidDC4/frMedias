package es.giralsoft.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import es.giralsoft.dominio.Competicion;

@Repository
public interface CompeticionRepository extends CrudRepository<Competicion, Long> {
	
	@Query("select c from Competicion c where c.activo = ?1")
	List<Competicion> buscarTodos(boolean activos);

}
