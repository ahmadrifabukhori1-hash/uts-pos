package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Format tanggal dan waktu untuk tampilan. */
public final class DateUtil {

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private DateUtil() {
    }

    public static String format(LocalDateTime dateTime) {
        return dateTime == null ? "" : FORMAT.format(dateTime);
    }
}
