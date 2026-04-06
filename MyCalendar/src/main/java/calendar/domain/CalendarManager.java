package calendar.domain;

import calendar.domain.event.Evenement;
import calendar.domain.valueobject.Conflit;
import calendar.domain.valueobject.EventId;
import calendar.domain.valueobject.Periode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CalendarManager {

    private final List<Evenement> evenements;

    public CalendarManager() {
        this.evenements = new ArrayList<>();
    }

    public void ajouterEvenement(Evenement evenement) {
        Objects.requireNonNull(evenement, "L'événement ne peut pas être nul");
        evenements.add(evenement);
    }

    public List<Evenement> evenementsDansPeriode(Periode periode) {
        Objects.requireNonNull(periode, "La période ne peut pas être nulle");
        return evenements.stream()
                .filter(e -> e.estDansPeriode(periode))
                .collect(Collectors.toUnmodifiableList());
    }

    public void supprimerParId(EventId id) {
        Objects.requireNonNull(id, "L'identifiant ne peut pas être nul");
        evenements.removeIf(e -> e.id().equals(id));
    }

    public Optional<Evenement> rechercherParId(EventId id) {
        return evenements.stream()
                .filter(e -> e.id().equals(id))
                .findFirst();
    }

    public List<Conflit> detecterConflits() {
        List<Evenement> ponctuels = evenements.stream()
                .filter(e -> !e.dureeEstNulle())
                .collect(Collectors.toList());

        return IntStream.range(0, ponctuels.size())
                .boxed()
                .flatMap(i -> IntStream.range(i + 1, ponctuels.size())
                        .filter(j -> seChevauchent(ponctuels.get(i), ponctuels.get(j)))
                        .mapToObj(j -> Conflit.entre(ponctuels.get(i), ponctuels.get(j))))
                .collect(Collectors.toUnmodifiableList());
    }

    private boolean seChevauchent(Evenement e1, Evenement e2) {
        return e1.dateDebut().estAvant(e2.dateFin())
                && e1.dateFin().estApres(e2.dateDebut());
    }

    public List<Evenement> tousLesEvenements() {
        return Collections.unmodifiableList(evenements);
    }
}