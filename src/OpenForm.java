import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.*;

public class OpenForm extends JFrame {
    public JButton btnBegin;
    public JButton btnContact;

    public OpenForm() {
        initUI();
    }

    private void initUI() {
        setTitle("Smart Crop Supply Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 650);
        setLocationRelativeTo(null);

        // Main Background
        JPanel mainPanel = new JPanel(new MigLayout("fill, insets 20", "[center]", "[center]"));
        mainPanel.setBackground(new Color(30, 30, 30));

        // The Welcome Card
        JPanel card = new JPanel(new MigLayout("wrap, fillx, insets 40 50 40 50", "[center]"));
        card.putClientProperty(FlatClientProperties.STYLE, "arc: 40; background: #252525");

        // App Logo/Icon Placeholder (Using Text for now)
        JLabel logoLabel = new JLabel("🌿"); // You can replace this with an ImageIcon later
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 60));

        // App Title
        // Note: Using <html> allows text wrapping for long names
        JLabel title = new JLabel("<html><center>Smart Crop Supply<br>Management System</center></html>");
        title.putClientProperty(FlatClientProperties.STYLE, "font: bold +16; foreground: #FFFFFF");
        title.setHorizontalAlignment(SwingConstants.CENTER);

        // Subtitle/Tagline
        JLabel subtitle = new JLabel("Efficiency in every harvest.");
        subtitle.setForeground(new Color(150, 150, 150));
        subtitle.setFont(new Font("Segoe UI", Font.ITALIC, 14));

        // "Begin" Button (Green)
        btnBegin = new JButton("Get Started");
        btnBegin.putClientProperty(FlatClientProperties.STYLE,
                "background: #2ecc71; foreground: #FFFFFF; arc: 25; font: bold +2; borderWidth: 0; focusWidth: 0");
        btnBegin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Contact Us Link
        btnContact = new JButton("Contact Support");
        btnContact.setForeground(new Color(100, 100, 100));
        btnContact.setBorderPainted(false);
        btnContact.setContentAreaFilled(false);
        btnContact.setFocusPainted(false);
        btnContact.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Assemble
        // ... previous code ...

// Assemble
        card.add(logoLabel, "gapbottom 10");
        card.add(title, "gapbottom 5");
        card.add(subtitle, "gapbottom 40");
        card.add(btnBegin, "width 220!, h 50!");

// FIXED: changed 'gatop' to 'gaptop'
        card.add(btnContact, "gaptop 20");

        mainPanel.add(card, "width 400!");
        add(mainPanel);
    }
}