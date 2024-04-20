import org.json.JSONException;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Calendar c1;
        Calendar c2;
        int meetingDuration;
        final List<Interval> availableIntervals = new ArrayList<>();

        try {
            c1 = Calendar.fromFile("calendar1.json");
            c2 = Calendar.fromFile("calendar2.json");
        } catch (IOException e) {
            System.err.println("File not found!");
            return;
        } catch (JSONException e) {
            System.err.println("Invalid data format!");
            return;
        } catch (DateTimeParseException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.print("Enter meeting duration (minutes): ");
        try (Scanner scanner = new Scanner(System.in)) {
            meetingDuration = scanner.nextInt() * 60;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input! (expected integer)");
            return;
        }

        for (int i1 = 0, i2 = 0; i1 < c1.length() && i2 < c2.length(); ) {
            LocalTime start1 = c1.getHour(i1);
            LocalTime start2 = c2.getHour(i2);
            LocalTime end1 = c1.getHour(i1 + 1);
            LocalTime end2 = c2.getHour(i2 + 1);

            if (start1.isBefore(end2) && start2.isBefore(end1)) {
                LocalTime latestStart = start1.isAfter(start2) ? start1 : start2;
                LocalTime earliestEnd = end1.isBefore(end2) ? end1 : end2;
                int duration = earliestEnd.toSecondOfDay() - latestStart.toSecondOfDay();
                if (duration >= meetingDuration)
                    availableIntervals.add(new Interval(latestStart, earliestEnd));
            }

            if (end1.isBefore(end2))
                i1 += 2;
            else
                i2 += 2;
        }

        System.out.println(availableIntervals);
    }
}