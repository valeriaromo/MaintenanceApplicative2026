package calendar;

import calendar.domain.CalendarManager;
import calendar.domain.event.*;
import calendar.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CalendarManager")
class CalendarManagerTest {

    private CalendarManager calendrier;

    @BeforeEach
    void setUp() { calendrier = new CalendarManager(); }

    private RendezVousPersonnel rdv(String titre, int jour, int heure, int duree) {
        return new RendezVousPersonnel(
                EventId.generer(), TitreEvenement.de(titre), Proprietaire.de("Alice"),
                DateEvenement.de(2025, 6, jour, heure, 0), DureeEvenement.deMinutes(duree)
        );
    }

    private Periode juin() {
        return Periode.entre(DateEvenement.de(2025, 6, 1, 0, 0), DateEvenement.de(2025, 6, 30, 23, 59));
    }

    @Nested @DisplayName("Ajout")
    class Ajout {
        @Test void ajouteEvenement() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            assertThat(calendrier.tousLesEvenements()).hasSize(1);
        }
        @Test void refuseNull() {
            assertThatThrownBy(() -> calendrier.ajouterEvenement(null))
                    .isInstanceOf(NullPointerException.class);
        }
        @Test void ajoutePlusieursTypes() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(new Reunion(
                    EventId.generer(), TitreEvenement.de("R"), Proprietaire.de("Bob"),
                    DateEvenement.de(2025, 6, 12, 10, 0), DureeEvenement.deMinutes(90),
                    LieuEvenement.de("Salle B"), Participants.de("Bob", "Alice")));
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(), TitreEvenement.de("P"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 2, 9, 0), FrequenceJours.de(1)));
            assertThat(calendrier.tousLesEvenements()).hasSize(3);
        }
    }

    @Nested @DisplayName("Période")
    class PeriodeTests {
        @Test void retourneEvenementsDansPeriode() {
            calendrier.ajouterEvenement(rdv("Juin", 5, 10, 30));
            assertThat(calendrier.evenementsDansPeriode(juin())).hasSize(1);
        }
        @Test void exclutEvenementHorsPeriode() {
            calendrier.ajouterEvenement(rdv("Juin", 5, 10, 30));
            var juillet = Periode.entre(DateEvenement.de(2025, 7, 1, 0, 0), DateEvenement.de(2025, 7, 31, 23, 59));
            assertThat(calendrier.evenementsDansPeriode(juillet)).isEmpty();
        }
        @Test void inclusPeriodiquesAvecOccurrence() {
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(), TitreEvenement.de("Hebdo"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 2, 9, 0), FrequenceJours.de(7)));
            assertThat(calendrier.evenementsDansPeriode(juin())).hasSize(1);
        }
        @Test void excluPeriodiqueSansOccurrence() {
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(), TitreEvenement.de("Noel"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 12, 25, 0, 0), FrequenceJours.de(365)));
            assertThat(calendrier.evenementsDansPeriode(juin())).isEmpty();
        }
        @Test void refusePeriodeNull() {
            assertThatThrownBy(() -> calendrier.evenementsDansPeriode(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested @DisplayName("Suppression & recherche")
    class SuppressionRecherche {
        @Test void supprimerParId() {
            var id = EventId.generer();
            calendrier.ajouterEvenement(new RendezVousPersonnel(id,
                    TitreEvenement.de("X"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 10, 10, 0), DureeEvenement.deMinutes(30)));
            calendrier.supprimerParId(id);
            assertThat(calendrier.tousLesEvenements()).isEmpty();
        }
        @Test void supprimerIdInconnuNeFaitRien() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.supprimerParId(EventId.generer());
            assertThat(calendrier.tousLesEvenements()).hasSize(1);
        }
        @Test void refuseSupprimerNull() {
            assertThatThrownBy(() -> calendrier.supprimerParId(null))
                    .isInstanceOf(NullPointerException.class);
        }
        @Test void rechercherParIdTrouve() {
            var id = EventId.generer();
            var e = new RendezVousPersonnel(id, TitreEvenement.de("X"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 10, 10, 0), DureeEvenement.deMinutes(30));
            calendrier.ajouterEvenement(e);
            assertThat(calendrier.rechercherParId(id)).contains(e);
        }
        @Test void rechercherParIdIntrouvable() {
            assertThat(calendrier.rechercherParId(EventId.generer())).isEmpty();
        }
    }

    @Nested @DisplayName("Détection de conflits")
    class Conflits {
        @Test void conflitSimultane() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("B", 10, 10, 60));
            assertThat(calendrier.detecterConflits()).hasSize(1);
        }
        @Test void conflitPartiel() {
            // A 10h–11h, B 10h30–11h30
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(new RendezVousPersonnel(
                    EventId.generer(), TitreEvenement.de("B"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 10, 10, 30), DureeEvenement.deMinutes(60)));
            assertThat(calendrier.detecterConflits()).hasSize(1);
        }
        @Test void pasDeConflitConsecutifs() {
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("B", 10, 11, 60));
            assertThat(calendrier.detecterConflits()).isEmpty();
        }
        @Test void pasDeConflitSepares() {
            calendrier.ajouterEvenement(rdv("A", 10, 9, 60));
            calendrier.ajouterEvenement(rdv("B", 10, 14, 60));
            assertThat(calendrier.detecterConflits()).isEmpty();
        }
        @Test void descriptionConflitContientTitres() {
            calendrier.ajouterEvenement(rdv("Médecin", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("Dentiste", 10, 10, 60));
            assertThat(calendrier.detecterConflits().get(0).toString())
                    .contains("Médecin").contains("Dentiste");
        }
        @Test void periodiquesExclusDuConflitCarDureeNulle() {
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(), TitreEvenement.de("P1"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 10, 10, 0), FrequenceJours.de(7)));
            calendrier.ajouterEvenement(new EvenementPeriodique(
                    EventId.generer(), TitreEvenement.de("P2"), Proprietaire.de("Alice"),
                    DateEvenement.de(2025, 6, 10, 10, 0), FrequenceJours.de(7)));
            assertThat(calendrier.detecterConflits()).isEmpty();
        }
        @Test void troisConflitsPossibles() {
            // 3 événements simultanés → 3 paires en conflit
            calendrier.ajouterEvenement(rdv("A", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("B", 10, 10, 60));
            calendrier.ajouterEvenement(rdv("C", 10, 10, 60));
            assertThat(calendrier.detecterConflits()).hasSize(3);
        }
    }

    @Nested @DisplayName("Polymorphisme")
    class Polymorphisme {
        @Test void chaqueTypeGenereSaDescription() {
            var rdv = rdv("Médecin", 10, 14, 60);
            var reunion = new Reunion(EventId.generer(), TitreEvenement.de("Lancement"),
                    Proprietaire.de("Bob"), DateEvenement.de(2025, 6, 5, 9, 0),
                    DureeEvenement.deMinutes(120), LieuEvenement.de("Salle A"),
                    Participants.de("Bob", "Alice"));
            var periodique = new EvenementPeriodique(EventId.generer(), TitreEvenement.de("Hebdo"),
                    Proprietaire.de("Alice"), DateEvenement.de(2025, 6, 2, 9, 0), FrequenceJours.de(7));

            assertThat(rdv.description()).startsWith("RDV :");
            assertThat(reunion.description()).startsWith("Réunion :");
            assertThat(periodique.description()).startsWith("Événement périodique");
        }
    }
}