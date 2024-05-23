package com.standings.ui.page;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class LoginPage extends ParentFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 6002789331622401022L;

    private final int ALL_FIELDS_ARE_EMPTY = 1;
    private final int EMAIL_FIELD_IS_EMPTY = 2;
    private final int PASSWORD_FIELD_IS_EMPTY = 3;
    private final int EMAIL_NOT_FOUND = 4;
    private final int INCORRECT_PASSWORD = 5;
    private static final int LOGIN_SUCCESSFUL = 6;
    private static final int INCORRECT_PASSWORD_OFFLINE = 7;
    private static final int LOGIN_SUCCESSFUL_OFFLINE = 8;


    


    private ImageIcon nflIcon;
    private HashMap<String, String> loginInfo;

    private JLabel nflIconLabel;
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

    private JButton loginButton;
    private JButton signUpButton;

    private JLabel errorMessageForEmail;
    private JLabel errorMessageForPassword;
    
    private int validationNumber;
   // private int selectedYear;
    private EntityManagerFactory emf;
    private EntityManager em;
    private static final String LOG_FILE = "log.txt";

   
    public LoginPage(HashMap<String, String> loginInfo) {
    	//this.selectedYear = selectedYear;
        initializeFrameGraphics();
        initializePanelGraphics();
        emf = Persistence.createEntityManagerFactory("bd/Users.odb");
        em = emf.createEntityManager();

      
    }


    private void initializeFrameGraphics() {
        this.setTitle("Iniciar sesión");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(650, 400);
        setSizeAndCenter();
    }

    private void setSizeAndCenter() {
        Toolkit tool = getToolkit();
        Dimension screenSize = tool.getScreenSize();
        this.setLocation(screenSize.width / 2 - getWidth() / 2, screenSize.height / 2 - getHeight() / 2);
    }

    private void initializePanelGraphics() {
        initialMainPanel();
        initialLeftPanel();
        initialRightPanel();
    }

    private void initialMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBackground(new Color(238, 238, 236));
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(null);
    }

    private void initialLeftPanel() {
        leftIneerPanel = new JPanel();
        leftIneerPanel.setBounds(0, 0, 322, 376);
        leftIneerPanel.setLayout(null);
        leftIneerPanel.setBackground(new Color(255, 255, 255));
        mainPanel.add(leftIneerPanel);
        initialNflIcon();
        initialCopyRights();
    }

    private void initialNflIcon() {
        nflIcon = new ImageIcon(ResizeIcon("/images/laliga.png", 280, 200));
        nflIconLabel = new JLabel("");
        nflIconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nflIconLabel.setIcon(nflIcon);
        nflIconLabel.setBounds(20, 37, 292, 224);
        leftIneerPanel.add(nflIconLabel);
    }

    private void initialCopyRights() {
        copyRights = new JLabel("<html> Copyright © 2024 LaLiga.<br> All rights reserved </html>");
        copyRights.setBounds(109, 265, 120, 40);
        copyRights.setFont(new Font(null, Font.PLAIN, 10));
        leftIneerPanel.add(copyRights);
    }

    private void initialRightPanel() {
        rightInnerPanel = new JPanel();
        rightInnerPanel.setBounds(323, 0, 317, 376);
        rightInnerPanel.setLayout(null);
        mainPanel.add(rightInnerPanel);
        initializeLabels();
        initializeEmail();
        initializePassword();
        initializeButtons();
    }

    private void initializeLabels() {
        loginLabel = new JLabel("Iniciar sesión");
        loginLabel.setVerticalAlignment(SwingConstants.TOP);
        loginLabel.setBounds(45, 22, 184, 42);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 30));
        rightInnerPanel.add(loginLabel);

        creatAccountLabel = new JLabel("¿Crear una cuenta?");
        creatAccountLabel.setBounds(24, 310, 159, 15);
        rightInnerPanel.add(creatAccountLabel);
    }

    private void initializeEmail() {
        emailLabel = new JLabel("Email");
        emailLabel.setBounds(29, 87, 50, 25);
        rightInnerPanel.add(emailLabel);
        emailField = new JTextField();
        emailField.setBounds(29, 117, 200, 25);
        emailField.addKeyListener(this);
        rightInnerPanel.add(emailField);
        fieldFocusListener(emailField, null);
    }

    private void initializePassword() {
        passwordLabel = new JLabel("Contraseña");
        passwordLabel.setBounds(29, 150, 70, 15);
        rightInnerPanel.add(passwordLabel);
        passwordField = new JPasswordField();
        passwordField.setBounds(29, 177, 200, 25);
        passwordField.addKeyListener(this);
        rightInnerPanel.add(passwordField);
        fieldFocusListener(null, passwordField);
    }

    public void fieldFocusListener(JTextField emailField, JPasswordField passwordField) {
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
        }
    }

    private void initializeButtons() {
        loginButton = new JButton("Iniciar sesión");
        loginButton.setBounds(72, 212, 111, 25);
        loginButton.setFocusable(false);
        loginButton.addActionListener(this);
        loginButton.setBackground(Color.lightGray);
        rightInnerPanel.add(loginButton);

        signUpButton = new JButton("Regístrate");
        signUpButton.setBounds(201, 305, 100, 25);
        signUpButton.setFocusable(false);
        signUpButton.addActionListener(this);
        signUpButton.setBackground(Color.lightGray);
        rightInnerPanel.add(signUpButton);

        initializeErrorMessages();
    }

    private void initializeErrorMessages() {
        errorMessageForEmail = new JLabel();
        errorMessageForEmail.setText("");
        errorMessageForEmail.setBounds(132, 87, 130, 25);
        errorMessageForEmail.setForeground(Color.RED);
        rightInnerPanel.add(errorMessageForEmail);

        errorMessageForPassword = new JLabel();
        errorMessageForPassword.setForeground(Color.RED);
        errorMessageForPassword.setBounds(132, 151, 111, 25);
        rightInnerPanel.add(errorMessageForPassword);
    }

   

    @Override
    public void actionPerformed(ActionEvent e) {
        userClickLoginLogic(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        userKeyboardLoginLogic(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (keyChar != KeyEvent.VK_ENTER) {
            errorMessageForPassword.setText("");
            errorMessageForEmail.setText("");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void userKeyboardLoginLogic(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // Llama a validateLogin para validar el inicio de sesión
            validationNumber = validateLogin(emailField.getText(), String.valueOf(passwordField.getPassword()));
            handleValidationNumber(validationNumber);
        }
    }

    private void userClickLoginLogic(ActionEvent e) {
        if (e.getSource() == loginButton) {
            // Llama a validateLogin para validar el inicio de sesión
            validationNumber = validateLogin(emailField.getText(), String.valueOf(passwordField.getPassword()));
            handleValidationNumber(validationNumber);
        } else if (e.getSource() == signUpButton) {
            this.dispose();
            new SignUpPage(this.loginInfo);
        }
    }
    private boolean tryConnectToDatabase() {
        try {
            // Suponiendo que estás usando JDBC
            String url = "jdbc:mysql://localhost:3306/reto3";
            Connection conn = DriverManager.getConnection(url, "root", ""); // Usuario root sin contraseña
            return conn != null;
        } catch (SQLException e) {
            return false; // Retorna false si la conexión falla
        }
    }
    private void showConnectionErrorDialog() {
        Object[] options = {"Cerrar", "Reintentar"};
        int choice = JOptionPane.showOptionDialog(null, "No hay conexión con la base de datos.", "Error de Conexión",
            JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]);

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0); // Cierra la aplicación
        } else {
            if (tryConnectToDatabase()) {
                // Si la reconexión es exitosa, puedes hacer algo aquí, como refrescar la ventana o reintentar la operación
                JOptionPane.showMessageDialog(null, "Conexión restablecida exitosamente.");
                SeasonSetupPage seasonSetupPage = new SeasonSetupPage(this);
                seasonSetupPage.setVisible(true);
                dispose();
            } else {
                showConnectionErrorDialog(); // Si falla nuevamente, muestra el diálogo de nuevo
            }
        }
    }
    private void handleValidationNumber(int validationNumber) {
        switch (validationNumber) {
            case ALL_FIELDS_ARE_EMPTY:
                errorMessageForEmail.setText("Campo obligatorio");
                errorMessageForPassword.setText("Campo obligatorio");
                break;
            case EMAIL_FIELD_IS_EMPTY:
                errorMessageForEmail.setText("Campo obligatorio");
                break;
            case PASSWORD_FIELD_IS_EMPTY:
                errorMessageForPassword.setText("Campo obligatorio");
                break;
            case EMAIL_NOT_FOUND:
                errorMessageForEmail.setText("Email no registrado");
                break;
            case INCORRECT_PASSWORD:
                errorMessageForPassword.setText("Clave incorrecta");
                break;
            default:

                // Intenta conectar a la base de datos antes de proceder
                if (!tryConnectToDatabase()) {
                    showConnectionErrorDialog(); // Muestra el diálogo de error y maneja los intentos de reconexión
                } else {
                	logAttempt("Inicio de sesión exitoso para el usuario con correo electrónico:" + emailField.getText());
                    // Si la conexión es exitosa, abre la ventana SeasonSetupPage
                    SeasonSetupPage seasonSetupPage = new SeasonSetupPage(this);
                    seasonSetupPage.setVisible(true);
                    this.dispose(); // Cierra la ventana actual
                }
                break;
        }
    }

    public int validateLogin(String userEmail, String userPassword) {
        if (userEmail.isEmpty() && userPassword.isEmpty()) {
            return ALL_FIELDS_ARE_EMPTY;
        } else if (userEmail.isEmpty()) {
            return EMAIL_FIELD_IS_EMPTY;
        } else if (userPassword.isEmpty()) {
            return PASSWORD_FIELD_IS_EMPTY;
        }

        try {
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
            query.setParameter("email", userEmail);
            List<User> users = query.getResultList();
            if (!users.isEmpty()) {
                User user = users.get(0);
                if (verifyPassword(userPassword, user.getPassword())) {
                    if (userEmail.equals("admin@laliga.com")) {
                    	admin admin = new admin();
                        admin.setVisible(true);
                        return -1;
                    } else {
                        return LOGIN_SUCCESSFUL;
                    }
                } else {
                    return INCORRECT_PASSWORD;
                }
            } else {
                return EMAIL_NOT_FOUND;
            }
        } catch (Exception e) {
            logAttempt("Error de conexión a la base de datos al intentar iniciar sesión para el usuario con correo electrónico: " + userEmail);
            userDialog("Error de conexión a la base de datos. Iniciando sesión en modo sin conexión.", "Error de conexión");
            // Verificar el inicio de sesión a través del HashMap
            if (loginInfo.containsKey(userEmail) && loginInfo.get(userEmail).equals(userPassword)) {
                return LOGIN_SUCCESSFUL_OFFLINE;
            } else {
                return INCORRECT_PASSWORD_OFFLINE;
            }
        }
    }



    private void userDialog(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }



    private boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        try {
            // Crear una instancia de MessageDigest con el algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Obtener el hash de la contraseña proporcionada por el usuario
            byte[] encodedHash = digest.digest(plainTextPassword.getBytes(StandardCharsets.UTF_8));
            // Convertir el hash obtenido a una representación hexadecimal
            String hashedPasswordAttempt = bytesToHex(encodedHash);
            // Comparar el hash obtenido con el hash almacenado en la base de datos
            return hashedPasswordAttempt.equals(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            // Manejar el error si el algoritmo SHA-256 no está disponible
            e.printStackTrace();
            return false;
        }
    }
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Método para escribir en el archivo de log
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
