package edu.java.scrapper.service;

import edu.java.scrapper.domain.Link;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapperLinkService implements LinkService {
//    private final LinkRepository repository;

    @Override
    public List<Link> getAll(Long chatId) {
//        repository.getAllLByChatId(chatId)
        return new LinkedList<>();
    }

    @Override
    public Link trackNew(Long chatId, String url) {
        return null;
    }

    @Override
    public Link untrack(Long chatId, String url) {
        return null;
    }
}
