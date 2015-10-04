package es.giralsoft.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import es.giralsoft.util.BaseDatosConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

@ComponentScan(basePackages = "es.giralsoft", lazyInit = true)
public class Main extends Application {

	private Stage primaryStage;
	private VBox rootLayout;

	private static Main main;
	private static MainController controller;

	private static ApplicationContext context;

	@Override
	public void start(Stage primaryStage) throws Exception {
		cargarConfiguracion();
		cargarBeans();
		cargarAplicacion(primaryStage);
	}

	private void cargarConfiguracion() {
		InputStream inputStream = null;
		try {
			Properties properties = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				properties.load(inputStream);
			} else {
				throw new FileNotFoundException("No se ha encontrado el fichero " + propFileName);
			}
			
			BaseDatosConfig.cargarConfiguracion(properties);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void cargarBeans() {
		context = new AnnotationConfigApplicationContext(Main.class);
	}

	private void cargarAplicacion(Stage primaryStage) {
		main = this;
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("frMedias");

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(Pantallas.PRINCIPAL));
			loader.setControllerFactory(new Callback<Class<?>, Object>() {
				@Override
				public Object call(Class<?> defaultControllerClass) {
					Object controller = null;
					try {
						controller = Main.getBean(defaultControllerClass);
					} catch (NoSuchBeanDefinitionException e) {
						e.printStackTrace();
					}
					return controller;
				}
			});
			rootLayout = (VBox) loader.load();
			controller = (MainController) context.getBean(loader.getController().getClass());

			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add("https://fonts.googleapis.com/css?family=Homenaje");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Stage getStage() {
		return primaryStage;
	}

	public MainController getController() {
		return controller;
	}

	public static Main getMain() {
		return main;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> tipoRequerido) {
		try {
			// Busca en el contexto por tipo
			return context.getBean(tipoRequerido);
		} catch (NoSuchBeanDefinitionException e) {
			// Si falla buscaremos en el contexto por el nombre por defecto del
			// Bean (nombre de la clase con primera letra minúscula)
			String[] split = tipoRequerido.getName().split("\\.");
			String name = split[split.length - 1];
			name = Character.toLowerCase(name.charAt(0)) + name.substring(1, name.length());
			return (T) context.getBean(name);
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
