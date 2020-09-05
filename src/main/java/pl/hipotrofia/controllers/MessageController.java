package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.MessageDtoConverter;
import pl.hipotrofia.dto.MessageDto;
import pl.hipotrofia.entities.Message;
import pl.hipotrofia.services.ArticlesService;
import pl.hipotrofia.services.MessageService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN')")
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

    @GetMapping("/anonymous/byArticle")
    public List<MessageDto> getMessagesByArticle(@RequestParam Long id) {
        return messageDtoConverter.convertToDto(messageService.findAllByArticle(id));
    }

    @PostMapping("/add")
    public void addMessage(@RequestBody MessageDto messageDto, HttpServletResponse response) {

        try {
            Message message = messageDtoConverter.convertFromDto(messageDto);
            message.setArticle(articlesService.findArticleById(messageDto.getArticleId()));
            messageService.addMessage(message);
            response.setStatus(201);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(404);
            response.setHeader("ERROR", ex.getMessage());
        }

    }

    @DeleteMapping("/delete")
    public void deleteMessage(@RequestParam Long id, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();


        try {
            Message message = messageService.getById(id).orElseThrow(NullPointerException::new);
            if(role.equals("[ADMIN]") || userName.equals(message.getAuthor().getEmail())) {
            messageService.deleteMessage(message);
            response.setStatus(200); } else {
                response.setStatus(403);
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
            response.setStatus(404);
        }

    }

    @PostMapping("/edit")
    public void editMessage(@RequestBody MessageDto messageDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        try {
            Message message = messageDtoConverter.convertFromDto(messageDto);
            if(role.equals("[ADMIN]") || userName.equals(message.getAuthor().getEmail())) {
            message.setArticle(articlesService.findArticleById(messageDto.getArticleId()));
            messageService.addMessage(message);
            response.setStatus(200);} else {
                response.setStatus(403);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(404);
            response.setHeader("ERROR", ex.getMessage());
        }
    }

}
