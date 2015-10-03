package es.giralsoft.servicios;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.persistencia.CompeticionRepository;

@Service
@Transactional
public class CompeticionService {
	
	@Autowired
	private CompeticionRepository competicionRepository;
	
	public List<Competicion> findAll() {
		return (List<Competicion>) competicionRepository.findAll();
	}

	public void save(Competicion competicion) {
		competicionRepository.save(competicion);
	}

	public void delete(Competicion competicion) {
		competicionRepository.delete(competicion);
	}
	
	public List<Competicion> buscarTodos(boolean activos) {
		return (List<Competicion>) competicionRepository.buscarTodos(activos);
	}

}
