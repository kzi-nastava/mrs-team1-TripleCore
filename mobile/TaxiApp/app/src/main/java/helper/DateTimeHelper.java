package helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeHelper {
    public static String getTimeOnly(String isoString) {
        if (isoString == null || isoString.isEmpty()) return "";
        try {
            LocalDateTime dt = LocalDateTime.parse(isoString);
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            return dt.format(timeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            return isoString;
        }
    }

    public static String getDateTime(String isoString) {
        if (isoString == null || isoString.isEmpty()) return "";

        try {
            LocalDateTime dt = LocalDateTime.parse(isoString);
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            return dt.format(formatter);

        } catch (Exception e) {
            e.printStackTrace();
            return isoString;
        }
    }
}
