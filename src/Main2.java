import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.*;
import java.awt.*;

public class Main2 {
    public static void main(String[] args) {
        try {
            // 1. Setup the Modern FlatLaf Dark Look and Feel
            FlatDarkLaf.setup();

            // Global UI Customizations
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("ScrollBar.showButtons", true);
            UIManager.put("Component.innerFocusWidth", 0);
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        // 2. Run the UI on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Create the first screen instance
            OpenForm open = new OpenForm();

            // --- LOGIC FOR BEGIN BUTTON ---
            open.btnBegin.addActionListener(e -> {
                Point currentPos = open.getLocation();
                open.dispose(); // Close the Welcome Page

                LoginForm loginForm = new LoginForm();
                loginForm.setLocation(currentPos);

                // Handle Login Button Click
                loginForm.btnLogin.addActionListener(loginEvent -> {
                    handleLogin(loginForm);
                });

                loginForm.setVisible(true);
            });

            // --- LOGIC FOR CONTACT US POPUP ---
            // FIXED: Removed the redundant 'showContactDialog' call and
            // corrected 'OpenForm' (Class) to 'open' (Instance variable).
            open.btnContact.addActionListener(e -> {
                Contact dialog = new Contact(open);
                dialog.setVisible(true);
            });

            // Display the Welcome Page
            open.setVisible(true);
        });
    }

    /**
     * Logic for checking login credentials
     */
    private static void handleLogin(LoginForm form) {
        String user = form.txtUser.getText();
        String pass = new String(form.txtPass.getPassword());

        if (user.isEmpty() || pass.isEmpty()) {
            // Modern UX: Highlight fields in red if empty
            if(user.isEmpty()) form.txtUser.putClientProperty(FlatClientProperties.OUTLINE, "error");
            if(pass.isEmpty()) form.txtPass.putClientProperty(FlatClientProperties.OUTLINE, "error");

            JOptionPane.showMessageDialog(form,
                    "Please enter both username and password.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            // Clear errors if fixed
            form.txtUser.putClientProperty(FlatClientProperties.OUTLINE, null);
            form.txtPass.putClientProperty(FlatClientProperties.OUTLINE, null);

            System.out.println("Login attempt for user: " + user);
            // This is where you will add your MySQL verification code next
        }
    }
}