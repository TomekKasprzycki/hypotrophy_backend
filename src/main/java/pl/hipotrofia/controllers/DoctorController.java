package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.DoctorDtoConverter;
import pl.hipotrofia.dto.DoctorDto;
import pl.hipotrofia.entities.Doctor;
import pl.hipotrofia.services.DoctorService;

import javax.servlet.http.HttpServletResponse;
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
    public void addDoctor(@RequestBody DoctorDto doctorDto, HttpServletResponse response) {

        try {
            doctorService.add(doctorDtoConverter.convertFromDto(doctorDto));
            response.setStatus(200);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/edit")
    public void editDoctor(@RequestBody DoctorDto doctorDto, HttpServletResponse response) {

        try {
            doctorService.add(doctorDtoConverter.convertFromDto(doctorDto));
            response.setStatus(201);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
        }

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @DeleteMapping("/delete")
    public void deleteDoctor(@RequestParam Long id, HttpServletResponse response) {

        try {
            Doctor doctor = doctorService.getById(id).orElseThrow(NullPointerException::new);
            doctorService.remove(doctor);
            response.setStatus(200);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
        }
    }

}
