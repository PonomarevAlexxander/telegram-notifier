package edu.java.scrapper.controller.dto;

import edu.java.scrapper.domain.Link;

public record LinkResponse(
    Long id,
    String url
) {
    public static LinkResponse fromLink(Link link) {
        return new LinkResponse(link.id(), link.resource().toString());
    }
}
