import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class AdminDash extends JFrame {

    public JButton btnFarmers, btnCrops, btnSupply, btnInventory, btnReports, btnLogout;
    private String officerName,username2;

    public AdminDash(String name, String usern) {
        this.officerName = name;
        this.username2 = usern;
        initUI();
    }

    private void initUI() {
        setTitle("Officer Dashboard - Smart Crop");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1150, 750);
        setLocationRelativeTo(null);

        // ===== MAIN LAYOUT =====
        JPanel mainPanel = new JPanel(
                new MigLayout("fill, insets 0", "[260!]0[grow,fill]", "[fill]")
        );

        // ===== SIDEBAR =====
        JPanel sidebar = new JPanel(
                new MigLayout("wrap, fillx, insets 30", "[fill]", "[]10[]30[]10[]10[]10[]push[]")
        );
        sidebar.setBackground(new Color(32, 32, 32));

        ImageIcon icon = new ImageIcon("C:\\Users\\SANDANIMNE\\Desktop\\EAD fnl\\logo.png");
        Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        sidebar.add(logo, "center");

        JLabel logoText = new JLabel("SMART CROP");
        logoText.putClientProperty(
                FlatClientProperties.STYLE,
                "font: bold +18; foreground: #2ecc71"
        );
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
        JPanel contentArea = new JPanel(
                new MigLayout("wrap, fill, insets 0", "[grow,fill]", "[]0[grow,fill]")
        );
        contentArea.setBackground(new Color(24, 24, 24));

        // ---- TOP HEADER ----
        JPanel topHeader = new JPanel(
                new MigLayout("fill, insets 0 20 0 20", "[]push[]")
        );
        topHeader.setBackground(new Color(30, 30, 30));
        topHeader.setPreferredSize(new Dimension(0, 70));

        JLabel pageTitle = new JLabel("Officer Overview");
        pageTitle.putClientProperty(
                FlatClientProperties.STYLE,
                "font: bold +5; foreground: #FFFFFF"
        );

        JLabel userProfile = new JLabel("Logged in as: " + username2);
        userProfile.setForeground(new Color(150, 150, 150));

        topHeader.add(pageTitle);
        topHeader.add(userProfile);

        // ---- BODY ----
        JPanel body = new JPanel(
                new MigLayout("wrap, fill, insets 10 20 10 20",
                        "[grow,fill]", "[]15[]15[grow,fill]")
        );
        body.setOpaque(false);

        JLabel welcome = new JLabel("Welcome back, " + officerName + "!");
        welcome.putClientProperty(
                FlatClientProperties.STYLE,
                "font: bold +14; foreground: #FFFFFF"
        );

        JPanel statsRow = new JPanel(
                new MigLayout("fillx, insets 0", "[fill]20[fill]20[fill]")
        );
        statsRow.setOpaque(false);
        statsRow.add(createStatCard("Total Farmers", "1,240", "#3498db"));
        statsRow.add(createStatCard("Active Crops", "45", "#f1c40f"));
        statsRow.add(createStatCard("Supplies Dispatched", "892", "#2ecc71"));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.putClientProperty(
                FlatClientProperties.STYLE,
                "arc: 30; background: #1e1e1e"
        );
        tableContainer.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel hint = new JLabel(
                "Please select a module from the sidebar to view data tables.",
                SwingConstants.CENTER
        );
        hint.setForeground(new Color(90, 90, 90));
        tableContainer.add(hint);

        body.add(welcome);
        body.add(statsRow, "h 130!");
        body.add(tableContainer);

        contentArea.add(topHeader, "h 70!");
        contentArea.add(body);

        mainPanel.add(sidebar);
        mainPanel.add(contentArea);

        setContentPane(mainPanel);
    }

    // ===== BUTTON FACTORY =====
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.putClientProperty(FlatClientProperties.STYLE,
                "arc: 15;" +
                        "background: #202020;" +
                        "foreground: #D0D0D0;" +
                        "borderWidth: 0;" +
                        "focusWidth: 0;" +
                        "margin: 10,20,10,20"
        );
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(45, 45, 45));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(32, 32, 32));
            }
        });
        return btn;
    }

    // ===== STAT CARD =====
    private JPanel createStatCard(String title, String value, String color) {
        JPanel card = new JPanel(new MigLayout("wrap, insets 20", "[fill]"));
        card.putClientProperty(
                FlatClientProperties.STYLE,
                "arc: 25; background: #2a2a2a"
        );

        JLabel t = new JLabel(title.toUpperCase());
        t.putClientProperty(
                FlatClientProperties.STYLE,
                "font: -3; foreground: #AAAAAA"
        );

        JLabel v = new JLabel(value);
        v.putClientProperty(
                FlatClientProperties.STYLE,
                "font: bold +15; foreground: " + color
        );

        card.add(t);
        card.add(v);
        return card;
    }
}
