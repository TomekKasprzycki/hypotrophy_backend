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

    @GetMapping("/anonymous/add")
    public boolean anonymousAdd() {
        boolean result = false;

        try {
            Children kid = new Children();
            User user = userService.findUserById(23L);
            kid.setUser(user);
            kid.setDateOfBirth(null);
            kid.setGender(Children.Gender.FEMALE);
            kid.setName("Jasiu");
            childrenService.addChild(kid);
            result = true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    @GetMapping("/anonymous/get")
    public List<ChildrenDto> anonymousGet() {
        return childrenDtoConverter.convertToDto(childrenService.getUserChildren(23L));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @GetMapping("/byUser")
    public List<ChildrenDto> getUserChildren(@RequestParam Long parentId, HttpServletResponse response) {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (userName.equals(userService.findUserById(parentId).getEmail())) {
            response.setStatus(200);
            return childrenDtoConverter.convertToDto(childrenService.getUserChildren(parentId));
        } else {
            response.setHeader("ERROR", "Brak zgodności dziecka/dzieci z rodzicem");
            response.setStatus(405);
            return null;
        }
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
                response.setHeader("ERROR", "Brak zgodności dziecka/dzieci z rodzicem");
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
