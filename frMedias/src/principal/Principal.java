package principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Principal {

	private JFrame frmFrmedias;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Principal window = new Principal();
					window.frmFrmedias.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Principal() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFrmedias = new JFrame();
		frmFrmedias.setTitle("frMedias");
		frmFrmedias.setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/javax/swing/plaf/metal/icons/ocean/homeFolder.gif")));
		frmFrmedias.setBounds(100, 100, 581, 461);
		frmFrmedias.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmFrmedias.getContentPane().add(splitPane, BorderLayout.NORTH);
		
		JButton btnCargarDatos = new JButton("Cargar datos");
		btnCargarDatos.setFont(btnCargarDatos.getFont().deriveFont(btnCargarDatos.getFont().getStyle() | Font.BOLD));
		btnCargarDatos.setIcon(new ImageIcon(Principal.class.getResource("/com/sun/java/swing/plaf/windows/icons/HardDrive.gif")));
		splitPane.setLeftComponent(btnCargarDatos);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		JButton btnMostrarDatosDe = new JButton("Mostrar medias de la jornada");
		btnMostrarDatosDe.setFont(new Font("Verdana", Font.PLAIN, 11));
		splitPane_1.setLeftComponent(btnMostrarDatosDe);
		
		JButton btnMostrarDatosDe_1 = new JButton("Mostrar medias de la temporada");
		btnMostrarDatosDe_1.setFont(new Font("Verdana", Font.PLAIN, 11));
		splitPane_1.setRightComponent(btnMostrarDatosDe_1);
		
		JPanel panel = new JPanel();
		frmFrmedias.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		final JEditorPane editorPane = new JEditorPane();
		editorPane.setBackground(new Color(30, 144, 255));
		editorPane.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		panel_2.add(editorPane);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnGuardarMediasDe = new JButton("Guardar medias de la jornada");
		btnGuardarMediasDe.setEnabled(false);
		btnGuardarMediasDe.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_1.add(btnGuardarMediasDe);
		
		// Definición de funciones
	}

}
