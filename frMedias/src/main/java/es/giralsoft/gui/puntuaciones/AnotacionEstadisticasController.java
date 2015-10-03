package es.giralsoft.gui.puntuaciones;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Jugador;
import es.giralsoft.dominio.Participacion;
import es.giralsoft.dominio.Partido;
import es.giralsoft.gui.Main;
import es.giralsoft.servicios.JugadorService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;

@Controller
public class AnotacionEstadisticasController implements Initializable {

	@FXML
	private TableView<Participacion> tablaEstadisticas;

	@FXML
	private TableColumn<Participacion, String> columnaJugador;

	@FXML
	private TableColumn<Participacion, Number> columnaAsistencias, columnaGoles;

	@FXML
	private ComboBox<Jugador> comboJugador;

	@FXML
	private TextField inputAsistencias, inputGoles;

	@Autowired
	private Sesion sesion;

	@Autowired
	private JugadorService jugadorService;

	private Partido partido;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnaJugador.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participacion, String>, ObservableValue<String>>() {

			@Override
			public ObservableValue<String> call(CellDataFeatures<Participacion, String> param) {
				return new SimpleStringProperty(param.getValue().getJugador().getNombre());
			}
		});
		columnaAsistencias.setCellValueFactory(new PropertyValueFactory<Participacion, Number>("asistencias"));
		columnaGoles.setCellValueFactory(new PropertyValueFactory<Participacion, Number>("goles"));

		columnaAsistencias.setCellFactory(TextFieldTableCell.<Participacion, Number> forTableColumn(new NumberStringConverter()));
		columnaGoles.setCellFactory(TextFieldTableCell.<Participacion, Number> forTableColumn(new NumberStringConverter()));

		columnaAsistencias.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Participacion, Number>>() {
			@Override
			public void handle(CellEditEvent<Participacion, Number> event) {
				event.getRowValue().setAsistencias(event.getNewValue().intValue());
			}
		});

		columnaGoles.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Participacion, Number>>() {
			@Override
			public void handle(CellEditEvent<Participacion, Number> event) {
				event.getRowValue().setGoles(event.getNewValue().intValue());
			}
		});

		List<Participacion> participaciones = (List<Participacion>) sesion.getDato(IntroduccionPuntuacionesController.CLAVE_LISTA_PARTICIPACIONES);
		tablaEstadisticas.setItems(FXCollections.observableArrayList(participaciones));

		List<Jugador> jugadores = jugadorService.buscarJugadores(true);
		comboJugador.setItems(FXCollections.observableArrayList(jugadores));

		partido = (Partido) sesion.getDato(IntroduccionPuntuacionesController.CLAVE_PARTIDO);
	}

	public void aceptar() {
		List<Participacion> participaciones = tablaEstadisticas.getItems();
		sesion.putDato(IntroduccionPuntuacionesController.CLAVE_LISTA_PARTICIPACIONES, participaciones);
		Main.getMain().getController().cerrarModal();
	}

	public void addJugador() {
		Jugador jugador = comboJugador.getSelectionModel().getSelectedItem();
		if (jugador != null) {
			Integer asistencias = null;
			try {
				asistencias = new Integer(inputAsistencias.getText());
			} catch (Exception e) {
				asistencias = 0;
			}
			Integer goles = null;
			try {
				goles = new Integer(inputGoles.getText());
			} catch (Exception e) {
				goles = 0;
			}

			Participacion participacion = new Participacion();
			participacion.setJugador(jugador);
			participacion.setAsistencias(asistencias);
			participacion.setGoles(goles);
			participacion.setNota(-1);
			participacion.setPartido(partido);
			
			tablaEstadisticas.getItems().add(participacion);
		} else {
			Alertas.crearAlertaAviso("No hay jugador seleccionado", "Selecciona un jugador para seguir");
		}
	}

}
