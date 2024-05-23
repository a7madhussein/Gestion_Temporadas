package com.standings.ui.page;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignUpPage extends ParentFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = -4747175902106077767L;

	private ImageIcon nflIcon;

	private HashMap<String, String> loginInfo;

	private JLabel LaligaIconLabel;
	private JLabel copyRights;
	private JLabel loginLabel;
	private JLabel emailLabel;
	private JLabel passwordLabel;
	private JLabel creatAccountLabel;

	private JPanel mainPanel;
	private JPanel leftIneerPanel;
	private JPanel rightInnerPanel;

	private JTextField emailField;
	private JPasswordField passwordField;
	private JButton signUpButton;
	private JLabel fullNameLabel;
	private JTextField fullNameField;
	private JButton loginButton;

	private JLabel errorMessageForEmail;
	private JLabel errorMessageForPassword;
	private JLabel errorMessageForFullName;
	private EntityManagerFactory emf;
    private EntityManager em;
    
	public SignUpPage(HashMap<String, String> loginInfo) {
		setTitle("Registrar");
		this.loginInfo = loginInfo;
		initializFrameGraphics(loginInfo);
		initializePanelsGraphics();
		
		  emf = Persistence.createEntityManagerFactory("bd/Users.odb");
	      em = emf.createEntityManager();
		

	}

	private void initializFrameGraphics(HashMap<String, String> loginInfo) {
		this.setLocationRelativeTo(null);
		this.loginInfo = loginInfo;
		this.setResizable(false);
		this.setSize(650, 479);
		setSizeAndCenter();
	}

	private void setSizeAndCenter() {
		Toolkit tool = getToolkit();
		Dimension screenSize = tool.getScreenSize();
		this.setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
	}

	private void initializePanelsGraphics() {
		initializeMainPanel();
		initializeRightInnerPanel();
		initializeLeftInnerPanel();
	}

	private void initializeMainPanel() {
		mainPanel = new JPanel();
		mainPanel.setBackground(new Color(238, 238, 236));
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(null);
	}

	private void initializeRightInnerPanel() {
		rightInnerPanel = new JPanel();
		rightInnerPanel.setBounds(323, 0, 317, 442);
		mainPanel.add(rightInnerPanel);
		rightInnerPanel.setLayout(null);
		initializeLoginLabel();
		initializeEmail();
		initializePassword();
		initializeFullName();
		initializeCreatAcountLabel();
		initializeButtons();
		initializeErrorMessages();
	}

	private void initializeLoginLabel() {
		loginLabel = new JLabel("Registrarse");
		loginLabel.setBounds(60, 23, 188, 42);
		loginLabel.setVerticalAlignment(SwingConstants.TOP);
		loginLabel.setFont(new Font(null, Font.PLAIN, 35));
		rightInnerPanel.add(loginLabel);
	}

	private void initializeEmail() {
		emailLabel = new JLabel("Email");
		emailLabel.setBounds(34, 167, 50, 25);
		rightInnerPanel.add(emailLabel);

		emailField = new JTextField();
		emailField.setBounds(34, 197, 200, 25);
		rightInnerPanel.add(emailField);
		emailField.addKeyListener(this);

		fieldFocusListener(emailField, null, null);
	}

	private void initializePassword() {
		passwordLabel = new JLabel("Contraseña");
		passwordLabel.setBounds(34, 232, 70, 15);
		rightInnerPanel.add(passwordLabel);
		passwordField = new JPasswordField();
		passwordField.setBounds(34, 257, 200, 25);
		rightInnerPanel.add(passwordField);
		passwordField.addKeyListener(this);
		fieldFocusListener(null, passwordField, null);
	}

	private void initializeFullName() {
		fullNameLabel = new JLabel("Nombre");
		fullNameLabel.setBounds(34, 109, 70, 13);
		rightInnerPanel.add(fullNameLabel);

		fullNameField = new JTextField();
		fullNameField.setBounds(34, 132, 200, 25);
		rightInnerPanel.add(fullNameField);
		fullNameField.addKeyListener(this);
		fieldFocusListener(null, null, fullNameField);
	}

	private void fieldFocusListener(JTextField emailField, JPasswordField passwordField, JTextField fullNameField) {
		if (emailField != null) {
			emailField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					errorMessageForEmail.setText("");
				}

				@Override
				public void focusLost(FocusEvent e) {

				}
			});
		} else if (passwordField != null) {
			passwordField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					errorMessageForPassword.setText("");
				}

				@Override
				public void focusLost(FocusEvent e) {

				}
			});

		} else if (fullNameField != null) {

			fullNameField.addFocusListener(new FocusListener() {
				@Override
				public void focusGained(FocusEvent e) {
					errorMessageForFullName.setText("");
				}

				@Override
				public void focusLost(FocusEvent e) {

				}
			});
		}
	}

	private void initializeCreatAcountLabel() {
		creatAccountLabel = new JLabel("¿Ya tienes una cuenta?");
		creatAccountLabel.setBounds(21, 379, 159, 15);
		rightInnerPanel.add(creatAccountLabel);
	}

	private void initializeButtons() {
		initializeLoginButton();
		initializeSignUpButton();
	}

	private void initializeLoginButton() {
		loginButton = new JButton("Inicia sesión");
		loginButton.setBounds(183, 374, 111, 25);
		loginButton.setFocusable(false);
		loginButton.setBackground(Color.LIGHT_GRAY);
		rightInnerPanel.add(loginButton);
		loginButton.addActionListener(this);
	}

	private void initializeSignUpButton() {
		signUpButton = new JButton("Regístrate");
		signUpButton.setBounds(34, 304, 100, 25);
		signUpButton.addActionListener(this);
		signUpButton.setFocusable(false);
		signUpButton.setBackground(Color.lightGray);
		rightInnerPanel.add(signUpButton);
	}

	private void initializeErrorMessages() {
		initializeEmailErrorMessage();
		initializePasswordErrorMessage();
		initializeFullNameErrorMessage();
	}

	private void initializeEmailErrorMessage() {
		errorMessageForEmail = new JLabel();
		errorMessageForEmail.setForeground(Color.RED);
		rightInnerPanel.add(errorMessageForEmail);
		errorMessageForEmail.setBounds(137, 167, 111, 25);
	}

	private void initializePasswordErrorMessage() {
		errorMessageForPassword = new JLabel();
		errorMessageForPassword.setForeground(Color.RED);
		rightInnerPanel.add(errorMessageForPassword);
		errorMessageForPassword.setBounds(137, 232, 111, 25);
	}

	private void initializeFullNameErrorMessage() {
		errorMessageForFullName = new JLabel();
		errorMessageForFullName.setForeground(Color.RED);
		rightInnerPanel.add(errorMessageForFullName);
		errorMessageForFullName.setBounds(137, 103, 111, 25);
	}

	private void initializeLeftInnerPanel() {
		leftIneerPanel = new JPanel();
		leftIneerPanel.setBackground(new Color(238, 238, 236));
		leftIneerPanel.setBounds(0, 0, 322, 442);
		mainPanel.add(leftIneerPanel);
		leftIneerPanel.setLayout(null);
		leftIneerPanel.setBackground(new Color(255, 255, 255));
		initializeNflIcon();
		initializeCopyRights();
	}

	private void initializeNflIcon() {
		nflIcon = new ImageIcon(ResizeIcon("/images/laliga.png", 280, 200));
		LaligaIconLabel = new JLabel("");
		LaligaIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
		LaligaIconLabel.setIcon(nflIcon);
		LaligaIconLabel.setBounds(10, 56, 290, 223);
		leftIneerPanel.add(LaligaIconLabel);
	}

	private void initializeCopyRights() {
		copyRights = new JLabel("<html> Copyright © 2024 LaLiga.<br> all rights reserved </html>");
		leftIneerPanel.add(copyRights);
		copyRights.setBounds(100, 289, 120, 40);
		copyRights.setFont(new Font(null, Font.PLAIN, 10));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		userClickLoginLogic(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char keyChar = e.getKeyChar();
		if (keyChar != KeyEvent.VK_ENTER) {
			errorMessageForPassword.setText("");
			errorMessageForEmail.setText("");
			errorMessageForFullName.setText("");

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		userKeyboardLoginLogic(e);

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	private void userKeyboardLoginLogic(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			validateLogin(fullNameField.getText(), emailField.getText(), String.valueOf(passwordField.getPassword()));

		}
	}

	private void userClickLoginLogic(ActionEvent e) {

		if (e.getSource() == signUpButton) {

			validateLogin(fullNameField.getText(), emailField.getText(), String.valueOf(passwordField.getPassword()));

		} else if (e.getSource() == loginButton) {
			this.dispose();
			new LoginPage(this.loginInfo);
		}
	}

	// EFFECTS: if the handleEmptyFields returns true; call
	// handleCredentialvalidation passing to it all the fields.

	private void validateLogin(String userFullName, String userEmail, String userPassword) {

		if (handleEmptyFields(userFullName, userEmail, userPassword)) {
			handleCredentialValidation(userFullName, userEmail, userPassword);
		}
	}

	// EFFECTS: checks if any field is empty, if any call, the promptErrorMessage
	// with appropriate arguments.

	private boolean handleEmptyFields(String userFullName, String userEmail, String userPassword) {

		if (SignUpValidationUtil.checkForEmptyField(userFullName, userEmail, userPassword)) {
			promptErrorMessage(1, 1, 1, "Campo obligatorio");
			return false;

		} else if (SignUpValidationUtil.checkForEmptyField(userEmail, userFullName)) {
			promptErrorMessage(1, 1, 0, "Campo obligatorio");
			return false;

		} else if (SignUpValidationUtil.checkForEmptyField(userEmail, userPassword)) {
			promptErrorMessage(0, 1, 1, "Campo obligatorio");
			return false;

		} else if (SignUpValidationUtil.checkForEmptyField(userFullName, userPassword)) {
			promptErrorMessage(1, 0, 1, "Campo obligatorio");
			return false;

		} else if (SignUpValidationUtil.checkForEmptyField(userFullName)) {
			promptErrorMessage(1, 0, 0, "Campo obligatorio");
			return false;

		} else if (SignUpValidationUtil.checkForEmptyField(userEmail)) {
			promptErrorMessage(0, 1, 0, "Campo obligatorio");
			return false;

		} else if (SignUpValidationUtil.checkForEmptyField(userPassword)) {
			promptErrorMessage(0, 0, 1, "Campo obligatorio");
			return false;
		}
		return true;
	}

	// MODIFIES: this
	// EFFECTS: if the credentials pass all the validations, open the main frame;
	// otherwise prompt
	// the user with appropriate message.
	public void handleCredentialValidation(String userFullName, String userEmail, String userPassword) {
        if (SignUpValidationUtil.isValidFullName(userFullName)) {
            if (SignUpValidationUtil.isValidEmail(userEmail)) {
                if (!emailExists(userEmail)) {
                    if (SignUpValidationUtil.isValidPassword(userPassword)) {
                        try {
                            em.getTransaction().begin();
                            User user = new User(userFullName, userEmail, hashPassword(userPassword));
                            em.persist(user);
                            em.getTransaction().commit();
                            writeLog("Se ha dado de alta un nuevo usuario: " + userEmail);
                            dispose();
                            new LoginPage(this.loginInfo);
                        } catch (Exception e) {
                            writeLog("Error de conexión a la base de datos al registrar usuario con correo electrónico: " + userEmail);
                            userDialog("Error al conectar con la base de datos", "Error de conexión");
                        }
                    } else {
                        userDialog("La contraseña debe tener un mínimo de 8 caracteres y debe incluir al menos una mayúscula y un número", "Requisito de contraseña");
                    }
                } else {
                    promptErrorMessage(0, 1, 0, "El email ya existe");
                }
            } else {
                promptErrorMessage(0, 1, 0, "Formato inválido");
            }
        } else {
            errorMessageForFullName.setBounds(114, 103, 144, 25);
            promptErrorMessage(1, 0, 0, "Solo se permite texto");
        }
    }

    private boolean emailExists(String email) {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        return !users.isEmpty();
    }

	
	// Método para escribir en el archivo de registro
	private void writeLog(String message) {
	    try (PrintWriter writer = new PrintWriter(new FileWriter("log.txt", true))) {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String timestamp = dateFormat.format(new Date());
	        writer.println(timestamp + " - " + message);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private String hashPassword(String password) {
        try {
            // Obtener instancia de MessageDigest con el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Convertir la contraseña a bytes y calcular el hash
            byte[] hashBytes = digest.digest(password.getBytes());
            // Convertir el hash a una representación hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Manejar la excepción si el algoritmo no está disponible
            e.printStackTrace();
            return null;
        }
    }
	
	// MODIFIES: this
	// EFFECTS: prompt a message based on the active bits; 1 equals true and 0
	// equals false.

	private void promptErrorMessage(int fullNameBit, int emailBit, int passwordBit, String errorMessage) {
		for (int i = 0; i < 3; i++) {
			if (fullNameBit == 1) {
				errorMessageForFullName.setText(errorMessage);
			}
			if (emailBit == 1) {
				errorMessageForEmail.setText(errorMessage);
			}
			if (passwordBit == 1) {
				errorMessageForPassword.setText(errorMessage);
			}

		}
	}

	// EFFECTS: make a dialog with the given message and title by the caller method.

	private void userDialog(String warrningText, String warrningTitle) {

		JOptionPane passwordRequirementPane = new JOptionPane(warrningText, JOptionPane.YES_OPTION);

		passwordRequirementPane.setMessageType(JOptionPane.WARNING_MESSAGE);

		JPanel buttonPanel = (JPanel) passwordRequirementPane.getComponent(1);

		JButton accepetButton = (JButton) buttonPanel.getComponent(0);
		accepetButton.setText("Accept");
		accepetButton.setFocusable(false);
		accepetButton.setBackground(Color.LIGHT_GRAY);

		JDialog passwordRequirementdialog = passwordRequirementPane.createDialog(this, warrningTitle);
		passwordRequirementdialog.setVisible(true);
	}
}