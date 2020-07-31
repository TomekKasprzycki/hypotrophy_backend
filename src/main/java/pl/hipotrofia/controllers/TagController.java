package pl.hipotrofia.controllers;

import org.hibernate.HibernateException;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/anonymous/getAll")
    public List<TagDto> getAllTags(){

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public boolean addTag(@RequestBody TagDto tagDto){

        boolean result = false;

        Tag tag = tagDtoConverter.convertFromDto(tagDto);

        try {
            tagService.addTag(tag);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete}")
    public boolean deleteTag(@RequestBody TagDto tagDto){

        boolean result = false;

        try {
            tagService.deleteTag(tagDtoConverter.convertFromDto(tagDto));
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit}")
    public List<TagDto> editTag(@RequestBody TagDto tagDto){

        try {
            Tag tag = tagDtoConverter.convertFromDto(tagDto);;
            tagService.addTag(tag);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

}
