import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Calculator extends JFrame implements ActionListener {

    JPanel panel;

    JPanel display;

    JTextField output1;
    JTextField output2;

    List<JButton> numbers = new ArrayList<>();

    HashMap<String, JButton> features = new HashMap<>();

    String num1, operation, num2;

    public Calculator() {
        num1 = operation = num2 = "";

        panel = new JPanel();
        display = new JPanel();

        output1 = new JTextField();
        output2 = new JTextField();

        for(int i = 0; i < 10; i++) {
            JButton btn = new JButton(String.valueOf(i));

            btn.addActionListener(this);

            numbers.add(btn);
        }

        // Memory feature
        features.put("MC", new JButton("MC")); // Memory clear
        features.get("MC").addActionListener(this);
        features.put("MR", new JButton("MR")); // Memory recovery
        features.get("MR").addActionListener(this);
        features.put("M+", new JButton("M+")); // Memory sum
        features.get("M+").addActionListener(this);
        features.put("M-", new JButton("M-")); // Memory sub
        features.get("M-").addActionListener(this);

        // Operations
        features.put("×", new JButton("×"));
        features.get("×").addActionListener(this);
        features.put("÷", new JButton("÷"));
        features.get("÷").addActionListener(this);
        features.put("+", new JButton("+"));
        features.get("+").addActionListener(this);
        features.put("-", new JButton("-"));
        features.get("-").addActionListener(this);
        features.put("=", new JButton("="));
        features.get("=").addActionListener(this);

        // Others features
        features.put("±", new JButton("±"));
        features.get("±").addActionListener(this);
        features.put("√", new JButton("√"));
        features.get("√").addActionListener(this);
        features.put("%", new JButton("%"));
        features.get("%").addActionListener(this);
        features.put("CE", new JButton("CE"));
        features.get("CE").addActionListener(this);
        features.put("C", new JButton("C"));
        features.get("C").addActionListener(this);
        features.put(".", new JButton("."));
        features.get(".").addActionListener(this);

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
        display.setLayout(new GridLayout(2, 1));

        display.add(output1);
        display.add(output2);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.BOTH;

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
        gbc.gridheight = 3;
        panel.add(features.get("+"), gbc);
        gbc.gridheight = 1;

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(features.get("C"), gbc);

        gbc.gridx = 1;
        panel.add(numbers.getFirst(), gbc);
        gbc.gridx = 2;
        panel.add(features.get("."), gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(features.get("CE"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(features.get("="), gbc);

        gbc.gridwidth = 1;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        String output1Text = output1.getText();

        if (command.charAt(0) >= '0' && command.charAt(0) <= '9' || command.charAt(0) == '.') {

            if (output1Text.isEmpty() || output1Text.charAt(output1Text.length() - 1) != '=') {
                if (!operation.isEmpty()) {
                    num2 = num2 + command;
                    output2.setText(num2);
                } else {
                    num1 = num1 + command;
                    output2.setText(num1 + operation + num2);
                }
            } else {
                num1 = command;
                num2 = "";
                operation = "";
                output1.setText("");
                output2.setText(num1 + operation + num2);
            }


        }

        else if (command.charAt(0) == '=') {
            if (num2.isEmpty()) {
                num2 = num1;
            }

            calculate();
        }

        else if (command.charAt(0) == '±') {
            if (!operation.isEmpty()) {
                if (num2.isEmpty()) {
                    num2 = changeSignal(num1);
                } else {
                    num2 = changeSignal(num2);
                }

                output2.setText(num2);
            } else {
                if (!num1.isEmpty()) {
                    num1 = changeSignal(num1);
                }

                output2.setText(num1);
            }
        }

        else if (command.charAt(0) == 'C') {
            if (command.equals("C")) {
                num1 = operation = num2 = "";
                output1.setText("");
                output2.setText("0");
            } else {
                if (operation.isEmpty()) {
                    num1 = "";
                } else {
                    num2 = "";
                }
                output2.setText("0");
            }
        }

        else {
            if (!operation.isEmpty() && !num2.isEmpty()) {
                calculate();
            }
             operation = command;

            output1.setText(num1 + operation + num2);
        }

    }

    public void calculate() {
        double result = switch (operation) {
            case "+" -> Double.parseDouble(num1) + Double.parseDouble(num2);
            case "-" -> Double.parseDouble(num1) - Double.parseDouble(num2);
            case "×" -> Double.parseDouble(num1) * Double.parseDouble(num2);
            case "÷" -> Double.parseDouble(num1) / Double.parseDouble(num2);
            default -> 0.0;
        };

        output1.setText(num1 + operation + num2 + "=");
        output2.setText(Double.toString(result));
        num1 = Double.toString(result);
        //num2 = "";
    }

    public String changeSignal(String number) {
        if (number.charAt(0) == '-') {
            return number.replace("-", "");
        } else {
            return "-" + number;
        }
    }
}
