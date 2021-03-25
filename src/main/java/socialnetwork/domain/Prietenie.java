package socialnetwork.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Prietenie extends Entity<Tuple<Long,Long>> {

    LocalDateTime date;
    public Prietenie(){

    }

    public Prietenie(Tuple<Long,Long> t) {
        id =t;
        this.date = LocalDateTime.now();


    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "id1=" + id.getLeft() +
                " id2=" + id.getRight() +
                " date= "+ this.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")) +
                '}';
    }
}
