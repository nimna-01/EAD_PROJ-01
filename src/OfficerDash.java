import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import db.DBconnection; // Matches your import

public class OfficerDash extends JFrame {

    public JButton btnFarmers, btnCrops, btnSupply, btnInventory, btnReports, btnLogout;
    private String officerName, username2;
    private JPanel tableContainer;
    private JLabel pageTitle;

    public OfficerDash(String name, String usern) {
        this.officerName = name;
        this.username2 = usern;
        initUI();
    }

    private void initUI() {
        setTitle("Officer Dashboard - Smart Crop");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 0, gap 0", "[260!]0[grow,fill]", "[fill]"));

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel(new MigLayout("wrap, fillx, insets 30", "[fill]", "[]10[]30[]10[]10[]10[]push[]"));
        sidebar.setBackground(new Color(32, 32, 32));

        try {
            ImageIcon icon = new ImageIcon("C:\\Users\\SANDANIMNE\\Desktop\\EAD fnl\\logo.png");
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            sidebar.add(new JLabel(new ImageIcon(img)), "center");
        } catch (Exception e) {}

        JLabel logoText = new JLabel("SMART CROP");
        logoText.putClientProperty(FlatClientProperties.STYLE, "font: bold +18; foreground: #2ecc71");
        sidebar.add(logoText, "center, gapbottom 40");

        btnFarmers   = createMenuButton("👨‍🌾  Farmers");
        btnCrops     = createMenuButton("🌾  Crops");
        btnSupply    = createMenuButton("📦  Prepare Supply");
        btnInventory = createMenuButton("📊  Inventory");
        btnReports   = createMenuButton("📄  Reports");
        btnLogout    = createMenuButton("🚪  Logout");
        btnLogout.setForeground(new Color(231, 76, 60));

        sidebar.add(btnFarmers);
        sidebar.add(btnCrops);
        sidebar.add(btnSupply);
        sidebar.add(btnInventory);
        sidebar.add(btnReports);
        sidebar.add(btnLogout);

        // ===== CONTENT AREA =====
        JPanel contentArea = new JPanel(new MigLayout("wrap, fill, insets 0", "[grow,fill]", "[]0[grow,fill]"));
        contentArea.setBackground(new Color(24, 24, 24));

        JPanel topHeader = new JPanel(new MigLayout("fill, insets 0 20 0 20", "[]push[]"));
        topHeader.setBackground(new Color(30, 30, 30));
        topHeader.setPreferredSize(new Dimension(0, 70));

        pageTitle = new JLabel("Officer Overview");
        pageTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold +5; foreground: #FFFFFF");
        JLabel userProfile = new JLabel("Logged in as: " + username2);
        userProfile.setForeground(new Color(150, 150, 150));
        topHeader.add(pageTitle);
        topHeader.add(userProfile);

        JPanel body = new JPanel(new MigLayout("wrap, fill, insets 15 20 20 20", "[grow,fill]", "[]15[]15[grow,fill]"));
        body.setOpaque(false);

        JLabel welcome = new JLabel("Welcome back, " + officerName + "!");
        welcome.putClientProperty(FlatClientProperties.STYLE, "font: bold +14; foreground: #FFFFFF");

        JPanel statsRow = new JPanel(new MigLayout("fillx, insets 0", "[fill]20[fill]20[fill]"));
        statsRow.setOpaque(false);
        statsRow.add(createStatCard("Total Farmers", "1,240", "#3498db"));
        statsRow.add(createStatCard("Active Crops", "45", "#f1c40f"));
        statsRow.add(createStatCard("Supplies Dispatched", "892", "#2ecc71"));

        tableContainer = new JPanel(new BorderLayout());
        tableContainer.putClientProperty(FlatClientProperties.STYLE, "arc: 30; background: #1e1e1e");
        tableContainer.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel hint = new JLabel("Please select a module from the sidebar to view data tables.", SwingConstants.CENTER);
        hint.setForeground(new Color(90, 90, 90));
        tableContainer.add(hint);

        // Sidebar Actions
        btnFarmers.addActionListener(e -> {
            tableContainer.removeAll();
            tableContainer.add(createFarmerManager(), BorderLayout.CENTER);
            tableContainer.revalidate();
            tableContainer.repaint();
            pageTitle.setText("Farmer Management Module");
        });

        body.add(welcome);
        body.add(statsRow, "h 130!");
        body.add(tableContainer, "grow");
        contentArea.add(topHeader, "h 70!");
        contentArea.add(body, "grow");
        mainPanel.add(sidebar);
        mainPanel.add(contentArea);
        setContentPane(mainPanel);
    }

    private JPanel createFarmerManager() {
        JPanel managerPanel = new JPanel(new MigLayout("fill, insets 0", "[320!]20[grow, fill]", "[fill]"));
        managerPanel.setOpaque(false);

        // --- LEFT COLUMN: FORM ---
        JPanel formPanel = new JPanel(new MigLayout("wrap, fillx, insets 20", "[fill]"));
        formPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #2a2a2a");

        JLabel title = new JLabel("Farmer Registration");
        title.putClientProperty(FlatClientProperties.STYLE, "font: bold +3; foreground: #FFFFFF");
        formPanel.add(title, "gapbottom 15");

        // Fields
        formPanel.add(createFieldLabel("Full Name"));
        JTextField txtName = new JTextField();
        formPanel.add(txtName, "h 38!");

        formPanel.add(createFieldLabel("NIC Number"));
        JTextField txtNIC = new JTextField();
        formPanel.add(txtNIC, "h 38!");

        formPanel.add(createFieldLabel("Phone Number"));
        JTextField txtPhone = new JTextField();
        formPanel.add(txtPhone, "h 38!");

        formPanel.add(createFieldLabel("Address"));
        JTextField txtAddress = new JTextField();
        formPanel.add(txtAddress, "h 38!");

        formPanel.add(createFieldLabel("District"));
        JComboBox<String> comboDist = new JComboBox<>(new String[]{"Colombo", "Gampaha", "Galle", "Kandy", "Matara"});
        formPanel.add(comboDist, "h 38!");

        // Buttons
        JButton btnAdd = new JButton("Add Farmer");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "background: #2ecc71; foreground: #FFFFFF; arc: 15; font: bold");

        JButton btnUpdate = new JButton("Update");
        btnUpdate.putClientProperty(FlatClientProperties.STYLE, "background: #3498db; foreground: #FFFFFF; arc: 15");

        JButton btnDelete = new JButton("Delete");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, "background: #e74c3c; foreground: #FFFFFF; arc: 15");

        JButton btnClear = new JButton("Clear");
        btnClear.putClientProperty(FlatClientProperties.STYLE, "background: #444444; foreground: #FFFFFF; arc: 15");

        formPanel.add(btnAdd, "gaptop 15, h 40!");
        formPanel.add(btnUpdate, "split 2, h 35!, growx");
        formPanel.add(btnDelete, "h 35!, growx");
        formPanel.add(btnClear, "h 35!");

        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.setOpaque(false);
        formScroll.getViewport().setOpaque(false);

        // --- RIGHT COLUMN: TABLE AREA ---
        JPanel tableArea = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[]10[grow]"));
        tableArea.setOpaque(false);

        // REFRESH BUTTON (Top Right of Table)
        JButton btnRefresh = new JButton("🔄 Refresh Table");
        btnRefresh.putClientProperty(FlatClientProperties.STYLE, "background: #1e1e1e; foreground: #2ecc71; borderWidth: 1; borderColor: #2ecc71; arc: 10");
        tableArea.add(btnRefresh, "right, wrap");

        String[] cols = {"ID", "Name", "NIC", "Phone","Address", "District"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);
        table.setRowHeight(35);
        tableScroll.getViewport().setBackground(new Color(30, 30, 30));
        tableArea.add(tableScroll, "grow");

        // --- LOGIC ---

        // Refresh Logic
        btnRefresh.addActionListener(e -> loadFarmerData(table));

        // Clear Logic
        btnClear.addActionListener(e -> {
            clearFields(txtName, txtNIC, txtPhone, txtAddress);
            table.clearSelection();
        });

        // Add Logic
        btnAdd.addActionListener(e -> {
            try (Connection conn = DBconnection.getConnection()) {
                String sql = "INSERT INTO farmer_tbl (fullName, nic, phone, address, district) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, txtName.getText());
                pst.setString(2, txtNIC.getText());
                pst.setString(3, txtPhone.getText());
                pst.setString(4, txtAddress.getText());
                pst.setString(5, comboDist.getSelectedItem().toString());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Farmer Added Successfully!");
                loadFarmerData(table);
                clearFields(txtName, txtNIC, txtPhone, txtAddress);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Selection Logic: Click table row to fill form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                txtName.setText(model.getValueAt(row, 1).toString());
                txtNIC.setText(model.getValueAt(row, 2).toString());
                txtPhone.setText(model.getValueAt(row, 3).toString());
                // District and Address might need a separate query or extra hidden columns to fill perfectly
                comboDist.setSelectedItem(model.getValueAt(row, 4).toString());
            }
        });

        // Initial load
        loadFarmerData(table);

        managerPanel.add(formScroll, "growy");
        managerPanel.add(tableArea, "grow");
        return managerPanel;
    }

    private void loadFarmerData(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        try (Connection conn = DBconnection.getConnection()) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT fId,fullName, nic, phone,address, district FROM farmer_tbl");
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }

    private JLabel createFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.putClientProperty(FlatClientProperties.STYLE, "foreground: #AAAAAA; font: -1");
        return lbl;
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #202020; foreground: #D0D0D0; borderWidth: 0; focusWidth: 0; margin: 10,20,10,20");
        return btn;
    }

    private JPanel createStatCard(String title, String value, String color) {
        JPanel card = new JPanel(new MigLayout("wrap, insets 20", "[fill]"));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #2a2a2a");
        JLabel v = new JLabel(value);
        v.putClientProperty(FlatClientProperties.STYLE, "font: bold +15; foreground: " + color);
        card.add(new JLabel(title.toUpperCase()));
        card.add(v);
        return card;
    }
}