import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import db.DBconnection;

public class UserMgr extends JPanel {

    private JTextField txtFullName, txtUserName, txtPassword, txtContact, txtEmail;
    private JComboBox<String> cbRole;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear;
    private JTable userTable;
    private DefaultTableModel model;
    private JLabel lblTotalUsers;

    public UserMgr(JLabel lblTotalUsers) {
        // Main layout splits the screen: [Scrollable Form (380px)] and [Table Area]
        this.lblTotalUsers = lblTotalUsers;
        setLayout(new MigLayout("fill, insets 10", "[380!]10[grow, fill]", "[fill]"));
        setOpaque(false);
        initUI();
        loadTableData();
    }

    private void initUI() {
        // --- LEFT: SCROLLABLE FORM PANEL ---
        JPanel formContainer = new JPanel(new MigLayout("wrap, fillx, insets 25", "[fill]"));
        formContainer.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #2a2a2a");

        JLabel title = new JLabel("System User Control");
        title.putClientProperty(FlatClientProperties.STYLE, "font: bold +5; foreground: #FFFFFF");
        formContainer.add(title, "gapbottom 20");

        // Input Styling
        txtFullName = createStyledField("Enter Full Name");
        txtUserName = createStyledField("Assign Username");
        txtPassword = new JPasswordField();
        applyFieldStyle(txtPassword, "Assign Password");
        txtContact = createStyledField("e.g. 0771234567");
        txtEmail = createStyledField("e.g. user@smartcrop.com");

        cbRole = new JComboBox<>(new String[]{"Admin", "Officer", "Buyer"});
        cbRole.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #1e1e1e; foreground: #FFFFFF");

        // Adding components to form
        formContainer.add(new JLabel("Full Name")); formContainer.add(txtFullName, "h 40!");
        formContainer.add(new JLabel("Username"), "gaptop 10"); formContainer.add(txtUserName, "h 40!");
        formContainer.add(new JLabel("Password"), "gaptop 10"); formContainer.add(txtPassword, "h 40!");
        formContainer.add(new JLabel("Contact"), "gaptop 10"); formContainer.add(txtContact, "h 40!");
        formContainer.add(new JLabel("Email"), "gaptop 10"); formContainer.add(txtEmail, "h 40!");
        formContainer.add(new JLabel("User Role"), "gaptop 10"); formContainer.add(cbRole, "h 40!");

        // Buttons Grid (2x2)
        JPanel btnPanel = new JPanel(new MigLayout("fillx, insets 0, gap 10", "[fill]5[fill]"));
        btnPanel.setOpaque(false);

        btnAdd = createActionButton("Add User", "#2ecc71");
        btnUpdate = createActionButton("Update", "#3498db");
        btnDelete = createActionButton("Delete", "#e74c3c");
        btnClear = createActionButton("Clear", "#95a5a6");

        btnPanel.add(btnAdd, "h 40!"); btnPanel.add(btnUpdate, "h 40!");
        btnPanel.add(btnDelete, "h 40!"); btnPanel.add(btnClear, "h 40!");
        formContainer.add(btnPanel, "gaptop 30");

        // WRAP THE FORM IN A SCROLLPANE
        JScrollPane formScroll = new JScrollPane(formContainer);
        formScroll.setBorder(null);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);
        formScroll.getVerticalScrollBar().setUnitIncrement(15);

        // --- RIGHT: TABLE AREA ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #1e1e1e");

        model = new DefaultTableModel(new String[]{"ID", "Name", "User", "Role", "Contact", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        userTable = new JTable(model);
        userTable.setRowHeight(40);
        userTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(userTable);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        // --- BUTTON LOGIC ---
        btnAdd.addActionListener(e -> performDatabaseAction("INSERT"));
        btnUpdate.addActionListener(e -> performDatabaseAction("UPDATE"));
        btnDelete.addActionListener(e -> performDatabaseAction("DELETE"));
        btnClear.addActionListener(e -> clearFields());

        userTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = userTable.getSelectedRow();
                txtFullName.setText(model.getValueAt(row, 1).toString());
                txtUserName.setText(model.getValueAt(row, 2).toString());
                cbRole.setSelectedItem(model.getValueAt(row, 3).toString());
                txtContact.setText(model.getValueAt(row, 4).toString());
                txtEmail.setText(model.getValueAt(row, 5).toString());
            }
        });

        add(formScroll, "growy");
        add(tablePanel, "grow");
    }

    private void performDatabaseAction(String type) {
        if (type.equals("INSERT") || type.equals("UPDATE")) {
            if (txtUserName.getText().isEmpty() || txtFullName.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill required fields!");
                return;
            }
        }

        try (Connection conn = DBconnection.getConnection()) {
            String sql;
            if (type.equals("INSERT")) {
                sql = "INSERT INTO register_tbl (fullName, username, password, contactNo, email, role) VALUES (?,?,?,?,?,?)";
            } else if (type.equals("UPDATE")) {
                sql = "UPDATE register_tbl SET fullName=?, username=?, password=?, contactNo=?, email=?, role=? WHERE userId=?";
            } else { // DELETE
                int confirm = JOptionPane.showConfirmDialog(this, "Confirm delete user?", "Warning", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                sql = "DELETE FROM register_tbl WHERE userId=?";
            }

            PreparedStatement pst = conn.prepareStatement(sql);
            if (type.equals("INSERT") || type.equals("UPDATE")) {
                pst.setString(1, txtFullName.getText());
                pst.setString(2, txtUserName.getText());
                pst.setString(3, txtPassword.getText());
                pst.setString(4, txtContact.getText());
                pst.setString(5, txtEmail.getText());
                pst.setString(6, cbRole.getSelectedItem().toString());
                if (type.equals("UPDATE")) {
                    pst.setInt(7, (int) model.getValueAt(userTable.getSelectedRow(), 0));
                }
            } else {
                pst.setInt(1, (int) model.getValueAt(userTable.getSelectedRow(), 0));
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Success!");
            loadTableData();
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadTableData() {
        model.setRowCount(0);
        int count = 0;
        try (Connection conn = DBconnection.getConnection();
             ResultSet rs = conn.createStatement().executeQuery("SELECT userId, fullName, username, role, contactNo, email FROM register_tbl")) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
                count++;
            }
            if(lblTotalUsers != null) lblTotalUsers.setText(String.valueOf(count));
        } catch (Exception e) { e.printStackTrace(); }
    }

    private JTextField createStyledField(String p) {
        JTextField f = new JTextField();
        applyFieldStyle(f, p);
        return f;
    }

    private void applyFieldStyle(JTextField f, String p) {
        f.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, p);
        f.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #1e1e1e; foreground: #FFFFFF; margin: 5,10,5,10");
    }

    private JButton createActionButton(String text, String hex) {
        JButton b = new JButton(text);
        b.putClientProperty(FlatClientProperties.STYLE, "background: " + hex + "; foreground: #FFFFFF; arc: 15; font: bold");
        return b;
    }

    private void clearFields() {
        txtFullName.setText(""); txtUserName.setText(""); txtPassword.setText("");
        txtContact.setText(""); txtEmail.setText(""); cbRole.setSelectedIndex(0);
        userTable.clearSelection();
    }
}