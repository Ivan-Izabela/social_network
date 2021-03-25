package socialnetwork.utils.events;

import socialnetwork.domain.Utilizator;

public class UtilizatorChangeEvent implements Event{
    private ChangeEventType type;
    private Utilizator utilizator;

    public UtilizatorChangeEvent(ChangeEventType type, Utilizator utilizator) {
        this.type = type;
        this.utilizator = utilizator;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getUtilizator() {
        return utilizator;
    }
}
