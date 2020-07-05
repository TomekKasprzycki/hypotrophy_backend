package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.MessageDto;
import pl.hipotrofia.entities.Message;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageDtoConverter {

    public MessageDto convertToDto(Message message){

        MessageDto messageDto = new MessageDto();
        messageDto.setId(message.getId());
        messageDto.setContents(message.getContents());
        messageDto.setAuthor(message.getAuthor().getName());
        messageDto.setArticleId(message.getArticle().getId());
        messageDto.setCreated(message.getCreated());

        return messageDto;
    }

    public Message convertFromDto(MessageDto messageDto){

        Message message = new Message();
        message.setId(message.getId());
        message.setContents(messageDto.getContents());

        return message;
    }

    public List<MessageDto> convertToDto(List<Message> messages){

        return messages.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
