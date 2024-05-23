package com.standings.ui.page;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class VentanaGestionEquipos extends ParentFrame {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private Connection conexion;
	private JTextField txtCodigo, txtNombre, txtPresidente, txtCantSocios, txtCodEstadio;
	private JButton btnAgregar, btnEliminar, btnEditar, btnSubirFoto, btnAsignarJugador;
	private DefaultTableModel equiposTableModel;
	private JTable tablaEquipos;
	private DefaultListModel<String> listaJugadoresModel;
	private JList<String> listaJugadores;
	private String rutaFotoSeleccionada;
	private static final String LOG_FILE = "log_control.txt";
	
	public VentanaGestionEquipos() {
		try {
			// Establecer conexión con la base de datos
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");

			// Configuración básica de la ventana
			setTitle("Gestión de Equipos");
			setSize(944, 600);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null); // Centrar la ventana en la pantalla

			// Panel principal
			JPanel panelPrincipal = new JPanel();

			// Panel de formulario para agregar/editar equipos
			JPanel panelFormulario = new JPanel();
			panelFormulario.setBounds(0, 1, 927, 100);
			panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));

			// Campos de texto para ingresar información del equipo
			txtCodigo = new JTextField(10);
			txtCodigo.setBounds(65, 10, 75, 21);
			txtNombre = new JTextField(5);
			txtNombre.setBounds(219, 10, 97, 21);
			txtNombre.setHorizontalAlignment(SwingConstants.CENTER);
			txtPresidente = new JTextField(20);
			txtPresidente.setBounds(404, 10, 97, 21);
			txtCantSocios = new JTextField(10);
			txtCantSocios.setBounds(650, 10, 34, 21);
			txtCodEstadio = new JTextField(10);
			txtCodEstadio.setBounds(831, 10, 34, 21);

			// Botón para subir foto
			btnSubirFoto = new JButton("Subir Foto");
			btnSubirFoto.setBounds(66, 56, 119, 21);
			btnSubirFoto.setBackground(Color.lightGray);
			btnSubirFoto.addActionListener(e -> subirFoto());

			// Etiquetas para los campos de texto
			JLabel lblCodigo = new JLabel("Código:");
			lblCodigo.setBounds(11, 10, 44, 21);
			JLabel lblNombre = new JLabel("Nombre:");
			lblNombre.setBounds(156, 10, 53, 21);
			JLabel lblPresidente = new JLabel("Presidente:");
			lblPresidente.setBounds(324, 10, 70, 21);
			JLabel lblCantSocios = new JLabel("Cantidad de Socios:");
			lblCantSocios.setBounds(521, 10, 119, 21);
			JLabel lblCodEstadio = new JLabel("Código de Estadio:");
			lblCodEstadio.setBounds(703, 10, 131, 21);
			JLabel lblEscudoURL = new JLabel("Escudo:");
			lblEscudoURL.setBounds(11, 56, 75, 21);
			panelFormulario.setLayout(null);

			// Agregar componentes al panel de formulario
			panelFormulario.add(lblCodigo);
			panelFormulario.add(txtCodigo);
			panelFormulario.add(lblNombre);
			panelFormulario.add(txtNombre);
			panelFormulario.add(lblPresidente);
			panelFormulario.add(txtPresidente);
			panelFormulario.add(lblCantSocios);
			panelFormulario.add(txtCantSocios);
			panelFormulario.add(lblCodEstadio);
			panelFormulario.add(txtCodEstadio);
			panelFormulario.add(lblEscudoURL);
			panelFormulario.add(btnSubirFoto);

			// Panel de botones
			JPanel panelBotones = new JPanel();
			panelBotones.setBounds(452, 122, 217, 441);
			btnAgregar = new JButton("Agregar");
			btnAgregar.setBackground(Color.lightGray);
			btnAgregar.setBounds(20, 28, 92, 21);
			btnEliminar = new JButton("Eliminar");
			btnEliminar.setBackground(Color.lightGray);
			btnEliminar.setBounds(64, 59, 92, 21);
			btnEditar = new JButton("Editar");
			btnEditar.setBackground(Color.lightGray);
			btnEditar.setBounds(115, 28, 92, 21);

			// Agregar acciones a los botones
			btnAgregar.addActionListener(e -> agregarEquipo());
			btnEliminar.addActionListener(e -> eliminarEquipo());
			btnEditar.addActionListener(e -> editarEquipo());
			panelBotones.setLayout(null);

			// Agregar botones al panel de botones
			panelBotones.add(btnAgregar);
			panelBotones.add(btnEliminar);
			panelBotones.add(btnEditar);

			// Panel de lista de equipos
			JPanel panelListaEquipos = new JPanel(new BorderLayout());
			panelListaEquipos.setBounds(0, 122, 452, 441);
			equiposTableModel = new DefaultTableModel(
					new Object[] { "Código", "Nombre", "Presidente", "Cant. Socios", "Cod. Estadio" }, 0);
			tablaEquipos = new JTable(equiposTableModel);
			@SuppressWarnings("unused")
			JScrollPane scrollTablaEquipos = new JScrollPane(tablaEquipos);
			tablaEquipos.getSelectionModel().addListSelectionListener(e -> {
				cargarDatosEquipoSeleccionado();

			});

			// Configuración de la tabla de equipos con edición deshabilitada
			tablaEquipos = new JTable(equiposTableModel) {
				/**
				* 
				*/
				private static final long serialVersionUID = 1L;

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			JScrollPane scrollTablaEquipos1 = new JScrollPane(tablaEquipos);
			tablaEquipos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Opcional, selecciona una sola fila a la vez

			// Configuración de listener para seleccionar y mostrar datos del equipo
			tablaEquipos.getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					cargarDatosEquipoSeleccionado();
				}
			});

			// Agregar la tabla al panel correspondiente
			panelListaEquipos.add(new JLabel("Equipos:"), BorderLayout.NORTH);
			panelListaEquipos.add(scrollTablaEquipos1, BorderLayout.CENTER);

			// Cargar lista de equipos desde la base de datos
			cargarEquipos();

			// Agregar componentes al panel principal
			panelListaEquipos.add(new JLabel("Equipos:"), BorderLayout.NORTH);
			panelListaEquipos.add(scrollTablaEquipos1, BorderLayout.CENTER);

			// Panel de asignación de jugadores
			JPanel panelAsignacionJugadores = new JPanel(new BorderLayout());
			panelAsignacionJugadores.setBounds(669, 122, 258, 441);
			listaJugadoresModel = new DefaultListModel<>();
			listaJugadores = new JList<>(listaJugadoresModel);
			JScrollPane scrollListaJugadores = new JScrollPane(listaJugadores);
			btnAsignarJugador = new JButton("Asignar Jugador");
			btnAsignarJugador.setBackground(Color.lightGray);
			btnAsignarJugador.addActionListener(e -> asignarJugadorAEquipo());

			// Cargar lista de jugadores disponibles desde la base de datos
			cargarJugadoresDisponibles();

			// Agregar componentes al panel de asignación de jugadores
			panelAsignacionJugadores.add(new JLabel("Jugadores Disponibles:"), BorderLayout.NORTH);
			panelAsignacionJugadores.add(scrollListaJugadores, BorderLayout.CENTER);
			panelAsignacionJugadores.add(btnAsignarJugador, BorderLayout.SOUTH);
			panelPrincipal.setLayout(null);

			// Agregar paneles al panel principal
			panelPrincipal.add(panelFormulario);
			panelPrincipal.add(panelBotones);

			JButton btnverjugador = new JButton("Ver Jugador");
			btnverjugador.setBounds(49, 190, 115, 21);
			btnverjugador.setBackground(Color.lightGray);
			btnverjugador.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mostrarJugadoresPopup();
				}
			});
			panelBotones.add(btnverjugador);

			JButton btnVolver = new JButton("Volver");
			btnVolver.setBounds(64, 221, 80, 21);
			btnVolver.setBackground(Color.lightGray);
			btnVolver.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					SeasonSetupPage ventanaTemporada = new SeasonSetupPage(null);
					ventanaTemporada.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					ventanaTemporada.setVisible(true);
					dispose();
				}
			});
			panelBotones.add(btnVolver);
			panelPrincipal.add(panelListaEquipos);
			panelPrincipal.add(panelAsignacionJugadores);

			// Agregar panel principal a la ventana
			getContentPane().add(panelPrincipal);

			// Mostrar la ventana
			setVisible(true);
		} catch (SQLException ex) {
			logAttempt("Error:" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarEquipos() {
		try {
			Statement statement = conexion.createStatement();
			ResultSet rs = statement.executeQuery("SELECT * FROM equipo");

			// Limpiar tabla de equipos
			equiposTableModel.setRowCount(0);

			// Llenar tabla de equipos con los datos de la base de datos
			while (rs.next()) {
				Object[] row = { rs.getString("CodEq"), rs.getString("NomEq"), rs.getString("PresEq"), rs.getInt("CantSocios"),
						rs.getString("CodEst"),
						// Convertir el arreglo de bytes de la imagen a un ImageIcon y mostrarlo en la
						// tabla
						getImageIconFromBytes(rs.getBytes("Escudo")) };
				equiposTableModel.addRow(row);
			}
		} catch (SQLException ex) {
			logAttempt("Error:" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error al cargar la lista de equipos", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private ImageIcon getImageIconFromBytes(byte[] imageData) {
		if (imageData != null) {
			// Convertir bytes en imagen y luego escalarla adecuadamente
			ImageIcon imageIcon = new ImageIcon(imageData);
			Image image = imageIcon.getImage();
			// Ajusta estas dimensiones según las necesidades específicas de tu tabla
			Image scaledImage = image.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
			return new ImageIcon(scaledImage);
		} else {
			// Si no hay imagen, puedes devolver un icono predeterminado o null
			return null;
		}
	}

	private void cargarJugadoresDisponibles() {
		try {
			Statement statement = conexion.createStatement();
			ResultSet rs = statement.executeQuery(
					"SELECT CONCAT(NomJug, ' ', ApeJug) AS NombreCompleto FROM jugador WHERE NomJug NOT IN (SELECT NomJug FROM jugador_equipo)");

			// Limpiar lista de jugadores disponibles
			listaJugadoresModel.clear();

			// Llenar lista de jugadores disponibles con los datos de la base de datos
			while (rs.next()) {
				listaJugadoresModel.addElement(rs.getString("NombreCompleto"));
			}
		} catch (SQLException ex) {
			logAttempt("Error:" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error al cargar la lista de jugadores disponibles", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void agregarEquipo() {
	    try {
	        // Obtener los datos del formulario
	        String codigo = txtCodigo.getText().trim();
	        String nombre = txtNombre.getText().trim();
	        String presidente = txtPresidente.getText().trim();
	        String cantSociosText = txtCantSocios.getText().trim();
	        String codEstadio = txtCodEstadio.getText().trim();

	        // Verificar si los campos de texto están vacíos
	        if (codigo.isEmpty() || nombre.isEmpty() || presidente.isEmpty() || cantSociosText.isEmpty() || codEstadio.isEmpty()) {
	            JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // Verificar si la cantidad de socios es un número entero válido
	        int cantSocios;
	        try {
	            cantSocios = Integer.parseInt(cantSociosText);
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(null, "La cantidad de socios debe ser un número entero", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // Verificar si los campos de texto contienen solo texto y no contienen caracteres especiales ni números
	        if (!nombre.matches("[a-zA-Z\\s]+") || !presidente.matches("[a-zA-Z\\s]+")) {
	            JOptionPane.showMessageDialog(null, "Los campos de Nombre y Presidente deben contener solo letras y espacios", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // Verificar si los códigos contienen solo caracteres alfanuméricos
	        if (!codigo.matches("[a-zA-Z0-9]+") || !codEstadio.matches("[a-zA-Z0-9]+")) {
	            JOptionPane.showMessageDialog(null, "Los campos de Código y Código del Estadio deben contener solo caracteres alfanuméricos", "Error", JOptionPane.ERROR_MESSAGE);
	            return;
	        }

	        // Insertar el nuevo equipo en la base de datos
	        PreparedStatement statement = conexion.prepareStatement(
	                "INSERT INTO equipo (CodEq, NomEq, PresEq, CantSocios, CodEst, EscudoURL) VALUES (?, ?, ?, ?, ?, ?)");
	        statement.setString(1, codigo);
	        statement.setString(2, nombre);
	        statement.setString(3, presidente);
	        statement.setInt(4, cantSocios);
	        statement.setString(5, codEstadio);
	        statement.setString(6, rutaFotoSeleccionada); // Guardar la ruta de la imagen
	        statement.executeUpdate();

	        // Actualizar la lista de equipos
	        cargarEquipos();

	        // Limpiar campos de texto
	        limpiarCampos();
	        logAttempt("Equipo agregado correctamente:" + nombre);
	    } catch (SQLException ex) {
	    	logAttempt("Error:" + ex.getMessage());
	        JOptionPane.showMessageDialog(null, "Error al agregar el equipo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


	private void eliminarEquipo() {
		try {
			// Obtener el código del equipo seleccionado
			int filaSeleccionada = tablaEquipos.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(null, "Seleccione un equipo para eliminar", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String codigoEquipo = (String) equiposTableModel.getValueAt(filaSeleccionada, 0);

			// Eliminar el equipo de la base de datos
			PreparedStatement statement = conexion.prepareStatement("DELETE FROM equipo WHERE CodEq = ?");
			statement.setString(1, codigoEquipo);
			statement.executeUpdate();

			// Actualizar la lista de equipos
			cargarEquipos();
			logAttempt("Equipo eliminado correctamente:" + tablaEquipos.getSelectedRow());
		} catch (SQLException ex) {
			logAttempt("Error:" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error al eliminar equipo", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editarEquipo() {
		try {
			// Obtener el código del equipo seleccionado
			int filaSeleccionada = tablaEquipos.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(null, "Seleccione un equipo para editar", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String codigoEquipo = (String) equiposTableModel.getValueAt(filaSeleccionada, 0);

			// Obtener los datos del formulario
			String nombre = txtNombre.getText();
			String presidente = txtPresidente.getText();
			int cantSocios = Integer.parseInt(txtCantSocios.getText());
			String codEstadio = txtCodEstadio.getText();

			// Actualizar los datos del equipo en la base de datos
			PreparedStatement statement = conexion.prepareStatement(
					"UPDATE equipo SET NomEq = ?, PresEq = ?, CantSocios = ?, CodEst = ?, EscudoURL = ? WHERE CodEq = ?");
			statement.setString(1, nombre);
			statement.setString(2, presidente);
			statement.setInt(3, cantSocios);
			statement.setString(4, codEstadio);
			statement.setString(5, rutaFotoSeleccionada); // Asegúrate de que rutaFotoSeleccionada contenga la ruta correcta
			statement.setString(6, codigoEquipo); // El código del equipo seleccionado

			int filasActualizadas = statement.executeUpdate();

			if (filasActualizadas > 0) {
				JOptionPane.showMessageDialog(null, "Equipo actualizado correctamente", "Información",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "No se pudo actualizar el equipo", "Error", JOptionPane.ERROR_MESSAGE);
			}

			// Actualizar la lista de equipos
			cargarEquipos();

			// Limpiar campos de texto
			limpiarCampos();
			logAttempt("Equipo agregado correctamente:" + nombre);
		} catch (SQLException | NumberFormatException ex) {
			logAttempt("Error:" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error al editar equipo", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void cargarDatosEquipoSeleccionado() {
		int selectedRow = tablaEquipos.getSelectedRow();
		if (selectedRow != -1) {
			String codigo = (String) equiposTableModel.getValueAt(selectedRow, 0);
			String nombre = (String) equiposTableModel.getValueAt(selectedRow, 1);
			String presidente = (String) equiposTableModel.getValueAt(selectedRow, 2);
			int cantSocios = (int) equiposTableModel.getValueAt(selectedRow, 3);
			String codEstadio = (String) equiposTableModel.getValueAt(selectedRow, 4);

			// Mostrar los datos del equipo seleccionado en los campos de texto
			txtCodigo.setText(codigo);
			txtNombre.setText(nombre);
			txtPresidente.setText(presidente);
			txtCantSocios.setText(String.valueOf(cantSocios));
			txtCodEstadio.setText(codEstadio);
		}
	}

	private void asignarJugadorAEquipo() {
		try {
			// Obtener el nombre del jugador seleccionado
			String nombreJugador = listaJugadores.getSelectedValue();
			if (nombreJugador == null) {
				JOptionPane.showMessageDialog(null, "Seleccione un jugador para asignar", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Obtener la fila seleccionada del equipo
			int filaSeleccionada = tablaEquipos.getSelectedRow();
			if (filaSeleccionada == -1) {
				JOptionPane.showMessageDialog(null, "Seleccione un equipo al que asignar el jugador", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Obtener los datos del equipo seleccionado de la base de datos
			String codigoEquipo = (String) equiposTableModel.getValueAt(filaSeleccionada, 0);
			String nombreEquipo = (String) equiposTableModel.getValueAt(filaSeleccionada, 1);

			// Verificar si el jugador existe en la base de datos
			PreparedStatement stmt = conexion
					.prepareStatement("SELECT NumFicha, ruta_foto FROM jugador WHERE CONCAT(NomJug, ' ', ApeJug) = ?");
			stmt.setString(1, nombreJugador);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) { // Si no se encontró el jugador, mostrar un mensaje de error
				JOptionPane.showMessageDialog(null, "El jugador seleccionado no existe en la base de datos.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Obtener el número de ficha del jugador y su ruta de foto
			@SuppressWarnings("unused")
			int numFichaJugador = rs.getInt("NumFicha");
			String rutaFotoJugador = rs.getString("ruta_foto");

			// Obtener el URL del escudo del equipo de la base de datos
			PreparedStatement statement2 = conexion.prepareStatement("SELECT EscudoURL FROM equipo WHERE CodEq = ?");
			statement2.setString(1, codigoEquipo);
			ResultSet rs2 = statement2.executeQuery();
			rs2.next();
			String escudoURL = rs2.getString("EscudoURL");

			// Asignar el jugador al equipo en la base de datos
			PreparedStatement statement3 = conexion.prepareStatement(
					"INSERT INTO jugador_equipo (NomEq, EscudoURL, NomJug, ApeJug, ruta_foto) VALUES (?, ?, ?, ?, ?)");
			statement3.setString(1, nombreEquipo);
			statement3.setString(2, escudoURL);
			statement3.setString(3, nombreJugador.split(" ", 2)[0]); // Separar el nombre y apellido del jugador
			statement3.setString(4, nombreJugador.split(" ", 2)[1]);
			statement3.setString(5, rutaFotoJugador);
			statement3.executeUpdate();

			// Actualizar la lista de jugadores disponibles
			cargarJugadoresDisponibles();
		} catch (SQLException ex) {
			logAttempt("Error:" + ex.getMessage());
			JOptionPane.showMessageDialog(null, "Error al asignar jugador a equipo", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void subirFoto() {
		// Obtén el directorio del proyecto
		String projectDir = System.getProperty("user.dir");

		// Define el directorio inicial del JFileChooser
		File initialDir = new File(projectDir, "Media");

		// Verifica si el directorio existe, si no, crea el directorio
		if (!initialDir.exists()) {
			initialDir.mkdirs();
		}

		// Configura el JFileChooser para abrir en el directorio inicial
		JFileChooser fileChooser = new JFileChooser(initialDir);
		fileChooser.setDialogTitle("Seleccionar Foto del Escudo");
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// Filtro para tipos de archivos de imagen
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de Imagen", "jpg", "png", "jpeg", "gif");
		fileChooser.setFileFilter(filter);

		int returnValue = fileChooser.showOpenDialog(this);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();

			// Obtén la ruta relativa del archivo seleccionado con respecto al directorio
			// del proyecto
			String relativePath = initialDir.toURI().relativize(selectedFile.toURI()).getPath();

			// Construye la ruta completa con las carpetas adicionales
			String fullPath = "Media/" + relativePath;

			// Guardar la ruta completa de la foto seleccionada
			rutaFotoSeleccionada = fullPath;
		}
	}

	private void limpiarCampos() {
		txtCodigo.setText("");
		txtNombre.setText("");
		txtPresidente.setText("");
		txtCantSocios.setText("");
		txtCodEstadio.setText("");
	}

	private void mostrarJugadoresPopup() {
		int filaSeleccionada = tablaEquipos.getSelectedRow();
		if (filaSeleccionada != -1) {
			try {
				// Obtener el nombre del equipo seleccionado
				String nombreEquipo = (String) equiposTableModel.getValueAt(filaSeleccionada, 1);

				// Preparar y ejecutar la consulta SQL para obtener los jugadores asignados al
				// equipo
				PreparedStatement statement = conexion
						.prepareStatement("SELECT NomJug, ApeJug FROM jugador_equipo WHERE NomEq = ?");
				statement.setString(1, nombreEquipo);
				ResultSet rs = statement.executeQuery();

				// Crear una lista para almacvnar los nombres de los jugadores
				DefaultListModel<String> listaModeloJugadores = new DefaultListModel<>();

				// Agregar los nombres de los jugadores a la lista
				while (rs.next()) {
					String nombreCompleto = rs.getString("NomJug") + " " + rs.getString("ApeJug");
					listaModeloJugadores.addElement(nombreCompleto);
				}

				// Crear un JList con el modelo de lista de jugadores
				JList<String> listaJugadores = new JList<>(listaModeloJugadores);

				// Mostrar un cuadro de diálogo emergente con la lista de jugadores
				JOptionPane.showMessageDialog(null, new JScrollPane(listaJugadores), "Jugadores del Equipo",
						JOptionPane.PLAIN_MESSAGE);
			} catch (SQLException ex) {
				logAttempt("Error:" + ex.getMessage());
				JOptionPane.showMessageDialog(null, "Error al obtener jugadores del equipo", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(null, "Seleccione un equipo de la tabla", "Error", JOptionPane.WARNING_MESSAGE);
		}
	}
	private void logAttempt(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            writer.println(timestamp + " - " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
