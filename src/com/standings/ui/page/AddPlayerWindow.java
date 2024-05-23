package com.standings.ui.page;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPlayerWindow extends ParentFrame {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;
	private JTextField txtNumFicha, txtNomJug, txtApeJug, txtEdad, txtPeso, txtAltura, txtDNIJug;
	private JButton btnAgregar, btnEditar, btnBorrar, btnSubirFoto;
	private JTable tblJugadores;
	private DefaultTableModel model;
	private JLabel lblFoto;

	private String imagePath;
	private Connection conn;
	private PreparedStatement pstmt;
	private static final String LOG_FILE = "log_control.txt";
	
	public AddPlayerWindow() {
		setTitle("Gestión de Jugadores");
		setSize(736, 504);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 786, 106);

		JPanel panelFormulario = new JPanel();
		panelFormulario.setBounds(0, 10, 786, 70);
		panelFormulario.setLayout(null);

		JLabel label = new JLabel("NumFicha:");
		label.setBounds(0, 0, 67, 21);
		panelFormulario.add(label);
		txtNumFicha = new JTextField();
		txtNumFicha.setBounds(77, 0, 50, 21);
		panelFormulario.add(txtNumFicha);

		JLabel label_1 = new JLabel("Nombre:");
		label_1.setBounds(162, 0, 86, 21);
		panelFormulario.add(label_1);
		txtNomJug = new JTextField();
		txtNomJug.setBounds(212, 0, 98, 21);
		panelFormulario.add(txtNomJug);

		JLabel label_2 = new JLabel("Apellido:");
		label_2.setBounds(346, 0, 57, 21);
		panelFormulario.add(label_2);
		txtApeJug = new JTextField();
		txtApeJug.setBounds(413, 0, 98, 21);
		panelFormulario.add(txtApeJug);

		JLabel label_3 = new JLabel("Edad:");
		label_3.setBounds(0, 32, 50, 21);
		panelFormulario.add(label_3);
		txtEdad = new JTextField();
		txtEdad.setBounds(77, 31, 50, 21);
		panelFormulario.add(txtEdad);

		JLabel label_4 = new JLabel("Peso:");
		label_4.setBounds(346, 31, 50, 21);
		panelFormulario.add(label_4);

		JLabel label_5 = new JLabel("Altura:");
		label_5.setBounds(162, 31, 43, 21);
		panelFormulario.add(label_5);
		txtAltura = new JTextField();
		txtAltura.setBounds(212, 32, 43, 21);
		panelFormulario.add(txtAltura);

		JLabel label_6 = new JLabel("DNI:");
		label_6.setBounds(524, 0, 43, 21);
		panelFormulario.add(label_6);
		txtPeso = new JTextField();
		txtPeso.setBounds(413, 31, 98, 21);
		panelFormulario.add(txtPeso);
		txtDNIJug = new JTextField();
		txtDNIJug.setBounds(577, 0, 128, 21);
		panelFormulario.add(txtDNIJug);

		JLabel label_7 = new JLabel("Foto:");
		label_7.setBounds(524, 32, 43, 21);
		panelFormulario.add(label_7);
		lblFoto = new JLabel();
		lblFoto.setBounds(0, 105, 262, 21);
		panelFormulario.add(lblFoto);

		btnSubirFoto = new JButton("Subir Foto");
		btnSubirFoto.setBounds(577, 32, 128, 21);
		btnSubirFoto.setBackground(Color.lightGray);
		btnSubirFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subirFoto();
			}
		});

		panel.setLayout(null);
		panelFormulario.add(btnSubirFoto);

		panel.add(panelFormulario);

		JPanel panelBotones = new JPanel();
		panelBotones.setBounds(0, 78, 786, 28);

		btnAgregar = new JButton("Agregar");
		btnAgregar.setBounds(159, 5, 93, 21);
		btnAgregar.setBackground(Color.lightGray);
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				agregarJugador();
			}
		});
		panelBotones.setLayout(null);
		panelBotones.add(btnAgregar);

		btnEditar = new JButton("Editar");
		btnEditar.setBounds(276, 5, 93, 21);
		btnEditar.setBackground(Color.lightGray);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarCambios();
				cargarJugadores();
			}
		});
		panelBotones.add(btnEditar);

		btnBorrar = new JButton("Borrar");
		btnBorrar.setBounds(390, 5, 93, 21);
		btnBorrar.setBackground(Color.lightGray);
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borrarJugador();
			}
		});
		getContentPane().setLayout(null);
		panelBotones.add(btnBorrar);

		panel.add(panelBotones);

		JButton btnVolver = new JButton("Volver");
		btnVolver.setBounds(501, 5, 85, 21);
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

		getContentPane().add(panel);

		model = new DefaultTableModel();
		model.addColumn("Nombre");
		model.addColumn("Apellido");
		model.addColumn("DNI");

		tblJugadores = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(tblJugadores);
		scrollPane.setBounds(0, 109, 722, 358);
		getContentPane().add(scrollPane);

		tblJugadores.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					mostrarPerfilJugador();
				}
			}
		});

		cargarJugadores();

		setVisible(true);
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
			imagePath = fullPath;
			lblFoto.setIcon(
					new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
		}
	}

	private void mostrarPerfilJugador() {
		int selectedRow = tblJugadores.getSelectedRow();
		if (selectedRow != -1) {
			String nombre = (String) tblJugadores.getValueAt(selectedRow, 0);
			String apellido = (String) tblJugadores.getValueAt(selectedRow, 1);
			String dni = (String) tblJugadores.getValueAt(selectedRow, 2);

			// Crea un cuadro de diálogo para mostrar el perfil del jugador
			JDialog dialog = new JDialog(this, "Perfil de Jugador", true);
			dialog.getContentPane().setLayout(new BorderLayout());

			JPanel panelDatos = new JPanel(new GridLayout(4, 2));
			panelDatos.add(new JLabel("Nombre:"));
			panelDatos.add(new JLabel(nombre));
			panelDatos.add(new JLabel("Apellido:"));
			panelDatos.add(new JLabel(apellido));
			panelDatos.add(new JLabel("DNI:"));
			panelDatos.add(new JLabel(dni));
			// Agrega más etiquetas para mostrar otros datos del jugador

			try {
				pstmt = conn.prepareStatement(
						"SELECT ruta_foto, NumFicha, Edad, Peso, Altura FROM jugador WHERE NomJug = ? AND ApeJug = ? AND DNIJug = ?");
				pstmt.setString(1, nombre);
				pstmt.setString(2, apellido);
				pstmt.setString(3, dni);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					panelDatos.add(new JLabel("Número de Ficha: "));
					panelDatos.add(new JLabel(String.valueOf(rs.getInt("NumFicha"))));
					panelDatos.add(new JLabel("Edad:"));
					panelDatos.add(new JLabel(String.valueOf(rs.getInt("Edad"))));
					panelDatos.add(new JLabel("Peso:"));
					panelDatos.add(new JLabel(String.valueOf(rs.getFloat("Peso"))));
					panelDatos.add(new JLabel("Altura:"));
					panelDatos.add(new JLabel(String.valueOf(rs.getFloat("Altura"))));
					int numFicha = rs.getInt("NumFicha");
					int edad = rs.getInt("Edad");
					float peso = rs.getFloat("Peso");
					float altura = rs.getFloat("Altura");
					txtNumFicha.setText(String.valueOf(numFicha));
					txtNomJug.setText(nombre);
					txtApeJug.setText(apellido);
					txtEdad.setText(String.valueOf(edad));
					txtPeso.setText(String.valueOf(peso));
					txtAltura.setText(String.valueOf(altura));
					txtDNIJug.setText(dni);

				}
			} catch (SQLException ex) {
				logAttempt("Error al mostrar el perfil de los jugadores:" + ex.getMessage());
			}

			dialog.getContentPane().add(panelDatos, BorderLayout.NORTH);

			// Agrega la foto del jugador si está disponible
			try {
				pstmt = conn.prepareStatement(
						"SELECT ruta_foto FROM jugador WHERE NomJug = ? AND ApeJug = ? AND DNIJug = ?");
				pstmt.setString(1, nombre);
				pstmt.setString(2, apellido);
				pstmt.setString(3, dni);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					String imagePath = rs.getString("ruta_foto");
					if (imagePath != null) {
						File imageFile = new File(imagePath);
						if (imageFile.exists()) {
							ImageIcon imageIcon = new ImageIcon(new ImageIcon(imageFile.getAbsolutePath()).getImage()
									.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
							JLabel lblFoto = new JLabel(imageIcon);
							dialog.getContentPane().add(lblFoto, BorderLayout.CENTER);
						} else {
							//System.out.println("La imagen no existe en la ruta especificada: " + imagePath);
						}
					} else {
						//System.out.println("La ruta de la imagen en la base de datos es nula.");
					}
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			// Ajusta el tamaño del diálogo y muéstralo
			dialog.pack();
			dialog.setLocationRelativeTo(this);
			dialog.setVisible(true);
		}
	}

	private void cargarJugadores() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");
			pstmt = conn.prepareStatement("SELECT NomJug, ApeJug, DNIJug  FROM jugador");
			model.setRowCount(0);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				model.addRow(new Object[] { rs.getString("NomJug"), rs.getString("ApeJug"), rs.getString("DNIJug"),

				});
			}
		} catch (SQLException ex) {
			logAttempt("Error al cargar los jugadores:" + ex.getMessage());
			
		}
	}

	private void agregarJugador() {
		try {
			// Validación del campo NumFicha
			String numFichaText = txtNumFicha.getText();
			if (!numFichaText.matches("\\d+")) { // Acepta solo números enteros positivos
				throw new IllegalArgumentException("El número de ficha solo debe contener números enteros positivos.");
			}

			// Validación del campo Nombre y Apellido
			String nombre = txtNomJug.getText();
			String apellido = txtApeJug.getText();
			if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+") || !apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
				throw new IllegalArgumentException("El nombre y apellido solo deben contener letras y espacios.");
			}

			// Validación del campo Edad
			String edadText = txtEdad.getText();
			if (!edadText.matches("\\d+")) { // Acepta solo números enteros positivos
				throw new IllegalArgumentException("La edad solo debe contener números enteros positivos.");
			}

			// Validación del campo Peso y Altura
			String pesoText = txtPeso.getText();
			String alturaText = txtAltura.getText();
			if (!pesoText.matches("\\d+(\\.\\d+)?") || !alturaText.matches("\\d+(\\.\\d+)?")) {
				throw new IllegalArgumentException(
						"El peso y la altura deben ser números positivos, pueden incluir decimales.");
			}

			// Validación del campo DNI
			String dni = txtDNIJug.getText();
			if (!dni.matches("\\d{1,8}[a-zA-Z]?")) { // Acepta hasta 8 dígitos seguidos opcionalmente de una letra
				throw new IllegalArgumentException(
						"El DNI debe contener hasta 8 dígitos seguidos opcionalmente de una letra.");
			}

			// Si todas las validaciones pasan, procede a insertar el jugador en la base de
			// datos
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");
			pstmt = conn.prepareStatement(
					"INSERT INTO jugador (NumFicha, NomJug, ApeJug, Edad, Peso, Altura, DNIJug, ruta_foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, Integer.parseInt(numFichaText));
			pstmt.setString(2, nombre);
			pstmt.setString(3, apellido);
			pstmt.setInt(4, Integer.parseInt(edadText));
			pstmt.setFloat(5, Float.parseFloat(pesoText));
			pstmt.setFloat(6, Float.parseFloat(alturaText));
			pstmt.setString(7, dni);
			pstmt.setString(8, imagePath); // Suponiendo que lblFoto ahora contiene la ruta
			pstmt.executeUpdate();
			model.addRow(new Object[] { Integer.parseInt(numFichaText), nombre, apellido, Integer.parseInt(edadText),
					Float.parseFloat(pesoText), Float.parseFloat(alturaText), dni });
			JOptionPane.showMessageDialog(null, "Jugador agregado exitosamente.");
			logAttempt("Jugador agregado:" + nombre + apellido);
			cargarJugadores();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(null,
					"Por favor, ingrese valores numéricos válidos para la edad, peso y altura.");
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(null, "Error al agregar el jugador: " + ex.getMessage());

		}
	}

	private void guardarCambios() {
	    int selectedRow = tblJugadores.getSelectedRow();
	    if (selectedRow != -1) {
	        try {
	            String nombre = txtNomJug.getText();
	            String apellido = txtApeJug.getText();
	            String dni = txtDNIJug.getText();
	            int numFicha = Integer.parseInt(txtNumFicha.getText());
	            int edad = Integer.parseInt(txtEdad.getText());
	            float peso = Float.parseFloat(txtPeso.getText());
	            float altura = Float.parseFloat(txtAltura.getText());

	            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");

	            // Verificar si el jugador con el numFicha proporcionado existe en la base de datos
	            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM jugador WHERE numFicha = ?");
	            stmt.setInt(1, numFicha);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) { // Si el registro existe, actualizarlo
	                String rutaFoto = imagePath;

	                pstmt = conn.prepareStatement(
	                        "UPDATE jugador SET NomJug=?, ApeJug=?, DNIJug=?, Edad=?, Peso=?, Altura=?, ruta_foto=? WHERE numFicha=?");
	                pstmt.setString(1, nombre);
	                pstmt.setString(2, apellido);
	                pstmt.setString(3, dni);
	                pstmt.setInt(4, edad);
	                pstmt.setFloat(5, peso);
	                pstmt.setFloat(6, altura);
	                pstmt.setString(7, rutaFoto);
	                pstmt.setInt(8, numFicha);
	                pstmt.executeUpdate();

	                JOptionPane.showMessageDialog(null, "Los cambios se han guardado exitosamente.");
	                cargarJugadores();
	            } else { // Si el registro no existe, mostrar error
	                JOptionPane.showMessageDialog(null, "El jugador no existe en la base de datos.");
	            }
	        } catch (SQLException ex) {
	            logAttempt("Error:" + ex.getMessage());
	        } catch (NumberFormatException ex) {
	            JOptionPane.showMessageDialog(null, "Por favor, ingrese un valor válido para la edad, peso y altura.");
	        }
	    } else {
	        JOptionPane.showMessageDialog(null, "Por favor, seleccione un jugador para editar.");
	    }
	}


	private void borrarJugador() {
		int selectedRow = tblJugadores.getSelectedRow();
		if (selectedRow != -1) {
			String nombre = (String) tblJugadores.getValueAt(selectedRow, 0);
			String apellido = (String) tblJugadores.getValueAt(selectedRow, 1);
			String dni = (String) tblJugadores.getValueAt(selectedRow, 2);

			int confirmacion = JOptionPane.showConfirmDialog(null,
					"¿Está seguro de que desea borrar al jugador seleccionado?", "Confirmación de borrado",
					JOptionPane.YES_NO_OPTION);
			if (confirmacion == JOptionPane.YES_OPTION) {
				try {
					conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/reto3", "root", "");
					pstmt = conn.prepareStatement("DELETE FROM jugador WHERE NomJug = ? AND ApeJug = ? AND DNIJug = ?");
					pstmt.setString(1, nombre);
					pstmt.setString(2, apellido);
					pstmt.setString(3, dni);
					pstmt.executeUpdate();

					// Remover la fila de la tabla
					model.removeRow(selectedRow);

					JOptionPane.showMessageDialog(null, "El jugador ha sido borrado exitosamente.");
					cargarJugadores();
				} catch (SQLException ex) {
					logAttempt("Error:" + ex.getMessage());
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Por favor, seleccione un jugador para borrar.");
		}
	}

	class ImageRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JLabel label = new JLabel();

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			label.setIcon((ImageIcon) value);
			return label;
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
