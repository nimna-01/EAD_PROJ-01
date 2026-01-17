import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class Contact extends JDialog {

    public Contact(JFrame parent) {
        super(parent, true); // Modal = true (blocks main window)
        initUI();
    }

    private void initUI() {
        // Step 1: Dialog Settings
        setUndecorated(true); // Removes standard borders
        setSize(400, 550);
        setLocationRelativeTo(getOwner());

        // Background color of the very edge
        getContentPane().setBackground(new Color(30, 30, 30));

        // Step 2: Main Panel with a rounded border
        JPanel container = new JPanel(new MigLayout("wrap, fillx, insets 30 35 30 35", "[fill]"));
        container.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc: 40;" +
                "background: #252525;" +
                "border: 2,2,2,2,#3a3a3a"); // Subtle border

        // Header
        JLabel title = new JLabel("Contact Support");
        title.putClientProperty(FlatClientProperties.STYLE, "font: bold +8; foreground: #2ecc71");

        JLabel subtitle = new JLabel("Smart Crop Supply Management");
        subtitle.setForeground(new Color(150, 150, 150));

        // Step 3: The 4 Contact Ways
        JPanel list = new JPanel(new MigLayout("wrap 1, fillx, gapy 12", "[fill]"));
        list.setOpaque(false);

        list.add(createRow("📧 Email", "support@smartcrop.com"));
        list.add(createRow("📞 Hotline", "+94 11 234 5678"));
        list.add(createRow("📍 Location", "Colombo, Sri Lanka"));
        list.add(createRow("💬 WhatsApp", "+94 77 123 4567"));

        // Close Button
        JButton btnClose = new JButton("Dismiss");
        btnClose.putClientProperty(FlatClientProperties.STYLE, "" +
                "background: #333333;" +
                "foreground: #FFFFFF;" +
                "arc: 20;" +
                "borderWidth: 0;" +
                "focusWidth: 0");
        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());

        // Assemble
        container.add(title, "center");
        container.add(subtitle, "center, gapbottom 25");
        container.add(list, "growx, gapbottom 25");
        container.add(btnClose, "h 40!, width 120!, center");

        setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);
    }

    private JPanel createRow(String label, String value) {
        JPanel p = new JPanel(new MigLayout("insets 10 15 10 15", "[]", "[]0[]"));
        p.putClientProperty(FlatClientProperties.STYLE, "arc: 15; background: #2d2d2d");

        JLabel l1 = new JLabel(label);
        l1.putClientProperty(FlatClientProperties.STYLE, "font: -2; foreground: #888888");

        JLabel l2 = new JLabel(value);
        l2.putClientProperty(FlatClientProperties.STYLE, "font: bold; foreground: #FFFFFF");

        p.add(l1, "wrap");
        p.add(l2);
        return p;
    }
}