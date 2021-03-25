package socialnetwork.utils.events;

import socialnetwork.domain.Prietenie;

public class PrietenieChangeEvent implements Event{
    private ChangeEventType type;
    private Prietenie prietenie;

    public PrietenieChangeEvent(ChangeEventType type, Prietenie prietenie) {
        this.type = type;
        this.prietenie = prietenie;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Prietenie getPrietenie() {
        return prietenie;
    }

}
