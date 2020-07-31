package pl.hipotrofia.dto;

import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String password2;
    private String roleId;
    
}
