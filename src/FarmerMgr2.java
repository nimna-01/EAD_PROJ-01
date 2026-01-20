import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import db.DBconnection;

public class FarmerMgr2 extends JPanel {

    private JTextField txtFullName, txtNic, txtContact, txtAddress, txtDistrict;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable farmerTable;
    private DefaultTableModel model;
    private JLabel lblTotalFarmers; // Reference to update dashboard stat

    public FarmerMgr2(JLabel lblTotalFarmers) {
        this.lblTotalFarmers = lblTotalFarmers;
        // Layout: [Scrollable Side Form (380px)] [Gap] [Table Area]
        setLayout(new MigLayout("fill, insets 10", "[380!]10[grow, fill]", "[fill]"));
        setOpaque(false);
        initUI();
        loadTableData();
    }

    private void initUI() {
        // --- LEFT: SCROLLABLE FORM ---
        JPanel formContainer = new JPanel(new MigLayout("wrap, fillx, insets 25", "[fill]"));
        formContainer.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #2a2a2a");

        JLabel title = new JLabel("Farmer Registry System");
        title.putClientProperty(FlatClientProperties.STYLE, "font: bold +5; foreground: #FFFFFF");
        formContainer.add(title, "gapbottom 20");

        // Inputs
        txtFullName = createStyledField("Enter Farmer Full Name");
        txtNic      = createStyledField("Enter NIC Number");
        txtContact  = createStyledField("e.g. 071XXXXXXX");
        txtAddress  = createStyledField("Residential Address");
        txtDistrict = createStyledField("Primary Farming District");

        formContainer.add(new JLabel("Full Name")); formContainer.add(txtFullName, "h 40!");
        formContainer.add(new JLabel("NIC Number"), "gaptop 10"); formContainer.add(txtNic, "h 40!");
        formContainer.add(new JLabel("Contact Number"), "gaptop 10"); formContainer.add(txtContact, "h 40!");
        formContainer.add(new JLabel("Address"), "gaptop 10"); formContainer.add(txtAddress, "h 40!");
        formContainer.add(new JLabel("District"), "gaptop 10"); formContainer.add(txtDistrict, "h 40!");

        // Action Buttons
        JPanel btnPanel = new JPanel(new MigLayout("fillx, insets 0, gap 10", "[fill]5[fill]"));
        btnPanel.setOpaque(false);

        btnAdd    = createActionButton("Register", "#2ecc71");
        btnUpdate = createActionButton("Update Info", "#3498db");
        btnDelete = createActionButton("Remove", "#e74c3c");
        btnClear  = createActionButton("Reset", "#95a5a6");

        btnPanel.add(btnAdd, "h 40!");    btnPanel.add(btnUpdate, "h 40!");
        btnPanel.add(btnDelete, "h 40!"); btnPanel.add(btnClear, "h 40!");
        formContainer.add(btnPanel, "gaptop 30");

        // ScrollPane for Form
        JScrollPane formScroll = new JScrollPane(formContainer);
        formScroll.setBorder(null);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formScroll.getVerticalScrollBar().setUnitIncrement(15);

        // --- RIGHT: TABLE ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #1e1e1e");

        model = new DefaultTableModel(new String[]{"ID", "Full Name", "NIC", "Contact", "District", "Address"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        farmerTable = new JTable(model);
        farmerTable.setRowHeight(40);
        farmerTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(farmerTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // --- EVENTS ---
        btnAdd.addActionListener(e -> performAction("INSERT"));
        btnUpdate.addActionListener(e -> performAction("UPDATE"));
        btnDelete.addActionListener(e -> performAction("DELETE"));
        btnClear.addActionListener(e -> clearFields());

        farmerTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = farmerTable.getSelectedRow();
                txtFullName.setText(model.getValueAt(row, 1).toString());
                txtNic.setText(model.getValueAt(row, 2).toString());
                txtContact.setText(model.getValueAt(row, 3).toString());
                txtDistrict.setText(model.getValueAt(row, 4).toString());
                txtAddress.setText(model.getValueAt(row, 5).toString());
            }
        });

        add(formScroll, "growy");
        add(tablePanel, "grow");
    }

    private void performAction(String type) {
        if ((type.equals("INSERT") || type.equals("UPDATE")) && txtFullName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Farmer name is required!");
            return;
        }

        try (Connection conn = DBconnection.getConnection()) {
            String sql;
            if (type.equals("INSERT")) {
                sql = "INSERT INTO farmer_tbl (fullName, nic, phone, address, district) VALUES (?,?,?,?,?)";
            } else if (type.equals("UPDATE")) {
                sql = "UPDATE farmer_tbl SET fullName=?, nic=?, phone=?, address=?, district=? WHERE fId=?";
            } else {
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this farmer?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                sql = "DELETE FROM farmer_tbl WHERE fId=?";
            }

            PreparedStatement pst = conn.prepareStatement(sql);
            if (!type.equals("DELETE")) {
                pst.setString(1, txtFullName.getText());
                pst.setString(2, txtNic.getText());
                pst.setString(3, txtContact.getText());
                pst.setString(4, txtAddress.getText());
                pst.setString(5, txtDistrict.getText());
                if (type.equals("UPDATE")) pst.setInt(6, (int) model.getValueAt(farmerTable.getSelectedRow(), 0));
            } else {
                pst.setInt(1, (int) model.getValueAt(farmerTable.getSelectedRow(), 0));
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Success!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void loadTableData() {
        model.setRowCount(0);
        int count = 0;
        try (Connection conn = DBconnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT fId, fullName, nic, phone, address, district FROM farmer_tbl")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
                count++;
            }
            if(lblTotalFarmers != null) lblTotalFarmers.setText(String.valueOf(count));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JTextField createStyledField(String p) {
        JTextField f = new JTextField();
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, p);
        f.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #1e1e1e; foreground: #FFFFFF; margin: 5,10,5,10");
        return f;
    }

    private JButton createActionButton(String text, String hex) {
        JButton b = new JButton(text);
        b.putClientProperty(FlatClientProperties.STYLE, "background: " + hex + "; foreground: #FFFFFF; arc: 15; font: bold");
        return b;
    }

    private void clearFields() {
        txtFullName.setText(""); txtNic.setText(""); txtContact.setText("");
        txtAddress.setText(""); txtDistrict.setText("");
        farmerTable.clearSelection();
    }
}