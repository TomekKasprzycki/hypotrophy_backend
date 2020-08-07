package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.Measurement;
import pl.hipotrofia.repositories.MeasurementRepository;

import java.util.List;

@Service
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository=measurementRepository;
    }

    public List<Measurement> getAll(Children kid) {
        return measurementRepository.findAllMeasurementByKid(kid);
    }

    public void save(Measurement measurement) {
        measurementRepository.save(measurement);
    }
}
