package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Doctor;
import pl.hipotrofia.repositories.DoctorRepository;

import java.util.List;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository){
        this.doctorRepository=doctorRepository;
    }

    public List<Doctor> getAll(int limit, int offset) {
        List<Doctor> doctors = doctorRepository.findAlmostAll(limit, offset);
        return doctors;
    }

    public void add(Doctor doctor) {
        doctorRepository.save(doctor);
    }
}
