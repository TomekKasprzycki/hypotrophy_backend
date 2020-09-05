package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Doctor;
import pl.hipotrofia.repositories.DoctorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository){
        this.doctorRepository=doctorRepository;
    }

    public List<Doctor> getAll(int limit, int offset) {
        return doctorRepository.findAlmostAll(limit, offset);
    }

    public void add(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    public Optional<Doctor> getById(Long id) {
        return doctorRepository.findById(id);
    }

    public void remove(Doctor doctor) {
        doctorRepository.delete(doctor);
    }
}
