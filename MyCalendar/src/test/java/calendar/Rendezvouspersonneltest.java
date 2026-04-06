package calendar;

import calendar.domain.event.RendezVousPersonnel;
import calendar.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RendezVousPersonnel")
class RendezVousPersonnelTest {

    private RendezVousPersonnel unRdv() {
        return new RendezVousPersonnel(
                EventId.generer(),
                TitreEvenement.de("Dentiste"),
                Proprietaire.de("Alice"),
                DateEvenement.de(2025, 6, 15, 10, 0),
                DureeEvenement.deMinutes(30)
        );
    }

    @Nested @DisplayName("Création & getters")
    class Creation {
        @Test void titre() { assertThat(unRdv().titre()).isEqualTo(TitreEvenement.de("Dentiste")); }
        @Test void proprietaire() { assertThat(unRdv().proprietaire()).isEqualTo(Proprietaire.de("Alice")); }
        @Test void dateDebut() { assertThat(unRdv().dateDebut()).isEqualTo(DateEvenement.de(2025, 6, 15, 10, 0)); }
        @Test void duree() { assertThat(unRdv().duree()).isEqualTo(DureeEvenement.deMinutes(30)); }
        @Test void idUnique() { assertThat(unRdv().id()).isNotEqualTo(unRdv().id()); }
        @Test void dateFin() { assertThat(unRdv().dateFin()).isEqualTo(DateEvenement.de(2025, 6, 15, 10, 30)); }
        @Test void dureeNonNulle() { assertThat(unRdv().dureeEstNulle()).isFalse(); }
    }

    @Nested @DisplayName("Description")
    class Description {
        @Test void prefixeRdv() { assertThat(unRdv().description()).startsWith("RDV :"); }
        @Test void contientTitre() { assertThat(unRdv().description()).contains("Dentiste"); }
        @Test void contientDate() { assertThat(unRdv().description()).contains("2025"); }
    }

    @Nested @DisplayName("Période")
    class Appartenance {
        private final Periode juin = Periode.entre(
                DateEvenement.de(2025, 6, 1, 0, 0), DateEvenement.de(2025, 6, 30, 23, 59));
        private final Periode juillet = Periode.entre(
                DateEvenement.de(2025, 7, 1, 0, 0), DateEvenement.de(2025, 7, 31, 23, 59));

        @Test void estDansPeriode() { assertThat(unRdv().estDansPeriode(juin)).isTrue(); }
        @Test void nestPasDansPeriode() { assertThat(unRdv().estDansPeriode(juillet)).isFalse(); }
        @Test void uneOccurrence() { assertThat(unRdv().occurrencesDans(juin)).hasSize(1); }
        @Test void aucuneOccurrence() { assertThat(unRdv().occurrencesDans(juillet)).isEmpty(); }
    }
}