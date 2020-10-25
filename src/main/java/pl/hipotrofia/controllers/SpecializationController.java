package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.SpecializationDtoConverter;
import pl.hipotrofia.dto.SpecializationDto;
import pl.hipotrofia.entities.Specialization;
import pl.hipotrofia.services.SpecializationService;

import javax.servlet.http.HttpServletResponse;
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

    @GetMapping("/anonymous/addSpecialization")
    public SpecializationDto addSpecialization(@RequestParam String name, HttpServletResponse response) {

        Specialization specialization = new Specialization();
        specialization.setNameOfSpecialization(name);

        try {
            specialization = specializationService.save(specialization);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("ERORR", ex.getMessage());
        }

        return specializationDtoConverter.convertToDto(specialization);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public void deleteSpecialization(@RequestBody SpecializationDto specializationDto, HttpServletResponse response) {

        try {
            Specialization specialization = specializationDtoConverter.convertFromDto(specializationDto);
            specializationService.remove(specialization);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/editSpecialization")
    public List<SpecializationDto> editSpecializations (@RequestBody SpecializationDto specializationDto,
                                                        HttpServletResponse response) {

        try {
            Specialization specialization = specializationDtoConverter.convertFromDto(specializationDto);
            specializationService.save(specialization);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setHeader("ERROR", ex.getMessage());
        }

        List<Specialization> specializations = specializationService.getAll();

        return specializationDtoConverter.convertToDto(specializations);
    }
}
