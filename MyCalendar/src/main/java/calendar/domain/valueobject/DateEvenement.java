package calendar.domain.valueobject;

import java.time.LocalDateTime;
import java.util.Objects;

public final class DateEvenement {

    private final LocalDateTime valeur;

    private DateEvenement(LocalDateTime valeur) {
        Objects.requireNonNull(valeur, "La date ne peut pas être nulle");
        this.valeur = valeur;
    }

    public static DateEvenement de(LocalDateTime valeur) {
        return new DateEvenement(valeur);
    }

    public static DateEvenement de(int annee, int mois, int jour, int heure, int minute) {
        return new DateEvenement(LocalDateTime.of(annee, mois, jour, heure, minute));
    }

    public LocalDateTime valeur() {
        return valeur;
    }

    public boolean estAvant(DateEvenement autre) {
        return valeur.isBefore(autre.valeur);
    }

    public boolean estApres(DateEvenement autre) {
        return valeur.isAfter(autre.valeur);
    }

    public boolean estEntreInclus(DateEvenement debut, DateEvenement fin) {
        return !valeur.isBefore(debut.valeur) && !valeur.isAfter(fin.valeur);
    }

    public DateEvenement plusMinutes(DureeEvenement duree) {
        return new DateEvenement(valeur.plusMinutes(duree.enMinutes()));
    }

    public DateEvenement plusJours(FrequenceJours frequence) {
        return new DateEvenement(valeur.plusDays(frequence.valeur()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateEvenement that)) return false;
        return valeur.equals(that.valeur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(valeur);
    }

    @Override
    public String toString() {
        return valeur.toString();
    }
}