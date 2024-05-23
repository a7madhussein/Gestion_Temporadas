package com.standings.ui.page;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.util.Comparator;
import java.util.List;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

public class SportsDashboardPage1 extends ParentFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private SeasonSetupPage seasonSetupPage;
	private JPanel mainPanel;
	private JTable standingsTable;
	private DefaultTableModel standingsTableModel;
	private int selectedYear;

	public SportsDashboardPage1(Season season, List<Team> loadedData, SeasonSetupPage parent, int selectedYear,
			String fileName, SeasonSetupPage seasonSetupPage) {
		this.seasonSetupPage = seasonSetupPage;
		this.selectedYear = selectedYear;

		initializeFrameGraphics();
		initializePanelGraphics();
		initializeStandingsTable();
		initializeButtons();
		lastPaint();
		loadStandingsFromDatabase();
	}

	private void initializeFrameGraphics() {
		setTitle("Panel administrativo de La Liga");
		setResizable(false);
		setSizeAndCenter();
	}

	private void setSizeAndCenter() {
		setSize(887, 515);
		setLocationRelativeTo(null);
	}

	private void initializePanelGraphics() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
	}

	private void initializeStandingsTable() {
		standingsTableModel = new DefaultTableModel();
		standingsTableModel.addColumn("Equipo");
		standingsTableModel.addColumn("Pts.");
		standingsTableModel.addColumn("PJ");
		standingsTableModel.addColumn("PG");
		standingsTableModel.addColumn("PE");
		standingsTableModel.addColumn("PP");
		standingsTableModel.addColumn("GF");
		standingsTableModel.addColumn("GC");
		standingsTableModel.addColumn("Dif.");

		standingsTable = new JTable(standingsTableModel);
		standingsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		// Ajustar el tamaño de las celdas
		
		standingsTable.setRowHeight(30); // AjustaR el tamaño de las filas
		standingsTable.setFont(new Font("Arial", Font.PLAIN, 12)); // Ajusta el tamaño de la fuente

		// Centrar el contenido de las celdas
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < standingsTable.getColumnCount(); i++) {
			standingsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		standingsTable.setEnabled(false);

		mainPanel.add(new JScrollPane(standingsTable), BorderLayout.CENTER);
	}

	private void initializeButtons() {
		JPanel buttonsPanel = new JPanel();

		// Crear botón para generar PDF
		
		JButton pdfButton = new JButton("Generar PDF");
		pdfButton.setBackground(Color.lightGray);
		pdfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Lógica para generar el PDF
				//generatePdf();
				
			}
		});

		// Crear botón para generar XML
		
		JButton jornadas = new JButton("Jornadas");
		jornadas.setBackground(Color.lightGray);
		jornadas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Lógica para generar el XML
				showMatchesForSelectedYear();
			}
		});
		// Crear botón Volver
		
		JButton returnButton = new JButton("Volver");
		returnButton.setBackground(Color.lightGray);
		returnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Lógica para Volver
				returntoseason();
			}
		});
		// Agregar los botones al panel de botones
		buttonsPanel.add(pdfButton);
		buttonsPanel.add(jornadas);
		buttonsPanel.add(returnButton);
		// Agregar el panel de botones al mainPanel
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
	}

	private void lastPaint() {
		getContentPane().setLayout(new BorderLayout());

		// Agrega una etiqueta encima de la tabla
		
		JLabel titleLabel = new JLabel("Clasificación de la Liga " + selectedYear);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);

		// Ajusta el tamaño de la tabla hacia abajo
		standingsTable.setPreferredScrollableViewportSize(new Dimension(800, 400));

		// Agrega la etiqueta y la tabla al mainPanel
		mainPanel.add(titleLabel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(standingsTable), BorderLayout.CENTER);

		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	private void loadStandingsFromDatabase() {
	    // Aquí ejecutas una consulta SQL para recuperar la clasificación desde la base de datos
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
	        // Consulta SQL para recuperar la clasificación
	        String query = "SELECT equipo, puntos, partidos_jugados, partidos_ganados, partidos_empatados, partidos_perdidos, " +
	                       "goles_favor, goles_contra, diferencia_goles FROM clasificacion WHERE año_temporada = ?";
	        
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setString(1, String.valueOf(selectedYear));
	            ResultSet rs = pstmt.executeQuery();
	            
	            // Limpiar datos previos en el modelo de la tabla de clasificación
	            clearTableModel();
	            
	            // Cargar los datos de la clasificación desde la base de datos
	            while (rs.next()) {
	                // Obtener los datos de cada equipo y agregarlos a la tabla de clasificación
	                String equipo = rs.getString("equipo");
	                int puntos = rs.getInt("puntos");
	                int partidosJugados = rs.getInt("partidos_jugados");
	                int partidosGanados = rs.getInt("partidos_ganados");
	                int partidosEmpatados = rs.getInt("partidos_empatados");
	                int partidosPerdidos = rs.getInt("partidos_perdidos");
	                int golesFavor = rs.getInt("goles_favor");
	                int golesContra = rs.getInt("goles_contra");
	                int diferenciaGoles = rs.getInt("diferencia_goles");
	                
	                // Agregar una fila con los datos del equipo a la tabla de clasificación
	                addTableRow(equipo, puntos, partidosJugados, partidosGanados, partidosEmpatados, partidosPerdidos, golesFavor, golesContra, diferenciaGoles);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error al cargar la clasificación desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void clearTableModel() {
		// limpiar los datos previos en la tabla
		DefaultTableModel model = (DefaultTableModel) standingsTable.getModel();
		model.setRowCount(0);
	}

	private void addTableRow(String equipo, int puntos, int partidosJugados, int partidosGanados, int partidosEmpatados, int partidosPerdidos, int golesFavor, int golesContra, int diferenciaGoles) {
	    DefaultTableModel model = (DefaultTableModel) standingsTable.getModel();
	    model.addRow(new Object[]{equipo, puntos, partidosJugados, partidosGanados, partidosEmpatados, partidosPerdidos, golesFavor, golesContra, diferenciaGoles});
	}

	private void returntoseason() {
		if (seasonSetupPage != null) {
			seasonSetupPage.returnToSeasonSetupPage();
		}

		// Cerrar la ventana actual
		dispose();
	}
	private void showMatchesForSelectedYear() {
	    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
	        // Consulta SQL para recuperar las jornadas y los partidos del año seleccionado, ordenadas por el número de jornada en orden ascendente
	        String query = "SELECT NumJor, Partido1Local, Partido1Visitante, GolesLocalPartido1, GolesVisitantePartido1, " +
	                       "Partido2Local, Partido2Visitante, GolesLocalPartido2, GolesVisitantePartido2, " +
	                       "Partido3Local, Partido3Visitante, GolesLocalPartido3, GolesVisitantePartido3 " +
	                       "FROM jornada WHERE Año = ? ORDER BY NumJor ASC";
	        
	        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	            pstmt.setString(1, String.valueOf(selectedYear));
	            ResultSet rs = pstmt.executeQuery();
	            
	            // Crear un StringBuilder para almacenar el texto de las jornadas y los partidos
	            StringBuilder matchesText = new StringBuilder();
	            
	            // Iterar sobre los resultados de la consulta
	            while (rs.next()) {
	                int numJor = rs.getInt("NumJor");
	                String partido1Local = rs.getString("Partido1Local");
	                String partido1Visitante = rs.getString("Partido1Visitante");
	                int golesLocalPartido1 = rs.getInt("GolesLocalPartido1");
	                int golesVisitantePartido1 = rs.getInt("GolesVisitantePartido1");
	                String partido2Local = rs.getString("Partido2Local");
	                String partido2Visitante = rs.getString("Partido2Visitante");
	                int golesLocalPartido2 = rs.getInt("GolesLocalPartido2");
	                int golesVisitantePartido2 = rs.getInt("GolesVisitantePartido2");
	                String partido3Local = rs.getString("Partido3Local");
	                String partido3Visitante = rs.getString("Partido3Visitante");
	                int golesLocalPartido3 = rs.getInt("GolesLocalPartido3");
	                int golesVisitantePartido3 = rs.getInt("GolesVisitantePartido3");
	                
	                // Construir el texto para cada jornada y partido
	                String matchText = String.format("Jornada %d:\n\n" +
	                                                 "Partido 1: %s vs %s - Goles: %d-%d\n" +
	                                                 "Partido 2: %s vs %s - Goles: %d-%d\n" +
	                                                 "Partido 3: %s vs %s - Goles: %d-%d\n\n",
	                                                 numJor,
	                                                 partido1Local, partido1Visitante, golesLocalPartido1, golesVisitantePartido1,
	                                                 partido2Local, partido2Visitante, golesLocalPartido2, golesVisitantePartido2,
	                                                 partido3Local, partido3Visitante, golesLocalPartido3, golesVisitantePartido3);
	                
	                matchesText.append(matchText);
	            }
	            
	            // Crear un JTextArea para mostrar el texto de las jornadas y los partidos
	            JTextArea textArea = new JTextArea(matchesText.toString());
	            textArea.setEditable(false);
	            
	            // Envolver el JTextArea dentro de un JScrollPane
	            JScrollPane scrollPane = new JScrollPane(textArea);
	            
	            // Configurar el tamaño preferido del JScrollPane
	            scrollPane.setPreferredSize(new Dimension(400, 300));
	            
	            // Configurar la política de desplazamiento vertical
	            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	            
	            // Mostrar el contenido en un popup
	            JOptionPane.showMessageDialog(null, scrollPane, "Jornadas y Partidos", JOptionPane.INFORMATION_MESSAGE);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(null, "Error al cargar las jornadas desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

}
