package edu.java.scrapper.domain;

import java.net.URI;
import java.time.OffsetDateTime;
import lombok.Data;

/**
 * Tracked link
 *
 * @author Alexander Ponomarev
 */
@Data
public class Link {
    private final Long id;
    private final URI uri;
    private final OffsetDateTime lastTracked;
}
