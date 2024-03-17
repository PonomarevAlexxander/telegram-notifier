package edu.java.scrapper.service.impl;

import edu.java.scrapper.domain.jpa.Chat;
import edu.java.scrapper.domain.jpa.Link;
import edu.java.scrapper.exception.ResourceAlreadyExistException;
import edu.java.scrapper.exception.ResourceNotExistException;
import edu.java.scrapper.repository.jpa.JpaChatRepository;
import edu.java.scrapper.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.service.ChatService;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleStateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;
    private final JpaLinkRepository linkRepository;

    @Override
    @Transactional
    public void register(Long chatId) {
        Chat chat = new Chat();
        chat.setId(chatId);
        try {
            chatRepository.save(chat);
        } catch (DataIntegrityViolationException e) {
            throw new ResourceAlreadyExistException("Chat already registered", e);
        }
    }

    @Override
    @Transactional
    public void delete(Long chatId) {
        try {
            chatRepository.deleteById(chatId);
        } catch (StaleStateException e) {
            throw new ResourceNotExistException(String.format("Chat with id %d isn't registered", chatId), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<edu.java.scrapper.domain.Chat> getAllByUrl(String uri) {
        Optional<Link> link = linkRepository.getLinkByUri(uri);
        return link.map(value -> value.getTrackingChats().stream()
                .map(chat -> new edu.java.scrapper.domain.Chat(chat.getId()))
                .toList()).orElseGet(LinkedList::new);
    }
}
