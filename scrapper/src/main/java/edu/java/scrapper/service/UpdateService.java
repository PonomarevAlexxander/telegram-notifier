package edu.java.scrapper.service;

import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import java.util.List;

/**
 * Service that fetches updates from websites
 *
 * @author Alexander Ponomarev
 */
public interface UpdateService {
    boolean supports(String link);

    List<LinkUpdate> fetchUpdates(List<Link> toUpdate);
}
