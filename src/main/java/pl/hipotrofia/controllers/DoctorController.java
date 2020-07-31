package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.DoctorDtoConverter;
import pl.hipotrofia.dto.DoctorDto;
import pl.hipotrofia.services.DoctorService;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorDtoConverter doctorDtoConverter;
    private final DoctorService doctorService;

    public DoctorController(DoctorDtoConverter doctorDtoConverter, DoctorService doctorService) {
        this.doctorDtoConverter=doctorDtoConverter;
        this.doctorService=doctorService;
    }

    @GetMapping("/anonymous/getAll/{limit}/{offset}")
    public List<DoctorDto> getAll(@PathVariable int limit, @PathVariable int offset) {

        return doctorDtoConverter.convertToDto(doctorService.getAll(limit, offset));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public boolean addDoctor(@RequestBody DoctorDto doctorDto) {

        boolean result = false;

        try {
            doctorService.add(doctorDtoConverter.convertFromDto(doctorDto));
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

}
