package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Message;
import pl.hipotrofia.repositories.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public List<Message> findAllByArticle(Long id) {
        return messageRepository.findAllById(id);
    }

    public Message addMessage(Message message) {
        return messageRepository.save(message);
    }

    public void deleteMessage(Message message) {
    }

    public Optional<Message> getById(Long id) {
        return messageRepository.findById(id);
    }
}
