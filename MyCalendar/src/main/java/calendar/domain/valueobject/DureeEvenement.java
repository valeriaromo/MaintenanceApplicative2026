package calendar.domain.valueobject;

import java.util.Objects;

public final class DureeEvenement {

    private final long minutes;

    private DureeEvenement(long minutes) {
        if (minutes < 0) throw new IllegalArgumentException("La durée ne peut pas être négative");
        this.minutes = minutes;
    }

    public static DureeEvenement deMinutes(long minutes) {
        return new DureeEvenement(minutes);
    }

    public long enMinutes() {
        return minutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DureeEvenement that)) return false;
        return minutes == that.minutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes);
    }

    @Override
    public String toString() {
        return minutes + " min";
    }
}