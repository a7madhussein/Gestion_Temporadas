package com.standings.ui.page;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class SeasonSetupPage extends ParentFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JPanel teamsPanel;
	private JTextField yearTextField;
	private JButton createSeasonButton;
	private JButton openSeasonButton;
	private JComboBox<String> temporadas;

	private DefaultListModel<String> seasonsListModel;
	private JList<String> seasonsList;
	private int selectedYear;
	private JButton btnEquipos;
	private JButton btnJugadores;
	private JButton btnxml;

	private static final String LOG_FILE = "log_control.txt";

//Clase principal que extiende JFrame e implementa ActionListener

	public SeasonSetupPage(LoginPage loginPage) {
		initializeFrameGraphics();
		initializePanelGraphics();
		loadTemporadas();

	}
//Métodos de inicialización de la interfaz

	private void initializeFrameGraphics() {
		setTitle("Configuración de Temporada");
		setResizable(false);
		setSizeAndCenter();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void setSizeAndCenter() {
		setSize(500, 300);
		setLocationRelativeTo(null);
	}

	private void initializePanelGraphics() {
		initializeMainPanel();
		initializeTeamsPanel();
		initializeYearTextField();
		initializeButtons();
		initializeSeasonsList();
		updateComboBoxFromSelectedSeason();
		lastPaint();
	}

	private void initializeMainPanel() {
		mainPanel = new JPanel(new BorderLayout());
	}

//Método para mostrar una confirmación al intentar cerrar la aplicación

	private void confirmExit() {
		int result = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea cerrar la aplicación?",
				"Confirmar salida", JOptionPane.YES_NO_OPTION);
		if (result == JOptionPane.YES_OPTION) {
			// Vaciar la tabla antes de cerrar la aplicación
			vaciarTablaJugadorEquipo();
			// Cerrar la aplicación solo si el usuario elige "Sí"
			System.exit(0);
		} else {
			// No hacer nada si el usuario elige "No"
			// Esto permite que la aplicación continúe ejecutándose
		}
	}

	// Configuración del panel de equipos con cajas de verificación
	public void initializeTeamsPanel() {
		teamsPanel = new JPanel();
		teamsPanel.setLayout(new BoxLayout(teamsPanel, BoxLayout.Y_AXIS));

		// Conectar a la base de datos y obtener los nombres únicos de los equipos
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT DISTINCT NomEq, EscudoURL FROM equipo");

			// Estructura de datos para almacenar los jugadores asignados a cada equipo
			Map<String, TeamInfo> teamInfoMap = new HashMap<>();

			// Obtener los nombres de los equipos y las URLs de los escudos
			while (resultSet.next()) {
				String teamName = resultSet.getString("NomEq");
				String teamLogoUrl = resultSet.getString("EscudoURL");
				TeamInfo teamInfo = new TeamInfo(teamName, teamLogoUrl);
				teamInfoMap.put(teamName, teamInfo);
			}

			// Obtener los jugadores asignados a cada equipo y sus detalles
			for (Map.Entry<String, TeamInfo> entry : teamInfoMap.entrySet()) {
				String teamName = entry.getKey();
				TeamInfo teamInfo = entry.getValue();
				teamInfo.setPlayerNames(getPlayerDetailsForTeam(teamName));
			}

			// Crear y agregar casillas de verificación para cada equipo
			for (Map.Entry<String, TeamInfo> entry : teamInfoMap.entrySet()) {
				String teamName = entry.getKey();
				TeamInfo teamInfo = entry.getValue();
				JCheckBox checkBox = new JCheckBox(teamName + " (" + teamInfo.getPlayerNames().size() + " jugadores)");
				teamsPanel.add(checkBox);
			}

			connection.close();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
			logAttempt("Error:" + e.getMessage());
		}

		JScrollPane scrollPane = new JScrollPane(teamsPanel);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	}

	// Método para obtener los detalles de los jugadores asignados a un equipo
	// específico
	private List<PlayerInfo> getPlayerDetailsForTeam(String teamName) throws SQLException {
		List<PlayerInfo> playerDetails = new ArrayList<>();
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");
		PreparedStatement statement = connection.prepareStatement(
				"SELECT jugador.NomJug, jugador.ApeJug, jugador.ruta_foto FROM jugador_equipo INNER JOIN jugador ON jugador_equipo.NomJug = jugador.NomJug AND jugador_equipo.ApeJug = jugador.ApeJug WHERE NomEq = ?");
		statement.setString(1, teamName);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			String playerName = resultSet.getString("NomJug") + " " + resultSet.getString("ApeJug");
			String playerPhotoUrl = resultSet.getString("ruta_foto");
			PlayerInfo playerInfo = new PlayerInfo(playerName, playerPhotoUrl);
			playerDetails.add(playerInfo);
		}
		connection.close();
		return playerDetails;
	}

	// Configuración del panel de año con JTextField y JComboBox

	private void initializeYearTextField() {
		JPanel yearPanel = new JPanel();
		yearTextField = new JTextField(5);

		btnxml = new JButton("Exportar XML");
		yearPanel.add(btnxml);
		btnxml.setBackground(Color.lightGray);
		btnxml.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GenerateXML();
			}
		});
		// Agregar el JComboBox de temporadas
		temporadas = new JComboBox<>();
		yearPanel.add(temporadas);

		temporadas.addItem("Nueva Temporada");
		yearPanel.add(new JLabel("Año:"));
		yearPanel.add(yearTextField);
		mainPanel.add(yearPanel, BorderLayout.NORTH);
	}
	// Configuración de los botones y su panel

	private void initializeButtons() {
		createSeasonButton = new JButton("Crear Temporada");
		createSeasonButton.setBackground(Color.lightGray);
		createSeasonButton.addActionListener(this);

		openSeasonButton = new JButton("Abrir Temporada");
		openSeasonButton.setBackground(Color.lightGray);
		openSeasonButton.addActionListener(this);

		// Crear un nuevo panel para los botones en la parte inferior
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		// Agregar los botones al panel de botones
		buttonPanel.add(createSeasonButton);
		buttonPanel.add(openSeasonButton);

		// Agregar el panel de botones al mainPanel
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);

		btnEquipos = new JButton("Equipos");
		btnEquipos.setBackground(Color.lightGray);
		btnEquipos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				VentanaGestionEquipos ventanaEquipos = new VentanaGestionEquipos();
				ventanaEquipos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ventanaEquipos.setVisible(true);
				dispose();
			}
		});
		buttonPanel.add(btnEquipos);

		btnJugadores = new JButton("Jugadores");
		btnJugadores.setBackground(Color.lightGray);
		btnJugadores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddPlayerWindow ventanaJugadores = new AddPlayerWindow();
				ventanaJugadores.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				ventanaJugadores.setVisible(true);
				dispose();

			}
		});
		buttonPanel.add(btnJugadores);

		// Agregar un ActionListener al JComboBox para manejar los cambios de selección
		temporadas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Verificar y ajustar la habilitación de los botones según la selección del
				// JComboBox
				String selectedSeason = (String) temporadas.getSelectedItem();
				boolean isNewSeason = "Nueva Temporada".equals(selectedSeason);

				// Habilitar o deshabilitar botones según la selección del JComboBox
				createSeasonButton.setEnabled(isNewSeason);
				openSeasonButton.setEnabled(!isNewSeason);
			}
		});
	}

	// Configuración de la lista de temporadas con información de estado
	private void initializeSeasonsList() {
		seasonsListModel = new DefaultListModel<>();
		seasonsList = new JList<>(seasonsListModel);

		seasonsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		seasonsList.setVisibleRowCount(5);
		seasonsList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int index = seasonsList.locationToIndex(e.getPoint());
				if (index >= 0) {
					String selectedSeason = seasonsListModel.getElementAt(index);
					String status = getStatusFromSeason(selectedSeason);
					seasonsList.setToolTipText("Estado: " + status);
				}
			}
		});
		seasonsList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					updateComboBoxFromSelectedSeason();
				}
			}
		});

		JScrollPane seasonsScrollPane = new JScrollPane(seasonsList);
		// Añade la lista al oeste del panel principal
		mainPanel.add(seasonsScrollPane, BorderLayout.WEST);
	}

	// Lógica para obtener el estado de la temporada desde la cadena seleccionada

	private String getStatusFromSeason(String selectedSeason) {
		// Extraer el estado de la temporada desde la cadena seleccionada
		int dashIndex = selectedSeason.indexOf("-");
		if (dashIndex != -1) {
			String status = selectedSeason.substring(dashIndex + 1).trim();

			// Personalizar el mensaje según el estado

			switch (status) {
			case "En Curso":
				return "Esta temporada está actualmente en curso. Puedes crear o editar la temporada.";
			case "Próxima":
				return "Esta temporada aún no ha comenzado. No se pueden crear nuevas temporadas hasta que finalice.";
			case "Finalizada":
				return "Esta temporada ha finalizado. Solo puedes ver la clasificación y las jornadas.";
			default:
				return "";
			}
		}
		return "";
	}
	// Lógica para actualizar el JComboBox desde la temporada seleccionada

	private void updateComboBoxFromSelectedSeason() {
		String selectedSeason = seasonsList.getSelectedValue();
		if (selectedSeason != null) {
			try {
				// Buscar el primer espacio en blanco
				int firstSpaceIndex = selectedSeason.indexOf(" ");

				if (firstSpaceIndex != -1) {
					// Extraer la cadena correspondiente al año (antes del primer espacio en blanco)
					String yearString = selectedSeason.substring(0, firstSpaceIndex);

					// Convertir la cadena del año a un número entero
					int selectedYear = Integer.parseInt(yearString);

					// Cargar las temporadas desde la base de datos
					List<Integer> temporadasList = getTemporadasFromDatabase();

					// Actualizar el JComboBox con las temporadas de la base de datos
					temporadas.removeAllItems();
					temporadas.addItem("Nueva Temporada");
					for (int year : temporadasList) {
						temporadas.addItem("Temporada " + year);
					}

					// Seleccionar el año en el JComboBox
					temporadas.setSelectedItem("Temporada " + selectedYear);
					clearTeamSelection();
				}
			} catch (NumberFormatException ex) {
				logAttempt("Error:" + ex.getMessage());

			}
		}
	}

	// Método para obtener la lista de temporadas desde la base de datos
	private List<Integer> getTemporadasFromDatabase() {
		List<Integer> temporadasList = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			// Establecer la conexión con la base de datos
			String url = "jdbc:mysql://localhost:3306/reto3";
			String user = "root";
			String password = "";

			connection = DriverManager.getConnection(url, user, password);

			// Consulta SQL para obtener la lista de años de temporadas
			String query = "SELECT Año FROM temporada";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			// Iterar sobre el resultado y agregar cada año a la lista
			while (resultSet.next()) {
				int year = resultSet.getInt("Año");
				temporadasList.add(year);
			}
		} catch (SQLException e) {
			logAttempt("Error:" + e.getMessage());
		} finally {
			// Cerrar recursos
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logAttempt("Error:" + e.getMessage());
			}
		}
		return temporadasList;
	}

	// Lógica para agregar componentes al panel principal

	private void lastPaint() {
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}
	// Manejo de eventos de botones

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == createSeasonButton) {
			createSeason();
		} else if (e.getSource() == openSeasonButton) {
			openSeason();
		}
	}

	public void insertarTemporadaEnDB(int year) {
		Connection conn = null;
		try {
			// Configurar los detalles de la conexión
			String url = "jdbc:mysql://localhost:3306/reto3";
			String user = "root";
			String password = "";

			// Obtener la conexión
			conn = DriverManager.getConnection(url, user, password);

			String sql = "INSERT INTO temporada (Año, Estado) VALUES (?, 'actual')";
			try (PreparedStatement stmt = conn.prepareStatement(sql)) {
				stmt.setInt(1, year);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error al insertar la temporada en la base de datos: " + e.getMessage(),
					"Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
			logAttempt("Error al insertar la temporada en la base de datos :" + e.getMessage());
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logAttempt("Error:" + e.getMessage());
					JOptionPane.showMessageDialog(null,
							"Error al cerrar la conexión de la base de datos: " + e.getMessage(),
							"Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	// Lógica para crear una nueva temporada

	private void createSeason() {
		Component[] components = teamsPanel.getComponents();
		List<String> selectedTeams = new ArrayList<>();
		for (Component component : components) {
			if (component instanceof JCheckBox) {
				JCheckBox checkBox = (JCheckBox) component;
				if (checkBox.isSelected()) {
					// Extraer solo el nombre del equipo, sin la cantidad de jugadores
					String teamName = checkBox.getText().split(" \\(")[0];
					selectedTeams.add(teamName);
				}
			}
		}

		// Verificar que se hayan seleccionado exactamente 6 equipos
		if (selectedTeams.size() != 6) {
			JOptionPane.showMessageDialog(this,
					"Debe seleccionar exactamente 6 equipos, si los equipos no son suficientes pulsa el boton equipos para añadir nuevos equipos.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Asegurarse de que cada equipo seleccionado tenga al menos un jugador
		if (!teamsHavePlayers(selectedTeams)) {
			JOptionPane.showMessageDialog(this, "Todos los equipos seleccionados deben tener al menos un jugador.",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Proceso para determinar y validar el año
		int currentYear = java.time.Year.now().getValue();
		int selectedYear;
		try {
			selectedYear = Integer.parseInt(yearTextField.getText());
			if (selectedYear < currentYear) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Ingrese un año válido y razonable.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		insertarTemporadaEnDB(selectedYear);

		// Crear la temporada y manejar el archivo de temporada
		Season season = new Season(selectedTeams);
		String seasonStatus = determineSeasonStatus(selectedYear, currentYear);
		seasonsListModel.addElement(String.valueOf(selectedYear) + " - " + seasonStatus);
		SportsDashboardPage dashboardPage = new SportsDashboardPage(season, season.getMatches(), this, selectedYear);
		dashboardPage.setVisible(true);
		dispose();
	}

	// Método auxiliar para comprobar que todos los equipos seleccionados tienen
	// jugadores
	private boolean teamsHavePlayers(List<String> teams) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");
				PreparedStatement stmt = connection
						.prepareStatement("SELECT COUNT(*) FROM jugador_equipo WHERE NomEq = ?")) {
			for (String team : teams) {
				stmt.setString(1, team);
				ResultSet rs = stmt.executeQuery();
				if (rs.next() && rs.getInt(1) == 0) {
					return false;
				}
			}
		} catch (SQLException e) {
			logAttempt("Error:" + e.getMessage());
		}
		return true;
	}

	private String determineSeasonStatus(int selectedYear, int currentYear) {
		// Obtener los años de temporadas finalizadas desde la base de datos
		List<Integer> finishedSeasons = getFinishedSeasonsFromDatabase();

		// Determinar el estado de la temporada según los años existentes
		if (finishedSeasons.isEmpty()) {
			// Si no hay temporadas finalizadas, la nueva temporada es la actual
			return "En Curso";
		} else {
			// Obtener el año más reciente de las temporadas finalizadas
			int latestFinishedYear = finishedSeasons.get(finishedSeasons.size() - 1);

			if (selectedYear == latestFinishedYear + 1) {
				// La nueva temporada es la próxima
				return "Próxima";
			} else if (selectedYear <= currentYear) {
				// La nueva temporada ha finalizado
				return "Finalizada";
			} else {
				// La nueva temporada es actual
				return "En Curso";
			}
		}
	}

	// Método para obtener los años de temporadas finalizadas desde la base de datos
	private List<Integer> getFinishedSeasonsFromDatabase() {
		List<Integer> finishedSeasons = new ArrayList<>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			// Establecer la conexión con la base de datos
			String url = "jdbc:mysql://localhost:3306/reto3";
			String user = "root";
			String password = "";

			connection = DriverManager.getConnection(url, user, password);

			// Consulta SQL para obtener la lista de años de temporadas finalizadas
			String query = "SELECT Año FROM temporada ORDER BY Año ASC";
			statement = connection.prepareStatement(query);
			resultSet = statement.executeQuery();

			// Iterar sobre el resultado y agregar cada año a la lista
			while (resultSet.next()) {
				int year = resultSet.getInt("Año");
				finishedSeasons.add(year);
			}
		} catch (SQLException e) {
			logAttempt("Error:" + e.getMessage());
		} finally {
			// Cerrar recursos
			try {
				if (resultSet != null)
					resultSet.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logAttempt("Error:" + e.getMessage());
			}
		}
		return finishedSeasons;
	}

	private void openSeason() {
		// Obtener el año seleccionado
		String selectedSeason = (String) temporadas.getSelectedItem();

		if (selectedSeason.equals("Nueva Temporada")) {
			// No hacer nada si se selecciona "Nueva Temporada"
		} else {
			// Extraer el año de la temporada seleccionada
			int selectedYear = Integer.parseInt(selectedSeason.substring(10));

			// Construir el nombre del archivo correspondiente al año seleccionado
			String fileName = "clasificacion" + selectedYear + ".ser";

			// Abrir la ventana de SportsDashboardPage1 con el año seleccionado y el nombre
			// del archivo
			SportsDashboardPage1 dashboardPage1 = new SportsDashboardPage1(null, null, this, selectedYear, fileName,
					this);
			dashboardPage1.setVisible(true);
		}

		// Cerrar la ventana actual después de abrir la temporada
		dispose();
	}

	// Cargar la temporada
	private void loadTemporadas() {
		// Limpiar elementos anteriores
		temporadas.removeAllItems();
		temporadas.addItem("Nueva Temporada");
		seasonsListModel.removeAllElements();

		// Cargar las temporadas finalizadas desde la base de datos
		List<Integer> finishedSeasons = getFinishedSeasonsFromDatabase();

		for (int finishedYear : finishedSeasons) {
			addSeasonToList(finishedYear, "Finalizada");
			temporadas.addItem("Temporada " + finishedYear);
		}

		if (!finishedSeasons.isEmpty()) {
			int currentYear = finishedSeasons.get(finishedSeasons.size() - 1) + 1;
			yearTextField.setText(Integer.toString(currentYear));
			yearTextField.setEditable(false);

			int nextYear = currentYear + 1;
			addSeasonToList(currentYear, "En Curso");
			addSeasonToList(nextYear, "Próxima");

		}
	}

	// Método auxiliar para añadir temporadas a la lista
	private void addSeasonToList(int year, String status) {
		seasonsListModel.addElement(year + " - " + status);
	}

	private void clearTeamSelection() {
		Component[] components = teamsPanel.getComponents();

		for (Component component : components) {
			if (component instanceof JCheckBox) {
				JCheckBox checkBox = (JCheckBox) component;
				checkBox.setSelected(false);
			}
		}
	}

	// Lógica para cargar y actualizar la lista de temporadas
	void actualizarListaTemporadas() {

		loadTemporadas();
	}

	public int getSelectedYear() {
		return selectedYear;
	}

	public void returnToSeasonSetupPage() {
		// Lógica para volver a SeasonSetupPage
		this.setVisible(true);
	}

	public void GenerateXML() {
		try {
			DataExtractor extractor = new DataExtractor();
			List<Temporada> temporadas = extractor.getTemporadas();
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			// Declaración XML
			doc.setXmlVersion("1.0");
			doc.setXmlStandalone(true);

			// Elemento raíz
			Element rootElement = doc.createElement("LaLiga");
			doc.appendChild(rootElement);

			for (Temporada temporada : temporadas) {
				Element temporadaElement = doc.createElement("Temporada");
				rootElement.appendChild(temporadaElement);

				// Crear un elemento de texto para el año dentro de la etiqueta Temporada
				Element añoElement = doc.createElement("Año");
				añoElement.appendChild(doc.createTextNode(temporada.getAño()));
				temporadaElement.appendChild(añoElement);

				// Obtener jornadas y ordenar por número
				List<Jornada> jornadas = temporada.getJornadas();
				Collections.sort(jornadas, Comparator.comparingInt(Jornada::getNumero));

				for (Jornada jornada : jornadas) {
					Element jornadaElement = doc.createElement("Jornada");
					jornadaElement.setAttribute("Numero", String.valueOf(jornada.getNumero()));
					temporadaElement.appendChild(jornadaElement);

					for (Partido partido : jornada.getPartidos()) {
						Element partidoElement = doc.createElement("Partido");
						partidoElement.setAttribute("Local", partido.getLocal());
						partidoElement.setAttribute("GolesLocal", String.valueOf(partido.getGolesLocal()));
						partidoElement.setAttribute("Visitante", partido.getVisitante());
						partidoElement.setAttribute("GolesVisitante", String.valueOf(partido.getGolesVisitante()));
						jornadaElement.appendChild(partidoElement);
					}
				}

				Element equiposElement = doc.createElement("Equipos");
				temporadaElement.appendChild(equiposElement);

				for (Equipo equipo : temporada.getEquipos()) {
					Element equipoElement = doc.createElement("Equipo");
					equipoElement.setAttribute("Nombre", equipo.getNombre());
					equipoElement.setAttribute("EscudoURL", equipo.getEscudoURL());
					equiposElement.appendChild(equipoElement);

					for (Jugador jugador : equipo.getJugadores()) {
						Element jugadorElement = doc.createElement("Jugador");
						jugadorElement.setAttribute("Nombre", jugador.getNombre());
						jugadorElement.setAttribute("Apellido", jugador.getApellido());
						jugadorElement.setAttribute("RutaFoto", jugador.getRutaFoto());
						equipoElement.appendChild(jugadorElement);
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("laliga.xml"));

			transformer.transform(source, result);

		} catch (SQLException | ParserConfigurationException | TransformerException e) {
			logAttempt("Error:" + e.getMessage());
		}
	}

	private void vaciarTablaJugadorEquipo() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "")) {
			String deleteQuery = "DELETE FROM jugador_equipo";
			try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			logAttempt("Error:" + e.getMessage());;
			JOptionPane.showMessageDialog(null, "Error al vaciar la tabla jugador_equipo.", "Error",
					JOptionPane.ERROR_MESSAGE);
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
	private void logAttempt(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            writer.println(timestamp + " - " + message);
        } catch (IOException e) {
        	logAttempt("Error:" + e.getMessage());
        }
    }
}
