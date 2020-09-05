package pl.hipotrofia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.hipotrofia.services.PicturesService;

@RestController
@RequestMapping("/api/pictures")
public class PicturesController {

    private final PicturesService picturesService;

    public PicturesController(PicturesService picturesService) {
        this.picturesService=picturesService;
    }

     

}
