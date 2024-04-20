import java.time.LocalTime;

public record Interval (LocalTime start, LocalTime end) {
    @Override
    public String toString() {
        return "[" + start + ", " + end + "]";
    }
}
