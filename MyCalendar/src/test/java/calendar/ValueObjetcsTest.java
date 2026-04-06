package calendar;

import calendar.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Couvre 100% des branches de tous les Value Objects :
 * EventId, TitreEvenement, DureeEvenement, FrequenceJours,
 * LieuEvenement, Proprietaire, Participants, DateEvenement, Periode.
 */
@DisplayName("Value Objects")
class ValueObjectsTest {

    // ══════════════════════════════════════════════════════════════
    // EventId
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("EventId")
    class EventIdTest {

        @Test void genererProduitsUnId() {
            assertThat(EventId.generer().valeur()).isNotBlank();
        }

        @Test void deuxGenererSontDifferents() {
            assertThat(EventId.generer()).isNotEqualTo(EventId.generer());
        }

        @Test void deCreerAvecValeur() {
            assertThat(EventId.de("abc").valeur()).isEqualTo("abc");
        }

        @Test void refuseValeurNulle() {
            assertThatThrownBy(() -> EventId.de(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void refuseValeurVide() {
            assertThatThrownBy(() -> EventId.de(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void refuseValeurBlanc() {
            assertThatThrownBy(() -> EventId.de("   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void egaliteMemeReference() {
            var id = EventId.de("x");
            assertThat(id).isEqualTo(id);
        }

        @Test void egaliteMemValeur() {
            assertThat(EventId.de("abc")).isEqualTo(EventId.de("abc"));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(EventId.de("abc")).isNotEqualTo(EventId.de("xyz"));
        }

        @Test void inegaliteAutreType() {
            assertThat(EventId.de("abc")).isNotEqualTo("abc");
        }

        @Test void hashCodeCoherent() {
            assertThat(EventId.de("abc").hashCode()).isEqualTo(EventId.de("abc").hashCode());
        }

        @Test void toStringRetourneValeur() {
            assertThat(EventId.de("abc").toString()).isEqualTo("abc");
        }
    }

    // ══════════════════════════════════════════════════════════════
    // TitreEvenement
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("TitreEvenement")
    class TitreEvenementTest {

        @Test void creerTitre() {
            assertThat(TitreEvenement.de("Médecin").valeur()).isEqualTo("Médecin");
        }

        @Test void trimmeLesEspaces() {
            assertThat(TitreEvenement.de("  Médecin  ").valeur()).isEqualTo("Médecin");
        }

        @Test void refuseNul() {
            assertThatThrownBy(() -> TitreEvenement.de(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void refuseVide() {
            assertThatThrownBy(() -> TitreEvenement.de(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void refuseBlanc() {
            assertThatThrownBy(() -> TitreEvenement.de("   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void egaliteMemeReference() {
            var t = TitreEvenement.de("A");
            assertThat(t).isEqualTo(t);
        }

        @Test void egaliteMemValeur() {
            assertThat(TitreEvenement.de("A")).isEqualTo(TitreEvenement.de("A"));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(TitreEvenement.de("A")).isNotEqualTo(TitreEvenement.de("B"));
        }

        @Test void inegaliteAutreType() {
            assertThat(TitreEvenement.de("A")).isNotEqualTo("A");
        }

        @Test void hashCodeCoherent() {
            assertThat(TitreEvenement.de("A").hashCode()).isEqualTo(TitreEvenement.de("A").hashCode());
        }

        @Test void toStringRetourneValeur() {
            assertThat(TitreEvenement.de("Médecin").toString()).isEqualTo("Médecin");
        }
    }

    // ══════════════════════════════════════════════════════════════
    // DureeEvenement
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("DureeEvenement")
    class DureeEvenementTest {

        @Test void creerDuree() {
            assertThat(DureeEvenement.deMinutes(60).enMinutes()).isEqualTo(60);
        }

        @Test void accepteZero() {
            assertThat(DureeEvenement.deMinutes(0).enMinutes()).isEqualTo(0);
        }

        @Test void refuseNegative() {
            assertThatThrownBy(() -> DureeEvenement.deMinutes(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void egaliteMemeReference() {
            var d = DureeEvenement.deMinutes(30);
            assertThat(d).isEqualTo(d);
        }

        @Test void egaliteMemValeur() {
            assertThat(DureeEvenement.deMinutes(30)).isEqualTo(DureeEvenement.deMinutes(30));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(DureeEvenement.deMinutes(30)).isNotEqualTo(DureeEvenement.deMinutes(60));
        }

        @Test void inegaliteAutreType() {
            assertThat(DureeEvenement.deMinutes(30)).isNotEqualTo(30);
        }

        @Test void hashCodeCoherent() {
            assertThat(DureeEvenement.deMinutes(30).hashCode())
                    .isEqualTo(DureeEvenement.deMinutes(30).hashCode());
        }

        @Test void toStringContientMinutes() {
            assertThat(DureeEvenement.deMinutes(45).toString()).contains("45");
        }
    }

    // ══════════════════════════════════════════════════════════════
    // FrequenceJours
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("FrequenceJours")
    class FrequenceJoursTest {

        @Test void creerFrequence() {
            assertThat(FrequenceJours.de(7).valeur()).isEqualTo(7);
        }

        @Test void refuseZero() {
            assertThatThrownBy(() -> FrequenceJours.de(0))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void refuseNegatif() {
            assertThatThrownBy(() -> FrequenceJours.de(-1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void egaliteMemeReference() {
            var f = FrequenceJours.de(7);
            assertThat(f).isEqualTo(f);
        }

        @Test void egaliteMemValeur() {
            assertThat(FrequenceJours.de(7)).isEqualTo(FrequenceJours.de(7));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(FrequenceJours.de(7)).isNotEqualTo(FrequenceJours.de(14));
        }

        @Test void inegaliteAutreType() {
            assertThat(FrequenceJours.de(7)).isNotEqualTo(7);
        }

        @Test void hashCodeCoherent() {
            assertThat(FrequenceJours.de(7).hashCode())
                    .isEqualTo(FrequenceJours.de(7).hashCode());
        }

        @Test void toStringContientJours() {
            assertThat(FrequenceJours.de(7).toString()).contains("7");
        }
    }

    // ══════════════════════════════════════════════════════════════
    // LieuEvenement
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("LieuEvenement")
    class LieuEvenementTest {

        @Test void creerLieu() {
            assertThat(LieuEvenement.de("Salle A").valeur()).isEqualTo("Salle A");
        }

        @Test void trimmeLesEspaces() {
            assertThat(LieuEvenement.de("  Salle A  ").valeur()).isEqualTo("Salle A");
        }

        @Test void refuseNul() {
            assertThatThrownBy(() -> LieuEvenement.de(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void refuseVide() {
            assertThatThrownBy(() -> LieuEvenement.de(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void refuseBlanc() {
            assertThatThrownBy(() -> LieuEvenement.de("   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void egaliteMemeReference() {
            var l = LieuEvenement.de("A");
            assertThat(l).isEqualTo(l);
        }

        @Test void egaliteMemValeur() {
            assertThat(LieuEvenement.de("A")).isEqualTo(LieuEvenement.de("A"));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(LieuEvenement.de("A")).isNotEqualTo(LieuEvenement.de("B"));
        }

        @Test void inegaliteAutreType() {
            assertThat(LieuEvenement.de("A")).isNotEqualTo("A");
        }

        @Test void hashCodeCoherent() {
            assertThat(LieuEvenement.de("A").hashCode())
                    .isEqualTo(LieuEvenement.de("A").hashCode());
        }

        @Test void toStringRetourneValeur() {
            assertThat(LieuEvenement.de("Salle A").toString()).isEqualTo("Salle A");
        }
    }

    // ══════════════════════════════════════════════════════════════
    // Proprietaire
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("Proprietaire")
    class ProprietaireTest {

        @Test void creerProprietaire() {
            assertThat(Proprietaire.de("Alice").nom()).isEqualTo("Alice");
        }

        @Test void trimmeLesEspaces() {
            assertThat(Proprietaire.de("  Alice  ").nom()).isEqualTo("Alice");
        }

        @Test void refuseNul() {
            assertThatThrownBy(() -> Proprietaire.de(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void refuseVide() {
            assertThatThrownBy(() -> Proprietaire.de(""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void refuseBlanc() {
            assertThatThrownBy(() -> Proprietaire.de("   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void egaliteMemeReference() {
            var p = Proprietaire.de("Alice");
            assertThat(p).isEqualTo(p);
        }

        @Test void egaliteMemValeur() {
            assertThat(Proprietaire.de("Alice")).isEqualTo(Proprietaire.de("Alice"));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(Proprietaire.de("Alice")).isNotEqualTo(Proprietaire.de("Bob"));
        }

        @Test void inegaliteAutreType() {
            assertThat(Proprietaire.de("Alice")).isNotEqualTo("Alice");
        }

        @Test void hashCodeCoherent() {
            assertThat(Proprietaire.de("Alice").hashCode())
                    .isEqualTo(Proprietaire.de("Alice").hashCode());
        }

        @Test void toStringRetourneNom() {
            assertThat(Proprietaire.de("Alice").toString()).isEqualTo("Alice");
        }
    }

    // ══════════════════════════════════════════════════════════════
    // Participants
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("Participants")
    class ParticipantsTest {

        @Test void creerViaVarargs() {
            assertThat(Participants.de("Alice", "Bob").liste())
                    .containsExactly("Alice", "Bob");
        }

        @Test void creerViaListe() {
            assertThat(Participants.de(List.of("Alice")).liste())
                    .containsExactly("Alice");
        }

        @Test void refuseListeNulle() {
            assertThatThrownBy(() -> Participants.de((List<String>) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void refuseListeVide() {
            assertThatThrownBy(() -> Participants.de(List.of()))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void toStringConcateneAvecVirgule() {
            assertThat(Participants.de("Alice", "Bob").toString())
                    .isEqualTo("Alice, Bob");
        }

        @Test void egaliteMemeReference() {
            var p = Participants.de("Alice");
            assertThat(p).isEqualTo(p);
        }

        @Test void egaliteMemValeur() {
            assertThat(Participants.de("Alice", "Bob"))
                    .isEqualTo(Participants.de("Alice", "Bob"));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(Participants.de("Alice"))
                    .isNotEqualTo(Participants.de("Bob"));
        }

        @Test void inegaliteAutreType() {
            assertThat(Participants.de("Alice")).isNotEqualTo("Alice");
        }

        @Test void hashCodeCoherent() {
            assertThat(Participants.de("Alice").hashCode())
                    .isEqualTo(Participants.de("Alice").hashCode());
        }
    }

    // ══════════════════════════════════════════════════════════════
    // DateEvenement
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("DateEvenement")
    class DateEvenementTest {

        private final DateEvenement d1 = DateEvenement.de(2025, 6, 15, 10, 0);
        private final DateEvenement d2 = DateEvenement.de(2025, 6, 15, 12, 0);
        private final DateEvenement d3 = DateEvenement.de(2025, 6, 15, 8, 0);

        @Test void creerViaLocalDateTime() {
            var ldt = LocalDateTime.of(2025, 6, 15, 10, 0);
            assertThat(DateEvenement.de(ldt).valeur()).isEqualTo(ldt);
        }

        @Test void creerViaComposants() {
            assertThat(d1.valeur()).isEqualTo(LocalDateTime.of(2025, 6, 15, 10, 0));
        }

        @Test void refuseLocalDateTimeNul() {
            assertThatThrownBy(() -> DateEvenement.de((LocalDateTime) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void estAvantVrai() {
            assertThat(d1.estAvant(d2)).isTrue();
        }

        @Test void estAvantFaux() {
            assertThat(d2.estAvant(d1)).isFalse();
        }

        @Test void estApresVrai() {
            assertThat(d2.estApres(d1)).isTrue();
        }

        @Test void estApresFaux() {
            assertThat(d1.estApres(d2)).isFalse();
        }

        @Test void estEntreInclusDebut() {
            // d3(8h) <= d1(10h) <= d2(12h)
            assertThat(d1.estEntreInclus(d3, d2)).isTrue();
        }

        @Test void estEntreInclusExactementDebut() {
            assertThat(d3.estEntreInclus(d3, d2)).isTrue();
        }

        @Test void estEntreInclusExactementFin() {
            assertThat(d2.estEntreInclus(d3, d2)).isTrue();
        }

        @Test void estEntreInclusHorsBornes() {
            // d1(10h) n'est pas entre d2(12h) et d2(12h)
            assertThat(d1.estEntreInclus(d2, d2)).isFalse();
        }

        @Test void plusMinutesAjoute() {
            var resultat = d1.plusMinutes(DureeEvenement.deMinutes(30));
            assertThat(resultat).isEqualTo(DateEvenement.de(2025, 6, 15, 10, 30));
        }

        @Test void plusJoursAjoute() {
            var resultat = d1.plusJours(FrequenceJours.de(1));
            assertThat(resultat).isEqualTo(DateEvenement.de(2025, 6, 16, 10, 0));
        }

        @Test void egaliteMemeReference() {
            assertThat(d1).isEqualTo(d1);
        }

        @Test void egaliteMemValeur() {
            assertThat(DateEvenement.de(2025, 6, 15, 10, 0))
                    .isEqualTo(DateEvenement.de(2025, 6, 15, 10, 0));
        }

        @Test void inegaliteValeursDifferentes() {
            assertThat(d1).isNotEqualTo(d2);
        }

        @Test void inegaliteAutreType() {
            assertThat(d1).isNotEqualTo("2025-06-15");
        }

        @Test void hashCodeCoherent() {
            assertThat(DateEvenement.de(2025, 6, 15, 10, 0).hashCode())
                    .isEqualTo(DateEvenement.de(2025, 6, 15, 10, 0).hashCode());
        }

        @Test void toStringNonVide() {
            assertThat(d1.toString()).isNotBlank();
        }
    }

    // ══════════════════════════════════════════════════════════════
    // Periode
    // ══════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("Periode")
    class PeriodeTest {

        private final DateEvenement debut = DateEvenement.de(2025, 6, 1, 0, 0);
        private final DateEvenement fin   = DateEvenement.de(2025, 6, 30, 23, 59);
        private final Periode juin = Periode.entre(debut, fin);

        @Test void creerPeriode() {
            assertThat(juin.debut()).isEqualTo(debut);
            assertThat(juin.fin()).isEqualTo(fin);
        }

        @Test void refuseDebutNul() {
            assertThatThrownBy(() -> Periode.entre(null, fin))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void refuseFinNulle() {
            assertThatThrownBy(() -> Periode.entre(debut, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test void refuseFinAvantDebut() {
            assertThatThrownBy(() -> Periode.entre(fin, debut))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test void accepteDebutEgalFin() {
            // période d'un instant = valide
            assertThat(Periode.entre(debut, debut)).isNotNull();
        }

        @Test void contientDateDedans() {
            assertThat(juin.contient(DateEvenement.de(2025, 6, 15, 12, 0))).isTrue();
        }

        @Test void contientDateAuDebut() {
            assertThat(juin.contient(debut)).isTrue();
        }

        @Test void contientDateALaFin() {
            assertThat(juin.contient(fin)).isTrue();
        }

        @Test void neContientPasDateAvant() {
            assertThat(juin.contient(DateEvenement.de(2025, 5, 31, 23, 59))).isFalse();
        }

        @Test void neContientPasDateApres() {
            assertThat(juin.contient(DateEvenement.de(2025, 7, 1, 0, 0))).isFalse();
        }

        @Test void egaliteMemeReference() {
            assertThat(juin).isEqualTo(juin);
        }

        @Test void egaliteMemValeur() {
            assertThat(Periode.entre(debut, fin)).isEqualTo(Periode.entre(debut, fin));
        }

        @Test void inegaliteDebutDifferent() {
            var autre = Periode.entre(DateEvenement.de(2025, 5, 1, 0, 0), fin);
            assertThat(juin).isNotEqualTo(autre);
        }

        @Test void inegaliteFinDifferente() {
            var autre = Periode.entre(debut, DateEvenement.de(2025, 7, 31, 23, 59));
            assertThat(juin).isNotEqualTo(autre);
        }

        @Test void inegaliteAutreType() {
            assertThat(juin).isNotEqualTo("juin");
        }

        @Test void hashCodeCoherent() {
            assertThat(Periode.entre(debut, fin).hashCode())
                    .isEqualTo(Periode.entre(debut, fin).hashCode());
        }
    }
}