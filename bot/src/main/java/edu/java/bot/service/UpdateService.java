package edu.java.bot.service;

import edu.java.bot.domain.LinkUpdate;
import java.util.List;

public interface UpdateService {
    void processUpdates(LinkUpdate update, List<Long> chatIds);
}
