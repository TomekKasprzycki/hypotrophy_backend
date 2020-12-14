package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.PictureDtoConverter;
import pl.hipotrofia.dto.PictureDto;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.PicturesService;
import pl.hipotrofia.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/pictures")
public class PicturesController {

    private final PicturesService picturesService;
    private final PictureDtoConverter pictureDtoConverter;
    private final UserService userService;

    public PicturesController(PicturesService picturesService,
                              PictureDtoConverter pictureDtoConverter,
                              UserService userService) {
        this.picturesService = picturesService;
        this.pictureDtoConverter = pictureDtoConverter;
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'RLOLE_PUBISHER')")
    @PostMapping("/upload/{articleId}/{position}/{typeOfPhoto}")
    public void uploadPictures(@PathVariable Long articleId,
                               @PathVariable int position,
                               @PathVariable int typeOfPhoto,
                               HttpServletResponse response,
                               HttpServletRequest request) throws IOException, ServletException {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findUserByEmail(userName);
        Long userId = user.getId();

        Part filePart = request.getPart("myFile");

        picturesService.addPictures(articleId, position, typeOfPhoto, response, userId, filePart);
    }

    @GetMapping("/anonymous/download")
    public List<PictureDto> download(@RequestParam Long id,
                                     @RequestParam int typeOfPhoto,
                                     HttpServletResponse response) {

        List<PictureDto> pictureDtoList = new ArrayList<>();

        try {
            pictureDtoList = pictureDtoConverter.convertToDto(picturesService.getAllByArticleId(id, typeOfPhoto));
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            response.setStatus(404);
        }

        return pictureDtoList;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'RLOLE_PUBISHER')")
    @DeleteMapping("/delete/{articleId}/{position}")
    public void delete(@PathVariable Long articleId,
                       @PathVariable int position,
                       HttpServletResponse response) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        if(!picturesService.removePictures(articleId, position, userName)){
          response.setStatus(404);
        }

    }



}
