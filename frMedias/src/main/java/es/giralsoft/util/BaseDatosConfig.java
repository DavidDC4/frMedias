package es.giralsoft.util;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

public class BaseDatosConfig {

	public static String serverName;
	public static String user;
	public static String password;
	public static String databaseName;

	public static String backupExe;
	public static int backupFrecuency;
	public static String backupPath;

	public static void cargarConfiguracion(Properties properties) {
		serverName = properties.getProperty("db.server_name");
		user = properties.getProperty("db.user");
		password = properties.getProperty("db.password");
		databaseName = properties.getProperty("db.database_name");
		backupExe = properties.getProperty("db.backup.exe");
		backupFrecuency = new Integer(properties.getProperty("db.backup.frecuency"));
		backupPath = properties.getProperty("db.backup.path");
	}

	public static void arrancarTrabajoBackup() throws SchedulerException {
		if(StringUtils.isNotBlank(backupExe) && backupFrecuency > 0 && StringUtils.isNotBlank(backupPath)) {
			JobDetail job = new JobDetail();
	    	job.setName("backupBDJob");
	    	job.setJobClass(BackupBD.class);
	    	
			SimpleTrigger trigger = new SimpleTrigger();
	    	trigger.setName("backupBD");
	    	trigger.setStartTime(new Date(System.currentTimeMillis() + 1000));
	    	trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
	    	trigger.setRepeatInterval(backupFrecuency);
	    	
	    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
	    	scheduler.start();
	    	scheduler.scheduleJob(job, trigger);
		}
	}

}
