package calendar;

import calendar.domain.event.EvenementPeriodique;
import calendar.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EvenementPeriodique")
class EvenementPeriodiqueTest {

    private EvenementPeriodique hebdomadaire() {
        return new EvenementPeriodique(
                EventId.generer(),
                TitreEvenement.de("Stand-up"),
                Proprietaire.de("Alice"),
                DateEvenement.de(2025, 1, 1, 9, 0),
                FrequenceJours.de(7)
        );
    }

    @Nested @DisplayName("Création & getters")
    class Creation {
        @Test void titre() { assertThat(hebdomadaire().titre()).isEqualTo(TitreEvenement.de("Stand-up")); }
        @Test void frequence() { assertThat(hebdomadaire().frequence()).isEqualTo(FrequenceJours.de(7)); }
        @Test void dateFinEgaleDebut() { var ep = hebdomadaire(); assertThat(ep.dateFin()).isEqualTo(ep.dateDebut()); }
        @Test void dureeNulle() { assertThat(hebdomadaire().dureeEstNulle()).isTrue(); }
    }

    @Nested @DisplayName("Occurrences")
    class Occurrences {
        private final Periode janvier = Periode.entre(
                DateEvenement.de(2025, 1, 1, 0, 0), DateEvenement.de(2025, 1, 31, 23, 59));
        private final Periode avant = Periode.entre(
                DateEvenement.de(2024, 12, 1, 0, 0), DateEvenement.de(2024, 12, 31, 23, 59));

        @Test void cinqOccurrencesEnJanvier() { assertThat(hebdomadaire().occurrencesDans(janvier)).hasSize(5); }
        @Test void aucuneOccurrenceAvant() { assertThat(hebdomadaire().occurrencesDans(avant)).isEmpty(); }

        @Test void premiereOccurrence() {
            assertThat(hebdomadaire().occurrencesDans(janvier).get(0))
                    .isEqualTo(DateEvenement.de(2025, 1, 1, 9, 0));
        }
        @Test void deuxiemeOccurrence() {
            assertThat(hebdomadaire().occurrencesDans(janvier).get(1))
                    .isEqualTo(DateEvenement.de(2025, 1, 8, 9, 0));
        }

        @Test void estDansPeriodeAvecOccurrence() {
            var periode = Periode.entre(
                    DateEvenement.de(2025, 1, 7, 0, 0), DateEvenement.de(2025, 1, 14, 23, 59));
            assertThat(hebdomadaire().estDansPeriode(periode)).isTrue();
        }

        @Test void nestPasDansPeriodeSansOccurrence() {
            var periode = Periode.entre(
                    DateEvenement.de(2025, 1, 2, 0, 0), DateEvenement.de(2025, 1, 7, 23, 59));
            assertThat(hebdomadaire().estDansPeriode(periode)).isFalse();
        }
    }

    @Nested @DisplayName("Description")
    class Description {
        @Test void prefixe() { assertThat(hebdomadaire().description()).startsWith("Événement périodique"); }
        @Test void contientTitre() { assertThat(hebdomadaire().description()).contains("Stand-up"); }
        @Test void contientFrequence() { assertThat(hebdomadaire().description()).contains("7"); }
    }
}