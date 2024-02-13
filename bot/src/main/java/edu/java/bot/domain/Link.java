package edu.java.bot.domain;

import java.net.URI;
import java.time.LocalDateTime;

public record Link(Long id, Long userId, URI resource, LocalDateTime lastTracked) {
}
