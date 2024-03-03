package edu.java.bot.controller;

import edu.java.bot.controller.dto.LinkUpdateRequest;
import edu.java.bot.controller.dto.LinkUpdateResponse;
import edu.java.bot.service.UpdateService;
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
        updateService.processUpdates(body.toLinkUpdate(), body.tgChatIds());
        return new LinkUpdateResponse("Update successfully processed");
    }
}
