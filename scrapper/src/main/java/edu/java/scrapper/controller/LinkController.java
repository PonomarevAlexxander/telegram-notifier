package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.LinkRequest;
import edu.java.scrapper.controller.dto.LinksResponse;
import edu.java.scrapper.dto.LinkDTO;
import edu.java.scrapper.service.LinkService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
    public LinksResponse getAllTracked(@Positive @RequestHeader("Tg-Chat-Id") Long chatId) {
        List<LinkDTO> links = service.getAllByChatId(chatId);
        return new LinksResponse(links, links.size());
    }

    @PostMapping
    public LinkDTO trackNew(@Positive @RequestHeader("Tg-Chat-Id") Long chatId, @Valid @RequestBody LinkRequest link) {
        Long id = service.trackNew(chatId, link.url());
        return new LinkDTO(id, link.url());
    }

    @DeleteMapping
    public LinkDTO untrackLink(
        @Positive @RequestHeader("Tg-Chat-Id") Long chatId,
        @Valid @RequestBody LinkRequest link
    ) {
        Long id = service.untrack(chatId, link.url());
        return new LinkDTO(id, link.url());
    }
}
