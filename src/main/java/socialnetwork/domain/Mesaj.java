package socialnetwork.domain;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Mesaj extends Entity<Long> {
    private Long from;
    private List<Long> to;
    private String message;
    private LocalDateTime date;

    public Mesaj(Long from, List<Long> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date=LocalDateTime.now();
    }

    public Long getFrom() {
        return from;
    }

    public List<Long> getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Mesaj{" +
                "id="+id+
                ", from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")) +
                '}';
    }
}
