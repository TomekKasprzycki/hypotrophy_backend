package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MeasurementDto {

    private Long id;
    private Long childId;
    private Integer weight;
    private Double height; //czy mają być połówki cm
    private Double headCircuit; //czy mają być połówki cm
    private Date dateOfMeasure;

}
