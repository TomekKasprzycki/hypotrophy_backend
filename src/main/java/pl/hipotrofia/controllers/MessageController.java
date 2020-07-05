package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.MessageDtoConverter;
import pl.hipotrofia.dto.MessageDto;
import pl.hipotrofia.entities.Message;
import pl.hipotrofia.services.ArticlesService;
import pl.hipotrofia.services.MessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final MessageDtoConverter messageDtoConverter;
    private final ArticlesService articlesService;

    public MessageController(MessageDtoConverter messageDtoConverter,
                             MessageService messageService,
                             ArticlesService articlesService) {
        this.messageDtoConverter = messageDtoConverter;
        this.messageService = messageService;
        this.articlesService = articlesService;
    }

    @GetMapping("/byArticle")
    public List<MessageDto> getMessagesByArticle(@RequestParam Long id) {

        return messageDtoConverter.convertToDto(messageService.findAllByArticle(id));
    }

    @PostMapping("/addMessage")
    public List<MessageDto> addMessage(@RequestBody MessageDto messageDto) {

        Message message = messageDtoConverter.convertFromDto(messageDto);
        message.setArticle(articlesService.findArticleById(messageDto.getArticleId()));
        messageService.addMessage(message);

        return messageDtoConverter.convertToDto(messageService.findAllByArticle(messageDto.getArticleId()));
    }

    @DeleteMapping("/deleteMessage")
    public List<MessageDto> deleteMessage(@RequestBody MessageDto messageDto) {

        messageService.deleteMessage(messageDtoConverter.convertFromDto(messageDto));

        return messageDtoConverter.convertToDto(messageService.findAllByArticle(messageDto.getArticleId()));
    }

}
