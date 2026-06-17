
package com.pluralsight.concerttracker.data;
import com.pluralsight.concerttracker.models.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface VenueRepository extends JpaRepository<Venue, Long> {
    List<Venue> findByCity(String city);
    List<Venue> findByName(String name);
    List<Venue> findByCapacity(int capacity);
}
