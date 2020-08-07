package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MeasurementDto {

    private Long id;
    private Long childId;
    private Integer weight;
    private Integer height;
    private Integer headCircuit;
    private Date dateOfMeasure;

}
