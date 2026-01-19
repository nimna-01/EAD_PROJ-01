import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import db.DBconnection;

public class InventoryMgr extends JPanel {

    private JTable inventoryTable;
    private DefaultTableModel model;
    private JLabel lblTotalStock, lblLowStockAlert;
    private JPanel alertPanel;

    public InventoryMgr() {
        setLayout(new BorderLayout());
        setOpaque(false);
        initUI();
        loadInventoryData();
    }

    private void initUI() {
        // Main split: Left (Summary/Alerts) and Right (Data Table)
        JPanel managerPanel = new JPanel(new MigLayout("fill, insets 0", "[320!]25[grow, fill]", "[fill]"));
        managerPanel.setOpaque(false);

        // --- LEFT COLUMN: SUMMARY & ALERTS ---
        JPanel infoPanel = new JPanel(new MigLayout("wrap, fillx, insets 25", "[fill]"));
        infoPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 25; background: #2a2a2a");

        JLabel title = new JLabel("Stock Summary");
        title.putClientProperty(FlatClientProperties.STYLE, "font: bold +4; foreground: #FFFFFF");
        infoPanel.add(title, "gapbottom 20");

        // Total Stock Card
        JPanel cardTotal = new JPanel(new MigLayout("wrap, insets 15", "[fill]"));
        cardTotal.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #34495e");
        lblTotalStock = new JLabel("Calculating...");
        lblTotalStock.putClientProperty(FlatClientProperties.STYLE, "font: bold +12; foreground: #3498db");
        cardTotal.add(new JLabel("TOTAL ITEMS IN STOCK"));
        cardTotal.add(lblTotalStock);
        infoPanel.add(cardTotal, "h 100!, gapbottom 15");

        // Low Stock Alert Card
        alertPanel = new JPanel(new MigLayout("wrap, insets 15", "[fill]"));
        alertPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #3d2b2b"); // Dark Red Tint
        lblLowStockAlert = new JLabel("All levels stable");
        lblLowStockAlert.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #e74c3c");
        alertPanel.add(new JLabel("⚠️ LOW STOCK ALERTS"));
        alertPanel.add(lblLowStockAlert);
        infoPanel.add(alertPanel, "h 120!");

        JLabel note = new JLabel("<html><body style='width: 200px'>This view is <b>Read-Only</b>. To adjust stock, please use the 'Prepare Supply' or 'Crops' modules.</body></html>");
        note.setForeground(new Color(120, 120, 120));
        infoPanel.add(note, "pushy, bottom");

        // --- RIGHT COLUMN: TABLE ---
        JPanel tableArea = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[]15[grow]"));
        tableArea.setOpaque(false);

        JLabel tableTitle = new JLabel("Inventory Master List");
        tableTitle.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #FFFFFF");

        JTextField txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Quick Filter Inventory...");
        txtSearch.putClientProperty(FlatClientProperties.STYLE, "arc: 20; background: #1e1e1e");

        model = new DefaultTableModel(new String[]{"Item ID", "Crop Name", "Category", "Current Stock", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Read-only
        };

        inventoryTable = new JTable(model);
        inventoryTable.setRowHeight(40);
        JScrollPane tableScroll = new JScrollPane(inventoryTable);
        tableScroll.getViewport().setBackground(new Color(30, 30, 30));

        tableArea.add(tableTitle, "split 2");
        tableArea.add(txtSearch, "growx, h 35!, wrap");
        tableArea.add(tableScroll, "grow");

        managerPanel.add(infoPanel, "growy");
        managerPanel.add(tableArea, "grow");
        add(managerPanel);
    }

    private void loadInventoryData() {
        model.setRowCount(0);
        int totalItems = 0;
        int lowStockCount = 0;

        try (Connection c = DBconnection.getConnection();
             Statement s = c.createStatement();
             ResultSet r = s.executeQuery("SELECT cropId, cropName, category FROM crops_tbl")) {

            while (r.next()) {
                // In a real app, you would join this with a quantities table
                // For this example, we'll simulate stock levels
                int stock = (int)(Math.random() * 100);
                String status = (stock < 20) ? "LOW STOCK" : "IN STOCK";

                model.addRow(new Object[]{
                        r.getInt(1),
                        r.getString(2),
                        r.getString(3),
                        stock + " units",
                        status
                });

                totalItems++;
                if (stock < 20) lowStockCount++;
            }

            lblTotalStock.setText(String.valueOf(totalItems) + " Categories");
            if (lowStockCount > 0) {
                lblLowStockAlert.setText(lowStockCount + " Items need restocking!");
                alertPanel.setBackground(new Color(61, 43, 43)); // Warning red
            } else {
                lblLowStockAlert.setText("All levels stable");
                alertPanel.setBackground(new Color(43, 61, 43)); // Healthy green
            }

        } catch (Exception ex) { ex.printStackTrace(); }
    }
}