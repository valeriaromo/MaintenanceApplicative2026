package calendar.domain.event;

import calendar.domain.valueobject.*;

import java.util.List;

public final class Reunion extends Evenement {

    private final DureeEvenement duree;
    private final LieuEvenement lieu;
    private final Participants participants;

    public Reunion(EventId id, TitreEvenement titre, Proprietaire proprietaire,
                   DateEvenement dateDebut, DureeEvenement duree,
                   LieuEvenement lieu, Participants participants) {
        super(id, titre, proprietaire, dateDebut);
        this.duree = duree;
        this.lieu = lieu;
        this.participants = participants;
    }

    public DureeEvenement duree() { return duree; }
    public LieuEvenement lieu() { return lieu; }
    public Participants participants() { return participants; }

    @Override
    public DateEvenement dateFin() {
        return dateDebut().plusMinutes(duree);
    }

    @Override
    public String description() {
        return "Réunion : " + titre()
                + " à " + lieu
                + " avec " + participants
                + " (" + dateDebut() + ", durée : " + duree + ")";
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