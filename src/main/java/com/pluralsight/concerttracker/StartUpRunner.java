package com.pluralsight.concerttracker;

import com.pluralsight.concerttracker.models.Artist;
import com.pluralsight.concerttracker.models.Concert;
import com.pluralsight.concerttracker.models.Promoter;
import com.pluralsight.concerttracker.models.Venue;
import com.pluralsight.concerttracker.service.ConcertTrackerService;
import com.pluralsight.concerttracker.service.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.List;
import java.util.Scanner;

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
            System.out.println("1) Concerts");
            System.out.println("2) Artists");
            System.out.println("3) Venues");
            System.out.println("4) Promoters");
            System.out.println("0) Quit");
            switch (prompt("Choose: ")) {
                case "1" -> concertsScreen();
                case "2" -> artistsScreen();
                case "3" -> venuesScreen();
                case "4" -> promotersScreen();
                case "0" -> running = false;
                default -> System.out.println("Unknown option.");
            }
        }
    }

    // ===== Concerts =====
    private void concertsScreen() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Concerts -----");
            System.out.println("1) List all concerts");
            System.out.println("2) View one by id");
            System.out.println("3) Add a concert");
            System.out.println("4) Update ticket price");
            System.out.println("5) Update tickets sold");
            System.out.println("6) Delete a concert");
            System.out.println("0) Back");
            try {
                switch (prompt("Choose: ")) {
                    case "1" -> printConcerts(service.allConcerts());
                    case "2" -> viewConcert();
                    case "3" -> addConcert();
                    case "4" -> updateConcertPrice();
                    case "5" -> updateTicketsSold();
                    case "6" -> deleteConcert();
                    case "0" -> back = true;
                    default -> System.out.println("Unknown option.");
                }
            } catch (NotFoundException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printConcerts(List<Concert> concerts) {
        if (concerts.isEmpty()) { System.out.println("No concerts found."); return; }
        for (Concert c : concerts) System.out.println(concertLine(c));
    }

    private void viewConcert() {
        Concert c = service.concertById(promptLong("Concert id: "));
        System.out.println("\nConcert #" + c.getId());
        System.out.println("  Artist:       " + c.getArtist().getName());
        System.out.println("  Venue:        " + c.getVenue().getName() + " (" + c.getVenue().getCity() + ")");
        System.out.println("  Promoter:     " + c.getPromoter().getName());
        System.out.println("  Year:         " + c.getConcertYear());
        System.out.println("  Ticket price: " + money.format(c.getTicketPrice()));
        System.out.println("  Tickets sold: " + c.getTicketsSold());
    }

    private void addConcert() {
        List<Artist> artists = service.allArtists();
        List<Venue> venues = service.allVenues();
        List<Promoter> promoters = service.allPromoters();
        if (artists.isEmpty() || venues.isEmpty() || promoters.isEmpty()) {
            System.out.println("You need at least one artist, one venue, and one promoter first.");
            return;
        }
        System.out.println("Artists:");
        for (Artist a : artists) System.out.println("  " + a.getId() + " - " + a.getName());
        long artistId = promptLong("Artist id: ");
        System.out.println("Venues:");
        for (Venue v : venues) System.out.println("  " + v.getId() + " - " + v.getName() + " (capacity " + v.getCapacity() + ")");
        long venueId = promptLong("Venue id: ");
        System.out.println("Promoters:");
        for (Promoter p : promoters) System.out.println("  " + p.getId() + " - " + p.getName());
        long promoterId = promptLong("Promoter id: ");
        int year = promptInt("Year: ");
        double price = promptDouble("Ticket price: ");
        int sold = promptInt("Tickets sold: ");
        Concert saved = service.addConcert(artistId, venueId, promoterId, year, price, sold);
        System.out.println("Added concert #" + saved.getId() + ": " + concertLine(saved));
    }

    private void updateConcertPrice() {
        long id = promptLong("Concert id: ");
        double price = promptDouble("New ticket price: ");
        Concert updated = service.updateConcertPrice(id, price);
        System.out.println("Updated price to " + money.format(updated.getTicketPrice()) + ".");
    }

    private void updateTicketsSold() {
        long id = promptLong("Concert id: ");
        int sold = promptInt("New tickets sold: ");
        Concert updated = service.updateTicketsSold(id, sold);
        System.out.println("Updated tickets sold to " + updated.getTicketsSold() + ".");
    }

    private void deleteConcert() {
        service.deleteConcert(promptLong("Concert id to delete: "));
        System.out.println("Deleted.");
    }

    // ===== Artists =====
    private void artistsScreen() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Artists -----");
            System.out.println("1) List");
            System.out.println("2) Add");
            System.out.println("3) Find by genre");
            System.out.println("4) Find by name");
            System.out.println("5) Update genre");
            System.out.println("6) Delete");
            System.out.println("0) Back");
            try {
                switch (prompt("Choose: ")) {
                    case "1" -> printArtists(service.allArtists());
                    case "2" -> {
                        String name = prompt("Name: ");
                        String genre = prompt("Genre: ");
                        Artist a = service.addArtist(name, genre);
                        System.out.println("Added artist #" + a.getId() + " " + a.getName() + ".");
                    }
                    case "3" -> printArtists(service.artistsByGenre(prompt("Genre: ")));
                    case "4" -> printArtists(service.artistsByName(prompt("Name contains: ")));
                    case "5" -> {
                        long id = promptLong("Artist id: ");
                        Artist a = service.updateArtistGenre(id, prompt("New genre: "));
                        System.out.println("Updated " + a.getName() + " to genre " + a.getGenre() + ".");
                    }
                    case "6" -> {
                        service.deleteArtist(promptLong("Artist id to delete: "));
                        System.out.println("Deleted.");
                    }
                    case "0" -> back = true;
                    default -> System.out.println("Unknown option.");
                }
            } catch (NotFoundException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printArtists(List<Artist> artists) {
        if (artists.isEmpty()) { System.out.println("No artists found."); return; }
        for (Artist a : artists) System.out.println(a.getId() + " - " + a.getName() + " (" + a.getGenre() + ")");
    }

    // ===== Venues =====
    private void venuesScreen() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Venues -----");
            System.out.println("1) List");
            System.out.println("2) Add");
            System.out.println("3) Find by city");
            System.out.println("4) Find by name");
            System.out.println("5) Find by minimum capacity");
            System.out.println("6) Update capacity");
            System.out.println("7) Delete");
            System.out.println("0) Back");
            try {
                switch (prompt("Choose: ")) {
                    case "1" -> printVenues(service.allVenues());
                    case "2" -> {
                        String name = prompt("Name: ");
                        String city = prompt("City: ");
                        int capacity = promptInt("Capacity: ");
                        Venue v = service.addVenue(name, city, capacity);
                        System.out.println("Added venue #" + v.getId() + " " + v.getName() + ".");
                    }
                    case "3" -> printVenues(service.venuesByCity(prompt("City: ")));
                    case "4" -> printVenues(service.venuesByName(prompt("Name contains: ")));
                    case "5" -> printVenues(service.venuesByMinCapacity(promptInt("Minimum capacity: ")));
                    case "6" -> {
                        long id = promptLong("Venue id: ");
                        int capacity = promptInt("New capacity: ");
                        Venue v = service.updateVenueCapacity(id, capacity);
                        System.out.println("Updated " + v.getName() + " capacity to " + v.getCapacity() + ".");
                    }
                    case "7" -> {
                        service.deleteVenue(promptLong("Venue id to delete: "));
                        System.out.println("Deleted.");
                    }
                    case "0" -> back = true;
                    default -> System.out.println("Unknown option.");
                }
            } catch (NotFoundException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printVenues(List<Venue> venues) {
        if (venues.isEmpty()) { System.out.println("No venues found."); return; }
        for (Venue v : venues) System.out.println(v.getId() + " - " + v.getName() + " (" + v.getCity() + ", capacity " + v.getCapacity() + ")");
    }

    // ===== Promoters =====
    private void promotersScreen() {
        boolean back = false;
        while (!back) {
            System.out.println("\n----- Promoters -----");
            System.out.println("1) List");
            System.out.println("2) Add");
            System.out.println("3) Find by name");
            System.out.println("4) Delete");
            System.out.println("0) Back");
            try {
                switch (prompt("Choose: ")) {
                    case "1" -> printPromoters(service.allPromoters());
                    case "2" -> {
                        Promoter p = service.addPromoter(prompt("Name: "));
                        System.out.println("Added promoter #" + p.getId() + " " + p.getName() + ".");
                    }
                    case "3" -> printPromoters(service.promotersByName(prompt("Name contains: ")));
                    case "4" -> {
                        service.deletePromoter(promptLong("Promoter id to delete: "));
                        System.out.println("Deleted.");
                    }
                    case "0" -> back = true;
                    default -> System.out.println("Unknown option.");
                }
            } catch (NotFoundException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printPromoters(List<Promoter> promoters) {
        if (promoters.isEmpty()) { System.out.println("No promoters found."); return; }
        for (Promoter p : promoters) System.out.println(p.getId() + " - " + p.getName());
    }

    // ===== Display + input helpers =====
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

    private int promptInt(String label) {
        while (true) {
            try { return Integer.parseInt(prompt(label)); }
            catch (NumberFormatException e) { System.out.println("Please enter a whole number."); }
        }
    }

    private long promptLong(String label) {
        while (true) {
            try { return Long.parseLong(prompt(label)); }
            catch (NumberFormatException e) { System.out.println("Please enter a whole number."); }
        }
    }

    private double promptDouble(String label) {
        while (true) {
            try { return Double.parseDouble(prompt(label)); }
            catch (NumberFormatException e) { System.out.println("Please enter a number."); }
        }
    }
}