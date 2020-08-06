package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.SpecializationDto;
import pl.hipotrofia.entities.Specialization;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecializationDtoConverter {

    public SpecializationDto convertToDto(Specialization specialization) {

        SpecializationDto specializationDto = new SpecializationDto();
        specializationDto.setId(specialization.getId());
        specializationDto.setNameOfSpecialization(specialization.getNameOfSpecialization());

        return specializationDto;
    }

    public Specialization convertFromDto(SpecializationDto specializationDto) {

        Specialization specialization = new Specialization();
        specialization.setId(specializationDto.getId());
        specialization.setNameOfSpecialization(specializationDto.getNameOfSpecialization());

        return specialization;
    }

    public List<SpecializationDto> convertToDto(List<Specialization> specializations) {
        return specializations.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<Specialization> convertFromDto(List<SpecializationDto> specializationDtoList) {
        return specializationDtoList.stream().map(this::convertFromDto).collect(Collectors.toList());
    }

}
