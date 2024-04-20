import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public class Calendar {
    private final LocalTime[] hours;

    private Calendar(LocalTime[] hours) {
        this.hours = hours;
    }

    public static Calendar fromFile(String filePath) throws IOException, JSONException, DateTimeParseException {
        String string = Files.readString(Path.of(filePath));
        JSONObject json = new JSONObject(string);
        JSONObject workingHours = json.getJSONObject("working_hours");
        JSONArray meetings = json.getJSONArray("planned_meeting");

        LocalTime[] hours = new LocalTime[meetings.length() * 2 + 2];
        hours[0] = LocalTime.parse(workingHours.getString("start"));
        hours[hours.length - 1] = LocalTime.parse(workingHours.getString("end"));
        for (int i = 0; i < meetings.length(); i++) {
            hours[i * 2 + 1] = LocalTime.parse(meetings.getJSONObject(i).getString("start"));
            hours[i * 2 + 2] = LocalTime.parse(meetings.getJSONObject(i).getString("end"));
        }
        Arrays.sort(hours, 1, hours.length-1);
        return new Calendar(hours);
    }

    public LocalTime getHour(int i) {
        return hours[i];
    }

    public int length() {
        return hours.length;
    }
}
