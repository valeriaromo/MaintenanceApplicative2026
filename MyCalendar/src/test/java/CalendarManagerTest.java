package calendar;

import calendar.domain.CalendarManager;
import calendar.domain.event.*;
import calendar.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CalendarManager")
class CalendarManagerTest {

    private CalendarManager calendrier;

    @BeforeEach
    void setUp() {
        calendrier = new CalendarManager();
    }


    private RendezVousPersonnel rdv(String titre, int jour, int heureDebut, int dureeMin) {
        return new RendezVousPersonnel(
                EventId.generer(),
                TitreEvenement.de(titre),
                Proprietaire.de("Alice"),
                DateEvenement.de(2025, 6, jour, heureDebut, 0),
                DureeEvenement.deMinutes(dureeMin)
        );
    }

    private Periode juin2025() {
        return Periode.entre(
                DateEvenement.de(2025, 6, 1, 0, 0),
                DateEvenement.de(2025, 6, 30, 23, 59)
        );
    }


    @Nested
    @DisplayName("Ajout d'événements")
    class AjoutEvenements {

        @Test
        @DisplayName("ajoute un événement au calendrier")
        void ajouterUnEvenement() {
            calendrier.ajouterEvenement(rdv("Médecin", 10, 14, 60));
            assertThat(calendrier.tousLesEvenements()).hasSize(1);
        }

        @Test
        @DisplayName("refuse l'ajout d'un événement nul")
        void refuseEvenementNul() {
            assertThatThrownBy(() -> calendrier.ajouterEvenement(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("ajoute plusieurs types d'événements")
        void ajouterPlusieursTypes() {
            calendrier.ajouterEvenement(rdv("Médecin", 10, 14, 60));
            calendrier.ajouterEvenement(new Reunion(
                    EventId.generer(),
                    TitreEvenement.de("Réunion projet"),
                    Proprietaire.de("Bob"),
                    DateEvenement.de(2025, 6, 12, 10, 0),
                    DureeEvenement.deMinutes(90),
                    LieuEvenement.de("Salle B"),
                    Participants.de("Bob", "Alice")
            ));
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(),
                    TitreEvenement.de("Stand-up"),
                    Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 2, 9, 0),
                    FrequenceJours.de(1)
            ));

            assertThat(calendrier.tousLesEvenements()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Événements dans une période")
    class EvenementsDansPeriode {

        @Test
        @DisplayName("retourne les événements dans la période")
        void evenementsDansPeriode() {
            calendrier.ajouterEvenement(rdv("Dentiste", 5, 10, 30));
            calendrier.ajouterEvenement(rdv("Hors période", 1, 10, 30)); // juillet

            var juillet = Periode.entre(
                    DateEvenement.de(2025, 7, 1, 0, 0),
                    DateEvenement.de(2025, 7, 31, 23, 59)
            );

            assertThat(calendrier.evenementsDansPeriode(juin2025())).hasSize(1);
            assertThat(calendrier.evenementsDansPeriode(juillet)).isEmpty();
        }

        @Test
        @DisplayName("inclut les périodiques qui ont une occurrence dans la période")
        void inclusPeriodiquesAvecOccurrence() {
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(),
                    TitreEvenement.de("Hebdo"),
                    Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 2, 9, 0),
                    FrequenceJours.de(7)
            ));
            assertThat(calendrier.evenementsDansPeriode(juin2025())).hasSize(1);
        }

        @Test
        @DisplayName("exclut les périodiques sans occurrence dans la période")
        void excluPeriodiqueSansOccurrence() {
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(),
                    TitreEvenement.de("Annuel"),
                    Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 12, 25, 0, 0),
                    FrequenceJours.de(365)
            ));
            assertThat(calendrier.evenementsDansPeriode(juin2025())).isEmpty();
        }
    }

    @Nested
    @DisplayName("Suppression par identifiant")
    class Suppression {

        @Test
        @DisplayName("supprime un événement existant par son id")
        void supprimerEvenementExistant() {
            var id = EventId.generer();
            calendrier.ajouterEvenement(new RendezVousPersonnel(
                    id,
                    TitreEvenement.de("À supprimer"),
                    Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 10, 10, 0),
                    DureeEvenement.deMinutes(30)
            ));

            assertThat(calendrier.tousLesEvenements()).hasSize(1);
            calendrier.supprimerParId(id);
            assertThat(calendrier.tousLesEvenements()).isEmpty();
        }

        @Test
        @DisplayName("ne supprime rien si l'id est inconnu")
        void supprimerIdInconnu() {
            calendrier.ajouterEvenement(rdv("Médecin", 10, 14, 60));
            calendrier.supprimerParId(EventId.generer()); // id différent
            assertThat(calendrier.tousLesEvenements()).hasSize(1);
        }

        @Test
        @DisplayName("retrouve un événement par son id")
        void rechercherParId() {
            var id = EventId.generer();
            var event = new RendezVousPersonnel(
                    id,
                    TitreEvenement.de("Physio"),
                    Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 20, 15, 0),
                    DureeEvenement.deMinutes(45)
            );
            calendrier.ajouterEvenement(event);

            assertThat(calendrier.rechercherParId(id)).contains(event);
        }
    }

    @Nested
    @DisplayName("Détection de conflits")
    class DetectionConflits {

        @Test
        @DisplayName("détecte un conflit entre deux événements qui se chevauchent")
        void conflitDetecte() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("B", 10, 10, 60)); 

            assertThat(calendrier.detecterConflits()).hasSize(1);
        }

        @Test
        @DisplayName("détecte un conflit partiel")
        void conflitPartiel() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(new RendezVousPersonnel(
                    EventId.generer(),
                    TitreEvenement.de("B"),
                    Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 10, 10, 30),
                    DureeEvenement.deMinutes(60)
            ));

            assertThat(calendrier.detecterConflits()).hasSize(1);
        }

        @Test
        @DisplayName("ne signale pas de conflit pour des événements consécutifs")
        void pasDeConflitConsecutifs() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("B", 10, 11, 60));

            assertThat(calendrier.detecterConflits()).isEmpty();
        }

        @Test
        @DisplayName("ne signale pas de conflit pour des événements distincts")
        void pasDeConflitEventsSepares() {
            calendrier.ajouterEvenement(rdv("Matin", 10, 9, 60));
            calendrier.ajouterEvenement(rdv("Après-midi", 10, 14, 60));

            assertThat(calendrier.detecterConflits()).isEmpty();
        }

        @Test
        @DisplayName("retourne la description du conflit")
        void descriptionConflitContientTitres() {
            calendrier.ajouterEvenement(rdv("Médecin", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("Dentiste", 10, 10, 60));

            var conflits = calendrier.detecterConflits();
            assertThat(conflits.get(0).toString())
                    .contains("Médecin")
                    .contains("Dentiste");
        }
    }

    @Nested
    @DisplayName("Polymorphisme — description sans conditionnel")
    class Polymorphisme {

        @Test
        @DisplayName("chaque type génère sa description propre sans if/switch")
        void chaqueTypeGenereSaDescription() {
            var rdv = rdv("Médecin", 10, 14, 60);
            var reunion = new Reunion(
                    EventId.generer(),
                    TitreEvenement.de("Lancement"),
                    Proprietaire.de("Bob"),
                    DateEvenement.de(2025, 6, 5, 9, 0),
                    DureeEvenement.deMinutes(120),
                    LieuEvenement.de("Salle A"),
                    Participants.de("Bob", "Alice")
            );
            var periodique = new EvenementPeriodique(
                    EventId.generer(),
                    TitreEvenement.de("Hebdo"),
                    Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 2, 9, 0),
                    FrequenceJours.de(7)
            );

            assertThat(rdv.description()).startsWith("RDV :");
            assertThat(reunion.description()).startsWith("Réunion :");
            assertThat(periodique.description()).startsWith("Événement périodique");
        }
    }
}