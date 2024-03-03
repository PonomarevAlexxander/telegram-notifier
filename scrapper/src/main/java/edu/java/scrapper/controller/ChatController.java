package edu.java.scrapper.controller;

import edu.java.scrapper.controller.dto.ChatResponse;
import edu.java.scrapper.service.ChatService;
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
    public ChatResponse register(@PathVariable Long id) {
//        service.register(id);
        return new ChatResponse("Chat successfully registered");
    }

    @DeleteMapping("/{id}")
    public ChatResponse delete(@PathVariable Long id) {
        //        service.delete(id);
        return new ChatResponse("Chat successfully deleted");
    }
}
