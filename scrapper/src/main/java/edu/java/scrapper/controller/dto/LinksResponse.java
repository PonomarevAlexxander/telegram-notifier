package edu.java.scrapper.controller.dto;

import edu.java.scrapper.domain.Link;
import java.util.List;

public record LinksResponse(
    List<LinkResponse> links,
    Integer size
) {
    public static LinksResponse fromLinks(List<Link> links) {
        return new LinksResponse(
            links.stream()
                .map(LinkResponse::fromLink)
                .toList(),
            links.size()
        );
    }
}
