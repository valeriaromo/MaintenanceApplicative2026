package calendar.domain.valueobject;

import java.util.Objects;

public final class TitreEvenement {
    private final String valeur;

    private TitreEvenement(String valeur) {
        Objects.requireNonNull(valeur, "Le titre ne peut pas être nul");
        if (valeur.isBlank()) throw new IllegalArgumentException("Le titre ne peut pas être vide");
        this.valeur = valeur.trim();
    }

    public static TitreEvenement de(String valeur) {
        return new TitreEvenement(valeur);
    }

    public String valeur() {
        return valeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TitreEvenement that)) return false;
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