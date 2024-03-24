package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.domain.jpa.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends ListCrudRepository<Link, Long> {
    boolean existsByUri(String uri);

    Optional<Link> getLinkByUri(String uri);

    List<Link> findAllByLastTrackedIsBefore(OffsetDateTime lastTracked);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Link l set l.lastTracked = :time where l.id in (:id)")
    void updateLinksLastTracked(List<Long> id, OffsetDateTime time);
}
