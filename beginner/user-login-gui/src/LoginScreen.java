import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LoginScreen extends JFrame {

    JPanel p = new JPanel();

    JLabel title;
    JLabel description;

    JLabel emailLabel;
    JLabel passwordLabel;
    JLabel forgotPassword;

    JTextField emailInput;
    JPasswordField passwordInput;
    JCheckBox checkBox;

    JButton loginBtn;
    JButton googleBtn;
    JButton appleBtn;

    JSeparator s1;
    JSeparator s2;

    public LoginScreen() {
        init();
        buildLayout();

        this.setLayout(null);

        this.setSize(1080, 720);

        this.setLocation(200, 100);

        this.add(p);

        this.setVisible(true);
    }

    public void buildLayout() {
        p.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        p.setSize(1080, 720);
        p.setBackground(Color.decode("#ffffff"));

        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        p.add(title, gbc);

        gbc.gridy = 1;
        p.add(description, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        p.add(emailLabel, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 4;
        p.add(emailInput, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 1;
        p.add(passwordLabel, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 4;
        p.add(passwordInput, gbc);

        gbc.gridy = 6;
        gbc.gridwidth = 1;
        p.add(checkBox, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        p.add(forgotPassword, gbc);

        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        p.add(loginBtn, gbc);

    }

    public void init() {
        initLabels();
        initInputs();
        initButtons();

        p = new JPanel();
    }

    public void initButtons() {
        loginBtn = new JButton("Log In");
        loginBtn.setBackground(Color.decode("#08519c"));
        loginBtn.setForeground(Color.decode("#ffffff"));
        loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    public void initInputs() {
        Border lineBorder = BorderFactory.createLineBorder(Color.decode("#c2c2c2"), 1);
        Border paddingBorder = BorderFactory.createEmptyBorder(10,10,10,10);

        emailInput = new JTextField("your@example.com");
        emailInput.setBackground(Color.decode("#ffffff"));
        emailInput.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
        emailInput.setFont(new Font("Arial", Font.PLAIN, 14));

        passwordInput = new JPasswordField("********");
        passwordInput.setBackground(Color.decode("#ffffff"));
        passwordInput.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
        passwordInput.setFont(new Font("Arial", Font.PLAIN, 14));

        checkBox = new JCheckBox("Remember Me");
        checkBox.setFont(new Font("Arial", Font.PLAIN, 12));
        checkBox.setForeground(Color.decode("#c2c2c2"));
        checkBox.setBackground(Color.decode("#ffffff"));

        Icon normalIcon = new ImageIcon("assets/checkbox-blank-line.png");
        Icon selectedIcon = new ImageIcon("assets/checkbox-line.png");

        checkBox.setIcon(normalIcon);
        checkBox.setSelectedIcon(selectedIcon);
    }

    public void initLabels() {
        title = new JLabel("Welcome Back");
        title.setFont(new Font("Arial", Font.BOLD, 28));

        description = new JLabel("Enter your email and password to access your account.");
        description.setFont(new Font("Arial", Font.PLAIN, 14));
        description.setForeground(Color.decode("#c2c2c2"));

        emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        forgotPassword = new JLabel("Forgot Your Password?");
        forgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        forgotPassword.setForeground(Color.decode("#08519c"));
        forgotPassword.setBorder(BorderFactory.createEmptyBorder(0, 120, 0, 0));
    }

}
