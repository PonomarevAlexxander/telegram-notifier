package edu.java.scrapper.client;

import edu.java.scrapper.domain.Link;
import edu.java.scrapper.domain.LinkUpdate;
import java.net.URI;

public interface UpdateHandler {
    LinkUpdate getUpdate(Link link);

    boolean supports(URI link);
}
