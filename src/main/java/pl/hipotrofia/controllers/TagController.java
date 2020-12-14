package pl.hipotrofia.controllers;

import org.hibernate.HibernateException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.TagDtoConverter;
import pl.hipotrofia.dto.TagDto;
import pl.hipotrofia.entities.Tag;
import pl.hipotrofia.services.TagService;

import javax.servlet.http.HttpServletResponse;
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
    public List<TagDto> getAllTags() {
        return tagDtoConverter.convertToDto(tagService.getAll());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public TagDto addTag(@RequestBody TagDto tagDto, HttpServletResponse response) {

        Tag tag = tagDtoConverter.convertFromDto(tagDto);

        try {
            tag = tagService.addTag(tag);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(404);
        }

        return tagDtoConverter.convertToDto(tag);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public void deleteTag(@RequestBody TagDto tagDto, HttpServletResponse response) {

        try {
            tagService.deleteTag(tagDtoConverter.convertFromDto(tagDto));
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit")
    public List<TagDto> editTag(@RequestBody TagDto tagDto) {

        try {
            Tag tag = tagDtoConverter.convertFromDto(tagDto);
            tagService.addTag(tag);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return tagDtoConverter.convertToDto(tagService.getAll());
    }

}
