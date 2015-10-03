package es.giralsoft.gui.jugadores;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Jugador;
import es.giralsoft.dominio.Posicion;
import es.giralsoft.gui.Main;
import es.giralsoft.servicios.JugadorService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

@Controller
public class DetalleJugadorController implements Initializable {
	
	@FXML
	private Label titulo;
	@FXML
	private TextField inputNombre, inputDorsal, inputSinonimo;
	@FXML
	private ComboBox<String> comboPosiciones;
	@FXML
	private ListView<String> listaSinonimos;
	@FXML
	private CheckBox checkActivo;
	
	@Autowired
	private Sesion sesion;
	
	@Autowired
	private JugadorService jugadorService;
	
	private Jugador jugador;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<String> posiciones = new ArrayList<>();
		posiciones.add(Posicion.PORTERO.toString());
		posiciones.add(Posicion.DEFENSA.toString());
		posiciones.add(Posicion.CENTROCAMPISTA.toString());
		posiciones.add(Posicion.DELANTERO.toString());
		posiciones.add(Posicion.ENTRENADOR.toString());
		comboPosiciones.setItems(FXCollections.observableArrayList(posiciones));
		
		if((boolean) sesion.getDato(JugadoresController.CLAVE_ES_NUEVO)) {
			jugador = new Jugador();
			titulo.setText("Nuevo jugador");
		}
		else {
			jugador = (Jugador) sesion.getDato(JugadoresController.CLAVE_JUGADOR);
			titulo.setText(jugador.getNombre());
			inputNombre.setText(jugador.getNombre());
			inputDorsal.setText(Integer.toString(jugador.getDorsal()));
			checkActivo.setSelected(jugador.isActivo());
			comboPosiciones.getSelectionModel().select(jugador.getPosicion().toString());
			listaSinonimos.setItems(FXCollections.observableArrayList(jugador.getSinonimos()));
		}
	}
	
	public void aceptar() {
		String nombre = inputNombre.getText();
		String dorsal = inputDorsal.getText();
		String posicion = comboPosiciones.getSelectionModel().getSelectedItem();
		if(StringUtils.isNotBlank(nombre) && StringUtils.isNotBlank(dorsal) && StringUtils.isNumeric(dorsal) && StringUtils.isNotBlank(posicion)) {
			jugador.setNombre(nombre);
			jugador.setDorsal(Integer.valueOf(dorsal));
			jugador.setPosicion(Posicion.valueOf(posicion));
			jugador.setActivo(checkActivo.isSelected());
			jugadorService.save(jugador);
			Alertas.crearAlertaInformacion("Operación realizada correctamente", "El jugador ha sido guardado correctamente.");
			Main.getMain().getController().cerrarModal();
		}
		else {
			Alertas.crearAlertaError("Ha habido un error", "Tienes que rellenar todos los datos.");
		}
	}
	
	public void cancelar() {
		Main.getMain().getController().cerrarModal();
	}
	
	public void addSinonimo() {
		String sinonimo = inputSinonimo.getText();
		listaSinonimos.getItems().add(sinonimo);
		jugador.getSinonimos().add(sinonimo);
	}

}
