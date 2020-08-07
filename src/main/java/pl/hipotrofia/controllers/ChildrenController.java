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
            return childrenDtoConverter.convertToDto(childrenService.getUserChildren(parentId));
        } else {
            response.setHeader("ERROR", "Brak zgodności dziecka/dzieci z rodzicem");
            return null;
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public boolean addChild(@RequestParam ChildrenDto childDto, HttpServletResponse response) {

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
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("ERROR", "Brak zgodności dziecka/dzieci z rodzicem");
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
        } catch (Exception ex) {
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

}
