package es.giralsoft.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.JRGraphics2DExporterParameter;
import net.sf.jasperreports.engine.fill.JRTemplatePrintRectangle;

@SuppressWarnings("deprecation")
@Component
public class ServicioExportacionImagen {

	public void exportarPuntuaciones(String informe, String sql) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();

		// fill report
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection("jdbc:mysql://" + BaseDatosConfig.serverName + ":3306/" + BaseDatosConfig.databaseName, BaseDatosConfig.user, BaseDatosConfig.password);
		
		PreparedStatement statement = conn.prepareStatement(sql);
		ResultSet rs = statement.executeQuery();
		JRDataSource jrDataSource = new JRResultSetDataSource(rs);
		JasperPrint print = JasperFillManager.fillReport(Thread.currentThread().getContextClassLoader().getResourceAsStream(informe), parameters, jrDataSource);

		// export image
		int height = 0;
		for(JRPrintPage page : print.getPages()) {
			for(JRPrintElement element : page.getElements()) {
				if(element instanceof JRTemplatePrintRectangle) {
					height = height + element.getHeight();
				}
			}
		}
		JRGraphics2DExporter gExporter = new JRGraphics2DExporter();
		BufferedImage bi = new BufferedImage(print.getPageWidth(), height, BufferedImage.TYPE_INT_RGB);
		Graphics2D reportImage = bi.createGraphics();

		gExporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		gExporter.setParameter(JRGraphics2DExporterParameter.GRAPHICS_2D, reportImage);
		gExporter.exportReport();

		// output as PNG file
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);
		baos.flush();
		byte[] result = baos.toByteArray();
		baos.close();
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
		OutputStream ot = new FileOutputStream("puntuaciones_" + format.format(new Date()) + ".png");
		ot.write(result);
		ot.close();
	}

}
