package calendar;

import calendar.domain.event.Reunion;
import calendar.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Reunion")
class ReunionTest {

    private Reunion uneReunion() {
        return new Reunion(
                EventId.generer(),
                TitreEvenement.de("Sprint planning"),
                Proprietaire.de("Bob"),
                DateEvenement.de(2025, 6, 16, 9, 0),
                DureeEvenement.deMinutes(90),
                LieuEvenement.de("Salle Agora"),
                Participants.de("Bob", "Alice", "Charlie")
        );
    }

    @Nested @DisplayName("Création & getters")
    class Creation {
        @Test void titre() { assertThat(uneReunion().titre()).isEqualTo(TitreEvenement.de("Sprint planning")); }
        @Test void lieu() { assertThat(uneReunion().lieu()).isEqualTo(LieuEvenement.de("Salle Agora")); }
        @Test void participants() { assertThat(uneReunion().participants()).isEqualTo(Participants.de("Bob", "Alice", "Charlie")); }
        @Test void duree() { assertThat(uneReunion().duree()).isEqualTo(DureeEvenement.deMinutes(90)); }
        @Test void dateFin() { assertThat(uneReunion().dateFin()).isEqualTo(DateEvenement.de(2025, 6, 16, 10, 30)); }
        @Test void dureeNonNulle() { assertThat(uneReunion().dureeEstNulle()).isFalse(); }
    }

    @Nested @DisplayName("Description")
    class Description {
        @Test void prefixe() { assertThat(uneReunion().description()).startsWith("Réunion :"); }
        @Test void contientTitre() { assertThat(uneReunion().description()).contains("Sprint planning"); }
        @Test void contientLieu() { assertThat(uneReunion().description()).contains("Salle Agora"); }
        @Test void contientParticipants() {
            assertThat(uneReunion().description()).contains("Bob").contains("Alice").contains("Charlie");
        }
    }

    @Nested @DisplayName("Période")
    class Appartenance {
        private final Periode juin = Periode.entre(
                DateEvenement.de(2025, 6, 1, 0, 0), DateEvenement.de(2025, 6, 30, 23, 59));
        private final Periode juillet = Periode.entre(
                DateEvenement.de(2025, 7, 1, 0, 0), DateEvenement.de(2025, 7, 31, 23, 59));

        @Test void estDansPeriode() { assertThat(uneReunion().estDansPeriode(juin)).isTrue(); }
        @Test void nestPasDansPeriode() { assertThat(uneReunion().estDansPeriode(juillet)).isFalse(); }
        @Test void uneOccurrence() { assertThat(uneReunion().occurrencesDans(juin)).hasSize(1); }
        @Test void aucuneOccurrence() { assertThat(uneReunion().occurrencesDans(juillet)).isEmpty(); }
    }
}