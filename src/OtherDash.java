import javax.swing.*;

public class OtherDash extends JFrame {

    public OtherDash(String username) {
        setTitle("Buyer Dashboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel lbl = new JLabel("Welcome Buyer: " + username);
        lbl.setHorizontalAlignment(JLabel.CENTER);

        add(lbl);
    }
}
