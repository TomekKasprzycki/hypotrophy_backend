package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.repositories.PicturesRepository;

import java.io.File;

@Service
public class PicturesService {

    private final PicturesRepository picturesRepository;

    public PicturesService(PicturesRepository picturesRepository) {
        this.picturesRepository=picturesRepository;
    }

    public String savePicture(File file) {

        String path = "./Files";

        return path;
    }

}
