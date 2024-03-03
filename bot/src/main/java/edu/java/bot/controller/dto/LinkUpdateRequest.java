package edu.java.bot.controller.dto;

import edu.java.bot.domain.LinkUpdate;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    Long id,
    URI url,
    String description,
    List<Long> tgChatIds
) {
    public LinkUpdate toLinkUpdate() {
        return new LinkUpdate(
            url,
            description
        );
    }
}
