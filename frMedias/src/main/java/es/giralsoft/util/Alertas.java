package es.giralsoft.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import es.giralsoft.gui.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class Alertas {
	
	public static void crearAlertaError(String titulo, String texto) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.initOwner(Main.getMain().getStage());
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setTitle(titulo);
		alert.setHeaderText(titulo);
		alert.setContentText(texto);
		alert.showAndWait();
	}
	
	public static void crearAlertaInformacion(String titulo, String texto) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.initOwner(Main.getMain().getStage());
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setTitle(titulo);
		alert.setContentText(texto);
		alert.setHeaderText(titulo);
		alert.showAndWait();
	}

	public static void crearAlertaAviso(String titulo, String texto) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.initOwner(Main.getMain().getStage());
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setTitle(titulo);
		alert.setHeaderText(titulo);
		alert.setContentText(texto);
		alert.showAndWait();
	}

	public static void crearAlertaExcepcion(String titulo, String texto, Exception e) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initStyle(StageStyle.UNDECORATED);
		alert.initOwner(Main.getMain().getStage());
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.setTitle(titulo);
		alert.setHeaderText(titulo);
		alert.setContentText(texto);
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();
		Label label = new Label("La traza del error es:");
		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		alert.getDialogPane().setExpandableContent(expContent);
		
		alert.showAndWait();
	}

}
