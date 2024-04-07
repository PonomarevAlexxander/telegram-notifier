package edu.java.scrapper.service;

import edu.java.scrapper.dto.LinkUpdateDTO;

public interface UpdatePushService {
    void sendUpdate(LinkUpdateDTO update);
}
