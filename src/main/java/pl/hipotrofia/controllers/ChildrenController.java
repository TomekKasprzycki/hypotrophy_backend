package pl.hipotrofia.controllers;

import org.hibernate.HibernateException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @GetMapping("/byUser")
    public List<ChildrenDto> getUserChildren(@RequestParam Long parentId) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();


        return childrenDtoConverter.convertToDto(childrenService.getUserChildren(parentId));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public boolean addChild(@RequestParam ChildrenDto childDto) {

        boolean result = false;
        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Children child = childrenDtoConverter.convertFromDto(childDto);

        try {
            User parent = userService.findUserById(childDto.getParent());
            if (parent.getEmail().equals(userName)) {
                child.setUser(parent);
                childrenService.addChild(child);
                result = true;
            }
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }


        return result;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public boolean removeChild(@RequestBody ChildrenDto childDto) {

        boolean result = false;
        Children child = childrenDtoConverter.convertFromDto(childDto);
        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        try {
            if (userName.equals(userService.findUserById(childDto.getParent()).getEmail())) {
                childrenService.removeChild(child);
                result = true;
            }
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return result;

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit")
    public boolean edit(@RequestBody ChildrenDto childDto) {

        boolean result = false;
        Children child = childrenDtoConverter.convertFromDto(childDto);
        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        try {
            if (userName.equals(userService.findUserById(childDto.getParent()).getEmail())) {
                childrenService.addChild(child);
                result = true;
            }
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return result;
    }

}
