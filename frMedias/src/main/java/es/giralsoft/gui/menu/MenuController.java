package es.giralsoft.gui.menu;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.stereotype.Controller;

import es.giralsoft.gui.Main;
import es.giralsoft.gui.Pantallas;
import javafx.fxml.Initializable;

@Controller
public class MenuController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void cerrar() {
		Main.getMain().getController().cerrarModal();
	}
	
	public void abrirCompeticiones() {
		Main.getMain().getController().cargarPantalla(Pantallas.COMPETICIONES);
		cerrar();
	}
	
	public void abrirJugadores() {
		Main.getMain().getController().cargarPantalla(Pantallas.JUGADORES);
		cerrar();
	}
	
	public void abrirIntroduccionPuntuaciones() {
		Main.getMain().getController().cargarPantalla(Pantallas.INTRODUCCION_PUNTUACIONES);
		cerrar();
	}
	
	public void abrirConsultaPuntuaciones() {
		Main.getMain().getController().cargarPantalla(Pantallas.PARTIDOS);
		cerrar();
	}

}
