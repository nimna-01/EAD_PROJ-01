import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatClientProperties;
import db.DBconnection;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main2 {

    public static void main(String[] args) {
        try {
            FlatDarkLaf.setup();
            UIManager.put("Button.arc", 20);
            UIManager.put("Component.focusWidth", 1);
        } catch (Exception e) {
            System.err.println("FlatLaf init failed");
        }

        SwingUtilities.invokeLater(() -> showWelcome(new Point(200, 200)));

    }

    // ---------- WELCOME ----------
    private static void showWelcome(Point pos) {
        OpenForm open = new OpenForm();
        open.btnBegin.addActionListener(e -> switchFrame(open, Main2::showLogin));
        open.btnContact.addActionListener(e -> new Contact(open).setVisible(true));
        open.setVisible(true);
    }

    // ---------- LOGIN ----------
    private static void showLogin(Point pos) {
        LoginForm login = new LoginForm();
        login.setLocation(pos);

        login.btnLogin.addActionListener(e -> handleLogin(login));
        login.btnRegister.addActionListener(e -> switchFrame(login, Main2::showRegister));

        login.setVisible(true);
    }

    // ---------- LOGIN LOGIC (UPDATED WITH DB CHECK) ----------
    private static void handleLogin(LoginForm f) {

        String username = f.txtUser.getText().trim();
        String password = new String(f.txtPass.getPassword()).trim();

        f.txtUser.putClientProperty(FlatClientProperties.OUTLINE, username.isEmpty() ? "error" : null
        );
        f.txtPass.putClientProperty(FlatClientProperties.OUTLINE, password.isEmpty() ? "error" : null
        );

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(f, "Please enter both username and password.", "Login Error", JOptionPane.WARNING_MESSAGE);return;
        }

        String query = "SELECT userId, username, fullname, role " + "FROM register_tbl " + "WHERE username=? AND password=?";

        try (Connection con = DBconnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                int id = rs.getInt("userId");
                String fullName = rs.getString("fullname");
                String role = rs.getString("role");
                String userName2 = rs.getString("username");

                JOptionPane.showMessageDialog(f, "System Login has Succeeded!", "Access Granted", JOptionPane.INFORMATION_MESSAGE
                );

                f.dispose();

                if ("OFFICER".equalsIgnoreCase(role)) {
                    showOfficerDash(fullName, userName2, id);
                } else if ("ADMIN".equalsIgnoreCase(role)) {
                    showAdminDash(fullName, userName2, id);
                } else if ("BUYER".equalsIgnoreCase(role)) {
                    showBuyerDash(fullName, userName2, id);
                } else {
                    JOptionPane.showMessageDialog(null, "Unknown role: " + role, "Login Error", JOptionPane.ERROR_MESSAGE
                    );
                }

            } else {
                JOptionPane.showMessageDialog(f, "Invalid username or password", "Access Denied", JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(f, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE
            );ex.printStackTrace();
        }
    }


    // ---------- REGISTER ----------
    private static void showRegister(Point pos) {
        RegisterForm reg = new RegisterForm();
        reg.setLocation(pos);

        // This calls the performRegistration method we wrote in the RegisterForm class
        reg.btnSubmit.addActionListener(e -> reg.btnSubmitActionPerformed());
        reg.btnBack.addActionListener(e -> switchFrame(reg, Main2::showLogin));

        reg.setVisible(true);
    }

    // ---------- OFFICER DASHBOARD ----------
    private static void showOfficerDash(String fullName,String userName2,int Id) {
        OfficerDash dash = new OfficerDash(fullName,userName2,Id);

        dash.btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dash, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dash.dispose();
                showWelcome(new Point(200, 200));
            }
        });
        dash.btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dash, "Are you sure you want exit from application?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        dash.setVisible(true);
    }
    // ---------- ADMIN DASHBOARD ----------
    private static void showAdminDash(String fullName,String userName2,int Id) {
        AdminDash dash = new AdminDash(fullName,userName2,Id);

        dash.btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dash, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dash.dispose();
                showWelcome(new Point(200, 200));
            }
        });
        dash.btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dash, "Are you sure you want exit from application?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        dash.setVisible(true);
    }
    // ---------- OFFICER DASHBOARD ----------
    private static void showBuyerDash(String fullName,String userName2,int Id) {
        BuyerDash dash = new BuyerDash(fullName,userName2,Id);

        dash.btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dash, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dash.dispose();
                showWelcome(new Point(200, 200));
            }
        });
        dash.btnExit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dash, "Are you sure you want exit from application?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        dash.setVisible(true);
    }



    // ---------- COMMON FRAME SWITCH ----------
    private static void switchFrame(JFrame current, java.util.function.Consumer<Point> next) {
        Point p = current.getLocation();
        current.dispose();
        next.accept(p);
    }
}