package com.pluralsight.concerttracker.data;

import com.pluralsight.concerttracker.models.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    // Searches a method name can handle on its own.
    List<Concert> findByConcertYear(int year);
    List<Concert> findByTicketPriceLessThanEqual(double maxPrice);
    List<Concert> findByTicketPriceBetween(double minPrice, double maxPrice);

    // Searches that reach across a relationship or combine conditions need JPQL.
    @Query("SELECT c FROM Concert c WHERE LOWER(c.artist.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Concert> searchByArtistName(@Param("name") String name);

    @Query("SELECT c FROM Concert c WHERE LOWER(c.venue.name) = LOWER(:name)")
    List<Concert> searchByVenueName(@Param("name") String name);

    // Reaches through the venue to its city.
    @Query("SELECT c FROM Concert c WHERE LOWER(c.venue.city) = LOWER(:city)")
    List<Concert> searchByCity(@Param("city") String city);

    @Query("SELECT c FROM Concert c WHERE c.ticketPrice <= :maxPrice AND c.concertYear >= :minYear")
    List<Concert> search(@Param("maxPrice") double maxPrice, @Param("minYear") int minYear);

    // Reports: each row comes back as an Object[]; the service reads it by position.
    @Query("SELECT c.venue.name, SUM(c.ticketPrice * c.ticketsSold) FROM Concert c " +
            "GROUP BY c.venue.name ORDER BY SUM(c.ticketPrice * c.ticketsSold) DESC")
    List<Object[]> revenuePerVenue();

    @Query("SELECT c.venue.name, COUNT(c) FROM Concert c " +
            "GROUP BY c.venue.name ORDER BY COUNT(c) DESC")
    List<Object[]> venueConcertCounts();

    @Query("SELECT c.artist.name, COUNT(c) FROM Concert c " +
            "GROUP BY c.artist.name ORDER BY COUNT(c) DESC")
    List<Object[]> artistConcertCounts();

    @Query("SELECT c.concertYear, AVG(c.ticketPrice) FROM Concert c " +
            "GROUP BY c.concertYear ORDER BY c.concertYear")
    List<Object[]> averagePriceByYear();
}