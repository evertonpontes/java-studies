import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;

public class Calculator implements ActionListener {
    int boardWidth = 360;
    int boardHeight = 540;

    Color customBlack = Color.decode("#2b2b3b");
    Color customGray = Color.decode("#898897");
    Color customDarkGray = Color.decode("#48485c");
    Color customLightGray = Color.decode("#c5c5cd");
    Color customGreen = Color.decode("#0fbd66");

    JFrame f = new JFrame("Simple Calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    String[] buttonValues = {
        "MC", "MR", "M+", "M-",
        "AC", "%", "√", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "±", "0", ".", "="
    };

    String[] memoryButtonValues = {"MC", "MR", "M+", "M-"};
    String[] rightButtonValues = {"÷", "×", "-", "+", "="};
    String[] topButtonValues = {"AC", "%", "√"};

    String prevNumber = "";
    String currNumber = "0";
    String pendingOperation = "";

    boolean isTypingNumber = false;
    boolean isOperationCompleted = false;

    public Calculator() {
        f.setSize(boardWidth, boardHeight);
        f.setLocationRelativeTo(null);
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());

        displayLabel.setBackground(customBlack);
        displayLabel.setForeground(Color.white);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel);
        f.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(6, 4));
        buttonsPanel.setBackground(customBlack);
        f.add(buttonsPanel);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = createButton(i);
            buttonsPanel.add(button);

            button.addActionListener(this);
        }

        f.setVisible(true);
    }

    private JButton createButton(int i) {
        JButton button = new JButton();
        String buttonValue = buttonValues[i];
        button.setFont(new Font("Arial", Font.PLAIN, 30));
        button.setText(buttonValue);
        button.setFocusable(false);
        button.setBorder(new LineBorder(customBlack));
        if (Arrays.asList(memoryButtonValues).contains(buttonValue)) {
            button.setBackground(customBlack);
            button.setForeground(customGray);
        }
        else if (Arrays.asList(topButtonValues).contains(buttonValue)) {
            button.setBackground(customGray);
            button.setForeground(customBlack);
        }
        else if (Arrays.asList(rightButtonValues).contains(buttonValue)) {
            button.setBackground(customGreen);
            button.setForeground(customBlack);
        }
        else {
            button.setBackground(customDarkGray);
            button.setForeground(customLightGray);
        }
        return button;
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String c = e.getActionCommand();

        if (c.charAt(0) >= '0' && c.charAt(0) <= '9' || c.charAt(0) == '.') {
            handleNumber(c);
        }
        else if (c.charAt(0) == '=') {
            handleEqual();
        }
        else if (c.charAt(0) == 'M') {

        }
        else if (c.charAt(0) == '±') {
            handleSignal();
        }
        else if (c.charAt(0) == '%') {
            handlePercentage();
        }
        else if (c.charAt(0) == '√') {
            handleSqrt();
        }
        else if (c.equals("AC")) {
            handleClear();
        }
        else {
            handleOperation(c);
        }
    }

    private void handleNumber(String num) {
        if (isTypingNumber) {
            currNumber = currNumber + num;
        } else {
            currNumber = num;
            isTypingNumber = true;
        }
        displayLabel.setText(currNumber);
    }

    private void handleOperation(String op) {
        if (!pendingOperation.isEmpty() && isTypingNumber) {
            double result = calculateOperation(prevNumber, pendingOperation, currNumber);
            prevNumber = new DecimalFormat("#.############").format(result).replace(',', '.');
            currNumber = new DecimalFormat("#.############").format(result).replace(',', '.');
            displayLabel.setText(currNumber);
        }
        else if (isOperationCompleted) {
            currNumber = "0";
            isOperationCompleted = false;
        }
        else {
            prevNumber = currNumber;
        }
        isTypingNumber = false;
        pendingOperation = op;
    }

    private void handleEqual() {
        double result = 0;

        if (!pendingOperation.isEmpty()) {
            isTypingNumber = false;
            result = calculateOperation(prevNumber, pendingOperation, currNumber);
        }
        else {
            result = Double.parseDouble(currNumber);
        }
        String resultValue = new DecimalFormat("#.############").format(result).replace(',', '.');
        displayLabel.setText(resultValue);
        prevNumber = resultValue;
        isOperationCompleted = true;
    }

    private void handleClear() {
        isOperationCompleted = false;
        isTypingNumber = false;
        currNumber = "0";
        prevNumber = "";
        pendingOperation = "";

        displayLabel.setText("0");
    }

    private void handleSignal() {
        if (currNumber.charAt(0) == '-') {
            currNumber = currNumber.replace("-", "");
        }
        else {
            currNumber = "-" + currNumber;
        }
        displayLabel.setText(currNumber);
    }

    private void handlePercentage() {
        if (!currNumber.isEmpty()) {
            double currNumberDouble = Double.parseDouble(currNumber);
            double percentValue = currNumberDouble / 100;
            currNumber = String.valueOf(percentValue);
        } else {
            currNumber = "0";
        }
        displayLabel.setText(currNumber);
    }

    private void handleSqrt() {
        if (!currNumber.isEmpty()) {
            double currNumberDouble = Double.parseDouble(currNumber);
            double sqrt = Math.sqrt(currNumberDouble);
            currNumber = new DecimalFormat("#.############").format(sqrt).replace(',', '.');
        } else {
            currNumber = "0";
        }
        displayLabel.setText(currNumber);
    }

    private double calculateOperation(String num1, String operation, String num2) {

        return switch (operation) {
            case "-" -> Double.parseDouble(num1) - Double.parseDouble(num2);
            case "+" -> Double.parseDouble(num1) + Double.parseDouble(num2);
            case "÷" -> Double.parseDouble(num1) / Double.parseDouble(num2);
            case "×" -> Double.parseDouble(num1) * Double.parseDouble(num2);
            default -> 0;
        };
    }
}
