public class CalculatorEvent {
    private final EventType type;
    private final String value;

    public CalculatorEvent(EventType type, String value) {
        this.type = type;
        this.value = value;
    }

    public EventType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
