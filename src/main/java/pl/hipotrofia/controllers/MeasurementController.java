package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.MeasurementDtoConverter;
import pl.hipotrofia.dto.MeasurementDto;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.Measurement;
import pl.hipotrofia.services.ChildrenService;
import pl.hipotrofia.services.MeasurementService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/measurement")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementDtoConverter measurementDtoConverter;
    private final ChildrenService childrenService;

    public MeasurementController(MeasurementService measurementService,
                                 ChildrenService childrenService,
                                 MeasurementDtoConverter measurementDtoConverter) {
        this.measurementService = measurementService;
        this.childrenService = childrenService;
        this.measurementDtoConverter = measurementDtoConverter;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN')")
    @GetMapping("/myKid")
    public List<MeasurementDto> getWeight(@RequestParam Long kidId, HttpServletResponse response) {

        List<MeasurementDto> measurementDtoList = new ArrayList<>();
        try {
            Children kid = childrenService.getKidById(kidId).orElseThrow(NullPointerException::new);
            measurementDtoList = measurementDtoConverter.convertToDto(measurementService.getAll(kid));
            response.setStatus(200);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
            response.setHeader("ERROR", "Brak żądanych informacji w bazie danych");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", "Error");
        }

        return measurementDtoList;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN')")
    @PostMapping("/add")
    public boolean add(@RequestBody MeasurementDto measurementDto, HttpServletResponse response) {

        boolean result = false;

        try {
            Measurement measurement = measurementDtoConverter.convertFromDto(measurementDto);
            measurementService.save(measurement);
            response.setStatus(200);
            result = true;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
            response.setHeader("ERROR", "Brak danych dziecka w bazie danych");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN')")
    @PostMapping("/edit")
    public boolean edit(@RequestBody MeasurementDto measurementDto, HttpServletResponse response) {

        boolean result = false;

        try {
            Measurement measurement = measurementDtoConverter.convertFromDto(measurementDto);
            measurementService.save(measurement);
            response.setStatus(200);
            result = true;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
            response.setHeader("ERROR", "Brak danych dziecka w bazie danych");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }

        return result;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN')")
    @DeleteMapping("/delete")
    public boolean delete(@RequestParam Long id, HttpServletResponse response) {

        boolean result = false;

        try {
            Measurement measurement = measurementService.getById(id).orElseThrow(NegativeArraySizeException::new);
            measurementService.delete(measurement);
            response.setStatus(200);
            result = true;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
            response.setHeader("ERROR", "Brak danych w bazie danych");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.setStatus(400);
            response.setHeader("ERROR", ex.getMessage());
        }

        return result;
    }

}
