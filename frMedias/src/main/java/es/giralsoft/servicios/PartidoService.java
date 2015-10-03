package es.giralsoft.servicios;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Partido;
import es.giralsoft.persistencia.PartidoRepository;

@Service
@Transactional
public class PartidoService {
	
	@Autowired
	private PartidoRepository partidoRepository;
	
	public List<Partido> findAll() {
		return (List<Partido>) partidoRepository.findAll();
	}
	
	public void save(Partido partido) {
		partidoRepository.save(partido);
	}
	
	public void delete(Partido partido) {
		partidoRepository.delete(partido.getId());
	}
	
	public List<Partido> buscarPartidos(boolean activo, Competicion competicion, Date fechaDesde, Date fechaHasta) {		
		if(competicion != null) {
			return partidoRepository.buscarPartidos(competicion);
		}
		else {
			return partidoRepository.buscarPartidos(activo, fechaDesde, fechaHasta);
		}
	}

}
