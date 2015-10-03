package es.giralsoft.gui.jugadores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Jugador;
import es.giralsoft.gui.Main;
import es.giralsoft.gui.Pantallas;
import es.giralsoft.servicios.JugadorService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

@Controller
public class JugadoresController implements Initializable {
	
	public static final String CLAVE_ES_NUEVO = "ES_NUEVO";
	public static final String CLAVE_JUGADOR = "JUGADOR";
	
	@FXML
	private TableView<Jugador> tablaJugadores;
	@FXML
	private TableColumn<Jugador, String> columnaDorsal, columnaNombre, columnaPosicion;
	@FXML
	private CheckBox checkActivos;
	
	@Autowired
	private Sesion sesion;
	
	@Autowired
	private JugadorService jugadorService;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		List<Jugador> jugadores = (List<Jugador>) jugadorService.buscarJugadores(true);

		columnaDorsal.setCellValueFactory(new PropertyValueFactory<Jugador, String>("dorsal"));
		columnaNombre.setCellValueFactory(new PropertyValueFactory<Jugador, String>("nombre"));
		columnaPosicion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Jugador,String>, ObservableValue<String>>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public ObservableValue<String> call(CellDataFeatures<Jugador, String> param) {
				String posicion = (param.getValue().getPosicion() != null && StringUtils.isNotBlank(param.getValue().getPosicion().toString())) ? param.getValue().getPosicion().toString() : "";
				return new ReadOnlyObjectWrapper(posicion);
			}
		});

		tablaJugadores.setItems(FXCollections.observableArrayList(jugadores));
		
		checkActivos.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<Jugador> jugadores = (List<Jugador>) jugadorService.buscarJugadores(checkActivos.isSelected());
				tablaJugadores.setItems(FXCollections.observableArrayList(jugadores));
			}
		});
	}
	
	public void crearJugador() {
		sesion.putDato(CLAVE_ES_NUEVO, true);
		Main.getMain().getController().cargarModal(Pantallas.DETALLE_JUGADOR);
		refrescarTabla();
	}
	
	public void editarJugador() {
		Jugador jugador = tablaJugadores.getSelectionModel().getSelectedItem();
		if(jugador != null) {
			sesion.putDato(CLAVE_ES_NUEVO, false);
			sesion.putDato(CLAVE_JUGADOR, jugador);
			Main.getMain().getController().cargarModal(Pantallas.DETALLE_JUGADOR);
			refrescarTabla();
		}
		else {
			Alertas.crearAlertaAviso("Ha habido un error", "Tienes que seleccionar un jugador.");
		}
	}
	
	public void eliminarJugador() {
		Jugador jugador = tablaJugadores.getSelectionModel().getSelectedItem();
		if(jugador != null) {
			jugadorService.delete(jugador);
			Alertas.crearAlertaInformacion("Jugador eliminado", "El jugador ha sido eliminado correctamente.");
			refrescarTabla();
		}
		else {
			Alertas.crearAlertaError("Ha habido un error", "Tienes que seleccionar un jugador.");
		}
	}

	private void refrescarTabla() {
		List<Jugador> jugadores = (List<Jugador>) jugadorService.buscarJugadores(checkActivos.isSelected());
		tablaJugadores.setItems(FXCollections.observableArrayList(jugadores));
	}

}
