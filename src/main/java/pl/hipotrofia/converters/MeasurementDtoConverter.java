package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.MeasurementDto;
import pl.hipotrofia.entities.Measurement;
import pl.hipotrofia.services.ChildrenService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasurementDtoConverter {

    private final ChildrenService childrenService;

    public MeasurementDtoConverter(ChildrenService childrenService) {
        this.childrenService=childrenService;
    }

    public MeasurementDto convertToDto(Measurement measurement) {

        MeasurementDto measurementDto = new MeasurementDto();
        measurementDto.setId(measurement.getId());
        measurementDto.setChildId(measurement.getChild().getId());
        measurementDto.setDateOfMeasure(measurement.getDateOfMeasure());
        measurementDto.setWeight(measurement.getWeight());
        measurementDto.setHeight(measurement.getHeight());
        measurementDto.setHeadCircuit(measurement.getHeadCircuit());

        return measurementDto;
    }

    public Measurement convertFromDto(MeasurementDto measurementDto) throws NullPointerException {

        Measurement measurement = new Measurement();
        measurement.setId(measurementDto.getId());
        measurement.setChild(childrenService.getKidById(measurementDto.getChildId()).orElseThrow(NullPointerException::new));
        measurement.setDateOfMeasure(measurementDto.getDateOfMeasure());
        measurement.setWeight(measurementDto.getWeight());
        measurement.setHeadCircuit(measurementDto.getHeadCircuit());
        measurement.setHeight(measurementDto.getHeight());

        return measurement;
    }

    public List<MeasurementDto> convertToDto(List<Measurement> measurements) {
        return measurements.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
