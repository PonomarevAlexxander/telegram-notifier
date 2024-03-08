package edu.java.scrapper.domain;

import java.net.URI;
import java.time.OffsetDateTime;

/**
 * Tracked link
 *
 * @author Alexander Ponomarev
 */
public record Link(Long id, URI resource, OffsetDateTime lastTracked) {
}
