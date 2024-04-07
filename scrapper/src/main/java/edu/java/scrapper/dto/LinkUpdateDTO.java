package edu.java.scrapper.dto;

import java.net.URI;
import java.util.List;

public record LinkUpdateDTO(
    Long id,
    URI url,
    String description,
    List<Long> tgChatIds
) {
}
