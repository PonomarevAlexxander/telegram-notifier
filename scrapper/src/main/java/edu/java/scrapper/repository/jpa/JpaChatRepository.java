package edu.java.scrapper.repository.jpa;

import edu.java.scrapper.domain.jpa.Chat;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChatRepository extends ListCrudRepository<Chat, Long> {
}
