package calendar.domain.valueobject;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class Participants {

    private final List<String> noms;

    private Participants(List<String> noms) {
        Objects.requireNonNull(noms, "La liste de participants ne peut pas être nulle");
        if (noms.isEmpty()) throw new IllegalArgumentException("Il faut au moins un participant");
        this.noms = List.copyOf(noms);
    }

    public static Participants de(List<String> noms) {
        return new Participants(noms);
    }

    public static Participants de(String... noms) {
        return new Participants(List.of(noms));
    }

    public List<String> liste() {
        return Collections.unmodifiableList(noms);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participants that)) return false;
        return noms.equals(that.noms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noms);
    }

    @Override
    public String toString() {
        return noms.stream().collect(Collectors.joining(", "));
    }
}