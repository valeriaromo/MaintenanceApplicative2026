package calendar.domain.event;

import calendar.domain.valueobject.DateEvenement;
import calendar.domain.valueobject.DureeEvenement;
import calendar.domain.valueobject.EventId;
import calendar.domain.valueobject.Periode;
import calendar.domain.valueobject.Proprietaire;
import calendar.domain.valueobject.TitreEvenement;

import java.util.List;

public final class RendezVousPersonnel extends Evenement {

    private final DureeEvenement duree;

    public RendezVousPersonnel(EventId id, TitreEvenement titre, Proprietaire proprietaire,
                               DateEvenement dateDebut, DureeEvenement duree) {
        super(id, titre, proprietaire, dateDebut);
        this.duree = duree;
    }

    public DureeEvenement duree() {
        return duree;
    }

    @Override
    public DateEvenement dateFin() {
        return dateDebut().plusMinutes(duree);
    }

    @Override
    public String description() {
        return "RDV : " + titre() + " le " + dateDebut() + " (durée : " + duree + ")";
    }

    @Override
    public boolean estDansPeriode(Periode periode) {
        return periode.contient(dateDebut());
    }

    @Override
    public List<DateEvenement> occurrencesDans(Periode periode) {
        return estDansPeriode(periode) ? List.of(dateDebut()) : List.of();
    }
}