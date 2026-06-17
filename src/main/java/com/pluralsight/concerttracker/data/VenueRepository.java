
package com.pluralsight.concerttracker.data;
import com.pluralsight.concerttracker.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByCityIgnoreCase(String city);
    List<Venue> findByNameContainingIgnoreCase(String name);
    List<Venue> findByCapacityGreaterThanEqual(int capacity);
}
