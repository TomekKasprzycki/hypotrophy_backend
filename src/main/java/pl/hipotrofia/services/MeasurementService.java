package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Children;
import pl.hipotrofia.entities.Measurement;
import pl.hipotrofia.repositories.MeasurementRepository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Measurement> getById(Long id) {
        return measurementRepository.findById(id);
    }

    public void delete(Measurement measurement) {
        measurementRepository.delete(measurement);
    }
}
