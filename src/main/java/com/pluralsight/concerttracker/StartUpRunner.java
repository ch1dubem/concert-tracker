package com.pluralsight.concerttracker;

import com.pluralsight.concerttracker.models.Concert;
import com.pluralsight.concerttracker.service.ConcertTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

// Seeds starter data when the database is empty, then runs the menu.
@Component
public class StartUpRunner implements CommandLineRunner {

    private final ConcertTrackerService service;
    private final Scanner scanner = new Scanner(System.in);
    private final NumberFormat money = NumberFormat.getCurrencyInstance();

    @Autowired
    public StartUpRunner(ConcertTrackerService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) {
        service.seedIfEmpty();
        mainMenu();
        System.out.println("Goodbye.");
    }

    private void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== Concert Tracker ==========");
            System.out.println("1) List all concerts");
            System.out.println("0) Quit");
            switch (prompt("Choose: ")) {
                case "1" -> printConcerts(service.allConcerts());
                case "0" -> running = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private void printConcerts(List<Concert> concerts) {
        if (concerts.isEmpty()) {
            System.out.println("No concerts found.");
            return;
        }
        for (Concert c : concerts) {
            System.out.println(concertLine(c));
        }
    }

    // One concert per line, always with the artist's and venue's names, never bare ids.
    private String concertLine(Concert c) {
        return c.getId() + " - " + c.getConcertYear() + "  "
                + c.getArtist().getName() + " @ " + c.getVenue().getName()
                + " (" + c.getVenue().getCity() + ")  "
                + money.format(c.getTicketPrice()) + "  " + c.getTicketsSold() + " sold";
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }
}
