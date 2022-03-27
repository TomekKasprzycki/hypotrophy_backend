package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ModificationDto {

    Long id;
    Date dateOfModification;
    String authorOfModification;

}
