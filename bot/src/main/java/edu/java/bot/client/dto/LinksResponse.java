package edu.java.bot.client.dto;

import java.util.List;

public record LinksResponse(
    List<LinkResponse> links,
    Integer size
) {
}
