package calendar;

import calendar.domain.event.Reunion;
import calendar.domain.valueobject.DateEvenement;
import calendar.domain.valueobject.DureeEvenement;
import calendar.domain.valueobject.EventId;
import calendar.domain.valueobject.LieuEvenement;
import calendar.domain.valueobject.Participants;
import calendar.domain.valueobject.Periode;
import calendar.domain.valueobject.Proprietaire;
import calendar.domain.valueobject.TitreEvenement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Nested
    @DisplayName("Création")
    class Creation {

        @Test
        @DisplayName("crée une réunion avec toutes ses propriétés")
        void creerReunion() {
            var r = uneReunion();
            assertThat(r.titre()).isEqualTo(TitreEvenement.de("Sprint planning"));
            assertThat(r.lieu()).isEqualTo(LieuEvenement.de("Salle Agora"));
            assertThat(r.participants()).isEqualTo(Participants.de("Bob", "Alice", "Charlie"));
            assertThat(r.duree()).isEqualTo(DureeEvenement.deMinutes(90));
        }

        @Test
        @DisplayName("calcule correctement la date de fin")
        void calculeDateFin() {
            assertThat(uneReunion().dateFin())
                    .isEqualTo(DateEvenement.de(2025, 6, 16, 10, 30));
        }

        @Test
        @DisplayName("n'a pas une durée nulle")
        void dureeNonNulle() {
            assertThat(uneReunion().dureeEstNulle()).isFalse();
        }
    }

    @Nested
    @DisplayName("Description")
    class Description {

        @Test
        @DisplayName("commence par Réunion :")
        void descriptionPrefixe() {
            assertThat(uneReunion().description()).startsWith("Réunion :");
        }

        @Test
        @DisplayName("contient le titre")
        void descriptionContientTitre() {
            assertThat(uneReunion().description()).contains("Sprint planning");
        }

        @Test
        @DisplayName("contient le lieu")
        void descriptionContientLieu() {
            assertThat(uneReunion().description()).contains("Salle Agora");
        }

        @Test
        @DisplayName("contient tous les participants")
        void descriptionContientParticipants() {
            assertThat(uneReunion().description())
                    .contains("Bob")
                    .contains("Alice")
                    .contains("Charlie");
        }
    }

    @Nested
    @DisplayName("Validation des Value Objects")
    class ValidationValueObjects {

        @Test
        @DisplayName("LieuEvenement refuse un lieu vide")
        void lieuVideRefuse() {
            assertThatThrownBy(() -> LieuEvenement.de(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Participants refuse une liste vide")
        void participantsVidesRefuses() {
            assertThatThrownBy(() -> Participants.de(List.of()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Participants retourne bien la liste des noms")
        void participantsListeNoms() {
            var p = Participants.de("Alice", "Bob");
            assertThat(p.liste()).containsExactly("Alice", "Bob");
        }

        @Test
        @DisplayName("deux Participants identiques sont égaux")
        void egaliteParticipants() {
            assertThat(Participants.de("Alice", "Bob"))
                    .isEqualTo(Participants.de("Alice", "Bob"));
        }
    }
}