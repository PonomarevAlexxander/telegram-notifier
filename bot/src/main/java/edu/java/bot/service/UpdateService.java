package edu.java.bot.service;

import edu.java.resilience.dto.LinkUpdateRequest;

public interface UpdateService {
    void processUpdates(LinkUpdateRequest update);
}
