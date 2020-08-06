package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.DoctorDto;
import pl.hipotrofia.entities.DocEvaluation;
import pl.hipotrofia.entities.Doctor;
import pl.hipotrofia.entities.Specialization;
import pl.hipotrofia.services.SpecializationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorDtoConverter {

    private final SpecializationService specializationService;

    public DoctorDtoConverter(SpecializationService specializationService) {
        this.specializationService=specializationService;
    }

    public DoctorDto convertToDto(Doctor doctor) {

        DoctorDto doctorDto = new DoctorDto();
        List<Specialization> specializations = specializationService.getAllByDoctorId(doctor.getId());

        doctorDto.setId(doctor.getId());
        doctorDto.setFirstName(doctor.getFirstName());
        doctorDto.setLastName(doctor.getLastName());
        doctorDto.setRegion(doctor.getRegion());
        doctorDto.setCity(doctor.getCity());
        doctorDto.setStreet(doctor.getStreet());
        doctorDto.setBuilding_number(doctor.getBuilding_number());
        doctorDto.setZip_code(doctor.getZip_code());
        doctorDto.setSpecialization(specializations.stream()
                .collect(Collectors.toMap(Specialization::getId, Specialization::getNameOfSpecialization)));
        doctorDto.setAvgEvaluation(doctor.getEvaluation().stream().collect(Collectors.averagingDouble(DocEvaluation::getEvaluation)));

        return doctorDto;
    }

    public Doctor convertFromDto(DoctorDto doctorDto) {

        Doctor doctor = new Doctor();

        doctor.setId(doctorDto.getId());
        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setRegion(doctorDto.getRegion());
        doctor.setCity(doctorDto.getCity());
        doctor.setStreet(doctorDto.getStreet());
        doctor.setBuilding_number(doctorDto.getBuilding_number());
        doctor.setZip_code(doctorDto.getZip_code());
        doctor.setSpecialization(doctorDto.getSpecialization().keySet().stream()
                .map(specializationService::getById).collect(Collectors.toList()));

        return doctor;
    }

    public List<DoctorDto> convertToDto(List<Doctor> doctors) {
        return doctors.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
