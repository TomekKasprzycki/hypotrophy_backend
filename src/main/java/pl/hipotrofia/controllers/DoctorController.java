package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.DoctorDtoConverter;
import pl.hipotrofia.dto.DoctorDto;
import pl.hipotrofia.entities.Doctor;
import pl.hipotrofia.services.DoctorService;
import pl.hipotrofia.services.MailingService;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    private final DoctorDtoConverter doctorDtoConverter;
    private final DoctorService doctorService;
    private final MailingService mailingService;

    public DoctorController(DoctorDtoConverter doctorDtoConverter,
                            DoctorService doctorService,
                            MailingService mailingService) {
        this.doctorDtoConverter=doctorDtoConverter;
        this.doctorService=doctorService;
        this.mailingService=mailingService;
    }

    @GetMapping("/anonymous/getAll")
    public List<DoctorDto> getAll(@RequestParam int limit, @RequestParam int offset) {

        return doctorDtoConverter.convertToDto(doctorService.getAll(limit, offset));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PUBLISHER')")
    @PostMapping("/add")
    public void addDoctor(@RequestBody DoctorDto doctorDto, HttpServletResponse response) {

        try {
            doctorService.add(doctorDtoConverter.convertFromDto(doctorDto));
            response.setStatus(200);

            String subject = "Dodano lekarza";
            String contents = "Dodano nowego lekarza: " + doctorDto.getFirstName() + " " + doctorDto.getLastName() + ".<br/>"
                    + "Sprawdź ten wpis!";

            mailingService.sendEmailToAdmin(response, subject,contents);

        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
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
