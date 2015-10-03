package es.giralsoft.gui.resultados;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Partido;
import es.giralsoft.gui.Main;
import es.giralsoft.gui.Pantallas;
import es.giralsoft.servicios.CompeticionService;
import es.giralsoft.servicios.PartidoService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

@Controller
public class PartidosController implements Initializable {

	@FXML
	private DatePicker inputFechaDesde, inputFechaHasta;

	@FXML
	private CheckBox checkActivos;

	@FXML
	private ComboBox<Competicion> comboCompeticiones;

	@FXML
	private TableView<Partido> tablaPartidos;

	@FXML
	private TableColumn<Partido, String> columnaTemporada, columnaCompeticion, columnaJornada, columnaRival, columnaFecha;

	@Autowired
	private CompeticionService competicionService;

	@Autowired
	private PartidoService partidoService;
	
	@Autowired
	private Sesion sesion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Competicion> competicionesActivas = competicionService.buscarTodos(true);
		Competicion competicionVacia = new Competicion();
		competicionVacia.setNombre("-");
		competicionesActivas.add(0, competicionVacia);
		comboCompeticiones.setItems(FXCollections.observableArrayList(competicionesActivas));
		comboCompeticiones.getSelectionModel().select(0);

		checkActivos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<Competicion> competicionesActivas = competicionService.buscarTodos(checkActivos.isSelected());
				Competicion competicionVacia = new Competicion();
				competicionVacia.setNombre("-");
				competicionesActivas.add(0, competicionVacia);
				comboCompeticiones.setItems(FXCollections.observableArrayList(competicionesActivas));
				comboCompeticiones.getSelectionModel().select(0);
			}
		});

		inputFechaDesde.setShowWeekNumbers(false);
		inputFechaHasta.setShowWeekNumbers(false);
		
		columnaTemporada.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Partido,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Partido, String> param) {
				return new SimpleStringProperty(param.getValue().getCompeticion().getTemporada());
			}
		});
		columnaCompeticion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Partido,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Partido, String> param) {
				return new SimpleStringProperty(param.getValue().getCompeticion().getNombre());
			}
		});
		columnaJornada.setCellValueFactory(new PropertyValueFactory<Partido, String>("jornada"));
		columnaRival.setCellValueFactory(new PropertyValueFactory<Partido, String>("rival"));
		columnaFecha.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Partido,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Partido, String> param) {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				return new SimpleStringProperty(format.format(param.getValue().getFecha()));
			}
		});
		
		checkActivos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<Competicion> competicionesActivas = competicionService.buscarTodos(checkActivos.isSelected());
				Competicion competicionVacia = new Competicion();
				competicionVacia.setNombre("-");
				competicionesActivas.add(0, competicionVacia);
				comboCompeticiones.setItems(FXCollections.observableArrayList(competicionesActivas));
				comboCompeticiones.getSelectionModel().select(0);
			}
		});
		
		buscar();
	}

	public void buscar() {
		boolean activo = checkActivos.isSelected();
		
		Competicion competicion = comboCompeticiones.getValue();
		if(competicion != null && competicion.getId() == null) {
			competicion = null;
		}
		Date fechaDesde = null;
		if(inputFechaDesde.getValue() != null) {
			fechaDesde = Date.from(Instant.ofEpochSecond(inputFechaDesde.getValue().toEpochDay()));
		}
		Date fechaHasta = null;
		if(inputFechaHasta.getValue() != null) {
			fechaHasta = Date.from(Instant.ofEpochSecond(inputFechaHasta.getValue().toEpochDay()));
		}
		
		List<Partido> partidos = partidoService.buscarPartidos(activo, competicion, fechaDesde, fechaHasta);
		tablaPartidos.setItems(FXCollections.observableArrayList(partidos));
	}

	public void verPuntuacionesPartido() {
		Partido partido = tablaPartidos.getSelectionModel().getSelectedItem();
		if(partido != null) {
			sesion.putDato(VerPuntuacionesController.CLAVE_VALOR, partido);
			sesion.putDato(VerPuntuacionesController.CLAVE_TIPO_PANTALLA, 0);
			Main.getMain().getController().cargarModal(Pantallas.VER_PUNTUACIONES);
		}
		else {
			Alertas.crearAlertaAviso("Ha habido un error", "Tienes que seleccionar un partido.");
		}
	}

	public void verMediasCompeticion() {
		Competicion competicion = comboCompeticiones.getValue();
		if(competicion != null && competicion.getId() != null) {
			sesion.putDato(VerPuntuacionesController.CLAVE_VALOR, competicion);
			sesion.putDato(VerPuntuacionesController.CLAVE_TIPO_PANTALLA, 1);
			Main.getMain().getController().cargarModal(Pantallas.VER_PUNTUACIONES);
		}
		else {
			Alertas.crearAlertaError("No hay competición", "No se ha seleccionado una competición.");
		}
		
	}
	
	public void verMediasPartidos() {
		List<Partido> partidos = tablaPartidos.getItems().subList(0, tablaPartidos.getItems().size());
		if(partidos != null && !partidos.isEmpty()) {
			sesion.putDato(VerPuntuacionesController.CLAVE_VALOR, partidos);
			sesion.putDato(VerPuntuacionesController.CLAVE_TIPO_PANTALLA, 2);
			Main.getMain().getController().cargarModal(Pantallas.VER_PUNTUACIONES);
		}
		else {
			Alertas.crearAlertaError("No hay partidos", "No se ha seleccionado partidos.");
		}
	}

	public void eliminarPartido() {
		Partido partido = tablaPartidos.getSelectionModel().getSelectedItem();
		if(partido != null) {
			partidoService.delete(partido);
			Alertas.crearAlertaInformacion("Partido eliminado", "El partido se ha eliminado correctamente.");
			buscar();
		}
		else {
			Alertas.crearAlertaAviso("Ha habido un error", "Tienes que seleccionar un partido.");
		}
	}

}
