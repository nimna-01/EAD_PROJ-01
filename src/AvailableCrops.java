import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import db.DBconnection;

public class AvailableCrops extends JPanel {

    private JTable cropListTable;
    private DefaultTableModel model;
    private JTextField txtSearch;
    private JButton btnSearch, btnRefresh;
    private JLabel lblTotalAvailable;

    public AvailableCrops(JLabel lblTotalAvailable) {
        this.lblTotalAvailable = lblTotalAvailable;
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
        loadAvailableCrops("");
    }

    private void initUI() {
        // Main split: Left (Filter Sidebar) and Right (Marketplace Table)
        JPanel managerPanel = new JPanel(new MigLayout("fill, insets 0", "[280!]25[grow, fill]", "[fill]"));
        managerPanel.setOpaque(false);

        // --- LEFT COLUMN: SEARCH & FILTERS ---
        JPanel filterPanel = new JPanel(new MigLayout("wrap, fillx, insets 25", "[fill]"));
        filterPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #2a2a2a");

        JLabel title = new JLabel("Marketplace");
        title.putClientProperty(FlatClientProperties.STYLE, "font: bold +6; foreground: #FFFFFF");
        filterPanel.add(title, "gapbottom 10");

        JLabel subtitle = new JLabel("Search for fresh produce");
        subtitle.setForeground(new Color(150, 150, 150));
        filterPanel.add(subtitle, "gapbottom 20");

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter crop name...");
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #1e1e1e; margin: 5,10,5,10");

        btnSearch = new JButton("Search Crops");
        btnSearch.putClientProperty(FlatClientProperties.STYLE, "background: #2ecc71; foreground: #FFFFFF; arc: 15; font: bold");

        btnRefresh = new JButton("Refresh List");
        btnRefresh.putClientProperty(FlatClientProperties.STYLE, "background: #34495e; foreground: #FFFFFF; arc: 15");

        filterPanel.add(new JLabel("Quick Search:"));
        filterPanel.add(txtSearch, "h 40!");
        filterPanel.add(btnSearch, "h 40!, gaptop 10");
        filterPanel.add(btnRefresh, "h 40!");

        // --- RIGHT COLUMN: CROP TABLE ---
        JPanel tableArea = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[]15[grow]"));
        tableArea.setOpaque(false);

        JLabel tableTitle = new JLabel("Available Inventory");
        tableTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #FFFFFF");

        model = new DefaultTableModel(new String[]{"ID", "Crop Name", "Category", "Season", "Farmer/Supplier"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        cropListTable = new JTable(model);
        cropListTable.setRowHeight(45);
        cropListTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "background: #2a2a2a; foreground: #AAAAAA; font: bold");

        JScrollPane tableScroll = new JScrollPane(cropListTable);
        tableScroll.getViewport().setBackground(new Color(30, 30, 30));
        tableScroll.setBorder(BorderFactory.createEmptyBorder());

        tableArea.add(tableTitle, "wrap");
        tableArea.add(tableScroll, "grow");

        // --- LOGIC ---
        btnSearch.addActionListener(e -> loadAvailableCrops(txtSearch.getText().trim()));
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadAvailableCrops("");
        });

        // Instant search on typing
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                loadAvailableCrops(txtSearch.getText().trim());
            }
        });

        managerPanel.add(filterPanel, "growy");
        managerPanel.add(tableArea, "grow");
        add(managerPanel);
    }

    private void loadAvailableCrops(String query) {
        model.setRowCount(0);
        int count = 0;
        String sql = "SELECT * FROM crops_tbl WHERE cropName LIKE ? OR category LIKE ?";

        try (Connection c = DBconnection.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setString(1, "%" + query + "%");
            pst.setString(2, "%" + query + "%");
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5)
                });
                count++;
            }
            lblTotalAvailable.setText(String.valueOf(count));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JLabel createFieldLabel(String t) {
        JLabel l = new JLabel(t);
        l.putClientProperty(FlatClientProperties.STYLE, "foreground: #AAAAAA; font: -1");
        return l;
    }
}