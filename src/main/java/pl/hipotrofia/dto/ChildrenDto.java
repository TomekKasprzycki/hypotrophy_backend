package pl.hipotrofia.dto;

import lombok.Data;
import pl.hipotrofia.entities.Children;

import java.util.Date;

@Data
public class ChildrenDto {

    private Long id;
    private String name;
    private Long userId; //zmieniono z parent
    private Children.Gender gender;
    private Date dateOfBirth;

}
