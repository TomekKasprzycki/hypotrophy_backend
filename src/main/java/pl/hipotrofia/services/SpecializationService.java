package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Doctor;
import pl.hipotrofia.entities.Specialization;
import pl.hipotrofia.repositories.SpecializationRepository;

import java.util.List;

@Service
public class SpecializationService {

    private final SpecializationRepository specializationRepository;

    public SpecializationService(SpecializationRepository specializationRepository) {
        this.specializationRepository=specializationRepository;
    }

    public List<Specialization> getAllByDoctor(Doctor doctor) {
        return specializationRepository.findAllByDoctor(doctor);
    }

    public Specialization getById(Long id) {
        return specializationRepository.findSpecializationById(id);
    }

    public List<Specialization> getAll() {
        return specializationRepository.findAll();
    }

    public List<Specialization> getAllByDoctorId(Long id) {
        return specializationRepository.findAllByDoctorId(id);
    }

    public Specialization save(Specialization specialization) {
        return specializationRepository.save(specialization);
    }

    public void remove(Specialization specialization) {
        specializationRepository.delete(specialization);
    }
}
