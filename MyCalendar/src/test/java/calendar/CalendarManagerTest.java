package calendar;

import calendar.domain.event.EvenementPeriodique;
import calendar.domain.valueobject.DateEvenement;
import calendar.domain.valueobject.EventId;
import calendar.domain.valueobject.FrequenceJours;
import calendar.domain.valueobject.Periode;
import calendar.domain.valueobject.Proprietaire;
import calendar.domain.valueobject.TitreEvenement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Nested
    @DisplayName("Création")
    class Creation {

        @Test
        @DisplayName("crée un événement périodique avec ses propriétés")
        void creerEvenementPeriodique() {
            var ep = hebdomadaire();
            assertThat(ep.titre()).isEqualTo(TitreEvenement.de("Stand-up"));
            assertThat(ep.frequence()).isEqualTo(FrequenceJours.de(7));
        }

        @Test
        @DisplayName("a une durée nulle (pas de durée fixe)")
        void dureeNulle() {
            assertThat(hebdomadaire().dureeEstNulle()).isTrue();
        }

        @Test
        @DisplayName("FrequenceJours refuse zéro")
        void frequenceZeroRefusee() {
            assertThatThrownBy(() -> FrequenceJours.de(0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("FrequenceJours refuse une valeur négative")
        void frequenceNegativeRefusee() {
            assertThatThrownBy(() -> FrequenceJours.de(-3))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Occurrences dans une période")
    class Occurrences {

        @Test
        @DisplayName("trouve 5 occurrences en janvier 2025 (hebdo depuis le 1er)")
        void cinqOccurrencesEnJanvier() {
            // 1, 8, 15, 22, 29 janvier → 5 occurrences
            var janvier = Periode.entre(
                    DateEvenement.de(2025, 1, 1, 0, 0),
                    DateEvenement.de(2025, 1, 31, 23, 59)
            );
            assertThat(hebdomadaire().occurrencesDans(janvier)).hasSize(5);
        }

        @Test
        @DisplayName("trouve 0 occurrence si la période est entièrement avant le début")
        void aucuneOccurrenceAvantDebut() {
            var avant = Periode.entre(
                    DateEvenement.de(2024, 12, 1, 0, 0),
                    DateEvenement.de(2024, 12, 31, 23, 59)
            );
            assertThat(hebdomadaire().occurrencesDans(avant)).isEmpty();
        }

        @Test
        @DisplayName("est dans la période si au moins une occurrence y tombe")
        void estDansPeriodeSiOccurrencePresente() {
            var periode = Periode.entre(
                    DateEvenement.de(2025, 1, 7, 0, 0),
                    DateEvenement.de(2025, 1, 14, 23, 59)
            );
            assertThat(hebdomadaire().estDansPeriode(periode)).isTrue();
        }

        @Test
        @DisplayName("n'est pas dans une période sans occurrence")
        void nestPasDansPeriodeSansOccurrence() {
            // Entre le 2 et le 7 janvier à 8h59 → le 8 à 9h n'est pas inclus
            var periode = Periode.entre(
                    DateEvenement.de(2025, 1, 2, 0, 0),
                    DateEvenement.de(2025, 1, 7, 23, 59)
            );
            assertThat(hebdomadaire().estDansPeriode(periode)).isFalse();
        }

        @Test
        @DisplayName("les occurrences retournées sont dans l'ordre chronologique")
        void occurrencesOrdreChronologique() {
            var janvier = Periode.entre(
                    DateEvenement.de(2025, 1, 1, 0, 0),
                    DateEvenement.de(2025, 1, 31, 23, 59)
            );
            var occurrences = hebdomadaire().occurrencesDans(janvier);
            assertThat(occurrences.get(0)).isEqualTo(DateEvenement.de(2025, 1, 1, 9, 0));
            assertThat(occurrences.get(1)).isEqualTo(DateEvenement.de(2025, 1, 8, 9, 0));
        }
    }

    @Nested
    @DisplayName("Description")
    class Description {

        @Test
        @DisplayName("commence par Événement périodique")
        void descriptionPrefixe() {
            assertThat(hebdomadaire().description()).startsWith("Événement périodique");
        }

        @Test
        @DisplayName("contient le titre")
        void descriptionContientTitre() {
            assertThat(hebdomadaire().description()).contains("Stand-up");
        }

        @Test
        @DisplayName("contient la fréquence en jours")
        void descriptionContientFrequence() {
            assertThat(hebdomadaire().description()).contains("7");
        }
    }
}