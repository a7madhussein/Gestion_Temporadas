package com.standings.ui.page;

import javax.persistence.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class admin extends ParentFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntityManagerFactory emf;
    private EntityManager em;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public admin() {
        emf = Persistence.createEntityManagerFactory("bd/Users.odb");
        em = emf.createEntityManager();

        String[] columnNames = {"Nombre completo", "Correo electrónico", "Contraseña"};
        tableModel = new DefaultTableModel(columnNames, 0);
        userTable = new JTable(tableModel);
        loadUsers();

        JButton deleteButton = new JButton("Eliminar usuario seleccionado");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    String email = (String) tableModel.getValueAt(selectedRow, 1);
                    deleteUser(email);
                    loadUsers();
                }
            }
        });

        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(userTable), BorderLayout.CENTER);
        this.add(deleteButton, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        List<User> users = query.getResultList();
        for (User user : users) {
            Object[] row = {user.getFullName(), user.getEmail(), user.getPassword()};
            tableModel.addRow(row);
        }
    }

    public void deleteUser(String email) {
        // No permitir la eliminación del usuario admin
        if (email.equals("admin@laliga.com")) {
        	JOptionPane.showMessageDialog(null, "No se puede eliminar el usuario admin");;
            return;
        }

        em.getTransaction().begin();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        User user = query.getSingleResult();
        if (user != null) {
            em.remove(user);
            em.getTransaction().commit();
        } else {
            System.out.println("El usuario no existe");
        }
    }

    
    
}
