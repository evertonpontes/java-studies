public class StateMachine {
    private State currentState;
    private CalculatorContext context;

    public StateMachine(State state, CalculatorContext context) {
        this.currentState = state;
        this.context = context;
    }

    public void handleStart(CalculatorEvent event) {
        context = new CalculatorContext();

        currentState = State.PROCESSING;
    }

    private void handleProcessing(CalculatorEvent event) {

        switch (event.getType()) {
            case NUMBER ->
                handleNumber(event.getValue());
                break;
            case OPERATION ->
                handleOperation(event.getValue());
                break;
            case EQUAL ->
                handleEqual();
                break;
            case CLEAR ->
                //handleClear();
                break;
            case CLEAR_ENTRY ->
                //handleClearEntry();
                break;
        }
    }

    private void handleNumber(String entry) {
        if (context.getTypingNumber()) {
            context.setCurrNumber(context.getCurrNumber() + entry);
        } else {
            if (context.getOperationCompleted()) {
                context.setPendingOperation("");
                context.setPrevNumber("");
                context.getOutput1().setText("");
                context.setOperationCompleted(false);
            }
            context.setCurrNumber(entry);
            context.setTypingNumber(true);
        }
        context.getOutput2().setText(context.getCurrNumber());
    }

    private void handleOperation(String entry) {
        if (!context.getPendingOperation().isEmpty()) {
            handleEqual();
        }

        if (context.getCurrNumber().isEmpty()) {
            handleNumber("0");
        }
        if (context.getPrevNumber().isEmpty()) {
            context.setPrevNumber(context.getCurrNumber());
        }
        context.setPendingOperation(entry);
        context.setTypingNumber(false);

        context.getOutput1().setText(
                context.getPrevNumber() +
                context.getPendingOperation() +
                context.getCurrNumber()
        );
    }

    private void handleEqual() {
        if (context.getPendingOperation().isEmpty()) {
            handleOperation("=");
        }

        // calculate operation
        // set prev number with result value
        // set typing number to false
        // update display
    }
}
