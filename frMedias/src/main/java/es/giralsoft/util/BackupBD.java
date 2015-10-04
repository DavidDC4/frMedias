package es.giralsoft.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import es.giralsoft.gui.Main;

public class BackupBD implements Job {

	private Logger log = Logger.getLogger(getClass());

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			Main.getMain().getController().mostrarIndicador();
			
			Process p = null;

			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
			Date date = new Date();
			String filepath = "backup_" + dateFormat.format(date) + ".sql";

			String batchCommand = "";
			batchCommand = BaseDatosConfig.backupExe 
					+ " -h " + BaseDatosConfig.serverName 
					+ " --port 3306" 
					+ " -u " + BaseDatosConfig.user 
					+ " --password=" + BaseDatosConfig.password 
					+ " --add-drop-database -B " + BaseDatosConfig.databaseName 
					+ " -r \"" + BaseDatosConfig.backupPath + "" + filepath + "\"";

			Runtime runtime = Runtime.getRuntime();
			p = runtime.exec(batchCommand);
			int processComplete = p.waitFor();

			if (processComplete == 0) {
				log.info("Copia de seguridad creada correctamente para la base de datos " + BaseDatosConfig.databaseName + " en " + filepath);
			} else {
				log.info("No se pudo crear la copia de seguridad para la base de datos " + BaseDatosConfig.databaseName + " in " + BaseDatosConfig.serverName + ":3306");
			}
		} catch (IOException ioe) {
			log.error(ioe, ioe.getCause());
		} catch (Exception e) {
			log.error(e, e.getCause());
		}
	}

}
