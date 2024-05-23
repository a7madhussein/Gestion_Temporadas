package com.standings.ui.page;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SportsDashboardPage extends ParentFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JTabbedPane tabbedPane;
	private JPanel panelButton;
	private JTable standingsTable;
	private DefaultTableModel standingsTableModel;
	private List<Team> teams;
	private List<Match> allMatches;
	private JButton updateResultsButton;
	private JButton generatePdfButton;
	private JButton generateXmlButton;
	@SuppressWarnings("unused")
	private int currentRoundNumber = 1;
	private StandingsDialog standingsDialog;

	private SeasonSetupPage seasonSetupPage;
	private int selectedYear;

	public SportsDashboardPage(Season season, List<Match> matches, SeasonSetupPage seasonSetupPage, int selectedYear) {
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.seasonSetupPage = seasonSetupPage;
		this.selectedYear = selectedYear;
		initializeFrameGraphics();
		initializePanelGraphics();
		initializeStandingsTable();
		initializeStandingsData(season.getTeams());

		if (matches != null) {
			this.allMatches = new ArrayList<>(matches);
		} else {
			this.allMatches = new ArrayList<>(season.getMatches());

		}
		initializeTabs();
		distributeMatches();
		initializeMatchesData(season.getMatches());
		
			
	}
//Método para mostrar una confirmación al intentar cerrar la aplicación

	private void confirmExit() {
		int result = JOptionPane.showConfirmDialog(this,
				"¿Está seguro de que desea cerrar la aplicación?\nAsegúrese de finalizar la temporada, ya que cerrar la aplicación cancelará la operación.",
				"Confirmar salida", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			// Cerrar la aplicación
			vaciarTablaJugadorEquipo();
			deleteCurrentSeason();
			System.exit(0);
		}

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
		initializeMainPanel();
		initializePanelButton();
		lastPaint();
	}

	private void initializeMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
	}

	private void initializePanelButton() {
		panelButton = new JPanel();
		panelButton.setBounds(0, 0, 1522, 60);
		panelButton.setBackground(Color.WHITE);
		panelButton.setLayout(null);
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
		standingsTable.setRowHeight(30); // Ajustar el tamaño de las filas
		standingsTable.setFont(new Font("Arial", Font.PLAIN, 12)); // Ajustar el tamaño de la fuente

		// Centrar el contenido de las celdas
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		for (int i = 0; i < standingsTable.getColumnCount(); i++) {
			standingsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		}

		standingsTable.setEnabled(false);
		mainPanel.add(standingsTable, BorderLayout.SOUTH);

	}

	private void initializeTabs() {
		tabbedPane = new JTabbedPane();

		// Pestaña de Clasificación

		JPanel standingsPanel = new JPanel();
		standingsPanel.setLayout(new BorderLayout());
		standingsPanel.add(new JScrollPane(standingsTable), BorderLayout.CENTER);
		tabbedPane.addTab("Clasificación", standingsPanel);

		// Pestañas de Jornadas 1 a 10

		for (int roundNumber = 1; roundNumber <= 10; roundNumber++) {
			JPanel roundPanel = createRoundPanel(roundNumber);
			tabbedPane.addTab("Jornada " + roundNumber, roundPanel);
		}

		mainPanel.add(tabbedPane, BorderLayout.NORTH);

		// Añadir un listener para detectar cambios de pestañas
		tabbedPane.addChangeListener(e -> {
			if (tabbedPane.getSelectedIndex() != 0) {
				openStandingsDialog();
			} else {

				closeStandingsDialog();
			}
		});
	}

	private void openStandingsDialog() {
		if (standingsDialog == null) {
			standingsDialog = new StandingsDialog((JFrame) SwingUtilities.getWindowAncestor(this),
					(DefaultTableModel) standingsTable.getModel());
		}

		standingsDialog.setVisible(true);
	}

	private void closeStandingsDialog() {
		standingsDialog.dispose();

	}

	private JPanel createRoundPanel(int roundNumber) {
		JPanel roundPanel = new JPanel();

		// Boton de acutualizacion de resultados
		updateResultsButton = new JButton("Actualizar Resultados");
		updateResultsButton.setBounds(361, 372, 162, 30);
		updateResultsButton.setPreferredSize(new Dimension(30, 30));
		updateResultsButton.setEnabled(false); // Desactivar el botón al inicio
		updateResultsButton.setBackground(Color.lightGray);
		updateResultsButton.addActionListener(e -> {
			SwingUtilities.invokeLater(() -> {
				int selectedTabIndex = tabbedPane.getSelectedIndex();
				if (updateRoundResults(selectedTabIndex)) {
					updateResults(selectedTabIndex, selectedTabIndex);

				}
			});
		});

		// Crear un panel para los botones
		JPanel buttonsPanel = new JPanel();

		// Crear botón para generar PDF
		JButton pdfButton = new JButton("Generar PDF");
		pdfButton.setBackground(Color.lightGray);
		pdfButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Lógica para generar el PDF
				generatePdf();
			}
		});

		// Crear botón para generar XML
		JButton xmlButton = new JButton("Generar XML");
		xmlButton.setBackground(Color.lightGray);
		xmlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Lógica para generar el XML
				generateXML();
			}
		});
		// Crear botón para finalizar la temporada
		JButton Finalizar = new JButton("Finalizar Temporada");
		Finalizar.setBackground(Color.lightGray);
		Finalizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Lógica para finalizar la temporada
				finalizarTemporada();
			}
		});
		// Agregar los botones al panel de botones
		buttonsPanel.add(pdfButton);
		buttonsPanel.add(xmlButton);
		buttonsPanel.add(Finalizar);

		// Agregar el panel de botones al mainPanel
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int selectedIndex = tabbedPane.getSelectedIndex();
				// Mostrar u ocultar el panel de botones según la pestaña seleccionada
				buttonsPanel.setVisible(selectedIndex == 0);
			}
		});

		// Pestaña de partidos de ida y vuelta
		JPanel matchesPanel = new JPanel();
		matchesPanel.setBounds(0, 0, 871, 371);
		matchesPanel.setLayout(new GridLayout(3, 2));

		if (this.allMatches != null) {
			for (int i = 1; i <= 3; i++) {
				if ((roundNumber - 1) * 3 + (i - 1) < allMatches.size()) {
					Match match = allMatches.get((roundNumber - 1) * 3 + (i - 1));

					JPanel matchPanel = createMatchPanel(match, updateResultsButton);
					matchesPanel.add(matchPanel);
				}
			}
		}

		roundPanel.setLayout(null);

		roundPanel.add(matchesPanel);
		roundPanel.add(updateResultsButton);

		return roundPanel;
	}

	private JPanel createMatchPanel(Match match, JButton updateResultsButton) {
		JPanel matchPanel = new JPanel();
		matchPanel.setLayout(null);

		JLabel labelLocal = new JLabel();
		labelLocal.setBounds(250, 70, 120, 20);
		labelLocal.setFont(new Font("Arial", Font.BOLD, 14));

		JLabel labelVisitante = new JLabel();
		labelVisitante.setBounds(496, 70, 120, 20);
		labelVisitante.setFont(new Font("Arial", Font.BOLD, 14));

		JLabel labelGuion = new JLabel("-");
		labelGuion.setHorizontalAlignment(SwingConstants.CENTER);
		labelGuion.setBounds(389, 70, 58, 20);
		labelGuion.setFont(new Font("Arial", Font.BOLD, 14));

		JTextField textFieldLocal = new JTextField(10);
		textFieldLocal.setBounds(364, 70, 39, 20);
		textFieldLocal.setHorizontalAlignment(JTextField.CENTER);
		textFieldLocal.setName("Local");

		JTextField textFieldVisitante = new JTextField(10);
		textFieldVisitante.setBounds(434, 70, 39, 20);
		textFieldVisitante.setHorizontalAlignment(JTextField.CENTER);
		textFieldVisitante.setName("Visitante");

		// Agregar FocusListener al JTextField para activar/desactivar el botón cuando
		// se obtiene/perde el foco

		FocusListener focusListener = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				updateResultsButton.setEnabled(true);
			}

			@Override
			public void focusLost(FocusEvent e) {

			}
		};

		textFieldLocal.addFocusListener(focusListener);
		textFieldVisitante.addFocusListener(focusListener);

		matchPanel.add(labelLocal);
		matchPanel.add(textFieldLocal);
		matchPanel.add(labelGuion);
		matchPanel.add(textFieldVisitante);
		matchPanel.add(labelVisitante);

		if (match != null) {
			labelLocal.setText(match.getHomeTeam().getName());
			labelVisitante.setText(match.getAwayTeam().getName());
		}

		// Agregar los JTextField al panel del partido como propiedades del cliente
		matchPanel.putClientProperty("TextFieldLocal", textFieldLocal);
		matchPanel.putClientProperty("TextFieldVisitante", textFieldVisitante);

		return matchPanel;
	}

	private void initializeStandingsData(List<Team> teams) {
		this.teams = teams;

		for (Team team : teams) {
			standingsTableModel.addRow(new Object[] { team.getName(), 0, 0, 0, 0, 0, 0, 0, 0 });
		}
	}

	private boolean updateRoundResults(int roundNumber) {
		// Verificar el índice de la pestaña seleccionada
		if (roundNumber < 1 || roundNumber > 10) {
			return false;
		}

		for (Match match : allMatches) {
			if (match.getRoundNumber() == roundNumber) {
				Component[] components = ((JPanel) tabbedPane.getComponentAt(roundNumber)).getComponents();

				for (Component component : components) {
					if (component instanceof JPanel) {
						JPanel matchesPanel = (JPanel) component;

						for (Component matchComponent : matchesPanel.getComponents()) {
							if (matchComponent instanceof JPanel) {
								JPanel matchPanel = (JPanel) matchComponent;

								for (Component scoreComponent : matchPanel.getComponents()) {
									if (scoreComponent instanceof JTextField) {
										JTextField scoreField = (JTextField) scoreComponent;

										// Obtener el valor del campo y verificar si es válido
										String scoreText = scoreField.getText().trim();

										if (!scoreText.isEmpty()) {
											try {
												int scoreValue = Integer.parseInt(scoreText);

												if (!isValidScore(scoreValue)) {
													// Mostrar mensaje de error
													JOptionPane.showMessageDialog(this,
															"Por favor, ingrese resultados válidos (números entre 0 y 99) en todos los partidos.",
															"Error", JOptionPane.ERROR_MESSAGE);
													return false;
												}
											} catch (NumberFormatException e) {
												// Mostrar mensaje de error para caracteres no numéricos
												JOptionPane.showMessageDialog(this,
														"Por favor, ingrese solo números enteros en todos los campos.", "Error",
														JOptionPane.ERROR_MESSAGE);
												return false;
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// Devolver true si todas las validaciones pasan
		return true;
	}

	private boolean isValidScore(int score) {
		return score >= 0 && score <= 99;
	}

	private void initializeMatchesData(List<Match> matches) {
		if (matches != null) {
			// System.out.println("Initializing matches with existing data. Matches count: "
			// + matches.size());
			this.allMatches = new ArrayList<>(matches);
		} else {
			// System.out.println("Initializing matches with no existing data.");
			this.allMatches = new ArrayList<>();
			distributeMatches();
		}
	}

	private void distributeMatches() {
		int totalTeams = teams.size();
		int matchesPerRound = totalTeams / 2;

		// Distribuir partidos para las rondas 1 a 10 (ida)

		for (int roundNumber = 1; roundNumber <= 5; roundNumber++) {
			distributeMatchesForRound(roundNumber, matchesPerRound);
		}

		// Distribuir partidos para las rondas 6 a 10 (vuelta)

		for (int roundNumber = 6; roundNumber <= 10; roundNumber++) {
			distributeMatchesForRound(roundNumber, matchesPerRound);
		}
	}

	private void distributeMatchesForRound(int roundNumber, int matchesPerRound) {
		List<Team> homeTeams = new ArrayList<>(teams.subList(0, matchesPerRound));
		List<Team> awayTeams = new ArrayList<>(teams.subList(matchesPerRound, teams.size()));

		for (int i = 0; i < matchesPerRound; i++) {
			Team homeTeam = homeTeams.get(i);
			Team awayTeam = awayTeams.get(i);

			// Crear partido de ida

			Match match = new Match(homeTeam, awayTeam, roundNumber, i + 1);
			allMatches.add(match);
		}

		// Crear partido de vuelta

		for (int i = 0; i < matchesPerRound; i++) {
			Team homeTeam = awayTeams.get(i);
			Team awayTeam = homeTeams.get(i);

			Match match = new Match(homeTeam, awayTeam, roundNumber + 5, i + 1);
			allMatches.add(match);
		}

		// Rotar equipos para la próxima ronda (excepto el primero)

		Team firstTeam = homeTeams.remove(0);
		homeTeams.add(awayTeams.remove(awayTeams.size() - 1));
		homeTeams.add(0, firstTeam);
		awayTeams.add(awayTeams.remove(0));

		// Actualizar la lista general de equipos
		List<Team> updatedTeams = new ArrayList<>(homeTeams);
		updatedTeams.addAll(awayTeams);
		teams = updatedTeams;
	}

	private void updateMatchResults(Match match, int goalsHomeTeam, int goalsAwayTeam, boolean isUpdated) {
		// Verificar si el objeto result está inicializado
		if (match.getResult() == null) {
			// Si no está inicializado, crear un nuevo objeto MatchResult
			match.setResult(new MatchResult());
		}

		// Comprobar si los resultados han cambiado
		if (match.getResult().getGoalsHomeTeam() != goalsHomeTeam
				|| match.getResult().getGoalsAwayTeam() != goalsAwayTeam) {
			// Si hay algún cambio, actualizar resultados y estadísticas

			// Actualizar las estadísticas de los equipos solo si se ingresaron resultados
			if (goalsHomeTeam >= 0 && goalsAwayTeam >= 0) {
				match.getHomeTeam().updateStatistics(goalsHomeTeam, goalsAwayTeam, isUpdated,
						match.getResult().getGoalsHomeTeam(), match.getResult().getGoalsAwayTeam());
				match.getAwayTeam().updateStatistics(goalsAwayTeam, goalsHomeTeam, isUpdated,
						match.getResult().getGoalsAwayTeam(), match.getResult().getGoalsHomeTeam());
			}

			// Aquí necesitas verificar si el partido ya ha sido actualizado
			if (!isUpdated) {
				// Si el partido no ha sido actualizado, marca el partido como actualizado y
				// actualiza los resultados
				match.setUpdated(true);
			}

			// Actualiza los resultados
			match.getResult().setGoalsHomeTeam(goalsHomeTeam);
			match.getResult().setGoalsAwayTeam(goalsAwayTeam);
		}
	}
// logica del boton actualizar resultados

	private void updateResults(int roundNumber, int selectedTabIndex) {
		currentRoundNumber = selectedTabIndex;
		boolean isUpdated = false;
		int number = 1;
		boolean incompleteMatchFound = false;
		boolean previousMatchesComplete = true;

		for (int i = 0; i < 30; i++) {
			Match match = allMatches.get(i);
			int updatematch = i;

			if (match.getRoundNumber() == roundNumber) {
				Component[] components = ((JPanel) tabbedPane.getComponentAt(roundNumber)).getComponents();

				for (Component component : components) {
					if (component instanceof JPanel) {
						JPanel matchesPanel = (JPanel) component;

						for (Component matchComponent : matchesPanel.getComponents()) {
							if (matchComponent instanceof JPanel && !isUpdated) {
								JPanel matchPanel = (JPanel) matchComponent;

								JTextField textFieldLocal = (JTextField) matchPanel.getComponent(1);
								JTextField textFieldVisitante = (JTextField) matchPanel.getComponent(3);

								String goalsHomeTeamStr = textFieldLocal.getText().trim();
								String goalsAwayTeamStr = textFieldVisitante.getText().trim();

								number++;

								if (number == 4) {
									isUpdated = true;
								}

								if (!goalsHomeTeamStr.isEmpty() || !goalsAwayTeamStr.isEmpty()) {
									if (goalsHomeTeamStr.isEmpty() || goalsAwayTeamStr.isEmpty()) {
										incompleteMatchFound = true;
										break;
									}

									if (!previousMatchesComplete) {
										JOptionPane.showMessageDialog(this,
												"Por favor, asegúrese de completar los resultados de los partidos anteriores antes de actualizar este.",
												"Error", JOptionPane.ERROR_MESSAGE);
										return;
									}

									int goalsHomeTeam = Integer.parseInt(goalsHomeTeamStr);
									int goalsAwayTeam = Integer.parseInt(goalsAwayTeamStr);

									if (match.getResult().getGoalsHomeTeam() != goalsHomeTeam
											|| match.getResult().getGoalsAwayTeam() != goalsAwayTeam) {
										if (match.isUpdated()) {
											updateMatchResults(match, goalsHomeTeam, goalsAwayTeam, true);
										} else {
											updateMatchResults(match, goalsHomeTeam, goalsAwayTeam, false);
										}
										isUpdated = true;
									}

									if (updatematch < 29) {
										updatematch++;
										match = allMatches.get(updatematch);
									}
								} else {
									previousMatchesComplete = false;
								}
							}
						}
						if (incompleteMatchFound) {
							JOptionPane.showMessageDialog(this,
									"Por favor, asegúrese de completar ambos resultados de cada partido antes de actualizar.", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}
			}
		}

		if (isUpdated) {
			clearStandingsTable();
			updateStandingsTable();
		}
	}

	private void updateStandingsTable() {
		// Limpiar la tabla
		clearStandingsTable();

		// Actualizar la tabla de clasificación
		for (int i = 0; i < teams.size(); i++) {
			Team team = teams.get(i);

			// Actualizar puntos, partidos jugados, victorias, empates y derrotas
			standingsTableModel.setValueAt(team.getPoints(), i, 1);
			standingsTableModel.setValueAt(team.getMatchesPlayed(), i, 2);
			standingsTableModel.setValueAt(team.getWins(), i, 3);
			standingsTableModel.setValueAt(team.getDraws(), i, 4);
			standingsTableModel.setValueAt(team.getLosses(), i, 5);

			// Restablecer los valores de goles y diferencia de goles
			standingsTableModel.setValueAt(team.getGoalsScored(), i, 6);
			standingsTableModel.setValueAt(team.getGoalsConceded(), i, 7);
			standingsTableModel.setValueAt(team.getGoalDifference(), i, 8);
		}

		// Ordenar la tabla de clasificación
		sortStandings();
	}

	private void clearStandingsTable() {
		// Limpiar la tabla de clasificación
		for (int i = 0; i < teams.size(); i++) {
			for (int j = 1; j <= 8; j++) {
				standingsTableModel.setValueAt(0, i, j);
			}
		}

	}

	private void sortStandings() {
		// Crear una lista de equipos para ordenar
		List<Team> sortedTeams = new ArrayList<>(teams);

		// Ordenar la lista según los criterios de clasificacion

		sortedTeams.sort(Comparator.comparing(Team::getPoints).thenComparing(Team::getGoalDifference)
				.thenComparing(Team::getGoalsScored).reversed());

		// Actualizar la tabla de clasificación con los equipos ordenados
		for (int i = 0; i < sortedTeams.size(); i++) {
			Team team = sortedTeams.get(i);
			standingsTableModel.setValueAt(team.getName(), i, 0);
			standingsTableModel.setValueAt(team.getPoints(), i, 1);
			standingsTableModel.setValueAt(team.getMatchesPlayed(), i, 2);
			standingsTableModel.setValueAt(team.getWins(), i, 3);
			standingsTableModel.setValueAt(team.getDraws(), i, 4);
			standingsTableModel.setValueAt(team.getLosses(), i, 5);
			standingsTableModel.setValueAt(team.getGoalsScored(), i, 6);
			standingsTableModel.setValueAt(team.getGoalsConceded(), i, 7);
			standingsTableModel.setValueAt(team.getGoalDifference(), i, 8);
		}
	}

	private void lastPaint() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == generatePdfButton) {
			// generatePdf();
		} else if (e.getSource() == generateXmlButton) {
			generateXML();
		}
	}

	private void generatePdf() {
		MessageFormat header = new MessageFormat("Clasificaión de la temporada " + selectedYear);
		MessageFormat footer = new MessageFormat("La Liga");

		try {

			standingsTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
			userDialog("Se ha exportado el archivo PDF", "Exportar PDF", JOptionPane.INFORMATION_MESSAGE);

		} catch (java.awt.print.PrinterException e) {
			System.err.format("error al imprimir", e.getMessage());
		}
	}

	public void userDialog(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);

	}

	public void generateXML() {
		try {
			DataExtractor extractor = new DataExtractor();
			List<Temporada> temporadas = extractor.getTemporadas();

			// Crear un documento XML
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Declaración XML
			doc.setXmlStandalone(true);
			doc.setXmlVersion("1.0");

			// Elemento raíz 'Liga'
			Element rootElement = doc.createElement("LaLiga");
			doc.appendChild(rootElement);

			// Para cada temporada en la lista
			for (Temporada temporada : temporadas) {
				// Elemento 'Temporada'
				Element temporadaElement = doc.createElement("Temporada");
				temporadaElement.setAttribute("Año", temporada.getAño());
				rootElement.appendChild(temporadaElement);

				// Para cada jornada en la temporada
				for (Jornada jornada : temporada.getJornadas()) {
					// Elemento 'Jornada'
					Element jornadaElement = doc.createElement("Jornada");
					jornadaElement.setAttribute("Numero", String.valueOf(jornada.getNumero()));
					temporadaElement.appendChild(jornadaElement);

					// Para cada partido en la jornada
					for (Partido partido : jornada.getPartidos()) {
						// Elemento 'Partido'
						Element partidoElement = doc.createElement("Partido");
						partidoElement.setAttribute("Local", partido.getLocal());
						partidoElement.setAttribute("Visitante", partido.getVisitante());
						partidoElement.setAttribute("GolesLocal", String.valueOf(partido.getGolesLocal()));
						partidoElement.setAttribute("GolesVisitante", String.valueOf(partido.getGolesVisitante()));
						jornadaElement.appendChild(partidoElement);
					}
				}

				// Elemento 'Equipos'
				Element equiposElement = doc.createElement("Equipos");
				temporadaElement.appendChild(equiposElement);

				// Para cada equipo en la temporada
				for (Equipo equipo : temporada.getEquipos()) {
					// Elemento 'Equipo'
					Element equipoElement = doc.createElement("Equipo");
					equipoElement.setAttribute("Nombre", equipo.getNombre());
					equipoElement.setAttribute("EscudoURL", equipo.getEscudoURL());
					equiposElement.appendChild(equipoElement);

					// Para cada jugador en el equipo
					for (Jugador jugador : equipo.getJugadores()) {
						// Elemento 'Jugador'
						Element jugadorElement = doc.createElement("Jugador");
						jugadorElement.setAttribute("Nombre", jugador.getNombre());
						jugadorElement.setAttribute("Apellido", jugador.getApellido());
						jugadorElement.setAttribute("RutaFoto", jugador.getRutaFoto());
						equipoElement.appendChild(jugadorElement);
					}
				}
			}

			// Escribir el documento XML en un archivo
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("laliga.xml"));

			transformer.transform(source, result);

			System.out.println("Archivo XML creado correctamente.");

		} catch (SQLException | ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}

	private void finalizarTemporada() {
		if (!todasLasJornadasCompletas()) {
			JOptionPane.showMessageDialog(null,
					"Por favor, asegúrate de completar todos los resultados antes de finalizar la temporada.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return; // No continuar con la finalización si hay jornadas sin resultados
		}
		int respuesta = JOptionPane.showConfirmDialog(null,
				"¿Estás seguro de que quieres finalizar la temporada?\n"
						+ "Una vez finalizada, no podrás editar ni cambiar los resultados.",
				"Finalizar Temporada", JOptionPane.YES_NO_OPTION);

		if (respuesta == JOptionPane.YES_OPTION) {
			// Guardar las jornadas y sus partidos en la base de datos
			guardarJornadasEnBaseDeDatos();
			guardarClasificacionEnBaseDeDatos();
			transferirJugadorEquipoHistorial();
			vaciarTablaJugadorEquipo();
			marcarTemporadaComoFinalizada();
			JOptionPane.showMessageDialog(null, "Temporada finalizada exitosamente.", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			if (seasonSetupPage != null) {
				seasonSetupPage.actualizarListaTemporadas();
				seasonSetupPage.setVisible(true);
			} else {
				System.out.println("Error: Instancia de SeasonSetupPage no proporcionada.");
			}

			dispose(); // Cerrar la ventana actual
		}
	}

	private boolean todasLasJornadasCompletas() {
		for (int roundNumber = 1; roundNumber <= 10; roundNumber++) {
			// Verificar si la jornada actual tiene resultados completos
			if (!jornadaCompleta(roundNumber)) {
				return false; // Al menos una jornada no está completa
			}
		}
		return true; // Todas las jornadas están completas
	}

	private boolean jornadaCompleta(int roundNumber) {
		for (Match match : allMatches) {
			if (match.getRoundNumber() == roundNumber) {
				// Verificar si el resultado de este partido está completo
				Component[] components = ((JPanel) tabbedPane.getComponentAt(roundNumber)).getComponents();

				for (Component component : components) {
					if (component instanceof JPanel) {
						JPanel matchesPanel = (JPanel) component;

						for (Component matchComponent : matchesPanel.getComponents()) {
							if (matchComponent instanceof JPanel) {
								JPanel matchPanel = (JPanel) matchComponent;

								JTextField textFieldLocal = (JTextField) matchPanel.getComponent(1);
								JTextField textFieldVisitante = (JTextField) matchPanel.getComponent(3);

								String goalsHomeTeamStr = textFieldLocal.getText().trim();
								String goalsAwayTeamStr = textFieldVisitante.getText().trim();

								if (goalsHomeTeamStr.isEmpty() || goalsAwayTeamStr.isEmpty()) {
									return false; // Al menos un partido no tiene resultados
								}
							}
						}
					}
				}
			}
		}
		return true; // Todos los partidos de la jornada tienen resultados
	}

	/**
	 * Esto se guarda la tabla en un fichero serializado par poder abrirlo luego y
	 * ver los datos
	 * 
	 * @author Grupo3
	 */

	private void guardarJornadasEnBaseDeDatos() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
			String insertQuery = "INSERT INTO jornada (CodJor, NumJor, Año, "
					+ "Partido1Local, Partido1Visitante, GolesLocalPartido1, GolesVisitantePartido1, "
					+ "Partido2Local, Partido2Visitante, GolesLocalPartido2, GolesVisitantePartido2, "
					+ "Partido3Local, Partido3Visitante, GolesLocalPartido3, GolesVisitantePartido3) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
				for (int roundNumber = 1; roundNumber <= 10; roundNumber++) {
					// Obtener los partidos de la jornada
					List<Match> matchesInRound = getMatchesInRound(roundNumber);
					// Asegurarse de que hay exactamente 3 partidos por jornada
					if (matchesInRound.size() != 3) {
						throw new IllegalStateException("Cada jornada debe tener exactamente 3 partidos.");
					}
					// Rellenar los detalles de la jornada
					pstmt.setString(1, "J" + roundNumber + selectedYear); // CodJor
					pstmt.setInt(2, roundNumber); // NumJor
					pstmt.setInt(3, selectedYear); // CodTemp
					for (int i = 0; i < 3; i++) {
						Match match = matchesInRound.get(i);
						int index = 4 + i * 4;
						pstmt.setString(index, match.getHomeTeam().getName()); // Nombre del equipo local
						pstmt.setString(index + 1, match.getAwayTeam().getName()); // Nombre del equipo visitante
						pstmt.setInt(index + 2, match.getResult().getGoalsHomeTeam()); // Goles del equipo local
						pstmt.setInt(index + 3, match.getResult().getGoalsAwayTeam()); // Goles del equipo visitante
					}
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al guardar las jornadas en la base de datos.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void guardarClasificacionEnBaseDeDatos() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
			String insertQuery = "INSERT INTO clasificacion (equipo, puntos, partidos_jugados, "
					+ "partidos_ganados, partidos_empatados, partidos_perdidos, goles_favor, "
					+ "goles_contra, diferencia_goles, año_temporada	) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
				// Rellenar los detalles de la clasificación
				for (Team team : teams) { // Suponiendo que 'teams' es tu lista de equipos
					pstmt.setString(1, team.getName()); // Nombre del equipo
					pstmt.setInt(2, team.getPoints()); // Puntos
					pstmt.setInt(3, team.getMatchesPlayed()); // Partidos jugados
					pstmt.setInt(4, team.getWins()); // Partidos ganados
					pstmt.setInt(5, team.getDraws()); // Partidos empatados
					pstmt.setInt(6, team.getLosses()); // Partidos perdidos
					pstmt.setInt(7, team.getGoalsScored()); // Goles a favor
					pstmt.setInt(8, team.getGoalsConceded()); // Goles en contra
					pstmt.setInt(9, team.getGoalDifference()); // Diferencia de goles
					pstmt.setString(10, String.valueOf(selectedYear)); // Año
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al guardar la clasificación en la base de datos.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private List<Match> getMatchesInRound(int roundNumber) {
		List<Match> matchesInRound = new ArrayList<>();
		for (Match match : allMatches) {
			if (match.getRoundNumber() == roundNumber) {
				matchesInRound.add(match);
			}
		}
		return matchesInRound;
	}

	private void marcarTemporadaComoFinalizada() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
			String updateQuery = "INSERT INTO temporada (Año, Estado) VALUES (?, ?) ON DUPLICATE KEY UPDATE Estado = VALUES(Estado)";
			try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
				pstmt.setString(1, String.valueOf(selectedYear));
				pstmt.setString(2, "Finalizado"); // Marcar la temporada como finalizada
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al marcar la temporada como finalizada en la base de datos.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void transferirJugadorEquipoHistorial() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
			String transferQuery = "INSERT INTO jugador_equipo_historial (NomEq, EscudoURL, NomJug, ApeJug, ruta_foto, Año) "
					+ "SELECT NomEq, EscudoURL, NomJug, ApeJug, ruta_foto, ? FROM jugador_equipo";
			try (PreparedStatement pstmt = connection.prepareStatement(transferQuery)) {
				pstmt.setInt(1, selectedYear);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al transferir datos de jugador_equipo a jugador_equipo_historial.",
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void vaciarTablaJugadorEquipo() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
			String deleteQuery = "DELETE FROM jugador_equipo";
			try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error al vaciar la tabla jugador_equipo.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void deleteCurrentSeason() {
		String url = "jdbc:mysql://localhost:3306/reto3";
		String user = "root";
		String password = "";
		String query = "DELETE FROM temporada WHERE Estado = 'actual'";

		try (Connection connection = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = connection.prepareStatement(query)) {

			int affectedRows = pstmt.executeUpdate();
			if (affectedRows > 0) {
				// System.out.println("Temporada actual eliminada correctamente.");
			} else {
				// System.out.println("No se encontró una temporada actual para eliminar.");
			}
		} catch (SQLException e) {
			// e.printStackTrace();
			System.out.println("Error al conectar a la base de datos o al eliminar la temporada actual.");
		}
	}
	@Override
	protected void processWindowEvent(WindowEvent e) {
	    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
	        confirmExit();
	    } else {
	        super.processWindowEvent(e);
	    }
	    }
}
