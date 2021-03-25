package socialnetwork.utils.events;

import socialnetwork.domain.Cerere;

public class CerereChangeEvent implements Event {
    private ChangeEventType type;
    private Cerere cerere;

    public CerereChangeEvent(ChangeEventType type, Cerere cerere) {
        this.type = type;
        this.cerere = cerere;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Cerere getCerere() {
        return cerere;
    }
}

