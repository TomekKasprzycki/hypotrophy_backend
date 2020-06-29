package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.TagDto;
import pl.hipotrofia.entities.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagDtoConverter {

    public TagDto convertToDto(Tag tag){

        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());

        return tagDto;
    }

    public Tag convertFromDto(TagDto tagDto){
        Tag tag = new Tag();
        tag.setId(tagDto.getId());
        tag.setName(tagDto.getName());
        return tag;
    }

    public List<TagDto> convertToDto(List<Tag> tags){

        return tags.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
