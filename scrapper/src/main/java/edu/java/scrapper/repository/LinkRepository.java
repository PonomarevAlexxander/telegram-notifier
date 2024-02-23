package edu.java.scrapper.repository;

import edu.java.scrapper.domain.Link;
import java.util.List;

/**
 * Repository that operates with tracked links
 *
 * @author Alexander Ponomarev
 */
public interface LinkRepository {
    List<Link> getAllLinks();
}
