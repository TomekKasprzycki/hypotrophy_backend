package pl.hipotrofia.converters;

import org.springframework.stereotype.Service;
import pl.hipotrofia.dto.PictureDto;
import pl.hipotrofia.entities.Pictures;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PictureDtoConverter {

    public PictureDto convertToDto(Pictures picture) {

        PictureDto pictureDto = new PictureDto();
        pictureDto.setId(picture.getId());
        pictureDto.setPath(picture.getPath());
        pictureDto.setFileName(picture.getFileName());

        return pictureDto;
    }

    public List<PictureDto> convertToDto(List<Pictures> pictures) {

        return pictures.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
