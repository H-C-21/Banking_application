import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface Date_Time {
    default String getDate_Time() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    default String getDate() {
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        return date.format(now);
    }
}

