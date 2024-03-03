package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.LinkRequest;
import edu.java.scrapper.controller.dto.LinkResponse;
import edu.java.scrapper.controller.dto.LinksResponse;
import edu.java.scrapper.domain.Link;
import edu.java.scrapper.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {
    private final LinkService service;

    @GetMapping
    public LinksResponse getAllTracked(@RequestHeader("Tg-Chat-Id") Long chatId) {
        List<Link> links = service.getAll(chatId);
        return LinksResponse.fromLinks(links);
    }

    @PostMapping
    public LinkResponse trackNew(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody LinkRequest link) {
//        Link newLink = service.trackNew(chatId, link.url());
        return new LinkResponse(1L, "");
    }

    @DeleteMapping
    public LinkResponse untrackLink(@RequestHeader("Tg-Chat-Id") Long chatId, @RequestBody LinkRequest link) {
        return new LinkResponse(1L, "");
    }
}
