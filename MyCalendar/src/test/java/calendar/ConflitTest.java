package calendar;

import calendar.domain.event.RendezVousPersonnel;
import calendar.domain.valueobject.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ConflitHoraire")
class ConflitTest {

    private RendezVousPersonnel rdv(String titre, int heure) {
        return new RendezVousPersonnel(
                EventId.generer(),
                TitreEvenement.de(titre),
                Proprietaire.de("Alice"),
                DateEvenement.de(2025, 6, 10, heure, 0),
                DureeEvenement.deMinutes(60)
        );
    }

    @Test
    @DisplayName("crée un conflit avec les deux événements accessibles")
    void creerConflit() {
        var e1 = rdv("A", 10);
        var e2 = rdv("B", 10);
        var conflit = Conflit.entre(e1, e2);
        assertThat(conflit.premier()).isEqualTo(e1);
        assertThat(conflit.second()).isEqualTo(e2);
    }

    @Test
    @DisplayName("toString contient les deux titres")
    void toStringContientTitres() {
        var conflit = Conflit.entre(rdv("Médecin", 10), rdv("Dentiste", 10));
        assertThat(conflit.toString()).contains("Médecin").contains("Dentiste");
    }

    @Test
    @DisplayName("égalité : même référence")
    void egaliteMemeReference() {
        var conflit = Conflit.entre(rdv("A", 10), rdv("B", 10));
        assertThat(conflit).isEqualTo(conflit);
    }

    @Test
    @DisplayName("égalité : ordre normal (e1, e2) == (e1, e2)")
    void egaliteOrdreNormal() {
        var e1 = rdv("A", 10);
        var e2 = rdv("B", 10);
        assertThat(Conflit.entre(e1, e2)).isEqualTo(Conflit.entre(e1, e2));
    }

    @Test
    @DisplayName("égalité : ordre inversé (e1, e2) == (e2, e1)")
    void egaliteOrdreInverse() {
        var e1 = rdv("A", 10);
        var e2 = rdv("B", 10);
        assertThat(Conflit.entre(e1, e2)).isEqualTo(Conflit.entre(e2, e1));
    }

    @Test
    @DisplayName("inégalité avec un autre type")
    void inegaliteAutreType() {
        var conflit = Conflit.entre(rdv("A", 10), rdv("B", 10));
        assertThat(conflit).isNotEqualTo("conflit");
    }

    @Test
    @DisplayName("inégalité avec des événements différents")
    void inegaliteEvenementsDifferents() {
        var c1 = Conflit.entre(rdv("A", 10), rdv("B", 10));
        var c2 = Conflit.entre(rdv("C", 10), rdv("D", 10));
        assertThat(c1).isNotEqualTo(c2);
    }

    @Test
    @DisplayName("hashCode cohérent")
    void hashCodeCoherent() {
        var e1 = rdv("A", 10);
        var e2 = rdv("B", 10);
        assertThat(Conflit.entre(e1, e2).hashCode())
                .isEqualTo(Conflit.entre(e1, e2).hashCode());
    }
}