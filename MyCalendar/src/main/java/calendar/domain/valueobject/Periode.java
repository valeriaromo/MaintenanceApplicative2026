package calendar.domain.valueobject;

import java.util.Objects;

public final class Periode {
    private final DateEvenement debut;
    private final DateEvenement fin;

    private Periode(DateEvenement debut, DateEvenement fin) {
        Objects.requireNonNull(debut, "Le début de période ne peut pas être nul");
        Objects.requireNonNull(fin, "La fin de période ne peut pas être nulle");
        if (fin.estAvant(debut)) throw new IllegalArgumentException("La fin de période doit être après le début");
        this.debut = debut;
        this.fin = fin;
    }

    public static Periode entre(DateEvenement debut, DateEvenement fin) {
        return new Periode(debut, fin);
    }

    public DateEvenement debut() {
        return debut;
    }

    public DateEvenement fin() {
        return fin;
    }

    public boolean contient(DateEvenement date) {
        return date.estEntreInclus(debut, fin);
    }

    public boolean chevauche(DateEvenement dateDebut, DateEvenement dateFin) {
        return dateDebut.estAvant(fin) && dateFin.estApres(debut);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Periode that)) return false;
        return debut.equals(that.debut) && fin.equals(that.fin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(debut, fin);
    }
}