import javax.swing.*;

public class FarmerDash extends JFrame {

    public FarmerDash(String username) {
        setTitle("Officer Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lbl = new JLabel("Welcome Farmer: " + username);
        lbl.setHorizontalAlignment(JLabel.CENTER);

        add(lbl);
    }
}
