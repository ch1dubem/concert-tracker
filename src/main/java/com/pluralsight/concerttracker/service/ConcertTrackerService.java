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

// The menu talks only to this class. This class talks to the repositories.
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

    public long countConcerts() { return concertRepository.count(); }
    public List<Concert> allConcerts() { return concertRepository.findAll(); }
    public List<Venue> allVenues() { return venueRepository.findAll(); }
    public List<Artist> allArtists() { return artistRepository.findAll(); }
    public List<Promoter> allPromoters() { return promoterRepository.findAll(); }

    // Only seed when empty, so a second run doesn't pile on duplicates.
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
