package calendar.domain.valueobject;

import java.util.Objects;

public final class FrequenceJours {

    private final long jours;

    private FrequenceJours(long jours) {
        if (jours <= 0) throw new IllegalArgumentException("La fréquence doit être strictement positive");
        this.jours = jours;
    }

    public static FrequenceJours de(long jours) {
        return new FrequenceJours(jours);
    }

    public long valeur() {
        return jours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FrequenceJours that)) return false;
        return jours == that.jours;
    }

    @Override
    public int hashCode() {
        return Objects.hash(jours);
    }

    @Override
    public String toString() {
        return "tous les " + jours + " jours";
    }
}