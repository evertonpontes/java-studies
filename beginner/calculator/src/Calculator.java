import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Calculator extends JFrame {

    JPanel panel;

    JTextField display;

    List<JButton> numbers = new ArrayList<>();

    HashMap<String, JButton> features = new HashMap<>();

    public Calculator() {
        panel = new JPanel();

        display = new JTextField();

        for(int i = 0; i < 10; i++) {
            JButton btn = new JButton(String.valueOf(i));

            numbers.add(btn);
        }

        // Memory feature
        features.put("MC", new JButton("MC")); // Memory clear
        features.put("MR", new JButton("MR")); // Memory recovery
        features.put("M+", new JButton("M+")); // Memory sum
        features.put("M-", new JButton("M-")); // Memory sub

        // Operations
        features.put("×", new JButton("×"));
        features.put("÷", new JButton("÷"));
        features.put("+", new JButton("+"));
        features.put("-", new JButton("-"));
        features.put("=", new JButton("="));

        // Others features
        features.put("±", new JButton("±"));
        features.put("√", new JButton("√"));
        features.put("%", new JButton("%"));
        features.put("CE", new JButton("CE"));
        features.put("C", new JButton("C"));

    }

    public void initCalculator() {
        this.setBounds(100, 200, 315, 400);

        this.setLayout(null);

        panel.setSize(300, 400);

        setPanelLayout();

        this.add(panel);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setVisible(true);
    }

    public void setPanelLayout() {
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(display, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(features.get("MC"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(features.get("MR"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        panel.add(features.get("M+"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        panel.add(features.get("M-"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(features.get("±"), gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(features.get("√"), gbc);
        gbc.gridx = 2;
        gbc.gridy = 2;
        panel.add(features.get("%"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        panel.add(features.get("÷"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 3;
        panel.add(features.get("×"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 4;
        panel.add(features.get("-"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 5;
        panel.add(features.get("+"), gbc);
        gbc.gridx = 3;
        gbc.gridy = 6;
        panel.add(features.get("="), gbc);

        gbc.gridx = 0;
        panel.add(features.get("C"), gbc);
        gbc.gridx = 1;
        panel.add(features.get("CE"), gbc);
        gbc.gridx = 2;
        panel.add(numbers.getFirst(), gbc);

        for(int i = 0; i < 3; i++) {
            gbc.gridy = i + 3;
            for(int j = 2; j >= 0; j--) {
                gbc.gridx = 2 - j;
                int index = ((numbers.size() - 1) - j) - (i * 3);

                panel.add(numbers.get(index), gbc);
            }
        }



    }

    public static void  main(String[] args) {
        Calculator c = new Calculator();

        c.initCalculator();
    }
}
