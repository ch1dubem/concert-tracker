package com.pluralsight.concerttracker.service;

import com.pluralsight.concerttracker.data.ArtistRepository;
import com.pluralsight.concerttracker.data.ConcertRepository;
import com.pluralsight.concerttracker.data.PromoterRepository;
import com.pluralsight.concerttracker.data.VenueRepository;
import com.pluralsight.concerttracker.models.Artist;
import com.pluralsight.concerttracker.models.Concert;
import com.pluralsight.concerttracker.models.Promoter;
import com.pluralsight.concerttracker.models.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertTrackerService {

    private final VenueRepository venueRepository;
    private final ArtistRepository artistRepository;
    private final PromoterRepository promoterRepository;
    private final ConcertRepository concertRepository;

    @Autowired
    public ConcertTrackerService(VenueRepository venueRepository,
                                 ArtistRepository artistRepository,
                                 PromoterRepository promoterRepository,
                                 ConcertRepository concertRepository) {
        this.venueRepository = venueRepository;
        this.artistRepository = artistRepository;
        this.promoterRepository = promoterRepository;
        this.concertRepository = concertRepository;
    }

    // ===== Venues =====
    public List<Venue> allVenues() { return venueRepository.findAll(); }

    public Venue venueById(long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No venue with id " + id));
    }

    public Venue addVenue(String name, String city, int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative.");
        return venueRepository.save(new Venue(name, city, capacity));
    }

    public Venue updateVenueCapacity(long id, int capacity) {
        if (capacity < 0) throw new IllegalArgumentException("Capacity cannot be negative.");
        Venue venue = venueById(id);
        venue.setCapacity(capacity);
        return venueRepository.save(venue);
    }

    public void deleteVenue(long id) {
        if (!venueRepository.existsById(id)) throw new NotFoundException("No venue with id " + id);
        venueRepository.deleteById(id);
    }

    public List<Venue> venuesByCity(String city) { return venueRepository.findByCity(city); }
    public List<Venue> venuesByName(String name) { return venueRepository.findByName(name); }
    public List<Venue> venuesByMinCapacity(int capacity) { return venueRepository.findByCapacity(capacity); }

    // ===== Artists =====
    public List<Artist> allArtists() { return artistRepository.findAll(); }

    public Artist artistById(long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No artist with id " + id));
    }

    public Artist addArtist(String name, String genre) { return artistRepository.save(new Artist(name, genre)); }

    public Artist updateArtistGenre(long id, String genre) {
        Artist artist = artistById(id);
        artist.setGenre(genre);
        return artistRepository.save(artist);
    }

    public void deleteArtist(long id) {
        if (!artistRepository.existsById(id)) throw new NotFoundException("No artist with id " + id);
        artistRepository.deleteById(id);
    }

    public List<Artist> artistsByGenre(String genre) { return artistRepository.findByGenre(genre); }
    public List<Artist> artistsByName(String name) { return artistRepository.findByName(name); }

    // ===== Promoters =====
    public List<Promoter> allPromoters() { return promoterRepository.findAll(); }

    public Promoter addPromoter(String name) { return promoterRepository.save(new Promoter(name)); }

    public void deletePromoter(long id) {
        if (!promoterRepository.existsById(id)) throw new NotFoundException("No promoter with id " + id);
        promoterRepository.deleteById(id);
    }

    public List<Promoter> promotersByName(String name) { return promoterRepository.findByName(name); }

    private Promoter promoterById(long id) {
        return promoterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No promoter with id " + id));
    }

    // ===== Concerts =====
    public long countConcerts() { return concertRepository.count(); }
    public List<Concert> allConcerts() { return concertRepository.findAll(); }

    public Concert concertById(long id) {
        return concertRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No concert with id " + id));
    }

    public Concert addConcert(long artistId, long venueId, long promoterId,
                              int year, double price, int ticketsSold) {
        Artist artist = artistById(artistId);
        Venue venue = venueById(venueId);
        Promoter promoter = promoterById(promoterId);
        if (price < 0) throw new IllegalArgumentException("Ticket price cannot be negative.");
        if (ticketsSold < 0) throw new IllegalArgumentException("Tickets sold cannot be negative.");
        if (ticketsSold > venue.getCapacity())
            throw new IllegalArgumentException("Tickets sold (" + ticketsSold
                    + ") cannot exceed the venue capacity (" + venue.getCapacity() + ").");
        return concertRepository.save(new Concert(year, price, ticketsSold, artist, venue, promoter));
    }

    public Concert updateConcertPrice(long id, double price) {
        if (price < 0) throw new IllegalArgumentException("Ticket price cannot be negative.");
        Concert concert = concertById(id);
        concert.setTicketPrice(price);
        return concertRepository.save(concert);
    }

    public Concert updateTicketsSold(long id, int ticketsSold) {
        Concert concert = concertById(id);
        if (ticketsSold < 0) throw new IllegalArgumentException("Tickets sold cannot be negative.");
        if (ticketsSold > concert.getVenue().getCapacity())
            throw new IllegalArgumentException("Tickets sold (" + ticketsSold
                    + ") cannot exceed the venue capacity (" + concert.getVenue().getCapacity() + ").");
        concert.setTicketsSold(ticketsSold);
        return concertRepository.save(concert);
    }

    public void deleteConcert(long id) {
        if (!concertRepository.existsById(id)) throw new NotFoundException("No concert with id " + id);
        concertRepository.deleteById(id);
    }

    // ===== Searches =====
    public List<Concert> byYear(int year) { return concertRepository.findByConcertYear(year); }
    public List<Concert> byArtist(String name) { return concertRepository.searchByArtistName(name); }
    public List<Concert> byVenue(String name) { return concertRepository.searchByVenueName(name); }
    public List<Concert> byCity(String city) { return concertRepository.searchByCity(city); }
    public List<Concert> byMaxPrice(double maxPrice) { return concertRepository.findByTicketPriceLessThanEqual(maxPrice); }
    public List<Concert> byPriceRange(double min, double max) { return concertRepository.findByTicketPriceBetween(min, max); }
    public List<Concert> advancedSearch(double maxPrice, int minYear) { return concertRepository.search(maxPrice, minYear); }

    // ===== Reports =====
    public List<Object[]> revenuePerVenue() { return concertRepository.revenuePerVenue(); }

    public Object[] busiestVenue() {
        List<Object[]> rows = concertRepository.venueConcertCounts();
        return rows.isEmpty() ? null : rows.get(0);
    }

    public Object[] busiestArtist() {
        List<Object[]> rows = concertRepository.artistConcertCounts();
        return rows.isEmpty() ? null : rows.get(0);
    }

    public List<Object[]> averagePriceByYear() { return concertRepository.averagePriceByYear(); }
    // ===== Seeding =====
    public void seedIfEmpty() {
        if (concertRepository.count() > 0) {
            return;
        }
        Venue msg = venueRepository.save(new Venue("Madison Square Garden", "New York", 20000));
        Venue forum = venueRepository.save(new Venue("The Forum", "Los Angeles", 17500));
        Venue redRocks = venueRepository.save(new Venue("Red Rocks Amphitheatre", "Denver", 9525));
        Venue houseOfBlues = venueRepository.save(new Venue("House of Blues", "Chicago", 1300));
        Venue stubbs = venueRepository.save(new Venue("Stubb's", "Austin", 2200));

        Artist strokes = artistRepository.save(new Artist("The Strokes", "Rock"));
        Artist dua = artistRepository.save(new Artist("Dua Lipa", "Pop"));
        Artist kamasi = artistRepository.save(new Artist("Kamasi Washington", "Jazz"));
        Artist kendrick = artistRepository.save(new Artist("Kendrick Lamar", "Hip-Hop"));
        Artist tameImpala = artistRepository.save(new Artist("Tame Impala", "Rock"));
        Artist sza = artistRepository.save(new Artist("SZA", "Pop"));

        Promoter liveNation = promoterRepository.save(new Promoter("Live Nation"));
        Promoter aeg = promoterRepository.save(new Promoter("AEG Presents"));
        Promoter local = promoterRepository.save(new Promoter("Goldenvoice"));

        concertRepository.save(new Concert(2022, 89.50, 18000, strokes, msg, liveNation));
        concertRepository.save(new Concert(2022, 120.00, 17500, dua, forum, aeg));
        concertRepository.save(new Concert(2022, 65.00, 9525, kamasi, redRocks, local));
        concertRepository.save(new Concert(2023, 150.00, 20000, kendrick, msg, liveNation));
        concertRepository.save(new Concert(2023, 95.00, 8800, tameImpala, redRocks, local));
        concertRepository.save(new Concert(2023, 45.00, 1300, sza, houseOfBlues, liveNation));
        concertRepository.save(new Concert(2023, 110.00, 16200, dua, forum, aeg));
        concertRepository.save(new Concert(2024, 99.00, 2200, strokes, stubbs, local));
        concertRepository.save(new Concert(2024, 135.00, 19500, kendrick, msg, liveNation));
        concertRepository.save(new Concert(2024, 70.00, 9000, kamasi, redRocks, local));
        concertRepository.save(new Concert(2025, 125.00, 17500, sza, forum, aeg));
        concertRepository.save(new Concert(2025, 80.00, 2200, tameImpala, stubbs, local));
    }
}