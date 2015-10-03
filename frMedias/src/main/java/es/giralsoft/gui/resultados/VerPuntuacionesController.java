package es.giralsoft.gui.resultados;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.giralsoft.dominio.Competicion;
import es.giralsoft.dominio.Participacion;
import es.giralsoft.dominio.Partido;
import es.giralsoft.gui.Main;
import es.giralsoft.servicios.ParticipacionService;
import es.giralsoft.util.Alertas;
import es.giralsoft.util.ComparadorPosiciones;
import es.giralsoft.util.ServicioExportacionImagen;
import es.giralsoft.util.Sesion;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

@Controller
public class VerPuntuacionesController implements Initializable {

	public static final String CLAVE_TIPO_PANTALLA = "Tipo pantalla";
	public static final String CLAVE_VALOR = "Valor";
	
	@FXML
	private Label labelTitulo;
	
	@FXML
	private TableView<Participacion> tablaEstadisticas;
	
	@FXML
	private TableColumn<Participacion, String> columnaPosicion, columnaJugador, columnaPartidos, columnaAsistencias, columnaGoles, columnaMedia;
	
	@Autowired
	private Sesion sesion;
	
	@Autowired
	private ServicioExportacionImagen servicioExportacionImagen;
	
	@Autowired
	private ParticipacionService participacionService;

	private int tipoPantalla;
	private Partido partido;
	private Competicion competicion;
	private List<Partido> partidos;
	private List<Participacion> participaciones;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		columnaPosicion.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participacion,String>, ObservableValue<String>>() {
			@SuppressWarnings({ "rawtypes" })
			@Override
			public ObservableValue<String> call(CellDataFeatures<Participacion, String> param) {
				String posicion = (param.getValue().getJugador().getPosicion() != null && StringUtils.isNotBlank(param.getValue().getJugador().getPosicion().toString())) ? param.getValue().getJugador().getPosicion().toString() : "";
				return new ReadOnlyObjectWrapper(posicion);
			}
		});
		columnaJugador.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participacion,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Participacion, String> param) {
				return new ReadOnlyObjectWrapper<String>(param.getValue().getJugador().getNombre());
			}
		});
		columnaPartidos.setCellValueFactory(new PropertyValueFactory<Participacion, String>("partidos"));
		columnaAsistencias.setCellValueFactory(new PropertyValueFactory<Participacion, String>("asistencias"));
		columnaGoles.setCellValueFactory(new PropertyValueFactory<Participacion, String>("goles"));
		columnaMedia.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Participacion,String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<Participacion, String> param) {
				double nota = param.getValue().getNota();
				if(nota >= 0) {
					return new ReadOnlyObjectWrapper<String>(Double.toString(param.getValue().getNota()));
				}
				else {
					return new ReadOnlyObjectWrapper<String>("-");
				}
			}
		});
		
		tipoPantalla = (int) sesion.getDato(CLAVE_TIPO_PANTALLA);
		if(tipoPantalla == 0) {
			partido = (Partido) sesion.getDato(CLAVE_VALOR);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			labelTitulo.setText(partido.getCompeticion().getNombre() + " - " + partido.getJornada() + " - " + partido.getRival() + " (" + format.format(partido.getFecha()) + ")");
			ComparadorPosiciones comparador = new ComparadorPosiciones();
			Collections.sort(partido.getParticipaciones(), comparador);
			tablaEstadisticas.setItems(FXCollections.observableArrayList(partido.getParticipaciones()));
			columnaPartidos.setVisible(false);
		}
		else if(tipoPantalla == 1) {
			competicion = (Competicion) sesion.getDato(CLAVE_VALOR);
			
			if(competicion != null) {
				participaciones = participacionService.buscarParticipaciones(competicion);
				ComparadorPosiciones comparador = new ComparadorPosiciones();
				Collections.sort(participaciones, comparador);
				tablaEstadisticas.setItems(FXCollections.observableArrayList(participaciones));
			}
		}
		else if(tipoPantalla == 2) {
			partidos = (List<Partido>) sesion.getDato(CLAVE_VALOR);
			
			if(partidos != null && !partidos.isEmpty()) {
				labelTitulo.setText("Puntuaciones de partidos");
				
				participaciones = participacionService.buscarParticipaciones(partidos);
				ComparadorPosiciones comparador = new ComparadorPosiciones();
				Collections.sort(participaciones, comparador);
				tablaEstadisticas.setItems(FXCollections.observableArrayList(participaciones));
			}
		}
	}
	
	public void aceptar() {
		Main.getMain().getController().cerrarModal();
	}
	
	public void guardarImagen() {
		try {
			String jasperPath = null;
			String sql = null;
			if(tipoPantalla == 0) {
				sql = "SELECT NOMBRE, DORSAL, POSICION, ASISTENCIAS, GOLES, NOTA "
						+ "FROM PARTICIPACION P LEFT JOIN JUGADOR J ON J.ID = P.JUGADOR_ID "
						+ "WHERE P.PARTIDO_ID = " + partido.getId() + " ORDER BY J.POSICION, J.DORSAL";
				jasperPath = "resumen_puntuaciones_partido.jasper";
				servicioExportacionImagen.exportarPuntuaciones(jasperPath, sql);
			}
			else if(tipoPantalla == 1) {
				sql = "SELECT j.nombre, j.dorsal, j.posicion, COUNT(p.id) AS partidos, SUM(p.goles) AS goles, SUM(p.asistencias) AS asistencias, ROUND(AVG(p.nota),2) AS nota " 
						+ "FROM Competicion c "
							+ "JOIN Partido pa ON pa.competicion_id = c.id "
							+ "JOIN Participacion p ON p.partido_id = pa.id "
							+ "JOIN Jugador j ON j.id = p.jugador_id "
						+ "WHERE c.id = " + competicion.getId() + " "
						+ "GROUP BY p.jugador_id "
						+ "ORDER BY j.posicion, j.dorsal";
				jasperPath = "resumen_puntuaciones_global.jasper";
				servicioExportacionImagen.exportarPuntuaciones(jasperPath, sql);
			}
			else if(tipoPantalla == 2) {
				String partidosIds = "";
				for(Partido partido : partidos) {
					partidosIds = partidosIds + partido.getId() + ",";
				}
				partidosIds = partidosIds.substring(0, partidosIds.length()-1);
				
				sql = "SELECT j.nombre, j.dorsal, j.posicion, COUNT(p.id) AS partidos, SUM(p.goles) AS goles, SUM(p.asistencias) AS asistencias, ROUND(AVG(p.nota),2) AS nota " 
						+ "FROM Competicion c "
							+ "JOIN Partido pa ON pa.competicion_id = c.id "
							+ "JOIN Participacion p ON p.partido_id = pa.id "
							+ "JOIN Jugador j ON j.id = p.jugador_id "
						+ "WHERE p.partido_id IN (" + partidosIds + ") "
						+ "GROUP BY p.jugador_id "
						+ "ORDER BY j.posicion, j.dorsal";
				jasperPath = "resumen_puntuaciones_global.jasper";
				servicioExportacionImagen.exportarPuntuaciones(jasperPath, sql);		
			}
			Alertas.crearAlertaInformacion("Imagen generada", "Se ha generado la imagen correctamente");
		}
		catch(Exception e) {
			Alertas.crearAlertaExcepcion("Ha habido un error al guardar la imagen", "Ha habido un error al guardar la imagen, contacte con el administrador.", e);
		}
	}

}
