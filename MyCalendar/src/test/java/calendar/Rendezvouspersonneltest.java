package calendar;

import calendar.domain.event.RendezVousPersonnel;
import calendar.domain.valueobject.DateEvenement;
import calendar.domain.valueobject.DureeEvenement;
import calendar.domain.valueobject.EventId;
import calendar.domain.valueobject.Periode;
import calendar.domain.valueobject.Proprietaire;
import calendar.domain.valueobject.TitreEvenement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Nested
    @DisplayName("Création")
    class Creation {

        @Test
        @DisplayName("crée un RDV avec ses propriétés")
        void creerRdv() {
            var rdv = unRdv();
            assertThat(rdv.titre()).isEqualTo(TitreEvenement.de("Dentiste"));
            assertThat(rdv.proprietaire()).isEqualTo(Proprietaire.de("Alice"));
            assertThat(rdv.dateDebut()).isEqualTo(DateEvenement.de(2025, 6, 15, 10, 0));
            assertThat(rdv.duree()).isEqualTo(DureeEvenement.deMinutes(30));
        }

        @Test
        @DisplayName("génère un identifiant unique à chaque fois")
        void genereIdUnique() {
            assertThat(unRdv().id()).isNotEqualTo(unRdv().id());
        }

        @Test
        @DisplayName("calcule correctement la date de fin")
        void calculeDateFin() {
            assertThat(unRdv().dateFin())
                    .isEqualTo(DateEvenement.de(2025, 6, 15, 10, 30));
        }

        @Test
        @DisplayName("n'a pas une durée nulle")
        void dureeNonNulle() {
            assertThat(unRdv().dureeEstNulle()).isFalse();
        }
    }

    @Nested
    @DisplayName("Description")
    class Description {

        @Test
        @DisplayName("commence par RDV :")
        void descriptionPrefixe() {
            assertThat(unRdv().description()).startsWith("RDV :");
        }

        @Test
        @DisplayName("contient le titre")
        void descriptionContientTitre() {
            assertThat(unRdv().description()).contains("Dentiste");
        }
    }

    @Nested
    @DisplayName("Appartenance à une période")
    class Appartenance {

        @Test
        @DisplayName("est dans une période qui le contient")
        void estDansPeriode() {
            var periode = Periode.entre(
                    DateEvenement.de(2025, 6, 1, 0, 0),
                    DateEvenement.de(2025, 6, 30, 23, 59)
            );
            assertThat(unRdv().estDansPeriode(periode)).isTrue();
        }

        @Test
        @DisplayName("n'est pas dans une période qui ne le contient pas")
        void nestPasDansPeriode() {
            var periode = Periode.entre(
                    DateEvenement.de(2025, 7, 1, 0, 0),
                    DateEvenement.de(2025, 7, 31, 23, 59)
            );
            assertThat(unRdv().estDansPeriode(periode)).isFalse();
        }

        @Test
        @DisplayName("retourne une seule occurrence dans la période")
        void uneSeuleOccurrence() {
            var periode = Periode.entre(
                    DateEvenement.de(2025, 6, 1, 0, 0),
                    DateEvenement.de(2025, 6, 30, 23, 59)
            );
            assertThat(unRdv().occurrencesDans(periode)).hasSize(1);
        }

        @Test
        @DisplayName("retourne zéro occurrence hors période")
        void aucuneOccurrenceHorsPeriode() {
            var periode = Periode.entre(
                    DateEvenement.de(2025, 7, 1, 0, 0),
                    DateEvenement.de(2025, 7, 31, 23, 59)
            );
            assertThat(unRdv().occurrencesDans(periode)).isEmpty();
        }
    }

    @Nested
    @DisplayName("Validation des Value Objects")
    class ValidationValueObjects {

        @Test
        @DisplayName("TitreEvenement refuse un titre vide")
        void titreVideRefuse() {
            assertThatThrownBy(() -> TitreEvenement.de(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("TitreEvenement refuse un titre nul")
        void titreNulRefuse() {
            assertThatThrownBy(() -> TitreEvenement.de(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("DureeEvenement refuse une durée négative")
        void dureeNegativeRefusee() {
            assertThatThrownBy(() -> DureeEvenement.deMinutes(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("deux TitreEvenement identiques sont égaux")
        void egaliteTitre() {
            assertThat(TitreEvenement.de("Médecin"))
                    .isEqualTo(TitreEvenement.de("Médecin"));
        }

        @Test
        @DisplayName("EventId.de() crée un id reproductible")
        void eventIdDe() {
            assertThat(EventId.de("abc")).isEqualTo(EventId.de("abc"));
        }
    }
}