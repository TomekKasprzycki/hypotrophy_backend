package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Pictures;
import pl.hipotrofia.repositories.PicturesRepository;

import java.util.List;

@Service
public class PicturesService {

    private final PicturesRepository picturesRepository;

    public PicturesService(PicturesRepository picturesRepository) {
        this.picturesRepository=picturesRepository;
    }

    public Pictures savePicture(Pictures picture) {
        picturesRepository.save(picture);
        return picture;
    }

    public List<Pictures> getAllByArticleId(Long id, int typeOfPhoto) {
        return picturesRepository.findAllByArticleId(id, typeOfPhoto);
    }

    public void removeByArticleAndPosition(Long articleId, int position){
        picturesRepository.deleteByArticleAndPosition(articleId, position);
    }

    public List<Pictures> getAllByArticleAndPosition(Long articleId, int position) {
        return picturesRepository.findAllByArticleAndPosition(articleId,position);
    }

    public void remove(Pictures picture) {
        picturesRepository.delete(picture);
    }

    public void removePictureById(Long id) {
        picturesRepository.deleteById(id);
    }
}
