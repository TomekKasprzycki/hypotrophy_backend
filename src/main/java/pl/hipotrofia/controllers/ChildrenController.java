package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.ChildrenDtoConverter;
import pl.hipotrofia.dto.ChildrenDto;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.ChildrenService;
import pl.hipotrofia.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
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
    public List<ChildrenDto> getUserChildren(@RequestParam Long parentId, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<ChildrenDto> childrenDtoList = new ArrayList<>();

        if (userName.equals(userService.findUserById(parentId).getEmail())) {
            try {
                childrenDtoList = childrenDtoConverter.convertToDto(childrenService.getUserChildren(parentId));
            } catch (NullPointerException ex){
                response.setStatus(404);
                response.setHeader("ERROR", ex.getMessage());
            }
        } else {
            response.setHeader("ERROR", "Brak zgodności dziecka/dzieci z rodzicem");
            response.setStatus(405);
        }

        return childrenDtoList;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public void addChild(@RequestParam ChildrenDto childDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        try {
            User parent = userService.findUserById(childDto.getParent());
            if (parent.getEmail().equals(userName)) {
                Children child = childrenDtoConverter.convertFromDto(childDto);
                child.setUser(parent);
                childrenService.addChild(child);
                response.setStatus(201);
            } else {
                response.setStatus(404);
                response.setHeader("ERROR", "Brak zgodności dziecka z rodzicem");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public void removeChild(@RequestBody ChildrenDto childDto, HttpServletResponse response) {

        Children child = childrenDtoConverter.convertFromDto(childDto);
        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        try {
            if (userName.equals(userService.findUserById(childDto.getParent()).getEmail())) {
                childrenService.removeChild(child);
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.setHeader("ERROR", "Brak zgodności dziecka/dzieci z rodzicem");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit")
    public void edit(@RequestBody ChildrenDto childDto, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        try {
            if (userName.equals(userService.findUserById(childDto.getParent()).getEmail())) {
                Children child = childrenDtoConverter.convertFromDto(childDto);
                childrenService.addChild(child);
                response.setStatus(200);
            } else {
                response.setStatus(404);
                response.setHeader("ERROR", "Brak zgodności dziecka/dzieci z rodzicem");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }
    }

}
