package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class WeightDto {

    private Map<Date, Integer> measurements;

}
