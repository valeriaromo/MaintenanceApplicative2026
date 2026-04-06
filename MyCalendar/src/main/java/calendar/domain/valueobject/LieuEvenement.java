package calendar.domain.valueobject;

import java.util.Objects;

public final class LieuEvenement {

    private final String valeur;

    private LieuEvenement(String valeur) {
        Objects.requireNonNull(valeur, "Le lieu ne peut pas être nul");
        if (valeur.isBlank()) throw new IllegalArgumentException("Le lieu ne peut pas être vide");
        this.valeur = valeur.trim();
    }

    public static LieuEvenement de(String valeur) {
        return new LieuEvenement(valeur);
    }

    public String valeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LieuEvenement that)) return false;
        return valeur.equals(that.valeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur);
    }

    @Override
    public String toString() {
        return valeur;
    }
}