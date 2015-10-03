package es.giralsoft.gui.puntuaciones;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Jugador;
import es.giralsoft.gui.Main;
import es.giralsoft.servicios.JugadorService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.Sesion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

@Controller
@SuppressWarnings("unchecked")
public class NombresNoEncontradosController implements Initializable {
	
	@FXML
	private ListView<String> listaNombres;
	
	@Autowired
	private JugadorService jugadorService;
	
	@Autowired
	private Sesion sesion;
	
	private List<Jugador> jugadores;
	private List<CeldaNombre> celdas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jugadores = jugadorService.findAll();
		celdas = new ArrayList<>();
		
		listaNombres.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			
			@Override
			public ListCell<String> call(ListView<String> param) {
				return new CeldaNombre();
			}
		});
		
		Set<String> nombresNoEncontrados = (Set<String>) sesion.getDato(IntroduccionPuntuacionesController.CLAVE_NOMBRES_NO_ENCONTRADOS);
		listaNombres.setItems(FXCollections.observableArrayList(nombresNoEncontrados));
	}
	
	public void aceptar() {
		for(CeldaNombre celda : celdas) {
			if(celda.getJugador() != null && StringUtils.isNotBlank(celda.getNombre())) {
				Jugador jugador = celda.getJugador();
				jugador.addSinonimo(celda.getNombre());
				jugadorService.save(jugador);
			}
		}
		Alertas.crearAlertaInformacion("Nombres actualizados", "Los nombres de los jugadores se han actualizado correctamente.");
		Main.getMain().getController().cerrarModal();
	}
	
	public void cancelar() {
		Main.getMain().getController().cerrarModal();
	}
	
	class CeldaNombre extends ListCell<String> {
		
		private String nombre;
		private ComboBox<Jugador> comboJugadores;
		
		public CeldaNombre() {
			celdas.add(this);
		}
		
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            
            nombre = item;
            
            HBox box = new HBox();
            Label labelJugador = new Label(item);
            labelJugador.setPrefWidth(200);
            labelJugador.setPrefHeight(40);
            labelJugador.setAlignment(Pos.CENTER_LEFT);
            comboJugadores = new ComboBox<>(FXCollections.observableArrayList(jugadores));
            comboJugadores.setPrefWidth(200);
            comboJugadores.setPrefHeight(40);
            
            box.getChildren().add(labelJugador);
            box.getChildren().add(comboJugadores);
            
            setGraphic(box);
        }
        
        public String getNombre() {
        	return nombre;
        }
        
        public Jugador getJugador() {
        	return comboJugadores.getSelectionModel().getSelectedItem();
        }
    }

}
