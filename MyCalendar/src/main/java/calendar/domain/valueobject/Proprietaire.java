package calendar.domain.valueobject;

import java.util.Objects;

public final class Proprietaire {
    private final String nom;

    private Proprietaire(String nom) {
        Objects.requireNonNull(nom, "Le propriétaire ne peut pas être nul");
        if (nom.isBlank()) throw new IllegalArgumentException("Le propriétaire ne peut pas être vide");
        this.nom = nom.trim();
    }

    public static Proprietaire de(String nom) {
        return new Proprietaire(nom);
    }

    public String nom() {
        return nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Proprietaire that)) return false;
        return nom.equals(that.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom);
    }

    @Override
    public String toString() {
        return nom;
    }
}