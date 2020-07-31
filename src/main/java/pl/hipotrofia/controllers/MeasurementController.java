package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.Measurement;
import pl.hipotrofia.services.ChildrenService;
import pl.hipotrofia.services.MeasurementService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/measurement")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final ChildrenService childrenService;

    public MeasurementController(MeasurementService measurementService,
                                 ChildrenService childrenService) {
        this.measurementService=measurementService;
        this.childrenService=childrenService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PUBLISHER', 'ROLE_ADMIN')")
    @GetMapping("/myKidWeight")
    public Map<Date, Integer> getWeight(@RequestParam Long kidId) {

        Map<Date, Integer> result = new HashMap<>();
        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        long millis = System.currentTimeMillis();

        try {
            Children kid = childrenService.getKid(userName, kidId);
            List<Measurement> measurements = measurementService.getAll(kid, new Date(millis));
            result = measurements.stream().collect(Collectors.toMap(Measurement::getDateOfMeasure, Measurement::getWeight));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        return result;
    }

}
