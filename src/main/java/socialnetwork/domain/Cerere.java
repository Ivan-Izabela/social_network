package socialnetwork.domain;

import java.time.LocalDateTime;

public class Cerere extends Entity<Tuple<Long,Long>>{

    private String status;
    LocalDateTime data;

    public Cerere(Tuple<Long,Long> t) {
        id=t;
        this.status="pending";
        this.data=LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return data;
    }

    public void setDate(LocalDateTime date) {
        this.data = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Cerere{" +
                "idFrom= " + id.getLeft() +
                " idTo= " + id.getRight() +
                " status= "+status+
                " date="+data+
                '}';
    }
}
