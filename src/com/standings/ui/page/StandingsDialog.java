package com.standings.ui.page;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class StandingsDialog extends JDialog {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable standingsTable;
    private DefaultTableModel standingsTableModel;

    public StandingsDialog(JFrame owner, DefaultTableModel standingsTableModel) {
        super(owner, "Clasificación", false);
        this.standingsTableModel = standingsTableModel;

        // Configurar la ventana secundaria
        setSize(315, 160);
        setResizable(false);
        setLocationRelativeTo(owner);

        // Crear un panel para mostrar la tabla de clasificación
        JPanel panel = new JPanel(new BorderLayout());

        // Crear una nueva instancia de JTable utilizando el modelo de la tabla principal
        standingsTable = new JTable(this.standingsTableModel);
        // Deshabilitar la edición de celdas
        standingsTable.setEnabled(false);
        // Configurar el ajuste automático de las celdas al texto
        standingsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Establecer el ancho preferido de las columnas (ajusta estos valores según tus necesidades)
        TableColumn columnEquipo = standingsTable.getColumnModel().getColumn(0); // "Equipo"
        columnEquipo.setPreferredWidth(90);

        TableColumn columnPts = standingsTable.getColumnModel().getColumn(1); // "Pts."
        columnPts.setPreferredWidth(25);

        TableColumn columnPJ = standingsTable.getColumnModel().getColumn(2); // "PJ"
        columnPJ.setPreferredWidth(25);
        
        TableColumn columnPG = standingsTable.getColumnModel().getColumn(3); // "PG"
        columnPG.setPreferredWidth(25);
        
        TableColumn columnPE = standingsTable.getColumnModel().getColumn(4); // "PE"
        columnPE.setPreferredWidth(25);
        
        TableColumn columnPP = standingsTable.getColumnModel().getColumn(5); // "PP"
        columnPP.setPreferredWidth(25);
        
        TableColumn columnGF = standingsTable.getColumnModel().getColumn(6); // "GF"
        columnGF.setPreferredWidth(25);
        
        TableColumn columnGC = standingsTable.getColumnModel().getColumn(7); // "GC"
        columnGC.setPreferredWidth(25);
        
        TableColumn columnDif = standingsTable.getColumnModel().getColumn(8); // "Dif"
        columnDif.setPreferredWidth(30);

        // Establecer el aspecto visual como el de la tabla principal
        standingsTable.setRowHeight(standingsTable.getRowHeight());
        standingsTable.setFont(standingsTable.getFont());
        standingsTable.getTableHeader().setFont(standingsTable.getTableHeader().getFont());

        // Configurar el tamaño preferido del área visible de la tabla
        standingsTable.setPreferredScrollableViewportSize(new Dimension(400, 160));

        panel.add(new JScrollPane(standingsTable), BorderLayout.CENTER);

        setAlwaysOnTop(true);  // Hacer que la ventana esté siempre encima
        add(panel);
    }
}