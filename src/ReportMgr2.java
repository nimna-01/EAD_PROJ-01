import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import db.DBconnection;

public class ReportMgr2 extends JPanel {

    private JComboBox<String> comboReportType;
    private JButton btnGenerate, btnViewReport;
    private JTable reportPreviewTable;
    private DefaultTableModel model;
    private JLabel lblTableStatus;

    public ReportMgr2() {
        setLayout(new MigLayout("fill, insets 20", "[grow]", "[100!]20[grow]"));
        setOpaque(false);
        initUI();
    }

    private void initUI() {
        // --- TOP: CONTROL COMMAND BAR ---
        JPanel controlBar = new JPanel(new MigLayout("fillx, insets 15 25 15 25", "[]20[250!]push[]15[]"));
        controlBar.putClientProperty(FlatClientProperties.STYLE, "arc: 30; background: #2a2a2a");

        JLabel lblSelect = new JLabel("Report Type:");
        lblSelect.putClientProperty(FlatClientProperties.STYLE, "foreground: #bdc3c7; font: bold");

        comboReportType = new JComboBox<>(new String[]{
                "Farmer Report", "Buyer Report", "Officer Report", "Invoice Report"
        });
        comboReportType.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #1e1e1e; foreground: #FFFFFF");

        btnViewReport = new JButton("👁  Preview Data");
        btnViewReport.putClientProperty(FlatClientProperties.STYLE, "background: #3498db; foreground: #FFFFFF; arc: 15; font: bold");

        btnGenerate = new JButton("📑  Generate PDF");
        btnGenerate.putClientProperty(FlatClientProperties.STYLE, "background: #2ecc71; foreground: #FFFFFF; arc: 15; font: bold");

        controlBar.add(lblSelect);
        controlBar.add(comboReportType, "h 40!");
        controlBar.add(btnViewReport, "h 40!, w 150!");
        controlBar.add(btnGenerate, "h 40!, w 200!");

        // --- BOTTOM: PREVIEW DATA TABLE ---
        JPanel tablePanel = new JPanel(new MigLayout("fill, insets 0", "[grow]", "[]10[grow]"));
        tablePanel.setOpaque(false);

        lblTableStatus = new JLabel("Data Preview (Live Records)");
        lblTableStatus.putClientProperty(FlatClientProperties.STYLE, "font: bold +2; foreground: #FFFFFF");

        model = new DefaultTableModel();
        reportPreviewTable = new JTable(model);
        reportPreviewTable.setRowHeight(35);
        reportPreviewTable.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "background: #2a2a2a; foreground: #FFFFFF; font: bold");

        JScrollPane scroll = new JScrollPane(reportPreviewTable);
        scroll.putClientProperty(FlatClientProperties.STYLE, "border: null; background: #1e1e1e");
        scroll.getViewport().setBackground(new Color(30, 30, 30));

        tablePanel.add(lblTableStatus);
        tablePanel.add(scroll, "grow");

        // --- ACTIONS ---
        btnViewReport.addActionListener(e -> loadPreviewData());
        btnGenerate.addActionListener(e -> generateJasperReport());

        add(controlBar, "wrap");
        add(tablePanel, "grow");
    }

    private void loadPreviewData() {
        String selection = comboReportType.getSelectedItem().toString();
        model.setRowCount(0);
        String sql = "";

        switch (selection) {
            case "Farmer Report":
                sql = "SELECT fId, fullName, nic,address, district FROM farmer_tbl";
                setColumns(new String[]{"ID", "Full Name", "NIC","Address", "District"});
                break;
            case "Buyer Report":
                sql = "SELECT userId, fullName, email, contactNo,role FROM register_tbl WHERE role='Buyer'";
                setColumns(new String[]{"ID", "Buyer Name", "Email", "Contact","Role"});
                break;
            case "Officer Report":
                sql = "SELECT userId, fullName, email, contactNo,role FROM register_tbl WHERE role='Officer'"; // Adjust table names as per your DB
                setColumns(new String[]{"ID", "Buyer Name", "Email", "Contact","Role"});
                break;
            case "Invoice Report":
                sql = "SELECT * FROM invoice_tbl"; // Assuming this exists
                setColumns(new String[]{"Inv ID", "Date", "Amount", "Status"});
                break;
        }

        try (Connection conn = DBconnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int cols = model.getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[cols];
                for (int i = 0; i < cols; i++) row[i] = rs.getObject(i + 1);
                model.addRow(row);
            }
            lblTableStatus.setText("Previewing: " + selection + " (" + model.getRowCount() + " records found)");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Table Load Error: " + e.getMessage());
        }
    }

    private void setColumns(String[] cols) {
        model.setColumnIdentifiers(cols);
    }

    private void generateJasperReport() {

        String selection = comboReportType.getSelectedItem().toString();

        if (selection.equals("Select Report")) {
            JOptionPane.showMessageDialog(this, "Please select a report type");
            return;
        }

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        Connection conn = null;

        try {
            conn = DBconnection.getConnection();

            // --- SELECT REPORT FILE ---
            String reportPath = "/reports/" + selection.replace(" ", "") + ".jasper";

            InputStream is = getClass().getResourceAsStream(reportPath);
            if (is == null) {
                throw new RuntimeException("Report not found: " + reportPath);
            }

            JasperReport report = (JasperReport) JRLoader.loadObject(is);

            // --- PARAMETERS (OPTIONAL) ---
            Map<String, Object> params = new HashMap<>();
            params.put("printedBy", "Smart Crop Supply Management System");

            // --- FILL REPORT ---
            JasperPrint jp = JasperFillManager.fillReport(report, params, conn);

            // --- EXPORT PDF ---
            String outputDir = "C:\\Users\\SANDANIMNE\\Desktop\\EAD fnl\\reports\\";
            new java.io.File(outputDir).mkdirs();

            String outputFile = outputDir + selection + "_.pdf";
            JasperExportManager.exportReportToPdfFile(jp, outputFile);

            JOptionPane.showMessageDialog(this,
                    "Report generated successfully!\nSaved at:\n" + outputFile);

            // --- OPEN PDF ---
            Desktop.getDesktop().open(new java.io.File(outputFile));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Jasper Report Error:\n" + ex.getMessage());
            ex.printStackTrace();
        } finally {
            setCursor(Cursor.getDefaultCursor());
            try {
                if (conn != null) conn.close();
            } catch (Exception ignored) {}
        }
    }

}