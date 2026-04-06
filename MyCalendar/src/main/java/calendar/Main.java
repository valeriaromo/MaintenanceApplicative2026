package calendar;

import calendar.domain.CalendarManager;
import calendar.domain.event.EvenementPeriodique;
import calendar.domain.event.RendezVousPersonnel;
import calendar.domain.event.Reunion;
import calendar.domain.valueobject.DateEvenement;
import calendar.domain.valueobject.DureeEvenement;
import calendar.domain.valueobject.EventId;
import calendar.domain.valueobject.FrequenceJours;
import calendar.domain.valueobject.LieuEvenement;
import calendar.domain.valueobject.Participants;
import calendar.domain.valueobject.Periode;
import calendar.domain.valueobject.Proprietaire;
import calendar.domain.valueobject.TitreEvenement;

import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var calendrier = new CalendarManager();
        var scanner = new Scanner(System.in);
        var comptes = new ArrayList<Compte>();

        comptes.add(new Compte("Roger", "Chat"));
        comptes.add(new Compte("Pierre", "KiRouhl"));

        Compte utilisateurConnecte = null;

        while (true) {
            if (utilisateurConnecte == null) {
                afficherBanniere();
                System.out.println("1 - Se connecter");
                System.out.println("2 - Créer un compte");
                System.out.print("Choix : ");

                switch (scanner.nextLine()) {
                    case "1" -> utilisateurConnecte = seConnecter(scanner, comptes);
                    case "2" -> {
                        var nouveau = creerCompte(scanner, comptes);
                        if (nouveau != null) utilisateurConnecte = nouveau;
                    }
                }
                continue;
            }

            System.out.println("\nBonjour, " + utilisateurConnecte.nom());
            System.out.println("=== Menu Gestionnaire d'Événements ===");
            System.out.println("1 - Voir les événements");
            System.out.println("2 - Ajouter un rendez-vous personnel");
            System.out.println("3 - Ajouter une réunion");
            System.out.println("4 - Ajouter un événement périodique");
            System.out.println("5 - Détecter les conflits");
            System.out.println("6 - Supprimer un événement");
            System.out.println("7 - Se déconnecter");
            System.out.print("Votre choix : ");

            switch (scanner.nextLine()) {
                case "1" -> menuVisualisation(scanner, calendrier);
                case "2" -> ajouterRdvPersonnel(scanner, calendrier, utilisateurConnecte.nom());
                case "3" -> ajouterReunion(scanner, calendrier, utilisateurConnecte.nom());
                case "4" -> ajouterPeriodique(scanner, calendrier, utilisateurConnecte.nom());
                case "5" -> afficherConflits(calendrier);
                case "6" -> supprimerEvenement(scanner, calendrier);
                case "7" -> {
                    utilisateurConnecte = null;
                    System.out.print("Quitter l'application ? (oui/non) : ");
                    if (scanner.nextLine().trim().equalsIgnoreCase("oui")) return;
                }
            }
        }
    }


    private static Compte seConnecter(Scanner scanner, List<Compte> comptes) {
        System.out.print("Nom d'utilisateur : ");
        var nom = scanner.nextLine();
        System.out.print("Mot de passe : ");
        var mdp = scanner.nextLine();

        return comptes.stream()
                .filter(c -> c.correspond(nom, mdp))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("Identifiants incorrects.");
                    return null;
                });
    }

    private static Compte creerCompte(Scanner scanner, List<Compte> comptes) {
        System.out.print("Nom d'utilisateur : ");
        var nom = scanner.nextLine();
        System.out.print("Mot de passe : ");
        var mdp = scanner.nextLine();
        System.out.print("Répéter mot de passe : ");
        if (!scanner.nextLine().equals(mdp)) {
            System.out.println("Les mots de passe ne correspondent pas.");
            return null;
        }
        var compte = new Compte(nom, mdp);
        comptes.add(compte);
        System.out.println("Compte créé !");
        return compte;
    }


    private static void menuVisualisation(Scanner scanner, CalendarManager calendrier) {
        System.out.println("\n=== Visualisation ===");
        System.out.println("1 - Tous les événements");
        System.out.println("2 - Par mois");
        System.out.println("3 - Par semaine");
        System.out.println("4 - Par jour");
        System.out.print("Votre choix : ");

        switch (scanner.nextLine()) {
            case "1" -> calendrier.tousLesEvenements()
                    .forEach(e -> System.out.println("- " + e.description()));
            case "2" -> {
                var annee = lireEntier(scanner, "Année (AAAA) : ");
                var mois  = lireEntier(scanner, "Mois (1-12) : ");
                var debut = DateEvenement.de(annee, mois, 1, 0, 0);
                var fin   = DateEvenement.de(debut.valeur().plusMonths(1).minusSeconds(1));
                afficherListe(calendrier.evenementsDansPeriode(Periode.entre(debut, fin)));
            }
            case "3" -> {
                var annee   = lireEntier(scanner, "Année (AAAA) : ");
                var semaine = lireEntier(scanner, "Numéro de semaine (1-52) : ");
                var debutLdt = java.time.LocalDateTime.now()
                        .withYear(annee)
                        .with(WeekFields.of(Locale.FRANCE).weekOfYear(), semaine)
                        .with(WeekFields.of(Locale.FRANCE).dayOfWeek(), 1)
                        .withHour(0).withMinute(0).withSecond(0).withNano(0);
                var debut = DateEvenement.de(debutLdt);
                var fin   = DateEvenement.de(debutLdt.plusDays(7).minusSeconds(1));
                afficherListe(calendrier.evenementsDansPeriode(Periode.entre(debut, fin)));
            }
            case "4" -> {
                var annee = lireEntier(scanner, "Année (AAAA) : ");
                var mois  = lireEntier(scanner, "Mois (1-12) : ");
                var jour  = lireEntier(scanner, "Jour (1-31) : ");
                var debut = DateEvenement.de(annee, mois, jour, 0, 0);
                var fin   = DateEvenement.de(debut.valeur().plusDays(1).minusSeconds(1));
                afficherListe(calendrier.evenementsDansPeriode(Periode.entre(debut, fin)));
            }
        }
    }


    private static void ajouterRdvPersonnel(Scanner scanner, CalendarManager calendrier, String nomUtilisateur) {
        System.out.print("Titre : ");
        var titre = scanner.nextLine();
        var date  = lireDate(scanner);
        var duree = lireEntier(scanner, "Durée (en minutes) : ");

        var event = new RendezVousPersonnel(
                EventId.generer(),
                TitreEvenement.de(titre),
                Proprietaire.de(nomUtilisateur),
                date,
                DureeEvenement.deMinutes(duree)
        );
        calendrier.ajouterEvenement(event);
        System.out.println("Rendez-vous ajouté (id : " + event.id() + ")");
    }

    private static void ajouterReunion(Scanner scanner, CalendarManager calendrier, String nomUtilisateur) {
        System.out.print("Titre : ");
        var titre = scanner.nextLine();
        var date  = lireDate(scanner);
        var duree = lireEntier(scanner, "Durée (en minutes) : ");
        System.out.print("Lieu : ");
        var lieu = scanner.nextLine();

        var noms = new ArrayList<String>();
        noms.add(nomUtilisateur);
        System.out.println("Ajouter un participant ? (oui/non)");
        while (scanner.nextLine().equalsIgnoreCase("oui")) {
            System.out.print("Nom du participant : ");
            noms.add(scanner.nextLine());
            System.out.println("Ajouter un autre participant ? (oui/non)");
        }

        var event = new Reunion(
                EventId.generer(),
                TitreEvenement.de(titre),
                Proprietaire.de(nomUtilisateur),
                date,
                DureeEvenement.deMinutes(duree),
                LieuEvenement.de(lieu),
                Participants.de(noms)
        );
        calendrier.ajouterEvenement(event);
        System.out.println("Réunion ajoutée (id : " + event.id() + ")");
    }

    private static void ajouterPeriodique(Scanner scanner, CalendarManager calendrier, String nomUtilisateur) {
        System.out.print("Titre : ");
        var titre     = scanner.nextLine();
        var date      = lireDate(scanner);
        var frequence = lireEntier(scanner, "Fréquence (en jours) : ");

        var event = new EvenementPeriodique(
                EventId.generer(),
                TitreEvenement.de(titre),
                Proprietaire.de(nomUtilisateur),
                date,
                FrequenceJours.de(frequence)
        );
        calendrier.ajouterEvenement(event);
        System.out.println("Événement périodique ajouté (id : " + event.id() + ")");
    }


    private static void afficherConflits(CalendarManager calendrier) {
        var conflits = calendrier.detecterConflits();
        if (conflits.isEmpty()) {
            System.out.println("Aucun conflit détecté.");
        } else {
            System.out.println("⚠ Conflits détectés :");
            conflits.forEach(c -> System.out.println("  " + c));
        }
    }

    private static void supprimerEvenement(Scanner scanner, CalendarManager calendrier) {
        if (calendrier.tousLesEvenements().isEmpty()) {
            System.out.println("Aucun événement à supprimer.");
            return;
        }
        System.out.println("Événements actuels :");
        calendrier.tousLesEvenements()
                .forEach(e -> System.out.println("  [" + e.id() + "] " + e.titre()));
        System.out.print("ID à supprimer : ");
        var id = scanner.nextLine().trim();
        calendrier.supprimerParId(EventId.de(id));
        System.out.println("Suppression effectuée.");
    }


    private static DateEvenement lireDate(Scanner scanner) {
        var annee  = lireEntier(scanner, "Année (AAAA) : ");
        var mois   = lireEntier(scanner, "Mois (1-12) : ");
        var jour   = lireEntier(scanner, "Jour (1-31) : ");
        var heure  = lireEntier(scanner, "Heure début (0-23) : ");
        var minute = lireEntier(scanner, "Minute début (0-59) : ");
        return DateEvenement.de(annee, mois, jour, heure, minute);
    }

    private static int lireEntier(Scanner scanner, String invite) {
        System.out.print(invite);
        return Integer.parseInt(scanner.nextLine());
    }

    private static void afficherListe(List<calendar.domain.event.Evenement> evenements) {
        if (evenements.isEmpty()) {
            System.out.println("Aucun événement trouvé pour cette période.");
        } else {
            System.out.println("Événements trouvés :");
            evenements.forEach(e -> System.out.println("  - " + e.description()));
        }
    }

    private static void afficherBanniere() {
        System.out.println("  _____         _                   _                __  __");
        System.out.println(" / ____|       | |                 | |              |  \\/  |");
        System.out.println("| |       __ _ | |  ___  _ __    __| |  __ _  _ __  | \\  / |  __ _  _ __    __ _   __ _   ___  _ __");
        System.out.println("| |      / _` || | / _ \\| '_ \\  / _` | / _` || '__| | |\\/| | / _` || '_ \\  / _` | / _` | / _ \\| '__|");
        System.out.println("| |____ | (_| || ||  __/| | | || (_| || (_| || |    | |  | || (_| || | | || (_| || (_| ||  __/| |");
        System.out.println(" \\_____| \\__,_||_| \\___||_| |_| \\__,_| \\__,_||_|    |_|  |_| \\__,_||_| |_| \\__,_| \\__, | \\___||_|");
        System.out.println("                                                                                   __/ |");
        System.out.println("                                                                                  |___/");
    }


    record Compte(String nom, String motDePasse) {
        boolean correspond(String nom, String mdp) {
            return this.nom.equals(nom) && this.motDePasse.equals(mdp);
        }
    }
}