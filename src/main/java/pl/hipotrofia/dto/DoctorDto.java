package pl.hipotrofia.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DoctorDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String city;
    private String zip_code;
    private String region;
    private String street;
    private String building_number;
    private Map<Long, String> specialization;
    private double avgEvaluation;

}
