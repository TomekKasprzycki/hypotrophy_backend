package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.converters.SpecializationDtoConverter;
import pl.hipotrofia.dto.SpecializationDto;
import pl.hipotrofia.services.SpecializationService;

import java.util.List;

@RestController
@RequestMapping("/api/specialization")
public class SpecializationController {

    private final SpecializationService specializationService;
    private final SpecializationDtoConverter specializationDtoConverter;

    public SpecializationController(SpecializationService specializationService,
                                    SpecializationDtoConverter specializationDtoConverter) {
        this.specializationService=specializationService;
        this.specializationDtoConverter=specializationDtoConverter;
    }

    @GetMapping("/anonymous/getAll")
    public List<SpecializationDto> getAll() {
        return specializationDtoConverter.convertToDto(specializationService.getAll());
    }

}
