import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class OfficerDash extends JFrame {

    public JButton btnFarmers, btnCrops, btnSupply, btnInventory, btnReports, btnLogout;
    private String officerName;

    public OfficerDash(String name) {
        this.officerName = name;
        initUI();
    }

    private void initUI() {
        setTitle("Officer Dashboard - Smart Crop");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1250, 850);
        setLocationRelativeTo(null);

        // --- MAIN LAYOUT ---
        // Column 1: [260!] Fixed sidebar
        // Column 2: [fill]   Growing content area
        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 0", "[260!]0[fill]", "[fill]"));

        // --- SIDEBAR SECTION ---
        JPanel sidebar = new JPanel(new MigLayout("wrap, fillx, insets 25", "[fill]", "[]40[]10[]10[]10[]10[]push[]"));
        sidebar.setBackground(new Color(32, 32, 32));

        JLabel logo = new JLabel("SMART CROP");
        logo.putClientProperty(FlatClientProperties.STYLE, "font: bold +12; foreground: #2ecc71");

        // Initialize Buttons
        btnFarmers = createMenuButton("👨‍🌾  Farmers");
        btnCrops = createMenuButton("🌾  Crops");
        btnSupply = createMenuButton("📦  Prepare Supply");
        btnInventory = createMenuButton("📊  Inventory");
        btnReports = createMenuButton("📄  Reports");
        btnLogout = createMenuButton("🚪  Logout");
        btnLogout.setForeground(new Color(231, 76, 60));

        sidebar.add(logo, "center, gapbottom 20");
        sidebar.add(btnFarmers);
        sidebar.add(btnCrops);
        sidebar.add(btnSupply);
        sidebar.add(btnInventory);
        sidebar.add(btnReports);
        sidebar.add(btnLogout, "gapbottom 10");

        // --- CONTENT AREA SECTION ---
        JPanel contentArea = new JPanel(new MigLayout("wrap, fillx, insets 0", "[fill]", "[]0[fill]"));
        contentArea.setBackground(new Color(24, 24, 24));

        // 1. Top Header Bar
        JPanel topHeader = new JPanel(new MigLayout("fill, insets 0 40 0 40", "[]push[]"));
        topHeader.setBackground(new Color(30, 30, 30));
        topHeader.setPreferredSize(new Dimension(0, 70));

        JLabel pageTitle = new JLabel("Officer Overview");
        pageTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold +5; foreground: #FFFFFF");

        JLabel userProfile = new JLabel("Logged in as: " + officerName);
        userProfile.setForeground(new Color(150, 150, 150));

        topHeader.add(pageTitle, "center");
        topHeader.add(userProfile, "center");

        // 2. Body Area (Stats and Table)
        JPanel body = new JPanel(new MigLayout("wrap, fillx, insets 30 40 30 40", "[fill]"));
        body.setOpaque(false);

        // Welcome Text
        JLabel welcomeLbl = new JLabel("Welcome back, " + officerName + "!");
        welcomeLbl.putClientProperty(FlatClientProperties.STYLE, "font: bold +14; foreground: #FFFFFF");

        // Stats Row (Cards)
        JPanel statsRow = new JPanel(new MigLayout("fillx, insets 0", "[fill]20[fill]20[fill]", "[]"));
        statsRow.setOpaque(false);
        statsRow.add(createStatCard("Total Farmers", "1,240", "#3498db"));
        statsRow.add(createStatCard("Active Crops", "45", "#f1c40f"));
        statsRow.add(createStatCard("Supplies Dispatched", "892", "#2ecc71"));

        // Main Work Panel (Where tables will go)
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.putClientProperty(FlatClientProperties.STYLE, "arc: 30; background: #1e1e1e");
        tableContainer.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel hint = new JLabel("Please select a module from the sidebar to view data tables.", SwingConstants.CENTER);
        hint.setForeground(new Color(80, 80, 80));
        tableContainer.add(hint);

        body.add(welcomeLbl, "gapbottom 20");
        body.add(statsRow, "h 130!, gapbottom 30");
        body.add(tableContainer, "grow, push");

        // Assemble Content Area
        contentArea.add(topHeader, "h 70!");
        contentArea.add(body, "grow");

        // Assemble Everything
        mainPanel.add(sidebar);
        mainPanel.add(contentArea);

        setContentPane(mainPanel);
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 15;" +
                "background: #202020;" +
                "foreground: #D0D0D0;" +
                "borderWidth: 0;" +
                "focusWidth: 0;" +
                "margin: 10,20,10,20");
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Simple Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(45, 45, 45));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(32, 32, 32));
            }
        });
        return btn;
    }

    private JPanel createStatCard(String title, String value, String accentHex) {
        JPanel card = new JPanel(new MigLayout("wrap, insets 20", "[fill]", "[]5[]"));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #2a2a2a");

        JLabel lblTitle = new JLabel(title.toUpperCase());
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font: -3; foreground: #AAAAAA");

        JLabel lblValue = new JLabel(value);
        lblValue.putClientProperty(FlatClientProperties.STYLE, "font: bold +15; foreground: " + accentHex);

        card.add(lblTitle);
        card.add(lblValue);
        return card;
    }
}