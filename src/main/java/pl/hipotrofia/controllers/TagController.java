package pl.hipotrofia.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.TagDtoConverter;
import pl.hipotrofia.dto.TagDto;
import pl.hipotrofia.entities.Tag;
import pl.hipotrofia.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;
    @Autowired
    private TagDtoConverter tagDtoConverter;

    @GetMapping("/getAllTags")
    public List<TagDto> getAllTags(){
        return tagDtoConverter.convertToDto(tagService.getAll());
    }

    @GetMapping("/addTag")
    public List<TagDto> addTag(@RequestParam String tagName){

        Tag tag = new Tag();
        tag.setName(tagName);
        tagService.addTag(tag);

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

    @DeleteMapping("/deleteTag")
    public List<TagDto> deleteTag(@RequestBody TagDto tagDto){

        tagService.deleteTag(tagDtoConverter.convertFromDto(tagDto));

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

}
