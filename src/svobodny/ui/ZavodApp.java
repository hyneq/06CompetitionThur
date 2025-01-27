/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package svobodny.ui;

import svobodny.app.Zavod;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import svobodny.Gender;

/**
 *
 * @author hynek
 */
public class ZavodApp {
    
    static Scanner sc = new Scanner(System.in);
    static Zavod competition;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        displayCredits();
        
        createCompetition();
        
        char choice;
        do {
            displayMenu();
            
            choice = sc.next().charAt(0);
            sc.nextLine(); // buffer cleanup
            try {
                switch (choice) {
                    case '1' -> registerCompetitors();
                    case '2' -> setStartTime();
                    case '3' -> setStartTimeOffset();
                    case '4' -> setFinishTime();
                    case '5' -> displayCompetitors();
                    case '6' -> displayResultList();
                }
            } catch (RuntimeException e) {
                handleErrors(e);
            }
            
        } while (choice != '0');
    }

    private static void displayCredits() {
        System.out.println("ZavodApp");
        System.out.println("");
    }
    
    private static void createCompetition() {
        System.out.print("Zadej jmeno zavodu: ");
        String name = sc.next();
        sc.nextLine(); // buffer cleanup
        competition = new Zavod(name);
    }

    private static void displayMenu() {
        System.out.println("");
        System.out.println("Vyber:");
        System.out.println("1. Zapsani zavodniku");
        System.out.println("2. Nastavit cas startu (hromadne)");
        System.out.println("3. Nastavit cas startu (s odstupy)");
        System.out.println("4. Nastavit cas cile");
        System.out.println("5. Vypsat zavodniky");
        System.out.println("6. Vysledkova listina");
        System.out.println("0. Konec");
    }

    private static void registerCompetitors() {
        char ans;
        String name;
        String surname;
        int birthYear;
        Gender gender;
        String club;
        do {
            try {
                System.out.print("Jmeno: ");
                name = sc.nextLine();
                
                System.out.print("Prijmeni: ");
                surname = sc.nextLine();
                
                System.out.print("Rok narozeni: ");
                birthYear = sc.nextInt();
                sc.nextLine(); // buffer cleanup
                
                System.out.print("Pohlavi (M/Z): ");
                gender = Gender.of(sc.next().toUpperCase().charAt(0));
                sc.nextLine(); // buffer cleanup
                
                System.out.print("Klub: ");
                club = sc.next();
                sc.nextLine(); // buffer cleanup
                
                competition.addCompetitor(name, surname, birthYear, gender, club);
            } catch (RuntimeException e) {
                handleErrors(e);
            }
            System.out.print("Chcete pokracovat? (a/n)");
            ans = sc.next().toLowerCase().charAt(0);
            sc.nextLine(); // buffer cleanup
        } while (ans == 'a');
    }

    private static void setStartTime() {
        System.out.println("Zadejte cas startu (prazdne pro aktualni cas) (HH:MM:SS): ");
        LocalTime time = LocalTime.parse(sc.next());
        competition.setStartTimeAll(time);
    }

    private static void setStartTimeOffset() {
        System.out.print("Zadejte cas startu (prazdne pro aktualni cas) (HH:MM:SS): ");
        LocalTime time = LocalTime.parse(sc.next());
        sc.nextLine(); // buffer cleanup
        System.out.print("Zadejte odstup v minutach:");
        Duration offset = Duration.of(sc.nextInt(),ChronoUnit.MINUTES);
        sc.nextLine(); // buffer cleanup
        competition.setStartTimeAll(time, offset);
    }

    private static void setFinishTime() {
        System.out.print("Zadejte cislo zavodnika: ");
        int number = sc.nextInt();
        sc.nextLine(); // buffer cleanup
        System.out.print("Zadejte cilovy cas (HH:MM:SS): ");
        LocalTime time = LocalTime.parse(sc.next());
        sc.nextLine(); // buffer cleanup
        competition.setFinishTimeOf(number, time);
    }

    private static void displayCompetitors() {
        System.out.println(competition.getCompetitorList());
    }
    
    private static void displayResultList() {
        System.out.println(competition.getResultList());
    }
    
    private static void handleErrors(RuntimeException e) {
        if (e instanceof InputMismatchException || e instanceof IllegalArgumentException || e instanceof DateTimeParseException) {
            System.out.println("Neplatny vstup.");
            if (sc.hasNextLine()) {
                sc.nextLine();
            }
        } else if (e instanceof NoSuchElementException) {
            System.out.println(e.getMessage());
        } else if (e instanceof IllegalStateException) {
            System.out.format("Neplatny stav: %s%n", e.getMessage());
        } else if (e instanceof RuntimeException) {
            System.out.format("Chyba %s: %s%n", e.getClass().getSimpleName(), e.getMessage());
        }
    }
    
}
