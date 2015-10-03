package es.giralsoft.gui.competiciones;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.gui.Main;
import es.giralsoft.gui.Pantallas;
import es.giralsoft.servicios.CompeticionService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

@Controller
public class CompeticionesController implements Initializable {
	
	public static final String CLAVE_ES_NUEVO = "ES_NUEVO";
	public static final String CLAVE_COMPETICION = "COMPETICION";

	@FXML
	private TableView<Competicion> tablaCompeticiones;
	
	@FXML
	private TableColumn<Competicion, String> columnaTemporada, columnaNombre;
	
	@FXML
	private CheckBox checkActivos;

	@Autowired
	private CompeticionService competicionService;
	
	@Autowired
	private Sesion sesion;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Competicion> competiciones = (List<Competicion>) competicionService.buscarTodos(true);

		columnaTemporada.setCellValueFactory(new PropertyValueFactory<Competicion, String>("temporada"));
		columnaNombre.setCellValueFactory(new PropertyValueFactory<Competicion, String>("nombre"));

		tablaCompeticiones.setItems(FXCollections.observableArrayList(competiciones));
		
		checkActivos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<Competicion> competiciones = (List<Competicion>) competicionService.buscarTodos(checkActivos.isSelected());
				tablaCompeticiones.setItems(FXCollections.observableArrayList(competiciones));
			}
		});
	}

	public void crearCompeticion() {
		sesion.putDato(CLAVE_ES_NUEVO, true);
		Main.getMain().getController().cargarModal(Pantallas.DETALLE_COMPETICION);
		refrescarTabla();
	}
	
	public void editarCompeticion() {
		Competicion competicion = tablaCompeticiones.getSelectionModel().getSelectedItem();
		if(competicion != null) {
			sesion.putDato(CLAVE_ES_NUEVO, false);
			sesion.putDato(CLAVE_COMPETICION, competicion);
			Main.getMain().getController().cargarModal(Pantallas.DETALLE_COMPETICION);
			refrescarTabla();
		}
		else {
			Alertas.crearAlertaAviso("Ha habido un error", "Tienes que seleccionar un jugador.");
		}
	}
	
	public void eliminarCompeticion() {
		Competicion competicion = tablaCompeticiones.getSelectionModel().getSelectedItem();
		if(competicion != null) {
			competicionService.delete(competicion);
			Alertas.crearAlertaInformacion("Competición eliminado", "La competición ha sido eliminada correctamente.");
			refrescarTabla();
		}
		else {
			Alertas.crearAlertaError("Ha habido un error", "Tienes que seleccionar una competición.");
		}
	}

	private void refrescarTabla() {
		List<Competicion> competiciones = (List<Competicion>) competicionService.buscarTodos(checkActivos.isSelected());
		tablaCompeticiones.setItems(FXCollections.observableArrayList(competiciones));
	}

}
