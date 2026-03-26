package calendar.domain.event;

import calendar.domain.valueobject.*;

import java.util.List;


public abstract class Evenement {

    private final EventId id;
    private final TitreEvenement titre;
    private final Proprietaire proprietaire;
    private final DateEvenement dateDebut;

    protected Evenement(EventId id, TitreEvenement titre, Proprietaire proprietaire, DateEvenement dateDebut) {
        this.id = id;
        this.titre = titre;
        this.proprietaire = proprietaire;
        this.dateDebut = dateDebut;
    }

    public EventId id() { return id; }
    public TitreEvenement titre() { return titre; }
    public Proprietaire proprietaire() { return proprietaire; }
    public DateEvenement dateDebut() { return dateDebut; }

  
    public abstract String description();

 
    public abstract boolean estDansPeriode(Periode periode);

 
    public abstract DateEvenement dateFin();

  
    public boolean dureeEstNulle() {
        return dateFin().equals(dateDebut());
    }

  
    public abstract List<DateEvenement> occurrencesDans(Periode periode);
}