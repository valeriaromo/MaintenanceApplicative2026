package calendar.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public final class EventId {

    private final String value;

    private EventId(String value) {
        Objects.requireNonNull(value, "L'identifiant ne peut pas être nul");
        if (value.isBlank()) throw new IllegalArgumentException("L'identifiant ne peut pas être vide");
        this.value = value;
    }

    public static EventId generer() {
        return new EventId(UUID.randomUUID().toString());
    }

    public static EventId de(String value) {
        return new EventId(value);
    }

    public String valeur() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventId eventId)) return false;
        return value.equals(eventId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}