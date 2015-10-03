package es.giralsoft.gui.competiciones;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Posicion;
import es.giralsoft.gui.Main;
import es.giralsoft.servicios.CompeticionService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Controller
public class DetalleCompeticionController implements Initializable {
	
	@FXML
	private Label titulo;
	@FXML
	private TextField inputTemporada, inputNombre;
	@FXML
	private CheckBox checkActivo;
	
	@Autowired
	private Sesion sesion;
	
	@Autowired
	private CompeticionService competicionService;
	
	private Competicion competicion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<String> posiciones = new ArrayList<>();
		posiciones.add(Posicion.PORTERO.toString());
		posiciones.add(Posicion.DEFENSA.toString());
		posiciones.add(Posicion.CENTROCAMPISTA.toString());
		posiciones.add(Posicion.DELANTERO.toString());
		
		if((boolean) sesion.getDato(CompeticionesController.CLAVE_ES_NUEVO)) {
			competicion = new Competicion();
			titulo.setText("Nueva competición");
		}
		else {
			competicion = (Competicion) sesion.getDato(CompeticionesController.CLAVE_COMPETICION);
			titulo.setText(competicion.getNombre());
			inputTemporada.setText(competicion.getTemporada());
			inputNombre.setText(competicion.getNombre());
			checkActivo.setSelected(competicion.isActivo());
		}
	}
	
	public void aceptar() {
		String temporada = inputTemporada.getText();
		String nombre = inputNombre.getText();
		if(StringUtils.isNotBlank(nombre) && StringUtils.isNotBlank(temporada)) {
			competicion.setNombre(nombre);
			competicion.setTemporada(temporada);
			competicion.setActivo(checkActivo.isSelected());
			competicionService.save(competicion);
			Alertas.crearAlertaInformacion("Operación realizada correctamente", "La competición ha sido guardada correctamente.");
			Main.getMain().getController().cerrarModal();
		}
		else {
			Alertas.crearAlertaError("Ha habido un error", "Tienes que rellenar todos los datos.");
		}
	}
	
	public void cancelar() {
		Main.getMain().getController().cerrarModal();
	}

}
