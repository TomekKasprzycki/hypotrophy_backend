package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.UserDtoConverter;
import pl.hipotrofia.dto.UserDto;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.RoleService;
import pl.hipotrofia.services.UserService;

import java.util.List;

@RestController
@RequestMapping("api/users")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    private final UserService userService;
    private final UserDtoConverter userDtoConverter;
    private final RoleService roleService;

    public UserController(UserService userService,
                          UserDtoConverter userDtoConverter,
                          RoleService roleService) {
        this.userDtoConverter = userDtoConverter;
        this.userService = userService;
        this.roleService=roleService;
    }

    @GetMapping("/anonymous/getAll/{limit}/{offset}")
    public List<UserDto> getAll(@PathVariable int limit, @PathVariable int offset) {

        return userDtoConverter.convertToDto(userService.getAllLimited(limit, offset));
    }

    @DeleteMapping("/delete")
    public boolean delete(@RequestParam Long id) {

        try {
            User user = userService.findUserById(id);
            userService.delete(user);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @PostMapping("/edit")
    public boolean edit(@RequestBody UserDto userDto) {

        try {
            User user = userService.findUserById(userDto.getId());
            user.setActive(userDto.isActive());
            user.setRole(roleService.findByName(userDto.getRoleName()));
            user.setName(userDto.getName());

            userService.save(user);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
