package edu.java.resilience.dto;

import java.util.List;

public record LinksResponse(
    List<LinkResponse> links,
    Integer size
) {
}
