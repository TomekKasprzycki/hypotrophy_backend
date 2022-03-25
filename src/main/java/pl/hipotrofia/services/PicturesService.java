package pl.hipotrofia.services;

import org.springframework.stereotype.Service;
import pl.hipotrofia.entities.Articles;
import pl.hipotrofia.entities.Pictures;
import pl.hipotrofia.entities.User;
import pl.hipotrofia.myExceptions.UserNotFoundException;
import pl.hipotrofia.repositories.PicturesRepository;

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

import static java.nio.file.Path.*;

@Service
public class PicturesService {

    private final PicturesRepository picturesRepository;
    private final ArticlesService articlesService;
    private final UserService userService;

    public PicturesService(PicturesRepository picturesRepository,
                           ArticlesService articlesService,
                           UserService userService) {
        this.picturesRepository = picturesRepository;
        this.articlesService = articlesService;
        this.userService = userService;
    }

    public Pictures savePicture(Pictures picture) {
        picturesRepository.save(picture);
        return picture;
    }

    public List<Pictures> getAllByArticleId(Long id, int typeOfPhoto) {
        return picturesRepository.findAllByArticleId(id, typeOfPhoto);
    }

    public List<Pictures> getAllByArticleAndPosition(Long articleId, int position) {
        return picturesRepository.findAllByArticleAndPosition(articleId, position);
    }

    public void remove(Pictures picture) {
        picturesRepository.delete(picture);
    }

    public void removePictureById(Long id) {
        picturesRepository.deleteById(id);
    }

    public void addPictures(Long articleId, int position, int typeOfPhoto, HttpServletResponse response, Long userId, Part filePart) throws IOException {
        InputStream fileInputStream = filePart.getInputStream();
        String fileName = filePart.getSubmittedFileName();
        Path dirPath = of(ServiceConstants.PATH_TO_FILE + "\\" + userId);
        Path directories = Files.createDirectories(dirPath);
        String fullPath = directories.toString() + "\\";

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

        File fileToSave = new File(fullPath + fileName);
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
            picture = savePicture(picture);

            id = picture.getId();

            Files.copy(fileInputStream, fileToSave.toPath(), StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception ex) {

            fileToSave.delete();

            if (id != null) {
                removePictureById(id);
            }

            response.setHeader("ERROR", ex.getMessage());
        }
    }

    public boolean removePictures(Long articleId, int position, String userName) throws UserNotFoundException {

        User user = userService.findUserByEmail(userName).orElseThrow(() -> new UserNotFoundException("User not found"));
        Articles article = articlesService.findArticleById(articleId);

        if (article.getAuthor() == user || user.getRole().getId() == 1 || user.getRole().getId() == 2) {

            try {
                List<Pictures> picturesToRemove = getAllByArticleAndPosition(articleId, position);
                List<Path> pathList = picturesToRemove.stream().map(picture -> Paths.get(picture.getPath())).collect(Collectors.toList());
                pathList.forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                picturesToRemove.forEach(this::remove);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
