package edu.java.scrapper.client;

import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;

public interface UpdateHandler {
    LinkUpdate getUpdate(Link link);

    boolean supports(String link);
}
