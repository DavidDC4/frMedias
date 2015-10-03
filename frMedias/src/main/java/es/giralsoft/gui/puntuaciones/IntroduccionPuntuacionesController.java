package es.giralsoft.gui.puntuaciones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Jugador;
import es.giralsoft.dominio.Participacion;
import es.giralsoft.dominio.Partido;
import es.giralsoft.gui.Main;
import es.giralsoft.gui.Pantallas;
import es.giralsoft.servicios.CompeticionService;
import es.giralsoft.servicios.JugadorService;
import es.giralsoft.servicios.PartidoService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

@Controller
@SuppressWarnings("unchecked")
public class IntroduccionPuntuacionesController implements Initializable {

	public static final String CLAVE_NOMBRES_NO_ENCONTRADOS = "NOMBRES_NO_ENCONTRADOS";
	public static final String CLAVE_LINEAS_ERRONEAS = "LINEAS_ERRONEAS";
	public static final String CLAVE_LISTA_LINEAS_ERRONEAS = "LISTA_LINEAS_ERRONEAS";
	public static final String CLAVE_LISTA_PARTICIPACIONES = "LISTA_PARTICIPACIONES";
	public static final String CLAVE_PARTIDO = "PARTIDO";

	@FXML
	private TextArea inputPuntuaciones;
	@FXML
	private ComboBox<Competicion> comboCompeticiones;
	@FXML
	private TextField inputJornada, inputRival;
	@FXML
	private DatePicker inputFecha;
	@FXML
	private CheckBox checkLocal;

	@Autowired
	private CompeticionService competicionService;

	@Autowired
	private JugadorService jugadorService;

	@Autowired
	private PartidoService partidoService;

	@Autowired
	private Sesion sesion;

	private List<String> lineas;
	private Map<Jugador, List<Double>> puntuaciones;
	private List<String> lineasErroneas;
	private Set<String> jugadoresNoEncontrados;
	private List<String> lineasJugadoresNoEncontrados;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Competicion> competiciones = competicionService.findAll();
		comboCompeticiones.setItems(FXCollections.observableArrayList(competiciones));

		inputFecha.setShowWeekNumbers(false);
		inputFecha.setValue(LocalDate.now());
	}

	public void procesarPuntuaciones() {
		try {
			String texto = inputPuntuaciones.getText();
			Competicion competicion = comboCompeticiones.getSelectionModel().getSelectedItem();
			String rival = inputRival.getText();
			Date fecha = Date.from(Instant.ofEpochSecond(inputFecha.getValue().toEpochDay()));

			if (StringUtils.isNotBlank(texto) && competicion != null && StringUtils.isNotBlank(rival) && fecha != null) {
				lineas = new ArrayList<>();
				BufferedReader bufReader = new BufferedReader(new StringReader(texto));
				String linea = null;
				while ((linea = bufReader.readLine()) != null) {
					lineas.add(linea);
				}

				puntuaciones = new HashMap<>();
				lineasErroneas = new ArrayList<>();
				jugadoresNoEncontrados = new HashSet<>();
				lineasJugadoresNoEncontrados = new ArrayList<>();

				while (!lineas.isEmpty() || !lineasErroneas.isEmpty() || !lineasJugadoresNoEncontrados.isEmpty()) {
					procesarLineas();

					if (!lineasJugadoresNoEncontrados.isEmpty()) {
						sesion.putDato(CLAVE_NOMBRES_NO_ENCONTRADOS, jugadoresNoEncontrados);
						Main.getMain().getController().cargarModal(Pantallas.NOMBRES_NO_ECONTRADOS);
						procesarLineasNoEncontrados();
					}

					if (!lineasErroneas.isEmpty()) {
						sesion.putDato(CLAVE_LISTA_LINEAS_ERRONEAS, lineasErroneas);
						Main.getMain().getController().cargarModal(Pantallas.LINEAS_ERRONEAS);
						lineasErroneas = (List<String>) sesion.getDato(CLAVE_LISTA_LINEAS_ERRONEAS);
						procesarLineasErroneas();
					}
				}

				calcularMedias();
			} else {
				Alertas.crearAlertaAviso("Formulario incompleto", "Por favor, rellena todos los campos.");
			}
		} catch (IOException e) {
			Alertas.crearAlertaExcepcion("Ha habido un error", "No se han podido procesar las puntuaciones.", e);
		}
	}

	private void procesarLineas() {
		Iterator<String> iterator = lineas.iterator();
		while (iterator.hasNext()) {
			String linea = iterator.next();
			procesarLinea(linea);
			iterator.remove();
		}
	}

	private void procesarLineasNoEncontrados() {
		Iterator<String> iterator = lineasJugadoresNoEncontrados.iterator();
		while (iterator.hasNext()) {
			String linea = iterator.next();
			procesarLinea(linea);
			iterator.remove();
		}
	}

	private void procesarLineasErroneas() {
		Iterator<String> iterator = lineasErroneas.iterator();
		while (iterator.hasNext()) {
			String linea = iterator.next();
			procesarLinea(linea);
			iterator.remove();
		}
	}

	private void procesarLinea(String linea) {
		Pattern pattern = Pattern.compile("(\\D+)\\s*-\\s*(\\d+)\\s*-*.*");
		if (StringUtils.isNotBlank(linea)) {
			Matcher matcher = pattern.matcher(linea);
			if (matcher.find()) {
				String nombre = matcher.group(1).trim();
				Double puntuacion = Double.valueOf(matcher.group(2));
				if (StringUtils.isNotBlank(nombre) && puntuacion != null) {
					Jugador jugador = jugadorService.buscarPorNombre(nombre);
					if (jugador != null) {
						if (puntuacion < 0) {
							puntuacion = 0.0;
						} else if (puntuacion > 10) {
							puntuacion = 10.0;
						}

						List<Double> puntuacionesJugador = puntuaciones.get(jugador);
						if (puntuacionesJugador == null) {
							puntuacionesJugador = new ArrayList<>();
						}
						puntuacionesJugador.add(puntuacion);
						puntuaciones.put(jugador, puntuacionesJugador);
					} else {
						jugadoresNoEncontrados.add(nombre);
						lineasJugadoresNoEncontrados.add(linea);
					}
				}
			} else {
				lineasErroneas.add(linea);
			}
		}
	}

	private void calcularMedias() {
		Partido partido = new Partido();
		Competicion competicion = comboCompeticiones.getSelectionModel().getSelectedItem();
		String rival = inputRival.getText();
		String jornada = inputJornada.getText();
		Calendar calendar = Calendar.getInstance();
		calendar.set(inputFecha.getValue().getYear(), inputFecha.getValue().getMonth().getValue() - 1, inputFecha.getValue().getDayOfMonth());
		partido.setCompeticion(competicion);
		partido.setRival(rival);
		partido.setJornada(jornada);
		partido.setFecha(calendar.getTime());
		partido.setPuntuaciones(inputPuntuaciones.getText());

		List<Participacion> participaciones = new ArrayList<>();
		for (Jugador jugador : puntuaciones.keySet()) {
			List<Double> puntuacionesLista = puntuaciones.get(jugador);
			Double media = 0.0;
			for (Double d : puntuacionesLista) {
				media = media + d;
			}
			media = media / puntuacionesLista.size();
			long factor = (long) Math.pow(10, 2);
			media = media * factor;
			long tmp = Math.round(media);
			media = (double) tmp / factor;

			Participacion participacion = new Participacion();
			participacion.setJugador(jugador);
			participacion.setNota(media);
			participacion.setPartido(partido);
			participaciones.add(participacion);
		}

		sesion.putDato(CLAVE_LISTA_PARTICIPACIONES, participaciones);
		sesion.putDato(CLAVE_PARTIDO, partido);
		Main.getMain().getController().cargarModal(Pantallas.ANOTACION_ESTADISTICAS);
		participaciones = (List<Participacion>) sesion.getDato(CLAVE_LISTA_PARTICIPACIONES);
		partido.setParticipaciones(participaciones);
		partidoService.save(partido);

		Alertas.crearAlertaInformacion("Puntuaciones procesadas", "Las puntuaciones han sido procesadas correctamente");
		limpiarFormulario();
	}

	private void limpiarFormulario() {
		inputPuntuaciones.setText("");
		inputFecha.setValue(LocalDate.now());
		inputJornada.setText("");
		inputRival.setText("");
		checkLocal.setSelected(false);
	}

}
