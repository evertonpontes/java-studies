import javax.swing.*;

public class CalculatorContext {

    private JTextField output1;
    private JTextField output2;

    private String prevNumber; // previously pressed number
    private String currNumber; // current number

    private String pendingOperation; // pending operation

    private Boolean isTypingNumber;
    private Boolean isOperationCompleted;

    public String getPrevNumber() {
        return prevNumber;
    }

    public void setPrevNumber(String prevNumber) {
        this.prevNumber = prevNumber;
    }

    public String getCurrNumber() {
        return currNumber;
    }

    public void setCurrNumber(String currNumber) {
        this.currNumber = currNumber;
    }

    public String getPendingOperation() {
        return pendingOperation;
    }

    public void setPendingOperation(String pendingOperation) {
        this.pendingOperation = pendingOperation;
    }

    public Boolean getTypingNumber() {
        return isTypingNumber;
    }

    public void setTypingNumber(Boolean typingNumber) {
        isTypingNumber = typingNumber;
    }

    public Boolean getOperationCompleted() {
        return isOperationCompleted;
    }

    public void setOperationCompleted(Boolean operationCompleted) {
        isOperationCompleted = operationCompleted;
    }

    public JTextField getOutput1() {
        return output1;
    }

    public void setOutput1(JTextField output1) {
        this.output1 = output1;
    }

    public JTextField getOutput2() {
        return output2;
    }

    public void setOutput2(JTextField output2) {
        this.output2 = output2;
    }
}
