package pl.hipotrofia.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pl.hipotrofia.converters.PictureDtoConverter;
import pl.hipotrofia.dto.PictureDto;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.entities.Pictures;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.services.ArticlesService;
import pl.hipotrofia.services.PicturesService;
import pl.hipotrofia.services.UserService;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pictures")
public class PicturesController {

    private final PicturesService picturesService;
    private final ArticlesService articlesService;
    private final PictureDtoConverter pictureDtoConverter;
    private final UserService userService;

    public PicturesController(PicturesService picturesService,
                              ArticlesService articlesService,
                              PictureDtoConverter pictureDtoConverter,
                              UserService userService) {
        this.articlesService = articlesService;
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
                               HttpServletRequest request) throws IOException, ServletException, MessagingException {

        final String userName = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userService.findUserByEmail(userName);
        Long userId = user.getId();

        Part filePart = request.getPart("myFile");

        InputStream fileInputStream = filePart.getInputStream();
        String fileName = filePart.getSubmittedFileName();

        switch (typeOfPhoto) {
            case 1:
                fileName = "fullSize_id_" + userId + "_" + fileName;
                break;
            case 2:
                fileName = "mobile_id_" + userId + "_" + fileName;
                break;
            case 3:
                fileName = "miniature_id_" + userId + "_" + fileName;
                break;
        }
        
        File fileToSave = new File("D:\\Hipotrofia\\pictures\\src\\files\\" + fileName);
        String path = fileToSave.getPath();

        Long id = null;

        try {
            Articles article = articlesService.findArticleById(articleId);

            Pictures picture = new Pictures();
            picture.setFileName(fileName);
            picture.setPath(path);
            picture.setArticle(article);
            picture.setPosition(position);
            picture.setTypeOfPhoto(typeOfPhoto);

            picture = picturesService.savePicture(picture);
            id = picture.getId();

            Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            fileToSave.delete();
            if (id != null) {
                picturesService.removePictureById(id);
            }
            response.setHeader("ERROR", ex.getMessage());
        }
    }

    @GetMapping("/anonymous/download")
    public List<PictureDto> download(@RequestParam Long id,
                                     @RequestParam int typeOfPhoto) {

        return pictureDtoConverter.convertToDto(picturesService.getAllByArticleId(id, typeOfPhoto));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'RLOLE_PUBISHER')")
    @DeleteMapping("/delete/{articleId}/{position}")
    public void delete(@PathVariable Long articleId,
                       @PathVariable int position) {

        try {
            List<Pictures> picturesToRemove = picturesService.getAllByArticleAndPosition(articleId, position);
            List<Path> pathList = picturesToRemove.stream().map(picture -> Paths.get(picture.getPath())).collect(Collectors.toList());
            pathList.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            picturesToRemove.forEach(picturesService::remove);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
