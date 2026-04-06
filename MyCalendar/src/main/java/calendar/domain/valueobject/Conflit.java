package calendar.domain.valueobject;

import calendar.domain.event.Evenement;

import java.util.Objects;

public final class Conflit {

    private final Evenement premier;
    private final Evenement second;

    private Conflit(Evenement premier, Evenement second) {
        this.premier = premier;
        this.second = second;
    }

    public static Conflit entre(Evenement premier, Evenement second) {
        return new Conflit(premier, second);
    }

    public Evenement premier() {
        return premier;
    }

    public Evenement second() {
        return second;
    }

    @Override
    public String toString() {
        return "Conflit entre [" + premier.titre() + "] et [" + second.titre() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conflit that)) return false;
        return (premier.equals(that.premier) && second.equals(that.second))
                || (premier.equals(that.second) && second.equals(that.premier));
    }

    @Override
    public int hashCode() {
        return Objects.hash(premier.id(), second.id());
    }
}