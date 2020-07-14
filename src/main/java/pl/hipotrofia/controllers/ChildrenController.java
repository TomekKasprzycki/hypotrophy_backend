package pl.hipotrofia.controllers;

import org.hibernate.HibernateException;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.ChildrenDtoConverter;
import pl.hipotrofia.dto.ChildrenDto;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.ChildrenService;
import pl.hipotrofia.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/children")
public class ChildrenController {

    private final ChildrenDtoConverter childrenDtoConverter;
    private final ChildrenService childrenService;
    private final UserService userService;

    public ChildrenController(ChildrenService childrenService,
                              ChildrenDtoConverter childrenDtoConverter,
                              UserService userService) {
        this.childrenDtoConverter = childrenDtoConverter;
        this.childrenService = childrenService;
        this.userService = userService;
    }

    @GetMapping("/byUser")
    public List<ChildrenDto> getUserChildren(@RequestParam Long parentId) {

        return childrenDtoConverter.convertToDto(childrenService.getUserChildren(parentId));
    }

    @PostMapping("/add")
    public List<ChildrenDto> addChild(@RequestParam ChildrenDto childDto) {

        Children child = childrenDtoConverter.convertFromDto(childDto);

        try {
            User parent = userService.findUserById(childDto.getParent());
            child.setUser(parent);
            childrenService.addChild(child);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return childrenDtoConverter.convertToDto(childrenService.getUserChildren(childDto.getParent()));
    }

    @DeleteMapping("/delete")
    public List<ChildrenDto> removeChild(@RequestBody ChildrenDto childDto) {
        Children child = childrenDtoConverter.convertFromDto(childDto);

        try {
            childrenService.removeChild(child);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return childrenDtoConverter.convertToDto(childrenService.getUserChildren(childDto.getParent()));

    }

    @PostMapping("/edit")
    public List<ChildrenDto> edit(@RequestBody ChildrenDto childDto){
        Children child = childrenDtoConverter.convertFromDto(childDto);

        try {
            childrenService.addChild(child);
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return childrenDtoConverter.convertToDto(childrenService.getUserChildren(childDto.getParent()));
    }

}
