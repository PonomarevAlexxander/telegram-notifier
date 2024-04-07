package edu.java.bot.service;

import edu.java.bot.dto.LinkUpdateDTO;

public interface UpdateService {
    void processUpdates(LinkUpdateDTO update);
}
