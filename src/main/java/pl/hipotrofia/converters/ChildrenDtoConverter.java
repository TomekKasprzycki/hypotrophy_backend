package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.ChildrenDto;
import pl.hipotrofia.entities.Children;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChildrenDtoConverter {

    public ChildrenDto convertToDto(Children child) {

        ChildrenDto childDto = new ChildrenDto();
        childDto.setId(child.getId());
        childDto.setName(child.getName());
        childDto.setGender(child.getGender());
        childDto.setParent(child.getUser().getId());

        return childDto;
    }

    public Children convertFromDto(ChildrenDto childDto) {

        Children child = new Children();
        child.setId(childDto.getId());
        child.setName(childDto.getName());
        child.setGender(childDto.getGender());
        child.setDateOfBirth(childDto.getDateOfBirth());

        return child;
    }

    public List<ChildrenDto> convertToDto(List<Children> children) {
        return children.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
