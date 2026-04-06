package calendar.domain.event;

import calendar.domain.valueobject.DateEvenement;
import calendar.domain.valueobject.EventId;
import calendar.domain.valueobject.FrequenceJours;
import calendar.domain.valueobject.Periode;
import calendar.domain.valueobject.Proprietaire;
import calendar.domain.valueobject.TitreEvenement;

import java.util.ArrayList;
import java.util.List;

public final class EvenementPeriodique extends Evenement {

    private final FrequenceJours frequence;

    public EvenementPeriodique(EventId id, TitreEvenement titre, Proprietaire proprietaire,
                               DateEvenement dateDebut, FrequenceJours frequence) {
        super(id, titre, proprietaire, dateDebut);
        this.frequence = frequence;
    }

    public FrequenceJours frequence() {
        return frequence;
    }

    @Override
    public DateEvenement dateFin() {
        return dateDebut();
    }

    @Override
    public String description() {
        return "Événement périodique : " + titre()
                + " à partir du " + dateDebut()
                + " — " + frequence;
    }

    @Override
    public boolean estDansPeriode(Periode periode) {
        return !occurrencesDans(periode).isEmpty();
    }

    @Override
    public List<DateEvenement> occurrencesDans(Periode periode) {
        List<DateEvenement> occurrences = new ArrayList<>();
        DateEvenement occurrence = dateDebut();

        while (!occurrence.estApres(periode.fin())) {
            if (periode.contient(occurrence)) {
                occurrences.add(occurrence);
            }
            occurrence = occurrence.plusJours(frequence);
        }

        return List.copyOf(occurrences);
    }
}