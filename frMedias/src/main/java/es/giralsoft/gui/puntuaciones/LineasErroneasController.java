package es.giralsoft.gui.puntuaciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.gui.Main;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

@Controller
public class LineasErroneasController implements Initializable {
	
	@FXML
	private TextArea inputLineas;
	
	@Autowired
	private Sesion sesion;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<String> lineasErroneas = (List<String>) sesion.getDato(IntroduccionPuntuacionesController.CLAVE_LISTA_LINEAS_ERRONEAS);
		String texto = "";
		for(String linea : lineasErroneas) {
			texto = texto + System.lineSeparator() + linea;
		}
		inputLineas.setText(texto);
	}
	
	public void aceptar() {
		try {
			String texto = inputLineas.getText();
			BufferedReader bufReader = new BufferedReader(new StringReader(texto));
			List<String> lineas = new ArrayList<>();
			String linea = null;
			while((linea = bufReader.readLine()) != null) {
				lineas.add(linea);
			}
			
			sesion.putDato(IntroduccionPuntuacionesController.CLAVE_LISTA_LINEAS_ERRONEAS, lineas);
			Main.getMain().getController().cerrarModal();
		} catch (IOException e) {
			Alertas.crearAlertaExcepcion("Ha habido un error", "Ha habido un error al procesar las nuevas lineas.", e);
		}
	}
	
	public void cancelar() {
		Main.getMain().getController().cerrarModal();
	}

}
