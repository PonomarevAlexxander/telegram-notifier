package edu.java.scrapper.controller;

import edu.java.resilience.dto.ChatResponse;
import edu.java.scrapper.service.ChatService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService service;

    @PostMapping("/{id}")
    public ChatResponse register(@Positive @PathVariable Long id) {
        service.register(id);
        return new ChatResponse("Chat successfully registered");
    }

    @DeleteMapping("/{id}")
    public ChatResponse delete(@Positive @PathVariable Long id) {
        service.delete(id);
        return new ChatResponse("Chat successfully deleted");
    }
}
