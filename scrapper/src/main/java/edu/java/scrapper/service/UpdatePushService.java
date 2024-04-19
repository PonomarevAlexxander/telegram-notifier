package edu.java.scrapper.service;

import edu.java.resilience.dto.LinkUpdateRequest;

public interface UpdatePushService {
    void sendUpdate(LinkUpdateRequest update);
}
