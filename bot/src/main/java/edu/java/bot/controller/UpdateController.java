package edu.java.bot.controller;

import edu.java.bot.domain.LinkUpdate;
import edu.java.bot.service.UpdateService;
import edu.java.resilience.dto.LinkUpdateRequest;
import edu.java.resilience.dto.LinkUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateService updateService;

    @PostMapping
    public LinkUpdateResponse processUpdate(@RequestBody LinkUpdateRequest body) {
        updateService.processUpdates(new LinkUpdate(body.url(), body.description()), body.tgChatIds());
        return new LinkUpdateResponse("Update successfully processed");
    }
}
