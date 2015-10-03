package es.giralsoft.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

@Controller
public class MainController implements Initializable {

	@FXML
	private AnchorPane cuerpo;
	
	@FXML
	private Button botonMenu;
	
	private Stage stageModal;

	public void cargarPantalla(String fxml) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
			fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> defaultControllerClass) {
					Object controller = null;
					try {
						controller = Main.getBean(defaultControllerClass);
					} catch(NoSuchBeanDefinitionException e) {
						e.printStackTrace();
					}
					return controller;
				}
			});
			fxmlLoader.load();
			AnchorPane pantalla = fxmlLoader.getRoot();
			cuerpo.getChildren().clear();
			cuerpo.getChildren().add(pantalla);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cargarModal(String fxml) {
		try {
			Main.getMain().getStage().getScene().getRoot().setEffect(new GaussianBlur(3));
			stageModal = new Stage();
			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
			fxmlLoader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> defaultControllerClass) {
					Object controller = null;
					try {
						controller = Main.getBean(defaultControllerClass);
					} catch(NoSuchBeanDefinitionException e) {
						e.printStackTrace();
					}
					return controller;
				}
			});
			fxmlLoader.load();
			Scene scene = new Scene((Parent) fxmlLoader.getRoot());
			scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Homenaje");
			stageModal.setScene(scene);
			stageModal.initOwner(Main.getMain().getStage());
			stageModal.initModality(Modality.APPLICATION_MODAL);
			stageModal.initStyle(StageStyle.UNDECORATED);
			stageModal.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cerrarModal() {
		Main.getMain().getStage().getScene().getRoot().setEffect(null);
		stageModal.close();
	}
	
	public void abrirMenu() {
		cargarModal(Pantallas.MENU);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

}
