package principal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import utilidades.Calculos;
import utilidades.Datos;

import com.google.common.collect.Multimap;

public class Principal {
	
	private Map<String, Double> puntuacionesJornada;
	private Multimap<String, Double> puntuacionesGlobal;

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

	public Principal() {
		initialize();
	}

	private void initialize() {
		frmFrmedias = new JFrame();
		frmFrmedias.setTitle("frMedias");
		frmFrmedias.setIconImage(Toolkit.getDefaultToolkit().getImage(Principal.class.getResource("/javax/swing/plaf/metal/icons/ocean/homeFolder.gif")));
		frmFrmedias.setBounds(100, 100, 384, 461);
		frmFrmedias.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setEnabled(false);
		splitPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmFrmedias.getContentPane().add(splitPane, BorderLayout.NORTH);
		
		JButton btnCargarDatos = new JButton("Cargar datos");
		btnCargarDatos.setFont(btnCargarDatos.getFont().deriveFont(btnCargarDatos.getFont().getStyle() | Font.BOLD));
		btnCargarDatos.setIcon(new ImageIcon(Principal.class.getResource("/com/sun/java/swing/plaf/windows/icons/HardDrive.gif")));
		splitPane.setLeftComponent(btnCargarDatos);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		
		JButton btnMostrarJornada = new JButton("Mostrar medias de la jornada");
		btnMostrarJornada.setFont(new Font("Verdana", Font.PLAIN, 11));
		splitPane_1.setLeftComponent(btnMostrarJornada);
		
		JButton btnMostrarTemporada = new JButton("Mostrar medias de la temporada");
		btnMostrarTemporada.setFont(new Font("Verdana", Font.PLAIN, 11));
		splitPane_1.setRightComponent(btnMostrarTemporada);
		
		JPanel panel = new JPanel();
		frmFrmedias.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		final JEditorPane editorPane = new JEditorPane();
		editorPane.setAutoscrolls(false);
		editorPane.setMargin(new Insets(10, 10, 10, 10));
		editorPane.setBackground(new Color(255, 250, 250));
		editorPane.setFont(new Font("Lucida Sans", Font.PLAIN, 14));
		panel_2.add(editorPane);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		final JButton btnGuardarMedias = new JButton("Guardar medias de la jornada");
		btnGuardarMedias.setEnabled(false);
		btnGuardarMedias.setFont(new Font("Verdana", Font.PLAIN, 11));
		panel_1.add(btnGuardarMedias);
		
		// Definición de funciones
		
		btnCargarDatos.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Multimap<String, Double> jornadaSinProcesar = null;
				try {
					jornadaSinProcesar = Datos.leerJornada();
					puntuacionesGlobal = Datos.leerTemporada();
				} catch (Exception e1) {
					editorPane.setText(e1.getMessage());
				}
				puntuacionesJornada = Calculos.calcularMedias(jornadaSinProcesar);
			}
		});
		
		btnMostrarJornada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				editorPane.setText("");
				for(String jugador : puntuacionesJornada.keySet()) {
					editorPane.setText(editorPane.getText() + jugador + " - " + puntuacionesJornada.get(jugador) + "\n");
				}
				btnGuardarMedias.setEnabled(true);
			}
		});
		
		btnMostrarTemporada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				btnGuardarMedias.setEnabled(false);
				editorPane.setText("");
				
				Map<String, Double> mediasGlobal = Calculos.calcularMedias(puntuacionesGlobal);
				for(String jugador : mediasGlobal.keySet()) {
					editorPane.setText(editorPane.getText() + jugador + " - " + mediasGlobal.get(jugador) + "\n");
				}
			}
		});
		
		btnGuardarMedias.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(String jugador : puntuacionesJornada.keySet()) {
					puntuacionesGlobal.put(jugador, puntuacionesJornada.get(jugador));
					try {
						Datos.guardarPuntuaciones(puntuacionesGlobal);
					} catch (IOException e1) {
						editorPane.setText(e1.getMessage());
					}
					btnGuardarMedias.setEnabled(false);
				}
			}
		});
	}

}
