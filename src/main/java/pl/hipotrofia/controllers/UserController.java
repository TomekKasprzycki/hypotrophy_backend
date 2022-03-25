package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.UserDtoConverter;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.ChildrenService;
import pl.hipotrofia.services.RoleService;
import pl.hipotrofia.services.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final RoleService roleService;
    private final ChildrenService childrenService;

    public UserController(UserService userService,
                          UserDtoConverter userDtoConverter,
                          RoleService roleService,
                          ChildrenService childrenService) {
        this.userDtoConverter = userDtoConverter;
        this.userService = userService;
        this.roleService = roleService;
        this.childrenService = childrenService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getAll")
    public List<UserDto> getAll(@RequestParam int limit, @RequestParam int offset) {

        return userDtoConverter.convertToDto(userService.getAllLimited(limit, offset));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id, HttpServletResponse response) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        final String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();

        try {
            User user = userService.findUserById(id);
            if (role.equals("[ROLE_ADMIN]") ||  user.getEmail().equals(email)) {

                List<Children> children = childrenService.getUserChildren(user.getId());
                children.forEach(childrenService::removeChild);

                userService.delete(user);
            } else {
                response.setStatus(405);
                response.setHeader("ERROR", "Something went wrong...");
            }
        } catch (Exception ex) {
            response.setHeader("ERROR", ex.getMessage());
            ex.printStackTrace();
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit")
    public void edit(@RequestBody UserDto userDto, HttpServletResponse response) {

        final String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if (email.equals(userDto.getEmail())) {
            try {
                User user = userService.findUserById(userDto.getId());
                user.setActive(userDto.isActive());
                user.setRole(roleService.findByName(userDto.getRoleName()));
                user.setName(userDto.getName());

                userService.save(user);
                response.setHeader("EDIT", "Success");
            } catch (Exception ex) {
                response.setHeader("ERROR", ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            response.setStatus(405);
            response.setHeader("ERROR", "Something went wrong...");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deactivate")
    public void deactivate(@RequestBody UserDto userDto, HttpServletResponse response) {

        Optional<User> optional = userService.findUserByEmail(userDto.getEmail());
        if (optional.isPresent()) {
            userService.deactivate(optional.get());
        } else {
            response.setHeader("ERROR", "USER NOT FOUND");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/activate")
    public void setActive(@RequestBody UserDto userDto, HttpServletResponse response) {


            Optional<User> optional = userService.findUserByEmail(userDto.getEmail());
            if (optional.isPresent()) {
                userService.activate(optional.get());
            } else {
                response.setHeader("ERROR", "USER NOT FOUND");
            }
    }

    @PostMapping("/anonymous/checkName")
    public boolean checkName(@RequestParam String name) {
        return userService.isNameTaken(name);
    }

}
