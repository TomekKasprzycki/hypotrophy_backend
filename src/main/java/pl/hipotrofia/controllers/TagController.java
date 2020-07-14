package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.TagDtoConverter;
import pl.hipotrofia.dto.TagDto;
import pl.hipotrofia.entities.Tag;
import pl.hipotrofia.services.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final TagDtoConverter tagDtoConverter;

    public TagController(TagService tagService, TagDtoConverter tagDtoConverter) {
        this.tagService = tagService;
        this.tagDtoConverter = tagDtoConverter;
    }

    @GetMapping("/getAll")
    public List<TagDto> getAllTags(){
        return tagDtoConverter.convertToDto(tagService.getAll());
    }

    @GetMapping("/add")
    public List<TagDto> addTag(@RequestParam String tagName){

        Tag tag = new Tag();
        tag.setName(tagName);
        tagService.addTag(tag);

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

    @DeleteMapping("/delete")
    public List<TagDto> deleteTag(@RequestBody TagDto tagDto){

        tagService.deleteTag(tagDtoConverter.convertFromDto(tagDto));

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

    @GetMapping("/edit")
    public List<TagDto> editTag(@RequestParam Long id, @RequestParam String tagName){

        Tag tag = tagService.findTagById(id);
        tag.setName(tagName);
        tagService.addTag(tag);

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

}
