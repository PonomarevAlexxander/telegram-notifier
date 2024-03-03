package edu.java.bot.domain;

import java.net.URI;

public record LinkUpdate(
    URI url,
    String description
) {
}
