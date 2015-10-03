package es.giralsoft.util;

import java.util.Properties;

public class BaseDatosConfig {

	public static String serverName;
	public static String user;
	public static String password;
	public static String databaseName;

	public static void cargarConfiguracion(Properties properties) {
		serverName = properties.getProperty("db.server_name");
		user = properties.getProperty("db.user");
		password = properties.getProperty("db.password");
		databaseName = properties.getProperty("db.database_name");
	}

}
